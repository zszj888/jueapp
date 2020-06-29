package com.somecom.demo.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "t_order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;//订单id;
    Integer order_id;//订单编号
    Integer status;//商品状态;0待付款1;待发货
    Integer order_type;//1是否是门店自提2;外卖订单
    String address;//'xxxx';//外卖地址
    Integer is_integral;// 是否是积分兑换商品
    LocalDateTime creat_time;//订单创建时间
    LocalDateTime pay_time;//下单时间
    LocalDateTime charge_back;//退货时间
    BigDecimal total_price;//总支付金额
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "order_id")
    Set<OrderItem> order_list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrder_type() {
        return order_type;
    }

    public void setOrder_type(Integer order_type) {
        this.order_type = order_type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getIs_integral() {
        return is_integral;
    }

    public void setIs_integral(Integer is_integral) {
        this.is_integral = is_integral;
    }

    public LocalDateTime getCreat_time() {
        return creat_time;
    }

    public void setCreat_time(LocalDateTime creat_time) {
        this.creat_time = creat_time;
    }

    public LocalDateTime getPay_time() {
        return pay_time;
    }

    public void setPay_time(LocalDateTime pay_time) {
        this.pay_time = pay_time;
    }

    public LocalDateTime getCharge_back() {
        return charge_back;
    }

    public void setCharge_back(LocalDateTime charge_back) {
        this.charge_back = charge_back;
    }

    public BigDecimal getTotal_price() {
        return total_price;
    }

    public void setTotal_price(BigDecimal total_price) {
        this.total_price = total_price;
    }

    public Set<OrderItem> getOrder_list() {
        return order_list;
    }

    public void setOrder_list(Set<OrderItem> order_list) {
        this.order_list = order_list;
    }
}
