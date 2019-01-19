package com.articleshare.webproject.service;

import com.articleshare.webproject.dao.userMapper;
import com.articleshare.webproject.domain.user.ClickLog;
import com.articleshare.webproject.domain.user.UserInterest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author:"liu.yucheng",
 * @Data:$Date
 */
@Service("UserService")
public class UserServiceImpl implements UserService {
    @Autowired
    userMapper userMapper;

    @Override
    public void CollectArticle(Integer userId, Integer articleId, Date collectDate) throws SQLException {
        userMapper.CollectArticle(userId, articleId, collectDate);
    }

    @Override
    public boolean checkCollectArticle(Integer userId, Integer articleId) {
        if (userMapper.checkCollectArticle(userId, articleId) == null) {
            return true;
        } else
            return false;
    }

    @Override
    public List<Map> qryUserCollection(Integer userId) {
        return userMapper.qryUserCollection(userId);
    }

    @Override
    public boolean deleteCollectById(String collectId) {
        try {
            userMapper.deleteCollectById(collectId);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void reportComment(Integer userId, Integer articleId, Integer score, String comment, Date createDate) {
        userMapper.reportComment(userId, articleId, score, comment, createDate);
    }

    @Override
    public void recordClickLog(ClickLog clickLog) {
        userMapper.recordClickLog(clickLog);
    }

    @Override
    public void saveInterest(List<UserInterest> userInterestList) {
        userMapper.saveInterest(userInterestList);
    }

    @Override
    public boolean checkComment(Integer userId ,Integer articleId){
      if(userMapper.checkComment(userId,articleId)!=null){
          return  true;
      }
      else
          return  false;
    }

}
