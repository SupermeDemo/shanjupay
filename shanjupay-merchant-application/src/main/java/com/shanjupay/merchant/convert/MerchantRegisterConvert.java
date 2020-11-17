package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.vo.MerchantRegisterVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @Description: MerchantRegisterConvert
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/17 12:43
 * @since 0.1.0
 */
@Mapper
public interface MerchantRegisterConvert {

    MerchantRegisterConvert INSTANCE = Mappers.getMapper(MerchantRegisterConvert.class);

    //将 dto 转为vo
    MerchantRegisterVO dto2vo(MerchantDTO merchantDTO);

    //将vo 转为 dto
    MerchantDTO vo2dot(MerchantRegisterVO merchantRegisterVO);


}
