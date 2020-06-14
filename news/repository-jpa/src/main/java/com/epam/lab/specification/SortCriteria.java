package com.epam.lab.specification;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SortCriteria that = (SortCriteria) o;
        return Objects.equals(param, that.param) &&
                orderValue == that.orderValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(param, orderValue);
    }
}
