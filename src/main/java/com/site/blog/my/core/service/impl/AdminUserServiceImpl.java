package com.site.blog.my.core.service.impl;

import com.site.blog.my.core.dao.AdminUserMapper;
import com.site.blog.my.core.entity.AdminUser;
import com.site.blog.my.core.service.AdminUserService;
import com.site.blog.my.core.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Resource
    private AdminUserMapper adminUserMapper;

    @Override
    public AdminUser login(String userName, String password) {
        System.out.println("=== Service层登录调试 ===");
        System.out.println("Service - 接收到的用户名: " + userName);
        String passwordMd5 = MD5Util.MD5Encode(password, "UTF-8");
        System.out.println("Service - MD5加密后的密码: " + passwordMd5);
        System.out.println("Service - 开始调用Mapper查询数据库");
        AdminUser result = adminUserMapper.login(userName, passwordMd5);
        System.out.println("Service - Mapper查询结果: " + (result != null ? "找到用户" : "未找到用户"));
        if (result != null) {
            System.out.println("Service - 用户详情 - ID:" + result.getAdminUserId() + ", 昵称:" + result.getNickName());
        }
        System.out.println("=== Service层调试结束 ===");
        return result;
    }

    @Override
    public AdminUser getUserDetailById(Integer loginUserId) {
        return adminUserMapper.selectByPrimaryKey(loginUserId);
    }

    @Override
    public Boolean updatePassword(Integer loginUserId, String originalPassword, String newPassword) {
        System.out.println("=== 密码修改调试信息 ===");
        System.out.println("用户ID: " + loginUserId);
        System.out.println("原密码: " + originalPassword);
        System.out.println("新密码: " + newPassword);
        
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        System.out.println("查询到的用户: " + (adminUser != null ? adminUser.getLoginUserName() : "null"));
        
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            String originalPasswordMd5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPasswordMd5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            
            System.out.println("MD5加密后原密码: " + originalPasswordMd5);
            System.out.println("数据库中密码: " + adminUser.getLoginPassword());
            System.out.println("密码是否匹配: " + originalPasswordMd5.equals(adminUser.getLoginPassword()));
            
            //比较原密码是否正确
            if (originalPasswordMd5.equals(adminUser.getLoginPassword())) {
                //设置新密码并修改
                adminUser.setLoginPassword(newPasswordMd5);
                System.out.println("准备更新密码...");
                
                int updateResult = adminUserMapper.updateByPrimaryKeySelective(adminUser);
                System.out.println("数据库更新结果: " + updateResult);
                
                if (updateResult > 0) {
                    System.out.println("密码修改成功!");
                    return true;
                }
            } else {
                System.out.println("原密码不匹配!");
            }
        } else {
            System.out.println("用户不存在!");
        }
        System.out.println("密码修改失败!");
        return false;
    }

    @Override
    public Boolean updateName(Integer loginUserId, String loginUserName, String nickName) {
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(loginUserId);
        //当前用户非空才可以进行更改
        if (adminUser != null) {
            //修改信息
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
            if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0) {
                //修改成功则返回true
                return true;
            }
        }
        return false;
    }
}
