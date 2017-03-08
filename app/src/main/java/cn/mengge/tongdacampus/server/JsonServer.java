package cn.mengge.tongdacampus.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.mengge.tongdacampus.classes.DataClassTableInfo;
import cn.mengge.tongdacampus.classes.DataValAndText;
import cn.mengge.tongdacampus.classes.DataGrade;
import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by MengGE on 2016/10/21.
 */
public class JsonServer {

    public static List<DataValAndText> parseClassValAndTextJsonData(String jsonStr) {

        List<DataValAndText> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.opt(i);
                String val = object.getString("val");
                String text = object.getString("text");
                list.add(new DataValAndText(val, text));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<DataClassTableInfo> parseClassTableData(String jsonStr) {
        List<DataClassTableInfo> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = (JSONObject) jsonArray.opt(i);
                String xqj1 = object.getString("xqj1");
                String xqj2 = object.getString("xqj2");
                String xqj3 = object.getString("xqj3");
                String xqj4 = object.getString("xqj4");
                String xqj5 = object.getString("xqj5");
                String xqj6 = object.getString("xqj6");
                String xqj7 = object.getString("xqj7");
                list.add(new DataClassTableInfo(xqj1, xqj2, xqj3, xqj4, xqj5, xqj6, xqj7));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return list;
        }

        return list;
    }

    public static List<DataGrade> parseGradeTableData(int totalCountInSql, String jsonStr) {
        List<DataGrade> list = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            int totalCount = jsonObject.getInt("totalCount");
            if (totalCountInSql/*sqlServer.getSQLTotalCount(GRADE_TABLE_NAME)*/ == totalCount) {
                return list;
            }
            if (totalCountInSql < totalCount) {
                for (int i = totalCountInSql; i < jsonArray.length(); i++) {
                    JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
                    String kCMC = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_KCMC);
                    String jSXM = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_JSXM);
                    String xQ = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_XQ);
                    String kCSX = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_KCSX);
                    String cJID = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_CJID);
                    String kSFSM = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_KSFSM);
                    String pXCJ = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_PXCJ);
                    String xS = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_XS);
                    String xF = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_XF);
                    String zPCJ = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_ZPCJ);
                    String pSCJ = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_PSCJ);
                    String qMCJ = jsonObject2.getString(FinalStrings.SQLServerStrings.GRADE_QMCJ);
                    list.add(new DataGrade(kCMC, jSXM, xQ, kCSX, cJID, kSFSM, pXCJ,
                            xS, xF, zPCJ, pSCJ, qMCJ));
                }

                return list;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}
