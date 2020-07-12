package com.pengw.demo.service;

import com.pengw.demo.config.ApiException;
import com.pengw.demo.dao.OrderDao;
import com.pengw.demo.dao.SupplierDao;
import com.pengw.demo.entity.Order;
import com.pengw.demo.entity.Supplier;
import com.pengw.demo.type.OrderStatus;
import com.pengw.demo.type.SupplierType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
public class OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    SupplierDao supplierDao;


    public Page get(int page, int size){
        return orderDao.findAll(PageRequest.of(page,size));
    }

    public Order add(Order order){
        Long supplierId = order.getSupplierId();
        Supplier supplierDO = supplierDao.getOne(supplierId);
        //数据先校验一波在做后面逻辑
        if(supplierDO!= null && supplierDO.getType() != SupplierType.SUPPLIER){
            throw new ApiException("供应商信息异常");
        }
        if(supplierDO.getStockNum() < order.getTotal()){
            throw new ApiException("供应商货品不足");
        }
        Long firmId = order.getFirmId();
        Supplier firmDO = supplierDao.getOne(firmId);
        if(firmDO!= null && firmDO.getType() != SupplierType.FIRM){
            throw new ApiException("公司信息异常");
        }
        //扣除供应商库存
        supplierDO.setStockNum(supplierDO.getStockNum() - order.getTotal());
        supplierDao.save(supplierDO);
        //添加订单
        order.setFreezeNum(order.getTotal());
        order.setFinishNum(0D);
        order.setStatus(OrderStatus.START);
        order.setCreateTime(LocalDateTime.now());
        return orderDao.save(order);
    }
}
