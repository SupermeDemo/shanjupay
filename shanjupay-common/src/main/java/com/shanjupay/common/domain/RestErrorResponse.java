package com.shanjupay.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @Description: RestErrorResponse
 * @author: oldmonk
 * @version: 1.0
 * @create 2020/11/17 12:58
 * @since 0.1.0
 */
@Data
@ApiModel(value = "RestErrorResponse",description = "错误响应参数包装")
public class RestErrorResponse {

    private String errCode;

    private String errMessage;

    public RestErrorResponse(String errCode, String errMessage) {
        this.errCode = errCode;
        this.errMessage = errMessage;
    }
}
