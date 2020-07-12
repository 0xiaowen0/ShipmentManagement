package com.pengw.demo.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderDetailsVO {

    @NotNull
    private Long orderId;

    @NotNull
    private Double num;
}
