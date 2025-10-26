package icu.axospark.service.iml;

import icu.axospark.mapper.VideoCategoriesMapper;
import icu.axospark.pojo.entity.Categories;
import icu.axospark.pojo.vo.CategoriesVO;
import icu.axospark.service.VideoCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VideoCategoryServiceIml implements VideoCategoryService {
    @Autowired
    private VideoCategoriesMapper videoCategoriesMapper;

    @Override
    public List<CategoriesVO> getVideoCategories() {
        List<Categories> categories =videoCategoriesMapper.selectAllVideoCategories();
        List<CategoriesVO> categoriesVOS = new ArrayList<>();
        for (Categories category : categories) {
            CategoriesVO categoriesVO = new CategoriesVO();
            BeanUtils.copyProperties(category,categoriesVO);
            categoriesVOS.add(categoriesVO);
        }
        return categoriesVOS;

    }
}
