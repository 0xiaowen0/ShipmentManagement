package com.pengw.demo.service;

import com.pengw.demo.config.ApiException;
import com.pengw.demo.dao.OrderDao;
import com.pengw.demo.dao.OrderDetailsDao;
import com.pengw.demo.dao.SupplierDao;
import com.pengw.demo.entity.Order;
import com.pengw.demo.entity.OrderDetails;
import com.pengw.demo.entity.Supplier;
import com.pengw.demo.type.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class OrderDetailsService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    SupplierDao supplierDao;
    @Autowired
    OrderDetailsDao orderDetailsDao;

    public Page get(int page, int size){
        return orderDetailsDao.findAll(PageRequest.of(page,size));
    }

    public OrderDetails add(Long orderId,Double num){
        Order order = orderDao.getOne(orderId);
        Double freezeNum = order.getFreezeNum();
        if(order.getFreezeNum()<num){
            throw new ApiException("订单出货量溢出");
        }
        //扣除冻结库存+添加完成货物数量
        double finishNum = order.getFinishNum() + num;
        order.setFreezeNum(freezeNum - num);
        order.setFinishNum(finishNum);
        if(order.getTotal() == finishNum){
            order.setStatus(OrderStatus.FINISH);
        }else {
            order.setStatus(OrderStatus.WAIT);
        }
        orderDao.save(order);

        //增加公司库存
        Supplier firmDO = supplierDao.getOne(order.getFirmId());
        firmDO.setStockNum(firmDO.getStockNum()+num);
        supplierDao.save(firmDO);

        //添加订单批量完成订单
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setOrder(order);
        orderDetails.setNum(num);
        orderDetails.setCreatTime(LocalDateTime.now());
        OrderDetails orderDetails1 = orderDetailsDao.save(orderDetails);

        return  orderDetails;
    }
}
