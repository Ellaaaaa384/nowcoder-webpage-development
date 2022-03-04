package com.nowcoder.community.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    // 从cookie中取值
    public static String getValue (HttpServletRequest request, String name) {
        if (request == null || name == null) {
            throw new IllegalArgumentException("参数为空"); // 抛出异常
        }

        Cookie[] cookies = request.getCookies(); // 得到所有cookie对象（数组）
        if (cookies != null) {
            for (Cookie cookie : cookies) { // 遍历
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
