package com.ccnode.codegenerator.util;

import com.rits.cloning.Cloner;

/**
 * @Author bruce.ge
 * @Date 2017/2/20
 * @Description
 */
public class MyCloner {
    private static MyCloner myCloner;

    private MyCloner() {
    }

    public static Cloner getCloner() {
        return HelperHolder.myCloner;
    }

    private static class HelperHolder {
        private static final Cloner myCloner =
                new Cloner();
    }
}
