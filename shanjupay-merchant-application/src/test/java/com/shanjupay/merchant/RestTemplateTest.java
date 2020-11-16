package com.shanjupay.merchant;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: RestTemplateTest
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/16 17:46
 * @since 0.1.0
 */

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RestTemplateTest {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 测试okHttp
     */
    @Test
    public void getHtml(){
        String url = "http://www.baidu.com/";
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        String body = forEntity.getBody();
        System.out.println(body);
    }

    /**
     * 测试短信验证码的获取
     */
    @Test
    public void testGetSmsCode(){
        //验证码过期世界为600秒
        String url = "http://localhost:56085/sailing/generate?effectiveTime=600&name=sms";
        String phone = "2324345354";
        log.info("调用短信微服务验证码: url:{}",url);

        //请求体
        Map<String,Object> body = new HashMap<>();
        body.put("mobile",phone);
        //请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        //设置格式为json
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //封装请求参数
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);

        Map responseMap = null;
        try {
            //post请求
            ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            log.info("调用短信验证码微服务:返回值:{}", JSON.toJSONString(exchange));
            //获取响应体
            responseMap = exchange.getBody();
        } catch (RestClientException e) {
            log.info(e.getMessage(),e);
        }
        //获取body中的result数据
        if (responseMap !=null || responseMap.get("result") !=null){
            Map resultMap = (Map) responseMap.get("result");
            String value = resultMap.get("key").toString();
            System.out.println(value);
        }


    }

}
