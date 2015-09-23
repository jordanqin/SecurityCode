package com.jordanqin.securitycode.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.jordanqin.securitycode.constants.Constant;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;
import com.tencent.mm.sdk.platformtools.Util;

/**
 * Created by fmy_gardiner on 2015/9/1.
 */
public class ShareUtils {
    private static final String TAG = "ShareUtils";

    public static IWXAPI getWXAPI(Context context) {
        IWXAPI api= WXAPIFactory.createWXAPI(context, Constant.WX_APP_ID,true);
        api.registerApp(Constant.WX_APP_ID);
        return api;
    }

    public static String SendWX(IWXAPI api, int scene, String url, String type, String title, String description, Bitmap bitmap){
        int wxSdkVersion = api.getWXAppSupportAPI();
        if (wxSdkVersion == 0)
        {
            return "您没有安装微信，所以不能分享！";
        }
        else if (scene==SendMessageToWX.Req.WXSceneTimeline && wxSdkVersion < Constant.TIMELINE_SUPPORTED_VERSION)
        {
            return "您的微信版本太低，不支持分享到朋友圈！";
        }
        else
        {
            WXWebpageObject webpage = new WXWebpageObject();
            webpage.webpageUrl = url;
            WXMediaMessage msg = new WXMediaMessage(webpage);
            msg.title = title;
            msg.description = description;

            if (bitmap != null)
            {
                msg.thumbData = Util.bmpToByteArray(bitmap, true);
            }

            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction(type);
            req.message = msg;
            req.scene = scene;
            api.sendReq(req);
            return null;
        }
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
