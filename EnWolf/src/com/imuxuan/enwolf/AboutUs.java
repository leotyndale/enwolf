package com.imuxuan.enwolf;

import com.imuxuan.enwolf.R;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class AboutUs extends Activity {
	private AdBanner adBanner;
	private View adBannerView;
	private static final String TAG_BANNER = "faaf16e322dd4c251d2f09a65a820a3f";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
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
