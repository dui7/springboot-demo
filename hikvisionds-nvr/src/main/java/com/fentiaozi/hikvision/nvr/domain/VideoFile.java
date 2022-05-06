package com.fentiaozi.hikvision.nvr.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "视频文件", description = "视频文件")
public class VideoFile {

    @ApiModelProperty(value = "文件名称", example = "文件名称")
    private String fileNme;

    @ApiModelProperty(value = "文件大小", example = "文件大小")
    private String fileSize;

    @ApiModelProperty(value = "开始时间", example = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间", example = "结束时间")
    private String endTime;
}
