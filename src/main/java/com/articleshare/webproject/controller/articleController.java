package com.articleshare.webproject.controller;

import com.articleshare.webproject.Util.SessionUtil;
import com.articleshare.webproject.dao.ArticleMapper;
import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.article.ArticleScore;
import com.articleshare.webproject.domain.user.User;
import com.articleshare.webproject.service.ArticleService;
import com.articleshare.webproject.service.UserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class articleController {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;

    @RequestMapping(value = "searchArticle.do", method = RequestMethod.POST)
    public List<Article> SearchArticle(@RequestBody String param) {
        List<Article> searchfrom = articleMapper.SearchArticle(param);
        return searchfrom;
    }

    @RequestMapping(value = "qryArticle.do", method = RequestMethod.POST)
    @ResponseBody
    public Map qryArticle(@RequestBody Map param) {
        return articleService.qryArticle(param);
    }

    /**
     * 设置当前阅读文章Session
     *
     * @param articleId
     */
    @RequestMapping(value = "saveNowLogin.do", method = RequestMethod.POST)
    @ResponseBody
    public Integer saveNowLogin(@RequestBody String articleId) {
        Article nowReadingArticle = articleService.qryArticleBYId(articleId);
        SessionUtil.setSessionAttribute("nowReadingArticle", nowReadingArticle);
        return 1;
    }

    @RequestMapping(value = "loadNowArticleInfo.do")
    @ResponseBody
    public Map loadNowArticleInfo() {
        Article nowArticle = articleService.loadNowArticleInfo();
        Integer nowArticleId = nowArticle.getArticleId();
        List<ArticleScore> articleScores = articleService.qryArticleScoreByaId(nowArticleId);
        //获取评价分级人数根据评价等级分层
        if(articleScores.size()==0){
            Map<String, Object> result = new HashMap<>();
            result.put("isEmpty", true);
            return  result;
        }
        int countTotal = articleScores.size();
        double totalScore = 0.0;
        Map<Integer, Integer> scoreMap = new HashMap<Integer, Integer>();
        for (ArticleScore articleScore : articleScores) {
            totalScore += articleScore.getScore();
            if (!scoreMap.containsKey(articleScore.getScore())) {
                scoreMap.put(articleScore.getScore(), 1);
            } else {
                int nowVal = scoreMap.get(articleScore.getScore());
                scoreMap.put(articleScore.getScore(), ++nowVal);
            }
        }
        BigDecimal bg = new BigDecimal(totalScore / (articleScores.size()));
        double avg = bg.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
        Map<String, Object> result = new HashMap<>();
        result.put("nowArticle", nowArticle);
        result.put("articleScores", articleScores);
        result.put("scoreMap", scoreMap);
        result.put("avg-score", avg);
        return result;
    }

    @RequestMapping(value = "getCommentPages.do")
    @ResponseBody
    public Object getCommentPages(@RequestBody Map param) {
        Map<String, Object> resultMap = new HashMap<>();
        int startPage = 0, pageNum = 0;
        if (param.get("startPage") != null) {
            startPage = Integer.parseInt(param.get("startPage").toString());
        }
        if (param.get("pageNum") != null) {
            pageNum = Integer.parseInt(param.get("pageNum").toString());
        }
        Article article = (Article) SessionUtil.getSessionAttribute("nowReadingArticle");
        PageHelper.startPage(startPage, pageNum);
        List<ArticleScore> articleScores = articleService.qryArticleScoreByaId(article.getArticleId());
        PageInfo<ArticleScore> articleScorePageInfo = new PageInfo<>(articleScores);
        Integer totalCount = articleScores.size();
        resultMap.put("totalCount", totalCount);
        resultMap.put("articleScorePageInfo", articleScorePageInfo);
        return resultMap;
    }

    @RequestMapping(value = "readTheArticle.do")
    @ResponseBody
    public Object readTheArticle() {
        //获取本地Url
        Article nowReadingArticle = (Article) SessionUtil.getSessionAttribute("nowReadingArticle");
        String homeUrl = nowReadingArticle.getHomeUrl();
        Map<String, Object> resultMap = new HashMap<>();
        StringBuffer articleContent = new StringBuffer();
        try {
            String encoding = "utf-8";
            File file = new File(homeUrl);

            if (file.isFile() && file.exists()) { //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file), encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while ((lineTxt = bufferedReader.readLine()) != null) {
                    System.out.println(lineTxt);
                    articleContent.append(lineTxt);
                }
                read.close();

            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        resultMap.put("articleContent", articleContent);
        resultMap.put("nowReadingArticle", nowReadingArticle);
        return resultMap;
    }


    /**
     * 查新作者关注
     *
     * @return
     */
    @RequestMapping(value = "qryUserFollow.do")
    @ResponseBody
    public Object qryUserFollow() {
        return null;
    }

    /**
     * 查询用户的评价
     *
     * @return
     */
    @RequestMapping(value = "qryUserScore.do")
    @ResponseBody
    public Object qryUserScore() {
        return null;
    }

    @RequestMapping(value = "addToPersonCollect.do")
    @ResponseBody
    public Object addToCollection() {
        Article article = (Article) SessionUtil.getSessionAttribute("nowReadingArticle");
        User user = (User) SessionUtil.getSessionAttribute("User");
        java.sql.Date collectDate = new java.sql.Date(new java.util.Date().getTime());
        Boolean is = true;
        //查询用户是否添加收藏
        if (!userService.checkCollectArticle(Integer.parseInt(user.getUserId()), article.getArticleId())) {
            return false;
        } else {
            try {
                userService.CollectArticle(Integer.parseInt(user.getUserId()), article.getArticleId(), collectDate);

            } catch (Exception e) {
                e.printStackTrace();
                is = false;
                return is;
            }
            return is;
        }
    }

    @RequestMapping(value = "getHotArticles.do")
    @ResponseBody
    public Object getHotArticles() {
        //获取一周内收藏热门
        List<Map> hotCollectArticles = articleService.getHotCollectArticles();
        int i = 0, j = 0;
        for (Map map : hotCollectArticles) {
            map.put("ranking", ++i);
        }
        //获取一周内点击量热门
        List<Map> hotClickArticles = articleService.getHotClickArticles();
        for (Map map : hotClickArticles) {
            map.put("ranking", ++j);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("hotCollectArticles", hotCollectArticles);
        resultMap.put("hotClickArticles", hotClickArticles);
        return resultMap;
    }

    @RequestMapping(value = "deleteArticleById.do")
    @ResponseBody
    public boolean deleteArticleById(@RequestBody String ArticleId) {
        try {
            articleMapper.deleteArticleById(Integer.parseInt(ArticleId));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

