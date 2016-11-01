package com.nealyi.superwechat.utils;

import android.widget.Toast;
import com.nealyi.superwechat.I;
import com.nealyi.superwechat.R;
import com.nealyi.superwechat.SuperWeChatApplication;


public class CommonUtils {
    public static void showLongToast(String msg) {
        Toast.makeText(SuperWeChatApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(String msg) {
        Toast.makeText(SuperWeChatApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(int rId) {
        showLongToast(SuperWeChatApplication.getInstance().getString(rId));
    }

    public static void showShortToast(int rId) {
        showShortToast(SuperWeChatApplication.getInstance().getString(rId));
    }

    public static void showMsgShortToast(int msgId) {
        if (msgId > 0) {
            showShortToast(SuperWeChatApplication.getInstance().getResources()
                    .getIdentifier(I.MSG_PREFIX_MSG + msgId, "string",
                            SuperWeChatApplication.getInstance().getPackageName()));
        } else {
            showShortToast(R.string.msg_1);
        }
    }
}
