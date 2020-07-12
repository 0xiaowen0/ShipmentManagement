package com.pengw.demo.action;

import com.pengw.demo.service.OrderDetailsService;
import com.pengw.demo.service.OrderService;
import com.pengw.demo.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping
@Validated
public class IndexAction {

    @Autowired
    SupplierService supplierService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderDetailsService orderDetailsService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @RequestMapping(value = "user",method = RequestMethod.GET)
    public String user(){
        return "user";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "admin",method = RequestMethod.GET)
    public String admin(){
        return "admin";
    }
}
