package org.example.dao;

import org.example.pojo.News;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDaoImpl implements NewsDao {
    public List<News> queryNewsList() {
        // 数据库链接
        Connection connection = null;
        // 预编译statement
        PreparedStatement preparedStatement = null;
        // 结果集
        ResultSet resultSet = null;
        // 商品列表
        List<News> list = new ArrayList<>();

        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college", "root", "123456");
            System.out.println("连接成功！");
            // SQL语句
            String sql = "select * from news";
            // 创建preparedStatement
            preparedStatement = connection.prepareStatement(sql);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();
            // 结果集解析
            while (resultSet.next()) {
                News news = new News();
                news.setTitle(resultSet.getString("title"));
                news.setSource(resultSet.getString("source"));
                news.setCategory(resultSet.getString("category"));
                news.setKeyword(resultSet.getString("keyword"));
                news.setTag(resultSet.getString("tag"));
                news.setDescription(resultSet.getString("description"));
                news.setContent(resultSet.getString("content"));
                news.setHead_img(resultSet.getString("head_img"));
                news.setUrl(resultSet.getString("url"));
                list.add(news);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list.size() + "size");
        return list;
    }

    @Override
    public News detail(String tiurl) {
        Connection connection = null;
        // 预编译statement
        PreparedStatement preparedStatement = null;
        // 结果集
        ResultSet resultSet = null;
        // 商品列表
        News news = new News();
        try {
            // 加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 连接数据库
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college", "root", "123456");
            System.out.println("连接成功！");
            // SQL语句
            String sql = "select * from news where title = tiurl or url = tiurl";
            // 创建preparedStatement
            preparedStatement = connection.prepareStatement(sql);
            // 获取结果集
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                news.setTitle(resultSet.getString("title"));
                news.setSource(resultSet.getString("source"));
                news.setCategory(resultSet.getString("category"));
                news.setKeyword(resultSet.getString("keyword"));
                news.setTag(resultSet.getString("tag"));
                news.setDescription(resultSet.getString("description"));
                news.setContent(resultSet.getString("content"));
                news.setHead_img(resultSet.getString("head_img"));
                news.setUrl(resultSet.getString("url"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return news;

    }
}
