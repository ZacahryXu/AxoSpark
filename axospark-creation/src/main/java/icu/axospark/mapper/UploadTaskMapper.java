package icu.axospark.mapper;


import icu.axospark.pojo.entity.UploadTask;
import org.apache.ibatis.annotations.Mapper;

/**
* @author ZacharyXu
* @description 针对表【upload_task(大文件上传任务日志表)】的数据库操作Mapper
* @createDate 2025-11-08 22:18:32
* @Entity generator.domain.UploadTask
*/
@Mapper
public interface UploadTaskMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UploadTask record);

    int insertSelective(UploadTask record);

    UploadTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UploadTask record);

    int updateByPrimaryKey(UploadTask record);

    UploadTask selectNotUploaded(UploadTask uploadTask);

    void deleteByFileHash(String fileHash);

    void updateByUploadId(UploadTask uploadTask);
}
