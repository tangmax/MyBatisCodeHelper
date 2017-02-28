package com.ccnode.codegenerator.util;

import javax.swing.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/22
 * @Description
 */
public class IconUtils {
    private static ImageIcon methodIcon = new ImageIcon(IconUtils.class.getClassLoader().getResource("icon/mybatis.png"));

    private static ImageIcon xmlIcon = new ImageIcon(IconUtils.class.getClassLoader().getResource("icon/mybatis-ns.png"));

    public static Icon useMyBatisIcon() {
        return methodIcon;
    }

    public static Icon useXmlIcon() {
        return xmlIcon;
    }
}
