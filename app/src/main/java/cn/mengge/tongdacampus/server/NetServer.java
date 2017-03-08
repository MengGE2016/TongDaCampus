package cn.mengge.tongdacampus.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

import cn.mengge.tongdacampus.Activies.ActivityStart;
import cn.mengge.tongdacampus.CustomWidget.LogUtil;
import cn.mengge.tongdacampus.classes.DataCookieAndCheckCodeBm;
import cn.mengge.tongdacampus.utils.FinalStrings;

/**
 * Created by MengGE on 2016/9/21.
 */
public class NetServer {

    public NetServer(){}

    //判断网络是否连接
    public static boolean isNetworkConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null) return networkInfo.isAvailable();

        return false;
    }

    //判断wifi是否可用
    public static boolean isWifiConnected(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo != null) return networkInfo.isAvailable();

        return false;
    }

    //判断monile网络是否可用
    public static boolean isMobileConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = connectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) return mMobileNetworkInfo.isAvailable();

        return false;
    }


    //判断网络类型
    //返回值 -1：没有网络  1：WIFI网络2：wap网络3：net网络
    public static int GetNetype(Context context)
    {
        int netType = -1;
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo==null)
        {
            return netType;
        }
        int nType = networkInfo.getType();
        if(nType==ConnectivityManager.TYPE_MOBILE)
        {
            if(networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
            {
                netType = 3;
            }
            else
            {
                netType = 2;
            }
        }
        else if(nType==ConnectivityManager.TYPE_WIFI)
        {
            netType = 1;
        }
        return netType;
    }







    public static String getHtmlAndCookie(String cookie, String urlStr) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        String htmlStr = "";
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

            httpURLConnection.setRequestProperty("Cookie", cookie);

            LogUtil.d("HtmlCode", String.valueOf(httpURLConnection.getResponseCode()));
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FinalStrings.NetStrings.NET_DATA_CHARSET_UTF_8));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                htmlStr = response.toString();
                return htmlStr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return "";
    }

    public static String getStudentInfo(String cookie, String urlStr) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        String htmlStr = "";
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

            httpURLConnection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            httpURLConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            httpURLConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            httpURLConnection.setRequestProperty("Referer", FinalStrings.NetStrings.URL_JWGL_LOGIN);
            httpURLConnection.setRequestProperty("Cookie", cookie);
            httpURLConnection.setRequestProperty("Connection", "keep-alive");
            httpURLConnection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            LogUtil.d("HtmlCode", String.valueOf(httpURLConnection.getResponseCode()));
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FinalStrings.NetStrings.NET_DATA_CHARSET_UTF_8));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                htmlStr = response.toString();
                return htmlStr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return "";
    }


    /**
     * 获取网页源码
     *
     * @param urlStr 网址
     * @return
     */
    public static String getHtml(String urlStr) {

        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        String htmlStr = "";
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
//            Log.d("HtmlCode", String.valueOf(httpURLConnection.getResponseCode()));
            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FinalStrings.NetStrings.NET_DATA_CHARSET_UTF_8));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                htmlStr = response.toString();
                return htmlStr;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return "";
    }

    /**
     * 获取Bitmap图片
     *
     * @param urlStr 图片地址
     * @return
     */
    public static Bitmap getBitmap(String urlStr) {
        Bitmap bitmap = null;
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();

            httpURLConnection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
            httpURLConnection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            httpURLConnection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpURLConnection != null) httpURLConnection.disconnect();
        }
        return bitmap;
    }

    /**
     * 获取Cookie和验证码
     *
     * @param context 上下文对象--用于Toast信息显示
     * @param urlStr  获取Cookie和验证码的网址URL--一般为验证码图片地址
     * @return
     */
    public static DataCookieAndCheckCodeBm getCookieAndCheckCodeBitmap(Context context, String urlStr) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_GET);
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                final Bitmap checkCodeBitmap = BitmapFactory.decodeStream(inputStream);
                String cookie_cache = connection.getHeaderField("Set-Cookie");

                char[] cookie = cookie_cache.toCharArray();
                String Cookie = "";
                for (int i = 0; i < cookie.length; i++) {
                    if (cookie[i] == ';')
                        break;
                    Cookie += cookie[i];
                }

                return new DataCookieAndCheckCodeBm(Cookie, checkCodeBitmap);
            } else {
                Toast.makeText(context, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    /**
     * 登录教务管理系统
     *
     * @param context          上下文对象
     * @param urlStr           教务管理系统网址
     * @param cookie           cookie
     * @param studentNum       学号
     * @param studentSFZH      身份证号
     * @param studentPassword  登录密码(初始为身份证号)
     * @param studentCheckCode 验证码
     * @return
     */
    public static boolean isLoginJwglSuccess(Context context, String urlStr, String cookie, String studentNum, String studentSFZH, String studentPassword, String studentCheckCode) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_POST);
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String content = FinalStrings.NetStrings.VIEW_STATE +
                    "&xh=" + studentNum +
                    "&sfzh=" + studentSFZH +
                    "&kl=" + studentPassword +
                    "&yzm=" + URLEncoder.encode(studentCheckCode, FinalStrings.NetStrings.NET_DATA_CHARSET_GBK);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();
            LogUtil.d("ResponseCode", String.valueOf(connection.getResponseCode()));
            if (connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM || connection.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP) {
                return true;
            } else if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                Toast.makeText(context, FinalStrings.CommonStrings.INFO_ERROR, Toast.LENGTH_SHORT).show();
                return false;
            } else {
                Toast.makeText(context, FinalStrings.CommonStrings.CHECK_NETWORK_CONNECTION_STATE, Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }

    //获取院系的Json数据
    public static String getClassTableYuanXi(String cookie, String xq) {
        String jsonDataStr = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(FinalStrings.NetStrings.URL_CLASSTABLE);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_POST);
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/Main.aspx");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String content = "xq=" + xq;
            LogUtil.d("Resode", xq);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                jsonDataStr = NetServer.getHtmlAndCookie(cookie, "http://jwgl.ntu.edu.cn/cjcx/Data/Basis/dep.aspx?_dc=1477066227855");
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return jsonDataStr;
    }

    //获取院系所有班级的Json数据
    public static String getClassTable(String cookie, String depId) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String jsonDataStr = "";
        try {
            URL url = new URL(FinalStrings.NetStrings.URL_CLASS_CLASS);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_POST);
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/Main.aspx");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String content = "depId=" + depId;
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);

                jsonDataStr = response.toString();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return jsonDataStr;
    }

    //获取该班级的课表的Json数据
    public static String getClassTable(String cookie, String xq, String banJiId) {
        String jsonStr = "";
        URL url = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            url = new URL("http://jwgl.ntu.edu.cn/cjcx/Data/Table/ClassTableData.aspx");
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);

            connection.setReadTimeout(5000);

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");

            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String content = "xq=" + xq + "&bjid=" + banJiId;
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                jsonStr = response.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }

        return jsonStr;
    }

    //获取校区的Json数据
    public static String getXiaoQu(String cookie, String xq) {
        String jsonDataStr = "";
        HttpURLConnection connection = null;
        try {
            URL url = new URL(FinalStrings.NetStrings.URL_XIAO_QU);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_POST);
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/Main.aspx");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String content = "xq=" + xq;
            LogUtil.d("Resode", xq);
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                jsonDataStr = NetServer.getHtmlAndCookie(cookie, FinalStrings.NetStrings.URL_XIAO_QU_JSON);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return jsonDataStr;
    }

    /**
     * 获取校区所有教室的Json数据
     *
     * @param cookie cookie
     * @param campId 校区ID
     * @return 教室编号的json数据
     */
    public static String getClassRoom(String cookie, String campId) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String jsonDataStr = "";
        try {
            URL url = new URL(FinalStrings.NetStrings.URL_CLASSROOM_JSON);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(FinalStrings.NetStrings.NET_REQUEST_METHOD_POST);
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/Main.aspx");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Upgrade-Insecure-Requests", "1");

            String content = "campId=" + campId;
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);

                jsonDataStr = response.toString();
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }

        return jsonDataStr;
    }

    /**
     * 获取某教室的使用情况的json数据
     *
     * @param cookie cookie
     * @param xq     学期
     * @param fjbh   房间编号
     * @return 教室使用情况Json数据
     */
    public static String getUseClassRoom(String cookie, String xq, String fjbh) {
        String jsonStr = "";
        URL url = null;
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            url = new URL(FinalStrings.NetStrings.URL_USE_CLASSROOM_JSON);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setInstanceFollowRedirects(false);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");

            connection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            connection.setRequestProperty("Referer", FinalStrings.NetStrings.URL_STUDENT_MAIN);
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("Connection", "keep-alive");
            String content = "xq=" + xq + "&fjbh=" + URLEncoder.encode(fjbh, "utf-8");
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content/*URLEncoder.encode(content, "utf-8")*/);
            dataOutputStream.flush();
            dataOutputStream.close();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                StringBuilder response = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                jsonStr = response.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                connection.disconnect();
            }
        }

        return jsonStr;
    }

    //获取成绩Json数据
    public static String getGradeJsonData(String cookie) {
        InputStream inputStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(FinalStrings.NetStrings.URL_STUDENT_FINAL_GRADE_QUERY);
            connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setConnectTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setReadTimeout(FinalStrings.NetStrings.NET_TIME_OUT);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setRequestProperty("Host", "jwgl.ntu.edu.cn");
            connection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:48.0) Gecko/20100101 Firefox/48.0");

            connection.setRequestProperty("Referer", "http://jwgl.ntu.edu.cn/cjcx/Main.aspx");
            connection.setRequestProperty("Cookie", cookie);

            String content = "start=0&pageSize=200";
            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.writeBytes(content);
            dataOutputStream.flush();
            dataOutputStream.close();

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) response.append(line);
                return response.toString();

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }


    /***************************获取StartPage页面的图片的URL********************************/
    public void getMainHtml(final SharedPreferences.Editor editor, final String startpageImageUrl) {
        // TODO Auto-generated method stub
         new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream inputStream = null;
                URL url = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(startpageImageUrl);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);

                        }
                        // String newsHtml = response.toString();
                        // System.out.println(response.toString());
                        // getDataFromHtml(newsHtml);

//						System.out.println(response.toString());


                        String secondUrl = getSubUrlFormHtml(response.toString());
                        getHtmlFromSecondUrl(editor,secondUrl);

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }


        }).start();

    }

    private void getHtmlFromSecondUrl(final SharedPreferences.Editor editor,final String secondUrl){
        new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream inputStream = null;
                URL url = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(secondUrl);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);

                        }
                        // String newsHtml = response.toString();
                        // System.out.println(response.toString());
                        // getDataFromHtml(newsHtml);

//						System.out.println(response.toString());


                        String secondUrl = getSecondUrlFromHtml(response.toString());
//                        System.out.println(response.toString());
//                        System.out.println(secondUrl);
//						getSecondUrlFromHtml(secondUrl);

                        getImageHtml(editor,secondUrl);

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }

        }).start();
    }

    private void getImageHtml(final SharedPreferences.Editor editor, final String secondUrl) {
        // TODO Auto-generated method stub
        new Thread(new Runnable() {

            @Override
            public void run() {
                InputStream inputStream = null;
                URL url = null;
                HttpURLConnection httpURLConnection = null;
                try {
                    url = new URL(secondUrl);

                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setReadTimeout(5000);
                    httpURLConnection.setConnectTimeout(5000);

                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        inputStream = httpURLConnection.getInputStream();

                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                        String line;
                        StringBuilder response = new StringBuilder();

                        while ((line = reader.readLine()) != null) {
                            response.append(line);

                        }
                        // String newsHtml = response.toString();
                        // System.out.println(response.toString());
                        // getDataFromHtml(newsHtml);

//						System.out.println(response.toString());

                        System.out.println(response.toString());
                        String imageUrl = getImageUrlFromHtml(response.toString());
                        editor.putString("start_page_image_url",imageUrl);
//                        ActivityStart.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                imageUrlStr=imageUrl;
//                            }
//                        });

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }

        }).start();
    }

    private String getImageUrlFromHtml(String html){
        Document document = Jsoup.parse(html);
        Element element = document.getElementById("bigImg");
        String imageUrl = element.attr("src");
//        System.err.println(imageUrl);
        String image1920=imageUrl.replace("320x510", "1080x1920");
//        System.err.println(image1920);
//        return imageUrl;
        return image1920;
    }

    private String getSecondUrlFromHtml(String html){
        Document document = Jsoup.parse(html);
        Elements elements = document.getElementById("img1").getElementsByTag("a");


        Random random = new Random();

        int select = random.nextInt(elements.size())%(elements.size()-0+1)+0;


        return "http://sj.zol.com.cn"+elements.get(select).attr("href");

    }

    private String getSubUrlFormHtml(String string) {
        // TODO Auto-generated method stub
        Document document = Jsoup.parse(string);
        Elements elements = document.getElementsByClass("pic");
        Random random = new Random();

        int select = random.nextInt(elements.size())%(elements.size()-0+1)+0;
//		System.err.println(select);
        return "http://sj.zol.com.cn"+elements.get(select).select("a").attr("href");
    }
    /***************************结束分割线*************************************************/
}
