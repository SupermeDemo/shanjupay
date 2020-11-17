package com.shanjupay.merchant.service;

import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.QiniuUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Description: FileServiceImpl
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/17 17:58
 * @since 0.1.0
 */
@Slf4j
@Service
public class FileServiceImpl implements FileService{

    @Value("${oss.qiniu.url}")
    private String qiniuUrl;
    @Value("${oss.qiniu.accessKey}")
    private String accessKey;
    @Value("${oss.qiniu.secretKey}")
    private String secretKey;
    @Value("${oss.qiniu.bucket}")
    private String bucket;

    /**上传文件
     * @param bytes    文件字节数组
     * @param fileName 文件名
     * @return
     * @throws BusinessException
     */
    @Override
    public String upload(byte[] bytes, String fileName) throws BusinessException {

        try {
            QiniuUtils.upload2qiniu(accessKey,secretKey,bucket,bytes,fileName);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new BusinessException(CommonErrorCode.E_100106);
        }
        //返回文件名称
        return qiniuUrl+fileName;
    }
}
