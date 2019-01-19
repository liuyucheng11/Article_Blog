$(document).ready(function () {
    $('#registerForm').bootstrapValidator(
        {
            feedbackIcons: {//根据验证结果显示的各种图标
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                username: {
                    validators: {
                        notEmpty: {
                            message: "用户名不能为空"
                        },
                        stringLength: {
                            /*长度提示*/
                            min: 2,
                            max: 15,
                            message: '用户名长度必须在2到15之间'
                        },
                        threshold: 2,//只有2个字符以上才发送ajax请求
                        remote: {
                            url: "/check/userUnique",
                            type: 'POST',
                            data: function (validator) {
                                return {
                                    inputvalue: $("#username").val()
                                };
                            },
                            message: '该用户名已被使用，请使用其他用户名',
                            delay: 2000//ajax 延时请求
                        }
                    }
                },
                age: {
                    validators: {
                        notEmpty: {
                            message: "年龄不能为空"
                        },
                        stringLength: {
                            min: 1,
                            max: 3,
                            message: "请输入正确年龄"
                        },
                        regexp: {
                            regexp: /^[0-9]+$/,
                            message: "年龄只能为数字"
                        }
                    }
                },
                occupation: {
                    validators: {
                        notEmpty: {
                            message: "请输入您的职业"
                        }
                    }
                },
                password: {
                    validators: {
                        notEmpty: {
                            message: "密码不能为空！"
                        },
                        stringLength: {
                            min: 6,
                            max: 20,
                            message: "密码长度请设置为6到20位"
                        },
                        regexp: {
                            regexp: /^[a-zA-Z0-9.]+$/,
                            message: '密码只能包含大写、小写、数字和.'
                        }

                    }
                },
                repassword: {
                    message: "两次输入的密码不相同",
                    validators: {
                        identical: {//判断密码相同
                            field: "password"
                        }
                    }
                }
                // sex: {
                //     validators: {
                //         notEmpty: {
                //             message: "请选择您的性别"
                //         }
                //     }
                // }
            }
        })
        .on('success.form.bv', function (e) {
            // Prevent submit form
            e.preventDefault();

            var $form = $(e.target),
                validator = $form.data('bootstrapValidator');
            $form.find('.alert').html('注册成功！ ' + validator.getFieldElements('username').val()).show();
        });


});

function register()//注册事件
{

    var username = $('#username').val();
    var age = $('#age').val();
    var occupation = $('#occupation').val();
    var sex = $("input[name='sex']:checked").val();
    var address = $('#address').val();
    var user = {};

    user.userName = username;
    user.userAge = age;
    user.sex = sex;
    user.userAddress = address;
    user.userOccupation = occupation;
     user.registerDate=new Date();
    user.userPassword=$('#password').val();
    console.log(user);
    console.log(JSON.stringify(user))
    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json; charset=utf-8",
        url: "/registerUser",
        data:JSON.stringify(user),// 这里要传json字符串*/
        success: function (result) {
            if (result.status = "success") {
                alert("注册成功！");
                window.location.href = "/login";
                // 跳转主页
            }
            else {
                alert("注册异常!");
            }
        }
    });
}