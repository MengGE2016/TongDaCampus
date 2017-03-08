package cn.mengge.tongdacampus.classes;

/**
 * Created by MengGE on 2016/10/14.
 */
public class DataZbxx {

    private String itemLinkStr;
    private String itemTitleStr;
    private String itemTimeStr;
    private boolean itemFlag;

    public DataZbxx() {
    }

    public DataZbxx(String itemLinkStr, String itemTitleStr, String itemTimeStr, boolean itemFlag) {
        this.itemLinkStr = itemLinkStr;
        this.itemTitleStr = itemTitleStr;
        this.itemTimeStr = itemTimeStr;
        this.itemFlag = itemFlag;
    }

    public String getItemLinkStr() {
        return itemLinkStr;
    }

    public void setItemLinkStr(String itemLinkStr) {
        this.itemLinkStr = itemLinkStr;
    }

    public String getItemTitleStr() {
        return itemTitleStr;
    }

    public void setItemTitleStr(String itemTitleStr) {
        this.itemTitleStr = itemTitleStr;
    }

    public String getItemTimeStr() {
        return itemTimeStr;
    }

    public void setItemTimeStr(String itemTimeStr) {
        this.itemTimeStr = itemTimeStr;
    }

    public boolean getItemFlag() {
        return itemFlag;
    }

    public void setItemFlag(boolean itemFlag) {
        this.itemFlag = itemFlag;
    }
}
