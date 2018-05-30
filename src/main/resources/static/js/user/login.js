
var Login = (function () {

    function Login() {

    }

    Login.prototype.checkOn = function() {
        var prevUrl = $("#prevUrl").val();
        var formData = $("#login_form").serialize();
        var username = $("input[name=username]").val().trim();
        var password = $("input[name=password]").val().trim();
        if(username==""){
            $("#msg").text("请输入用户名")
            return;
        }if(password==""){
            $("#msg").text("请输入密码")
            return;
        }
        $.ajax({
            type: "POST",
            dataType: "json",
            url: "/login" ,
            data: formData,
            success: function (rs) {
                if(rs.code==0){
                    localStorage.setItem("token",rs.data.token)
                    window.location.href=prevUrl; //跳转到上一个未登陆页
                }else {
                    $("#msg").text(rs.msg);
                }
            },
            error : function() {
                alert("异常！");
            }
        });
    }

    return Login;
})();