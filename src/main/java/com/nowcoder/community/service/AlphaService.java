package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
// @Scope("prototype")
// 管理bean的作用域范围的注解：默认"singleton"，如果用"prototype"每次访问bean都会创建新实例
public class AlphaService {

    @Autowired // 实现依赖注入（把AlphaDao注入AlphaService）
    private AlphaDao alphaDao;

    public AlphaService(){
        System.out.println("实例化AlphaService");
    }

    @PostConstruct // 初始化方法在构造之后调用
    public void init() {
        System.out.println("初始化AlphaService");
    }

    @PreDestroy //销毁对象前调用
    public void destroy(){
        System.out.println("销毁AlphaService");
    }

    // 模拟查询业务
    public String find(){
        return alphaDao.select();
    }

}
