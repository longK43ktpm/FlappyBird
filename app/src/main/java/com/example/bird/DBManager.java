package com.example.bird;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    public static final String DATABASE_NAME ="myDatabase";
    private static final String TABLE_NAME ="myTable";
    private static final String ID ="id";
    private static final String BEST_SCORE ="best_score";
    private Context context;

    public DBManager(Context context) {
        super(context, DATABASE_NAME,null, 1); //tạo csdl
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE "+TABLE_NAME +" (" +
                ID +" integer primary key AUTOINCREMENT, "+ BEST_SCORE +" integer)";
        db.execSQL(sqlQuery);
        //insert best score mặc định = 0
        ContentValues values = new ContentValues();
        values.put(BEST_SCORE, 0);
        db.insert(TABLE_NAME,null,values);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public int UpdateBestScore(int score){
        SQLiteDatabase db = this.getWritableDatabase();
        //lấy ra bestScore hiện tại
        Cursor cursor = db.query(TABLE_NAME, new String[] { ID, BEST_SCORE },
                ID + "=?",new String[] { "1" },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        int bestScore = cursor.getInt(1);
        cursor.close();

        //update lại database nếu score lớn hơn bestScore
        if (score > bestScore) {
            ContentValues values = new ContentValues();
            values.put(BEST_SCORE, score);
            db.update(TABLE_NAME, values, ID + "=?", new String[]{"1"});
            return score;
        }
        return bestScore;
    }
}
