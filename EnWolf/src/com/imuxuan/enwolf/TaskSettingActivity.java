package com.imuxuan.enwolf;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.imuxuan.enwolf.service.AutoCleanService;
import com.imuxuan.enwolf.utils.ServiceUtils;
import com.imuxuan.enwolf.R;

public class TaskSettingActivity extends Activity {
	private CheckBox cb_show_system;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_task_setting);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		cb_show_system = (CheckBox) findViewById(R.id.cb_show_system);
		cb_show_system.setChecked(sp.getBoolean("showsystem", false));
		cb_show_system
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						Editor editor = sp.edit();
						editor.putBoolean("showsystem", isChecked);
						editor.commit();
					}
				});
	}

	@Override
	protected void onStart() {
		super.onStart();
	}
}
