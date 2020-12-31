package com.somecom.websocket;

import lombok.Data;

import javax.websocket.Session;

@Data
public class WrapperSession {
    private final Session session;
    //1客服，2客户
    private Integer sessionType;

    public WrapperSession(Session session, Integer sessionType) {
        this.session = session;
        this.sessionType = sessionType;
    }
}
