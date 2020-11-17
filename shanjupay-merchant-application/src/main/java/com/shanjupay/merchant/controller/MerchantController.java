package com.shanjupay.merchant.controller;

import com.alibaba.fastjson.JSON;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.PhoneUtil;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.convert.MerchantRegisterConvert;
import com.shanjupay.merchant.service.FileService;
import com.shanjupay.merchant.service.SmsService;
import com.shanjupay.merchant.vo.MerchantRegisterVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;


/**
 * @Description: MerchantController
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/15 20:03
 * @since 0.1.0
 */
@RestController
@Slf4j
public class MerchantController {

    @Reference
    private MerchantService merchantService;

    @Reference
    private SmsService smsService;

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "根据id查询商户信息")
    @GetMapping("/merchants/{id}")
    public MerchantDTO queryMerchantById(@PathVariable("id") Long id) {
        MerchantDTO merchantDTO = merchantService.queryMerchantById(id);
        return merchantDTO;
    }

    @ApiOperation("测试")
    @GetMapping(path = "/hello")
    public String hello() {
        return "hello";
    }


    @ApiOperation("测试")
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string")
    @PostMapping(value = "/hi")
    public String hi(String name) {
        return "hi," + name;
    }

    @ApiOperation("获取手机验证码")
    @GetMapping("/sms")
    @ApiImplicitParam(value = "手机号", name = "phone", required = true, dataType = "string", paramType = "query")
    public String getSMSCode(@RequestParam("phone") String phone) {
        //向验证码服务请求发送验证码
        return smsService.sendMsg(phone);
    }

    @ApiOperation("商户注册")
    @ApiImplicitParam(value = "商户注册信息", name = "merchantRegisterVO", required = true, dataType = "MerchantRegisterVO", paramType = "body")
    @PostMapping("/merchants/register")
    public MerchantRegisterVO registerMerchant(@RequestBody MerchantRegisterVO merchantRegisterVO) {

        if (merchantRegisterVO == null){
            throw new BusinessException(CommonErrorCode.E_100108);
        }
        if (StringUtil.isBlank(merchantRegisterVO.getMobile())){
            throw  new BusinessException(CommonErrorCode.E_100112);
        }
        if (!PhoneUtil.isMatches(merchantRegisterVO.getMobile())){
            throw  new BusinessException(CommonErrorCode.E_100109);
        }
        if (StringUtil.isBlank(merchantRegisterVO.getUsername())){
            throw  new BusinessException(CommonErrorCode.E_100110);
        }
        if (StringUtil.isBlank(merchantRegisterVO.getPassword())){
            throw  new BusinessException(CommonErrorCode.E_100111);
        }
        //验证码校验
        if (StringUtil.isBlank(merchantRegisterVO.getVerifiyCode())|| StringUtil.isBlank(merchantRegisterVO.getVerifiykey())){
            throw new BusinessException(CommonErrorCode.E_100103);
        }

       //校验验证码
        smsService.checkVerifiyCode(merchantRegisterVO.getVerifiyCode()
                ,merchantRegisterVO.getVerifiykey());
        //注册商户
        //MerchantDTO merchantDTO = new MerchantDTO();

        MerchantDTO merchantDTO = MerchantRegisterConvert.INSTANCE.vo2dot(merchantRegisterVO);
        /*merchantDTO.setUsername(merchantRegisterVO.getUsername());
        merchantDTO.setMobile(merchantRegisterVO.getMobile());
        merchantDTO.setPassword(merchantRegisterVO.getPassword());*/
        merchantService.createMerchant(merchantDTO);
        return merchantRegisterVO;
    }


    @ApiOperation("证件上传")
    @PostMapping("/upload")
    public String upload(@ApiParam(value = "上传的文件",required = true) @RequestParam("file")
                         MultipartFile multipartFile) throws IOException {
        //调用fileService上传文件
        //生成的文件名称fileName，要保证它的唯一
        //文件原始名称
        String originalFilename = multipartFile.getOriginalFilename();
        //扩展名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")-1);
        //文件名称
        String fileName = UUID.randomUUID()+suffix;
        //byte[] bytes,String fileName
        return fileService.upload(multipartFile.getBytes(),fileName);
    }

}


