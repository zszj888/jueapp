package com.somecom.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Api(tags = "微信登录验证")
@RestController
@RequestMapping(path = "/wx")
public class LoginController {
    @Autowired
    private WebClient.Builder builder;

    private final static String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?" +
            "appid=wx61089dda2b3d8b09&secret=1e8f235c05a63838358076eb10c96624&grant_type=authorization_code&js_code=";

    @ApiOperation(value = "验证接口，传入code即可",notes = "详情参考微信API")
    @GetMapping(path = "/finishLogin")
    public Mono<WxResp> login(String code) {
        if (StringUtils.hasText(code))
            return builder.baseUrl(WX_URL + code).build().get().retrieve().bodyToMono(WxResp.class);
        return Mono.empty();
    }

    private static class WxResp {
        private String openid;
        private String session_key;
        private String unionid;
        private String errcode;
        private String errmsg;

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getSession_key() {
            return session_key;
        }

        public void setSession_key(String session_key) {
            this.session_key = session_key;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getErrcode() {
            return errcode;
        }

        public void setErrcode(String errcode) {
            this.errcode = errcode;
        }

        public String getErrmsg() {
            return errmsg;
        }

        public void setErrmsg(String errmsg) {
            this.errmsg = errmsg;
        }
    }
}