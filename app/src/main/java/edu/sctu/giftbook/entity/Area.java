package edu.sctu.giftbook.entity;


import java.io.Serializable;

/**
 * Created by zhengsenwen on 2018/4/8.
 */

public class Area  implements Serializable {
    public static final long serialVersionUID = 1L;

    private Integer id;
    private String province;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
