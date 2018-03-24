package edu.sctu.giftbook.entity;

import java.io.Serializable;

/**
 * Created by zhengsenwen on 2018/3/23.
 */

public class WishCard implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer wishCardId;
    private String createTime;
    private String description;
    private String price;
    private String type;
    private Integer giverId;
    private Integer userId;
    private Integer avatarId;
    private Integer replyId;
    private Integer deadline;

    public Integer getWishCardId() {
        return wishCardId;
    }

    public void setWishCardId(Integer wishCardId) {
        this.wishCardId = wishCardId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getGiverId() {
        return giverId;
    }

    public void setGiverId(Integer giverId) {
        this.giverId = giverId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getDeadline() {
        return deadline;
    }

    public void setDeadline(Integer deadline) {
        this.deadline = deadline;
    }
}