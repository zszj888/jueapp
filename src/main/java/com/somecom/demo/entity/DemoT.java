package com.somecom.demo.entity;

import javax.persistence.*;

@Entity
@Table(name = "t_demo")
public class DemoT {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "demo_name")
    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DemoT{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
