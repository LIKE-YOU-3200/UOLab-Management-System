# UOLab联合开放实验室管理系统 - 项目说明文档

## 📋 项目介绍

**UOLab联合开放实验室管理系统** 是一个基于Java Swing和MySQL开发的桌面应用程序，旨在为高校实验室提供统一的信息管理平台。系统涵盖了用户管理、成员管理、公开课管理、参赛管理和创新学分管理等核心功能，帮助实验室实现信息化、规范化管理。

### 项目背景
随着高校实验室管理的日益复杂化，传统的手工管理方式已无法满足现代实验室高效、规范的管理需求。本项目针对这一痛点，独立设计开发了一套功能全面、操作简便的实验室综合管理平台。

### 主要功能
- **用户管理**：用户账号的增删改查、启用/禁用、密码重置
- **成员管理**：实验室成员信息管理、多条件查询、多维度统计
- **公开课管理**：公开课发布、查询、报名管理
- **参赛管理**：竞赛记录录入、获奖情况统计
- **创新学分管理**：创新学分录入、查询、统计

---

## 🛠️ 技术栈

| 层次 | 技术 | 说明 |
|------|------|------|
| 开发语言 | Java 8 | JDK 1.8 |
| 界面框架 | Swing | 图形用户界面 |
| 数据库 | MySQL 8.0 | 关系型数据库 |
| 版本控制 | Git | 代码管理 |
| IDE | IntelliJ IDEA | 开发工具 |
| 项目管理 | Maven | 依赖管理（可选） |

---

## 📦 环境配置与运行步骤

### 系统要求
- **操作系统**：Windows 10/11
- **JDK版本**：Java 8 或更高版本
- **数据库**：MySQL 8.0
- **内存**：建议4GB以上
- **硬盘空间**：100MB以上

### 第一步：克隆项目
```bash
git clone https://github.com/LIKE-YOU-3200/UOLab-Management-System.git
```

### 第二步：导入数据库
1. 打开MySQL命令行或MySQL Workbench
2. 创建数据库：
```sql
CREATE DATABASE uolab_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
3. 使用数据库：
```sql
USE uolab_system;
```

4. 执行以下SQL脚本创建数据表（项目根目录下的`sql/uolab_system.sql`）：

```sql
-- 创建院系表
CREATE TABLE college (
    college_id INT PRIMARY KEY AUTO_INCREMENT,
    college_name VARCHAR(100) NOT NULL COMMENT '院系名称',
    college_code VARCHAR(20) UNIQUE COMMENT '院系代码',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建专业表
CREATE TABLE major (
    major_id INT PRIMARY KEY AUTO_INCREMENT,
    major_name VARCHAR(100) NOT NULL COMMENT '专业名称',
    college_id INT NOT NULL COMMENT '所属院系ID',
    remark TEXT COMMENT '备注',
    FOREIGN KEY (college_id) REFERENCES college(college_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建用户表
CREATE TABLE user (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    role_id INT NOT NULL COMMENT '角色ID：1-超级管理员，2-管理员',
    status VARCHAR(20) DEFAULT '启用' COMMENT '用户状态：启用/禁用',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    college_id INT COMMENT '院系ID',
    major_id INT COMMENT '专业ID',
    grade VARCHAR(20) COMMENT '年级',
    class_name VARCHAR(50) COMMENT '班级',
    student_position VARCHAR(50) COMMENT '校内职务',
    phone VARCHAR(20) COMMENT '手机号',
    qq VARCHAR(20) COMMENT 'QQ号',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (college_id) REFERENCES college(college_id),
    FOREIGN KEY (major_id) REFERENCES major(major_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建成员表
CREATE TABLE member (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    stu_id VARCHAR(20) UNIQUE NOT NULL COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender VARCHAR(2) NOT NULL COMMENT '性别：M-男，F-女',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    hometown VARCHAR(100) COMMENT '籍贯',
    phone VARCHAR(20) NOT NULL COMMENT '电话',
    college_id INT NOT NULL COMMENT '院系ID',
    major_id INT NOT NULL COMMENT '专业ID',
    student_position VARCHAR(50) COMMENT '校内职务',
    join_date DATE NOT NULL COMMENT '加入日期',
    status VARCHAR(20) DEFAULT '正常' COMMENT '状态：正常/退出/毕业',
    lab_position VARCHAR(50) DEFAULT '无' COMMENT '实验室职务',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (college_id) REFERENCES college(college_id),
    FOREIGN KEY (major_id) REFERENCES major(major_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建公开课表
CREATE TABLE course (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    topic VARCHAR(200) NOT NULL COMMENT '技术专题',
    course_date DATE NOT NULL COMMENT '开课日期',
    speaker VARCHAR(50) NOT NULL COMMENT '主讲人',
    organizer VARCHAR(200) COMMENT '组织人',
    location VARCHAR(100) COMMENT '地点',
    target_group TEXT NOT NULL COMMENT '群体说明',
    method VARCHAR(50) COMMENT '授课方式',
    credit_value DECIMAL(4,1) DEFAULT 0.5 COMMENT '创新学分值',
    participants_count INT DEFAULT 0 COMMENT '参加人数',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建公开课参与表
CREATE TABLE course_participant (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_id INT NOT NULL COMMENT '课程ID',
    stu_id VARCHAR(20) NOT NULL COMMENT '学号',
    sign_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
    is_credited INT DEFAULT 0 COMMENT '0-未录入学分,1-已录入学分',
    FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建竞赛表
CREATE TABLE competition (
    competition_id INT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(100) NOT NULL COMMENT '竞赛类别',
    title VARCHAR(200) NOT NULL COMMENT '项目题目',
    year INT NOT NULL COMMENT '参赛年度',
    leader VARCHAR(50) NOT NULL COMMENT '组长姓名',
    members TEXT COMMENT '组员姓名（多个用逗号分隔）',
    award_level VARCHAR(50) DEFAULT '无' COMMENT '获奖等级',
    advisor VARCHAR(50) COMMENT '指导老师',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 创建创新学分表
CREATE TABLE innovation_credit (
    credit_id INT PRIMARY KEY AUTO_INCREMENT,
    stu_id VARCHAR(20) NOT NULL COMMENT '学号',
    stu_name VARCHAR(50) NOT NULL COMMENT '学生姓名',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    college VARCHAR(100) NOT NULL COMMENT '院系',
    major VARCHAR(100) COMMENT '专业',
    credit_value DECIMAL(4,1) NOT NULL COMMENT '学分分值',
    credit_type VARCHAR(50) NOT NULL COMMENT '学分类别',
    reason TEXT NOT NULL COMMENT '事由说明',
    acquire_date DATE NOT NULL COMMENT '取得时间',
    record_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '录入时间',
    recorder VARCHAR(50) NOT NULL COMMENT '录入人',
    remark TEXT COMMENT '备注'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 插入初始院系数据
INSERT INTO college (college_name, college_code) VALUES 
('软件学院', '01'),
('计算机学院', '02'),
('信息与通信工程学院', '03');

-- 插入初始专业数据
INSERT INTO major (major_name, college_id) VALUES 
('软件工程', 1),
('网络工程', 1),
('信息安全', 1),
('计算机科学', 2),
('人工智能', 2),
('数据科学', 2),
('通信工程', 3),
('电子信息工程', 3),
('物联网工程', 3);

-- 插入初始用户（密码均为123456）
INSERT INTO user (username, password, role_id, status, real_name, college_id, major_id, grade, class_name, phone) VALUES 
('admin', '123456', 1, '启用', '超级管理员', 1, 1, '2021', '1班', '13800000001'),
('manager', '123456', 2, '启用', '实验室管理员', 1, 1, '2021', '1班', '13800000002'),
('user01', '123456', 2, '启用', '张三', 1, 1, '2021', '1班', '13800000003');
```

### 第三步：修改数据库配置
打开 `src/com/uolab/util/DbUtil.java`，修改数据库连接信息：
```java
public static Connection getConnection() {
    Connection conn = null;
    String url = "jdbc:mysql://localhost:3306/uolab_system?useSSL=false&serverTimezone=UTC";
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(url, "root", "123456"); // 修改为你的数据库密码
    } catch (Exception e) {
        throw new RuntimeException(e);
    }
    return conn;
}
```

### 第四步：运行项目
1. 用IDEA打开项目
2. 运行 `src/com/uolab/Main.java`
3. 登录系统（默认账号：`admin`，密码：`123456`）

---

## 📝 功能模块详细说明

### 1. 登录模块
- **功能描述**：用户通过用户名和密码登录系统
- **实现逻辑**：
  - 输入验证：用户名和密码不能为空
  - 身份验证：调用UserService验证用户名和密码
  - 权限判断：根据用户角色跳转到不同主界面
- **界面设计**：简洁的登录窗口，支持回车键登录

### 2. 用户管理模块
- **功能描述**：管理系统用户账号
- **核心功能**：
  - **添加用户**：仅超级管理员可操作，需填写用户名、真实姓名、手机号等必填项
  - **查询用户**：支持按姓名、院系、专业、年级、班级等多条件组合查询
  - **修改用户**：可修改手机号、QQ、校内职务、角色、备注等信息
  - **启用/禁用**：禁用后用户无法登录系统
  - **重置密码**：将用户密码重置为123456
  - **删除用户**：仅可删除未产生过任何信息的用户
- **数据验证**：
  - 用户名：字母开头，3-15个字母数字组合
  - 真实姓名：2-5个汉字
  - 手机号：11位有效手机号码
  - QQ号：5-15位数字（可选）

### 3. 院系管理模块
- **功能描述**：管理学院和专业信息
- **核心功能**：
  - **院系管理**：添加、查询院系信息
  - **专业管理**：添加专业，关联所属院系
- **数据关系**：院系和专业是一对多关系

### 4. 成员管理模块
- **功能描述**：管理实验室成员详细信息
- **核心功能**：
  - **成员添加**：录入学号、姓名、性别、年级、籍贯、电话、院系、专业等
  - **成员查询**：支持按姓名、年级、电话、院系、专业、状态、管理职务等多条件查询
  - **成员修改**：可修改电话、校内职务、状态、实验室职务、备注
  - **成员统计**：支持按年级、院系、专业、状态等多维度统计，包括人数、性别分布、管理人员数量等
- **数据验证**：
  - 学号：必须为数字
  - 姓名：2-5个汉字
  - 手机号：11位有效手机号码

### 5. 公开课管理模块
- **功能描述**：管理实验室公开课信息
- **核心功能**：
  - **课程录入**：添加技术专题、开课日期、主讲人、组织人、地点、群体说明、授课方式、创新学分值等
  - **课程查询**：按专题、主讲人、时间范围、授课方式等条件查询
  - **课程详情**：查看课程详细信息
  - **参与管理**：记录学生参与情况，支持签到和学分标记
- **数据验证**：
  - 技术专题：1-100个字符，只能包含字母和汉字
  - 主讲人：2-10个汉字
  - 创新学分值：0-10之间

### 6. 参赛管理模块
- **功能描述**：记录学生竞赛参与和获奖情况
- **核心功能**：
  - **参赛录入**：记录竞赛类别、项目题目、参赛年度、组长、组员、获奖等级、指导老师等
  - **参赛查询**：按年度、获奖等级等条件查询
  - **参赛修改**：可修改组员名单、获奖等级、备注
  - **参赛详情**：查看完整的竞赛信息
- **数据验证**：
  - 项目题目：2-30个汉字或字母组合
  - 组长/组员：2-6个汉字
  - 获奖等级：无/优秀奖/三等奖/二等奖/一等奖/特等奖

### 7. 创新学分管理模块
- **功能描述**：管理学生创新学分获取情况
- **核心功能**：
  - **学分录入**：选择学生，填写学分类别、学分值、事由、取得时间等
  - **学分查询**：按学号、姓名、时间段等条件查询
  - **学分统计**：按年度统计学生获得学分情况，包括总人数、总学分、人均学分、最高学分、院系排名等
- **统计维度**：
  - 获得学分总人数
  - 总学分数
  - 人均学分
  - 最高学分学生
  - 学分最高院系
  - 学分类别分布

---

## ✨ 本人完成的工作

本项目由本人独立完成，主要工作包括：

### 1. 系统分析与设计
- 完成需求分析，梳理出7大功能模块
- 设计系统架构，采用经典的三层架构（GUI层、Service层、DAO层）
- 绘制E-R图，设计数据库表结构（共8张表）
- 设计实体类及类之间的关系

### 2. 数据库设计与实现
- 设计符合第三范式的数据库表结构
- 合理设置主键、外键约束，保证数据完整性
- 设计索引优化查询性能
- 编写完整的建表SQL脚本
- 设计初始测试数据

### 3. 后端代码实现
- **实体层**：实现User、Member、College、Course、Competition、InnovationCredit等实体类
- **DAO层**：实现各实体对应的数据访问类，完成CRUD操作
- **Service层**：实现业务逻辑，包括数据验证、权限控制、统计计算等
- **工具类**：实现数据库连接管理（DbUtil）和密码加密（EncryptUtil）

### 4. 前端界面开发
- **登录界面**：实现用户登录功能
- **管理员主界面**：设计菜单栏和标签页，集成所有管理功能
- **用户主界面**：为普通用户设计简化版界面
- **各功能面板**：实现添加、查询、修改、统计等功能的图形界面
  - 用户管理面板（添加、查询、修改）
  - 成员管理面板（添加、查询、修改、统计）
  - 公开课管理面板（录入、查询）
  - 参赛管理面板（录入、查询、修改）
  - 创新学分管理面板（录入、查询、统计）

### 5. 核心功能实现
- **多条件组合查询**：动态构建SQL语句，实现灵活查询
- **数据统计功能**：使用分组统计和子查询，生成多维度统计数据
- **事务处理**：在关键操作中使用事务保证数据一致性
- **输入验证**：在Service层实现完整的数据格式验证
- **权限控制**：根据用户角色控制功能访问权限

### 6. 代码优化与调试
- 添加详细的代码注释，提高可读性
- 完善异常处理机制
- 优化数据库连接管理，防止连接泄漏
- 调试并修复多个Bug（如日期处理、SQL语法错误等）

---

## 📁 项目结构

```
UOLab-Management-System/
├── src/
│   └── com/
│       └── uolab/
│           ├── dao/           # 数据访问层
│           │   ├── UserDAO.java          # 用户数据操作
│           │   ├── MemberDAO.java         # 成员数据操作
│           │   ├── CollegeDAO.java        # 院系数据操作
│           │   ├── CourseDAO.java         # 公开课数据操作
│           │   ├── CompetitionDAO.java    # 竞赛数据操作
│           │   └── InnovationCreditDAO.java # 创新学分数据操作
│           ├── entity/         # 实体层
│           │   ├── User.java              # 用户实体
│           │   ├── Member.java             # 成员实体
│           │   ├── College.java            # 院系实体
│           │   ├── Course.java             # 公开课实体
│           │   ├── CourseParticipant.java  # 课程参与实体
│           │   ├── Competition.java        # 竞赛实体
│           │   └── InnovationCredit.java   # 创新学分实体
│           ├── service/        # 业务逻辑层
│           │   ├── UserService.java        # 用户业务逻辑
│           │   ├── MemberService.java       # 成员业务逻辑
│           │   ├── CourseService.java       # 公开课业务逻辑
│           │   ├── CompetitionService.java  # 竞赛业务逻辑
│           │   └── InnovationCreditService.java # 创新学分业务逻辑
│           ├── gui/            # 图形界面层
│           │   ├── LoginFrame.java         # 登录界面
│           │   ├── AdminMainFrame.java     # 管理员主界面
│           │   └── UserMainFrame.java      # 普通用户主界面
│           ├── util/           # 工具类层
│           │   ├── DbUtil.java             # 数据库连接工具
│           │   └── EncryptUtil.java        # 加密工具
│           └── Main.java       # 程序入口
├── sql/                        # SQL脚本
│   └── uolab_system.sql        # 数据库建表脚本
├── config/                      # 配置文件
│   └── config.properties       # 系统配置文件
├── README.md                    # 项目说明文档
├── LICENSE                      # 开源协议（MIT）
└── .gitignore                   # Git忽略文件
```

---

## 🎯 使用说明

### 默认登录账号
| 角色 | 用户名 | 密码 | 说明 |
|------|--------|------|------|
| 超级管理员 | admin | 123456 | 拥有所有权限，可添加用户 |
| 管理员 | manager | 123456 | 可管理业务数据 |
| 普通用户 | user01 | 123456 | 仅可查看个人信息 |

### 操作流程示例

#### 添加新成员
1. 使用管理员账号登录
2. 点击菜单栏"成员管理" → "添加成员"
3. 填写学号、姓名、性别、年级、籍贯、电话等信息
4. 选择院系和专业
5. 选择加入日期（默认为当天）
6. 点击"保存"完成添加

#### 录入竞赛信息
1. 使用管理员账号登录
2. 点击菜单栏"参赛管理" → "参赛信息录入"
3. 选择或输入竞赛类别
4. 填写项目题目、参赛年度、组长姓名
5. 填写组员（多个用逗号分隔）
6. 选择获奖等级，填写指导老师
7. 点击"保存"完成录入

#### 添加创新学分
1. 使用管理员账号登录
2. 点击菜单栏"创新学分" → "创新学分录入"
3. 输入学号，点击"选择"自动加载学生信息
4. 选择学分类别，设置学分分值
5. 选择事由（可自定义输入）
6. 选择取得时间（年/月）
7. 点击"保存"完成录入

#### 查询学分记录
1. 使用管理员账号登录
2. 点击菜单栏"创新学分" → "创新学分查询"
3. 可输入学号、姓名进行精确查询
4. 可选择时间段进行范围查询
5. 点击"查询"显示结果
6. 下方自动统计总记录数、总学分数、平均学分

---

## ⚠️ 注意事项

1. **数据库配置**：首次运行前请确保已正确修改`DbUtil.java`中的数据库连接信息
2. **MySQL版本**：建议使用MySQL 8.0，如果使用5.7版本需注意驱动版本
3. **编码问题**：如果出现中文乱码，请检查数据库、连接URL、IDE的编码设置是否为UTF-8
4. **权限说明**：只有超级管理员可以添加新用户，管理员只能管理业务数据
5. **删除限制**：已产生过信息的用户只能禁用，不能删除，以保证数据完整性
6. **数据备份**：建议定期备份数据库，防止数据丢失

---

## 🔧 常见问题解决

### Q1: 连接数据库失败？
- 检查MySQL服务是否启动
- 检查用户名密码是否正确
- 检查数据库`uolab_system`是否存在
- 检查防火墙是否阻止3306端口

### Q2: 登录提示"用户名或密码错误"？
- 确认用户名密码正确
- 检查用户状态是否为"启用"
- 查看数据库中的密码是否正确存储

### Q3: 添加用户失败？
- 检查是否使用超级管理员账号登录
- 确认所有必填字段都已填写
- 检查数据格式是否符合要求
  - 用户名：字母开头，3-15个字母数字
  - 真实姓名：2-5个汉字
  - 手机号：11位数字

### Q4: 查询不到数据？
- 确认查询条件是否正确
- 检查数据库中是否有对应数据
- 尝试使用更宽松的查询条件（如只输入部分关键词）

### Q5: 日期格式错误？
- 所有日期输入格式必须为：yyyy-MM-dd（例如：2026-02-27）
- 如果手动输入，请确保格式正确
- 建议使用日期选择按钮选择日期

### Q6: 统计数据显示异常？
- 检查数据库中是否有空值
- 确认统计时间范围设置正确
- 查看控制台是否有SQL错误信息

---

## 📄 开源协议

本项目采用 MIT 协议开源。详见 [LICENSE](LICENSE) 文件。

---

## 👤 作者信息

**李柯延**
- **学校**：中北大学
- **学院**：软件学院
- **邮箱**：2910953569@qq.com
- **GitHub**：[LIKE-YOU-3200](https://github.com/LIKE-YOU-3200)


**最后更新**：2026年2月27日
