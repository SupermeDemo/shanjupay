package com.shanjupay.merchant.convert;

import com.shanjupay.merchant.api.dto.MerchantDTO;
import com.shanjupay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义 dto 与 entity 之间的转换
 * @Description: MerchantCovert
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/17 12:28
 * @since 0.1.0
 */
@Mapper
public interface MerchantCovert {

    MerchantCovert INSTANCE = Mappers.getMapper(MerchantCovert.class);

    MerchantDTO entity2dto(Merchant entity);

    //dot ->entity
    Merchant dto2entity(MerchantDTO dto);

    // List之间的转换
    List<MerchantDTO> listentity2dto(List<Merchant> list);


    public static void main(String[] args) {
        //dot 2 entity
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setUsername("测试");
        merchantDTO.setPassword("345354");
        Merchant entity = MerchantCovert.INSTANCE.dto2entity(merchantDTO);

        //entity 转 dto
        entity.setMobile("2134234");
        MerchantDTO merchantDTO1 = MerchantCovert.INSTANCE.entity2dto(entity);
        System.out.println(merchantDTO1);


        ArrayList<Merchant> list_entity = new ArrayList<>();
        list_entity.add(entity);
        List<MerchantDTO> merchantDTOS = MerchantCovert.INSTANCE.listentity2dto(list_entity);
        System.out.println(merchantDTOS);

    }
}
