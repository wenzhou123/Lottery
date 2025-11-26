package com.lottery.mapper;

import com.lottery.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface NewsMapper {
    @Select("SELECT * FROM news ORDER BY publish_date DESC")
    List<News> findAll();

    @Select("SELECT * FROM news WHERE category = #{category} ORDER BY publish_date DESC")
    List<News> findByCategory(String category);
}
