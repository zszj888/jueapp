Date.prototype.format = function(fmt) {
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "h+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt)) {
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    }
    for(var k in o) {
        if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
    return fmt;
};
(function ($) {
    var wsUri = "wss://www.13cyw.com/websocket/1";
    var socketOpen = false;
    var messageArray = [];
    websocket = new WebSocket(wsUri);
    websocket.onopen = function (res) {
        console.log("连接服务器成功。");
        $("#inputMessage").attr("placeholder", "连接成功，等待客户");
        socketOpen = true;
    };
    websocket.onclose = function (res) {
        console.log("断开连接。");

    };
    websocket.onmessage = function (res) {
        console.log('收到服务器内容：' + res.data);
        var data = JSON.parse(res.data);
        messageArray = messageArray.concat(data);
        reBuild(messageArray);
    };

    websocket.onerror = function (res) {
        console.log("连接错误。");
    };

    $("#sendButton").on("click", function () {
        var message = $("#inputMessage").val();
        if (message !== '') {
            $("#inputMessage").attr("placeholder", "请输入信息");
            var msgobj = {};
            msgobj.type = 1;
            msgobj.time =new Date().format("yyyy-MM-dd hh:mm:ss");
            msgobj.timeFormat = new Date().toISOString();
            msgobj.message = message;
            msgobj.userId = 0;
            sendSocketMessage(msgobj);
            $("#inputMessage").val("");
        }
    });

    function sendSocketMessage(message) {
        if (socketOpen) {
            websocket.send(JSON.stringify(message));
        }
    }

    function reBuild(messageArray) {
        $("#topArea").empty();
        var htmlDom = "";
        for (var i in messageArray) {
            if (messageArray[i].type === 1) {
                htmlDom += '<div class="selfMessage"><div class="nameInfo">'
                    + "我："
                    + messageArray[i].time
                    + '</div><div class="detailMessage">'
                    + messageArray[i].message
                    + '</div></div>';
            } else {
                htmlDom += '<div class="otherMessage"><div class="nameInfo">'
                    + "客户" + messageArray[i].userId+"："
                    + messageArray[i].time
                    + '</div><div class="detailMessage">'
                    + messageArray[i].message
                    + '</div></div>';
            }
        }
        htmlDom += '<div class="clear"></div>'
        $("#topArea").append(htmlDom);
    }

})(Zepto);