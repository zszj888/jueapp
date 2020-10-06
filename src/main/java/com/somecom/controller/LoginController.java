package com.somecom.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.somecom.model.ResultVo;
import com.somecom.repo.UserRepository;
import com.somecom.util.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;

@Api(tags = "微信登录验证")
@RestController
@RequestMapping(path = "/wx")
public class LoginController {
    private final static String WX_URL = "https://api.weixin.qq.com/sns/jscode2session?" +
            "appid=wx61089dda2b3d8b09&secret=1e8f235c05a63838358076eb10c96624&grant_type=authorization_code&js_code=";
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Resource
    private UserRepository userRepository;
    @Value("${wx.file.path}")
    private String filePath;
    @Autowired
    private RestTemplate restTemplate;

    public String getWxResult(String code) {
        log.info("To getWxResult :{}", code);
        String forObject = restTemplate.getForObject(WX_URL + code, String.class);
        log.info("Code:{}, WX return:{}", code, forObject);
        return forObject;
    }

    public static void main(String[] args) {
        JsonNode read = JSON.read(new RestTemplate().getForObject(WX_URL + "013qKXkl2oWMJ54xcWnl2DypC11qKXkv", String.class));
//        JsonNode read = JSON.read(getWxResult("013qKXkl2oWMJ54xcWnl2DypC11qKXkv"));
        int errcode = read.get("errcode").asInt();
        System.out.println(read.toString());
    }

    @ApiOperation(value = "验证接口，传入code即可", notes = "详情参考微信API")
    @GetMapping(path = "/finishLogin")
    @ApiImplicitParam(name = "code", required = true, dataType = "string", paramType = "query")
    public String login(String code) {
        if (StringUtils.hasText(code)) {
            JsonNode read = JSON.read(getWxResult(code));
            log.info("Json read result:{}", read.toString());
            return read.toString();
//            openid = read.get("openid").asText();
//            Optional<User> one = userRepository.findOne(Example.of(new User(openid)));
//            String finalOpenid = openid;
//            User user = one.orElseGet(() -> userRepository.save(new User(finalOpenid)));

//            SystemUtil.login(one.orElse(user));
        }
        return "Code必传";
    }

    @ApiOperation(value = "上传文件接口", notes = "上传头像或者短视频")
    @PostMapping(path = "/upload")
    public ResultVo upload(MultipartFile file, String openid, HttpServletRequest request) throws IOException {
        if (!Files.exists(Paths.get(filePath))) {
            Files.createDirectories(Paths.get(filePath));
        }
        if (!StringUtils.hasText(openid)) {
            return ResultVo.err("openid为空");
        }
        if (!Files.exists(Paths.get(filePath, openid))) {
            Files.createDirectories(Paths.get(filePath, openid));
        }
        String fileName = UUID.randomUUID().toString().replaceAll("-", "");
        if (Objects.nonNull(file)) {
            Path finalFileName = Paths.get(filePath, openid,
                    fileName + "." + StringUtils.getFilenameExtension(file.getOriginalFilename()));
            IOUtils.copy(file.getInputStream(), new FileOutputStream(finalFileName.toFile()));
            CharSequence charSequence = request.getRequestURL().subSequence(0, request.getRequestURL().indexOf(request.getRequestURI()));
            return ResultVo.ok(charSequence.toString() + "/files/" + finalFileName.toString().split(filePath)[1]);
        }
        return ResultVo.err("upload failed");
    }

}