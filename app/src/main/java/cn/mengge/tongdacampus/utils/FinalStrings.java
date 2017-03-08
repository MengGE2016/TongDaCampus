package cn.mengge.tongdacampus.utils;

/**
 * Created by MengGE on 2016/10/27.
 */
public class FinalStrings {
    public static class AuthorInfoStrings {
        public static final String TEL = "15706299377";
        public static final String QQ = "616173102";
        public static final String NAME = "杜锦波";
        public static final String UNIVERSTITY = "南通大学杏林学院";
    }

    public static class ActionStrings {
        public static final String SEMD_MESSAGE = "SENT_SMS_ACTION";
    }

    public static class ApplicationStrings {
        public final static int PROGRAM_NORMAL_EXIT_CODE = 0;
        public final static int PROGRAM_EXIT_BACK_PRESSED_TIME = 2000;
    }

    public static class ColorStrins {
        public final static String COLOR_WHITE = "#ffffff";
        public final static String COLOR_BLACK = "#000000";
    }


    public static class PrefServerStrings {
        public final static String GUEST_PREF = "guests_pref";
        public final static String GUEST_NUM = "guests_num";
        public final static String GUEST_SFZH = "guests_sfzh";
        public final static String GUEST_PASSORD = "guest_password";
        public final static String GUEST_COOKIE = "guests_cookie";
        public final static String GUEST_SQL_NAME = "guest_db";

        public final static String STUDENT_FIRST_LOGIN = "first_login";
        public final static String STUDENT_PREF = "PERSONAL_PREF";
        public final static String COOKIE = "cookie";
        public final static String STUDENT_NUM = "student_num";
        public final static String STUDENT_SFZH = "student_sfzh";
        public final static String STUDENT_JWGL_PASSWORD = "student_jwgl_passwd";
        public final static String STUDENT_SQL_NAME = "student_personal_db";
        public final static String STUDENT_SQL_NAME_DEFAULT = "0";
    }

    public static class CustomWidgetIdStrings {
        //主界面抽屉中的课表中ListView的Id
        public final static int drawerLlForClassLvId = 999999999;
    }

    public static class SQLServerStrings {
        public final static int SQL_VERSION = 3;
        public final static String SQL_NAME = ".db";
        public final static String SQL_NAME_GUESTS = "GuestsAndTempData.db";
        public final static String GRADE_TABLE_NAME = "gradeTb";
        public final static String CLASS_TABLE_NAME = "classTableTb";
        public final static String TEMP_GRADE_TABLE_NAME = "tempGradeTb";
        public final static String TEMP_CLASS_TABLE_NAME = "tempClassTb";
        public final static String EMPTY_CLASSROOM_TABLE_NAME = "emptyClassRoomTb";

        public final static String CREATE_TEMP_GRADE_TABLE = "create table "
                + TEMP_GRADE_TABLE_NAME
                + " (id integer primary key autoincrement, "
                + "kcmc text, jsxm text, xq text, xs text, xf text, zpcj text, pscj text, qmcj text, kcsx text, cjid text, ksfsm text, pxcj text)";

        public final static String CREATE_GRADE_TABLE = "create table "
                + GRADE_TABLE_NAME
                + " (id integer primary key autoincrement, "
                + "kcmc text, jsxm text, xq text, xs text, xf text, zpcj text, pscj text, qmcj text, kcsx text, cjid text, ksfsm text, pxcj text)";
        public final static String GRADE_KCMC = "kcmc";
        public final static String GRADE_JSXM = "jsxm";
        public final static String GRADE_XQ = "xq";
        public final static String GRADE_XS = "xs";
        public final static String GRADE_XF = "xf";
        public final static String GRADE_ZPCJ = "zpcj";
        public final static String GRADE_PSCJ = "pscj";
        public final static String GRADE_QMCJ = "qmcj";
        public final static String GRADE_KCSX = "kcsx";
        public final static String GRADE_CJID = "cjid";
        public final static String GRADE_KSFSM = "ksfsm";
        public final static String GRADE_PXCJ = "pxcj";

        public final static String CREATE_CLASS_TABLE_TABLE = "create table " +
                CLASS_TABLE_NAME +
                " (id integer primary key autoincrement, xqj1 text,  xqj2 text, xqj3 text, xqj4 text, xqj5 text, xqj6 text, xqj7 text)";
        public final static String CREATE_TEMP_CLASS_TABLE_TABLE = "create table " +
                TEMP_CLASS_TABLE_NAME +
                " (id integer primary key autoincrement, xqj1 text,  xqj2 text, xqj3 text, xqj4 text, xqj5 text, xqj6 text, xqj7 text)";

        public final static String CREATE_EMPTY_CLASSROOM_TABLE = "create table " +
                EMPTY_CLASSROOM_TABLE_NAME +
                " (id integer primary key autoincrement, xqj1 text,  xqj2 text, xqj3 text, xqj4 text, xqj5 text, xqj6 text, xqj7 text)";
        public final static String CLASS_TABLE_XQJ1 = "xqj1";
        public final static String CLASS_TABLE_XQJ2 = "xqj2";
        public final static String CLASS_TABLE_XQJ3 = "xqj3";
        public final static String CLASS_TABLE_XQJ4 = "xqj4";
        public final static String CLASS_TABLE_XQJ5 = "xqj5";
        public final static String CLASS_TABLE_XQJ6 = "xqj6";
        public final static String CLASS_TABLE_XQJ7 = "xqj7";
        public final static String CLASS_TABLE_TABLE_NAME = "classTableTb";
    }

    public static class FileSreverStrings {
        //SD卡的主文件夹
        public final static String MAIN_DIR = "TongDaCapmus";
        //图片缓存文件夹
        public final static String IMAGE_CACHE_DIR = "TongDaCapmus/cache/image_cache";
        //缓存文件夹
        public final static String CACHE_DIR = "TongDaCapmus/cache";
        //图片保存文件夹
        public final static String IMAGE_DIR = "TongDaCapmus/tdc_Image";
    }

    public static class CommonStrings {
        public final static String LOGIN_LOGINING = "正在登录......";
        public final static String LOGIN_SUCCESS = "登录成功";
        public final static String LOGIN_FAILED = "登录失败";
        public final static String GET_CONTENT_ING = "正在获取内容......";
        public final static String REFRESHING = "刷新中......";
        public final static String INFO_ERROR = "请输入正确的信息";
        public final static String CHECK_NETWORK_CONNECTION_STATE = "请检查网络是否连接";
        public final static String MAKE_SURE = "确定";
        public static final String IS_EXIT = "确认要退出吗?";
        public final static String CANCEL = "取消";
        public final static String YES = "是";
        public final static String NO = "否";
        public final static String I_KNOWN = "我知道了";
        public final static String PLEASE_WAITE = "请稍候......";
        public final static String VERSION = "版本:";
        public final static String STORE_SUCCESS = "保存成功";
        public final static String GET_CONTENT_FAILED = "获取失败";
        public final static String STORE = "保存";
        public final static String AUTHOR_INFO = "作者信息";
        public final static String AUTHOR_INFO_CONTENT = "班级：计143（杏）\n姓名：MengGE（杜锦波）\n学号：1413023125\n";
        public final static String PRESS_AGAIN_TO_EXIT_PRO = "再按一次返回键退出程序";
    }

    public static class NetStrings {

        public final static String URL_STARTPAGE_IMAGE = "http://sj.zol.com.cn/bizhi/fengjing/1080x1920_p2/good_1.html";

        /**
         * Net
         **/
        public final static int NET_TIME_OUT = 8000;
        public final static String NET_REQUEST_METHOD_GET = "GET";
        public final static String NET_REQUEST_METHOD_POST = "POST";
        public final static String NET_DATA_CHARSET_UTF_8 = "utf-8";
        public final static String NET_DATA_CHARSET_GBK = "GBK";
        //南通大学官网
        public final static String URL_OFFICIAL_URL = "http://www.ntu.edu.cn/";

        //教务管理HOST
        public final static String REQUEST_HOST = "Host";
        public final static String REQUEST_HOST_VALUE_JWGL = "jwgl.ntu.edu.cn";

        public final static String REQUEST_DNT = "DNT";
        public final static String REQUEST_DNT_VALUE_JWGL = "1";

        public final static String REQUEST_XREQUESTEDWITH = "X-Requested-With";
        public final static String REQUEST_XREQUESTEDWITH_VALUE_JWGL = "XMLHttpRequest";


        /**
         * 通大新闻
         **/
        // Toolbar主标题显示标志
        public final static int NEWS_TDYW = 1;
        public final static int NEWS_XWDT = 2;
        public final static int NEWS_XYSH = 3;
        public final static int NEWS_ZTBD = 4;
        public final static int NEWS_MTTD = 5;
        public final static int NEWS_TZGG = 6;
        public final static int NEWS_TDXW = 7;
        public final static int ZBXX_ZBXX = 8;
        //通大要闻
        public final static String URL_TDYW = "http://news.ntu.edu.cn/xww/tdyw/tdyw.html";
        //新闻动态
        public final static String URL_XWDT = "http://news.ntu.edu.cn/xww/xwdt/xwdt.html";
        //校园生活
        public final static String URL_XYSH = "http://news.ntu.edu.cn/xww/xysh/xysh.html";
        //专题报道
        public final static String URL_ZTBD = "http://news.ntu.edu.cn/xww/ztbd/ztbd.html";
        //媒体通大
        public final static String URL_MTTD = "http://news.ntu.edu.cn/xww/mttd/mttd.html";
        //通知公告
        public final static String URL_TZGG = "http://www.ntu.edu.cn/zzy/tzgg/tzgg.html";

        /**
         * 教务管理
         **/
        //教务规定
        public final static String URL_JWGD = "http://jwgl.ntu.edu.cn/jwgd.aspx";
        //教务信息
        public final static String URL_JWXX = "http://jwgl.ntu.edu.cn/jwxx.aspx";
        //教务管理主界面
        public final static String URL_JWGL = "http://jwgl.ntu.edu.cn/";
        //获取校区页面
        public final static String URL_XIAO_QU = "http://jwgl.ntu.edu.cn/cjcx/ClassRoomTable.aspx";
        //获取校区的JSON数据
        public final static String URL_XIAO_QU_JSON = "http://jwgl.ntu.edu.cn/cjcx/Data/Basis/campus.aspx?_dc=1478249567537";
        //获取一个校区的教室JSON数据
        public final static String URL_CLASSROOM_JSON = "http://jwgl.ntu.edu.cn/cjcx/Data/Basis/classroom.aspx";
        //获取教室的使用情况
        public final static String URL_USE_CLASSROOM_JSON = "http://jwgl.ntu.edu.cn/cjcx/Data/Table/ClassRoomTableData.aspx";
        //获取某学院的班级信息
        public final static String URL_CLASSTABLE = "http://jwgl.ntu.edu.cn/cjcx/ClassTable.aspx";
        //获取某班级的课表信息
        public final static String URL_CLASS_CLASS = "http://jwgl.ntu.edu.cn/cjcx/Data/Basis/class.aspx";
        //教务管理登录界面的验证码和Cookie的获取链接
        public final static String URL_CHECK_CODE_COOKIE = "http://jwgl.ntu.edu.cn/cjcx/checkImage.aspx";
        //登录教务管理系统的登录界面
        public final static String URL_JWGL_LOGIN = "http://jwgl.ntu.edu.cn/cjcx/Default.aspx";
        //学生信息主页
        public final static String URL_STUDENT_MAIN = "http://jwgl.ntu.edu.cn/cjcx/Main.aspx";
        //学生冲抵成绩查询
        public final static String URL_STUDENT_FINAL_GRADE_QUERY = "http://jwgl.ntu.edu.cn/cjcx/Data/ScoreOffsetData.aspx";
        //学生成绩查询
        public final static String URL_STUDENT_GRADE_QUERY = "http://jwgl.ntu.edu.cn/cjcx/Data/ScoreAllData.aspx";
        //登录教务管理要提交的数据POST数据
        public final static String VIEW_STATE = "__VIEWSTATE=/wEPDwUJODExMDE5NzY5ZGRgtUdRucUbXsT8g55XmVsTwV6PMw==&__VIEWSTATEGENERATOR=6C0FF253";

        /**
         * 招标信息
         **/
        //招标信息网页
        public final static String URL_ZBXX = "http://ztb.ntu.edu.cn/ztb/zbxx/zbxx.html";
        //中标公示网页
        public final static String URL_ZBGG = "http://ztb.ntu.edu.cn/ztb/zbgs/zbgs.html";
        //网页中的链接前加的主链接
        public final static String URL_ZB_ADD = "http://ztb.ntu.edu.cn/";
        //Toolbar中的标题显示标志(招标信息或中标公示)
        public final static boolean FLAG_ZBXX = false;
        public final static boolean FLAG_ZBGG = true;

        /**
         * 关于我们界面的链接
         **/
        //关于我们界面的Handler Message的值
        public final static int FLAG_WE_CHAT = 0;
        public final static int FlAG_SINA_WB = 1;
        public final static int FLAG_SCHOOL_CALENDAR = 2;
        //保存到SD卡中的文件名
        public final static String NTU_WE_CHAT_IMG_FILE_NAME = "南通大学官方微信.jpg";
        public final static String NTU_SINA_WB_IMG_FILE_NAME = "南通大学官方微博.jpg";
        public final static String NTU_SCHOOL_CAL_IMG_FILE_NAME = "南通大学校历.jpg";
        //对话框的标题
        public final static String NTU_WE_CHAT_IMG_DIALOG_TITLE = "南通大学官方微信";
        public final static String NTU_SINA_WB_IMG_DIALOG_TITLE = "南通大学官方微博";
        public final static String NTU_SCHOOL_CAL_IMG_DIALOG_TITLE = "南通大学校历";

        //南通大学微信二维码
        public final static String URL_WE_CHAT_IMG = "http://www.ntu.edu.cn/zzy/images/wx.jpg";
        //南通大学微博二维码
        public final static String URL_SINA_WB_LMG = "http://www.ntu.edu.cn/zzy/images/wb.jpg";
        //南通大学校历2016-2
        public final static String URL_SCHOOL_CALENDAR_IMG = "http://www.ntu.edu.cn/zzy/images/xl2016-2.jpg";
        //南通大学校历2017-1
        public final static String URL_SCHOOL_CALENDAR_IMG_2017_1 = "http://www.ntu.edu.cn/zzy/images/xl2017-1.jpg";
    }

}
