package com.nowcoder.community;

import com.nowcoder.community.dao.AlphaDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import com.nowcoder.community.service.AlphaService;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

// @RunWith(SpringRunner.class) 始终报错 无法解决
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class) // 以正式文件中的CommunityApplication为配置类
class CommunityApplicationTests implements ApplicationContextAware {
	// ApplicationContextAware是一个接口，也是bin，一直接到Spring容器的顶层接口，ApplicationContext是容器，也是子接口
	private ApplicationContext applicationContext; // 用来暂存容器的变量

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext; // 暂存容器
	}

	@Test
	public void testApplicationContext(){
		System.out.println(applicationContext);

		AlphaDao alphaDao = applicationContext.getBean(AlphaDao.class); // AlphaDao的实现类
		System.out.println(alphaDao.select());

		alphaDao = applicationContext.getBean("alphaHibernate", AlphaDao.class); // 指定bean的名字，强行调用AlphaDaoHibernateImpl
		System.out.println(alphaDao.select());
	}

	@Test
	public void testBeanManagement(){
		AlphaService alphaService = applicationContext.getBean(AlphaService.class); // 通过容器获取service
		System.out.println(alphaService);
		// 被Spring容器管理的bean只被实例化和销毁一次，默认是单例的
		// 即使再复制一次上面的代码，日志中的"实例化AlphaService"也只会打印一次
		// 强行每次调用都实例一次：见AlphaService的@Scope代码注释
		alphaService = applicationContext.getBean(AlphaService.class);
		System.out.println(alphaService);
	}

	@Test
	public void testBeanConfig(){
		SimpleDateFormat simpleDateFormat = applicationContext.getBean(SimpleDateFormat.class);
		System.out.println(simpleDateFormat.format(new Date()));
	}

	@Autowired // 加在程序变量AlphaDao之前，提示Spring容器将AlphaDao注入这个bean/属性
	// 传说中的依赖注入就实现了
	@Qualifier("alphaHibernate") // 希望注入曾经重命名的那个"alphaHibernate"
	private AlphaDao alphaDao;

	@Autowired
	private AlphaService alphaService;

	@Autowired
	private  SimpleDateFormat simpleDateFormat;

	@Test
	public void testDI(){ //希望能通过alohaDao这个程序变量取到bean
		System.out.println(alphaDao);
		System.out.println(alphaService);
		System.out.println(simpleDateFormat);
	}
}
