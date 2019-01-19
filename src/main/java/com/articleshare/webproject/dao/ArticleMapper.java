package com.articleshare.webproject.dao;

import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.article.ArticleScore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface ArticleMapper {
    public int publishArticle(Map<String, Object> map);

    public List<Article> SearchArticle(String articleTitle);

    public List<Article> qryArticle(@Param("type") String type, @Param("articleName") String articleName, @Param("author") String author);

    public Integer qryArticleCount(@Param("type") String type, @Param("articleName") String articleName, @Param("author") String author);

    public Article qryArticleById(@Param("articleId") String articleId);

    public List<ArticleScore> qryArticleScoreById(@Param("articleId") Integer articleId);

    public List<Map> getHotCollectArticles();

    public List<Map> getHotClickArticles();

    //推荐用户喜欢的类别文章
    public List<Integer> qryHotArticleByType(List<Integer> articleType);

    //根据articleIds查询
    List<Article> qryArticleByIds(List<Integer> articleIds);

    void deleteArticleById (Integer articleId);

}
