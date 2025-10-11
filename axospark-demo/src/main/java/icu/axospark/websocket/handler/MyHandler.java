package icu.axospark.websocket.handler;

import com.google.gson.Gson;
import icu.axospark.websocket.pojo.entity.Danmaku;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

public class MyHandler extends TextWebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Gson json = new Gson();
        String ok = "连接成功";
        session.sendMessage(new TextMessage(json.toJson(ok)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Gson json = new Gson();
        Danmaku danmaku = new Danmaku(1L, message.getPayload());
        session.sendMessage(new TextMessage(json.toJson(danmaku)));
    }
}
