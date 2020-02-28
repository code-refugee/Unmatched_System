package com.unmatched.mapper;

import com.unmatched.pojo.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* Description:@Repository需要在Spring中配置扫描地址，
 * 然后生成Dao层的Bean才能被注入到Service层中。
 *
 * @Mapper不需要配置扫描地址，通过xml里面的namespace里面的接口地址，
 * 生成了Bean后注入到Service层中。
 * 这里我们使用@Repository，因为我们需要使用后置处理器捕获该注解抛出的异常
 * 转换为特定的Spring异常
* @author: yuhang tao
* @date: 2019/12/21
* @version: v1.0
*/
//@Mapper
@Repository
public interface UserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}