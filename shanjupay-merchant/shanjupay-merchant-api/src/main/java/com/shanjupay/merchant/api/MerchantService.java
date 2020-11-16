package com.shanjupay.merchant.api;

import com.shanjupay.merchant.api.dto.MerchantDTO;

import java.util.List;

/**
 * @Description: MerchantService
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/15 19:55
 * @since 0.1.0
 */
public interface MerchantService {

    /**根据ID查询详细信息
     * @param id
     * @return
     */
    //根据 id查询商户
    public MerchantDTO queryMerchantById(Long id);



}
