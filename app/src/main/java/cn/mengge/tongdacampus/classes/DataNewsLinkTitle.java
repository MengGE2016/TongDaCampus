package cn.mengge.tongdacampus.classes;

/**
 * Created by MengGE on 2016/10/2.
 */
public class DataNewsLinkTitle {
    private String link;
    private String title;
    private String time;

    public DataNewsLinkTitle() {
    }

    public DataNewsLinkTitle(String link, String title, String time) {

        this.link = link;
        this.title = title;
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
