package com.nealyi.superwechat.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.easeui.domain.User;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
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
    }

    private void setUserInfo() {
        EaseUserUtils.setAppUserAvatar(this, user.getMUserName(), mIvAvatar);
        mTvNick.setText(user.getMUserNick());
        mTvUsername.setText(user.getMUserName());
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        MFGT.finish(this);
    }
}
