package cn.mengge.tongdacampus.classes;

/**
 * Created by MengGE on 2016/10/23.
 */
public class Data3Strings1IntTLTF {

    private String titleStr;
    private String linkStr;
    private String timeStr;
    private int flag;

    public Data3Strings1IntTLTF(String titleStr, String linkStr, String timeStr, int flag) {
        this.titleStr = titleStr;
        this.linkStr = linkStr;
        this.timeStr = timeStr;
        this.flag = flag;
    }

    public String getTitleStr() {
        return titleStr;
    }

    public void setTitleStr(String titleStr) {
        this.titleStr = titleStr;
    }

    public String getLinkStr() {
        return linkStr;
    }

    public void setLinkStr(String linkStr) {
        this.linkStr = linkStr;
    }

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
