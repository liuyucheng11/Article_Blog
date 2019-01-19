var isLogin;
var article;
$(function () {
    $.post("checkLogin.do", function (result) {
        isLogin = result;
        console.log(result);
    });
    //请求后台当前阅读图书数据
    url = "loadNowArticleInfo.do";
    $.post(url, function (result) {
        if(result["isEmpty"] == true){
            alert("当前文章暂无评价信息！");
            return;
        }
        var obj = result;
        console.log(obj);
        //当前阅读文章对象
        if (obj == undefined || obj == null)
            return;
        var nowArticle = obj.nowArticle;
        article = obj.nowArticle;
        $("#article-score").val(obj["avg-score"]);
        $("#article-score").rating({
            starCaptions: function (val) {
                if (val < 3.5) {
                    return val;
                }
                else {
                    return '好评';
                }
            },
            starCaptionClasses: function (val) {
                if (val < 2.5) {
                    return 'label label-danger';
                }
                else if (val < 3.5)
                    return 'label label-info';
                else {
                    return 'label label-success';
                }


            },
            hoverOnClear: false

        });
        $('#article-score').rating('refresh', {readonly: true, showClear: false, showCaption: false});

        $(".article-title").html(nowArticle.articleTitle);
        $(".author").html(nowArticle.userName);
        $(".publish-date").html(nowArticle.publishDate);
        $(".article-score-value").html(obj["avg-score"] + "分");

        // $("#article-score").val(4);
        $("#count").html(obj.articleScores.length);
        //计算评分百分比
        $(".progress-bar").each(function (index, ele) {
            var a = 5 - index;
            var star = obj.scoreMap[a];
            if (star != undefined && star != null && star != '') {
                var ratio = star / (obj.articleScores.length);
                var percentage = ratio * 100 + "%";
                $(ele).css("width", percentage).html(percentage);
            }
            else {
                var percentage = 0;
                $(ele).css("width", percentage).html(percentage);
            }
        })
    });
    //以分页形式获取评论信息
    qryArticleScorePage(1, 10);


});

function qryArticleScorePage(startPage, pageNum) {
    var liHtml = undefined;
    liHtml = $("#demo").html();
    $("#demo").hide();
    var param = {};
    param.startPage = startPage;
    param.pageNum = pageNum;
    $.ajax({
        type: 'POST',
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "getCommentPages.do",
        data: JSON.stringify(param),
        success: function (result) {
            var obj = result;
            console.log(obj);
            var articleScores = obj.articleScorePageInfo.list;
            $.each(articleScores, function (index, element) {
                var tempHtml = liHtml;
                var liHead = "<li class='score-li' evaluate-id='" + element.evaluateId + "'>";
                tempHtml = tempHtml.replace("张三", element.userName);
                tempHtml = tempHtml.replace("create-Date", element.createDate);
                tempHtml = tempHtml.replace("121", element.likeNumber);
                tempHtml = tempHtml.replace("commentDemo", element.comment);
                var finalHtml = liHead + tempHtml + "</li>";
                $(".article-comments").append(finalHtml);

                $("li[evaluate-id = " + element.evaluateId + "]").find(".score").append("<input class='rating score-star' type='test' data-min= '0' data-max='5' step= '1' data-size= 'xs'  value=" + element.score + ">");
            });
            $('.score-star').rating({
                'showCaption': true,
                'stars': '5',
                'min': '0',
                'max': '5',
                'step': '1',
                'size': 'xs',
                'starCaptions': {0: '差评', 1: '差评', 2: '一般', 3: '一般', 4: '好评', 5: '强烈推荐'}
            });
            $('.score-star').rating('refresh', {readonly: true, showClear: false, showCaption: true});
            //将评价进行分页处理
            var totalNum = obj.articleScorePageInfo.total;
            var totalPage = obj.articleScorePageInfo.pages;
            $(".count").html(totalNum);
            if (totalPage > 8) {
                for (var i = 8; i > 0; i--) {
                    var pageIndex = "<span class='pageIndex'><a>" + i + "</a>&nbsp;</span>";
                    $(".previous-page").after(pageIndex);
                }
                ;
                $(".previous-page").after("<span class='morePage'><a>...</a>");
            }
            else {
                for (var i = totalPage; i > 0; i--) {
                    var pageIndex = "<span class='pageIndex'><a>" + i + "</a>&nbsp;</span>";
                    $(".previous-page").after(pageIndex);
                }
            }
            $(".totalPageNum").html(totalPage);
            $(".nowPageIndex").html(startPage);

        }
    })

}

//给一些按钮绑顶事件
$(function () {
    //绑定跳转页号事件
    $(document).on("click", ".pageIndex a", function () {
        var pageIndex = $(this).html();
        //移除该区域之前的查询结果
        $("li.score-li").remove();
        $("span.pageIndex").remove();
        qryArticleScorePage(pageIndex, 10);
    });
    $(".jump-page").click(
        function () {
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
            $("li.score-li").remove();
            $("span.pageIndex").remove();
            qryArticleScorePage(pageNum, 10);
        }
    );
    //前一页绑定事件
    $(".previous-page").click(function () {
        var nowPageIndex = $(".nowPageIndex").html();
        if (nowPageIndex == 1) {
            alert("已经是最小页啦！");
            return
        }
        var startPage = --nowPageIndex;
        $("li.score-li").remove();
        $("span.pageIndex").remove();
        qryArticleScorePage(startPage, 10);
    });
    $(".next-page").click(function () {
        //当前页
        var nowPageIndex = $(".nowPageIndex").html();
        var totalPageNum = $(".totalPageNum").html();

        if (nowPageIndex == totalPageNum) {
            alert("已经是最大页啦！");
            return
        }

        var startPage = ++nowPageIndex;
        $("li.score-li").remove();
        //移除页码信息
        $("span.pageIndex").remove();
        qryArticleScorePage(startPage, 10);
    });
    //绑定事件
    var $model = $("#write-comment");
    if (isLogin == false) {
        $model.find(".modal-body").html("您当前未登录，请登录后再在执行此操作哦！");
        $model.find('.modal-footer').hide();
        $model.modal('show');
        return;
    }
    ;
    $('#write-comment').on('show.bs.modal', function (event) {
        var $modal = $(this);
        $modal.find(".report").on("click", function () {
            var theScore = $("#score").val();
            var theCommentText = $("#comment-text").val();
            if ((theScore == null) || theCommentText == null || theCommentText == '') {
                alert("请完整填写您的评论信息！");
                return;
            }
            var param = {};
            param.theScore = theScore;
            param.theCommentText = theCommentText;
            $.ajax({
                type: 'post',
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                data: JSON.stringify(param),
                url: "reportComment.do",
                success: function (result) {
                    if (result == true) {
                        $modal.modal('hide');
                        alert("评论发表成功！")
                    }
                    else {
                        $modal.modal('hide');
                        alert("您已评价过该文章了！")
                    }

                }
            })
        })

    });
    $("#read").click(function () {
        $.ajax({
            type: 'POST',
            dataType: "json",
            contentType: "application/json; charset=utf-8",
            url: "saveNowLogin.do",
            data: JSON.stringify(article.articleId),
            success: function (result) {
                //跳转页面

                window.open("/readArticle");
                //点击量+1可在未登录下实现
                $.post("recordClickLog.do", function () {
                });
            }


        })
    })
})