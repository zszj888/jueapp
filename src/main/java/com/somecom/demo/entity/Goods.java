package com.somecom.demo.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "goods")
public class Goods {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;
    String categories;
    Integer host_sale;
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "goods_id")
    Set<ChildGoods> good_list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public Integer getHost_sale() {
        return host_sale;
    }

    public void setHost_sale(Integer host_sale) {
        this.host_sale = host_sale;
    }

    public Set<ChildGoods> getGood_list() {
        return good_list;
    }

    public void setGood_list(Set<ChildGoods> good_list) {
        this.good_list = good_list;
    }
}
