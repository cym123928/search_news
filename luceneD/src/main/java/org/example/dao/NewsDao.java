package org.example.dao;

import org.example.pojo.News;

import java.util.List;

public interface NewsDao {

    public List<News> queryNewsList();
    public News detail(String tiurl);
}
