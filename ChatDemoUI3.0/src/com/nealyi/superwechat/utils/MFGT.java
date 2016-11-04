package com.nealyi.superwechat.utils;

import android.app.Activity;
import android.content.Intent;

import com.nealyi.superwechat.R;
import com.nealyi.superwechat.ui.MainActivity;
import com.nealyi.superwechat.ui.SettingsActivity;
import com.nealyi.superwechat.ui.UserProfileActivity;


public class MFGT {
    public static void finish(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
    public static void gotoMainActivity(Activity context){
        startActivity(context, MainActivity.class);
    }
    public static void startActivity(Activity context,Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(context,cls);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
    }

    public static void gotoSettingActivity(Activity context){
        startActivity(context, SettingsActivity.class);
    }

    public static void gotoUserProfile(Activity context){
        startActivity(context, UserProfileActivity.class);
    }
}
