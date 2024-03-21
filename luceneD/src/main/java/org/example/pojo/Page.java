package org.example.pojo;

import java.util.List;

public class Page <T>{
    private int total;
    private int current;
    private int size;
    private News news;
    List<T> item;

    public Page()
    {}

    public Page(int total, int size, int current){
        this.total = total;
        this.size = size;
        this.current = current;
    }



    public List<T> getItem() {
        return item;
    }

    public void setItem(List<T> item) {
        this.item = item;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
}
