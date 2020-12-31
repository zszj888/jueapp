package com.somecom.websocket;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint(value = "/websocket/{clientType}")
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static AtomicInteger onlineCount = new AtomicInteger(0);
    private static AtomicBoolean serving = new AtomicBoolean(false);

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private Integer clientType;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("clientType") Integer clientType) {
        this.session = session;
        this.clientType = clientType;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
        if (clientType == 1) {
            serving.getAndSet(true);
        } else if (clientType == 2 && !serving.get()) {
            MessageDto msg = new MessageDto();
            msg.setMessage("当前客服不在线，请联系4006699510");
            msg.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            msg.setType(1);
            msg.setUserId(0);
            sendMessage(JSON.toJSONString(msg));
        } else if (getOnlineCount() > 3) {
            MessageDto msg = new MessageDto();
            msg.setMessage("当前排队人数2，请稍等");
            msg.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            msg.setType(1);
            msg.setUserId(0);
            sendMessage(JSON.toJSONString(msg));
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (this.clientType == 1) {
            serving.getAndSet(false);
        }
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) {
        System.out.println("来自客户端的消息:" + message);
        if (serving.get() && getOnlineCount() < 3) {
            //群发消息
            for (MyWebSocket item : webSocketSet) {
                item.sendMessage(message);
            }
        } else if (serving.get() && this.clientType == 2) {
            MessageDto msg = new MessageDto();
            msg.setMessage("当前排队人数2，请稍等");
            msg.setTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            msg.setType(1);
            msg.setUserId(0);
            sendMessage(JSON.toJSONString(msg));
        }

    }

    /**
     * 发生错误时调用
     *
     * @OnError public void onError(Session session, Throwable error) {
     * System.out.println("发生错误");
     * error.printStackTrace();
     * }
     */
    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        //this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     */
//    public static void sendInfo(String message) throws IOException {
////        for (MyWebSocket item : webSocketSet) {
////            try {
////                item.sendMessage(message);
////            } catch (IOException e) {
////                continue;
////            }
////        }
//    }
    public static int getOnlineCount() {
        return onlineCount.get();
    }

    public static void addOnlineCount() {
        MyWebSocket.onlineCount.getAndIncrement();
    }

    public static void subOnlineCount() {
        MyWebSocket.onlineCount.getAndDecrement();
    }
}