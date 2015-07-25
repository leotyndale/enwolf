package com.imuxuan.enwolf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class HomeActivity extends Activity {
	protected static final String TAG = "HomeActivity";
	private GridView list_home;
	private MyAdapter adapter;
	private Button ok;
	private Button cancel;
	private RelativeLayout anti;
	private ImageView wolfback;
	private RelativeLayout rela1;
	private SharedPreferences sp;
	private AdBanner adBanner;
	private View adBannerView;
	private static final String TAG_BANNER = "faaf16e322dd4c251d2f09a65a820a3f";
	private static String[] names = { "通讯卫士", "软件管理", "进程管理", "关于我们" };
	private static int[] ids = { R.drawable.txws, R.drawable.rjgl,
			R.drawable.jcgl, R.drawable.gywm };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		list_home = (GridView) findViewById(R.id.list_home);
		anti = (RelativeLayout) findViewById(R.id.anti);
		wolfback = (ImageView) findViewById(R.id.wolfback);
		rela1 = (RelativeLayout) findViewById(R.id.rela1);
		showBannerAd();
		adapter = new MyAdapter();
		list_home.setAdapter(adapter);
		list_home.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent;
				switch (position) {
				case 0:
					intent = new Intent(HomeActivity.this,
							CallSmsSafeActivity.class);
					startActivity(intent);
					break;
				case 1:
					intent = new Intent(HomeActivity.this, SimpleActivity.class);
					startActivity(intent);
					break;
				case 2:
					intent = new Intent(HomeActivity.this,
							TaskManagerActivity.class);
					startActivity(intent);
					break;
				case 3:
					intent = new Intent(HomeActivity.this, AboutUs.class);
					startActivity(intent);
					break;
				}
			}
		});

		anti.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomeActivity.this,
						AntiVirusActivity.class);
				startActivity(intent);
			}
		});
		rela1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent2 = new Intent(HomeActivity.this,
						SettingActivity.class);
				startActivity(intent2);
			}
		});
	}

	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return names.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this,
					R.layout.list_item_home, null);
			ImageView iv_item = (ImageView) view.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) view.findViewById(R.id.tv_item);
			tv_item.setText(names[position]);
			iv_item.setImageResource(ids[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
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