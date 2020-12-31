package com.somecom.websocket;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class MessageDto {
    //1客服发往用户的消息，2相反
    private Integer type;
    private String message;
    private Integer userId;
    private String time;
    private String timeFormat;

    public static void main(String[] args) {
        MessageDto messageDto = new MessageDto();
//        {"message":"当前排队人数2，请稍等","time":"2020-12-08 20:42:29","type":1,"userId":0}
        System.out.println(JSON.toJSONString(messageDto));
    }
}
