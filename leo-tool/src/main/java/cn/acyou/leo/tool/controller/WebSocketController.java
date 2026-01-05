package cn.acyou.leo.tool.controller;

import cn.acyou.leo.tool.ws.MessageWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;

/**
 * @author youfang
 * @version [1.0.0, 2018-08-10 下午 05:51]
 **/
@Controller
@RequestMapping("ws")
@DependsOn("messageWebSocket")
@Slf4j
public class WebSocketController {

    @Autowired
    private MessageWebSocket webSocket;

    @RequestMapping(value = "/message", method = {RequestMethod.GET})
    @ResponseBody
    public String sendMessage(String clientId, String message) {
        message = "System Message: " + message;
        if (clientId != null){
            webSocket.sendMessageToUser(clientId, new TextMessage(message));
        }else {
            webSocket.sendMessageToAllUsers(new TextMessage(message));
        }
        return "发送成功";
    }

}
