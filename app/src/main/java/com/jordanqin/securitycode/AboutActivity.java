package com.jordanqin.securitycode;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jordanqin.securitycode.utils.ShareUtils;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.drakeet.library.UIButton;

/**
 * Created by qjd on 2015/8/24.
 * desc:
 */

public class AboutActivity extends AppCompatActivity {
    @InjectView(R.id.tv_version)
    TextView mVersionTextView;

    @InjectView(R.id.rl_author)
    RelativeLayout rlAuthor;

    @InjectView(R.id.btn_circle)
    UIButton btnCircle;

    @InjectView(R.id.btn_wechat)
    UIButton btnWechat;

    @InjectView(R.id.btn_share)
    UIButton btnShare;

    private IWXAPI wxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.inject(this);

        //友盟统计
        //MobclickAgent.setDebugMode(true);
        MobclickAgent.updateOnlineConfig(this);

        //友盟更新
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);

        mVersionTextView.setText("Version " + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")");

        wxApi= ShareUtils.getWXAPI(this);

        rlAuthor.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            Uri uri = Uri.parse("mailto:" + getString(R.string.author_email));
                            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                            startActivity(intent);
                        } catch (Exception ex) {
                        }
                    }
                }
        );

        btnCircle.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.share_icon);
                        String result = ShareUtils.SendWX(wxApi, SendMessageToWX.Req.WXSceneTimeline, getString(R.string.share_url), "ShareApp", getString(R.string.app_name), getString(R.string.share_text),thumb);
                        if (result != null) {
                            Toast.makeText(view.getContext(), result, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        btnWechat.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.share_icon);
                        String result = ShareUtils.SendWX(wxApi, SendMessageToWX.Req.WXSceneSession, getString(R.string.share_url), "ShareApp", getString(R.string.app_name), getString(R.string.share_text), thumb);
                        if (result != null) {
                            Toast.makeText(view.getContext(), result, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        btnShare.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
                        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(Intent.createChooser(intent, getTitle()));
                        }
                }
        );
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
