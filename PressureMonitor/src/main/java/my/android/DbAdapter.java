package my.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DbAdapter {

	private static final String DATABASE_NAME = "pressure.db";
	private static final String DATABASE_TABLE = "pTable";
	private static final String TABLE_CREATE =
			"create table IF NOT EXISTS  " + DATABASE_TABLE + " ( _id integer primary key	autoincrement," +
					"_time NUMERIC not null, pressure NUMERIC not null);";
	private static final String TABLE_DROP =
			"drop table IF EXISTS " + DATABASE_TABLE+";";
	SQLiteDatabase myDatabase;
	private Context ctx;

	public void createDatabase(Context ctx) {
		this.ctx=ctx;
		Log.d(DbAdapter.class.getName(), "Creating database ***********************");
		myDatabase = ctx.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE,null);
		try{
			//myDatabase.execSQL(TABLE_DROP);

			myDatabase.execSQL(TABLE_CREATE);
		}catch (SQLiteException ex){}
		Log.d(DbAdapter.class.getName(), "Created database ***********************");

	}

	public void insValue(int press){
		// Создайте новую строку со значениями для вставки.
		ContentValues newValues = new ContentValues();
		// Задайте значения для каждой строки.
		newValues.put("_time", System.currentTimeMillis());
		newValues.put("pressure", press);
		// Вставьте строку в вашу базу данных.
		myDatabase.insert(DATABASE_TABLE, null, newValues);
	}

	public void delOldValues(int history){
		myDatabase.delete(DATABASE_TABLE, " _ID < (SELECT MAX(_ID)  FROM "+DATABASE_TABLE+")-"+history, null);
	}
	public List<ResultP> getValues(int length) {
		// Возвращает все строки для первого и третьего столбца, без повторений
		String[] result_columns = new String[] {"_id","_time", "pressure"};
		Cursor allRows = myDatabase.query(DATABASE_TABLE, result_columns,	" _ID >= (SELECT MAX(_ID)  FROM "+DATABASE_TABLE+")+1-"+length, null, null, null, null, null);
		ArrayList<ResultP> l = new ArrayList<ResultP>();
		if (allRows.moveToFirst()) {
			// Пройдитесь по каждой строке.
			do {
				int id = allRows.getInt(0);
				Long tt = allRows.getLong(1);
				int p = allRows.getInt(2);
				Log.d(DbAdapter.class.getName(), "readed "+id+" "+tt+" "+p);
				l.add(new ResultP(id, new Date(tt), p, 0));
			} while(allRows.moveToNext());
		}
		return l;
	}

}
