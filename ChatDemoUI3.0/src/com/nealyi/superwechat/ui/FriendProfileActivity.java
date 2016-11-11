package com.nealyi.superwechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.SuperWeChatHelper;
import com.nealyi.superwechat.utils.L;
import com.nealyi.superwechat.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FriendProfileActivity extends BaseActivity {
    private static final String TAG = FriendProfileActivity.class.getSimpleName();

    @BindView(R.id.iv_back)
    ImageView mIvBack;
    @BindView(R.id.tv_common_title)
    TextView mTvCommonTitle;
    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_username)
    TextView mTvUsername;

    User user;
    @BindView(R.id.btn_send_message)
    Button mBtnSendMessage;
    @BindView(R.id.btn_video_chat)
    Button mBtnVideoChat;
    @BindView(R.id.btn_add_friend)
    Button mBtnAddFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        L.e(TAG,"onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        L.e(TAG,"onCreate,user="+user);
        if (user == null) {
            MFGT.finish(this);
            return;
        }
        initView();
    }

    private void initView() {
        L.e(TAG,"initView,user="+user);
        mIvBack.setVisibility(View.VISIBLE);
        mTvCommonTitle.setVisibility(View.VISIBLE);
        mTvCommonTitle.setText(getResources().getString(R.string.detail_information));
        setUserInfo();
        isFriend();
    }

    private void isFriend() {
//        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())) {
        if (SuperWeChatHelper.getInstance().getContactList().containsKey(user.getMUserName())) {
            mBtnSendMessage.setVisibility(View.VISIBLE);
            mBtnVideoChat.setVisibility(View.VISIBLE);
        } else {
            mBtnAddFriend.setVisibility(View.VISIBLE);
        }
    }

    private void setUserInfo() {
        L.e(TAG,"user = "+user);
        L.e(TAG,"username = "+user.getMUserName());
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), mIvAvatar);
        mTvNick.setText(user.getMUserNick());
        mTvUsername.setText(user.getMUserName());
    }

    @OnClick({R.id.iv_back, R.id.btn_send_message, R.id.btn_video_chat, R.id.btn_add_friend})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                MFGT.finish(this);
                break;
            case R.id.btn_send_message:
                MFGT.gotoChat(this, user.getMUserName());
                break;
            case R.id.btn_video_chat:
                if (!EMClient.getInstance().isConnected()) {
                    Toast.makeText(this, R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                } else {
                    startActivity(new Intent(this,VideoCallActivity.class).putExtra("username",user.getMUserName())
                            .putExtra("isComingCall",false));
                }
                break;
            case R.id.btn_add_friend:
                MFGT.gotoAddFriendMsg(FriendProfileActivity.this, user);
                break;
        }
    }
}
