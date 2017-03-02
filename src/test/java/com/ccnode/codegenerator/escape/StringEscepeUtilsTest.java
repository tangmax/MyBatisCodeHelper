package com.ccnode.codegenerator.escape;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;

/**
 * @Author bruce.ge
 * @Date 2017/3/2
 * @Description
 */
public class StringEscepeUtilsTest {
    @Test
    public void test(){
        String s = StringEscapeUtils.escapeHtml("<br>");
        System.out.println(s);
    }

}
