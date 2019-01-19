package com.articleshare.webproject.dao;

import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.article.ArticleCollect;
import com.articleshare.webproject.domain.article.ArticleScore;
import com.articleshare.webproject.domain.user.ClickLog;
import com.articleshare.webproject.domain.user.User;
import com.articleshare.webproject.domain.user.UserInterest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface userMapper {
    /*  List<User> findAllUser();*/
    User findByName(String name);

    void registerUser(Map user);

    void deleteById(String userId);

    void CollectArticle(@Param(value = "userId") Integer userId, @Param(value = "articleId") Integer articleId, @Param(value = "collectDate") Date collectDate);

    ArticleCollect checkCollectArticle(@Param(value = "userId") Integer userId, @Param(value = "articleId") Integer articleId);

    List<Map> qryUserCollection(@Param(value = "userId") Integer userId);

    void deleteCollectById(@Param(value = "collectId") String collectId);

    void reportComment(@Param(value = "userId") Integer userId, @Param(value = "articleId") Integer articleId, @Param(value = "theScore") Integer theScore, @Param(value = "theCommentText") String theCommentText, @Param(value = "createDate") Date createDate);

    void recordClickLog(ClickLog clickLog);

    void saveInterest(List<UserInterest> userInterestList);

    void deleteInterest(@Param(value = "userId") Integer userId);

    List<UserInterest> qryInterest(@Param(value = "userId") Integer userId);

    List<ArticleScore> qryUserArticleScore(@Param(value = "userId") Integer userId);

    List<ArticleScore> getSimilarUser(List<Integer> articleIds);

    //查询是否有评价
    Integer checkComment(@Param(value = "userId") Integer userId, @Param(value = "articleId") Integer articleId);

    void resetPassword(@Param(value = "newPassword") String newPassword, @Param(value = "userId") Integer userId);

    List<Article> qryUserArticle(Integer userId);

}
