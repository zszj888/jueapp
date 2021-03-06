//package com.somecom.conf;
//
//import javax.websocket.OnClose;
//import javax.websocket.OnError;
//import javax.websocket.OnMessage;
//import javax.websocket.OnOpen;
//import javax.websocket.Session;
//import javax.websocket.server.PathParam;
//import javax.websocket.server.ServerEndpoint;
//import java.io.IOException;
//import java.util.Set;
//import java.util.concurrent.CopyOnWriteArraySet;
//
//@ServerEndpoint("/ws")
//public class ChatAnnotation_KF {
//    private static final Set<ChatAnnotation_KF> connections = new CopyOnWriteArraySet<ChatAnnotation_KF>();
//    private Session session;
//    public static String xx;
//
//    @OnOpen
//    public void onOpen(Session session, @PathParam("url") String url) {
//        session.getUserProperties().put("key", url);
//        this.session = session;
//        connections.add(this);
//    }
//
//    /**
//     * 收到客户端消息时触发
//     * @return
//     * @throws InterruptedException
//    */
//    @OnMessage
//    public void onMessage(Session session, String url) {
//        System.out.println(url);
//    }
//
//    /**
//     * 异常时触发
//     * @param session
//    */
//    @OnError
//    public void onError(Throwable throwable,Session session) {
//
//
//    }
//
//    /**
//     * 关闭连接时触发
//     * @param session
//    */
//    @OnClose
//    public void onClose(Session session) {
//        connections.remove(this);
//    }
//
//    public static void broadcast(String msg, String num) {
//        for (ChatAnnotation_KF client : connections) {
//            try {
//                synchronized (client) {
//                    String code = client.session.getUserProperties().get("key").toString();
//                    if (code.equals(msg)) {
//                        client.session.getBasicRemote().sendText(num);
//                    }
//                }
//            } catch (IOException e) {
//                connections.remove(client);
//                try {
//                    client.session.close();
//                } catch (IOException e1) {}
//            }
//        }
//    }
//
//}