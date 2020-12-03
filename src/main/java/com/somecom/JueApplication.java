package com.somecom;

import com.somecom.utils.ToolUtil;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.unit.DataSize;
import org.springframework.web.client.RestTemplate;

import javax.servlet.MultipartConfigElement;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
@EnableCaching // 开启缓存
public class JueApplication {
    public static void main(String[] args) {
        SpringApplication.run(JueApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() throws IOException {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        Path path = Paths.get(Paths.get(ToolUtil.getProjectPath()) + "/temp/");
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        factory.setLocation(path.toString());

        factory.setMaxFileSize(DataSize.ofGigabytes(4));
        factory.setMaxRequestSize(DataSize.ofTerabytes(1));
        return factory.createMultipartConfig();
    }

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(8888);
        return connector;
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
        return new RestTemplate(factory);
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);//单位为ms
        factory.setConnectTimeout(5000);//单位为ms
        return factory;
    }

}
