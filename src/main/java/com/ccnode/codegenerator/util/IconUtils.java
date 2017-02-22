package com.ccnode.codegenerator.util;

import javax.swing.*;

/**
 * @Author bruce.ge
 * @Date 2017/2/22
 * @Description
 */
public class IconUtils {
    private static ImageIcon myBatisIcon = new ImageIcon(IconUtils.class.getClassLoader().getResource("icon/mybatis.png"));


    public static Icon useMyBatisIcon() {
        return myBatisIcon;
    }
}
