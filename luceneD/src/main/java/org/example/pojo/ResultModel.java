package org.example.pojo;

import java.util.List;

//分页用的  自定义分页实体类
public class ResultModel {
    public List<News> item;  // 新闻列表
    public int recordCount;   // 商品总数
    public int pageCount;     // 总页数
    public int curPage;       // 当前页
    public News news;

    public List<News> getNewsList() {
        return item;
    }

    public void setNewsList(List<News> newsList) {
        this.item = newsList;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
