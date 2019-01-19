var isLogin;

$(function () {
    $.post("checkLogin.do", function (result) {
        isLogin = result;
    });
     url = "readTheArticle.do";
    $.post(url,function (result) {
        var obj = result;
        console.log(obj);
        var article = obj.nowReadingArticle;
        $(".head h3").html(article.articleTitle);
        $(".author").html(article.userName);
        $(".publishDate").html(article.publishDate);
        $(".article-content").html(obj.articleContent);
    });
    //绑定查看信息事件
    $('#showInfo').click(function () {
        window.open("/showArticleInfo");
    });
    //星星事件
    $('.user-score').rating({
        'showCaption': true,
        'stars': '5',
        'min': '1',
        'max': '5',
        'step': '1',
        'size': 'xs',
        'starCaptions': {'':'未评分',0: '差评', 1: '差评', 2: '一般', 3: '一般', 4: '好评', 5: '强烈推荐'}
    });
    $('.user-score').rating('refresh', {readonly: false, showClear: false, showCaption: true});
    //写评论
    $("#comment").click(function () {
        if(isLogin == false){
            $("#msg").modal('show');
            return;
        }
        else{
            $("#write-comment").modal('show');
        }
    });
    $("#add-collection").click(function () {
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

    });

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
                    else{
                        $modal.modal('hide');
                        alert("您已经发表过对这篇文章的评价了！")
                    }
                }
            })
        })

    });

});