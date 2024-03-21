package org.example.service;

import org.example.pojo.News;
import org.example.pojo.ResultModel;
import org.springframework.ui.Model;

import java.util.List;

public interface SearchService {

    public ResultModel query(String queryString,int page) throws Exception;
}
