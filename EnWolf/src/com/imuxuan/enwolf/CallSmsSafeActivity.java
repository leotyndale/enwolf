package com.imuxuan.enwolf;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.imuxuan.enwolf.db.dao.BlackNumberDao;
import com.imuxuan.enwolf.domain.BlackNumberInfo;
import com.imuxuan.enwolf.R;
import com.wandoujia.ads.sdk.Ads;
import com.wandoujia.ads.sdk.widget.AdBanner;

public class CallSmsSafeActivity extends Activity {
	public static final String TAG = "CallSmsSafeActivity";
	private ListView lv_callsms_safe;
	private List<BlackNumberInfo> infos;
	private BlackNumberDao dao;
	private CallSmsSafeAdapter adapter;
	private LinearLayout ll_loading;
	private int offset = 0;
	private int maxnumber = 20;
	private AdBanner adBanner;
	private View adBannerView;
	private static final String TAG_BANNER = "faaf16e322dd4c251d2f09a65a820a3f";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_sms_safe);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		dao = new BlackNumberDao(this);
		showBannerAd();
		fillData();
		lv_callsms_safe.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int lastposition = lv_callsms_safe.getLastVisiblePosition();
					if (lastposition == (infos.size() - 1)) {
						offset += maxnumber;
						fillData();
					}
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					break;
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
			}
		});
	}

	private void fillData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (infos == null) {
					infos = dao.findPart(offset, maxnumber);
				} else {
					infos.addAll(dao.findPart(offset, maxnumber));
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new CallSmsSafeAdapter();
							lv_callsms_safe.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
					}
				});
			};
		}.start();
	}

	private class CallSmsSafeAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view;
			ViewHolder holder;
			if (convertView == null) {
				view = View.inflate(getApplicationContext(),
						R.layout.list_item_callsms, null);
				holder = new ViewHolder();
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_black_number);
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_block_mode);
				holder.iv_delete = (ImageView) view
						.findViewById(R.id.iv_delete);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();// 5%
			}
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
			if ("1".equals(mode)) {
				holder.tv_mode.setText("电话拦截");
			} else if ("2".equals(mode)) {
				holder.tv_mode.setText("短信拦截");
			} else {
				holder.tv_mode.setText("全部拦截");
			}
			holder.iv_delete.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					AlertDialog.Builder builder = new Builder(
							CallSmsSafeActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定要删除这条记录么？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dao.delete(infos.get(position).getNumber());
									infos.remove(position);
									adapter.notifyDataSetChanged();
								}
							});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			});
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

	static class ViewHolder {
		TextView tv_number;
		TextView tv_mode;
		ImageView iv_delete;
	}

	private EditText et_blacknumber;
	private CheckBox cb_phone;
	private CheckBox cb_sms;
	private RelativeLayout bt_ok;
	private RelativeLayout bt_cancel;

	public void addBlackNumber(View view) {
		AlertDialog.Builder builder = new Builder(this);
		final AlertDialog dialog = builder.create();
		View contentView = View.inflate(this, R.layout.dialog_add_blacknumber,
				null);
		et_blacknumber = (EditText) contentView
				.findViewById(R.id.et_blacknumber);
		cb_phone = (CheckBox) contentView.findViewById(R.id.cb_phone);
		cb_sms = (CheckBox) contentView.findViewById(R.id.cb_sms);
		bt_cancel = (RelativeLayout) contentView.findViewById(R.id.cancel);
		bt_ok = (RelativeLayout) contentView.findViewById(R.id.ok);
		dialog.setView(contentView, 0, 0, 0, 0);
		dialog.show();
		bt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String blacknumber = et_blacknumber.getText().toString().trim();
				if (TextUtils.isEmpty(blacknumber)) {
					Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 0)
							.show();
					return;
				}
				String mode;
				if (cb_phone.isChecked() && cb_sms.isChecked()) {
					mode = "3";
				} else if (cb_phone.isChecked()) {
					mode = "1";
				} else if (cb_sms.isChecked()) {
					mode = "2";
				} else {
					Toast.makeText(getApplicationContext(), "请选择拦截模式", 0)
							.show();
					return;
				}
				dao.add(blacknumber, mode);
				BlackNumberInfo info = new BlackNumberInfo();
				info.setMode(mode);
				info.setNumber(blacknumber);
				infos.add(0, info);
				adapter.notifyDataSetChanged();
				dialog.dismiss();
			}
		});
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
