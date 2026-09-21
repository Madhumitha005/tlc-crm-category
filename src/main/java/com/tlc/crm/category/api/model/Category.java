package com.tlc.crm.category.api.model;

public final class Category {

    private Long id;
    private long orgId;
    private String name;
    private long createdTime;
    private long modifiedTime;

    public Long id() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public long getOrgId() {
        return orgId;
    }

    public void setOrgId(final long orgId) {
        this.orgId = orgId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(final long createdTime) {
        this.createdTime = createdTime;
    }

    public long getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(final long modifiedTime) {
        this.modifiedTime = modifiedTime;
    }
}