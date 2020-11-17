package com.shanjupay.merchant.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shanjupay.common.domain.BusinessException;
import com.shanjupay.common.domain.CommonErrorCode;
import com.shanjupay.common.util.StringUtil;
import com.shanjupay.merchant.api.MerchantService;
import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.convert.MerchantCovert;
import com.shanjupay.merchant.entity.Merchant;
import com.shanjupay.merchant.mapper.MerchantMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.beans.Transient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description: MerchantServiceImpl
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/15 19:59
 * @since 0.1.0
 */
@Service
@Slf4j
public class MerchantServiceImpl implements MerchantService {

    @Autowired
    MerchantMapper merchantMapper;

    /**
     * 根据Id查询详细信息
     * @param id merchantId
     * @return
     */
    @Override
    public MerchantDTO queryMerchantById(Long id) {
        Merchant merchant = merchantMapper.selectById(id);
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(merchant.getId());
        merchantDTO.setMerchantName(merchant.getMerchantName());
        return merchantDTO;
    }

    @Override
    @Transactional
    public MerchantDTO createMerchant(MerchantDTO merchantDTO) {
        //校验
        if (merchantDTO == null){
            throw  new BusinessException(CommonErrorCode.E_100108);
        }
        //手机号非空校验
        if (StringUtil.isBlank(merchantDTO.getMobile())){
            throw  new BusinessException(CommonErrorCode.E_100112);
        }
        //联系人非空校验
        if (StringUtil.isBlank(merchantDTO.getUsername())){
            throw  new BusinessException(CommonErrorCode.E_100110);
        }
        //密码非空校验
        if (StringUtil.isBlank(merchantDTO.getPassword())){
            throw  new BusinessException(CommonErrorCode.E_100111);
        }

        //校验商户手机号的唯一性，根据商户的手机号查询商户表，如果存在记录则说明手机号码已经重复
        LambdaQueryWrapper<Merchant> lambdaQueryWrapper = new LambdaQueryWrapper<Merchant>()
                .eq(Merchant::getMobile, merchantDTO.getMobile());
        Integer count = merchantMapper.selectCount(lambdaQueryWrapper);
        if (count>0){
            throw  new BusinessException(CommonErrorCode.E_100113);
        }
        //Merchant merchant = new Merchant();
        //将 dot 转为 entity
        Merchant merchant = MerchantCovert.INSTANCE.dto2entity(merchantDTO);
        //设置审核状态 0 没有申请 1 已申请等待审核 2 审核通过 3 审核拒绝
        merchant.setAuditStatus("0");
        //设置手机号
        merchant.setMobile(merchant.getMobile());
        //...
        //保存商户
        merchantMapper.insert(merchant);
        //将新增商户id返回
        //将 entity 转为 dto
        MerchantDTO merchantDTONEW = MerchantCovert.INSTANCE.entity2dto(merchant);
        merchantDTO.setId(merchant.getId());

        return merchantDTONEW;
    }


}
