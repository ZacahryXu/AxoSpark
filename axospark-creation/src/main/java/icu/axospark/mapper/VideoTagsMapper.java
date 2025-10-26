package icu.axospark.mapper;


import icu.axospark.pojo.entity.VideoTags;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZacharyXu
* @description 针对表【video_tags(视频标签表)】的数据库操作Mapper
* @createDate 2025-10-25 17:07:54
* @Entity generator.domain.VideoTags
*/
@Mapper
public interface VideoTagsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(VideoTags record);

    int insertSelective(VideoTags record);

    VideoTags selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VideoTags record);

    int updateByPrimaryKey(VideoTags record);

    VideoTags findTagByName(String tagName);
}
