package com.articleshare.webproject.service;

import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.article.ArticleScore;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author:"liu.yucheng",
 * @Data:$Date
 */
public interface ArticleService {
    Map qryArticle(Map<String, Object> param);

    Article qryArticleBYId(String articleId);

    Article loadNowArticleInfo();

    List<ArticleScore> qryArticleScoreByaId(Integer articleId);

    List<Map> getHotCollectArticles();

    List<Map> getHotClickArticles();
}
