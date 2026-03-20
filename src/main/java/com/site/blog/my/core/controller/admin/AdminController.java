package com.site.blog.my.core.controller.admin;

import cn.hutool.captcha.ShearCaptcha;
import com.site.blog.my.core.entity.AdminUser;
import com.site.blog.my.core.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author 13
 * @qq交流群 796794009
 * @email 2449207463@qq.com
 * @link http://13blog.site
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Resource
    private AdminUserService adminUserService;
    @Resource
    private BlogService blogService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private LinkService linkService;
    @Resource
    private TagService tagService;
    @Resource
    private CommentService commentService;


    @GetMapping({"/login"})
    public String login() {
        return "admin/login";
    }

    @GetMapping({"", "/", "/index", "/index.html"})
    public String index(HttpServletRequest request) {
        request.setAttribute("path", "index");
        
        // 调试日志
        Integer categoryCount = categoryService.getTotalCategories();
        Integer blogCount = blogService.getTotalBlogs();
        Integer linkCount = linkService.getTotalLinks();
        Integer tagCount = tagService.getTotalTags();
        Integer commentCount = commentService.getTotalComments();

        System.out.println("=== 后台首页统计数据调试 ===");
        System.out.println("分类数量: " + categoryCount);
        System.out.println("文章数量: " + blogCount);
        System.out.println("链接数量: " + linkCount);
        System.out.println("标签数量: " + tagCount);
        System.out.println("评论数量: " + commentCount);
        System.out.println("===========================");
        request.setAttribute("categoryCount", categoryCount);
        request.setAttribute("blogCount", blogCount);
        request.setAttribute("linkCount", linkCount);
        request.setAttribute("tagCount", tagCount);
        request.setAttribute("commentCount", commentCount);
        return "admin/index";
    }

    @PostMapping(value = "/login")
    /*
    @PostMapping - 处理POST请求，对应login.html中的form提交
    @RequestParam - 从请求参数中获取值
    @SessionAttribute - 从session中获取值
     */
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session) {
        System.out.println("=== 登录请求调试信息 ===");
        System.out.println("用户名: " + userName);
        System.out.println("密码是否为空: " + (password == null || password.isEmpty()));
        System.out.println("验证码: " + verifyCode);
        
        if (!StringUtils.hasText(verifyCode)) {
            System.out.println("调试: 验证码为空");
            session.setAttribute("errorMsg", "验证码不能为空");
            return "admin/login";
        }
        if (!StringUtils.hasText(userName) || !StringUtils.hasText(password)) {
            System.out.println("调试: 用户名或密码为空");
            session.setAttribute("errorMsg", "用户名或密码不能为空");
            return "admin/login";
        }
        ShearCaptcha shearCaptcha = (ShearCaptcha) session.getAttribute("verifyCode");
        System.out.println("调试: Session中的验证码对象: " + (shearCaptcha != null ? "存在" : "不存在"));

        if (shearCaptcha == null || !shearCaptcha.verify(verifyCode)) {
            System.out.println("调试: 验证码验证失败");
            session.setAttribute("errorMsg", "验证码错误");
            return "admin/login";
        }

        System.out.println("调试: 开始调用Service层登录验证");
        AdminUser adminUser = adminUserService.login(userName, password);
        System.out.println("调试: Service返回结果: " + (adminUser != null ? "登录成功" : "登录失败"));

        if (adminUser != null) {
            System.out.println("调试: 用户信息 - ID:" + adminUser.getAdminUserId() + ", 昵称:" + adminUser.getNickName());
            session.setAttribute("loginUser", adminUser.getNickName());
            session.setAttribute("loginUserId", adminUser.getAdminUserId());
            //session过期时间设置为7200秒 即两小时
            //session.setMaxInactiveInterval(60 * 60 * 2);
            System.out.println("调试: 登录成功，跳转到后台首页");
            return "redirect:/admin/index";
        } else {
            System.out.println("调试: 登录失败，返回登录页");
            session.setAttribute("errorMsg", "登陆失败");
            return "admin/login";
        }
    }

    @GetMapping("/profile")
    public String profile(HttpServletRequest request) {
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        AdminUser adminUser = adminUserService.getUserDetailById(loginUserId);
        if (adminUser == null) {
            return "admin/login";
        }
        request.setAttribute("path", "profile");
        request.setAttribute("loginUserName", adminUser.getLoginUserName());
        request.setAttribute("nickName", adminUser.getNickName());
        return "admin/profile";
    }

    @PostMapping("/profile/password")
    @ResponseBody
    public String passwordUpdate(HttpServletRequest request, @RequestParam("originalPassword") String originalPassword,
                                 @RequestParam("newPassword") String newPassword) {
        if (!StringUtils.hasText(originalPassword) || !StringUtils.hasText(newPassword)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updatePassword(loginUserId, originalPassword, newPassword)) {
            //修改成功后清空session中的数据，前端控制跳转至登录页
            request.getSession().removeAttribute("loginUserId");
            request.getSession().removeAttribute("loginUser");
            request.getSession().removeAttribute("errorMsg");
            return "success";
        } else {
            return "修改失败";
        }
    }

    @PostMapping("/profile/name")
    @ResponseBody
    public String nameUpdate(HttpServletRequest request, @RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName") String nickName) {
        if (!StringUtils.hasText(loginUserName) || !StringUtils.hasText(nickName)) {
            return "参数不能为空";
        }
        Integer loginUserId = (int) request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(loginUserId, loginUserName, nickName)) {
            return "success";
        } else {
            return "修改失败";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        request.getSession().removeAttribute("loginUserId");
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("errorMsg");
        return "admin/login";
    }
}
