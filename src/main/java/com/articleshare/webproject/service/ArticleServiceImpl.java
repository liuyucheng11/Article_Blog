package com.articleshare.webproject.service;

import com.articleshare.webproject.Util.SessionUtil;
import com.articleshare.webproject.dao.ArticleMapper;
import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.article.ArticleScore;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author:"liu.yucheng",
 * @Data:$Date
 */
@Service("ArticleService")
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;

    @Override
    /**
     * 分页查询文章信息
     */
    public Map qryArticle(@RequestBody Map param) {
        int starPage = Integer.parseInt(param.get("startPage").toString());
        int pageSize = Integer.parseInt(param.get("pageSize").toString());
        //根据前端数据进行分页查询
        String type, articleName, author;
        if (param.get("type") != null) {
            type = param.get("type").toString();
        } else {
            type = null;
        }
        if (param.get("articleName") != null) {
            articleName = param.get("articleName").toString();
        } else {
            articleName = null;
        }
        if (param.get("author") != null) {
            author = param.get("author").toString();
        } else {
            author = null;
        }
        Integer articleCount = articleMapper.qryArticleCount(type, articleName, author);
        PageHelper.startPage(starPage, pageSize);
        List<Article> articles = articleMapper.qryArticle(type, articleName, author);
        PageInfo<Article> articlePageInfo = new PageInfo<>(articles);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("articlePageInfo", articlePageInfo);
        resultMap.put("articleNum", articleCount);
        return resultMap;
    }

    @Override
    public Article qryArticleBYId(String articleId) {
        return articleMapper.qryArticleById(articleId);
    }

    @Override
    public Article loadNowArticleInfo() {
        Article nowArticle = (Article) SessionUtil.getSessionAttribute("nowReadingArticle");
        return nowArticle;
    }

    @Override
    public List<ArticleScore> qryArticleScoreByaId(Integer articleId) {
        List<ArticleScore> articleScores = articleMapper.qryArticleScoreById(articleId);
        return articleScores;
    }

    @Override
    public List<Map> getHotCollectArticles() {
        List<Map> hotArticles = articleMapper.getHotCollectArticles();
        return hotArticles;
    }

    @Override
    public List<Map> getHotClickArticles() {
        List<Map> hotClickArticles = articleMapper.getHotClickArticles();
        return hotClickArticles;
    }


}
