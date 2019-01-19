package com.articleshare.webproject.domain.user;
import java.sql.Date;
public class ClickLog {
    private Integer ClickLogId;
    private Integer userId;
    private Integer articleId;
    private Date createDate;


    public Integer getClickLogId() {
        return ClickLogId;
    }

    public void setClickLogId(Integer clickLogId) {
        ClickLogId = clickLogId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
