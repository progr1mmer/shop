package com.x.shop.common;

import com.x.shop.entity.Area;


/**
 * 收货地址
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
public class Receiver {

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 地区名称
     */
    private String areaName;

    /**
     * 地址
     */
    private String address;

    /**
     * 邮编
     */
    private String zipCode;

    /**
     * 电话
     */
    private String phone;

    /**
     * 地区
     */
    private Area area;

    /**
     * 获取收货人
     *
     * @return 收货人
     */
    public String getConsignee() {
        return consignee;
    }

    /**
     * 设置收货人
     *
     * @param consignee 收货人
     */
    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    /**
     * 获取地区名称
     *
     * @return 地区名称
     */
    public String getAreaName() {
        return areaName;
    }

    /**
     * 设置地区名称
     *
     * @param areaName 地区名称
     */
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    /**
     * 获取地址
     *
     * @return 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取邮编
     *
     * @return 邮编
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * 设置邮编
     *
     * @param zipCode 邮编
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * 获取电话
     *
     * @return 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取地区
     *
     * @return 地区
     */
    public Area getArea() {
        return area;
    }

    /**
     * 设置地区
     *
     * @param area 地区
     */
    public void setArea(Area area) {
        this.area = area;
    }

}