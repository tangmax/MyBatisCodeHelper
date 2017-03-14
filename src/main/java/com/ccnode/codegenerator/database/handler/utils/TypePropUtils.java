package com.ccnode.codegenerator.database.handler.utils;

import com.ccnode.codegenerator.dialog.datatype.TypeProps;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @Author bruce.ge
 * @Date 2017/2/25
 * @Description
 */
public class TypePropUtils {

    @Nullable
    public static List<TypeProps> generateFromDefaultMap(List<TypeProps> fromMapTypes) {
        if (fromMapTypes == null) {
            return null;
        }
        List<TypeProps> typePropslist = Lists.newArrayList();

        for (TypeProps fromMapType : fromMapTypes) {
            typePropslist.add(convert(fromMapType));
        }
        return typePropslist;
    }

    private static TypeProps convert(TypeProps fromMapType) {
        TypeProps typeProps = new TypeProps();
        typeProps.setOrder(fromMapType.getOrder());
        typeProps.setIndex(fromMapType.getIndex());
        typeProps.setHasDefaultValue(fromMapType.getHasDefaultValue());
        typeProps.setPrimary(fromMapType.getPrimary());
        typeProps.setDefaultType(fromMapType.getDefaultType());
        typeProps.setSize(fromMapType.getSize());
        typeProps.setCanBeNull(fromMapType.getCanBeNull());
        typeProps.setUnique(fromMapType.getUnique());
        typeProps.setDefaultValue(fromMapType.getDefaultValue());
        return typeProps;
    }
}
