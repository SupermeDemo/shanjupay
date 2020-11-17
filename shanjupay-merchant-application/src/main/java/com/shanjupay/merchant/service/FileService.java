package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;

/**
 * @Description: FileService
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/17 17:25
 * @since 0.1.0
 */
public interface FileService {

    /**上传文件
     * @param bytes 文件字节数组
     * @param fileName 文件名
     * @return 文件的访问路径 绝对地址
     * @throws BusinessException
     */
    public String upload(byte[] bytes,String fileName) throws BusinessException;
}
