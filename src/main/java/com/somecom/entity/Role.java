package com.somecom.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.somecom.model.LocalDateConverter;
import com.somecom.model.LocalDateTimeConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
@Table(name = "t_role")
public class Role extends BaseEntity {

    @Column(name = "role_name")
    @ExcelProperty(value = "姓名",index = 1)
    private String name;

    //    @NotBlank(message = "图片文件名必传，例:c6e92c16-ae3c-49d1-9bfb-90aaf2ad4f5c.jpg")
    @Column(name = "img")
    @ExcelProperty(value = "头像",index = 2)
    private String img;

    //    @NotBlank(message = "文件uri路径，例:c6e92c16-ae3c-49d1-9bfb-90aaf2ad4f5c.jpg")
    @Column(name = "imgUrl")
    @ExcelIgnore
    private String imgUrl;

    @Column(name = "role_desc")
    @ExcelIgnore
    private String desc;

    @Column(name = "remrks")
    @ExcelIgnore
    private String remark;

    @Column(name = "age")
    @ExcelProperty(value = "年龄",index = 3)
    private Integer age;

    @Column(name = "sex")
    @ExcelProperty(value = "性别",index = 4)
    private String sex;
    @Column(name = "height")
    @ExcelProperty(value = "身高",index = 5)
    private Integer height;
    @ExcelProperty(value = "体重",index = 6)
    private Integer weight;

    @ExcelProperty(value = "才艺",index = 7)
    @Column(name = "skill")
    private String skill;
    @ExcelIgnore
    private String showInfo;
    @ExcelProperty(value = "演出费",index = 8)
    private BigDecimal fee;
    //是否已收藏
    @Transient
    @ExcelIgnore
    private Boolean collection;
    @Column(name = "apply_persons")
    @ExcelIgnore
    private String applyPersons;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "创建时间",converter = LocalDateTimeConverter.class)
    private LocalDateTime createTime;
    @ExcelIgnore
    @Column(name = "open_id")
    private String openid;
    @ExcelIgnore
    @Column(name = "video_url")
    private String video_url;
    @ExcelIgnore
    @Column(name = "role_type")
    private String type;
    @ExcelIgnore
    @Column(name = "create_by_id")
    private Integer createById;
    @ExcelIgnore
    @Column(name = "accepted_by_id")
    private Integer acceptedById;
    @ExcelProperty(value = "状态",index = 10)
    private String status;

    @Column(name = "birthda_day")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
//    @ExcelProperty(index = 8,value = "出生日期",converter = LocalDateConverter.class)
    @ExcelIgnore
    private LocalDate birthda_day;

    @ExcelProperty(value = "服务器范围",index = 9)
    private String position;

    public static Role of(Integer roleId) {
        Role role = new Role();
        role.setId(roleId);
        return role;
    }

    @PrePersist
    public void prePersist() {
        createTime = LocalDateTime.now();
    }
}
