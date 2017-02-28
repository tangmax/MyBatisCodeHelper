package com.ccnode.codegenerator.myconfigurable;


import com.ccnode.codegenerator.util.MyCloner;

/**
 * @Author bruce.ge
 * @Date 2017/2/20
 * @Description
 */
public class PluginState extends DomainObject implements Cloneable {
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    protected PluginState clone(){
        return MyCloner.getCloner().deepClone(this);
    }

    public Profile getDefaultProfile(){
        if(profile!=null){
            return profile;
        } else {
            profile = new Profile();
            profile.setDatabase(DataBaseConstants.MYSQL);
            profile.setAddMapperAnnotation(true);
            return profile;
        }
    }
}
