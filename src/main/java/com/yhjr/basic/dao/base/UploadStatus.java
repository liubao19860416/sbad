package com.yhjr.basic.dao.base;

/**
 * 用户状态 需要自动上传的状态:0
 */
public enum UploadStatus {
    INIT("0", "数据初始导入状态"), 
    NORMAL("1", "已上传征信系统状态(定时任务自动上传)"), 
    MANUAL("2", "已上传征信系统状态(人工手动上传)"), 
    UNAUTHED("3","未授信用户(不同意上传)状态"), 
    REFUSED("4", "人工审核后(不同意上传)状态"), 
    APPROVED("5","人工审核后(同意上传未上传)状态"), 
    NONEED("6", "补报数据在开始时间之前,不需上报"), 
    NA("-1", "-未知"); // N/A 未知

    private String key;
    private String value;

    private UploadStatus(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static UploadStatus getByKey(int key) {
        UploadStatus[] os = UploadStatus.values();
        for (int i = 0; i < os.length; i++) {
            if (os[i].getKey().equals(key)) {
                return os[i];
            }
        }
        return NA;
    }

    @Override
    public String toString() {
        return getKey();
    }
}