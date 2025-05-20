window.envList = {
    "wv-local-dev": {
        "backend_url": "http://localhost:8080/wv"
    },
    "wv-dev": {
        "backend_url": "http://localhost:8080/wv"
    },
    "wv-prod": {
        "backend_url": "https://assistant.guiji.ai/wv"
    }
}

window.env = envList["wv-prod"];

/**
 * ajax get
 * @param ajaxdata 提交数据
 * @param ajaxurl 提交路径
 * @param successcallback 成功回调
 * @param errorcallback 失败回调
 */
function ajaxGET(ajaxdata, ajaxurl, successcallback, errorcallback) {
    $.ajax({
        cache: true,
        type: "get",
        dataType: "json",
        url: ajaxurl,
        data: ajaxdata,
        async: true,
        success: function (data) {
            if ($.isFunction(successcallback)) {
                successcallback.call(this, data);
            }
        },
        error: function (data) {
            if ($.isFunction(errorcallback)) {
                errorcallback.call(this, data);
            }
        }
    });
}


/**
 * ajax post提交
 * @param ajaxdata 提交数据
 * @param ajaxurl 提交路径
 * @param successcallback 成功回调
 * @param errorcallback 失败回调
 */
function ajaxPost(ajaxdata, ajaxurl, successcallback, errorcallback) {
    $.ajax({
        cache: true,
        type: "post",
        //contentType : 'application/json;charset=utf-8', //设置请求头信息 会造成request payload
        dataType: "json",
        url: ajaxurl,
        data: ajaxdata,
        async: true,
        success: function (data) {
            if ($.isFunction(successcallback)) {
                successcallback.call(this, data);
            }
        },
        error: function (data) {
            if ($.isFunction(errorcallback)) {
                errorcallback.call(this, data);
            }
        }
    });
}

/**
 * ajax post提交 以contentType: "application/json"方式，**后端使用@RequestBody 接收参数**
 * @param ajaxdata 提交数据
 * @param ajaxurl 提交路径
 * @param successcallback 成功回调
 * @param errorcallback 失败回调
 */
function ajaxPostJSON(ajaxdata, ajaxurl, successcallback, errorcallback) {
    $.ajax({
        cache: true,
        type: "post",
        headers: {
            "token": "2",
            "Access-token": "d66987bfa0d2490990f9272648dfc99f"
        },
        contentType: "application/json;charset=UTF-8",//"application/json;charset=UTF-8" 必须 后台需要根据这个确定使用jsonhttpmessageconvert
        dataType: "json",//后台返回的数据类型
        url: ajaxurl,
        data: ajaxdata,
        async: true,
        //解决跨域问题？？？
        //开启后：虽然可以写入set-cookie 可是，多了一个options请求！TODO： 还要研究一下
        success: function (data) {
            if ($.isFunction(successcallback)) {
                successcallback.call(this, data);
            }
        },
        error: function (data) {
            if ($.isFunction(errorcallback)) {
                errorcallback.call(this, data);
            }
        }
    });
}

/**
 * processData设置为false。因为data值是FormData对象，不需要对数据做处理。
 * <form>标签添加enctype="multipart/form-data"属性。
 * cache设置为false，上传文件不需要缓存。
 * contentType设置为false。因为是由<form>表单构造的FormData对象，且已经声明了属性enctype="multipart/form-data"，所以这里设置为false。
 *
 * ajax Formpost提交
 */
function ajaxFormPost(ajaxdata, ajaxurl, successcallback, errorcallback) {
    $.ajax({
        type: "post",
        url: ajaxurl,
        data: ajaxdata,
        async: true,
        dataType: "json",
        contentType : false,
        processData: false,
        success: function (data) {
            if ($.isFunction(successcallback)) {
                successcallback.call(this, data);
            }
        },
        error: function (data) {
            if ($.isFunction(errorcallback)) {
                errorcallback.call(this, data);
            }
        }
    });
}


var browser = {
    versions: function () {
        var u = navigator.userAgent, app = navigator.appVersion;
        return {   //移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, //IE内核
            presto: u.indexOf('Presto') > -1, //opera内核
            webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/), //是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或uc浏览器
            iPhone: u.indexOf('iPhone') > -1, //是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, //是否iPad
            webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
        };
    }(),
    language: (navigator.browserLanguage || navigator.language).toLowerCase()
};
/**
 * Ajax设置
 */
$.ajaxSetup( {
    //设置ajax请求结束后的执行动作
    complete : function(XMLHttpRequest, textStatus) {
        // 通过XMLHttpRequest取得响应头，REDIRECT
        var redirect = XMLHttpRequest.getResponseHeader("REDIRECT");//若HEADER中含有REDIRECT说明后端想重定向
        if (redirect == "REDIRECT") {
            var win = window;
            while (win != win.top){
                win = win.top;
            }
            //将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
            win.location.href= XMLHttpRequest.getResponseHeader('CONTENTPATH');
        }
    }
});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) {
        return decodeURIComponent(r[2]);
    }
    return null;
}

/**
 * 将FormData类型转换为JSON对象
 * @param formData
 */
function convertFormData2JSONObject(formData) {
    var objData = {};
    formData.forEach((value, key) => objData[key] = value);
    return objData;
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,               //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

// 秒转换分钟00:00:00格式
// timeToMinute(document.getElementsByTagName("video")[0].duration)
// '00:18:56'
// document.getElementsByTagName("video")[0].duration
// 1136.124482
function timeToMinute(times){
    var t;
    if(times > -1){
        var hour = Math.floor(times/3600);
        var min = Math.floor(times/60) % 60;
        var sec = times % 60;
        if(hour < 10) {
            t = '0'+ hour + ":";
        } else {
            t = hour + ":";
        }

        if(min < 10){t += "0";}
        t += min + ":";
        if(sec < 10){t += "0";}
        t += sec.toFixed(2);
    }
    t=t.substring(0,t.length-3);
    return t;

}