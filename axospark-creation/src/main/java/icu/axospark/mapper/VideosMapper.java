package icu.axospark.mapper;


import icu.axospark.pojo.entity.Videos;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZacharyXu
* @description 针对表【videos(视频表)】的数据库操作Mapper
* @createDate 2025-10-25 17:07:54
* @Entity generator.domain.Videos
*/
@Mapper
public interface VideosMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Videos record);

    int insertSelective(Videos record);

    Videos selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Videos record);

    int updateByPrimaryKey(Videos record);

}
