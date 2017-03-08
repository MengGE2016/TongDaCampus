package cn.mengge.tongdacampus.classes;

/**
 * Created by MengGE on 2016/10/12.
 */
public class DataNewsDrawerTodayClassTable {
    private String jieCiStr;
    private String kcmcStr;
    private String classroomStr;

    public DataNewsDrawerTodayClassTable() {
    }

    public DataNewsDrawerTodayClassTable(String jieCiStr, String kcmcStr, String classroomStr) {
        this.jieCiStr = jieCiStr;
        this.kcmcStr = kcmcStr;
        this.classroomStr = classroomStr;
    }

    public String getJieCiStr() {
        return jieCiStr;
    }

    public void setJieCiStr(String jieCiStr) {
        this.jieCiStr = jieCiStr;
    }

    public String getKcmcStr() {
        return kcmcStr;
    }

    public void setKcmcStr(String kcmcStr) {
        this.kcmcStr = kcmcStr;
    }

    public String getClassroomStr() {
        return classroomStr;
    }

    public void setClassroomStr(String classroomStr) {
        this.classroomStr = classroomStr;
    }
}
