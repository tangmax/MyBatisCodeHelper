package ${daoPackageName};

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import ${pojoFullType};

<#if addMapperAnnotation>@Mapper</#if>
public interface ${daoType} {

    ${pojoType} selectByPrimaryKey(Long id);

    int deleteByPrimaryKey(Long id);

    int insert(${pojoType} pojo);

    int updateByPrimaryKeySelective(${pojoType} pojo);

    int updateByPrimaryKey(${pojoType} pojo);
}