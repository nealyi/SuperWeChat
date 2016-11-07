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
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.bean.Result;
import com.nealyi.superwechat.utils.CommonUtils;
import com.nealyi.superwechat.utils.L;
import com.nealyi.superwechat.utils.MFGT;
import com.nealyi.superwechat.utils.NetDao;
import com.nealyi.superwechat.utils.OkHttpUtils;
import com.nealyi.superwechat.utils.ResultUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddContactActivity extends BaseActivity {
    private static final String TAG = AddContactActivity.class.getSimpleName();
    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.et_username)
    EditText mEtUsername;
    private TextView nameText;
    private String toAddUsername;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_add_contact);
        ButterKnife.bind(this);
        initView();

        nameText = (TextView) findViewById(R.id.name);
    }

    private void initView() {
        mIvBack.setVisibility(View.VISIBLE);
        mTvCommonTitle.setVisibility(View.VISIBLE);
        mTvCommonTitle.setText(R.string.add_friend);
        mTvSearch.setVisibility(View.VISIBLE);
        String strUserName = getResources().getString(R.string.user_name);
        mEtUsername.setHint(strUserName);
    }


    /**
     * search contact
     */
    public void searchContact() {
        final String name = mEtUsername.getText().toString().trim();

        toAddUsername = name;
        if (TextUtils.isEmpty(name)) {
            new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.Is_sending_a_request);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        NetDao.searchUser(this, toAddUsername, new OkHttpUtils.OnCompleteListener<String>() {
            @Override
            public void onSuccess(String s) {
                progressDialog.dismiss();
                if (s != null) {
                    Result result = ResultUtils.getResultFromJson(s, User.class);
                    if (result != null && result.isRetMsg()) {
                        User user = (User) result.getRetData();
                        if (user != null) {
                            MFGT.gotoFriendProfile(AddContactActivity.this, user);
                        } else {
                            CommonUtils.showShortToast(R.string.msg_104);
                        }
                    } else {
                        CommonUtils.showShortToast(R.string.msg_104);
                    }
                } else {
                    CommonUtils.showShortToast(R.string.msg_104);
                }
            }

            @Override
            public void onError(String error) {
                L.e(TAG, "error=" + error);
                CommonUtils.showShortToast(error);
                progressDialog.dismiss();
            }
        });


    }


    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.tv_search:
                searchContact();
                break;
        }
    }
}
