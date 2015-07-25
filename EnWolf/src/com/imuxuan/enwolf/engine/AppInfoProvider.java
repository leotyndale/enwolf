package com.imuxuan.enwolf.engine;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.imuxuan.enwolf.domain.AppInfo;

public class AppInfoProvider {
	public static List<AppInfo> getAppInfos(Context context) {
		PackageManager pm = context.getPackageManager();
		List<PackageInfo> packInfos = pm.getInstalledPackages(0);
		List<AppInfo> appInfos = new ArrayList<AppInfo>();
		for (PackageInfo packInfo : packInfos) {
			AppInfo appInfo = new AppInfo();
			String packname = packInfo.packageName;
			Drawable icon = packInfo.applicationInfo.loadIcon(pm);
			String name = packInfo.applicationInfo.loadLabel(pm).toString();
			int flags = packInfo.applicationInfo.flags;
			int uid = packInfo.applicationInfo.uid;
			appInfo.setUid(uid);
			if ((flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				appInfo.setUserApp(true);
			} else {
				appInfo.setUserApp(false);
			}
			if ((flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0) {
				appInfo.setInRom(true);
			} else {
				appInfo.setInRom(false);
			}
			appInfo.setPackname(packname);
			appInfo.setIcon(icon);
			appInfo.setName(name);
			appInfos.add(appInfo);
		}
		return appInfos;
	}
}
