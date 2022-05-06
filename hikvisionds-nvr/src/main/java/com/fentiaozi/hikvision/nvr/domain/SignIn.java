package com.fentiaozi.hikvision.nvr.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "用户注册", description = "用户注册")
public class SignIn {
    @ApiModelProperty(value = "NVR IP地址", example = "NVR IP地址")
    private String ip;

    @ApiModelProperty(value = "端口", example = "端口")
    private int port;

    @ApiModelProperty(value = "用户名", example = "用户名")
    private String userName;

    @ApiModelProperty(value = "密码", example = "密码")
    private String password;

    @ApiModelProperty(value = "是否在线", example = "是否在线")
    private String isLine;

    @ApiModelProperty(value = "设备ID", example = "设备ID")
    private String deviceId;

    @ApiModelProperty(value = "摄像头 设备IP", example = "摄像头 设备IP")
    private String deviceIp;

    @ApiModelProperty(value = "参数appId", example = "参数appId")
    private String appId;

    @ApiModelProperty(value = "通道号", example = "通道号")
    private int channelNumber;
}
