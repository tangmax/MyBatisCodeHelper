package com.ccnode.codegenerator.sqlparse;

import com.ccnode.codegenerator.view.completion.MysqlCompleteCacheInteface;
import com.google.common.collect.Lists;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/3/9
 * @Description
 */
public class SqlParser {
    public static ParsedResult parse(String startText, Project project) {
        //first make it parse to a list of word.
        ParsedResult result = new ParsedResult();
        result.setRecommedValues(new ArrayList<>());
        List<String> words = extractWords(startText);
        if(words.size()==1){
            result.getRecommedValues().add("insert into");
            result.getRecommedValues().add("select");
            result.getRecommedValues().add("update");
            result.getRecommedValues().add("count");
            return result;
        }
        String lastWord = words.get(words.size() - 1);
        String lastSecondWord = words.get(words.size() - 2);
        if(lastWord.equals("from")||lastSecondWord.equals("from")){
            List<String> allTables = ServiceManager.getService(project, MysqlCompleteCacheInteface.class).getAllTables();
            result.setRecommedValues(allTables);
        }
        return result;
    }



    private static List<String> extractWords(String startText) {
        List<String> words = Lists.newArrayList();
        String m = "";
        for (int i = 0; i < startText.length(); i++) {
            char c = startText.charAt(i);
            if (c == ' ' || c == '\n' || c == '\t') {
                if (m.length() > 0) {
                    words.add(m);
                    m = "";
                }
            } else {
                m += c;
            }
        }
        if (m.length() > 0) {
            words.add(m);
        }
        return words;
    }
}
