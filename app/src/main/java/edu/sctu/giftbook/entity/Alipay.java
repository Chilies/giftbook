package edu.sctu.giftbook.entity;


import java.io.Serializable;

/**
 * Created by zhengsenwen on 2018/4/3.
 */

public class Alipay implements Serializable {
    private static final long serialVersionUID = 1L;

    private int alipayId;
    private String receiveCode;
    private int userId;

    public int getAlipayId() {
        return alipayId;
    }

    public void setAlipayId(int alipayId) {
        this.alipayId = alipayId;
    }

    public String getReceiveCode() {
        return receiveCode;
    }

    public void setReceiveCode(String receiveCode) {
        this.receiveCode = receiveCode;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
