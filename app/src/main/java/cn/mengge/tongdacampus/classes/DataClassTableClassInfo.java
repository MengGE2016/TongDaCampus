package cn.mengge.tongdacampus.classes;

/**
 * Created by MengGE on 2016/10/22.
 */
public class DataClassTableClassInfo {

    private int beginJieCi;
    private int endJieCi;

    private String kcInfo;

    public DataClassTableClassInfo() {
    }

    public DataClassTableClassInfo(int beginJieCi, int endJieCi, String kcInfo) {
        this.beginJieCi = beginJieCi;
        this.endJieCi = endJieCi;
        this.kcInfo = kcInfo;

    }

    public int getBeginJieCi() {
        return beginJieCi;
    }

    public void setBeginJieCi(int beginJieCi) {
        this.beginJieCi = beginJieCi;
    }

    public int getEndJieCi() {
        return endJieCi;
    }

    public void setEndJieCi(int endJieCi) {
        this.endJieCi = endJieCi;
    }


    public String getKcInfo() {
        return kcInfo;
    }

    public void setKcInfo(String kcInfo) {
        this.kcInfo = kcInfo;
    }


    public static class ClassInfo {
        private String kcmcStr;
        private String teacherName;
        private String weeksStr;
        private String classRoom;


        public ClassInfo(String kcmcStr, String teacherName, String weeksStr, String classRoom) {
            this.kcmcStr = kcmcStr;
            this.teacherName = teacherName;
            this.weeksStr = weeksStr;
            this.classRoom = classRoom;
        }

        public String getKcmcStr() {
            return kcmcStr;
        }

        public void setKcmcStr(String kcmcStr) {
            this.kcmcStr = kcmcStr;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getWeeksStr() {
            return weeksStr;
        }

        public void setWeeksStr(String weeksStr) {
            this.weeksStr = weeksStr;
        }

        public String getClassRoom() {
            return classRoom;
        }

        public void setClassRoom(String classRoom) {
            this.classRoom = classRoom;
        }
    }
}
