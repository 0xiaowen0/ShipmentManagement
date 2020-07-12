package com.pengw.demo.vo;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class OrderVO {

    //供应商Id
    @NotNull
    private Long supplierId;
    //公司Id
    @NotNull
    private Long firmId;
    //订单总数量
    @Min(0)
    @NotNull
    private Double total;
    //冻结数量
    @Min(0)
    private Double freezeNum;
    //已完成数量
    @Min(0)
    private Double finishNum;
    //订单创建时间
    private LocalDateTime createTime;
}
