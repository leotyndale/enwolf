package com.imuxuan.enwolf;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imuxuan.enwolf.db.dao.AntivirsuDao;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class AntiVirusActivity extends Activity {
	protected static final int SCANING = 0;
	protected static final int FINISH = 2;
	private ImageView iv_scan;
	private ProgressBar progressBar1;
	private PackageManager pm;
	private TextView tv_scan_status;
	private LinearLayout ll_container;
	private AdBanner adBanner;
	private View adBannerView;
	private static final String TAG_BANNER = "faaf16e322dd4c251d2f09a65a820a3f";
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCANING:
				ScanInfo scanInfo = (ScanInfo) msg.obj;
				tv_scan_status.setText("正在扫描：" + scanInfo.name);
				TextView tv = new TextView(getApplicationContext());
				if (scanInfo.isvirus) {
					tv.setTextColor(Color.RED);
					tv.setText("发现病毒：" + scanInfo.name);
				} else {
					tv.setTextColor(Color.BLACK);
					tv.setText("扫描安全：" + scanInfo.name);
				}
				ll_container.addView(tv, 0);
				break;
			case FINISH:
				tv_scan_status.setText("扫描完毕");
				iv_scan.clearAnimation();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_anti_virus);
		tv_scan_status = (TextView) findViewById(R.id.tv_scan_status);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		iv_scan = (ImageView) findViewById(R.id.iv_scan);
		RotateAnimation ra = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		ra.setDuration(1000);
		ra.setRepeatCount(Animation.INFINITE);
		iv_scan.startAnimation(ra);
		progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
		scanVirus();
		showBannerAd();
	}

	private void scanVirus() {
		pm = getPackageManager();
		tv_scan_status.setText("正在为您初始化杀毒引擎");
		new Thread() {
			public void run() {
				List<PackageInfo> infos = pm.getInstalledPackages(0);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				progressBar1.setMax(infos.size());
				int progress = 0;
				for (PackageInfo info : infos) {
					String sourcedir = info.applicationInfo.sourceDir;
					String md5 = getFileMd5(sourcedir);
					ScanInfo scaninfo = new ScanInfo();
					scaninfo.name = info.applicationInfo.loadLabel(pm)
							.toString();
					scaninfo.packname = info.packageName;
					System.out.println(scaninfo.packname + ":" + md5);
					if (AntivirsuDao.isVirus(md5)) {
						scaninfo.isvirus = true;
					} else {
						scaninfo.isvirus = false;
					}
					Message msg = Message.obtain();
					msg.obj = scaninfo;
					msg.what = SCANING;
					handler.sendMessage(msg);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					progress++;
					progressBar1.setProgress(progress);
				}
				Message msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			};
		}.start();
	}

	class ScanInfo {
		String packname;
		String name;
		boolean isvirus;
	}

	private String getFileMd5(String path) {
		try {
			File file = new File(path);
			MessageDigest digest = MessageDigest.getInstance("md5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = fis.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuffer sb = new StringBuffer();
			for (byte b : result) {
				int number = b & 0xff;
				String str = Integer.toHexString(number);
				if (str.length() == 1) {
					sb.append("0");
				}
				sb.append(str);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// 豌豆荚banner广告
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
