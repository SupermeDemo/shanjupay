package com.shanjupay.merchant.service;

import com.alibaba.fastjson.JSON;


import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.apache.dubbo.config.annotation.Service;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description: SmsServiceImpl
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/16 19:42
 * @since 0.1.0
 */
@Service  //实例为一个bean
@Slf4j
public class SmsServiceImpl implements SmsService{

    @Value("${sms.url}")
    String smsurl;

    @Value("${sms.effectiveTime}")
    String effectiveTime;

    @Autowired
    RestTemplate restTemplate;



    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return 验证码对应的key
     */
    @Override
    public String sendMsg(String phone) {
        //向验证码服务发送请求的地址

        String url = smsurl+"/generate?effectiveTime="+effectiveTime+"&name=sms";
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
            ResponseEntity<Map> exchange =  restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            log.info("请求验证码服务，得到响应:{}", JSON.toJSONString(exchange));
            responseMap = exchange.getBody();
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info(e.getMessage(),e);
            throw new RuntimeException("发送验证码失败");
        }
        if(responseMap == null || responseMap.get("result") == null){
            throw new RuntimeException("发送验证码失败");
        }

        Map result = (Map) responseMap.get("result");
        String key = (String) result.get("key");
        log.info("得到发送验证码对应的key:{}",key);
        return key;
    }

    /**校验验证码 抛出异常校验无效
     *
     * @param verifiyKey  验证码的key
     * @param verifiyCode 验证码
     */
    @Override
    public void checkVerifiyCode(String verifiyCode, String verifiyKey) throws BusinessException {

        //http://localhost:56085/sailing/verify?name=sms&verificationCode=126319&verificationKey=sms%3Ac068eac11ff1467b87534e8d78beb141
        String url = smsurl+"/verify?name=sms&verificationCode="+verifiyCode+"&verificationKey="+verifiyKey;
        Map bodyMap = null;

        try {
            //请求校验验证码
            ResponseEntity<Map> exchange = restTemplate.exchange(url, HttpMethod.POST, HttpEntity.EMPTY, Map.class);
            bodyMap = exchange.getBody();
            log.info("校验码,响应内容:{}",JSON.toJSONString(bodyMap));
        } catch (RestClientException e) {
            e.printStackTrace();
            log.info(e.getMessage(),e);
            throw new BusinessException(CommonErrorCode.E_100102);
        }
        if(bodyMap == null || bodyMap.get("result") == null || !(Boolean) bodyMap.get("result")){
            throw new BusinessException(CommonErrorCode.E_100102);
           // throw new RuntimeException("发送验证错误");
        }
    }


}
