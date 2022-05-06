package com.fentiaozi.hikvision.nvr.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wgq
 */
@Data
public class NvrTime {
    @ApiModelProperty(value = "年", example = "年")
    private Integer dwYear;

    @ApiModelProperty(value = "月", example = "月")
    private Integer dwMonth;

    @ApiModelProperty(value = "日", example = "日")
    private Integer dwDay;

    @ApiModelProperty(value = "时", example = "时")
    private Integer dwHour;

    @ApiModelProperty(value = "分", example = "分")
    private Integer dwMinute;

    @ApiModelProperty(value = "秒", example = "秒")
    private Integer dwSecond;
}
