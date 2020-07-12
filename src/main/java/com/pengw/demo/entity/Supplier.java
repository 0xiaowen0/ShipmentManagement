package com.pengw.demo.entity;

import com.pengw.demo.type.SupplierType;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_supplier")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //名称
    private String supplierName;

    //库存数量
    private Double stockNum;

    //供应商类型
    private SupplierType type;
}
