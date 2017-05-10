package com.ccnode.codegenerator.dialog.dto.mybatis;

/**
 * Created by bruce.ge on 2016/12/27.
 */
public class ColumnAndField {
    protected String column;

    protected String field;

    protected String jdbcType;

    public ColumnAndField(){}

    public ColumnAndField(String column, String field, String jdbcType) {
        this.column = column;
        this.field = field;
        this.jdbcType = jdbcType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }
}
