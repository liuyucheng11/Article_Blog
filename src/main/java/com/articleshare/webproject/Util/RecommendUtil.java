package com.articleshare.webproject.Util;

import com.articleshare.webproject.dao.userMapper;
import com.articleshare.webproject.domain.article.ArticleScore;
import com.articleshare.webproject.domain.article.ScoreDto;
import com.articleshare.webproject.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * @author:"liu.yucheng",
 * @Data:$Date 推荐算法
 */
public class RecommendUtil {
    @Autowired
    userMapper userMapper;

/*
    public void RecommendAlgorithm() {
        //获取评价记录
        Integer count = 0;
        //定义皮尔逊系数
        double Pearson = 0.0;
        //相似Map
        Map<Integer, Double> PearsonMap = new HashMap<>();
        //推荐文章
        List<Map<Integer, Double>> recommendArticle = new ArrayList<>();

        //根据登录条件判断，未登录则随机推荐高评分
        if (SessionUtil.getSessionAttribute("User") == null) {
        } else {
            //使用皮尔逊相关系数推荐算法(Pearson Correlation Coefficient)根据评分进行推荐
            User loginUser = (User) SessionUtil.getSessionAttribute("User");
            //获取用户打分的结果
            List<ArticleScore> articleScores = userMapper.qryUserArticleScore(Integer.parseInt(loginUser.getUserId()));
            Map<Integer, Integer> loginMp = new HashMap<>();
            //用户评价的文章id
            List<Integer> loginScoreIds = new ArrayList<>();
            for (ArticleScore articleScore : articleScores) {
                loginMp.put(articleScore.getArticleId(), articleScore.getScore());
                loginScoreIds.add(articleScore.getArticleId());
            }
            //寻找获取用户评价过的用户
            List<ArticleScore> allScores = userMapper.getSimilarUser(loginScoreIds);
            //得到用户和评价的关系 Map<userId,List<articleId>>
            Map<Integer, List<ScoreDto>> userScoreMap = new HashMap<>();
            for (ArticleScore articleScore : allScores) {
                if (!userScoreMap.containsKey(articleScore.getUserId())) {
                    userScoreMap.put(articleScore.getUserId(), new ArrayList<ScoreDto>());
                    ScoreDto scoreDto = new ScoreDto();
                    scoreDto.setScore(articleScore.getScore());
                    scoreDto.setArticleId(articleScore.getArticleId());
                    userScoreMap.get(articleScore.getUserId()).add(scoreDto);
                } else {
                    ScoreDto scoreDto = new ScoreDto();
                    scoreDto.setScore(articleScore.getScore());
                    scoreDto.setArticleId(articleScore.getArticleId());
                    userScoreMap.get(articleScore.getUserId()).add(scoreDto);
                }
            }
            //过滤掉不想相似的用户得到用户
            Iterator<Map.Entry<Integer, List<ScoreDto>>> it = userScoreMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, List<ScoreDto>> temp = it.next();
                if (temp.getKey() == Integer.parseInt(loginUser.getUserId())) {
                    count = temp.getValue().size();
                    userScoreMap.remove(temp.getKey());
                }
            }
            while (it.hasNext()) {
                Map.Entry<Integer, List<ScoreDto>> temp = it.next();
                if (!(temp.getValue().size() == count)) {
                    userScoreMap.remove(temp.getKey());
                }
            }
            //把userScoreMap根据Id 存入 Map中模拟二维数组计算值
            for (Integer userId : userScoreMap.keySet()) {
                List<ScoreDto> personScoreDtos = userScoreMap.get(userId);
                Map<Integer, Integer> personScoreMap = new HashMap<>();
                for (ScoreDto scoreDto : personScoreDtos) {
                    personScoreMap.put(scoreDto.getArticleId(), scoreDto.getScore());
                }
                //得到PersonNum
                double PearsonNum = getUserSimilar(loginMp, personScoreMap);
                //Pearson = (Pearson > PearsonNum) ? Pearson:PearsonNum;
                PearsonMap.put(userId, PearsonNum);
            }
            //其他用户的关系
            Map<Integer, List<ArticleScore>> otherScoreMap = new HashMap<>();
            for (Integer userId : PearsonMap.keySet()) {
                List<ArticleScore> articleScores1 = userMapper.qryUserArticleScore(userId);
                otherScoreMap.put(userId, articleScores1);
            }
            for (Integer userId : otherScoreMap.keySet()) {
                List<ArticleScore> otherScores = otherScoreMap.get(userId);
                for (ArticleScore articleScore : otherScores) {
                    //判断是否看过文章相同
                    if (loginMp.get(articleScore.getArticleId()) == null) {
                        //从相关系数Map取出
                        double pearson = PearsonMap.get(userId);
                        //判断相关权值相乘>=3
                        if (pearson * articleScore.getScore() >= 3) {
                            double a = pearson * articleScore.getScore();
                            Map<Integer, Double> map = new HashMap<>();
                            map.put(articleScore.getArticleId(), a);
                            boolean has = false;
                            List<Integer> recommendIds = new ArrayList<>();
                            if (recommendArticle.size() > 0) {
                                for (int i = 0; i < recommendArticle.size(); i++) {
                                    Map<Integer, Double> map2 = recommendArticle.get(i);
                                    Integer a1 = getKeyOrNull(map2);
                                    recommendIds.add(a1);
                                }
                                //判断处理
                                if (recommendIds.contains(articleScore.getArticleId())) {
                                    has = true;
                                } else has = false;
                                if (!has) {
                                    recommendArticle.add(map);
                                }
                            } else {
                                recommendArticle.add(map);
                            }
                        }
                    }
                }
            }
            //把List中的Map中的值按照value降序排序
            Collections.sort(recommendArticle, new Comparator<Map<Integer, Double>>() {
                @Override
                public int compare(Map<Integer, Double> o1, Map<Integer, Double> o2) {
                    Integer articleId_1 = getKeyOrNull(o1);
                    Integer articleId_2 = getKeyOrNull(o2);
                    if( (o1.get(articleId_1)- o2.get(articleId_2)<0)){
                        return 1;
                    }
                    else if(o1.get(articleId_1)- o2.get(articleId_2)==0)
                    {
                        return  0;
                    }
                    else{
                        return  -1;
                    }
                }
            });


        }

    }
*/

    /**
     * 获取用户相似度
     *
     * @param pm1
     * @param pm2
     * @return
     */
    public static double getUserSimilar(Map<Integer, Integer> pm1, Map<Integer, Integer> pm2) {
        int n = 0;// 数量n
        int sxy = 0;// Σxy=x1*y1+x2*y2+....xn*yn
        int sx = 0;// Σx=x1+x2+....xn
        int sy = 0;// Σy=y1+y2+...yn
        int sx2 = 0;// Σx2=(x1)2+(x2)2+....(xn)2
        int sy2 = 0;// Σy2=(y1)2+(y2)2+....(yn)2
        for (Map.Entry<Integer, Integer> pme : pm1.entrySet()) {
            Integer key = pme.getKey();
            Integer x = pme.getValue();
            Integer y = pm2.get(key);
            if (x != null && y != null) {
                n++;
                sxy += x * y;
                sx += x;
                sy += y;
                sx2 += Math.pow(x, 2);
                sy2 += Math.pow(y, 2);
            }
        }
        // p=(Σxy-Σx*Σy/n)/Math.sqrt((Σx2-(Σx)2/n)(Σy2-(Σy)2/n));
        double sd = sxy - sx * sy / n;
        double sm = Math.sqrt((sx2 - Math.pow(sx, 2) / n) * (sy2 - Math.pow(sy, 2) / n));
        return Math.abs(sm == 0 ? 1 : sd / sm);
    }

    /**
     * 获取map中第一个key值
     *
     * @param map 数据源
     * @return
     */
    private static Integer getKeyOrNull(Map<Integer, Double> map) {
        Integer obj = null;
        for (Map.Entry<Integer, Double> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }


}
