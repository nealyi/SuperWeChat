/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nealyi.superwechat.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.SuperWeChatApplication;
import com.nealyi.superwechat.SuperWeChatHelper;
import com.nealyi.superwechat.bean.Result;
import com.nealyi.superwechat.db.SuperWeChatDBManager;
import com.nealyi.superwechat.db.UserDao;
import com.nealyi.superwechat.utils.L;
import com.nealyi.superwechat.utils.MD5;
import com.nealyi.superwechat.utils.MFGT;
import com.nealyi.superwechat.utils.NetDao;
import com.nealyi.superwechat.utils.OkHttpUtils;
import com.nealyi.superwechat.utils.ResultUtils;
import com.nealyi.superwechat.video.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Login screen
 */
public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";
    public static final int REQUEST_CODE_SETNICK = 1;

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.password)
    EditText mPassword;

    private boolean progressShow;
    private boolean autoLogin = false;

    String currentUsername;
    String currentPassword;
    ProgressDialog pd;
    LoginActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // enter the main activity if already logged in
        if (SuperWeChatHelper.getInstance().isLoggedIn()) {
            autoLogin = true;
            startActivity(new Intent(LoginActivity.this, MainActivity.class));

            return;
        }
        setContentView(R.layout.em_activity_login);
        ButterKnife.bind(this);
        setListener();
        initView();
        mContext = this;
    }

    private void initView() {
        if (SuperWeChatHelper.getInstance().getCurrentUsernName() != null) {
            mUsername.setText(SuperWeChatHelper.getInstance().getCurrentUsernName());
        }
        mIvBack.setVisibility(View.VISIBLE);
        mTvCommonTitle.setVisibility(View.VISIBLE);
        mTvCommonTitle.setText(R.string.login);
    }

    private void setListener() {
        // if user changed, clear the password
        mUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPassword.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * login
     */
    public void login() {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
            return;
        }
        currentUsername = mUsername.getText().toString().trim();
        currentPassword = mPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        progressShow = true;
        pd = new ProgressDialog(LoginActivity.this);
        pd.setCanceledOnTouchOutside(false);
        pd.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d(TAG, "EMClient.getInstance().onCancel");
                progressShow = false;
            }
        });
        pd.setMessage(getString(R.string.Is_landing));
        pd.show();


        // After logout，the DemoDB may still be accessed due to async callback, so the DemoDB will be re-opened again.
        // close it before login to make sure DemoDB not overlap
        SuperWeChatDBManager.getInstance().closeDB();

        // reset current user name before login
        SuperWeChatHelper.getInstance().setCurrentUserName(currentUsername);

        final long start = System.currentTimeMillis();
        // call login method
        Log.d(TAG, "EMClient.getInstance().login");
        EMClient.getInstance().login(currentUsername, MD5.getMessageDigest(currentPassword), new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "login: onSuccess");


                loginAppServer();
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.d(TAG, "login: onProgress");
            }

            @Override
            public void onError(final int code, final String message) {
                Log.d(TAG, "login: onError: " + code);
                if (!progressShow) {
                    return;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void loginAppServer() {
        NetDao.login(mContext, currentUsername, currentPassword, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                L.e(TAG, "s=" + s);
                if (s != null && s != "") {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    L.e(TAG, "result=" + result);
                    if (result != null && result.isRetMsg()) {
                        User user = (User) result.getRetData();
                        L.e(TAG, "user=" + user);
                        if (user != null) {
                            UserDao dao = new UserDao(mContext);
                            dao.saveUser(user);
                            loginSuccess();
                        } else {
                            pd.dismiss();
                            L.e(TAG, "login fail," + result);
                        }
                    } else {
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error=" + error);
            }
        });
    }

    private void loginSuccess() {
        // ** manually load all local groups and conversation
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();

        // update current user's display name for APNs
        boolean updatenick = EMClient.getInstance().updateCurrentUserNick(
                SuperWeChatApplication.currentUserNick.trim());
        if (!updatenick) {
            Log.e("LoginActivity", "update current user nick fail");
        }

        if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
            pd.dismiss();
        }

        // get user's info (this should be get from App's server or 3rd party service)
        SuperWeChatHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

        Intent intent = new Intent(LoginActivity.this,
                MainActivity.class);
        startActivity(intent);

        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (autoLogin) {
            return;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pd != null) {
            pd.dismiss();
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_login})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.btn_login:
                login();
                break;
        }
    }
}
