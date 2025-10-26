package icu.axospark.controller;

import icu.axospark.entity.AxoSparkResult;
import icu.axospark.pojo.vo.CategoriesVO;
import icu.axospark.service.VideoCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "视频分类请求")
@Slf4j
@RequestMapping("/categories")
public class VideoCategoryController {
    @Autowired
    private VideoCategoryService videoCategoryService;

    /**
     * 查询视频所有分类
     * @return 视频所有分类
     */
    @GetMapping("/video")
    public AxoSparkResult<List<CategoriesVO>> getVideoCategories(){
        List<CategoriesVO> categories=videoCategoryService.getVideoCategories();
        return AxoSparkResult.success(categories);
    }

}
