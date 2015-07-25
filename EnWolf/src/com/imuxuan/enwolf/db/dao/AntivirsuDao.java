package com.imuxuan.enwolf.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirsuDao {
	public static boolean isVirus(String md5) {
		String path = "/data/data/com.imuxuan.enwolf/files/antivirus.db";
		boolean result = false;
		SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.rawQuery("select * from datable where md5=?",
				new String[] { md5 });
		if (cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		db.close();
		return result;
	}
}
