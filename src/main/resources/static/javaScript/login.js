 function  dologin(){

    var userName=$("#userName").val();
    var passWord=$("#passWord").val();
    var nowLogin={};
    nowLogin.userName=userName;
    nowLogin.passWord=passWord;
    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/dologin.do",
        data:JSON.stringify(nowLogin),
        success: function (result) {
            if(result.status == "success"){
                alert(result.userName+"欢迎登录系统！")
               /* var homeURL=encodeURI("/home?&userName="+result.userName+"");
                window.location.href=homeURL;*/
                var homeURL=encodeURI("/index");
                window.location.href=homeURL;
            }
            if(result.status == "notFound"){
                alert("找不到此用户请核实您的用户名！");
            }
            if(result.status == "passwordError")
                alert("密码错误！")
        }

    })

}