package com.epam.lab.service;

public class SortCriteria {
    private String param;
    private OrderValue orderValue;

    public SortCriteria() {
    }

    public SortCriteria(String param, OrderValue orderValue) {
        this.param = param;
        this.orderValue = orderValue;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public OrderValue getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(OrderValue orderValue) {
        this.orderValue = orderValue;
    }
}
