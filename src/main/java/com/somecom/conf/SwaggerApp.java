package com.somecom.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerApp {
    @Bean
    Docket total() {
        List<Parameter> pars = setHeader();
        return setDocket("com.somecom.controller", "jue-小程序", pars);
    }

//    private Docket setDocket(String path, String groupName) {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName(groupName)
//                .genericModelSubstitutes(DeferredResult.class)
//                .useDefaultResponseMessages(false)
//                .forCodeGeneration(true)
//                .pathMapping(env.getProperty("doc.api.basePath"))
//                .select()
////                .paths(PathSelectors.regex("/api/.*"))
////                .paths(PathSelectors.none())//如果是线上环境，添加路径过滤，设置为全部都不符合
//                .apis(RequestHandlerSelectors.basePackage(path))
//                .paths(PathSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }

    private Docket setDocket(String path, String groupName, List<Parameter> pars) {
        return new Docket(DocumentationType.SWAGGER_2)
                // 生产环境的时候关闭 swagger 比较安全
//                .enable(false)
                .groupName(groupName)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
//                .pathMapping(env.getProperty("doc.api.basePath"))
                .select()
//                .paths(PathSelectors.regex("/api/.*"))
//                .paths(PathSelectors.none())//如果是线上环境，添加路径过滤，设置为全部都不符合
                .apis(RequestHandlerSelectors.basePackage(path))
                .paths(PathSelectors.any())
                .build()
//                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private List<Parameter> setHeader() {
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("token").description("token").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        return pars;
    }

//    private ApiInfo apiInfo() {
//        return new ApiInfo("前置接口",//大标题
//                "内部接口",//小标题
//                env.getProperty("doc.api.version"),
//                env.getProperty("doc.api.termsOfServiceUrl"),
//                env.getProperty("doc.api.contact"),
//                env.getProperty("doc.api.license"),
//                env.getProperty("doc.api.licenseUrl")
//        );
//    }

    //    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                //为当前包路径
//                .apis(RequestHandlerSelectors.basePackage("com.somecom.controller"))
//                .paths(PathSelectors.any())
//                .build();
////        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).build();
//    }
//
//    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title("jue-API文档")
                //创建人
                .contact(new Contact("Sam", "https://github.com/zszj888", "463011648@qq.com"))
                //版本号
                .version("1.0")
                //描述
                .description("jue-小程序API接口文档，提供在线文档，也可以生成离线文档，有需要请联系后台开发人员")
                .build();
    }
}