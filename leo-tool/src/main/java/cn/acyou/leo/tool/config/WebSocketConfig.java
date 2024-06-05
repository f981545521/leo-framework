package cn.acyou.leo.tool.config;

import cn.acyou.leo.tool.ws.MessageWebSocket;
import cn.acyou.leo.tool.ws.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 配置WebSocket
 * @author youfang
 * @version [1.0.0, 2018-08-10 下午 04:56]
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private MessageWebSocket messageWebSocket;


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        //地址：   ws://192.168.4.239:8076/leo-tool/messages
        registry.addHandler(messageWebSocket, "/messages")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketInterceptor());

    }

}
