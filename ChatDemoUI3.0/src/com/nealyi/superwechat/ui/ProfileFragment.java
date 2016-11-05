package com.nealyi.superwechat.ui;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.redpacketui.utils.RedPacketUtil;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.nealyi.superwechat.Constant;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.utils.L;
import com.nealyi.superwechat.utils.MFGT;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_nick)
    TextView mTvNick;
    @BindView(R.id.tv_username)
    TextView mTvUsername;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState!=null&&savedInstanceState.getBoolean("isConflict",false))
            return;
        setUserInfo();
    }

    private void setUserInfo() {
        EaseUserUtils.setCurrentUserAvatar(getActivity(),mIvAvatar);
        EaseUserUtils.setCurrentUserNick(mTvNick);
        EaseUserUtils.setCurrentUserNameWithNo(mTvUsername);
    }

    @OnClick({R.id.layout_profile_view, R.id.ll_money, R.id.ll_setting})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_profile_view:
                MFGT.gotoUserProfile(getActivity());
                break;
            //red packet code : 进入零钱页面
            case R.id.ll_money:
                RedPacketUtil.startChangeActivity(getActivity());
                break;
            //end of red packet code
            case R.id.ll_setting:
                MFGT.gotoSettingActivity(getActivity());
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(((MainActivity)getActivity()).isConflict){
            outState.putBoolean("isConflict", true);
        }else if(((MainActivity)getActivity()).getCurrentAccountRemoved()){
            outState.putBoolean(Constant.ACCOUNT_REMOVED, true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserInfo();
    }
}
