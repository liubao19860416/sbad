package com.yhjr.basic.config;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.yhjr.basic.controller.base.MyGlobalController;

/**
 * WebMVC配置定义信息加载
 * 
 * @author LiuBao
 * @version 2.0 
 * 2017年3月27日
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class,WebMvcConfigurerAdapter.class })
//@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
//@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 1)
public class BaseWebMvcConfiguration extends WebMvcConfigurerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseWebMvcConfiguration.class);

    @Value("${resources.staticLocations:classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/,classpath:/templates/}")
    private String[]  resourcesStaticLocations ;
    
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        converters.add(customFastJsonHttpMessageConverter());
//        //converters.add(customMappingJackson2HttpMessageConverter());
//        super.addDefaultHttpMessageConverters(converters);
//        LOGGER.info("configureMessageConverters执行了");
//    }
    
	@Bean
    public MappingJackson2HttpMessageConverter customMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter=new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        jsonConverter.setObjectMapper(objectMapper);
        List<MediaType> supportedMediaTypes=new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        return jsonConverter;
    }
	
	/**
	 * 改配置默認生效
	 */
    @Bean
    @SuppressWarnings("deprecation")
    public FastJsonHttpMessageConverter customFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter() {
            @Override
            protected void writeInternal(Object obj, HttpOutputMessage outputMessage) 
                    throws IOException, HttpMessageNotWritableException {
                //super.writeInternal(obj,outputMessage);
                OutputStream out = outputMessage.getBody();
                String text = JSON.toJSONString(obj,new NullValueFilter(), super.getFeatures());
                byte[] bytes = text.getBytes(super.getCharset());
                out.write(bytes);
            }
        };
        SerializerFeature[] features = new SerializerFeature[] { /*SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse,*/
                SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat };
        jsonConverter.setFeatures(features);
        jsonConverter.setCharset(Charset.forName("UTF-8"));
        List<MediaType> supportedMediaTypes=new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        //supportedMediaTypes.add(MediaType.APPLICATION_XML);
        //supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        //supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        //supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        return jsonConverter;
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("注入的参数信息为:{}",JSON.toJSONString(resourcesStaticLocations));
        }
        if(ArrayUtils.isNotEmpty(resourcesStaticLocations)){
            registry.addResourceHandler("/**").addResourceLocations(resourcesStaticLocations);
        }
        super.addResourceHandlers(registry);
    }
    
    /**
     * 解决不引入改配置报错问题
     */
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    /**
     * 设置异常错误处理信息
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, MyGlobalController.ERROR_400);
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, MyGlobalController.ERROR_401);
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, MyGlobalController.ERROR_404);
                ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, MyGlobalController.ERROR_405);
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, MyGlobalController.ERROR_500);
                container.addErrorPages(error400Page, error401Page, error404Page, error405Page, error500Page);
                LOGGER.info("设置异常错误处理信息结束...");
            }
        };
    }
	
}
