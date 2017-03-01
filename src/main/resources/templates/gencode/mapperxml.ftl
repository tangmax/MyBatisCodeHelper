<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${daoFullType}">
    <!--auto generated Code-->
    <resultMap id="AllColumnMap" type="${pojoFullType}">
    <#list fieldAndColumns as fieldAndColumn>
        <result column="${fieldAndColumn.column}" property="${fieldAndColumn.field}"/>
    </#list>
    </resultMap>

    <!--auto generated Code-->
    <sql id="all_column">
    <#list fieldAndColumns as fieldAndColumn>
        <#if fieldAndColumn?is_last>
        ${fieldAndColumn.formattedColumn}
        <#else>
        ${fieldAndColumn.formattedColumn},
        </#if>
    </#list>
    </sql>

    <!--auto generated Code-->
    <insert id="insert"<#if useGeneratedKeys> useGeneratedKeys="true" keyProperty="pojo.${primaryField}"</#if>>
        INSERT INTO ${tableName} (
    <#list fieldAndColumns as fieldAndColumn>
            ${fieldAndColumn.formattedColumn}<#rt>
        <#lt><#if !fieldAndColumn?is_last>,</#if>
    </#list>
        ) VALUES (
    <#list fieldAndColumns as fieldAndColumn>
        <#if fieldAndColumn.field == primaryField>
            ${r"#"}{pojo.${fieldAndColumn.field}<#if primaryJdbcType??>,jdbcType=${primaryJdbcType}</#if>}<#rt>
        <#else>
            ${r"#"}{pojo.${fieldAndColumn.field}}<#rt>
        </#if>
        <#lt><#if !fieldAndColumn?is_last>,</#if>
    </#list>
        )
    </insert>

    <!--auto generated Code-->
    <insert id="insertSelective"<#if useGeneratedKeys> useGeneratedKeys="true" keyProperty="pojo.${primaryField}"</#if>>
        INSERT INTO ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list fieldAndColumns as fieldAndColumn>
            <if test="pojo.${fieldAndColumn.field}!=null"> ${fieldAndColumn.formattedColumn},</if>
        </#list>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list fieldAndColumns as fieldAndColumn>
            <#if fieldAndColumn.field == primaryField>
            <if test="pojo.${fieldAndColumn.field}!=null">${r"#"}{pojo.${fieldAndColumn.field}<#if primaryJdbcType??>,jdbcType=${primaryJdbcType}</#if>},</if>
            <#else>
            <if test="pojo.${fieldAndColumn.field}!=null">${r"#"}{pojo.${fieldAndColumn.field}},</if>
            </#if>
        </#list>
        </trim>
    </insert>

    <!--auto generated Code-->
<#if currentDatabase=="Mysql">
    <insert id="insertList">
        INSERT INTO ${tableName} (
        <include refid="all_column"/>
        )VALUES
        <foreach collection="pojos" item="pojo" index="index" separator=",">
            (
            <#list fieldAndColumns as fieldAndColumn>
            ${r"#"}{pojo.${fieldAndColumn.field}}<#if !fieldAndColumn?is_last>,</#if>
            </#list>
            )
        </foreach>
    </insert>
</#if>
<#if currentDatabase=="Oracle">
    <insert id="insertList">
        INSERT ALL
        <foreach collection="pojos" item="pojo">
            INTO ${tableName} (
            <include refid="all_column"/>
            )VALUES(
            <#list fieldAndColumns as fieldAndColumn>
                <#if fieldAndColumn.field == primaryField>
                ${r"#"}{pojo.${fieldAndColumn.field}<#if primaryJdbcType??>,jdbcType=${primaryJdbcType}</#if>}<#rt>
                <#else>
                ${r"#"}{pojo.${fieldAndColumn.field}}<#rt>
                <#lt></#if><#if !fieldAndColumn?is_last>,</#if>
            </#list>
            )
        </foreach>
        select * from dual
    </insert>
</#if>

    <!--auto generated Code-->
    <update id="update">
        UPDATE ${tableName}
        <set>
        <#list fieldAndColumns as filedAndColumn>
            <#if filedAndColumn?is_last>
            <if test="pojo.${filedAndColumn.field} != null"> ${filedAndColumn.formattedColumn} = ${r"#"}{pojo.${filedAndColumn.field}} </if>
            <#else>
            <if test="pojo.${filedAndColumn.field} != null"> ${filedAndColumn.formattedColumn} = ${r"#"}{pojo.${filedAndColumn.field}}, </if>
            </#if>
        </#list>
        </set>
        WHERE ${primaryColumn} = ${r"#"}{pojo.${primaryField}}
    </update>
</mapper>
