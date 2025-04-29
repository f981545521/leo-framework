package cn.acyou.leo.tool.ws;

import cn.acyou.leo.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author youfang
 * @version [1.0.0, 2018-08-10 下午 04:52]
 **/
@Slf4j
@Component
public class MessageWebSocket extends TextWebSocketHandler {
    /**
     * 用户 WebSocket 会话
     * key1：用户ID
     * key2：SessionID
     */
    private final ConcurrentMap<String, Pair<String, WebSocketSession>> userSessions = new ConcurrentHashMap<>();

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
     * 收到消息时触发的回调 （这个方法会进行匹配到对应的类型）
     * handleTextMessage
     * handleBinaryMessage
     * handlePongMessage
     */
    //@Override
    //public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
    //    log.info("handleMessage : " + message.getPayload());
    //    super.handleMessage(session, message);
    //}


    private String getQuery(WebSocketSession session, String key) {
        return getQueryMap(session).get(key);
    }

    private Map<String, String> getQueryMap(WebSocketSession session) {
        Map<String, String> paramsMap = new HashMap<>();
        URI uri = session.getUri();
        if (uri != null) {
            String query = uri.getQuery();
            if (query != null) {
                String[] split = query.split("&");
                for (String s : split) {
                    String[] kv = s.split("=");
                    paramsMap.put(kv[0], kv[1]);
                }
            }
        }
        return paramsMap;
    }

    /**
     * 建立连接后触发的回调
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("建立连接中...");
        String clientId = getQuery(session, "clientId");
        if (StringUtils.isBlank(clientId)) {
            session.sendMessage(new TextMessage("clientId不能为空"));
            session.close();
            return;
        }
        userSessions.put(clientId, Pair.of(session.getId(), session));
        session.sendMessage(new TextMessage("成功建立socket连接！ -> clientId: " + clientId + " sessionId:" + session.getId()));
        log.info(session.toString());
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
        userSessions.remove(getQuery(session, "clientId"));
        log.error("连接出错");
    }

    /**
     * 断开连接后触发的回调
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.error("连接已关闭：" + status);
        userSessions.remove(getQuery(session, "clientId"));
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
    public boolean sendMessageToUser(String clientId, TextMessage message) {
        if (userSessions.containsKey(clientId)) {
            Pair<String, WebSocketSession> sessionPair = userSessions.get(clientId);
            WebSocketSession session = sessionPair.getValue();
            if (!session.isOpen()) return false;
            try {
                session.sendMessage(message);
                return true;
            } catch (IOException e) {
                log.error("发送消息失败", e);
            }
        }
        return false;
    }

    /**
     * 广播信息
     */
    public boolean sendMessageToAllUsers(TextMessage message) {
        AtomicBoolean allSendSuccess = new AtomicBoolean(true);
        userSessions.forEach((k, v) -> {
            if (v.getValue().isOpen()) {
                try {
                    v.getValue().sendMessage(message);
                } catch (IOException e) {
                    log.error("发送消息失败", e);
                    allSendSuccess.set(false);
                }
            }
        });
        return allSendSuccess.get();
    }

}
