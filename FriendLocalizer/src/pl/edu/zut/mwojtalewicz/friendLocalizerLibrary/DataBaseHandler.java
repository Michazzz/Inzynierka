package pl.edu.zut.mwojtalewicz.friendLocalizerLibrary;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

	public class DataBaseHandler extends SQLiteOpenHelper {

		public DataBaseHandler(Context context) {
			super(context, Constans.DATABASE_NAME, null, Constans.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(Constans.CREATE_LOGIN_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Constans.TABLE_LOGIN);
			onCreate(db);
		}

		public void addUser(String name, String lastname, String email, String uid, String created_at) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(Constans.KEY_NAME, name); 
			values.put(Constans.KEY_LASTNAME, lastname);
			values.put(Constans.KEY_EMAIL, email);
			values.put(Constans.KEY_UID, uid);
			values.put(Constans.KEY_CREATED_AT, created_at);

			db.insert(Constans.TABLE_LOGIN, null, values);
			db.close();
		}
		
		public HashMap<String, String> getUserDetails(){
			HashMap<String,String> user = new HashMap<String,String>();
			String selectQuery = "SELECT  * FROM " + Constans.TABLE_LOGIN;
			 
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);

	        cursor.moveToFirst();
	        if(cursor.getCount() > 0){
	        	user.put("name", cursor.getString(1));
	        	user.put("lastname", cursor.getString(2));
	        	user.put("email", cursor.getString(3));
	        	user.put("uid", cursor.getString(4));
	        	user.put("created_at", cursor.getString(5));
	        }
	        cursor.close();
	        db.close();
			return user;
		}
		
		public HashMap<String, String> getUserFriends(String uid){
			HashMap<String,String> user = new HashMap<String,String>();
			String selectQuery = "SELECT  * FROM " + Constans.TABLE_FRIENDS + "WHERE " + Constans.KEY_UID_INVITING + " = " + uid + " AND status = 2";
			 
	        SQLiteDatabase db = this.getReadableDatabase();
	        Cursor cursor = db.rawQuery(selectQuery, null);

	        cursor.moveToFirst();
	        if(cursor.getCount() > 0){
	        	user.put("name", cursor.getString(1));
	        	user.put("lastname", cursor.getString(2));
	        	user.put("email", cursor.getString(3));
	        	user.put("uid", cursor.getString(4));
	        	user.put("created_at", cursor.getString(5));
	        }
	        cursor.close();
	        db.close();
			return user;
		}

		public int getRowCount() {
			String countQuery = "SELECT  * FROM " + Constans.TABLE_LOGIN;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			int rowCount = cursor.getCount();
			db.close();
			cursor.close();
			return rowCount;
		}
		
		public void resetTables(){
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(Constans.TABLE_LOGIN, null, null);
			db.close();
		}
}
