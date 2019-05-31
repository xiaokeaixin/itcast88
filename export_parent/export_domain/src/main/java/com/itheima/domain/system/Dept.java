package com.itheima.domain.system;

import java.io.Serializable;

/**
 * 部门的实体类
 */
public class Dept implements Serializable {
    private String id;
    private String deptName;
    private Integer state;
    private String companyId;
    private String companyName;
    //表现出来，每个部门都有一个父部门，所以是一个一对一（多对一）的特点  private String 	parent_id;
    private Dept parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Dept getParent() {
        return parent;
    }

    public void setParent(Dept parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id='" + id + '\'' +
                ", deptName='" + deptName + '\'' +
                ", state=" + state +
                ", companyId='" + companyId + '\'' +
                ", companyName='" + companyName + '\'' +
                ", parent=" + parent +
                '}';
    }
}
