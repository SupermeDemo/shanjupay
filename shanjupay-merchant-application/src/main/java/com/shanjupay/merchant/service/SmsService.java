package com.shanjupay.merchant.service;

/**
 * @Description: SmsService
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/16 19:20
 * @since 0.1.0
 */
public interface SmsService {

    /**
     * 发送手机验证码
     *
     * @param phone 手机号
     * @return 验证码对应的key
     */
    String sendMsg(String phone);
}

