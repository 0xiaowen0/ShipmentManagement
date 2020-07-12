package com.pengw.demo.entity;

import com.pengw.demo.type.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "t_order")
public class Order {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //供应商Id
    private Long supplierId;
    //公司Id
    private Long firmId;
    //订单总数量
    private Double total;
    //冻结数量
    private Double freezeNum;
    //已完成数量
    private Double finishNum;
    //订单状态
    private OrderStatus status;
    //订单创建时间
    private LocalDateTime createTime;

    @OneToMany(mappedBy = "order",cascade=CascadeType.ALL,fetch= FetchType.LAZY)
    //级联保存、更新、删除、刷新;延迟加载。当删除用户，会级联删除该用户的所有文章
    //拥有mappedBy注解的实体类为关系被维护端
    //mappedBy="author"中的author是Article中的author属性
//    @JsonIgnore
    private List<OrderDetails> orderDetailsList;//文章列表




}
