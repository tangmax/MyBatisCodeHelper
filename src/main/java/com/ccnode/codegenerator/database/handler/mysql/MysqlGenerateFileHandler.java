package com.ccnode.codegenerator.database.handler.mysql;

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
public class MysqlGenerateFileHandler implements GenerateFileHandler {
    @NotNull
    @Override
    public List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field) {
        return MysqlHandlerUtils.getRecommendDatabaseTypeOfFieldType(field);
    }

    private static volatile MysqlGenerateFileHandler instance;

    private static volatile HandlerValidator handlerValidator;

    private MysqlGenerateFileHandler() {
        if (instance != null) {
            throw new IllegalStateException("Already initialized.");
        }
    }

    public static MysqlGenerateFileHandler getInstance() {
        MysqlGenerateFileHandler result = instance;
        if (result == null) {
            synchronized (MysqlGenerateFileHandler.class) {
                result = instance;
                if (result == null) {
                    instance = result = new MysqlGenerateFileHandler();
                }
            }
        }
        return result;
    }

    @NotNull
    @Override
    public String generateSql(List<GenCodeProp> propList, GenCodeProp primaryKey, String tableName) {
        List<String> retList = Lists.newArrayList();
        String newTableName = GenCodeUtil.wrapComma(tableName);
        retList.add(String.format("-- auto Generated on %s ", DateUtil.formatLong(new Date())));
        retList.add("-- DROP TABLE IF EXISTS " + newTableName + "; ");
        retList.add("CREATE TABLE " + newTableName + "(");
        List<String> indexText = Lists.newArrayList();
        for (GenCodeProp field : propList) {
            String fieldSql = genfieldSql(field);
            retList.add(fieldSql);
            if (field.getIndex()) {
                indexText.add(GenCodeUtil.ONE_RETRACT + "INDEX(" + field.getColumnName() + "),");
            }
        }
        retList.addAll(indexText);
        // TODO: 2016/12/26 InnoDb and utf8 can be later configured
        retList.add(GenCodeUtil.ONE_RETRACT + "PRIMARY KEY (" + GenCodeUtil.wrapComma(primaryKey.getColumnName()) + ")");
        retList.add(String.format(")ENGINE=%s DEFAULT CHARSET=%s COMMENT '%s';", "InnoDB",
                "utf8", newTableName));
        return Joiner.on("\n").join(retList);
    }

    private static String genfieldSql(GenCodeProp field) {
        StringBuilder ret = new StringBuilder();
        UnsignedCheckResult result = MysqlHandlerUtils.checkUnsigned(field.getFiledType());
        ret.append(GenCodeUtil.ONE_RETRACT).append(GenCodeUtil.wrapComma(field.getColumnName()))
                .append(" ").append(result.getType());
        if (org.apache.commons.lang.StringUtils.isNotBlank(field.getSize())) {
            ret.append(" (" + field.getSize() + ")");
        }
        if (result.isUnsigned()) {
            ret.append(" UNSIGNED");
        }
        if (field.getUnique()) {
            ret.append(" UNIQUE");
        }
        if (!field.getCanBeNull()) {
            ret.append(" NOT NULL");
        }

        if (!field.getPrimaryKey() && field.getHasDefaultValue() && org.apache.commons.lang.StringUtils.isNotBlank(field.getDefaultValue())) {
            ret.append(" DEFAULT " + field.getDefaultValue());
        }

        if (field.getPrimaryKey()) {
            ret.append(" AUTO_INCREMENT");
        }
        ret.append(" COMMENT '" + field.getFieldName() + "',");
        return ret.toString();
    }


    @NotNull
    @Override
    public ClassValidateResult validateCurrentClass(PsiClass psiClass) {
        if (handlerValidator == null) {
            synchronized (MysqlGenerateFileHandler.class) {
                if (handlerValidator == null) {
                    handlerValidator = new HandlerValidator(new MysqlFieldValidator());
                }
            }
        }
        return handlerValidator.validateResult(psiClass);
    }


    class MysqlFieldValidator implements FieldValidator {
        @Override
        public boolean isValidField(PsiField psiField) {
            String canonicalText = psiField.getType().getCanonicalText();
            List<TypeProps> typePropss = MysqlHandlerUtils.getTypePropsByQulifiType(canonicalText);
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
}
