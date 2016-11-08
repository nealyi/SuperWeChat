package com.hyphenate.easeui.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.controller.EaseUI;
import com.hyphenate.easeui.controller.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.domain.User;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getUser(username);

        return null;
    }

    /**
     * get User according username
     *
     * @param username
     * @return
     */
    public static User getAppUserInfo(String username) {
        if (userProvider != null)
            return userProvider.getAppUser(username);

        return null;
    }

    /**
     * @return User
     */

    public static User getCurrentAppUserInfo() {
        String username = EMClient.getInstance().getCurrentUser();
        if (userProvider != null)
            return userProvider.getAppUser(username);

        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        Log.i("main", "setUserAvatar,user=" + user);
        Log.i("main", "setUserAvatar,userAvatar=" + user.getAvatar());
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Log.i("main", "setUserAvatar,avatarResId=" + avatarResId);
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setAppUserAvatar(Context context, String username, ImageView imageView) {
        User user = getAppUserInfo(username);
        Log.i("main", "setAppUserAvatar,user=" + user);
        Log.i("main", "setAppUserAvatar,userAvatar=" + user.getAvatar());
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }


    /**
     * set user avatar
     *
     * @param path
     */
    public static void setAppUserAvatarPath(Context context, String path, ImageView imageView) {
        if (path != null) {
            try {
                int avatarResId = Integer.parseInt(path);
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user's nickname
     */
    public static void setAppUserNick(String username, TextView textView) {
        if (textView != null) {
            User user = getAppUserInfo(username);
            if (user != null && user.getMUserNick() != null) {
                textView.setText(user.getMUserNick());
            } else {
                textView.setText(username);
            }
        }
    }

    public static void setCurrentUserAvatar(FragmentActivity activity, ImageView ivAvatar) {
        String username = EMClient.getInstance().getCurrentUser();
        setAppUserAvatar(activity, username, ivAvatar);
    }

    public static void setCurrentUserNick(TextView tvNick) {
        String username = EMClient.getInstance().getCurrentUser();
        setAppUserNick(username, tvNick);
    }

    public static void setCurrentUserNameWithNo(TextView tvUsername) {
        String username = EMClient.getInstance().getCurrentUser();
        tvUsername.setText(username);
    }
}
