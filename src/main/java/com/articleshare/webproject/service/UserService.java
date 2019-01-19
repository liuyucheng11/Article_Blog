package com.articleshare.webproject.service;

import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.user.ClickLog;
import com.articleshare.webproject.domain.user.UserInterest;
import org.omg.PortableInterceptor.INACTIVE;

import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * @author:"liu.yucheng",
 * @Data:$Date
 */
public interface UserService {
    public void CollectArticle(Integer userId, Integer ArticleId, Date collectDate) throws Exception;

    public boolean checkCollectArticle(Integer userId, Integer articleId);

    public List<Map> qryUserCollection(Integer userId);

    public boolean deleteCollectById(String collectId);

    public void reportComment(Integer userId, Integer articleId, Integer score, String comment, Date createDate);

    void recordClickLog(ClickLog clickLog);

    void saveInterest(List<UserInterest> userInterestList);

    boolean checkComment(Integer userId, Integer articleId);

}
