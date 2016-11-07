package com.nealyi.superwechat.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Selection;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.utils.MFGT;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFriendActivity extends BaseActivity {
    private static final String TAG = AddFriendActivity.class.getSimpleName();

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    @BindView(R.id.et_apply_message)
    EditText mEtApplyMessage;

    User user;
    private ProgressDialog progressDialog;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user == null) {
            MFGT.finish(this);
        }
        mIvBack.setVisibility(View.VISIBLE);
        mTvCommonTitle.setVisibility(View.VISIBLE);
        mTvCommonTitle.setText(getResources().getString(R.string.apply_friend));
        mTvSearch.setVisibility(View.VISIBLE);
        mTvSearch.setText(getResources().getString(R.string.send));
        String mUserNick = EaseUserUtils.getCurrentAppUserInfo().getMUserNick();
        String msg = getResources().getString(R.string.add_contact_send_message_prefix) + mUserNick;
        mEtApplyMessage.setText(msg);
        Selection.setSelection(mEtApplyMessage.getText(), 2, msg.length());
        mEtApplyMessage.requestFocus();
//        InputMethodManager imm = (InputMethodManager) mEtApplyMessage.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEtApplyMessage, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//        Timer timer = new Timer(); //设置定时器
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() { //弹出软键盘的代码
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(mEtApplyMessage, InputMethodManager.RESULT_SHOWN);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
//            }
//        }, 300); //设置300毫秒的时长
    }

    @OnClick({R.id.iv_back, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                imm.hideSoftInputFromWindow(mEtApplyMessage.getWindowToken(), 0);
                MFGT.finish(this);
                break;
            case R.id.tv_search:
                sendMsg();
                break;
        }
    }

    private void sendMsg() {
        progressDialog = new ProgressDialog(this);
        String stri = getResources().getString(R.string.addcontact_adding);
        progressDialog.setMessage(stri);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        new Thread(new Runnable() {
            public void run() {

                try {
                    //demo use a hardcode reason here, you need let user to input if you like
//                    String s = getResources().getString(R.string.Add_a_friend);
                    String s = mEtApplyMessage.getText().toString().trim();
                    EMClient.getInstance().contactManager().addContact(user.getMUserName(), s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s1 = getResources().getString(R.string.send_successful);
                            Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            progressDialog.dismiss();
                            String s2 = getResources().getString(R.string.Request_add_buddy_failure);
                            Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                } finally {
                    MFGT.finish(AddFriendActivity.this);
                }
            }
        }).start();
    }
}
