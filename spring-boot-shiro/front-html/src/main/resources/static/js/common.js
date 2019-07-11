/*
* 人员页面加载
 */
function userHtmlOnloadJs(url) {
    //后台接口地址
    var interfaceAddress = getAddress();
    // 初始化内容
    $.ajax({
        type: "POST",
        url: interfaceAddress + url,
        contentType: "application/json; charset=utf-8",
        xhrFields: {
            withCredentials: true
        },
        data: "{}",
        dataType: "json",
        success: function (message) {
            console.log("服务成功 > " + JSON.stringify(message))
            if (true === message.success) {
                var result = JSON.stringify(message, null, 2);
                $("#resJson").html(result)
            } else {
                //未登录 > 跳转回登录页面
                if ("4005" === message.errorCode) {
                    window.location.href = "/login"
                }
                //访问权限不足 > 跳转回上一页
                else if ("4003" === message.errorCode) {
                    // window.history.go(-1);
                    $("#resJson").html("对不起，您无权限访问！")
                    // alert(message.errorMsg)
                } else {
                    alert("2 -> " + message.errorMsg)
                }
            }
        },
        error: function (message) {
            alert("服务失败 > " + JSON.stringify(message));
        }
    });
}
