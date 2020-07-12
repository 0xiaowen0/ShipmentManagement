package com.pengw.demo;

import com.alibaba.fastjson.JSON;
import com.pengw.demo.action.OrderAction;
import com.pengw.demo.action.OrderDetailsAction;
import com.pengw.demo.action.SupplierAction;
import com.pengw.demo.config.Result;
import com.pengw.demo.entity.Order;
import com.pengw.demo.entity.OrderDetails;
import com.pengw.demo.entity.Supplier;
import com.pengw.demo.type.SupplierType;
import com.pengw.demo.vo.OrderDetailsVO;
import com.pengw.demo.vo.OrderVO;
import com.pengw.demo.vo.SupplierVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.Assert;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
class DemoApplicationTests {

    @Autowired
    SupplierAction supplierAction;
    @Autowired
    OrderAction orderAction;
    @Autowired
    OrderDetailsAction orderDetailsAction;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void contextLoads() throws Exception {
        /**
         * 添加商家
         * POST：localhost:8080/supplier
         * {
         * 	"supplierName": "红太阳供应商",
         * 	"stockNum": 200,
         * 	"type": "SUPPLIER"(SUPPLIER/FIRM)
         * }
         */
        //初始化供应商
        SupplierVO supplierVO = new SupplierVO();
        supplierVO.setSupplierName("红太阳供应商");
        supplierVO.setStockNum(200D);
        supplierVO.setType(SupplierType.SUPPLIER);
        Result<Supplier> supplierResult = supplierAction.add(supplierVO);
        assertGo(supplierResult, "初始化供应商信息失败");
        Supplier supplier = supplierResult.getData();

        //初始化公司
        SupplierVO firmVO = new SupplierVO();
        firmVO.setSupplierName("托克集团");
        firmVO.setStockNum(100D);
        firmVO.setType(SupplierType.FIRM);
        Result<Supplier> firmResult = supplierAction.add(firmVO);
        assertGo(firmResult, "初始化公司信息失败");
        Supplier firm = firmResult.getData();

        //"托克集团"向供应商下200订单
        OrderVO orderVO = new OrderVO();
        orderVO.setSupplierId(supplier.getId());
        orderVO.setFirmId(firm.getId());
        orderVO.setTotal(200D);
        Result<Order> orderResult = orderAction.add(orderVO);
        assertGo(orderResult, "下单失败");
        Order order = orderResult.getData();

        //供应商发货50给公司
        OrderDetailsVO orderDetailsVO = new OrderDetailsVO();
        orderDetailsVO.setOrderId(order.getId());
        orderDetailsVO.setNum(50D);
        Result<OrderDetails> orderDetailsResult = orderDetailsAction.add(orderDetailsVO);
        assertGo(orderDetailsResult, "发货失败");

        //供应商发货超出订单数给公司
        OrderDetailsVO orderDetailsVO1 = new OrderDetailsVO();
        orderDetailsVO1.setOrderId(order.getId());
        orderDetailsVO1.setNum(500D);

        MvcResult mvcResult = mockMvc.perform(
                post("/orderDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON.toJSONString(orderDetailsVO1)))
                .andReturn();
        String resultStr = mvcResult.getResponse().getContentAsString();
        Result r = JSON.parseObject(resultStr, Result.class);
        System.out.println("供应商订单超发货异常消息捕获："+r.getMessage());
    }

    @Test
    void showSupplier() throws Exception {
        /**
         * 分页查询商家
         * GET：localhost:8080/supplier
         * {
         *     "page":1,
         *     "size":10
         * }
         */
        MvcResult mvcResult = mockMvc.perform(
                get("/supplier"))
                .andReturn();
        String supplierList = mvcResult.getResponse().getContentAsString();
        System.out.println("商家信息列表："+supplierList);
    }

    /**
     * 断言请求
     *
     * @param result
     * @param addMsg
     */
    private void assertGo(Result result, String addMsg) {
        Assert.isTrue(isSuccess(result), addMsg + result.getMessage());
    }

    /**
     * 判断请求是否成功
     *
     * @param result
     * @return
     */
    private Boolean isSuccess(Result result) {
        if (result != null && result.getRetCode() == 0) {
            return true;
        }
        return false;
    }

}
