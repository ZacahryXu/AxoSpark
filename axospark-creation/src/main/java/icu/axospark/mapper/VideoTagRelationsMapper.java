package icu.axospark.mapper;


import icu.axospark.pojo.entity.VideoTagRelations;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZacharyXu
* @description 针对表【video_tag_relations(视频标签关联表)】的数据库操作Mapper
* @createDate 2025-10-25 17:07:54
* @Entity generator.domain.VideoTagRelations
*/
@Mapper
public interface VideoTagRelationsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(VideoTagRelations record);

    int insertSelective(VideoTagRelations record);

    VideoTagRelations selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VideoTagRelations record);

    int updateByPrimaryKey(VideoTagRelations record);

    void deleteByVideoId(Long videoId);
}
