package com.articleshare.webproject.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NewHomeUrl {
    public  static String newHomeUrl()
    {
        //生成本地目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        //随机数
        Random random = new Random();
        String url = sdf.format(new Date())+(random.nextInt(1000)+1);
         return  url;
    }
}
