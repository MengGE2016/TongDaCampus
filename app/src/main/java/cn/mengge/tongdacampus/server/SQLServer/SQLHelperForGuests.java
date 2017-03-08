package cn.mengge.tongdacampus.server.SQLServer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by MengGE on 2016/11/3.
 */
public class SQLHelperForGuests extends SQLiteOpenHelper {

    public SQLHelperForGuests(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //临时课表
        db.execSQL(FinalStrings.SQLServerStrings.CREATE_TEMP_CLASS_TABLE_TABLE);
        //临时成绩
        db.execSQL(FinalStrings.SQLServerStrings.CREATE_TEMP_GRADE_TABLE);
        //空教室表
        db.execSQL(FinalStrings.SQLServerStrings.CREATE_EMPTY_CLASSROOM_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
