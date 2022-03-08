package com.nowcoder.community.service;

import com.nowcoder.community.dao.AlphaDao;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;

@Service
// @Scope("prototype")
// 管理bean的作用域范围的注解：默认"singleton"，如果用"prototype"每次访问bean都会创建新实例
public class AlphaService {

    @Autowired // 实现依赖注入（把AlphaDao注入AlphaService）
    private AlphaDao alphaDao;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private TransactionTemplate transactionTemplate;

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

    // 事务管理1：声明式
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED) // Spring的事务管理，让它成为一个整体，任何地方报错都要回滚
    public Object save1() {
        // 新增用户
        User user = new User();
        user.setUsername("alphaUser");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
        user.setEmail("alphaUser@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);

        // 新增帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("Hello");
        post.setContent("I'm Alpha!");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        // 人为造错
        Integer.valueOf("abc");

        return "save1 ok";
    }

    // 事务管理2：编程式
    // 注入TransactionTemplate的bean
    public Object save2() {
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        return transactionTemplate.execute(new TransactionCallback<Object>() {
            // 实现接口方法（鼠标悬停在TransactionCallback上，点击Implement method）
            @Override
            public Object doInTransaction(TransactionStatus status) {
                // 新增用户
                User user = new User();
                user.setUsername("betaUser");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));
                user.setEmail("betaUser@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);

                // 新增帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("Hello");
                post.setContent("I'm Beta!");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                // 人为造错
                Integer.valueOf("abc");

                return "save2 ok";
            }
        }); // 传入回调接口
    }
}
