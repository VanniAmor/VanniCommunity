package com.vanni.community.util;

public interface CommunityConstant {
    /**
     * 激活成功
     */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     */
    int ACTIVATION_REPEAT = 1;

    /**
     * 激活失败
     */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态的登录凭证有效时间(秒)
     */
    int DEFAULT_EXPIRE_SECOND = 3600 * 3;

    /**
     * “记住我”状态下的登录凭证有效时间(秒)
     */
    int REMEMBER_EXPIRE_SECOND = 3600 * 24 * 100;

    /**
     * 实体类型：贴子
     */
    int ENTITY_TYPE_POST = 1;

    /**
     * 实体类型：评论
     */
    int ENTITY_TYPE_COMMENT = 2;
}
