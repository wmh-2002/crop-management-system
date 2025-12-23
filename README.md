# 农业管理系统

基于 Spring Boot + Vue 3 的农业管理系统，实现作物管理、农田管理、种植计划、生长监控、数据分析等功能。

## 技术栈

- **后端**: Spring Boot 3.2.0 + MySQL + MyBatis-Plus
- **前端**: Vue 3 + Element Plus + Vite
- **认证**: JWT

## 功能模块

- 用户管理：支持管理员、农场主、工作人员角色
- 作物管理：添加、编辑、删除作物信息
- 种植计划管理：制定种植计划，记录种植时间、预期收成等
- 作物生长监控：记录作物生长情况、气候数据、土壤湿度等
- 农田管理：管理农田信息，分配种植区域
- 数据分析与报告：提供作物生长过程中的数据分析报告
- 通知与提醒：自动提醒施肥、浇水、病虫害防治等事项

## 本地开发环境搭建

### 前端设置

1. 确保安装了 Node.js 16+ 和 npm
2. 安装依赖：

```bash
cd frontend
npm install
```

3. 启动前端服务：

```bash
npm run dev
```

前端服务将运行在 `http://localhost:3000`

**注意：前端使用Mock数据，不依赖后端API，因此无需启动后端服务。**

### 后端设置（可选）

如果需要使用真实API而非Mock数据：

1. 确保安装了 Java 17+ 和 Maven 3.6+
2. 确保 MySQL 服务正在运行
3. 创建数据库：

```sql
CREATE DATABASE agricultural_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

4. 运行数据库脚本创建表结构（位于项目根目录下的 database_schema.sql）
5. 修改 `backend/src/main/resources/application.properties` 中的数据库连接信息
6. 启动后端服务：

```bash
cd backend
mvn spring-boot:run
```

后端服务将运行在 `http://localhost:8080`

## 项目结构

```
agricultural-management-system/
├── backend/                 # Spring Boot 后端项目
│   ├── src/main/java/com/agrimanagement/
│   │   ├── config/         # 配置类
│   │   ├── controller/     # 控制器
│   │   ├── entity/         # 实体类
│   │   ├── repository/     # 数据访问层
│   │   ├── service/        # 业务逻辑层
│   │   └── util/           # 工具类
│   └── src/main/resources/
│       └── application.properties  # 配置文件
├── frontend/               # Vue 3 前端项目
│   ├── src/
│   │   ├── api/           # API 接口
│   │   ├── components/    # 组件
│   │   ├── views/         # 页面视图
│   │   ├── router/        # 路由配置
│   │   ├── store/         # 状态管理
│   │   ├── utils/         # 工具函数
│   │   └── assets/        # 静态资源
├── database_schema.sql     # 数据库表结构脚本
└── README.md
```

## 使用说明

1. 启动后端服务（端口 8080）
2. 启动前端服务（端口 3000）
3. 访问 `http://localhost:3000` 进入系统
4. 使用默认账号登录或注册新账号

## API 接口

**注意：前端使用Mock数据，所有API接口都返回模拟数据，不连接实际后端服务。**

### 认证相关
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册

### 用户管理
- `GET /api/users` - 获取用户列表
- `POST /api/users` - 创建用户
- `PUT /api/users/{id}` - 更新用户
- `DELETE /api/users/{id}` - 删除用户

### 作物管理
- `GET /api/crops` - 获取作物列表
- `POST /api/crops` - 创建作物
- `PUT /api/crops/{id}` - 更新作物
- `DELETE /api/crops/{id}` - 删除作物

### 农田管理
- `GET /api/farmlands` - 获取农田列表
- `POST /api/farmlands` - 创建农田
- `PUT /api/farmlands/{id}` - 更新农田
- `DELETE /api/farmlands/{id}` - 删除农田

### 种植计划管理
- `GET /api/planting-plans` - 获取种植计划列表
- `POST /api/planting-plans` - 创建种植计划
- `PUT /api/planting-plans/{id}` - 更新种植计划
- `DELETE /api/planting-plans/{id}` - 删除种植计划

### 生长监控
- `GET /api/growth-monitoring` - 获取生长监控记录
- `POST /api/growth-monitoring` - 创建生长监控记录
- `PUT /api/growth-monitoring/{id}` - 更新生长监控记录
- `DELETE /api/growth-monitoring/{id}` - 删除生长监控记录

### 报告管理
- `GET /api/reports` - 获取报告列表
- `POST /api/reports` - 创建报告
- `PUT /api/reports/{id}` - 更新报告
- `DELETE /api/reports/{id}` - 删除报告

### 通知管理
- `GET /api/notifications` - 获取通知列表
- `POST /api/notifications` - 创建通知
- `PUT /api/notifications/{id}` - 更新通知
- `DELETE /api/notifications/{id}` - 删除通知

## 配置说明

### 后端配置

在 `backend/src/main/resources/application.properties` 中可配置：

- 服务器端口
- 数据库连接信息
- JWT 密钥和过期时间
- 文件上传大小限制

### 前端配置

在 `frontend/vite.config.js` 中可配置：

- 开发服务器端口
- 代理设置（用于后端API）

## 部署说明

### 生产环境构建

1. 构建前端：

```bash
cd frontend
npm run build
```

2. 构建后端：

```bash
cd backend
mvn clean package
```

3. 运行后端 JAR 包：

```bash
java -jar target/agricultural-management-system-0.0.1-SNAPSHOT.jar
```

## 注意事项

- 首次启动时，数据库表会被自动创建
- 默认 JWT 过期时间为 24 小时
- 前端使用代理配置解决跨域问题
- 密码在数据库中以 BCrypt 加密形式存储

## 开发计划

- [x] 用户管理模块
- [x] 作物管理模块
- [x] 农田管理模块
- [x] 种植计划管理模块
- [x] 生长监控模块
- [x] 数据分析与报告模块
- [x] 通知提醒模块
- [x] 前端界面开发
- [ ] 完整测试
- [ ] 部署文档

