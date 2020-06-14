package com.epam.lab.specification;

import static com.epam.lab.repository.DAOConstants.SPACE;

public class SortSpecificationImpl implements SortSpecification {
    private String parameter;
    private String order;

    public SortSpecificationImpl(String parameter, String order) {
        this.parameter = parameter;
        this.order = order;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public String getSql() {
        return SPACE + parameter + SPACE + order;
    }
}
