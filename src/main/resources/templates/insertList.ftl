        <#if currentDatabase=="Mysql">
        INSERT INTO ${tableName}(
        <include refid="all_column"/>
        )VALUES
        <foreach collection="pojos" item="pojo" index="index" separator=",">
            (
        <#list finalFields as fieldAndColumn>
            <#if fieldAndColumn?is_last>
            ${r"#"}{pojo.${fieldAndColumn.field}}
            <#else>
            ${r"#"}{pojo.${fieldAndColumn.field}},
            </#if>
        </#list>
            )
        </foreach>
        </#if>
        <#if currentDatabase=="Oracle">
        INSERT ALL
        <foreach collection="pojos" item="pojo">
            INTO ${tableName} (
            <include refid="all_column"/>
            )VALUES(
            <#list finalFields as fieldAndColumn>
                <#if fieldAndColumn?is_last>
                ${r"#"}{pojo.${fieldAndColumn.field}}
                <#else>
                ${r"#"}{pojo.${fieldAndColumn.field}},
                </#if>
            </#list>
            )
        </foreach>
        select * from dual
        </#if>
