package com.somecom.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.ResourceTransformerSupport;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Sam
 * @date 2018/11/3
 */
@Configuration
public class UploadFileConfigurer implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("/login");
        registry.addViewController("/admin/index").setViewName("system/main/index");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/upload/video/**").addResourceLocations("file:/upload/video/")
                .resourceChain(true).addTransformer(new LoggerResourceTransformerSupport());
    }

    private static class LoggerResourceTransformerSupport extends ResourceTransformerSupport {
        private static final Logger log = LoggerFactory.getLogger(LoggerResourceTransformerSupport.class);

        @Override
        public Resource transform(HttpServletRequest httpServletRequest, Resource resource,
                                  ResourceTransformerChain resourceTransformerChain) throws IOException {
            if (httpServletRequest.getRequestURI().endsWith(".mp4")) {
                log.info("Video playing requests,{}", httpServletRequest.getPathInfo());
            }
            return resourceTransformerChain.transform(httpServletRequest, resource);

        }
    }
}
