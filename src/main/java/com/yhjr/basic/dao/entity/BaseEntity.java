package com.yhjr.basic.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.yhjr.basic.dao.base.UploadStatus;

/**
 * 基础BASE实体
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月7日
 */
public interface BaseEntity extends Serializable{

    public void setCode(String code);

    public void setDelFlag(Boolean delFlag);

    public void setDescription(String description);

    public void setCreateBy(String createBy);

    public void setUpdateBy(String updateBy);

    public void setCreateDate(Timestamp createDate);

    public void setUpdateDate(Timestamp updateDate);

    public String getCode();

    public Boolean getDelFlag();

    public String getDescription();

    public String getCreateBy();

    public String getUpdateBy();

    public Timestamp getCreateDate();

    public Timestamp getUpdateDate();

    public String getTransactionId();

    public void setTransactionId(String transactionId);

    public UploadStatus getUploadStatus();

    public void setUploadStatus(UploadStatus uploadStatus);

}
