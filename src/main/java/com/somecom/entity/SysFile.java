package com.somecom.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.somecom.enums.SystemDataStatusEnum;
import com.somecom.utils.HttpServletUtil;
import com.somecom.utils.StatusUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.util.StringUtils;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Data
@Entity
@ToString(exclude = "tClasses")
@EqualsAndHashCode(exclude = "tClasses")
@Table(name = "sys_file")
@SQLDelete(sql = "update sys_file" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
public class SysFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String path;
    private String mime;
    private Long size;
    private String md5;
    private String sha1;
    private Byte status = SystemDataStatusEnum.OK.getCode();
    @Basic
    @CreatedDate
    @Column(name = "create_date")
    private Date createDate;
    private Boolean free;
    private String catalog;
    /**
     * 创建者
     */
    @CreatedBy
    @ManyToOne(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "create_by",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @JsonIgnore
    private SysUser createBy;

    /**
     * 获取文件绝对路径
     */
    public String getUrl() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        if (!StringUtils.isEmpty(path)) {
            StringBuffer url = request.getRequestURL();
            String baseUrl = url.delete(url.length() - request.getRequestURI().length(), url.length())
                    .append(request.getContextPath()).toString();
            return baseUrl + path;
        }
        return path;
    }
}
