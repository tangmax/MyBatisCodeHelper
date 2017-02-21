package com.ccnode.codegenerator.view.completion;

import com.ccnode.codegenerator.constants.MyBatisXmlConstants;
import com.ccnode.codegenerator.util.PsiClassUtil;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.CompletionType;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.psi.xml.XmlTag;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/21
 * @Description
 */

/*completion for mybatis xml property, if test, refid, resultMap ect*/
public class MapperXmlTagElementCompletionContributor extends CompletionContributor {

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result) {
        if (parameters.getCompletionType() != CompletionType.BASIC) {
            return;
        }
        PsiElement element = parameters.getPosition();
        PsiElement parent = element.getParent();
        if (parent == null || !(parent instanceof XmlAttributeValue)) {
            return;
        }

        XmlAttributeValue attributeValue = (XmlAttributeValue) parent;
        PsiElement parent1 = attributeValue.getParent();
        if (!(parent1 instanceof XmlAttribute)) {
            return;
        }
        XmlAttribute attribute = (XmlAttribute) parent1;
        String name = attribute.getName();
        String startText = parameters.getOriginalPosition().getText();
        if (name.equals(MyBatisXmlConstants.PROPERTY)) {
            //do something
            handleWithPropertyComplete(result, element, attribute, startText);
        } else if (name.equals(MyBatisXmlConstants.REFID)) {
            handleWithRefidComplete(parameters, result, attribute, startText);
        } else if (name.equals(MyBatisXmlConstants.RESULTMAP)) {
            handleWithResultMap(parameters, result, attribute, startText);
        } else if (name.equals(MyBatisXmlConstants.TEST)) {

        } else if (name.equals(MyBatisXmlConstants.ID)) {
            //check if is was insert or select or update, solve it from dao interface methodName.
            handleWithMethodNameId(parameters, result, element, attribute, startText);
        }
    }

    private void handleWithMethodNameId(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, PsiElement element, XmlAttribute attribute, String startText) {
        XmlTag tag = attribute.getParent();
        if (tag == null) {
            return;
        }
        if (!MyBatisXmlConstants.mapperMethodSet.contains(tag.getName())) {
            return;
        }
        //find all the method name from interface method
        PsiFile originalFile =
                parameters.getOriginalFile();
        if (!(originalFile instanceof XmlFile)) {
            return;
        }
        XmlFile xmlFil = (XmlFile) originalFile;
        XmlTag rootTag = xmlFil.getRootTag();
        if (rootTag == null) {
            return;
        }
        String namespace = rootTag.getAttributeValue(MyBatisXmlConstants.NAMESPACE);
        if (StringUtils.isBlank(namespace)) {
            return;
        }

        PsiClass classOfQuatifiedType = PsiClassUtil.findClassOfQuatifiedType(element, namespace);
        if (classOfQuatifiedType == null || !(classOfQuatifiedType.isInterface())) {
            return;
        }

        PsiMethod[] allMethods = classOfQuatifiedType.getAllMethods();
        List<String> methodNames = Lists.newArrayList();
        for (PsiMethod allMethod : allMethods) {
            methodNames.add(allMethod.getName());
        }
        for (String methodName : methodNames) {
            if (methodName.startsWith(startText)) {
                result.addElement(LookupElementBuilder.create(methodName));
            }
        }
    }

    private void handleWithResultMap(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, XmlAttribute attribute, String startText) {
        XmlTag tag = attribute.getParent();
        if (tag == null) {
            return;
        }
        if (!(tag.getName().equals(MyBatisXmlConstants.SELECT))) {
            return;
        }
        PsiFile originalFile =
                parameters.getOriginalFile();
        if (!(originalFile instanceof XmlFile)) {
            return;
        }
        XmlFile xmlFil = (XmlFile) originalFile;
        XmlTag rootTag = xmlFil.getRootTag();
        if (rootTag == null) {
            return;
        }
        XmlTag[] subTags = rootTag.getSubTags();
        List<String> reslutMapIds = Lists.newArrayList();
        for (XmlTag subTag : subTags) {
            if (subTag.getName().equals(MyBatisXmlConstants.RESULTMAP)) {
                String resultMapId = subTag.getAttributeValue(MyBatisXmlConstants.ID);
                if (StringUtils.isNotBlank(resultMapId)) {
                    reslutMapIds.add(resultMapId);
                }
            }
        }

        for (String resultMapId : reslutMapIds) {
            if (resultMapId.startsWith(startText)) {
                result.addElement(LookupElementBuilder.create(resultMapId));
            }
        }
    }

    private void handleWithRefidComplete(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, XmlAttribute attribute, String startText) {
        XmlTag tag = attribute.getParent();
        if (tag == null) {
            return;
        }
        if (!(tag.getName().equals(MyBatisXmlConstants.INCLUDE))) {
            return;
        }
        PsiFile originalFile =
                parameters.getOriginalFile();
        if (!(originalFile instanceof XmlFile)) {
            return;
        }
        XmlFile xmlFil = (XmlFile) originalFile;
        XmlTag rootTag = xmlFil.getRootTag();
        if (rootTag == null) {
            return;
        }
        XmlTag[] subTags = rootTag.getSubTags();
        List<String> sqls = Lists.newArrayList();
        for (XmlTag subTag : subTags) {
            if (subTag.getName().equals(MyBatisXmlConstants.SQL)) {
                String sqlId = subTag.getAttributeValue(MyBatisXmlConstants.ID);
                if (StringUtils.isNotBlank(sqlId)) {
                    sqls.add(sqlId);
                }
            }
        }

        for (String sql : sqls) {
            if (sql.startsWith(startText)) {
                result.addElement(LookupElementBuilder.create(sql));
            }
        }
    }

    private void handleWithPropertyComplete(@NotNull CompletionResultSet result, PsiElement element, XmlAttribute attribute, String startText) {
        XmlTag tag = attribute.getParent();
        if (tag == null) {
            return;
        }
        if (!(tag.getName().equals(MyBatisXmlConstants.RESULT))) {
            return;
        }
        PsiElement parent2 = tag.getParent();
        if (!(parent2 instanceof XmlTag)) {
            return;
        }
        XmlTag resultMapTag = (XmlTag) parent2;
        if (!(resultMapTag.getName().equals(MyBatisXmlConstants.RESULTMAP))) {
            return;
        }
        String resultTypeValue = resultMapTag.getAttributeValue(MyBatisXmlConstants.TYPE);
        if (StringUtils.isBlank(resultTypeValue)) {
            return;
        }
        //then go search the resultType entity.
        PsiClass findedClass = PsiClassUtil.findClassOfQuatifiedType(element, resultTypeValue);
        if (findedClass == null) return;
        List<String> props =
                PsiClassUtil.extractProps(findedClass);


        for (String prop : props) {
            if (prop.startsWith(startText)) {
                result.addElement(LookupElementBuilder.create(prop));
            }
        }
    }

}
