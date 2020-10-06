package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data

@Entity
@Table(name = "t_role")
public class Role extends BaseEntity {

    @Column(name = "role_name")
    private String name;

    @NotBlank(message = "图片文件名必传，例:c6e92c16-ae3c-49d1-9bfb-90aaf2ad4f5c.jpg")
    @Column(name = "img")
    private String img;

    @NotBlank(message = "文件uri路径，例:c6e92c16-ae3c-49d1-9bfb-90aaf2ad4f5c.jpg")
    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "role_desc")
    private String desc;

    @Column(name = "remrks")
    private String remrks;

    @Column(name = "age")
    private Integer age;

    @Column(name = "sex")
    private Integer sex;

    @Column(name = "skill")
    private String skill;

    @Column(name = "favor")
    private String isCollection;

    @Column(name = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Column(name = "open_id")
    private String openid;

    @Column(name = "video_url")
    private String video_url;

    @Column(name = "role_type")
    private String role_type;

    @Column(name = "birthda_day")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate birthda_day;

}
