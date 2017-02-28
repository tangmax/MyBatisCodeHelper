
        INSERT INTO ${tableName} (
        <#list finalFields as filedAndColumn>
            <#if filedAndColumn?is_last>
                 ${filedAndColumn.column}
            <#else>
                ${filedAndColumn.column},
            </#if>
        </#list>
        ) VALUES (
        <#list finalFields as filedAndColumn>
            <#if filedAndColumn?is_last>
                ${r"#"}{pojo.${filedAndColumn.field}}
            <#else>
                ${r"#"}{pojo.${filedAndColumn.field}},
            </#if>
        </#list>
        )
