package com.jordanqin.securitycode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.jordanqin.securitycode.utils.ClipboardUtils;
import com.jordanqin.securitycode.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        for (Object p : pdus) {
            byte[] sms = (byte[]) p;
            SmsMessage message = SmsMessage.createFromPdu(sms);
            //获取短信内容
            final String content = message.getMessageBody();

            final String sender = message.getOriginatingAddress();

            if (!StringUtils.isPersonalMoblieNO(sender)) {
                boolean isCpatchasMessage = false;
                if (StringUtils.isCaptchasMessage(content) && !StringUtils.tryToGetCaptchas(content).equals("")) {
                    isCpatchasMessage = true;
                }
                if (isCpatchasMessage) {
                    //this.abortBroadcast();
                    String captchas = StringUtils.tryToGetCaptchas(content);
                    if (captchas != null) {
                        ClipboardUtils.putTextIntoClipboard(context, captchas);
                        // 弹两遍，加长时间。
                        String str=String.format(context.getResources().getString(R.string.tip), captchas);
                        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                        Toast.makeText(context, str, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

}
