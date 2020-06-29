package com.somecom.demo.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "child_goods")
public class ChildGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String good_name;
    String sale_name;
    String icon_url;
    String good_img_url;
    BigDecimal price;
    BigDecimal promotion_price;
    String specifications;
    @Column(name = "remark")
    String desc;
    Integer goods_id;

    public Integer getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(Integer goods_id) {
        this.goods_id = goods_id;
    }

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

    public String getSale_name() {
        return sale_name;
    }

    public void setSale_name(String sale_name) {
        this.sale_name = sale_name;
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

}
