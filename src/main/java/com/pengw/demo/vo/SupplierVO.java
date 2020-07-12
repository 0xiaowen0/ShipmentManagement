package com.pengw.demo.vo;

import com.pengw.demo.type.SupplierType;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SupplierVO {

    @NotBlank
    String supplierName;

    @NotNull
    Double stockNum;

    @NotNull
    SupplierType type;
}
