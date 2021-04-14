package com.zylear.commons.bean;

import com.zylear.commons.annotation.Mapped;
import com.zylear.commons.annotation.Mappeds;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


@ApiModel
@Data
public class Result<T> {

    @ApiModelProperty(value = "错误信息码")
    private Integer code;

    @ApiModelProperty(value = "错误信息")
    private String message;

    @ApiModelProperty(value = "错误信息的参数列表")
    private List<String> arguments;


    @ApiModelProperty(value = "数据")
    private T data;


}
