package com.leslie.feign;

import feign.codec.Decoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yanghaixiao
 **/
@Slf4j
public class TodayWeatherConfiguration {


    @Bean
    public Decoder feignDecoder() {
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(new TextHtmlMessageConverter());
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }


    class TextHtmlMessageConverter extends MappingJackson2HttpMessageConverter {
        public TextHtmlMessageConverter() {
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.TEXT_HTML);
            super.setSupportedMediaTypes(mediaTypes);
        }
    }
}
