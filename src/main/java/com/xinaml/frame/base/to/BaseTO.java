package com.xinaml.frame.base.to;

import com.xinaml.frame.base.entity.DEL;
import com.xinaml.frame.base.entity.EDIT;
import com.xinaml.frame.base.entity.GET;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public abstract class BaseTO implements Serializable {

    public static final String DATETIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String TIME = "HH:mm:ss";

    @NotBlank(groups = {GET.class, DEL.class, EDIT.class}, message = "id不能为空！")
    public String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
