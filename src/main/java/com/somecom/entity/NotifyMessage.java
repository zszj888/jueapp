package com.somecom.entity;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "account_detail")
public class NotifyMessage extends BaseEntity {

}
