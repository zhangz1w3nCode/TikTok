package com.java.pojo;

import java.util.Date;

public class Users {
    private String id;

    private String mobile;

    private String nickname;

    private String imoocNum;

    private String face;

    private Integer sex;

    private Date birthday;

    private String country;

    private String province;

    private String city;

    private String district;

    private String description;

    private String bgImg;

    private Integer canImoocNumBeUpdated;

    private Date createdTime;

    private Date updatedTime;

    public Users(String id, String mobile, String nickname, String imoocNum, String face, Integer sex, Date birthday, String country, String province, String city, String district, String description, String bgImg, Integer canImoocNumBeUpdated, Date createdTime, Date updatedTime) {
        this.id = id;
        this.mobile = mobile;
        this.nickname = nickname;
        this.imoocNum = imoocNum;
        this.face = face;
        this.sex = sex;
        this.birthday = birthday;
        this.country = country;
        this.province = province;
        this.city = city;
        this.district = district;
        this.description = description;
        this.bgImg = bgImg;
        this.canImoocNumBeUpdated = canImoocNumBeUpdated;
        this.createdTime = createdTime;
        this.updatedTime = updatedTime;
    }

    public Users() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile == null ? null : mobile.trim();
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    public String getImoocNum() {
        return imoocNum;
    }

    public void setImoocNum(String imoocNum) {
        this.imoocNum = imoocNum == null ? null : imoocNum.trim();
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face == null ? null : face.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getBgImg() {
        return bgImg;
    }

    public void setBgImg(String bgImg) {
        this.bgImg = bgImg == null ? null : bgImg.trim();
    }

    public Integer getCanImoocNumBeUpdated() {
        return canImoocNumBeUpdated;
    }

    public void setCanImoocNumBeUpdated(Integer canImoocNumBeUpdated) {
        this.canImoocNumBeUpdated = canImoocNumBeUpdated;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }
}