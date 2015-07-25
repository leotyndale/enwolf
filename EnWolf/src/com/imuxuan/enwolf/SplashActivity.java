package com.imuxuan.enwolf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import com.wandoujia.ads.sdk.Ads;

public class SplashActivity extends Activity {
	protected static final String TAG = "SplashActivity";
	private static final String ADS_APP_ID = "100015339";
	private static final String ADS_SECRET_KEY = "67afdff4427a484800d898534486ecb4";
	private static final String ID = "faaf16e322dd4c251d2f09a65a820a3f";

	private SharedPreferences sp;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		copyDB("antivirus.db");
		// Íã¶¹¼Ô¹ã¸æ
		try {
			Ads.init(this, ADS_APP_ID, ADS_SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				preferences = getSharedPreferences("count", MODE_WORLD_READABLE);
				int count = preferences.getInt("count", 0);
				if (count == 0) {
					Intent intent = new Intent();
					intent.setClass(getApplicationContext(),
							WhatsNewActivity.class);
					startActivity(intent);
					finish();
				} else {
					enterHome();
				}
				Editor editor = preferences.edit();
				editor.putInt("count", ++count);
				editor.commit();
			}
		}, 2000);
		AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
		aa.setDuration(2000);
		findViewById(R.id.rl_root_splash).setAnimation(aa);
		findViewById(R.id.rl_root_splash).startAnimation(aa);
	}

	private void copyDB(String filename) {
		try {
			File file = new File(getFilesDir(), filename);
			if (file.exists() && file.length() > 0) {
			} else {
				InputStream is = getAssets().open(filename);
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
	};

	protected void enterHome() {
		Intent intent = new Intent(this, HomeActivity.class);
		// Intent intent = new Intent(this, WhatsNewActivity.class);
		startActivity(intent);
		finish();
	}
}