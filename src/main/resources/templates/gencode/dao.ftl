package ${daoPackageName};

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import ${pojoFullType};

<#if addMapperAnnotation>@Mapper</#if>
public interface ${daoType} {
    int insert(@Param("pojo") ${pojoType} pojo);

    int insertSelective(@Param("pojo") ${pojoType} pojo);

    int insertList(@Param("pojos") List<${pojoType}> pojo);

    int update(@Param("pojo") ${pojoType} pojo);
}