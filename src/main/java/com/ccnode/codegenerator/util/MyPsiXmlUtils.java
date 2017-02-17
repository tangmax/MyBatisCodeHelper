package com.ccnode.codegenerator.util;

import com.google.common.collect.Lists;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlTag;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

/**
 * @Author bruce.ge
 * @Date 2017/2/16
 * @Description
 */
public class MyPsiXmlUtils {
    @NotNull
    public static List<XmlTag> getXmlAttributeOfType(XmlDocument xmlDocument, Set<String> tagNames) {
        List<XmlTag> values = Lists.newArrayList();
        XmlTag rootTag = xmlDocument.getRootTag();
        if (rootTag == null) {
            return values;
        }
        if (tagNames.contains(rootTag.getName())) {
            values.add(rootTag);
        }
        addTagsToList(rootTag, tagNames, values);
        return values;
    }

    @NotNull
    public static List<XmlTag> getXmlAttributeOfType(XmlDocument xmlDocument, String tagName) {
        List<XmlTag> values = Lists.newArrayList();
        XmlTag rootTag = xmlDocument.getRootTag();
        if (rootTag == null) {
            return values;
        }
        if (tagName.equals(rootTag.getName())) {
            values.add(rootTag);
        }
        addTagsToList(rootTag, tagName, values);
        return values;
    }

    private static void addTagsToList(XmlTag rootTag, Set<String> tagNames, List<XmlTag> values) {
        XmlTag[] subTags = rootTag.getSubTags();
        if (subTags.length == 0) {
            return;
        }
        for (XmlTag subTag : subTags) {
            if (tagNames.contains(subTag.getName())) {
                values.add(subTag);
            }
            addTagsToList(subTag, tagNames, values);
        }
    }


    private static void addTagsToList(XmlTag rootTag, String tagName, List<XmlTag> values) {
        XmlTag[] subTags = rootTag.getSubTags();
        if (subTags.length == 0) {
            return;
        }
        for (XmlTag subTag : subTags) {
            if (tagName.equals(subTag.getName())) {
                values.add(subTag);
            }
            addTagsToList(subTag, tagName, values);
        }
    }
}
