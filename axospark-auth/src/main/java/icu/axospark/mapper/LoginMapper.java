package icu.axospark.mapper;


import icu.axospark.pojo.entity.Users;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZacharyXu
* @description 针对表【users(用户表)】的数据库操作Mapper
* @createDate 2025-10-15 13:57:37
* @Entity generator.domain.Users
*/
@Mapper
public interface LoginMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Long id);
    Users selectByUsername(String username);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);


}
