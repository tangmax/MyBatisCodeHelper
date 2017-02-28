package com.ccnode.codegenerator.enums;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/08/31 16:39
 */
public class UrlManager {

    //    private static String GENERATOR_URL = "http://www.codehelper.me/generator/";
    private static String MAIN_PAGE = "https://github.com/gejun123456/MyBatisCodeHelper";

    // TODO: 2017/2/3 need fix?
    private static String POST_URL = "http://www.codehelper.me/generator/post";

    public static String getUrlSuffix() {
        return "";
    }

    public static String getMainPage() {
        return MAIN_PAGE;
    }


    public static String getPostUrl() {
        return POST_URL + getUrlSuffix();
    }


}
