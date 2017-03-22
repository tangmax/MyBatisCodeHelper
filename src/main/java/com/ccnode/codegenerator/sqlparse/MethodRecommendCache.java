package com.ccnode.codegenerator.sqlparse;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.InsertHandler;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.codeInsight.lookup.LookupElementRenderer;

import java.util.List;


/**
 * @Author bruce.ge
 * @Date 2017/3/21
 * @Description
 */
public class MethodRecommendCache {

    private static String[] sqlMethods = {"ABS", "COUNT", "DISTINCT", "AVG", "COUNT", "DISTINCT", "FIRST", "FORMAT", "LAST",
            "LCASE", "LEN", "MAX", "MIN", "MID", "NOW", "ROUND", "SUM",
            "TOP", "UCASE"};

    public static List<LookupElement> getRecommends(String currentWordStart) {
        List<LookupElement> methodRecommends = Lists.newArrayList();
        for (String sqlMethod : sqlMethods) {
            methodRecommends.add(LookupElementBuilder.create(currentWordStart + sqlMethod + "()").withRenderer(new LookupElementRenderer<LookupElement>() {
                @Override
                public void renderElement(LookupElement element, LookupElementPresentation presentation) {
                    presentation.setItemText(sqlMethod);
                    presentation.setTypeText("function");
                }
            }).withInsertHandler(new InsertHandler<LookupElement>() {
                @Override
                public void handleInsert(InsertionContext context, LookupElement item) {
                    context.getEditor().getCaretModel().moveToOffset(context.getTailOffset() - 1);
                }
            }));
        }
        return methodRecommends;
    }

}
