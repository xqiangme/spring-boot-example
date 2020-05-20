const req = {
    status: {
        ok: 200,
        fail: 400,
        other: 333
    },
    type: {
        get: 'GET',
        post: 'POST'
    }
};


const AjaxRequest = {
    /**
     * 异步post请求
     * @param url
     * @param params
     * @param callback
     */
    aPost: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        $.ajax({
            type: req.type.post,
            url: url,
            dataType: 'JSON',
            contentType: 'application/json;charset=utf-8',
            data: params,
            success: callback
        });
    },
    /**
     * ajax 同步请求
     * @param url
     * @param params
     * @param method
     * @param callback
     * @returns {string}
     */
    ajx: function (url, params, method, callback) {
        let result = "";
        if (!url) {
            return result;
        }
        params = params || {};
        method = method || req.type.get;
        callback = callback || function (r) {
            result = r;
        };
        $.ajax({
            type: method,
            url: url,
            data: params,
            async: false,
            success: callback
        });
        return result;
    },

    /**
     * 异步get请求
     * @param url
     * @param params
     * @param callback
     */
    aGet: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        $.ajax({
            type: req.type.get,
            url: url,
            data: params,
            success: callback
        });
    },

    /**
     * 同步get
     * @param url 请求地址：image/ascii
     * @param params 参数：{name:'kalvin'}
     * @param callback 回调函数
     */
    get: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        return this.ajx(url, params, req.type.get, callback);
    },

    /**
     * 同步post
     * @param url 请求地址：image/ascii
     * @param params 参数：{name:'kalvin'}
     * @param callback 回调函数
     */
    post: function (url, params, callback) {
        if (arguments.length === 2 && $.isFunction(arguments[1])) {
            callback = arguments[1];
            params = {};
        }
        return this.ajx(url, params, req.type.post, callback);
    },
};

const ResponseMsg = {
    layMsg: function (msg, iconCode) {
        return layer.msg(msg, {icon: iconCode, time: 500});
    },
    layMsg: function (msg, iconCode, time) {
        return layer.msg(msg, {icon: iconCode, time});
    },
    successMsg: function (msg) {
        return this.layMsg(msg, 1,);
    },
    errorMsg: function (msg) {
        return this.layMsg(msg, 2, 1500);
    },
    errorMsg: function (msg, time) {
        return this.layMsg(msg, 2, time);
    },
};

function RequestParameter() {
    const url = window.location.search; //获取url中"?"符后的字串
    const theRequest = new Object();
    if (url.indexOf("?") !== -1) {
        const str = url.substr(1);
        const strs = str.split("&");
        for (let i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = (strs[i].split("=")[1]);
        }
    }
    return theRequest
}

// 获取指定名称的cookie
function getValueByCookie(cookieKey, valueKey) {
    const cookieValue = decodeURIComponent(getCookie(cookieKey));
    const valueArray = cookieValue.split(";");//分割
    //遍历匹配
    for (let i = 0; i < valueArray.length; i++) {
        const arr = valueArray[i].split("=");
        if (arr[0] === valueKey) {
            return arr[1];
        }
    }
    return "";
}

// 获取指定名称的cookie
function getCookie(name) {
    const strcookie = document.cookie;//获取cookie字符串
    const arrcookie = strcookie.split(";");//分割
    //遍历匹配
    for (let i = 0; i < arrcookie.length; i++) {
        const arr = arrcookie[i].split("=");
        if (arr[0] === name) {
            return arr[1];
        }
    }
    return "";
}




