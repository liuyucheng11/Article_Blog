//查询数据
var isLogin;
//查询对象
var test = {};
test.startPage = 1;
test.pageSize = 6;
test.type = '';
test.author = '';

var hotTemplate_1 = " <li>\n" +
    "                      <div class=\"book-info\">\n" +
    "                          <h3>ranking</h3>\n" +
    "                          <h4>articleTitle</h4>\n" +
    "                          <p class=\"digital\">\n" +
    "                              <em>collectNum</em>收藏数\n" +
    "                          </p>\n" +
    "                           <h4 class='article-id' style='display: none'>articleId</h4>             " +
    "                          <p class=\"author\">\n" +
    "                              <a>classify</a>\n" +
    "                              <i>·</i>\n" +
    "                              <a>authorName</a>\n" +
    "                          </p>\n" +
    "                      </div>\n" +
    "                  </li>";
var hotTemplate_2 = "<li>\n" +
    "                        <div class=\"book-info\">\n" +
    "                            <h3>ranking</h3>\n" +
    "                            <h4>articleTitle</h4>\n" +
    "                            <p class=\"digital\">\n" +
    "                                <em>clickNum</em>点击数\n" +
    "                            </p>\n" +
    "                           <h4 class='article-id' style='display: none'>articleId</h4>             " +
    "                            <p class=\"author\">\n" +
    "                                <a>classify</a>\n" +
    "                                <i>·</i>\n" +
    "                                <a>authorName</a>\n" +
    "                            </p>\n" +
    "                        </div>\n" +
    "                    </li>";
var recommend_article = "<li>\n" +
    "            <div class=\"book-info\">\n" +
    "                <h4>article-title</h4>\n" +
    "                <h4 class='article-id' style='display: none'>articleId</h4>\n" +
    "                <div>\n" +
    "                    <button class=\"btn-success read-the-article btn-sm\">阅读</button>\n" +
    "                    <button class=\"btn-warning collect-article btn-sm\">收藏</button>\n" +
    "                    <button class=\"btn-primary show-info btn-sm\">查看</button>\n" +
    "                </div>\n" +
    "                <p class=\"author\">\n" +
    "                    <a>classify</a>\n" +
    "                    <i>·</i>\n" +
    "                    <a>authorName</a>\n" +
    "                </p>\n" +
    "            </div>\n" +
    "        </li>";
var  my_article ="<li>\n" +
    "                        <h3 style=\"display: none\" >articleId</h3>\n" +
    "                        <h4>articleTitle</h4>\n" + "<h4>publishDate</h4>"+
    "                        <div class=\"operation\">\n" +
    "                            <button class=\"btn btn-primary btn-sm read-article\">开始阅读</button>\n" +
    "                            <button class=\"btn btn-warning btn-sm show-baseInfo\">查看信息</button>\n" +
    "                            <button type=\"button\" class=\"btn btn-danger btn-sm delete-my-article\" data-toggle=\"modal\"\n" +
    "                                    data-target=\"#delete-my-article\">删除文章\n" +
    "                            </button>\n" +
    "                        </div>\n" +
    "                    </li>";


function qryArticleData(obj) {
    var liHtml = undefined;
    liHtml = $("#demo").html();
    $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            url: "qryArticle.do",
            data: JSON.stringify(obj),
            //回调函数
            success: function (result) {
                console.log(result);
                $("li.article-list").remove();
                //移除页码信息
                $("span.pageIndex").remove();
                var articledatas = result.articlePageInfo.list;
                var articleCount = result.articleNum;
                $(".count").html("一共" + " " + articleCount);
                $(".totalPageNum").html(result.articlePageInfo.pages);
                //生成分页信息
                if (result.articlePageInfo.pages > 8) {
                    for (var i = 8; i > 0; i--) {
                        var pageIndex = "<span class='pageIndex'><a>" + i + "</a>&nbsp;</span>";
                        $(".previous-page").after(pageIndex);
                    }
                    ;
                    $(".previous-page").after("<span class='morePage'><a>...</a>");
                }
                else {
                    for (var i = result.articlePageInfo.pages; i > 0; i--) {

                        var pageIndex = "<span class='pageIndex'><a>" + i + "</a>&nbsp;</span>";
                        $(".previous-page").after(pageIndex);
                    }
                }
                $(".nowPageIndex").html(obj.startPage);
                //遍历数组
                $.each(articledatas, function (index, val) {
                    var tempHtml = liHtml;
                    tempHtml = tempHtml.replace("tempId", val.articleId);
                    tempHtml = tempHtml.replace("书籍1", val.articleTitle);
                    tempHtml = tempHtml.replace("作者1", val.userName);
                    tempHtml = tempHtml.replace("情感", val.articleTypeName)
                    tempHtml = tempHtml.replace("2018-09-12", val.publishDate);
                    tempHtml = tempHtml.replace(" the summary", val.articleDesc);
                    var tempHtml_2 = tempHtml + " </li>";
                    var tempHtml_3 = " <li class='article-list'>" + tempHtml_2;
                    //追加元素
                    $("ul.search-result").append(tempHtml_3);
                });
                //为文章绑定点击事件
            }
        }
    )
};

function findType(val) {
    switch (val) {
        case 1:
            return '情感';
            break;
        case 2:
            return '励志';
            break;
        case 3:
            return '悬疑';
            break;
        case 4:
            return '搞笑';
            break;
        case 5:
            return '生活';
            break;
    }
};
//获取鼠标坐标函数

$(function () {
    $(".hot-articles").show(1000, function () {
        $(".book-list li").remove();

        $.post("getHotArticles.do", function (result) {
            console.log(result);
            var collectHot = result.hotCollectArticles;
            var clickHot = result.hotClickArticles;
            $.each(collectHot, function (index, element) {
                var liHtml = hotTemplate_1;
                liHtml = liHtml.replace("ranking", "NO." + element.ranking);
                liHtml = liHtml.replace("articleTitle", element.article_name);
                liHtml = liHtml.replace("collectNum", element.count);
                liHtml = liHtml.replace("articleId", element.articleId);
                liHtml = liHtml.replace("authorName", element.user_name);
                liHtml = liHtml.replace("classify", findType(element.article_type));
                $(".collect-hot-articles ul").append(liHtml);

            });
            $.each(clickHot, function (index, element) {
                var liHtml = hotTemplate_2;
                liHtml = liHtml.replace("ranking", "NO." + element.ranking);
                liHtml = liHtml.replace("articleTitle", element.article_name);
                liHtml = liHtml.replace("clickNum", element.count);
                liHtml = liHtml.replace("articleId", element.articleId);
                liHtml = liHtml.replace("authorName", element.user_name);
                liHtml = liHtml.replace("classify", findType(element.article_type));
                $(".click-hot-articles ul").append(liHtml);

            });
            //渲染排行榜
            $(".collect-hot-articles .book-list li").each(function (index, element) {
                if (index == 0) {
                    $(this).find("h3").css("background", "#bf0f1d");
                }
                else if (index == 1) {
                    $(this).find("h3").css("background", "#bf5d4e");
                }
                else if (index == 2) {
                    $(this).find("h3").css("background", "#ff8077");

                }
                else {
                    $(this).find("h3").css("background", "#ffdcb5");
                }
            });
            $(".click-hot-articles .book-list li").each(function (index, element) {
                if (index == 0) {
                    $(this).find("h3").css("background", "#bf0f1d");
                }
                else if (index == 1) {
                    $(this).find("h3").css("background", "#bf5d4e");
                }
                else if (index == 2) {
                    $(this).find("h3").css("background", "#ff8077");

                }
                else {
                    $(this).find("h3").css("background", "#ffdcb5");
                }
            });
        })
    }).siblings("div").hide();

    //绑定搜索事件
    $("#search").click(function () {
        test.articleName = $("#searchArticle").val();
        test.startPage = 1;
        test.type = '';
        qryArticleData(test);
        $("#search-result").show(500).siblings("div").hide();
    });
    //点击个人中心事件
    $(".dropdown-menu .reset-password").click(function () {
        $("#reset-password").modal('show');

    });
    $("#reset-pass").click(function () {
        var oldPassword = $("#old-password").val();
        var newPassword = $("#new-password-1").val();
        var confirmPassword = $("#new-password-2").val();
        if (oldPassword == null || oldPassword == '' || newPassword == null || newPassword == '' || confirmPassword == '' || confirmPassword == null) {
            alert("请填写必要信息！");
            return;
        }
        if(confirmPassword != newPassword){
            alert("两次密码输入不一致");
            $("#new-password-2").focus();
            return;
        }
        var obj = new Object();
        obj["oldPassword"] = oldPassword;
        obj['newPassword'] = newPassword;
        obj['confirmPassword'] = confirmPassword;
        $.ajax({
            type:'post',
            url: "resetPassword.do",
            data: JSON.stringify(obj),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            success:function (result) {
                if(result==true){
                    alert("密码修改成功！");
                }
                else {
                    alert("原密码错误！修改失败")
                }
                $("#reset-password").modal('hide');
            }
        })

    });
    //退出系统
    $(".exit-system").click(function () {
        $.post("quitSystem.do",function () {
            $("#collection-msg .modal-body").html("您已退出登录");
            $("#collection-msg").modal("show");
            setTimeout(function () { window.location.href ="/login"; },1000);

        });

    });


});
$(function () {
    //校验登录
    $.post("checkLogin.do", function (result) {
        isLogin = result;
        console.log(result);
    });
    //显示 "我的文章"
    $("a.my-article").click(
        function () {
            $("div.my-article").show(function () {
                $(".user-info h3").html(user.userName);
                $(".user-info>p").html(user.userId);

            }).siblings("div").hide();
        }
    );
    //变量当前页号
    $(document).on('click', '.read-article', function () {
        var articleId = $(this).parent().parent().find("h1").html();
        console.log(articleId);
        ajaxForArticleClick(articleId, "saveNowLogin.do", "reading");
    });
    $(document).on('click', '.add-to-collection', function () {
        var articleId = $(this).parent().parent().find("h1").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "collecting");
    });
    $(document).on('click', '.show-baseInfo', function () {
        var articleId = $(this).parent().parent().find("h1").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "seeDetail");
    });
    //点击页号编码查看相关信息
    $(document).on('click', '.pageIndex a', function () {
        var pageNum = $(this).html();
        test.startPage = pageNum;
        //移除之前查找结果
        $("li.article-list").remove();
        //移除页码信息
        $("span.pageIndex").remove();
        qryArticleData(test);

    });
    //输入页号进行查询
    $(".jump-page").click(function () {
        var pageNum = $('.input-page-num').val();
        var maxPageNum = $('.totalPageNum').html()
        if (pageNum == '' || pageNum == undefined) {
            alert("请输入页号！");
            return;
        }
        if (pageNum > maxPageNum) {
            alert('页号超出总范围！');
            return;
        }
        searchPageIndex(pageNum);
    });
    $(".previous-page").click(function () {
        //当前页
        var nowPageIndex = $(".nowPageIndex").html();
        if (nowPageIndex == 1) {
            alert("已经是最小页啦！");
            return
        }
        test.startPage = --nowPageIndex;
        $("li.article-list").remove();
        //移除页码信息
        $("span.pageIndex").remove();
        qryArticleData(test);
    });
    $(".next-page").click(function () {
        //当前页
        var nowPageIndex = $(".nowPageIndex").html();
        var totalPageNum = $(".totalPageNum").html();

        if (nowPageIndex == totalPageNum) {
            alert("已经是最大页啦！");
            return
        }
        test.startPage = ++nowPageIndex;
        $("li.article-list").remove();
        //移除页码信息
        $("span.pageIndex").remove();
        qryArticleData(test);
    });
    //点击删除收藏
    $(document).on("click", ".delete-collection", function () {
    });
    //移除文章模态框对应事件
    $('#exampleModal').on('show.bs.modal', function (event) {
        var $button = $(event.relatedTarget); // 触发事件的按钮
        var modal = $(this); //获得模态框本身
        modal.find(".remove-collections").on("click", function () {
            //执行ajax移除后台数据
            //获取收藏ID
            var collectId = $button.parent().parent().find(".collect-id").html();
            $.ajax({
                type: 'post',
                data: collectId,
                url: "deleteCollectById.do",
                success: function (result) {
                    if (result == true) {
                        $button.parent().parent().remove();
                        $(this).find(".modal-body").html("成功从收藏夹中移除改文章");
                        $('#exampleModal').modal('hide');
                        alert("成功移除");
                    } else {
                        alert("删除失败！");
                    }
                }
            })

        });
    });
    $('#delete-my-article').on('show.bs.modal', function (event) {
        var $button = $(event.relatedTarget); // 触发事件的按钮
        var modal = $(this); //获得模态框本身
        modal.find(".delete-mine").on("click", function () {
            //执行ajax移除后台数据
            //获取收藏ID
            var articleId = $button.parent().parent().find("h3").html();
            $.ajax({
                type: 'post',
                data:articleId,
                contentType: "application/json; charset=utf-8",
                url: "deleteArticleById.do",
                success: function (result) {
                    if (result == true) {
                        $button.parent().parent().remove();
                        $(this).find(".modal-body").html("成功从收藏夹中移除改文章");
                        $('#delete-my-article').modal('hide');
                        alert("成功删除");
                    } else {
                        alert("删除失败！");
                    }
                }
            })

        });
    });
    //绑定我的文章下的按钮
    $("#my-write-article").on("click",".read-article",function () {
        var articleId = $(this).parent().parent().children("h3").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "reading");
    });
    $("#my-write-article").on("click",".show-baseInfo",function () {
        var articleId = $(this).parent().parent().children("h3").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "seeDetail");
    });




});

//通用ajax请求
function ajaxForArticleClick(data, url, action) {
    $.ajax({
        type: 'POST',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: url,
        data: data,
        success: function (result) {
            //跳转页面
            if (action == "reading") {
                window.open("/readArticle");
                //点击量+1可在未登录下实现
                $.post("recordClickLog.do", function () {
                });
            }
            if (action == "collecting") {
                var $model = $("#collection-msg");
                if (isLogin == false) {
                    $model.find(".modal-body").html("您当前未登录，请登录后再在执行此操作哦！");
                    $model.modal('show');
                    return;
                }
                $.post("addToPersonCollect.do", function (result) {
                    if (result == true) {
                        $model.find(".modal-body").html("加入收藏成功！");
                        $model.modal('show');
                    }
                    else {
                        $model.find(".modal-body").html("您的收藏夹中已有此文章啦！");
                        $model.modal('show');
                    }
                })
            }
            if (action == "seeDetail") {
                window.open("/showArticleInfo");
            }
        }

    });

}

//根据页号进行查找
function searchPageIndex(pageNum) {
    test.startPage = pageNum;
    test.pageSize = 6;
    // test.type = '';
    // test.author = '';
    // test.articleName = '';
    //移除之前查找结果
    $("li.article-list").remove();
    //移除页码信息
    $("span.pageIndex").remove();
    qryArticleData(test);
}

$(function () {
    //点击主页上面的触发事件
    /*
    * 点击我的文章*/
    $(".my-article").click(function () {
        var url = "qryMyArticle.do";
        $.post()

    });
    $(".classify li").click(function () {
        var type = $(this).find("a").attr("type-id");
        if (type != null && type != undefined) {
            test.startPage = 1 ;
            test.articleName = '';
            test.type = type;
            qryArticleData(test);
            $("#search-result").show(500).siblings("div").hide();
        }
        else {
            //获取最近的热门文章
            // $.post("getHotArticles.do",function (result) {
            //     var obj = result;
            // })
            $(".hot-articles").show(1000, function () {
                $(".book-list li").remove();

                $.post("getHotArticles.do", function (result) {
                    console.log(result);
                    var collectHot = result.hotCollectArticles;
                    var clickHot = result.hotClickArticles;
                    $.each(collectHot, function (index, element) {
                        var liHtml = hotTemplate_1;
                        liHtml = liHtml.replace("ranking", "NO." + element.ranking);
                        liHtml = liHtml.replace("articleTitle", element.article_name);
                        liHtml = liHtml.replace("collectNum", element.count);
                        liHtml = liHtml.replace("articleId", element.articleId);
                        liHtml = liHtml.replace("authorName", element.user_name);
                        liHtml = liHtml.replace("classify", findType(element.article_type));
                        $(".collect-hot-articles ul").append(liHtml);

                    });
                    $.each(clickHot, function (index, element) {
                        var liHtml = hotTemplate_2;
                        liHtml = liHtml.replace("ranking", "NO." + element.ranking);
                        liHtml = liHtml.replace("articleTitle", element.article_name);
                        liHtml = liHtml.replace("clickNum", element.count);
                        liHtml = liHtml.replace("articleId", element.articleId);
                        liHtml = liHtml.replace("authorName", element.user_name);
                        liHtml = liHtml.replace("classify", findType(element.article_type));
                        $(".click-hot-articles ul").append(liHtml);

                    });
                    //渲染排行榜
                    $(".collect-hot-articles .book-list li").each(function (index, element) {
                        if (index == 0) {
                            $(this).find("h3").css("background", "#bf0f1d");
                        }
                        else if (index == 1) {
                            $(this).find("h3").css("background", "#bf5d4e");
                        }
                        else if (index == 2) {
                            $(this).find("h3").css("background", "#ff8077");

                        }
                        else {
                            $(this).find("h3").css("background", "#ffdcb5");
                        }
                    });
                    $(".click-hot-articles .book-list li").each(function (index, element) {
                        if (index == 0) {
                            $(this).find("h3").css("background", "#bf0f1d");
                        }
                        else if (index == 1) {
                            $(this).find("h3").css("background", "#bf5d4e");
                        }
                        else if (index == 2) {
                            $(this).find("h3").css("background", "#ff8077");

                        }
                        else {
                            $(this).find("h3").css("background", "#ffdcb5");
                        }
                    });
                })
            }).siblings("div").hide();
        }

    });
    //点击热门上触发事件
    $(".book-list").on("click", "ul li", function () {
        var articleId = $(this).find(".article-id").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "seeDetail");
    });
//点击li事件
    $(".li-menu li").click(function () {
        //对应div显示
        var divClass = "div." + $(this).attr("class");
        var url;
        if ($(this).attr("class") == "my-write-article") {
            url = "qryUserArticles.do";
        } else if ($(this).attr("class") == "my-collection") {
            url = "qryUserCollection.do";
        } else if ($(this).attr("class") == "my-follow") {
            url = "qryUserFollow.do";
        } else if ($(this).attr("class") == "my-score") {
            url = "qryUserScore.do";
        }
        $(divClass).fadeIn(1000, function () {
            qryForPersonalMsg(url, divClass);
        }).siblings("div").hide();
    });
    //模态框
    $('#myModal').modal('hide');
    //收藏夹移动显示收藏时间
    /* $(document).on("hover",".my-collections h4",function () {
         $(this).hide();

     },function () {
         $(this).show();
     })*/
    /*$("#my-collection h4").on({
        mouseenter:function(){
            $(this).css('border-color','#ccc');
            alert("aaa");
        },
        mouseleave:function(){
            $(this).css('border-color','#ff1f58');
        }
    })*/
    $("#my-collection").on("mouseover", "h4", function (e) {
        var collectDate = $(this).parent().find(".collect-date").html();
        var divHtml = "<div id='collect-msg'>您于" + collectDate + "收藏此文章</div> ";
        $('body').append(divHtml);
        $("#collect-msg").css(
            {
                "background-color": "yellow",
                "top": (e.pageY) + "px",
                "position": "absolute",
                "left": (e.pageX) + "px",
            }
        )
    });
    $("#my-collection").on("mouseout", "h4", function () {
        $("#collect-msg").remove();
    });
    $("#my-collection").on("click", ".read-article", function () {
        var articleId = $(this).parent().parent().find(".article-id").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "reading");
    });
    $("#my-collection").on("click", ".show-baseInfo", function () {
        var articleId = $(this).parent().parent().find(".article-id").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "seeDetail");

    });
    //点击兴趣设置触发事件
    $(".interest-settings").click(function () {
        //显示界面
        $("#interest-settings").show(500, function () {
            //查询用户的兴趣

            $.post("qryUserInterests.do", function (result) {
                console.log(result);
                var interestArray = [];
                $.each(result, function (i, e) {
                    interestArray.push(e.articleTypeId);
                });
                $('#article-type').selectpicker('val', interestArray).trigger("change");
            });
            $("#msg").modal('show');
        }).siblings("div").hide();
    });
    //保存用户的兴趣设置
    $("#save-your-interest").click(function () {
        var $model = $("#collection-msg");
        if (isLogin == false) {
            $model.find(".modal-body").html("您当前未登录，请登录后再在执行此操作哦！");
            $model.modal('show');
            return;
        }
        var interests = $("#article-type").val();
        $.ajax({
            type: 'post',
            dataType: "json",
            data: JSON.stringify(interests),
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            url: "saveInterest.do",
            success: function () {
                alert("保存成功");
            }

        })


    });
    //点击推荐按钮弹出推荐
    $("#recommend").click(function () {
        $(".today-recommend").fadeIn(800).siblings("div").hide();
        $.post("recommendArticle.do", function (result) {
            console.log(result);
            var obj = result;
            $.each(obj, function (index, element) {
                var template = recommend_article;
                template = template.replace("articleId", element.articleId);
                template = template.replace("article-title", element.articleTitle);
                template = template.replace("authorName", element.userName);
                template = template.replace("classify", element.articleTypeName);
                $(".today-recommend ul").append(template);
            });


        })

    });
    //绑定推荐界面下的按钮事件
    $(".today-recommend").on("click", ".read-the-article", function () {
        var articleId = $(this).parent().parent().find(".article-id").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "reading");
    });
    $(".today-recommend").on("click", ".collect-article", function () {
        var articleId = $(this).parent().parent().find(".article-id").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "collecting");
    });
    $(".today-recommend").on("click", ".show-info", function () {
        var articleId = $(this).parent().parent().find(".article-id").html();
        ajaxForArticleClick(articleId, "saveNowLogin.do", "seeDetail");
    });


});

//请求后端数据
function qryForPersonalMsg(url, divClass) {
    var $model = $("#collection-msg");
    if (isLogin == false) {
        $model.find(".modal-body").html("您当前未登录，请登录后再在执行此操作哦！");
        $model.modal('show');
        return;
    }
    if (url == "qryUserArticles.do") {
        $("div.my-write-article li").remove();
        $.post(url, function (result) {
            var obj = result;
            console.log(obj);
             $.each(obj,function (i,e) {
               var temp =   my_article;
               temp = temp.replace("articleId",e.articleId);
               temp = temp.replace("articleTitle",e.articleTitle);
               temp = temp.replace("publishDate",e.publishDate);
               $("div.my-write-article ul").append(temp);
             });
        })
    }
    if (url == "qryUserCollection.do") {
        $("#my-collection>ul").find(".my-collections").remove();
        $.post(url, function (result) {
            var obj = result;
            var liHtml = $("#collect-demo").html();
            if (result.Message == "NO_RESULT") {
                $model.find(".modal-body").html("您还没有任何收藏哦，快去收藏一些吧！");
                $model.modal('show');
                return;
            }
            console.log(result);
            $.each(obj.collectionSet, function (index, element) {
                var TempHtml = liHtml;
                TempHtml = TempHtml.replace("articleTitle", element.articleTitle);
                TempHtml = TempHtml.replace("collectDate", element.collectDate);
                TempHtml = TempHtml.replace("author", element.userName);
                TempHtml = TempHtml.replace("collectId", element.collectId);
                TempHtml = TempHtml.replace("articleId", element.articleId);
                TempHtml = TempHtml.replace("the-classify", findType(element.articleType));
                TempHtml = TempHtml.replace("publishDate", element.publishDate);
                var finalHtml = "<li class='my-collections'>" + TempHtml + "</li>";
                $("#my-collection>ul").append(finalHtml);
            })
        })
    }
    if (url == "qryUserFollow.do") {
        $.post(url, function (result) {
        })
    }
    if (url == "qryUserScore.do") {
        $.post(url, function (result) {
        })
    }


}