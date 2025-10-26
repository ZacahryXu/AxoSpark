package icu.axospark.mapper;


import icu.axospark.pojo.entity.Categories;
import icu.axospark.pojo.entity.VideoCategories;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author ZacharyXu
* @description 针对表【video_categories(视频分类表)】的数据库操作Mapper
* @createDate 2025-10-25 17:07:54
* @Entity generator.domain.VideoCategories
*/
@Mapper
public interface VideoCategoriesMapper {

    int deleteByPrimaryKey(Long id);

    int insert(VideoCategories record);

    int insertSelective(VideoCategories record);

    VideoCategories selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(VideoCategories record);

    int updateByPrimaryKey(VideoCategories record);
    List<Categories> selectAllVideoCategories();

}
