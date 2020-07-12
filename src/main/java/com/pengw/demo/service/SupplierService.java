package com.pengw.demo.service;

import com.pengw.demo.config.ApiException;
import com.pengw.demo.dao.SupplierDao;
import com.pengw.demo.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SupplierService {

    @Autowired
    SupplierDao supplierDao;

    public Page get(int page, int size){
        return supplierDao.findAll(PageRequest.of(page,size));
    }

    public Supplier update(Supplier supplier){
        return supplierDao.save(supplier);
    }

    public Supplier add(Supplier supplier){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("supplierName",m -> m.equals(supplier.getSupplierName()));
        Example<Supplier> example =  Example.of(supplier,matcher);
        if(!supplierDao.findAll(example).isEmpty()){
            throw new ApiException("供应商已存在");
        }
        return supplierDao.save(supplier);
    }
}
