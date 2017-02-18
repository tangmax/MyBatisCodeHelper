package com.ccnode.codegenerator.view;

import com.ccnode.codegenerator.constants.MyBatisXmlConstants;
import com.ccnode.codegenerator.reference.PsiResultMapSqlReference;
import com.ccnode.codegenerator.reference.PsiXmlAttributeReference;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.xml.XmlAttribute;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import com.intellij.util.ProcessingContext;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import static com.intellij.patterns.XmlPatterns.xmlAttributeValue;

/**
 * @Author bruce.ge
 * @Date 2017/2/16
 * @Description
 */
public class MyBatisXmlAttributeReferenceContributor extends PsiReferenceContributor {
    private static Map<String, String> refTagNameToDeclarationTagName = new HashMap<String, String>() {{
        put(MyBatisXmlConstants.RESULTMAP, MyBatisXmlConstants.RESULTMAP);
        put(MyBatisXmlConstants.REFID, MyBatisXmlConstants.SQL);
    }};


    private static Map<String, String> declareTagNameToUsageTagName = new HashMap<String, String>() {{
        put(MyBatisXmlConstants.RESULTMAP, MyBatisXmlConstants.SELECT);
        put(MyBatisXmlConstants.SQL, MyBatisXmlConstants.INCLUDE);
    }};


    private static Map<String, String> tagNameToAttributeName = new HashMap<String, String>() {{
        put(MyBatisXmlConstants.RESULTMAP, MyBatisXmlConstants.RESULTMAP);
        put(MyBatisXmlConstants.SQL, MyBatisXmlConstants.REFID);
    }};

    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
        registrar.registerReferenceProvider(xmlAttributeValue(),
                new PsiReferenceProvider() {
                    @NotNull
                    @Override
                    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context) {
                        PsiElement parent1 = element.getParent();
                        if (!(parent1 instanceof XmlAttribute)) {
                            return PsiReference.EMPTY_ARRAY;
                        }
                        XmlAttribute parent = (XmlAttribute) parent1;
                        String name = parent.getName();
                        if (refTagNameToDeclarationTagName.containsKey(name)) {
                            //check the file is mapper file.
                            XmlTag selectTag = parent.getParent();
                            XmlDocument xmlDocument = PsiTreeUtil.getParentOfType(selectTag, XmlDocument.class);
                            if (xmlDocument == null || xmlDocument.getRootTag() == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }

                            XmlTag[] subTags = xmlDocument.getRootTag().getSubTags();
                            if (subTags.length == 0) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            String value = parent.getValue();
                            if (StringUtils.isBlank(value)) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            XmlTag findSubTag = null;
                            for (XmlTag subTag : subTags) {
                                if (subTag.getName().equals(refTagNameToDeclarationTagName.get(name))) {
                                    String subTagValue = subTag.getAttributeValue("id");
                                    if (subTagValue != null && subTagValue.equals(value)) {
                                        findSubTag = subTag;
                                        break;
                                    }
                                }
                            }
                            if (findSubTag == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            return new PsiReference[]{new PsiXmlAttributeReference((XmlAttributeValue) element, findSubTag.getAttribute("id").getValueElement()),
                                /*new PsiXmlAttributeReference((XmlAttributeValue) element, findSubTag.getAttribute("type").getValueElement())*/
                                /*,new PsiXmlAttributeReference(findSubTag.getAttribute("id").getValueElement(),(XmlAttributeValue) element )*/
                            };
                        } else if (name.equals("id")) {
                            XmlTag parentTag = parent.getParent();
                            if (parentTag == null) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            if (!declareTagNameToUsageTagName.containsKey(parentTag.getName())) {
                                return PsiReference.EMPTY_ARRAY;
                            }

                            String tagIdValue = parent.getValue();
                            if (StringUtils.isBlank(tagIdValue)) {
                                return PsiReference.EMPTY_ARRAY;
                            }
                            return new PsiReference[]{new PsiResultMapSqlReference((XmlAttributeValue) element, (XmlAttributeValue) element)};
//
//                            XmlDocument xmlDocument = PsiTreeUtil.getParentOfType(parentTag, XmlDocument.class);
//                            if (xmlDocument == null || xmlDocument.getRootTag() == null) {
//                                return PsiReference.EMPTY_ARRAY;
//                            }
//                            List<XmlTag> xmlAttributeOfType = MyPsiXmlUtils.getXmlAttributeOfType(xmlDocument, declareTagNameToUsageTagName.get(parentTag.getName()));
//                            if (xmlAttributeOfType.size() == 0) {
//                                return PsiReference.EMPTY_ARRAY;
//                            }
//                            List<XmlAttributeValue> attributeValues = Lists.newArrayList();
//                            for (XmlTag tag : xmlAttributeOfType) {
//                                String s = tag.getAttributeValue(tagNameToAttributeName.get(parentTag.getName()));
//                                if (s != null && s.equals(tagIdValue)) {
//                                    attributeValues.add(tag.getAttribute(tagNameToAttributeName.get(parentTag.getName())).getValueElement());
//                                }
//                            }
//
//                            if (attributeValues.size() > 0) {
////                                PsiReference[] psiReferences = new PsiReference[attributeValues.size()];
////                                for (int i = 0; i < attributeValues.size(); i++) {
////                                    psiReferences[i] = new PsiResultMapSqlReference((XmlAttributeValue) element, attributeValues.get(i));
////                                }
////                                return psiReferences;
////                                return PsiReference.EMPTY_ARRAY;
//
//                            } else {
//                                return PsiReference.EMPTY_ARRAY;
                        } else {
                            return PsiReference.EMPTY_ARRAY;
                        }
                    }
                });
    }
}
