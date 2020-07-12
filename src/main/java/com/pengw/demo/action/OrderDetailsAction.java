package com.pengw.demo.action;

import com.pengw.demo.config.Result;
import com.pengw.demo.entity.OrderDetails;
import com.pengw.demo.service.OrderDetailsService;
import com.pengw.demo.vo.OrderDetailsVO;
import com.pengw.demo.vo.PageVO;
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
@RequestMapping("orderDetails")
public class OrderDetailsAction {

    @Autowired
    OrderDetailsService orderDetailsService;

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@Valid @RequestBody OrderDetailsVO orderDetailsVO){
        OrderDetails orderDetails = orderDetailsService.add(orderDetailsVO.getOrderId(),orderDetailsVO.getNum());
        return Result.success(orderDetails);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Page get(@Nullable @RequestBody PageVO pageVO){
        if (pageVO == null){//分页参数可选
            pageVO = new PageVO();
        }
        return orderDetailsService.get(pageVO.getPage(),pageVO.getSize());
    }
}
