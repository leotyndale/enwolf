package com.imuxuan.enwolf.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imuxuan.enwolf.R;

public class SettingItemView extends RelativeLayout {
	private CheckBox cb_status;
	private TextView tv_desc;
	private TextView tv_title;
	private String desc_on;
	private String desc_off;

	private void iniView(Context context) {
		View.inflate(context, R.layout.setting_item_view, this);
		cb_status = (CheckBox) this.findViewById(R.id.cb_status);
		tv_desc = (TextView) this.findViewById(R.id.tv_desc);
		tv_title = (TextView) this.findViewById(R.id.tv_title);
	}

	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.imuxuan.enwolf",
				"title");
		desc_on = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.imuxuan.enwolf",
				"desc_on");
		desc_off = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.imuxuan.enwolf",
				"desc_off");
		tv_title.setText(title);
		setDesc(desc_off);
	}

	public SettingItemView(Context context) {
		super(context);
		iniView(context);
	}

	public boolean isChecked() {
		return cb_status.isChecked();
	}

	public void setChecked(boolean checked) {
		if (checked) {
			setDesc(desc_on);
		} else {
			setDesc(desc_off);
		}
		cb_status.setChecked(checked);
	}

	public void setDesc(String text) {
		tv_desc.setText(text);
	}
}
