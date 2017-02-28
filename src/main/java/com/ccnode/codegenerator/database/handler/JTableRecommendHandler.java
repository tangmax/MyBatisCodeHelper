package com.ccnode.codegenerator.database.handler;

import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.intellij.psi.PsiField;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/27
 * @Description
 */
public interface JTableRecommendHandler {
    @NotNull
    List<TypeProps> getRecommendDatabaseTypeOfFieldType(PsiField field);
}
