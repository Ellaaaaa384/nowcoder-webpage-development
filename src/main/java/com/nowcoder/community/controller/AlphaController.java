package com.nowcoder.community.controller;
// import部分是靠后面的@符自动生成的
import com.nowcoder.community.service.AlphaService;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller

@RequestMapping("/alpha") // 类的访问名
public class AlphaController {

    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody // 使直接return的部分直接出现在页面上
    public String sayHello(){
        return "Hello Spring Boot.";
    }

    // 模拟处理查询请求
    @RequestMapping("/data")
    @ResponseBody // 声明路径
    public String getData() {
        return alphaService.find();
    }

    // 获得请求对象和响应对象
    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // 获取请求数据
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        // 返回响应数据
        response.setContentType("text/html;charset=utf-8");
        try(
                PrintWriter writer = response.getWriter();
                ) {
            writer.write("<h1>返回响应数据成功</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 接收请求数据（request）+返回响应数据（response）
    // GET请求
    // 查询所有学生 路径: /students?current=1&limit20
    @RequestMapping(path = "/students", method = RequestMethod.GET) // 必须是GET请求才能访问
    @ResponseBody
    public String getStudents( // 通过RequestParam注解，让request中名为current的参数赋值给int后面的current参数
            @RequestParam(name = "current", required = false, defaultValue = "1") int current,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit
    ) {
        System.out.println(current);
        System.out.println(limit);
        return "some students";
    }

    // 根据学生ID查询某个学生 路径: /student/123
    @RequestMapping(path = "/student/{id}", method = RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id) {
        System.out.println(id);
        return "a student";
    }

    // 浏览器向服务器提交数据
    // POST请求
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    @ResponseBody
    public String saveStudent(String name, int age) { // 参数和表单一致就能把值从网页传进来
        System.out.println(name);
        System.out.println(age);
        return "Success";
    }

    // 响应html数据
    // 服务器查询到"某老师"后返回给浏览器
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    // 不加@ResponseBody默认返回html
    public ModelAndView getTeacher() { // ModelAndView就是传给DispatcherServlet（前端控制器）的数据类型
        ModelAndView mav= new ModelAndView(); // 实例化
        mav.addObject("name", "Zhang San");
        mav.addObject("age", "30");
        mav.setViewName("/demo/view");// 模版的路径和名字 /templates/demo/view.html
        return mav;
    }

    // 查询学校
    // 但model那里报错
//    @RequestMapping(path = "/school", method = RequestMethod.GET)
//    public String getSchool(Model model) {
//        model.addAttribute("name", "UESTC");
//        model.addAttribute("age", "65");
//        return "/demo/view";
//    }

    // 异步请求中响应JSON数据（当前网页不刷新，但已经访问过数据库并返回某些东西了）
    // Java对象 -> JSON字符串 -> JS对象
    @RequestMapping(path = "/emp", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "Zhang San");
        emp.put("age", "40");
        emp.put("salary", "8000");
        return emp;
    }

    // 查询所有员工
    @RequestMapping(path = "/emps", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> emp = new HashMap<>();
        emp.put("name", "Zhang San");
        emp.put("age", "40");
        emp.put("salary", "8000");
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "Li Si");
        emp.put("age", "20");
        emp.put("salary", "4000");
        list.add(emp);

        emp = new HashMap<>();
        emp.put("name", "Wang Wu");
        emp.put("age", "10");
        emp.put("salary", "2000");
        list.add(emp);

        return list;
    }

    // cookie相关示例
    // 产生cookie
    @RequestMapping(path = "/cookie/set", method = RequestMethod.GET)
    // 响应，返回字符串
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        // 创建cookie
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 设置生效范围
        cookie.setPath("/community/alpha");
        // 设置生存时间（默认关闭浏览器就消失），单位是s
        cookie.setMaxAge(60 * 10);
        // 放到html头
        response.addCookie(cookie);

        return "set cookie";
    }

    @RequestMapping(path = "/cookie/get", method = RequestMethod.GET)
    // 获取cookie
    @ResponseBody
    public String getCookie(@CookieValue("code") String code) {
        System.out.println(code);
        return "get cookie";
    }

    // session示例
    // 第一次请求+创建session(自动创建并注入)
    @RequestMapping(path = "/session/set", method = RequestMethod.GET)
    @ResponseBody
    public String setSession(HttpSession session) {
        session.setAttribute("id", "1");
        session.setAttribute("name", "Test");
        return "set session";
    }

    // 再次访问的时候传回session id
    @RequestMapping(path = "/session/get", method = RequestMethod.GET)
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println(session.getAttribute("id"));
        System.out.println(session.getAttribute("name"));
        return "get session";
    }

    // AJAX示例
    @RequestMapping(path = "/ajax", method = RequestMethod.POST)
    @ResponseBody // 返回字符串
    public String testAjax(String name, int age) {
        System.out.println(name);
        System.out.println(age);
        return CommunityUtil.getJSONString(0, "操作成功");
    }

}
