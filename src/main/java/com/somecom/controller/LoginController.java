package com.somecom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.somecom.entity.User;
import com.somecom.model.ResultVo;
import com.somecom.repo.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Api(tags = "微信登录验证")
@RestController
@RequestMapping(path = "/wx")
public class LoginController {
    private final static String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?" +
            "appid=wx61089dda2b3d8b09&secret=1e8f235c05a63838358076eb10c96624&grant_type=authorization_code&js_code=";
    @Autowired
    private WebClient.Builder builder;
    @Autowired
    private UserRepository userRepository;
    @Value("${wx.file.path}")
    private String filePath;

    @ApiOperation(value = "验证接口，传入code即可", notes = "详情参考微信API")
    @GetMapping(path = "/finishLogin")
    public Mono<String> login(String code) throws IOException {
        if (StringUtils.hasText(code)) {
            Mono<String> stringMono = builder.baseUrl(WX_URL + code).build().get().retrieve().bodyToMono(String.class);
            String openid = new ObjectMapper().readTree(stringMono.toString()).asText("openid");
            Optional<User> one = userRepository.findOne(Example.of(new User(openid)));
            one.orElseGet(() -> userRepository.save(new User(openid)));
        }
        return Mono.empty();
    }

    @ApiOperation(value = "上传文件接口", notes = "上传头像或者短视频")
    @PostMapping(path = "/upload")
    public ResultVo upload(MultipartFile file, String openid) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            Files.createDirectories(Paths.get(filePath));
        }
        if (!StringUtils.hasText(openid)) {
            return ResultVo.err("openid为空");
        }
        if (!Files.exists(Paths.get(filePath, openid))) {
            Files.createDirectories(Paths.get(filePath, openid));
        }
        String fileName = UUID.randomUUID().toString();
        if (Objects.nonNull(file)) {
            Path finalFileName = Paths.get(filePath, openid,
                    fileName + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
            IOUtils.copy(file.getInputStream(), new FileOutputStream(finalFileName.toFile()));
            return ResultVo.ok(finalFileName);
        }
        return ResultVo.err("upload failed");
    }

}