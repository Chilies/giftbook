package edu.sctu.giftbook.entity;

import java.io.Serializable;

/**
 * Created by zhengsenwen on 2018/4/13.
 */
public class ContactFriend implements Serializable {
    public static final long serialVersionUID = 1L;

    private Integer id;
    private String nickName;
    private String phoneNumber;
    private String avatarSrc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatarSrc() {
        return avatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }
}
