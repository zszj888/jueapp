package com.somecom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import java.util.ArrayList;
import java.util.List;

public class main {
private final static Logger log  = LoggerFactory.getLogger(main.class);
    public static void main(String[] args) {
        JdkSerializationRedisSerializer jdkSeri = new JdkSerializationRedisSerializer();
        GenericJackson2JsonRedisSerializer genJsonSeri = new GenericJackson2JsonRedisSerializer();
        Jackson2JsonRedisSerializer jack2Seri = new Jackson2JsonRedisSerializer(Object.class);

        User user = new User(1,"tom",20,"boy");
        List<Object> list = new ArrayList<>();
        for(int i = 0; i < 1; i++){
            list.add(user);
        }

        long jdkSeri_Start = System.currentTimeMillis();
        byte[] serialize = jdkSeri.serialize(list);
        log.info("jdkSeri Serialization ends, time-consuming:{} ms，The length after serialization is:{}==============",(System.currentTimeMillis()-jdkSeri_Start),serialize.length/1024/1024);
        long jdkSeri_Start_again = System.currentTimeMillis();
        System.out.println(jdkSeri.deserialize(serialize));
        log.info("jdkSeri Deserialization takes time:{} ms===========",(System.currentTimeMillis()-jdkSeri_Start_again));


        long genJsonSeri_Start = System.currentTimeMillis();
        byte[] serialize1 = genJsonSeri.serialize(list);
        log.info("genJsonSeri Serialization ends, time-consuming:{} ms，The length after serialization is:{}==============",(System.currentTimeMillis()-genJsonSeri_Start),serialize1.length/1024/1024);
        long genJsonSeri_Start_again = System.currentTimeMillis();

        System.out.println(genJsonSeri.deserialize(serialize1));
        log.info("genJsonSeri Deserialization takes time:{} ms===========",(System.currentTimeMillis()-genJsonSeri_Start_again));

//
//        long jack2Seri_Start = System.currentTimeMillis();
//        byte[] serialize2 = jack2Seri.serialize(list);
//        log.info("jack2Seri Serialization ends, time-consuming:{} ms，The length after serialization is:{}===============",(System.currentTimeMillis()-jack2Seri_Start),serialize2.length/1024/1024);
//        long jack2Seri_Start_again = System.currentTimeMillis();
//
//        System.out.println(jack2Seri.deserialize(serialize2));
//        log.info("jack2Seri Deserialization takes time:{} ms=============",(System.currentTimeMillis()-jack2Seri_Start_again));
    }
}