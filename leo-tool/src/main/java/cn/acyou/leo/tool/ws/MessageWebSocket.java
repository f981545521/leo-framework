package cn.acyou.leo.tool.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author youfang
 * @version [1.0.0, 2018-08-10 下午 04:52]
 **/
@Slf4j
@Component
public class MessageWebSocket extends TextWebSocketHandler {
    //Session列表
    private static final Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    /**
     * 收到客户端发送的消息
     *
     * @param session 客户端session
     * @param message 消息
     * @throws Exception ex
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("接收到客户端消息 : " + message.getPayload());
        WebSocketMessage<String> respMessage = new TextMessage("你好，客户端：服务端已经收到你发的消息！" + message);
        session.sendMessage(respMessage);
    }

    /**
     * 建立连接后触发的回调
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("成功建立连接");
        sessionMap.put(session.getId(), session);
        session.sendMessage(new TextMessage("成功建立socket连接，sessionId:" + session.getId()));
        log.info(session.toString());
    }

    /**
     * 收到消息时触发的回调
     */
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    /**
     * 处理接收的Pong类型的消息
     */
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }

    /**
     * 传输消息出错时触发的回调
     */
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if (session.isOpen()) {
            session.close();
        }
        sessionMap.remove(session.getId());
        log.error("连接出错");
    }

    /**
     * 断开连接后触发的回调
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.error("连接已关闭：" + status);
        sessionMap.remove(session.getId());
    }

    /**
     * 是否处理分片消息
     */
    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 发送信息给指定用户
     */
    public boolean sendMessageToUser(String sessionId, TextMessage message) {
        if (sessionMap.get(sessionId) == null) return false;
        WebSocketSession session = sessionMap.get(sessionId);
        if (!session.isOpen()) return false;
        try {
            session.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 广播信息
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        boolean allSendSuccess = true;
        Set<String> sessionIds = sessionMap.keySet();
        WebSocketSession session = null;
        for (String sessionId : sessionIds) {
            try {
                session = sessionMap.get(sessionId);
                if (session.isOpen()) {
                    session.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                allSendSuccess = false;
            }
        }
        return allSendSuccess;
    }

}
