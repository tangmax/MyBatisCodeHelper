package ${servicePackage};

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import ${pojoFullType};
import ${daoFullType};

@Service
public class ${serviceType}{

    @Resource
    private ${daoType} ${daoName};

    public int insert(${pojoType} pojo){
        return ${daoName}.insert(pojo);
    }

    public int insertSelective(${pojoType} pojo){
        return ${daoName}.insertSelective(pojo);
    }

    public int insertList(List<${pojoType}> pojos){
        return ${daoName}.insertList(pojos);
    }

    public int update(${pojoType} pojo){
        return ${daoName}.update(pojo);
    }
}