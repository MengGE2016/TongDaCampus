package cn.mengge.tongdacampus.classes;

/**
 * Created by MengGE on 2016/9/15.
 */
public class DataGradeNameAndOthers {


    private String kcmc;
    private String grade;
    private String pxcj;

    private String teacher;
    private String kcsx;
    private String xf;


    private String xq;
    private String cjid;

    public DataGradeNameAndOthers(String kcmc, String grade, String xq , String pxcj, String cjid) {
        super();
        this.kcmc = kcmc;
        this.grade = grade;
        this.xq = xq;
        this.pxcj = pxcj;
        this.cjid = cjid;
    }

    public DataGradeNameAndOthers(String kcmc, String grade, String pxcj, String teacher, String kcsx, String xf, String xq, String cjid) {
        this.kcmc = kcmc;
        this.grade = grade;
        this.pxcj = pxcj;
        this.teacher = teacher;
        this.kcsx = kcsx;
        this.xf = xf;
        this.xq = xq;
        this.cjid = cjid;
    }

    public String getKcmc() {
        return kcmc;
    }

    public void setKcmc(String kcmc) {
        this.kcmc = kcmc;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getPxcj(){return pxcj;}

    public void setPxcj(String pxcj){this.pxcj = pxcj;}

    public void setCjid(String cjid) {
        this.cjid = cjid;
    }

    public String getCjid() {
        return cjid;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getKcsx() {
        return kcsx;
    }

    public void setKcsx(String kcsx) {
        this.kcsx = kcsx;
    }

    public String getXf() {
        return xf;
    }

    public void setXf(String xf) {
        this.xf = xf;
    }
}
