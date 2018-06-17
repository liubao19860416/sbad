package com.yhjr.basic.dao.entity;

import java.sql.Timestamp;

import com.yhjr.basic.dao.base.ToString;
import com.yhjr.basic.dao.base.UploadStatus;

/**
 * 基础实体
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月7日
 */
public abstract class BaseNullEntity extends ToString implements BaseEntity {
    private static final long serialVersionUID = 1L;

    @Override
    public void setCode(String code) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDelFlag(Boolean delFlag) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDescription(String description) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCreateBy(String createBy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setUpdateBy(String updateBy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCreateDate(Timestamp createDate) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setUpdateDate(Timestamp updateDate) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Boolean getDelFlag() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCreateBy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUpdateBy() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getCreateDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Timestamp getUpdateDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTransactionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTransactionId(String transactionId) {
        // TODO Auto-generated method stub

    }

    @Override
    public UploadStatus getUploadStatus() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUploadStatus(UploadStatus uploadStatus) {
        // TODO Auto-generated method stub

    }
}
