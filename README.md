# ShipmentManagement

[![](https://img.shields.io/badge/build-success-brightgreen)](
https://github.com/0xiaowen0/ShipmentManagement) [![](https://img.shields.io/badge/version-1.0-red)](
https://github.com/0xiaowen0/ShipmentManagement) [![](https://img.shields.io/badge/author-PengW-blue)](
https://github.com/0xiaowen0/ShipmentManagement)

**Problem Statement **
Our firm has trading arrangement with a supplier to ship goods to us. As part of trade agreement, the supplier agrees to ship X metric tons of goods in N number of shipments. N can be one or more. The supplier can alter the shipments allowing her to split or merge shipments.
## 需求分析

角色：供应商,公司。
业务：公司向供应商下单，然后供应商负责送货，可以分多次且每次数量不等。
需求：不论拆单还是合单后，最后商品数量总数保持不变。

# split
 公司向供应商下单后，供应商可分批次X次下单数量为Y，每一次下单会在OrderDetails中增加一次信息，记录下每次配送物流Y数量，并记录拆单的时间。
 
# merge
 供应商在每次运送一次货物Y的分量时，会合并到公司的仓储库存中。
 
# change
1.  确认订单时，供应商的库存变更扣除，并冻结该数量信息到Order表中。**旧供应商库存=新供应商库存+冻结库存**
2.  每次的配送后相应变更冻结库存，总数量不变。**订单总数=冻结数量+完成配送数量（每个OrderDetails的Num总和）**
3.  订单配送完成后，商品库存总和数量不变。**旧供应商库存+旧公司库存=新供应商库存+新公司库存**

**订单时序图**：http://tie.027cgb.com/631145/show.jpg

## 项目简介
采用SpringSecurity保证数据**安全性**。原子业务模块添加Transactional保证数据**一致性、完整性**，对参数对象封装，并添加@Valid数据接口校验。 增加全局异常捕获机**GlobalExceptionConfig**，并添加自定义异常**ApiException**。
**系统环境**：jdk8以上
**系统框架**：SpringBoot+SpringMvc+SpringSecurity+SpringJPA+Hibernate+MySql
**数据库**：MySql
**用户名密码**：user/123456(暂未完善，后期改进需要配合数据库动态读取配置)
**项目目录结构:**

1.  acton:项目请求入口
2.  config:项目配置全局异常处理
3.  dao：数据基本操作增删改查
4.  entity：对象关系映射模型
5.  security：安全策略，以及接口用户登录配置
6.  service：服务核心系统
7.  type：枚举类型(用户类型、订单状态)
8.  vo：请求接口参数封装

## 单元测试说明

1. contextLoads：初始化数据，这套系统的流程根据时效图
    1. 初始化“红太阳供应商”供应商名称以及总库存数据
    2. 初始化“托克集团”公司数据
    3. 向供应商发起订单
    4. 供应商向公司发货
    5. 再次下单，此单超过发起的总单据，异常捕获
2. showSupplier：查询供应商列表

## 核心代码描述
在每个业务中都加入了@Transactional，保证系统数据的一致性，进一步优化加入乐观锁。

* 公司先向供应商发起订单，先校验供应商的库存是否足够，如果足够，那么先扣除库存商品，生成订单，冻结记录相应扣除的数量。

```Java
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
```

供应商向公司发货，从冻结的商品扣除，并添加到公司库存中，如果订单完成变更订单状态WAIT
-->FINISH
```java
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
```

## RestAPI/初始化说明

1. 项目接口遵守**RESTful**规范的设计风格和开发方式。

* 项目运行时会自动在生成数据库表
* 运行junit测试中的contextLoads方法对数据初始化
* 访问接口

2. Api接口文档

##### 查询商家列表
> GET：[localhost:8080/supplier](localhost:8080/supplier)
> 格式类型：JSON
> 数据放入：RequestBody
###### 请求参数
 |参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|page    |false    |Integer|请求第几页 |
|size    |false    |Integer |请求页面的大小|

###### 示例
```json
{
    "page":1,
    "size":2
}
```

##### 添加商家列表
> POST：[localhost:8080/supplier](localhost:8080/supplier)
> 格式类型：JSON
> 数据提交方式：body
###### 请求参数
 |参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|supplierName    |true    |string|供应商名称 |
|stockNum    |true    |double |初始化商家库存数量|
|type    |true    |enum |SUPPLIER/供应商-FIRM/公司|

###### 示例
```json
{
    "supplierName":"公司",
    "stockNum":100,
    "type":"FIRM"
}
```
##### 发起订单
> POST：[localhost:8080/order](localhost:8080/order)
> 格式类型：JSON
> 数据提交方式：body
###### 请求参数
 |参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|supplierId    |true    |long|供应商id |
|firmId    |true    |long |公司id|
|total    |true    |double |发起订单数量|

###### 示例
```json
{
    "supplierId":1,
    "firmId":2,
    "total":200
}
```
##### 供应商发货（分批次发货）
> POST：[localhost:8080/orderDetails](localhost:8080/orderDetails)
> 格式类型：JSON
> 数据提交方式：body
###### 请求参数
 |参数|必选|类型|说明|
|:-----  |:-------|:-----|-----|
|orderId    |true    |long|订单id |
|num    |true    |long |发货数量|

###### 示例
```json
{
    "orderId":9,
    "num":1
}       
```







