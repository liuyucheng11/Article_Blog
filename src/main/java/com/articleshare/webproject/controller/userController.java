package com.articleshare.webproject.controller;

import com.articleshare.webproject.Util.RecommendUtil;
import com.articleshare.webproject.Util.SessionUtil;
import com.articleshare.webproject.dao.ArticleMapper;
import com.articleshare.webproject.dao.userMapper;
import com.articleshare.webproject.domain.article.Article;
import com.articleshare.webproject.domain.article.ArticleScore;
import com.articleshare.webproject.domain.article.ScoreDto;
import com.articleshare.webproject.domain.user.ClickLog;
import com.articleshare.webproject.domain.user.LoginDto;
import com.articleshare.webproject.domain.article.PublishArticleDto;
import com.articleshare.webproject.domain.user.User;
import com.articleshare.webproject.domain.user.UserInterest;
import com.articleshare.webproject.service.UserService;
import com.articleshare.webproject.tools.NewHomeUrl;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.text.SimpleDateFormat;

@RestController
public class userController {
    @Autowired
    userMapper userMapper;
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    UserService userService;

    /*@RequestMapping(value = "getUers")
   public List<User> getUsers() {
       return userMapper.findAllUser();

   }*/
    @RequestMapping(value = "qryUserArticles.do")
    @ResponseBody
    public Object qryUserArticles() {
        User user = (User)SessionUtil.getSessionAttribute("User");
        List<Article> personArticles = userMapper.qryUserArticle(Integer.parseInt(user.getUserId()));
        return personArticles;
    }

    @RequestMapping(value = "resetPassword.do")
    public boolean resetPassword(@RequestBody Map<String, String> map) {
        User user = (User) SessionUtil.getSessionAttribute("User");
        String oldPassword = user.getUserPassword();
        if (oldPassword.equals(map.get("oldPassword"))) {
            String newPassword = map.get("newPassword");
            try {
                userMapper.resetPassword(newPassword, Integer.parseInt(user.getUserId()));
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 退出系统移除用户Session
     */
    @RequestMapping(value = "quitSystem.do")
    public void quitSystem() {

        SessionUtil.removeSession("User");
    }

    @RequestMapping(value = "findByName")
    public User findByName() {
        return userMapper.findByName("abc");
    }

    @RequestMapping("/deleteById")
    public void deleteById() {
        userMapper.deleteById("10000002");
    }

    @RequestMapping(value = "check/userUnique")
    @ResponseBody
    public Map checkUserUnique(String username) {
        Map<String, Boolean> map = new HashMap<>();
        User user = userMapper.findByName(username);
        if (StringUtils.isEmpty(user))//查询为空
        {
            map.put("valid", true);
        } else {
            map.put("valid", false);
        }
        return map;
    }

    @RequestMapping(value = "registerUser", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> registerUser(@RequestBody User user) {
        Map<String, Object> userRegister = new HashMap<>();
        userRegister.put("userName", user.getUserName());
        userRegister.put("userAge", user.getUserAge());
        userRegister.put("userAddress", user.getUserAddress());
        userRegister.put("userPassword", user.getUserPassword());
        userRegister.put("userOccupation", user.getUserOccupation());
        userRegister.put("sex", user.getSex());
        userRegister.put("registerDate", user.getRegisterDate());
        userMapper.registerUser(userRegister);
        Map<String, Object> result = new HashMap<>();
        result.put("status", "successs");
        return result;
    }

    @RequestMapping(value = "dologin.do", method = RequestMethod.POST)
    @ResponseBody
    //实现登录跳转
    public Map<String, String> dologin(@RequestBody LoginDto loginDto) {

        String userName = loginDto.getUserName();
        String passWord = loginDto.getPassWord();
        User user = userMapper.findByName(userName);
        Map<String, String> map = new HashMap<>();
        if (StringUtils.isEmpty(user)) {
            map.put("status", "notFound");//用户为空
        } else {
            if (passWord.equals(user.getUserPassword())) {
                map.put("status", "success");
                map.put("userName", user.getUserName());
                //设置Session
                SessionUtil.setSessionAttribute("User", user);
            } else {
                map.put("status", "passwordError");
            }
        }
        return map;
    }

    @RequestMapping(value = "publishArticle.do", method = RequestMethod.POST)
    @ResponseBody
    //发表文章
    public Map<String, String> publishArticle(@RequestBody PublishArticleDto publishArticleDto) throws java.io.FileNotFoundException {
        String author = publishArticleDto.getAuthor();
        String content = publishArticleDto.getContent();
        String title = publishArticleDto.getTitle();
        String articleDesc = publishArticleDto.getArticleDesc();
        Integer articleType = publishArticleDto.getArticleType();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date publishDate = new java.sql.Date(new java.util.Date().getTime());
        Map<String, Object> insertValues = new HashMap<>();
        insertValues.put("userId", userMapper.findByName(author).getUserId());
        insertValues.put("articleName", title);
        insertValues.put("articleType", articleType);
        insertValues.put("articleDesc", articleDesc);
        insertValues.put("publishDate", publishDate);
        Boolean writetd = true;
        String catalog = "d:\\Article_recommend_catalog\\";
        //本地目录
        String homeUrl = catalog + title + NewHomeUrl.newHomeUrl() + ".txt";
        insertValues.put("homeUrl", homeUrl);
//        insertValues.put("articleType","1");
        File articleFile = new File(homeUrl);
        //创建文件存储文本
        if (!articleFile.exists()) {
            try {
                /*
                写入文件
                 */
                PrintStream ps = new PrintStream(new FileOutputStream(articleFile));
                ps.print(content);
                //插入数据库
                articleMapper.publishArticle(insertValues);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                writetd = false;
            }
        }
        Map<String, String> result = new HashMap<>();
        if (writetd) {
            result.put("status", "success");
            result.put("artileTitle", title);
        } else {
            result.put("status", "fail");
        }
        return result;

    }

    /**
     * 取登录人信息
     *
     * @return
     */
    @RequestMapping(value = "getUserInfo.do")
    public User getUserInfo() {
        User loginUer = (User) SessionUtil.getSessionAttribute("User");
        return loginUer;
    }

    /**
     * 查询登录人的收藏
     */
    @RequestMapping(value = "qryUserCollection.do")
    @ResponseBody
    public Object qryUserCollection() {
        User user = (User) SessionUtil.getSessionAttribute("User");
        Map<String, Object> resultMap = new HashMap();
        List<Map> list = userService.qryUserCollection(Integer.parseInt(user.getUserId()));
        //查询无结果
        if (list.size() == 0) {
            resultMap.put("Message", "NOT_RESULT");
            return resultMap;
        } else {
            resultMap.put("Message", "HAS_RESULT");
            resultMap.put("collectionSet", list);
            return resultMap;
        }
    }

    /**
     * 删除收藏记录
     *
     * @param collectId
     * @return
     */
    @RequestMapping(value = "deleteCollectById.do")
    @ResponseBody
    public boolean deleteCollectById(@RequestBody String collectId) {
        return userService.deleteCollectById(collectId);
    }

    /**
     * 发表评价
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "reportComment.do")
    @ResponseBody
    public boolean reportComment(@RequestBody Map param) {
        User user = (User) SessionUtil.getSessionAttribute("User");
        Article article = (Article) SessionUtil.getSessionAttribute("nowReadingArticle");
        Integer score = Integer.parseInt(param.get("theScore").toString());
        String commentText = param.get("theCommentText").toString();
        java.sql.Date createDate = new java.sql.Date(new java.util.Date().getTime());
        //防止重复发表评价
        if (userService.checkComment(Integer.parseInt(user.getUserId()), article.getArticleId())) {
            return false;
        }

        try {
            userService.reportComment(Integer.parseInt(user.getUserId()), article.getArticleId(), score, commentText, createDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 插入阅读点击日志
     *
     * @param
     */
    @RequestMapping(value = "recordClickLog.do")
    @ResponseBody
    public void recordClickLog() {
        ClickLog clickLog = new ClickLog();
        if (SessionUtil.getSessionAttribute("User") == null) {
            clickLog.setUserId(null);
        } else {
            User user = (User) SessionUtil.getSessionAttribute("User");
            clickLog.setUserId(Integer.parseInt(user.getUserId().toString()));
        }
        Article article = (Article) SessionUtil.getSessionAttribute("nowReadingArticle");
        clickLog.setArticleId(article.getArticleId());
        clickLog.setCreateDate(new java.sql.Date(new java.util.Date().getTime()));
        try {
            userService.recordClickLog(clickLog);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @RequestMapping(value = "saveInterest.do")
    @ResponseBody
    public boolean saveInterest(@RequestBody Integer interests[]) {
        List<UserInterest> userInterests = new ArrayList<>();
        User user = (User) SessionUtil.getSessionAttribute("User");
        for (int i = 0; i < interests.length; i++) {
            UserInterest userInterest = new UserInterest();
            userInterest.setUserId(Integer.parseInt(user.getUserId()));
            userInterest.setArticleTypeId(interests[i]);
            userInterests.add(userInterest);
        }
        try {
            userMapper.deleteInterest(Integer.parseInt(user.getUserId()));
            userService.saveInterest(userInterests);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;

    }

    @RequestMapping(value = "recommendArticle.do")
    @ResponseBody
    public Object recommendArticle() {
        List<Integer> RecommendArticleIds = (List<Integer>) RecommendAlgorithm();
        if (CollectionUtils.isEmpty(RecommendArticleIds)) {
            return null;
        } else {
            return articleMapper.qryArticleByIds(RecommendArticleIds);
        }
    }

    @RequestMapping(value = "qryUserInterests.do")
    @ResponseBody
    public Object qryUserInterests() {
        User user = (User) SessionUtil.getSessionAttribute("User");
        return userMapper.qryInterest(Integer.parseInt(user.getUserId()));
    }

    //混合推荐算法
    public Object RecommendAlgorithm() {
        //获取评价记录
        Integer count = 0;
        //定义皮尔逊系数
        double Pearson = 0.0;
        //相似Map
        Map<Integer, Double> PearsonMap = new HashMap<>();
        //推荐文章
        List<Map<Integer, Double>> recommendArticle = new ArrayList<>();
        //最终推荐的文章
        List<Integer> finalArticle = new ArrayList<>();

        //根据登录条件判断，未登录则随机推荐高评分
        if (SessionUtil.getSessionAttribute("User") == null) {
            return null;
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
            //过滤掉不想相似的用户
            Iterator<Map.Entry<Integer, List<ScoreDto>>> it = userScoreMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Integer, List<ScoreDto>> temp = it.next();
                if (temp.getKey() == Integer.parseInt(loginUser.getUserId())) {
                    count = temp.getValue().size();
                    it.remove();
                }
            }
            Iterator<Map.Entry<Integer, List<ScoreDto>>> it2 = userScoreMap.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry<Integer, List<ScoreDto>> temp = it2.next();
                if (!(temp.getValue().size() == count)) {
                    it2.remove();
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
                    if ((o1.get(articleId_1) - o2.get(articleId_2) < 0)) {
                        return 1;
                    } else if (o1.get(articleId_1) - o2.get(articleId_2) == 0) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            });
            //选取协同推荐权值最高的五篇文章
            if (!(recommendArticle.size() == 0)) {
                for (int i = 0; i < recommendArticle.size(); i++) {
                    finalArticle.add(getKeyOrNull(recommendArticle.get(i)));
                }
            }
            while (finalArticle.size() > 5) {
                finalArticle.remove(finalArticle.size() - 1);
            }
            //查询loginUser喜欢什么样的文章将这些文章存入最后推荐
            List<UserInterest> userInterests = userMapper.qryInterest(Integer.parseInt(loginUser.getUserId()));
            List<Integer> recommendByInterest = new ArrayList<>();
            //兴趣设置不为空
            if (!(userInterests.size() == 0)) {
                List<Integer> types = new ArrayList<>();
                for (int i = 0; i < userInterests.size(); i++) {
                    types.add(userInterests.get(i).getArticleTypeId());
                }
                recommendByInterest = articleMapper.qryHotArticleByType(types);
            }
            if (recommendByInterest.size() != 0) {
                for (int i = 0; i < recommendByInterest.size(); i++) {
                    if (!finalArticle.contains(recommendByInterest.get(i))) {
                        finalArticle.add(recommendByInterest.get(i));
                    }
                }
            }
            return finalArticle;
        }
    }

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
        double sd = sxy - sx * sy /(double)n;
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

    private static Integer getKey(Map<Integer, Integer> map) {
        Integer obj = null;
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }

}


