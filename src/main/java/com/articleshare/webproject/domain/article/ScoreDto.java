package com.articleshare.webproject.domain.article;

/**
 * @author:"liu.yucheng",
 * @Data:$Date
 */
public class ScoreDto {
    Integer articleId;
    Integer Score;
    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getScore() {
        return Score;
    }

    public void setScore(Integer score) {
        Score = score;
    }
}
