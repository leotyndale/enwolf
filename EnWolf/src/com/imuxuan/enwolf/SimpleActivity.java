package com.imuxuan.enwolf;

import java.util.List;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.format.Formatter;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;
import com.imuxuan.enwolf.domain.AppInfo;
import com.imuxuan.enwolf.R;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class SimpleActivity extends Activity {
	private List<ApplicationInfo> mAppList;
	private static final String TAG = "SimpleActivity";
	private PopupWindow popupWindow;
	private AppAdapter mAdapter;
	private SwipeMenuListView mListView;
	private LinearLayout ll_loading;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;
	private TextView tv_avail_rom;
	private TextView tv_avail_sd;
	private TextView tv_status;
	private ListView lv_app_manager;
	private AppInfo appInfo;
	private AdBanner adBanner;
	private View adBannerView;
	private static final String TAG_BANNER = "faaf16e322dd4c251d2f09a65a820a3f";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		showBannerAd();
		mAppList = getPackageManager().getInstalledApplications(0);
		mListView = (SwipeMenuListView) findViewById(R.id.listView);
		mAdapter = new AppAdapter();
		mListView.setAdapter(mAdapter);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_avail_rom = (TextView) findViewById(R.id.tv_avail_rom);
		tv_avail_sd = (TextView) findViewById(R.id.tv_avail_sd);
		long sdsize = getAvailSpace(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		long romsize = getAvailSpace(Environment.getDataDirectory()
				.getAbsolutePath());
		tv_avail_sd
				.setText("SD卡可用空间：" + Formatter.formatFileSize(this, sdsize));
		tv_avail_rom.setText("内存可用空间："
				+ Formatter.formatFileSize(this, romsize));

		lv_app_manager = (ListView) findViewById(R.id.lv_app_manager);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				SwipeMenuItem openItem = new SwipeMenuItem(
						getApplicationContext());
				openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				openItem.setWidth(dp2px(90));
				openItem.setTitle("Open");
				openItem.setTitleSize(18);
				openItem.setTitleColor(Color.WHITE);
				menu.addMenuItem(openItem);
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				deleteItem.setWidth(dp2px(90));
				deleteItem.setIcon(R.drawable.ic_delete);
				menu.addMenuItem(deleteItem);
			}
		};
		mListView.setMenuCreator(creator);
		mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				ApplicationInfo item = mAppList.get(position);
				switch (index) {
				case 0:
					open(item);
					break;
				case 1:
					SystemClock.sleep(1500);
					DisplayToast("恭喜您，应用已经卸载！");
					mAppList.remove(position);
					mAdapter.notifyDataSetChanged();
					break;
				}
				return false;
			}
		});
		mListView.setOnSwipeListener(new OnSwipeListener() {
			@Override
			public void onSwipeStart(int position) {
			}

			@Override
			public void onSwipeEnd(int position) {
			}
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(getApplicationContext(),
						position + " long click", 0).show();
				return false;
			}
		});
	}

	protected void DisplayToast(String str) {
		Toast toast = Toast.makeText(this, str, 0);
		toast.show();
	}

	private void open(ApplicationInfo item) {
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(item.packageName);
		List<ResolveInfo> resolveInfoList = getPackageManager()
				.queryIntentActivities(resolveIntent, 0);
		if (resolveInfoList != null && resolveInfoList.size() > 0) {
			ResolveInfo resolveInfo = resolveInfoList.get(0);
			String activityPackageName = resolveInfo.activityInfo.packageName;
			String className = resolveInfo.activityInfo.name;
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			ComponentName componentName = new ComponentName(
					activityPackageName, className);
			intent.setComponent(componentName);
			startActivity(intent);
		}
	}

	class AppAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mAppList.size();
		}

		@Override
		public ApplicationInfo getItem(int position) {
			return mAppList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_list_app, null);
				new ViewHolder(convertView);
			}
			ViewHolder holder = (ViewHolder) convertView.getTag();
			ApplicationInfo item = getItem(position);
			holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
			holder.tv_name.setText(item.loadLabel(getPackageManager()));
			return convertView;
		}

		class ViewHolder {
			ImageView iv_icon;
			TextView tv_name;

			public ViewHolder(View view) {
				iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				tv_name = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(this);
			}
		}
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private long getAvailSpace(String path) {
		StatFs statf = new StatFs(path);
		statf.getBlockCount();
		long size = statf.getBlockSize();
		long count = statf.getAvailableBlocks();
		return size * count;
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
