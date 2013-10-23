package com.dc.around.model;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-10-15
 * Time: 下午9:13
 * To change this template use File | Settings | File Templates.
 */
public class Category {
    private String name;
    private String code;
    private boolean ischildren;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isIschildren() {
        return ischildren;
    }

    public void setIschildren(boolean ischildren) {
        this.ischildren = ischildren;
    }
}
