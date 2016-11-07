package com.nealyi.superwechat.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.SuperWeChatHelper;
import com.nealyi.superwechat.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FriendProfileActivity extends BaseActivity {

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        user = (User) getIntent().getSerializableExtra(I.User.USER_NAME);
        if (user == null) {
            MFGT.finish(this);
        }
        initView();
    }

    private void initView() {
        mIvBack.setVisibility(View.VISIBLE);
        mTvCommonTitle.setVisibility(View.VISIBLE);
        mTvCommonTitle.setText(getResources().getString(R.string.detail_information));
        setUserInfo();
        isFriend();
    }

    private void isFriend() {
        if (SuperWeChatHelper.getInstance().getAppContactList().containsKey(user.getMUserName())) {
            mBtnSendMessage.setVisibility(View.VISIBLE);
            mBtnVideoChat.setVisibility(View.VISIBLE);
        } else {
            mBtnAddFriend.setVisibility(View.VISIBLE);
        }
    }

    private void setUserInfo() {
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
                break;
            case R.id.btn_video_chat:
                break;
            case R.id.btn_add_friend:
                MFGT.gotoAddFriendMsg(FriendProfileActivity.this,user);
                break;
        }
    }
}
