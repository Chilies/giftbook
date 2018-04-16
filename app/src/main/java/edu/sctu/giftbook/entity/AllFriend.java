package edu.sctu.giftbook.entity;

import java.io.Serializable;

/**
 * Created by zhengsenwen on 2018/4/16.
 */
public class AllFriend implements Serializable {
    public static final long serialVersionUID = 1L;

    private Integer id;
    private String nickName;
    private Integer fellowStatus;
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

    public Integer getFellowStatus() {
        return fellowStatus;
    }

    public void setFellowStatus(Integer fellowStatus) {
        this.fellowStatus = fellowStatus;
    }

    public String getAvatarSrc() {
        return avatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        this.avatarSrc = avatarSrc;
    }


}
