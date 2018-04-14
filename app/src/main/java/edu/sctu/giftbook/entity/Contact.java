package edu.sctu.giftbook.entity;

import java.io.Serializable;

/**
 * Created by zhengsenwen on 2018/4/13.
 */

public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    private String contactId;
    private String name;
    private String phoneNumber;

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
