/**
 * 实体基类
 *
 * @author lgq
 * @date 2018/4/15
 **/
package com.xinaml.frame.base.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体基类
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class BaseEntity implements Serializable {

    /**
     * id为 int 或者 long 类型时配置
     * @Id
     * @GeneratedValue(strategy = GenerationType.AUTO)
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", nullable = false, length = 36, updatable = false, insertable = false)
    private String id;

    @Column(nullable = false, columnDefinition = "DATETIME  COMMENT '创建时间'")
    private LocalDateTime createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss") //json 转换时日期处理
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

}
