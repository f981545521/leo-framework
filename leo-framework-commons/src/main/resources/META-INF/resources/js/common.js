window.leoUtil = {
    /**
     * 获取 查询参数
     * @param name 参数名称
     * @returns {*} result
     */
    getQueryString: function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return decodeURIComponent(r[2]);
        }
        return null;
    },

    /**
     * 从路径中获取 资源参数
     * @returns {*} result
     */
    getQueryStringFromPath: function getQueryStringFromPath() {
        var pathName = window.location.pathname;
        var index = window.location.pathname.lastIndexOf("/") + 1;
        var value = pathName.substr(index);
        if (value) {
            return value;
        }
        return null;
    },

    /**
     * 时间差异算法
     * @param dateStr Data: 2016/1/20 19:59:30
     * @returns {string|*|string}
     */
    getDateDiff: function getDateDiff(dateStr) {
        var dateTimeStamp = Date.parse(dateStr.replace(/-/gi, "/"));
        var minute = 1000 * 60;
        var hour = minute * 60;
        var day = hour * 24;
        var halfamonth = day * 15;
        var month = day * 30;
        var now = new Date().getTime();
        var diffValue = now - dateTimeStamp;
        if (diffValue < 0) {
            return;
        }
        var monthC = diffValue / month;
        var weekC = diffValue / (7 * day);
        var dayC = diffValue / day;
        var hourC = diffValue / hour;
        var minC = diffValue / minute;
        if (monthC >= 1) {
            result = "" + parseInt(monthC) + "月前";
        } else if (weekC >= 1) {
            result = "" + parseInt(weekC) + "周前";
        } else if (dayC >= 1) {
            result = "" + parseInt(dayC) + "天前";
        } else if (hourC >= 1) {
            result = "" + parseInt(hourC) + "小时前";
        } else if (minC >= 1) {
            result = "" + parseInt(minC) + "分钟前";
        } else
            result = "刚刚";
        return result;
    },

    /**
     * 通过正则表达式提取数字/非数字
     * @param str 字符串
     * @param NaN ture非数字，else 数字
     * @returns {XML|void|string|*}
     */
    extractDigits: function extractDigits(str, NaN) {
        if (NaN) {
            return str.replace(/[0-9.-]/ig, "");
        }
        return parseInt(str.replace(/[^0-9.-]/ig, ""));
    },


    /**
     * * 判断元素是否进入可视区域
     * @param objLiLast 最后一个元素
     * @returns {boolean}
     */
    see: function see(objLiLast) {
        //浏览器可视区域的高度
        var see = document.documentElement.clientHeight;
        //滚动条滑动的距离
        var winScroll = $(this).scrollTop();
        //距离浏览器顶部的
        var lastLisee = $(objLiLast).offset().top;
        return lastLisee < (see + winScroll);
    },


    /**
     * 金额格式化
     * @param number
     * @param decimals
     * @param dec_point
     * @param thousands_sep
     * @returns {string|*}
     */
    number_format: function number_format(number, decimals, dec_point, thousands_sep) {
        /*
         * 参数说明：
         * number：要格式化的数字
         * decimals：保留几位小数
         * dec_point：小数点符号
         * thousands_sep：千分位符号
         * */
        number = (number + '').replace(/[^0-9+-Ee.]/g, '');
        var n = !isFinite(+number) ? 0 : +number,
            prec = !isFinite(+decimals) ? 0 : Math.abs(decimals),
            sep = (typeof thousands_sep === 'undefined') ? ',' : thousands_sep,
            dec = (typeof dec_point === 'undefined') ? '.' : dec_point,
            s = '',
            toFixedFix = function (n, prec) {
                var k = Math.pow(10, prec);
                return '' + Math.ceil(n * k) / k;
            };

        s = (prec ? toFixedFix(n, prec) : '' + Math.round(n)).split('.');
        var re = /(-?\d+)(\d{3})/;
        while (re.test(s[0])) {
            s[0] = s[0].replace(re, "$1" + sep + "$2");
        }

        if ((s[1] || '').length < prec) {
            s[1] = s[1] || '';
            s[1] += new Array(prec - s[1].length + 1).join('0');
        }
        return s.join(dec);
    },


    /**
     * 验证提交的JSON数据
     * @param postData JSON对象
     */
    validatePostData: function validatePostData(postData) {
        for (var i in postData) {
            var content = postData[i];
            if (!content) {
                error('请填写内容！');
                return false;
            }
            var reg = new RegExp("<.*?script[^>]*?>.*?(<\/.*?script.*?>)*", "ig");
            if (reg.test(content)) {
                error('请不要提交非法内容！');
                return false;
            }
        }
        return true;
    },


    /**
     * 将json转url参数
     * @param json
     * @returns {string}
     */
    convertJsonToParams: function convertJsonToParams(json) {
        var params = Object.keys(json).map(function (key) {
            return encodeURIComponent(key) + "=" + encodeURIComponent(json[key]);
        }).join("&");
        return params;
    },

    /*
     * 三个参数
     * file：一个是文件(类型是图片格式)，
     * obj：一个是文件压缩的后宽度，宽度越小，字节越小
     * callback：回调
     */
    photoCompress: function photoCompress(file, obj, callback) {
        var ready = new FileReader();
        /*开始读取指定的Blob对象或File对象中的内容. 当读取操作完成时,readyState属性的值会成为DONE,如果设置了onloadend事件处理程序,则调用之.同时,result属性中将包含一个data: URL格式的字符串以表示所读取文件的内容.*/
        ready.readAsDataURL(file);
        ready.onload = function () {
            var re = this.result;
            canvasDataURL(re, obj, callback)
        }
    },

    canvasDataURL: function canvasDataURL(path, obj, callback) {
        var img = new Image();
        img.src = path;
        img.onload = function () {
            var that = this;
            // 默认按比例压缩
            var w = that.width,
                h = that.height,
                scale = w / h;
            w = obj.width || w;
            h = obj.height || (w / scale);
            var quality = 0.7;  // 默认图片质量为0.7
            //生成canvas
            var canvas = document.createElement('canvas');
            var ctx = canvas.getContext('2d');
            // 创建属性节点
            var anw = document.createAttribute("width");
            anw.nodeValue = w;
            var anh = document.createAttribute("height");
            anh.nodeValue = h;
            canvas.setAttributeNode(anw);
            canvas.setAttributeNode(anh);
            ctx.drawImage(that, 0, 0, w, h);
            // 图像质量
            if (obj.quality && obj.quality <= 1 && obj.quality > 0) {
                quality = obj.quality;
            }
            // quality值越小，所绘制出的图像越模糊
            var base64 = canvas.toDataURL('image/jpeg', quality);
            // 回调函数返回base64的值
            callback(base64);
        }
    },

    /**
     * 将base64转换为文件
     * @param dataurl
     * @param filename
     * @returns {File}
     */
    dataURLtoFile: function dataURLtoFile(dataurl, filename) {
        var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new File([u8arr], filename, {type: mime});
    },


    /**
     * 判断是否是微信浏览器
     * @returns {boolean}
     */
    isWeiXinBrowser: function isWeiXinBrowser() {
        var ua = navigator.userAgent.toLowerCase();
        return ua.match(/MicroMessenger/i) == "micromessenger";
    },

    /**
     * 判断是否是浏览器
     * @returns {boolean}
     */
    isBrowser: function isBrowser() {
        var ua = navigator.userAgent;
        return ua.indexOf("Safari") !== -1;
    },


    /**
     * 复制到剪切板
     * @param str
     */
    copyToClipboard: function copyToClipboard(str) {
        var save = function (e) {
            e.clipboardData.setData('text/plain', str);
            e.preventDefault();//阻止默认行为
        };
        document.addEventListener('copy', save);
        document.execCommand("copy");//使文档处于可编辑状态，否则无效
    },


    /**
     * 将以base64的图片url数据转换为Blob
     * @param urlData
     *            用url方式表示的base64图片数据
     */
    convertBase64UrlToBlob: function convertBase64UrlToBlob(urlData) {
        var arr = urlData.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], {type: mime});
    },


    /**
     * 数字前补充0
     * @param num 目标数字
     * @param length 位数
     * @returns {string}
     * @constructor
     */
    repairZero: function repairZero(num, length) {
        if (num.toString().length >= length) {
            return num;
        }
        return (Array(length).join('0') + num).slice(-length);
    },

    /**
     * 判断H5+环境
     * @returns {boolean}
     */
    isH5Plus: function isH5Plus() {
        return navigator.userAgent.indexOf("Html5Plus") > -1;
    }
}

/**
 * 时间格式转换 new Date().Format("yyyy-MM-dd hh:mm:ss")
 * @param fmt yyyy-MM-dd hh:mm:ss
 * @returns {*}
 * @constructor
 */
Date.prototype.Format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds() //秒
    };
    if (/(y+)/.test(fmt)) { //根据y的长度来截取年
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    }
    return fmt;
}


if (typeof jQuery === 'undefined') {
    console.error("未加载JQuery")
} else {
    console.log("已加载JQuery")

    //—————————————————————————JQuery Ajax ———————————————————————————————————————————————————————————————————————————

    /**
     * ajax get
     * @param ajaxurl 提交路径
     * @param successcallback 成功回调
     * @param errorcallback 失败回调
     */
    $.ajaxGet = function ajaxGet( ajaxurl, successcallback, errorcallback) {
        $.ajax({
            cache: true,
            type: "get",
            dataType: "json",
            url: ajaxurl,
            data: {},
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
    $.ajaxPost = function ajaxPost(ajaxdata, ajaxurl, successcallback, errorcallback) {
        $.ajax({
            cache: true,
            type: "post",
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
     * processData设置为false。因为data值是FormData对象，不需要对数据做处理。
     * <form>标签添加enctype="multipart/form-data"属性。
     * cache设置为false，上传文件不需要缓存。
     * contentType设置为false。因为是由<form>表单构造的FormData对象，且已经声明了属性enctype="multipart/form-data"，所以这里设置为false。
     *
     * ajax FormData post提交
     */
    function ajaxFormDataPost(ajaxdata, ajaxurl, successcallback, errorcallback) {
        $.ajax({
            type: "post",
            url: ajaxurl,
            data: ajaxdata,
            async: true,
            dataType: "json",
            contentType: false,
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

    /**
     * ajax post提交 以contentType: "application/json"方式，后端使用@RequestBody 接收参数
     * @param ajaxdata 提交数据
     * @param ajaxurl 提交路径
     * @param successcallback 成功回调
     * @param errorcallback 失败回调
     */
    $.ajaxPostJson = function ajaxPostJson(ajaxdata, ajaxurl, successcallback, errorcallback) {
        $.ajax({
            cache: true,
            type: "post",
            contentType: "application/json;charset=UTF-8",
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

    $.ajaxSetup({
        //设置ajax请求结束后的执行动作
        complete: function (XMLHttpRequest, textStatus) {
            // 通过XMLHttpRequest取得响应头，REDIRECT
            var redirect = XMLHttpRequest.getResponseHeader("REDIRECT");//若HEADER中含有REDIRECT说明后端想重定向
            if (redirect === "REDIRECT") {
                var win = window;
                while (win !== win.top) {
                    win = win.top;
                }
                //将后端重定向的地址取出来,使用win.location.href去实现重定向的要求
                win.location.href = XMLHttpRequest.getResponseHeader('CONTENTPATH');
            }
        }
    });
}


