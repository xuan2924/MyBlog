# My-Blog 项目入门指南

## 📋 项目概述

这是一个基于 Spring Boot + MyBatis + Thymeleaf 构建的个人博客系统，适合作为学习项目或个人博客使用。

### 🎯 项目特点
- **技术栈经典**：Spring Boot 2.7.5 + MyBatis + Thymeleaf
- **功能完整**：包含博客管理的所有核心功能
- **界面美观**：内置三套不同风格的博客主题
- **代码规范**：遵循 MVC 架构，代码结构清晰

## 🛠️ 技术栈详解

### 后端技术
- **Spring Boot 2.7.5**：核心框架，简化 Spring 应用开发
- **MyBatis 2.2.2**：ORM 框架，操作数据库
- **MySQL**：关系型数据库，存储博客数据
- **Thymeleaf**：模板引擎，渲染前端页面
- **Hutool**：Java 工具库，用于验证码等功能
- **CommonMark**：Markdown 解析器

### 前端技术
- **HTML5 + CSS3 + JavaScript**：前端基础三件套
- **Bootstrap**：UI 框架（部分页面使用）
- **jQuery**：JavaScript 库
- **Layui**：后台管理界面框架

## 📁 项目结构分析

```
My-Blog/
├── src/main/java/com/site/blog/my/core/
│   ├── MyBlogApplication.java          # 启动类
│   ├── config/                         # 配置类
│   ├── controller/                     # 控制器层
│   │   ├── admin/                     # 后台管理控制器
│   │   ├── blog/                      # 博客前台控制器
│   │   └── common/                    # 公共控制器
│   ├── dao/                           # 数据访问层（Mapper接口）
│   ├── entity/                        # 实体类
│   ├── interceptor/                    # 拦截器
│   ├── service/                       # 业务逻辑层
│   │   └── impl/                      # 业务实现类
│   └── util/                          # 工具类
├── src/main/resources/
│   ├── application.properties          # 配置文件
│   ├── mapper/                        # MyBatis XML映射文件
│   ├── static/                        # 静态资源
│   │   ├── admin/                     # 后台静态资源
│   │   └── blog/                      # 前台静态资源
│   └── templates/                     # Thymeleaf模板
│       ├── admin/                     # 后台页面模板
│       └── blog/                      # 前台页面模板
└── static-files/
    └── my_blog_db.sql                 # 数据库脚本
```

## 🗄️ 数据库设计

### 核心数据表

#### 1. tb_admin_user - 管理员表
```sql
- admin_user_id: 管理员ID（主键）
- login_user_name: 登录用户名
- login_password: 登录密码
- nick_name: 显示昵称
- locked: 是否锁定
```

#### 2. tb_blog - 博客文章表
```sql
- blog_id: 文章ID（主键）
- blog_title: 文章标题
- blog_sub_url: 文章自定义URL
- blog_cover_image: 封面图片
- blog_category_id: 分类ID
- blog_tags: 标签（逗号分隔）
- blog_status: 发布状态（0-发布，1-草稿）
- blog_views: 浏览次数
- enable_comment: 是否允许评论
- blog_content: 文章内容（Markdown格式）
```

#### 3. tb_blog_category - 博客分类表
```sql
- category_id: 分类ID（主键）
- category_name: 分类名称
- category_icon: 分类图标
- category_rank: 排序值
```

#### 4. tb_blog_tag - 博客标签表
```sql
- tag_id: 标签ID（主键）
- tag_name: 标签名称
```

#### 5. tb_blog_comment - 博客评论表
```sql
- comment_id: 评论ID（主键）
- blog_id: 关联文章ID
- commentator_name: 评论者昵称
- commentator_email: 评论者邮箱
- comment_content: 评论内容
- comment_create_time: 评论时间
```

## 🔧 核心功能模块

### 1. 用户认证与授权
- **登录功能**：管理员登录后台
- **拦截器**：`LoginInterceptor` 实现登录验证
- **Session管理**：使用 Spring Session 管理用户会话

### 2. 文章管理
- **文章CRUD**：创建、编辑、删除文章
- **Markdown支持**：使用 CommonMark 解析 Markdown
- **文章分类**：支持分类管理
- **文章标签**：支持标签系统
- **草稿功能**：支持保存草稿

### 3. 评论系统
- **评论发布**：游客可发表评论
- **评论管理**：管理员可审核、删除评论
- **评论回复**：支持评论回复功能

### 4. 友情链接
- **链接管理**：增删改查友情链接
- **链接分类**：支持链接分类显示

### 5. 系统配置
- **网站信息**：网站标题、描述等基础信息
- **主题切换**：支持三套主题模板

## 🚀 快速开始

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 5.7+
- IDE（推荐 IntelliJ IDEA）

### 启动步骤

1. **导入数据库**
   ```sql
   -- 创建数据库
   CREATE DATABASE my_blog_db CHARACTER SET utf8;
   
   -- 导入数据表
   -- 执行 static-files/my_blog_db.sql 文件
   ```

2. **修改配置**
   编辑 `src/main/resources/application.properties`：
   ```properties
   # 数据库配置（根据你的环境修改）
   spring.datasource.url=jdbc:mysql://localhost:3306/my_blog_db?useUnicode=true&characterEncoding=utf8&autoReconnect=true&useSSL=false&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=123456
   
   # 服务端口
   server.port=28083
   ```

3. **运行项目**
   - 方式1：IDE 中直接运行 `MyBlogApplication.java`
   - 方式2：命令行 `mvn spring-boot:run`

4. **访问系统**
   - 前台博客：http://localhost:28083
   - 后台管理：http://localhost:28083/admin
   - 默认账号：admin / 123456

## 🏗️ MVC 架构详解

### Controller 层
负责接收 HTTP 请求，调用 Service 处理业务逻辑，返回视图或数据。

**示例：BlogController**
```java
@Controller
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;
    
    @GetMapping("/{page}")
    public String page(@PathVariable("page") String page) {
        return "blog/" + page;
    }
}
```

### Service 层
负责业务逻辑处理，调用 DAO 层进行数据操作。

**示例：BlogService**
```java
@Service
public class BlogService {
    @Autowired
    private BlogMapper blogMapper;
    
    public PageResult getBlogPage(PageQueryUtil pageUtil) {
        // 分页查询逻辑
    }
}
```

### DAO 层（Mapper）
负责数据库操作，使用 MyBatis 进行数据映射。

**示例：BlogMapper**
```java
@Mapper
public interface BlogMapper {
    int getTotalCount();
    List<Blog> findBlogList(Map<String, Object> map);
}
```

## 🎨 前端模板系统

### Thymeleaf 模板语法
- **变量表达式**：`${variable}`
- **URL表达式**：`@{/path}`
- **条件判断**：`th:if`
- **循环遍历**：`th:each`

### 三套主题模板
1. **模板一**：简洁风格
2. **模板二**：卡片风格  
3. **模板三**：杂志风格

主题切换通过修改配置实现，模板文件位于 `templates/blog/` 目录下。

## 📚 学习路径建议

### 第一阶段：环境搭建（1-2天）
1. 配置开发环境
2. 导入项目并成功运行
3. 熟悉项目结构和配置文件

### 第二阶段：理解架构（2-3天）
1. 学习 Spring Boot 基础概念
2. 理解 MVC 架构模式
3. 掌握 MyBatis 基本用法

### 第三阶段：核心功能（3-4天）
1. 分析用户登录流程
2. 理解文章管理功能
3. 学习评论系统实现

### 第四阶段：前端技术（2-3天）
1. 学习 Thymeleaf 模板语法
2. 理解前后端数据交互
3. 掌握 AJAX 异步请求

### 第五阶段：深入优化（3-5天）
1. 学习数据库优化
2. 理解缓存机制
3. 掌握项目部署

## 🔍 关键代码分析

### 1. 启动类分析
```java
@MapperScan("com.site.blog.my.core.dao")  // 扫描Mapper接口
@SpringBootApplication
public class MyBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyBlogApplication.class, args);
    }
}
```

### 2. 登录拦截器
```java
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 登录验证逻辑
        if (session.getAttribute("loginUser") == null) {
            response.sendRedirect("/admin/login");
            return false;
        }
        return true;
    }
}
```

### 3. 分页查询实现
```java
public PageResult getBlogPage(PageQueryUtil pageUtil) {
    List<Blog> blogList = blogMapper.findBlogList(pageUtil);
    int total = blogMapper.getTotalCount(pageUtil);
    return new PageResult(blogList, total, pageUtil.getLimit(), pageUtil.getPage());
}
```

## 🛡️ 安全考虑

### 1. SQL注入防护
- 使用 MyBatis 参数化查询
- 避免字符串拼接 SQL

### 2. XSS防护
- 对用户输入进行转义
- 使用 Thymeleaf 自动转义

### 3. CSRF防护
- 使用 Spring Security CSRF 保护
- 验证请求来源

## 🚀 扩展建议

### 功能扩展
1. **用户系统**：增加用户注册登录功能
2. **文件上传**：支持图片上传到云存储
3. **搜索功能**：集成 Elasticsearch 实现全文搜索
4. **缓存优化**：集成 Redis 缓存热点数据
5. **邮件通知**：评论邮件通知功能

### 技术升级
1. **Spring Boot 3.x**：升级到最新版本
2. **前后端分离**：改造成 RESTful API + Vue/React
3. **容器化部署**：使用 Docker 部署
4. **微服务架构**：拆分为多个微服务

## 📖 推荐学习资源

### 官方文档
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [MyBatis 官方文档](https://mybatis.org/mybatis-3/)
- [Thymeleaf 官方文档](https://www.thymeleaf.org/)

### 在线教程
- Spring Boot 入门教程
- MyBatis 实战教程
- Thymeleaf 模板引擎教程

## ❓ 常见问题

### Q1: 项目启动失败怎么办？
A: 检查数据库连接配置，确保 MySQL 服务正常运行，数据库已创建。

### Q2: 前端页面样式丢失？
A: 检查静态资源路径，确保 `static` 目录下的文件完整。

### Q3: 如何修改默认端口？
A: 在 `application.properties` 中修改 `server.port` 配置。

### Q4: 如何添加新的功能模块？
A: 按照 MVC 架构，分别添加 Controller、Service、DAO 和对应的模板文件。

---

## 🎉 总结

这个 My-Blog 项目是一个非常适合初学者的 Spring Boot 实战项目，涵盖了 Web 开发的核心知识点：

- **后端框架**：Spring Boot 自动配置、依赖注入
- **数据持久化**：MyBatis ORM 框架
- **模板引擎**：Thymeleaf 服务端渲染
- **前端技术**：HTML、CSS、JavaScript、AJAX
- **数据库设计**：MySQL 关系型数据库
- **项目架构**：MVC 设计模式

通过学习这个项目，你将掌握企业级 Java Web 开发的核心技术栈，为后续深入学习和工作打下坚实基础。

建议按照学习路径逐步深入，先让项目跑起来，再理解每个模块的实现原理，最后尝试自己扩展功能。祝你学习愉快！🚀
