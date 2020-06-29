package com.somecom.demo.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_ontegral")
public class Ontegral {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String good_name;//'分类1-1',//商品名称
    private Integer sale_num;//133,//已经卖出的数量
    private String icon_url;//'xxxx',//商品小图片
    private String good_img_url;//'xxxx',//商品大图品
    private BigDecimal price;//18,//原价格
    private BigDecimal promotion_price;//'17',//促销价格
    private Byte is_integral;//1,//是否是积分兑换商品
    private String specifications;//'大杯',//商品规格大杯,中杯,小杯
    @Column(name = "onte_desc")
    private String desc;//'商品描述',
    private Integer integral_total;//1000,//积分数量

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGood_name() {
        return good_name;
    }

    public void setGood_name(String good_name) {
        this.good_name = good_name;
    }

    public Integer getSale_num() {
        return sale_num;
    }

    public void setSale_num(Integer sale_num) {
        this.sale_num = sale_num;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getGood_img_url() {
        return good_img_url;
    }

    public void setGood_img_url(String good_img_url) {
        this.good_img_url = good_img_url;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPromotion_price() {
        return promotion_price;
    }

    public void setPromotion_price(BigDecimal promotion_price) {
        this.promotion_price = promotion_price;
    }

    public Byte getIs_integral() {
        return is_integral;
    }

    public void setIs_integral(Byte is_integral) {
        this.is_integral = is_integral;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getIntegral_total() {
        return integral_total;
    }

    public void setIntegral_total(Integer integral_total) {
        this.integral_total = integral_total;
    }
}