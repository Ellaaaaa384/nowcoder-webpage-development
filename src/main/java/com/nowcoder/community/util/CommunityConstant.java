package com.nowcoder.community.util;

public interface CommunityConstant {

    // 定义激活状态
    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPEAT = 1;
    int ACTIVATION_FAILURE = 2;

    // 定义账号密码保存时间
    int DEFAULT_EXPIRED_SECONDS = 36000 * 12;
    int REMEMBER_EXPIRED_SECONDS = 36000 * 24 * 100;
}
