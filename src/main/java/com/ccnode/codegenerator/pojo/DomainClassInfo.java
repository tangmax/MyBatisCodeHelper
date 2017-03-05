package com.ccnode.codegenerator.pojo;

import com.intellij.psi.PsiClass;

/**
 * @Author bruce.ge
 * @Date 2017/3/5
 * @Description
 */
public class DomainClassInfo {
    private PsiClass domainClass;

    private DomainClassSourceType domainClassSourceType;

    public PsiClass getDomainClass() {
        return domainClass;
    }

    public void setDomainClass(PsiClass domainClass) {
        this.domainClass = domainClass;
    }

    public DomainClassSourceType getDomainClassSourceType() {
        return domainClassSourceType;
    }

    public void setDomainClassSourceType(DomainClassSourceType domainClassSourceType) {
        this.domainClassSourceType = domainClassSourceType;
    }
}
