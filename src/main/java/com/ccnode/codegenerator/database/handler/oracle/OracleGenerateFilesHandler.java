package com.ccnode.codegenerator.database.handler.oracle;

import com.ccnode.codegenerator.database.ClassValidateResult;
import com.ccnode.codegenerator.database.handler.FieldValidator;
import com.ccnode.codegenerator.database.handler.GenerateFileHandler;
import com.ccnode.codegenerator.database.handler.HandlerValidator;
import com.ccnode.codegenerator.dialog.GenCodeProp;
import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.ccnode.codegenerator.util.DateUtil;
import com.ccnode.codegenerator.util.GenCodeUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.intellij.psi.util.PsiTypesUtil;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public class OracleGenerateFilesHandler implements GenerateFileHandler {

    private static volatile OracleGenerateFilesHandler instance;


    private static volatile HandlerValidator handlerValidator;


    private OracleGenerateFilesHandler() {
    }


    public static OracleGenerateFilesHandler getInstance() {
        if (instance == null) {
            synchronized (OracleGenerateFilesHandler.class) {
                if (instance == null) {
                    instance = new OracleGenerateFilesHandler();
                }
            }
        }
        return instance;
    }

    @NotNull
    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
        return OracleHandlerUtils.getRecommendDatabaseTypeOfFieldType(field);
    }

    @NotNull
    @Override
    public String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        List<String> retList = Lists.newArrayList();
        retList.add(String.format("-- auto Generated on %s ", DateUtil.formatLong(new Date())));
        retList.add("-- DROP TABLE IF EXISTS " + tableName + "; ");
        retList.add("CREATE TABLE " + tableName + "(");
        List<String> indexText = Lists.newArrayList();
        List<String> uniques = Lists.newArrayList();
        for (GenCodeProp field : propList) {
            String fieldSql = genfieldSql(field);
            retList.add(fieldSql);
            if (field.getIndex()) {
                indexText.add("CREATE INDEX " + field.getColumnName() + "_idx ON " + tableName + " (" + field.getColumnName() + ");");
            }
            if (field.getUnique()) {
                uniques.add(GenCodeUtil.ONE_RETRACT + "CONSTRAINT " + field.getColumnName() + "_unique UNIQUE(" + field.getColumnName() + "),");
            }
        }
        retList.addAll(uniques);
        retList.add(GenCodeUtil.ONE_RETRACT + "CONSTRAINT " + tableName + "_pk PRIMARY KEY (" + primaryKey.getColumnName() + "));");
        retList.add(generateAutoIncrementAndTrigger(tableName, primaryKey.getColumnName()));
        retList.addAll(indexText);
        return Joiner.on("\n").join(retList);
    }

    @NotNull
    @Override
    public ClassValidateResult validateCurrentClass(PsiClass psiClass) {
        if (handlerValidator == null) {
            //this is not perfect but with less code to write, better use a lock object.
            synchronized (OracleGenerateFilesHandler.class) {
                if (handlerValidator == null) {
                    handlerValidator = new HandlerValidator(new OracleFieldValidator());
                }
            }
        }
        return handlerValidator.validateResult(psiClass);
    }


    class OracleFieldValidator implements FieldValidator {
        @Override
        public boolean isValidField(PsiField psiField) {
            String canonicalText = psiField.getType().getCanonicalText();
            List<TypeProps> typePropss = OracleHandlerUtils.getTypePropByQulitifiedName(canonicalText);
            if (typePropss != null) {
                return true;
            }
            PsiClass psiClass = PsiTypesUtil.getPsiClass(psiField.getType());
            if (psiClass != null && psiClass.isEnum()) {
                return true;
            }
            return false;
        }
    }


    private String genfieldSql(GenCodeProp field) {
        StringBuilder ret = new StringBuilder();
        ret.append(GenCodeUtil.ONE_RETRACT).append(field.getColumnName())
                .append(" ").append(field.getFiledType());
        if (org.apache.commons.lang.StringUtils.isNotBlank(field.getSize())) {
            ret.append("(" + field.getSize() + ")");
        }
        if (!field.getPrimaryKey() && field.getHasDefaultValue() && org.apache.commons.lang.StringUtils.isNotBlank(field.getDefaultValue())) {
            ret.append(" DEFAULT " + field.getDefaultValue());
        }
        if (!field.getCanBeNull()) {
            ret.append(" NOT NULL");
        }
        ret.append(",");
        return ret.toString();
    }


    private static String generateAutoIncrementAndTrigger(String tableName, String primaryColumnName) {
        return "create sequence " + tableName + "_seq start with 1 increment by 1 nomaxvalue;\n" +
                "create trigger " + tableName + "_trigger\n" +
                "before insert on " + tableName + "\n" +
                "for each row\n" +
                "   begin\n" +
                "     select " + tableName + "_seq.nextval into :new." + primaryColumnName + " from dual;\n" +
                "   end;";
    }
}
