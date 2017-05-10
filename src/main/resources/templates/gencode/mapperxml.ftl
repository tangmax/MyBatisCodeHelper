<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${daoFullType}">
    <!-- auto generated code -->
    <resultMap id="BaseResultMap" type="${pojoFullType}">
        <#--<id column="id" property="id" jdbcType="BIGINT" />-->
    <#list fieldAndColumns as fieldAndColumn>
        <result column="${fieldAndColumn.column}" property="${fieldAndColumn.field}" jdbcType="${fieldAndColumn.jdbcType}" />
    </#list>
    </resultMap>

    <!-- auto generated code -->
    <sql id="Base_Column_List">
    <#list fieldAndColumns as fieldAndColumn>
        <#if fieldAndColumn?is_last>
        ${fieldAndColumn.formattedColumn}
        <#else>
        ${fieldAndColumn.formattedColumn},
        </#if>
    </#list>
    </sql>

    <!-- auto generated code -->
    <select id="selectByPrimaryKey" resultMap="BaseResultMap" parameterType="java.lang.Long" >
        select
        <include refid="Base_Column_List" />
        from ${tableName}
        where id = #${r'{id,jdbcType=BIGINT}'}
    </select>

    <!-- auto generated code -->
    <delete id="deleteByPrimaryKey" parameterType="java.lang.Long" >
        delete from ${tableName}
        where id = #${r'{id,jdbcType=BIGINT}'}
    </delete>

    <!-- auto generated code -->
    <insert id="insert" parameterType="${pojoFullType}">
        INSERT INTO ${tableName} (
    <#list fieldAndColumns as fieldAndColumn>
            ${fieldAndColumn.formattedColumn}<#rt>
        <#lt><#if !fieldAndColumn?is_last>,</#if>
    </#list>
        ) VALUES (
    <#list fieldAndColumns as fieldAndColumn>
            ${r"#"}{${fieldAndColumn.field},jdbcType=${fieldAndColumn.jdbcType}}<#rt>
        <#lt><#if !fieldAndColumn?is_last>,</#if>
    </#list>
        )
    </insert>

    <!-- auto generated code -->
    <update id="updateByPrimaryKeySelective" parameterType="${pojoFullType}">
        UPDATE ${tableName}
        <set>
        <#list fieldAndColumns as filedAndColumn>
            <#if filedAndColumn?is_last>
            <if test="${filedAndColumn.field} != null"> ${filedAndColumn.formattedColumn} = ${r"#"}{${filedAndColumn.field},jdbcType=${filedAndColumn.jdbcType}} </if>
            <#else>
            <if test="${filedAndColumn.field} != null"> ${filedAndColumn.formattedColumn} = ${r"#"}{${filedAndColumn.field},jdbcType=${filedAndColumn.jdbcType}}, </if>
            </#if>
        </#list>
        </set>
        where id = #${r'{id,jdbcType=BIGINT}'}
    </update>

    <!-- auto generated code -->
    <update id="updateByPrimaryKey" parameterType="${pojoFullType}">
        UPDATE ${tableName}
        <set>
        <#list fieldAndColumns as filedAndColumn>
            <#if filedAndColumn?is_last>
                ${filedAndColumn.formattedColumn} = ${r"#"}{${filedAndColumn.field},jdbcType=${filedAndColumn.jdbcType}}
            <#else>
                ${filedAndColumn.formattedColumn} = ${r"#"}{${filedAndColumn.field},jdbcType=${filedAndColumn.jdbcType}},
            </#if>
        </#list>
        </set>
        where id = #${r'{id,jdbcType=BIGINT}'}
    </update>
</mapper>
