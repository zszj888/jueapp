package com.somecom.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "account_detail")
public class NotifyMessage {
    @Id
    @GeneratedValue
    private Integer id;

}
