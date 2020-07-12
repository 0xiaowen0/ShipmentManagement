package com.pengw.demo.action;

import com.pengw.demo.config.Result;
import com.pengw.demo.entity.Supplier;
import com.pengw.demo.service.SupplierService;
import com.pengw.demo.vo.PageVO;
import com.pengw.demo.vo.SupplierVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("supplier")
public class SupplierAction {

    @Autowired
    SupplierService supplierService;

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@Valid @RequestBody SupplierVO supplierVO){
        Supplier supplierDO = new Supplier();
        BeanUtils.copyProperties(supplierVO,supplierDO);
        Supplier supplier = supplierService.add(supplierDO);
        return Result.success(supplier);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Result get(@Nullable @RequestBody PageVO pageVO){
        if (pageVO == null){//分页参数可选
            pageVO = new PageVO();
        }
        return Result.success(supplierService.get(pageVO.getPage(),pageVO.getSize()));
    }

    @RequestMapping(method = RequestMethod.PUT)
    public Result update(@Valid @RequestBody SupplierVO supplierVO){
        Supplier supplierDO = new Supplier();
        BeanUtils.copyProperties(supplierVO,supplierDO);
        Supplier supplier = supplierService.update(supplierDO);
        return Result.success(supplier);
    }
}
