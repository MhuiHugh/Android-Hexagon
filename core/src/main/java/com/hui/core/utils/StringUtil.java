package com.hui.core.utils;

import android.app.Application;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mhui on 2016/5/27.
 */

public class StringUtil {
    public static final String PARAM_EQUAL = "=";
    public static final String PARAM_AND = "&";

    //从html中获取文本
    private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符

    /**
     * 计算收益
     *
     * @param day   天
     * @param get   年化率，包含加息
     * @param money 本金
     * @return
     */
    public static double getAllMoney(int day, double get, double money) {
        get = (money * (100 * get) * (day / 365.0f)) / 10000;
        return get;
    }

    /**
     * @param htmlStr
     * @return 删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
        Matcher m_space = p_space.matcher(htmlStr);
        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }

    /**
     * 从html中取得文本
     *
     * @param htmlStr
     * @return
     */
    public static String getTextFromHtml(String htmlStr) {
        htmlStr = delHTMLTag(htmlStr);
        htmlStr = htmlStr.replaceAll("&nbsp;", "");
        htmlStr = htmlStr.substring(0, htmlStr.indexOf("。") + 1);
        return htmlStr;
    }

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     *
     * @param content
     */
    public static void copy(String content, Context context) {
        //得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }

    /**
     * 实现粘贴功能
     * add by wangqianzhou
     *
     * @param context
     * @return
     */
    public static String paste(Context context) {
        //得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        return cmb.getText().toString().trim();
    }

    /**
     * 判断是否为空字符串
     *
     * @param src
     * @return
     */
    public static boolean isEmpty(String src) {
        return src == null || src.trim().length() == 0;
    }

    /**
     * 判断是否是空字符串(包括null, 长度为0, 只包含空格)
     *
     * @return
     */
    public static boolean isNotEmpty(String src) {
        return !isEmpty(src);
    }

    /**
     * 身份证号加星号显示，前三位和后四位正常显示，其他显示星号
     *
     * @param srcNum 原身份证号
     * @return 加密后的身份证号
     */
    public static String getMaskedIdNum(String srcNum) {
        if (StringUtil.isEmpty(srcNum) || srcNum.length() < 15) {
            return srcNum;
        }
        StringBuffer dstIdNum = new StringBuffer();

        dstIdNum.append(srcNum.substring(0, 3));
        int max = 9;
        for (int i = 0; i < max; i++) {
            dstIdNum.append('*');
        }
        dstIdNum.append(srcNum.substring(srcNum.length() - 4));

        return dstIdNum.toString();
    }

    //-------------------------------------------

    /**
     * 转换数字，转换为 “亿”，“万”
     *
     * @param num 原始数字
     * @return
     */
    public static String getMoneyFormatAbb2d(double num) {
        if (num >= 100000000) {
            return getMoneyFormat2d(num / 100000000) + "亿";
        } else if (num >= 10000) {
            return getMoneyFormat2d(num / 10000) + "万";
        } else {
            return getMoneyFormat2d(num) + "元";
        }
    }


    /**
     * 获取保留两位小数的数字字符串
     *
     * @param num 原始数字,double型
     * @return
     */
    public static String getNumFormat2d(float num) {
        return getNumberFormat("##,###,###,##0.00").format(num);
    }


    /**
     * 不保留小数
     *
     * @param num 原始数字
     * @return
     */
    public static String getMoneyFormat2d(double num) {
        return getNumberFormat("##,###,###,##0").format(num);
    }

    /**
     * 获取数字格式化实例
     *
     * @param pattern
     * @return
     */
    private static NumberFormat getNumberFormat(String pattern) {
        return new DecimalFormat(pattern);
    }
    //-------------------------------------

    /**
     * 手机号加星号显示，第四位到第七位显示“*”
     *
     * @param srcNum 原手机号
     * @return 加密后的手机号
     */
    public static String getMaskedPhone(String srcNum) {
        String dstNum = null;
        if (null != srcNum && srcNum.length() > 7) {
            dstNum = srcNum.substring(0, 3) + "****"
                    + srcNum.substring(7, srcNum.length());
        } else {
            dstNum = srcNum;
        }
        return dstNum;
    }

    /**
     * 最大百亿千分符算法
     */
    public static String getMillesimal(double str) {
        return new DecimalFormat("#,##0.00").format(str);
        //return NumberFormat.getNumberInstance().format(str);
    }

    /**
     * 千分符不保留小数
     *
     * @param str
     * @return
     */
    public static String getMillesima2(double str) {
        return new DecimalFormat("#,##0").format(str);
    }

    /**
     * 最大百亿千分符算法
     */
    public static String getMillesimal0(double str) {
        return new DecimalFormat("####0.00").format(str);
    }

    /**
     * 将千分符转double,非千分符会自动返回
     *
     * @param millesimal
     * @return
     */
    public static double getDoubleFromMillesimal(String millesimal) {
        double d = 0.0;
        millesimal = millesimal.replace(",", "");
        try {
            d = new DecimalFormat().parse(millesimal).doubleValue();
        } catch (ParseException e) {
            Log.e("StringUtil",e.toString());
        }
        return d;
    }

    /**
     * 将Double转千分符，保留2位有效小数
     *
     * @param str
     * @return
     */
    public static String getMillesimalAvailable(double str) {
        String temp = getMillesimal(str);
        if (temp.endsWith("00")) {
            temp = temp.substring(0, temp.indexOf("."));
        } else if (temp.endsWith("0")) {
            temp = temp.substring(0, temp.length() - 1);
        }
        if (temp.equals("0")) {
            temp = "";
        }
        return temp;
    }

    /**
     * Double转String 避免科学计数法
     *
     * @param d
     * @return
     */
    public static String getDoubleWithFormat(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

    /**
     * Get String UTF-8编码
     * String UTF8编码
     */
    public static String getStringToUTF8(String str) {
        String utf8Str = "";
        try {
            utf8Str = URLEncoder.encode(new String(str.getBytes("UTF-8")), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return utf8Str;
    }

    /**
     * UTF8 解码
     *
     * @param utf8Str
     */
    public static String getUTF8ToString(String utf8Str) {
        String str = "";
        try {
            str = URLDecoder.decode(utf8Str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }


    /**
     * 银行卡号加星号显示，四个星号加银行卡后四位
     *
     * @param bankName 银行名
     * @param srcNum   银行卡号
     * @return 加密后的银行卡号
     */
    public static String getMaskedCardNum(String bankName, String srcNum) {
        String dstNum = null;
        if (null != srcNum && srcNum.length() > 4) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(bankName);
            buffer.append(" （");
            buffer.append(getMaskedCardNum(srcNum));
            buffer.append("）");
            dstNum = buffer.toString();
        } else {
            dstNum = srcNum;
        }
        return dstNum;
    }

    /**
     * 银行卡号加星号显示，四个星号加银行卡后四位
     *
     * @param srcNum 银行卡号
     * @return 加密后的银行卡号
     */
    public static String getMaskedCardNum(String srcNum) {
        String dstNum = null;
        if (null != srcNum && srcNum.length() > 4) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("****" + srcNum.substring(srcNum.length() - 4));
            dstNum = buffer.toString();
        } else {
            dstNum = srcNum;
        }
        return dstNum;
    }

    /**
     * 银行卡号加星号显示，12个星 每4个空格隔开,银行卡后4位
     *
     * @param srcNum 银行卡号
     * @return 加密后的银行卡号
     */
    public static String getMaskedCardAllNum(String srcNum) {
        String dstNum = "";
        if (StringUtil.isNotEmpty(srcNum) && srcNum.length() > 4) {
            dstNum = "****\t****\t****\t" + srcNum.substring(srcNum.length() - 4);
        } else {
            dstNum = srcNum;
        }
        return dstNum;
    }

    /**
     * 银行卡号保留前后4位，中间加星号
     *
     * @param card
     * @return
     */
    public static String getMaskedCardId(String card) {
        String dstNum = "";
        if (null != card && card.length() > 8) {
            int tl = card.length() - 8;
            StringBuffer strb = new StringBuffer();
            for (int i = 0; i < tl; i++) {
                strb.append("*");
            }
            dstNum = card.substring(0, 4) + strb.toString() + card.substring(card.length() - 4, card.length());
        }
        return dstNum;
    }

    /**
     * 银行卡号保留后4位，每4位加一个空格
     *
     * @param card
     * @return
     */
    public static String getMaskedBankNum(String card) {
        String dstNum = "";
        if (null != card && card.length() > 8) {
            int tl = card.length() - 4;
            StringBuffer strb = new StringBuffer();
            for (int i = 1; i < tl + 1; i++) {
                strb.append("*");
                if (i != 1 && i % 4 == 0) {
                    strb.append("\t");
                }
            }
            dstNum = strb.toString() + "\t" + card.substring(card.length() - 4, card.length());
        }
        return dstNum;
    }

    /**
     * 转换数字，保留两位小数，转换为 “亿元”，“万元”， “元”
     *
     * @param num 原始数字
     * @return
     */
    public static String getNumUnit(double num) {
        if (num >= 100000000) {
            return getMillesimalAvailable(num / 100000000) + "亿";
        } else if (num >= 10000) {
            return getMillesimalAvailable(num / 10000) + "万";
        } else {
            return getMillesimalAvailable(num);
        }
    }

    /**
     * 从asset路径下读取对应文件转String输出
     *
     * @return
     */
    public static String readStrFromAsset(String fileName, Application application) {
        StringBuilder sb = new StringBuilder();
        AssetManager am = application.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
