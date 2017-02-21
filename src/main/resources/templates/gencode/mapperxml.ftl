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
        `${fieldAndColumn.column}`
        <#else>
        `${fieldAndColumn.column}`,
        </#if>
    </#list>
    </sql>

    <!--auto generated Code-->
    <insert id="insert">
        INSERT INTO ${tableName}
        (<#list fieldAndColumns as fieldAndColumn><#if fieldAndColumn?is_last>`${fieldAndColumn.column}`<#else>`${fieldAndColumn.column}`,</#if></#list>)
        VALUES
        (<#list fieldAndColumns as fieldAndColumn><#if fieldAndColumn?is_last>${r"#"}{pojo.${fieldAndColumn.field}}<#else>${r"#"}{pojo.${fieldAndColumn.field}},</#if></#list>)
    </insert>

    <!--auto generated Code-->
    <insert id="insertSelective">
        INSERT INTO ${tableName}
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list fieldAndColumns as filedAndColumn>
            <#if filedAndColumn?is_last>
                <if test="pojo.${filedAndColumn.field}!=null"> `${filedAndColumn.column}`</if>
            <#else>
                <if test="pojo.${filedAndColumn.field}!=null"> `${filedAndColumn.column}`,</if>
            </#if>
        </#list>
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
        <#list fieldAndColumns as filedAndColumn>
            <#if filedAndColumn?is_last>
                <if test="pojo.${filedAndColumn.field}!=null"> ${r"#"}{pojo.${filedAndColumn.field}}</if>
            <#else>
                <if test="pojo.${filedAndColumn.field}!=null"> ${r"#"}{pojo.${filedAndColumn.field}},</if>
            </#if>
        </#list>
        </trim>
    </insert>

    <!--auto generated Code-->
    <insert id="insertList">
        INSERT INTO ${tableName} (
        <include refid="all_column"/>
        )VALUES
        <foreach collection="pojos" item="pojo" index="index" separator=",">
            (
        <#list fieldAndColumns as fieldAndColumn>
            <#if fieldAndColumn?is_last>
            ${r"#"}{pojo.${fieldAndColumn.field}}
            <#else>
            ${r"#"}{pojo.${fieldAndColumn.field}},
            </#if>
        </#list>
            )
        </foreach>
    </insert>

    <!--auto generated Code-->
    <update id="update">
        UPDATE ${tableName}
        <set>
        <#list fieldAndColumns as filedAndColumn>
            <#if filedAndColumn?is_last>
            <if test="pojo.${filedAndColumn.field} != null"> `${filedAndColumn.column}` = ${r"#"}{pojo.${filedAndColumn.field}}</if>
            <#else>
            <if test="pojo.${filedAndColumn.field} != null"> `${filedAndColumn.column}` = ${r"#"}{pojo.${filedAndColumn.field}},</if>
            </#if>
        </#list>
        </set>
        WHERE `${primaryColumn}` = ${r"#"}{pojo.${primaryField}}
    </update>
</mapper>
