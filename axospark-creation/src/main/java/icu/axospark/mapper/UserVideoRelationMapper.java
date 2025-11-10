package icu.axospark.mapper;


import icu.axospark.pojo.entity.UserVideoRelation;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZacharyXu
* @description 针对表【user_video_relation(用户视频关联表)】的数据库操作Mapper
* @createDate 2025-11-07 21:46:33
* @Entity generator.domain.UserVideoRelation
*/
@Mapper
public interface UserVideoRelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserVideoRelation record);

    int insertSelective(UserVideoRelation record);

    UserVideoRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserVideoRelation record);

    int updateByPrimaryKey(UserVideoRelation record);

}
