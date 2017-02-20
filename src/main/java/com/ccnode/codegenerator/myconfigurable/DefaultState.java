package com.ccnode.codegenerator.myconfigurable;

/**
 * @Author bruce.ge
 * @Date 2017/2/20
 * @Description
 */
public class DefaultState {
    public static Profile createDefault() {
        Profile profile = new Profile();
        profile.setDatabase(DataBaseConstants.MYSQL);
        profile.setAddMapperAnnotation(true);
        return profile;
    }
}
