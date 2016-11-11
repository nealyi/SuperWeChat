package com.nealyi.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import com.hyphenate.easeui.domain.User;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.ui.AddContactActivity;
import com.nealyi.superwechat.ui.AddFriendActivity;
import com.nealyi.superwechat.ui.ChatActivity;
import com.nealyi.superwechat.ui.FriendProfileActivity;
import com.nealyi.superwechat.ui.MainActivity;
import com.nealyi.superwechat.ui.SettingsActivity;
import com.nealyi.superwechat.ui.UserProfileActivity;


public class MFGT {
    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }

    public static void gotoMainActivity(Activity context) {
        startActivity(context, MainActivity.class);
    }

    public static void startActivity(Activity context, Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void startActivity(Activity context, Intent intent) {
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    public static void gotoSettingActivity(Activity context) {
        startActivity(context, SettingsActivity.class);
    }

    public static void gotoUserProfile(Activity context) {
        startActivity(context, UserProfileActivity.class);
    }

    public static void gotoAddFriend(Activity context) {
        startActivity(context, AddContactActivity.class);
    }

    public static void gotoFriendProfile(Activity context, String username) {
        Intent intent = new Intent();
        intent.setClass(context, FriendProfileActivity.class);
        intent.putExtra(I.User.USER_NAME, username);
        startActivity(context, intent);
    }

    public static void gotoAddFriendMsg(Activity context, User user) {
        Intent intent = new Intent();
        intent.setClass(context, AddFriendActivity.class);
        intent.putExtra(I.User.USER_NAME, user);
        startActivity(context, intent);
    }

    public static void gotoChat(Activity context, String username) {
        Intent intent = new Intent();
        intent.setClass(context, ChatActivity.class);
        intent.putExtra("userId", username);
        startActivity(context, intent);
    }
}
