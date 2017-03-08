package cn.mengge.tongdacampus.server.SQLServer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.classes.DataClassTableClassInfo;
import cn.mengge.tongdacampus.classes.DataClassTableInfo;
import cn.mengge.tongdacampus.classes.DataGrade;
import cn.mengge.tongdacampus.classes.DataGradeNameAndOthers;
import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by MengGE on 2016/9/15.
 */
public class SQLServer {

    private SQLiteOpenHelper sqLiteHelper;

    /**
     * 创建数据库和表
     *
     * @param context    上下文对象（哪个activity）
     * @param sqlName    数据库名称
     * @param sqlVersion 数据库版本
     */
    public SQLServer(Context context, String sqlName, int sqlVersion) {
        sqLiteHelper = new SQLiteHelper(context, sqlName, null, sqlVersion);
    }

    /**
     * 创建数据库和表
     *
     * @param context 上下文对象（哪个activity）
     */
    public SQLServer(Context context) {
        sqLiteHelper = new SQLHelperForGuests(context, FinalStrings.SQLServerStrings.SQL_NAME_GUESTS, null, FinalStrings.SQLServerStrings.SQL_VERSION);
    }

    /**
     * 将成绩所有信息放到数据库的成绩表中
     *
     * @param tableName 成绩表名
     * @param gradeInfo 所有成绩
     */
    public void addGrade2GradeTable(String tableName, List<DataGrade> gradeInfo) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < gradeInfo.size(); i++) {
            values.put(FinalStrings.SQLServerStrings.GRADE_KCMC, gradeInfo.get(i).getKCMC());
            values.put(FinalStrings.SQLServerStrings.GRADE_JSXM, gradeInfo.get(i).getJSXM());
            values.put(FinalStrings.SQLServerStrings.GRADE_XQ, gradeInfo.get(i).getXQ());
            values.put(FinalStrings.SQLServerStrings.GRADE_XS, gradeInfo.get(i).getXS());
            values.put(FinalStrings.SQLServerStrings.GRADE_XF, gradeInfo.get(i).getXF());
            values.put(FinalStrings.SQLServerStrings.GRADE_ZPCJ, gradeInfo.get(i).getZPCJ());
            values.put(FinalStrings.SQLServerStrings.GRADE_PSCJ, gradeInfo.get(i).getPSCJ());
            values.put(FinalStrings.SQLServerStrings.GRADE_QMCJ, gradeInfo.get(i).getQMCJ());
            values.put(FinalStrings.SQLServerStrings.GRADE_KCSX, gradeInfo.get(i).getKCSX());
            values.put(FinalStrings.SQLServerStrings.GRADE_CJID, gradeInfo.get(i).getCJID());
            values.put(FinalStrings.SQLServerStrings.GRADE_KSFSM, gradeInfo.get(i).getKSFSM());
            values.put(FinalStrings.SQLServerStrings.GRADE_PXCJ, gradeInfo.get(i).getPXCJ());
            db.insert(tableName, null, values);
            values.clear();
        }
        db.close();
    }

    /**
     * 获取某门课程成绩的具体信息
     *
     * @param tableName 成绩表
     * @param cjid      成绩ID
     * @return 详细信息
     */
    public String getGradeDetails(String tableName, String cjid) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        String details = "";
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_CJID))
                    .equals(cjid)) {
                details = "教师姓名：\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_JSXM))
                        + "\n" + "学期：\t\t\t\t\t\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_XQ))
                        + "\n" + "学时：\t\t\t\t\t\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_XS))
                        + "\n" + "学分：\t\t\t\t\t\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_XF))
                        + "\n" + "平时成绩：\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_PSCJ))
                        + "\n" + "期末成绩：\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_QMCJ))
                        + "\n" + "总评成绩：\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_ZPCJ)) + "  (" + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_PXCJ)) + ")"
                        + "\n" + "课程属性：\t"
                        + cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_KCSX))
                        + "\n";
            }

        }

        return details;

    }

    /**
     * 获取数据库中记录条数
     *
     * @param tableName 表名
     * @return 记录条数
     */
    public int getSQLTotalCount(String tableName) {
        int count = 0;
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            count++;
        }

        cursor.close();
        db.close();
        return count;
    }

    /**
     * 从成绩表中获取成绩信息
     *
     * @param tableName 表名
     * @return 课程名称和其他信息
     */
    public List<DataGradeNameAndOthers> getGradeNameFromSQL(String tableName) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        List<DataGradeNameAndOthers> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            String kcmc = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_KCMC));
            String grade = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_ZPCJ));
            String pxcj = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_PXCJ));
            String teacher = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_JSXM));
            String kcsx = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_KCSX));
            String xf = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_XF));
            String xq = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_XQ));
            String cjid = cursor.getString(cursor.getColumnIndex(FinalStrings.SQLServerStrings.GRADE_CJID));
            list.add(new DataGradeNameAndOthers(kcmc,grade,pxcj,teacher,kcsx,xf,xq,cjid));
        }


        cursor.close();
        db.close();

        return list;
    }

    /**
     * 删除表中所有数据
     *
     * @param tableName 表名
     * @return 是否删除成功
     */
    public boolean deleteTableData(String tableName) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        db.delete(tableName, "id >= ?", new String[]{"0"});
        db.close();
        return true;
    }

    /**
     * 保存课程表信息到数据库中
     *
     * @param classTableList 从json数据中解析出来的课表信息,每个元素代表一周,共12节
     */
    public void saveClassTable2DB(String tableName, List<DataClassTableInfo> classTableList) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < classTableList.size(); i++) {

            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ1, classTableList.get(i).getXqj1());
            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ2, classTableList.get(i).getXqj2());
            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ3, classTableList.get(i).getXqj3());
            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ4, classTableList.get(i).getXqj4());
            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ5, classTableList.get(i).getXqj5());
            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ6, classTableList.get(i).getXqj6());
            values.put(FinalStrings.SQLServerStrings.CLASS_TABLE_XQJ7, classTableList.get(i).getXqj7());

            db.insert(tableName, null, values);
            values.clear();
        }
        db.close();
    }

    //从数据库中获取一天的课程
    public List<DataClassTableClassInfo> getSingleDayClassInfo(String tableName, int week) {
        SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);

        List<DataClassTableClassInfo> list = new ArrayList<>();
        String index = "xqj" + week;
        String kcInfo = "";
        String lastKcInfo = "";
        int endId = 0;
        int beginId = 0;
        int id = 0;
        int test = 0;
        DataClassTableClassInfo info = null;
        while (cursor.moveToNext()) {
            id++;
            if ((kcInfo = cursor.getString(cursor.getColumnIndex(index))).length() != 0) {
                if (!kcInfo.equals(lastKcInfo)) {
                    info = new DataClassTableClassInfo();
                    beginId = id;
                    info.setKcInfo(kcInfo);
                    info.setBeginJieCi(beginId);
                    lastKcInfo = kcInfo;
                    list.add(info);
                } else {
                    endId = id;
                    info.setEndJieCi(endId);
                }
            }
        }
        cursor.close();
        db.close();

        return list;
    }

    /**
     * 在课表中添加课程信息
     *
     * @param tableName  课表表名
     * @param week       星期?
     * @param begin      开始节次
     * @param end        结束节次
     * @param lessonInfo 课程信息
     */
    public void saveLessonInfo2SQL(String tableName, String week, int begin, int end, String lessonInfo) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("xqj" + week, lessonInfo);

        LogUtil.d("beginend", begin + " " + end);
        if (begin == end) {
            db.update(tableName, values, "id = ?", new String[]{String.valueOf(begin)});
        } else {
            for (int i = begin; i <= end; i++) {
                db.update(tableName, values, "id = ?", new String[]{String.valueOf(i)});
            }
        }
        db.close();
    }

    /**
     * 从课表中删除课程信息
     *
     * @param tableName 课表表名
     * @param week      星期
     * @param begin     开始节次
     * @param end       结束节次
     */
    public void deleteLessonInfoFromSQL(String tableName, int week, int begin, int end) {
        SQLiteDatabase db = sqLiteHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("xqj" + week, "");
        if (begin == end) {
            db.update(tableName, values, "id = ?", new String[]{String.valueOf(begin)});
        } else {
            for (int i = begin; i <= end; i++) {
                db.update(tableName, values, "id = ?", new String[]{String.valueOf(i)});
            }

        }
        db.close();
    }


}

