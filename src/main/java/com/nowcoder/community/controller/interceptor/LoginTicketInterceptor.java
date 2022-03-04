package com.nowcoder.community.controller.interceptor;

import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CookieUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor { // 实现接口

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 「请求一开始」就需要获取ticket，然后查找有无对应user
        // 有就暂存
        // 由于方法由接口定义，就不能改传入参数（用cookieValue注解）。cookie通过request传入（需要封装CookieUtil）

        // 从cookie（通过request传来）获得ticket
        String ticket = CookieUtil.getValue(request, "ticket");
        if (ticket != null) { // 说明已登录
            // 查询凭证
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            // 检查凭证是否有效（有凭证 & 已登录 & 未超时）
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())) {
                // 根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                // 查到之后暂存user（在本次请求中持有用户）
                // 考虑多线程并发：线程隔离，user存于ThreadLocal（hostHolder工具）
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    // 调用模版引擎之前，将user存入model
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser(); // 得到当前线程持有的user
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    // 清理hostHolder
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
