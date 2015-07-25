package com.imuxuan.enwolf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.imuxuan.enwolf.service.CallSmsSafeService;
import com.imuxuan.enwolf.ui.SettingItemView;
import com.imuxuan.enwolf.utils.ServiceUtils;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class SettingActivity extends Activity {
	private SettingItemView siv_callsms_safe;
	private Intent callSmsSafeIntent;
	private AdBanner adBanner;
	private View adBannerView;
	private static final String TAG_BANNER = "faaf16e322dd4c251d2f09a65a820a3f";


	@Override
	protected void onResume() {
		super.onResume();
		boolean iscallSmsServiceRunning = ServiceUtils.isServiceRunning(
				SettingActivity.this,
				"com.imuxuan.enwolf.service.CallSmsSafeService");
		siv_callsms_safe.setChecked(iscallSmsServiceRunning);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		siv_callsms_safe = (SettingItemView) findViewById(R.id.siv_callsms_safe);
		callSmsSafeIntent = new Intent(this, CallSmsSafeService.class);
		siv_callsms_safe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (siv_callsms_safe.isChecked()) {
					siv_callsms_safe.setChecked(false);
					stopService(callSmsSafeIntent);
				} else {
					siv_callsms_safe.setChecked(true);
					startService(callSmsSafeIntent);
				}
			}
		});
		showBannerAd();
	}
	// Íã¶¹¼Ôbanner¹ã¸æ
		void showBannerAd() {
			ViewGroup containerView = (ViewGroup) findViewById(R.id.banner_ad_container);
			if (adBannerView != null
					&& containerView.indexOfChild(adBannerView) >= 0) {
				containerView.removeView(adBannerView);
			}
			adBanner = Ads.showBannerAd(this,
					(ViewGroup) findViewById(R.id.banner_ad_container), TAG_BANNER);
			adBannerView = adBanner.getView();
		}
}
