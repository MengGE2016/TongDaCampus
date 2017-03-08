package cn.mengge.tongdacampus.server;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.classes.DataClassTableClassInfo;
import cn.mengge.tongdacampus.classes.Data3Strings1IntTLTF;
import cn.mengge.tongdacampus.classes.DataNewsLinkTitle;
import cn.mengge.tongdacampus.classes.DataUriVideo;
import cn.mengge.tongdacampus.classes.DataZbxx;
import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by MengGE on 2016/10/2.
 */
public class StringServer {

    /**
     * 从网页中获取有用的新闻信息
     *
     * @param html 包含新闻信息的网页
     * @return 新闻内容
     */
    public static String getNewsContent(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("p").removeAttr("style").removeAttr("src").removeAttr("href");
        return elements.toString();
    }

    public static List<Data3Strings1IntTLTF> getNewsData(int flag, String html) {
        List<Data3Strings1IntTLTF> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("initData");
        Elements elements = element.getElementsByTag("li");

        for (Element e : elements) {
            String time = e.select("span").text();
            String title = e.select("a").text();
            String link = e.select("a").attr("href");
            list.add(new Data3Strings1IntTLTF(title, link, time, flag));
        }

        return list;
    }

    public static List<DataNewsLinkTitle> getNewsData(String html) {
        List<DataNewsLinkTitle> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("initData");
        Elements elements = element.getElementsByTag("li");

        for (Element e : elements) {
            String time = e.select("span").text();
            String title = e.select("a").text();
            String link = e.select("a").attr("href");
            list.add(new DataNewsLinkTitle(link, title, time));
        }

        return list;
    }

    public static List<DataNewsLinkTitle> getDataFromHtmlByClass(String html) {

        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("lb_l");
        return getDataFromArrtibute(elements.toString());
    }

    public static List<Data3Strings1IntTLTF> getJwxxJwgdData(String html) {
        List<Data3Strings1IntTLTF> list = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements titleLinkElements = document.getElementsByClass("tdli");
        Elements timeElements = document.getElementsByAttribute("align");
        for (int i = 0; i < titleLinkElements.size(); i++) {
            String title = titleLinkElements.get(i).text();
            String link = FinalStrings.NetStrings.URL_JWGL + titleLinkElements.get(i).select("a").attr("href");
            String time = timeElements.get(i).text();

            list.add(new Data3Strings1IntTLTF(title, link, time, FinalStrings.NetStrings.ZBXX_ZBXX));
        }
        return list;

    }

    public static List<DataNewsLinkTitle> getDataFromArrtibute(String dataStr) {
        Document document = Jsoup.parse(dataStr);
        Elements elements = document.getElementsByAttribute("href");
        String link = "", title = "";
        char[] content;
        List<DataNewsLinkTitle> list = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {

            content = elements.get(i).toString().toCharArray();
            link = "";
            title = "";
            for (int j = 9; j < content.length; j++) {
                if (content[j] == '\"')
                    break;
                link += content[j];
            }

            for (int j = 9; j < content.length; j++) {
                if (content[j] == ' ') {
                    for (j += 8; j < content.length; j++) {
                        if (content[j] == '\"')
                            break;
                        title += content[j];
                    }

                    break;
                }
            }

            DataNewsLinkTitle linkTitle = new DataNewsLinkTitle(link, title,"");
            list.add(linkTitle);
        }
        return list;

    }

    public static String getNewsByTag(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("p");

        return getNewsContent(elements);
    }

    public static String getNewsContent(Elements elements) {

        String newsContent = "";

        for (Element element : elements) {
            char[] eleContent = element.toString().toCharArray();
            newsContent += "     ";
            for (int i = 0; i < eleContent.length; i++) {
                if (eleContent[i] == '>') {
                    for (int j = i + 1; j < eleContent.length; j++) {
                        if (eleContent[j] == '<')
                            break;
                        newsContent += eleContent[j];
                    }
                    newsContent += "\n";
                    break;
                }
            }
        }

        return newsContent;
    }

    public static List<DataZbxx> getZbxxDataList(String html, boolean flag) {

        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("item");

        char[] chs;

        List<DataZbxx> list = new ArrayList<>();
        for (Element e : elements) {
            StringBuilder itemLinkStr = new StringBuilder(FinalStrings.NetStrings.URL_ZB_ADD);
            StringBuilder itemTitleStr = new StringBuilder();
            StringBuilder itemTimeTempStr = new StringBuilder();
            chs = e.toString().toCharArray();
            int i = 23;
            for (; i < chs.length; i++) {
                if (chs[i] == '\"')
                    break;
                itemLinkStr.append(chs[i]);
            }
            for (i += 25; i < chs.length; i++) {
                if (chs[i] == '\"')
                    break;
                itemTitleStr.append(chs[i]);
            }

            for (i = chs.length - 39; i >= 23; i--) {
                if (chs[i] == '>') {
                    break;
                }
                itemTimeTempStr.append(chs[i]);
            }

            char[] cs = itemTimeTempStr.toString().toCharArray();
            StringBuilder itemTimeStr = new StringBuilder();
            for (int j = cs.length - 1; j >= 0; j--) {
                itemTimeStr.append(cs[j]);
            }

            list.add(new DataZbxx(itemLinkStr.toString(), itemTitleStr.toString(), itemTimeStr.toString(), flag));

        }
        //
        // for (int i = 0; i < list.size(); i++) {
        // System.out.println(list.get(i).getItemLinkStr() + " " +
        // list.get(i).getItemTitleStr() + " "
        // + list.get(i).getItemTimeStr());
        // }
        return list;
    }

    public static String getZbxxMessage(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByTag("p");
//        StringBuilder itemMessage = new StringBuilder();
//        for (int j = 0; j < elements.size(); j++) {
//            char[] chs = elements.get(j).toString().toCharArray();
//
//            for (int i = 0; i < chs.length; i++) {
//                if (chs[i] == '>') {
//                    for (int n = ++i; i < chs.length; i++) {
//                        if (chs[i] == '<') {
//                            break;
//                        }
//                        itemMessage.append(chs[i]);
//                    }
//                    itemMessage.append("\n");
//                    break;
//                }
//
//            }
//        }
//        return itemMessage.toString();
        return elements.toString();
    }

    //判断该字符是否是数字
    public static boolean isNum(char c) {
        if (c >= '0' && c <= '9') {
            return true;
        }
        return false;
    }

    //从课表中解析出课程的具体信息
    public static DataClassTableClassInfo.ClassInfo getClassInfo(String kcmcInfo) {
        String kcmcStr = "";
        String teacherStr = "";
        String weeksStr = "";
        String classRoomStr = "";
        String[] kcmc = kcmcInfo.split("\\[");

        kcmcStr = kcmc[0];
        kcmc = kcmc[1].split(" ");
        weeksStr = kcmc[1];
        teacherStr = kcmc[0];
//        char[] chs = kcmc[1].toCharArray();
//        for (char c : chs) {
//            if (c == '[' || c == ']') {
//                continue;
//            }
//            teacherStr += c;
//        }

        char[] chsc = kcmc[2].toCharArray();
        for (char c : chsc) {
            if (c == '[' || c == ']') {
                continue;
            }
            classRoomStr += c;
        }

        return new DataClassTableClassInfo.ClassInfo(kcmcStr, teacherStr, weeksStr, classRoomStr);
    }

    //从教务系统网页中获取学生信息
    public static String getStudentInfoFromHtml(String html) {
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("stuInfo");
        return element.toString();
    }


    public static String[] getURLformHtml(String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementsByClass("slides").select("img");

        String[] urls = new String[elements.size()];

        for (int i=0; i < elements.size(); i++) {
            char[] ch = elements.get(i).attr("style").toCharArray();
            String url = "";
            boolean flag = false;
            for (char c : ch) {
                if (c == ')') {
                    break;
                }
                if (c == '(') {
                    flag = true;
                    continue;
                }
                if (flag) {
                    url += c;
                }
            }
            urls[i]="http://www.ntu.edu.cn"+url;
        }
        return urls;
    }
}
