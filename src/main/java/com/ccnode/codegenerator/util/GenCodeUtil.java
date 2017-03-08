package com.ccnode.codegenerator.util;

import com.google.common.base.CaseFormat;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * What always stop you is what you always believe.
 * <p>
 * Created by zhengjun.du on 2016/05/21 22:05
 */
public class GenCodeUtil {


    public static final String ONE_RETRACT = "    ";
    public static final String TWO_RETRACT = "        ";
    public static final String THREE_RETRACT = "            ";
    public static final String FOUR_RETRACT = "                ";
    public static String MYSQL_TYPE = StringUtils.EMPTY;
    public static String PACKAGE_LINE = StringUtils.EMPTY;


    public static List<String> grapOld(@NotNull List<String> oldList,
                                       @NotNull String startKeyWord, @NotNull String endKeyWord) {
        int startIndex = -1;
        int endIndex = -1;
        int i = 0;
        for (String line : oldList) {
            if (sqlContain(line, startKeyWord)) {
                startIndex = i;
            }
            if (sqlContain(line, endKeyWord)) {
                endIndex = i;
            }
            i++;
        }
        if (startIndex == -1 || endIndex == -1) {
            return Lists.newArrayList();
        }
        return oldList.subList(startIndex, endIndex + 1);
    }


    public static String pathToPackage(String path) {
        path = path.replace("/", ".");
        path = path.replace("\\", ".");
        if (path.startsWith("src.main.java.")) {
            path = path.replace("src.main.java.", "");
        }
        if (path.startsWith("src.main.")) {
            path = path.replace("src.main.", "");
        }
        if (path.startsWith("src.")) {
            path = path.replace("src.", "");
        }
        if (path.startsWith(".")) {
            path = path.substring(1, path.length());
        }
        return path;
    }


    public static boolean sqlContain(String sequence, @NotNull String word) {
        if (StringUtils.isBlank(sequence)) {
            return false;
        }
        return StringUtils.containsIgnoreCase(
                StringUtils.deleteWhitespace(sequence),
                StringUtils.deleteWhitespace(word));
    }

    public static String getUnderScoreWithComma(String value) {
        if (value == null) {
            return StringUtils.EMPTY;
        }
        String to = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
        return "`" + to + "`";
    }


    public static String getUnderScore(String value) {
        if (value == null) {
            return StringUtils.EMPTY;
        }
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, value);
    }

    public static String getLowerCamel(String value) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, value);
    }

    public static String getUpperStart(String value){
        return value.substring(0,1).toUpperCase()+value.substring(1);
    }

    public static String getCamelFromUnderScore(String value) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, value);
    }


    public static void main(String[] args) {
//        System.out.println(deducePackage("src/main/java/com/qunar/insurance","com.qunar.insurance.annotion"));
//        System.out.println(deducePackage("src/com/java/com/qunar/insurance","com.com.com.annotion"));
//        System.out.println(deducePackage("src/com/java/com/qunar/insurance","xxx.com.com.annotion"));
    }

}
