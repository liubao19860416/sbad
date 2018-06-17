package com.yhjr.basic.utils;

/**
 * 常量定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public abstract class Constants {
    
    /** ODS 返回码*/
    public static final String ODS_SUCCESS = "00"; //成功
    /** 发送短信验证码*/
    public static final String SEND_MESSAGE_SMS = "ODS0003";
    /** 发送语言短信验证码*/
    public static final String SEND_MESSAGE_VOICE = "ODS0004";
    /**注册码*/
    public static final String SEND_ORG_NO = "800001";
    /**消息*/
    public static final String SMS_MESSAGE_UPDATE = "[%s]更新Mysql状态异常:执行类[%s]!";
    public static final String SMS_MESSAGE_INSERT = "[%s]插入Oracle数据操作异常:执行类[%s]!";
    public static final String SMS_MESSAGE_CHECK = "[%s]检查Mysql数据数据存在未处理数据:异常数据类[%s]!";
    public static final String NOTIFY_TYPE_SMS = "sms";
    public static final String ERROR_FLAG_INSERTBATCH = "ERROR_FLAG_INSERTBATCH";
    
    /**批量阀值*/
    public static final int PAGE_INDEX = 0;
    public static final int BATCH_SIZE_MAX = 1000;
    public static final int MILLISECONDS = 500;
    
    public static final String SWITCHFLAG_P = "SWITCHFLAG_P";
    public static final String SWITCHFLAG_E = "SWITCHFLAG_E";
    public static final String TRUE = "true";
    public static final String FALSE = "false";
    
}
