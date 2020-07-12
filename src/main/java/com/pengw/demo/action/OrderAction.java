package com.pengw.demo.action;

import com.pengw.demo.config.Result;
import com.pengw.demo.entity.Order;
import com.pengw.demo.service.OrderService;
import com.pengw.demo.vo.OrderVO;
import com.pengw.demo.vo.PageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("order")
public class OrderAction {

    @Autowired
    OrderService orderService;

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@Valid @RequestBody OrderVO orderVO){
        Order order = new Order();
        BeanUtils.copyProperties(orderVO,order);
        Order supplier = orderService.add(order);
        return Result.success(supplier);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page get(@Nullable @RequestBody PageVO pageVO){
        if (pageVO == null){//分页参数可选
            pageVO = new PageVO();
        }
        return orderService.get(pageVO.getPage(),pageVO.getSize());
    }

}
