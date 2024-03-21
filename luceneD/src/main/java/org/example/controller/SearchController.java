package org.example.controller;

import org.example.dao.NewsDaoImpl;
import org.example.pojo.News;
import org.example.pojo.Result;
import org.example.pojo.ResultModel;
import org.example.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Controller
@ResponseBody
@RestController
public class SearchController {
@Resource
private SearchService searchService;

    @RequestMapping("/sc/list")
    public Result<?> query(String queryString,int currentPage)throws Exception{
        System.out.println(queryString +"qs");

      //   接收查询的关键字

      //   处理当前页   stringUtils: 字符串若为空，则不抛出空指针异常，会对其进行处理
        if(StringUtils.isEmpty(currentPage) || currentPage <= 0){
            currentPage = 1;
        }

        //调用service查询
//       ResultModel resultModel = searchService.query(queryString,page);
//       model.addAttribute("result",resultModel);
//        //查询条件回响到页面
//        model.addAttribute("queryString",queryString);
//      //  model.addAttribute("price",price);
//        model.addAttribute("page",page);
//        System.out.println(page+"pppppppppppppppppp");
//       return "result";  //返回到 seach.html页面
        ResultModel resultModel = searchService.query(queryString,currentPage);   //page：currentPage
      //  List<News> newsList = resultModel.getNewsList();
//        List<News> list = new ArrayList<>();
//       // int pageSize = 20;
//        //int currentPage = resultModel.getCurPage();
//        Page<News> pages = new Page<>(newsList.size(), pageSize, currentPage);
//        int n = (currentPage-1)*pageSize;
//        for(int i = 1; i <= pageSize && n < pages.getTotal(); i++){
//            list.add(newsList.get(n));
//            n++;
//        }
//        pages.setItem(list);
//        System.out.println("查找完所有学生！");
        List<News> newsList = resultModel.getNewsList();
        System.out.println(newsList.size()+"controller");
        for (int i = 0; i < 10; i++) {
            System.out.println(newsList.get(i).title);
        }
        return Result.success(resultModel);
    }

    @RequestMapping("/sc/detail")
    public News detail(String tiurl)throws Exception{
        NewsDaoImpl newsDao = new NewsDaoImpl();
        News news = newsDao.detail(tiurl);
        System.out.println(news.title+"aaaaaaaaa");
        return news;
    }
}
