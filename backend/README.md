# 农业管理系统后端

基于Spring Boot开发的农业管理系统后端API服务。

## 技术栈

- **框架**: Spring Boot 3.1.5
- **数据库**: MySQL 8.0
- **ORM**: Spring Data JPA + Hibernate
- **安全**: Spring Security + JWT
- **构建工具**: Maven
- **Java版本**: 17

## 快速开始

### 环境准备

1. 安装Java 17
2. 安装MySQL 8.0
3. 创建数据库：

   #### 方法一：使用初始化脚本（推荐）
   ```bash
   # 登录MySQL
   mysql -u root -p

   # 运行初始化脚本
   source backend/init-database.sql
   ```

   #### 方法二：手动创建
   在MySQL中创建数据库：
   ```sql
   CREATE DATABASE agricultural_management CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

### 配置数据库

修改 `src/main/resources/application.yml` 中的数据库连接信息：
```yaml
spring:
  datasource:
    username: your_username
    password: your_password
```

### 运行项目

```bash
# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

项目启动后，访问：http://localhost:8080

### 快速体验农田管理功能

1. **创建测试数据**：
   ```bash
   ./create-farmlands.sh
   ```

2. **测试API功能**：
   ```bash
   ./test-farmland-api.sh
   ```

3. **查看已创建的农田**：
   ```bash
   curl -X GET "http://localhost:8080/api/farmlands" \
     -H "Authorization: Bearer YOUR_ADMIN_TOKEN"
   ```

## API接口

### 认证接口

#### 登录
- **URL**: `POST /api/auth/login`
- **请求体**:
  ```json
  {
    "username": "admin",
    "password": "admin123"
  }
  ```
- **响应**:
  ```json
  {
    "code": 200,
    "message": "登录成功",
    "data": {
      "token": "jwt_token_here",
      "id": 1,
      "username": "admin",
      "realName": "管理员",
      "role": "ADMIN"
    }
  }
  ```

#### 注册
- **URL**: `POST /api/auth/register`
- **请求体**:
  ```json
  {
    "username": "newuser",
    "password": "password123"
  }
  ```

## 用户管理API

### 获取用户列表
- **URL**: `GET /api/users?page=1&size=10&username=admin&role=ADMIN`
- **权限**: ADMIN
- **参数**:
  - `page`: 页码 (默认: 1)
  - `size`: 每页大小 (默认: 10)
  - `username`: 用户名筛选
  - `realName`: 真实姓名筛选
  - `email`: 邮箱筛选
  - `role`: 角色筛选 (ADMIN/FARMER/STAFF)
  - `status`: 状态筛选 (true/false)

### 获取用户信息
- **URL**: `GET /api/users/{id}`
- **权限**: ADMIN

### 创建用户
- **URL**: `POST /api/users`
- **权限**: ADMIN
- **请求体**:
  ```json
  {
    "username": "newuser",
    "password": "password123",
    "realName": "新用户",
    "email": "user@example.com",
    "phone": "13800138000",
    "role": "FARMER",
    "status": true
  }
  ```

### 更新用户
- **URL**: `PUT /api/users/{id}`
- **权限**: ADMIN
- **请求体**:
  ```json
  {
    "realName": "更新姓名",
    "email": "updated@example.com",
    "phone": "13800138001",
    "role": "STAFF",
    "status": true
  }
  ```

### 删除用户
- **URL**: `DELETE /api/users/{id}`
- **权限**: ADMIN

### 启用用户
- **URL**: `PUT /api/users/{id}/enable`
- **权限**: ADMIN

### 禁用用户
- **URL**: `PUT /api/users/{id}/disable`
- **权限**: ADMIN

### 使用API

在请求头中添加JWT Token：
```
Authorization: Bearer your_jwt_token_here
```

## 农田管理API

### 获取农田列表
- **URL**: `GET /api/farmlands?page=1&size=10&name=农田名&location=位置&status=AVAILABLE`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `name` (可选): 农田名称模糊查询
  - `location` (可选): 位置模糊查询
  - `status` (可选): 农田状态 (AVAILABLE, PLANTED, MAINTENANCE)

### 获取农田详情
- **URL**: `GET /api/farmlands/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建农田
- **URL**: `POST /api/farmlands`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**:
  ```json
  {
    "name": "测试农田",
    "area": 500.50,
    "location": "北京市朝阳区",
    "status": "AVAILABLE",
    "description": "农田描述"
  }
  ```

### 更新农田
- **URL**: `PUT /api/farmlands/{id}`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**: 同创建农田

### 删除农田
- **URL**: `DELETE /api/farmlands/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 获取当前用户农田
- **URL**: `GET /api/farmlands/my`
- **权限**: ADMIN, FARMER, STAFF

### 按状态查询农田
- **URL**: `GET /api/farmlands/status/{status}`
- **权限**: ADMIN, FARMER, STAFF
- **状态值**: AVAILABLE, PLANTED, MAINTENANCE

### 农田状态说明
- **AVAILABLE**: 可种植状态
- **PLANTED**: 已种植状态
- **MAINTENANCE**: 维护中状态

## 种植计划管理API

### 获取种植计划列表
- **URL**: `GET /api/planting-plans?page=1&size=10&planName=计划名&farmlandId=农田ID&cropId=作物ID&status=PLANNED&startDateFrom=2024-01-01&startDateTo=2024-12-31`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `planName` (可选): 计划名称模糊查询
  - `farmlandId` (可选): 农田ID
  - `cropId` (可选): 作物ID
  - `status` (可选): 计划状态 (PLANNED, IN_PROGRESS, COMPLETED, CANCELLED)
  - `startDateFrom` (可选): 开始日期从 (YYYY-MM-DD)
  - `startDateTo` (可选): 开始日期到 (YYYY-MM-DD)

### 获取种植计划详情
- **URL**: `GET /api/planting-plans/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建种植计划
- **URL**: `POST /api/planting-plans`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**:
  ```json
  {
    "farmlandId": 1,
    "cropId": 1,
    "planName": "春季蔬菜种植计划",
    "plannedStartDate": "2024-03-01",
    "plannedEndDate": "2024-05-01",
    "expectedHarvestDate": "2024-05-15",
    "sowingDensity": "每亩5000株",
    "notes": "春季蔬菜种植计划",
    "status": "PLANNED"
  }
  ```

### 更新种植计划
- **URL**: `PUT /api/planting-plans/{id}`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**: 同创建种植计划

### 删除种植计划
- **URL**: `DELETE /api/planting-plans/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 获取当前用户种植计划
- **URL**: `GET /api/planting-plans/my`
- **权限**: ADMIN, FARMER, STAFF

### 根据农田获取种植计划
- **URL**: `GET /api/planting-plans/farmland/{farmlandId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据作物获取种植计划
- **URL**: `GET /api/planting-plans/crop/{cropId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据状态获取种植计划
- **URL**: `GET /api/planting-plans/status/{status}`
- **权限**: ADMIN, FARMER, STAFF
- **状态值**: PLANNED, IN_PROGRESS, COMPLETED, CANCELLED

### 种植计划状态说明
- **PLANNED**: 已计划，尚未开始执行
- **IN_PROGRESS**: 进行中，正在执行种植计划
- **COMPLETED**: 已完成，种植计划执行完毕
- **CANCELLED**: 已取消，种植计划被取消

## 种植记录管理API

### 获取种植记录列表
- **URL**: `GET /api/planting-records?page=1&size=10&planId=种植计划ID&operationType=SOWING&operatorId=操作员ID&operationDateFrom=2024-01-01&operationDateTo=2024-12-31&weatherConditions=天气`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `planId` (可选): 种植计划ID
  - `operationType` (可选): 操作类型 (SOWING, FERTILIZING, WATERING, PESTICIDING, HARVESTING)
  - `operatorId` (可选): 操作员ID
  - `operationDateFrom` (可选): 操作日期从 (YYYY-MM-DD)
  - `operationDateTo` (可选): 操作日期到 (YYYY-MM-DD)
  - `weatherConditions` (可选): 天气条件模糊查询

### 获取种植记录详情
- **URL**: `GET /api/planting-records/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建种植记录
- **URL**: `POST /api/planting-records`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**:
  ```json
  {
    "planId": 1,
    "operationType": "SOWING",
    "operationDate": "2024-03-01",
    "quantityUsed": 100.50,
    "operatorId": 1,
    "weatherConditions": "晴天，温度25°C",
    "notes": "春季蔬菜播种记录"
  }
  ```

### 更新种植记录
- **URL**: `PUT /api/planting-records/{id}`
- **权限**: ADMIN, FARMER, STAFF（只有操作员本人可以修改）
- **请求体**: 同创建种植记录

### 删除种植记录
- **URL**: `DELETE /api/planting-records/{id}`
- **权限**: ADMIN, FARMER, STAFF（只有操作员本人可以删除）

### 获取当前用户种植记录
- **URL**: `GET /api/planting-records/my`
- **权限**: ADMIN, FARMER, STAFF

### 根据种植计划获取记录列表
- **URL**: `GET /api/planting-records/plan/{planId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据操作类型获取记录列表
- **URL**: `GET /api/planting-records/operation-type/{operationType}`
- **权限**: ADMIN, FARMER, STAFF
- **操作类型**: SOWING, FERTILIZING, WATERING, PESTICIDING, HARVESTING

### 根据操作员获取记录列表
- **URL**: `GET /api/planting-records/operator/{operatorId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据种植计划和操作类型获取记录
- **URL**: `GET /api/planting-records/plan/{planId}/operation-type/{operationType}`
- **权限**: ADMIN, FARMER, STAFF

### 根据日期范围获取记录
- **URL**: `GET /api/planting-records/date-range?startDate=2024-01-01&endDate=2024-12-31`
- **权限**: ADMIN, FARMER, STAFF

### 种植记录操作类型说明
- **SOWING**: 播种操作，记录种子用量和播种情况
- **FERTILIZING**: 施肥操作，记录肥料用量和施肥方式
- **WATERING**: 灌溉操作，记录水量和灌溉方式
- **PESTICIDING**: 施药操作，记录农药用量和防治情况
- **HARVESTING**: 收获操作，记录收获量和质量情况

## 生长监测管理API

### 获取生长监测列表
- **URL**: `GET /api/growth-monitoring?page=1&size=10&planId=种植计划ID&monitoringDateFrom=2024-01-01&monitoringDateTo=2024-12-31&healthStatus=EXCELLENT&minHeight=20.0&maxHeight=30.0&minTemperature=20.0&maxTemperature=30.0`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `planId` (可选): 种植计划ID
  - `monitoringDateFrom` (可选): 监测日期从 (YYYY-MM-DD)
  - `monitoringDateTo` (可选): 监测日期到 (YYYY-MM-DD)
  - `healthStatus` (可选): 健康状态 (EXCELLENT, GOOD, FAIR, POOR)
  - `minHeight` (可选): 最小高度 (cm)
  - `maxHeight` (可选): 最大高度 (cm)
  - `minTemperature` (可选): 最小温度 (°C)
  - `maxTemperature` (可选): 最大温度 (°C)
  - 其他环境参数范围查询: minWidth, maxWidth, minHumidity, maxHumidity, minSoilMoisture, maxSoilMoisture, minPhLevel, maxPhLevel

### 获取生长监测详情
- **URL**: `GET /api/growth-monitoring/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建生长监测记录
- **URL**: `POST /api/growth-monitoring`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**:
  ```json
  {
    "planId": 1,
    "monitoringDate": "2024-03-01",
    "heightMeasurement": 25.5,
    "widthMeasurement": 15.8,
    "healthStatus": "EXCELLENT",
    "soilMoisture": 65.5,
    "temperature": 23.5,
    "humidity": 75.2,
    "lightIntensity": 85000,
    "phLevel": 6.8,
    "notes": "作物生长状态良好",
    "photoUrl": "https://example.com/monitoring/photo.jpg"
  }
  ```

### 更新生长监测记录
- **URL**: `PUT /api/growth-monitoring/{id}`
- **权限**: ADMIN, FARMER, STAFF（只有创建者本人可以修改）
- **请求体**: 同创建生长监测记录

### 删除生长监测记录
- **URL**: `DELETE /api/growth-monitoring/{id}`
- **权限**: ADMIN, FARMER, STAFF（只有创建者本人可以删除）

### 获取当前用户生长监测记录
- **URL**: `GET /api/growth-monitoring/my`
- **权限**: ADMIN, FARMER, STAFF

### 根据种植计划获取生长监测记录
- **URL**: `GET /api/growth-monitoring/plan/{planId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据种植计划和日期范围获取生长监测记录
- **URL**: `GET /api/growth-monitoring/plan/{planId}/date-range?startDate=2024-01-01&endDate=2024-12-31`
- **权限**: ADMIN, FARMER, STAFF

### 根据种植计划和健康状态获取生长监测记录
- **URL**: `GET /api/growth-monitoring/plan/{planId}/health-status/{healthStatus}`
- **权限**: ADMIN, FARMER, STAFF

### 根据健康状态获取生长监测记录列表
- **URL**: `GET /api/growth-monitoring/health-status/{healthStatus}`
- **权限**: ADMIN, FARMER, STAFF

### 根据日期范围获取生长监测记录
- **URL**: `GET /api/growth-monitoring/date-range?startDate=2024-01-01&endDate=2024-12-31`
- **权限**: ADMIN, FARMER, STAFF

### 根据温度范围获取生长监测记录
- **URL**: `GET /api/growth-monitoring/temperature-range?minTemp=20.0&maxTemp=30.0`
- **权限**: ADMIN, FARMER, STAFF

### 根据土壤湿度范围获取生长监测记录
- **URL**: `GET /api/growth-monitoring/soil-moisture-range?minMoisture=60.0&maxMoisture=80.0`
- **权限**: ADMIN, FARMER, STAFF

### 根据pH值范围获取生长监测记录
- **URL**: `GET /api/growth-monitoring/ph-range?minPh=6.0&maxPh=7.5`
- **权限**: ADMIN, FARMER, STAFF

### 生长监测健康状态说明
- **EXCELLENT**: 优秀，作物生长状态极佳，无任何问题
- **GOOD**: 良好，作物生长发育正常，健康状况良好
- **FAIR**: 一般，作物生长发育一般，有轻微问题需要关注
- **POOR**: 较差，作物生长发育不良，需要及时采取措施

### 生长监测环境参数范围
- **高度测量**: 0.01 - 99999.99 cm
- **宽度测量**: 0.01 - 99999.99 cm
- **土壤湿度**: 0.00 - 100.00 %
- **温度**: -50.00 - 60.00 °C
- **湿度**: 0.00 - 100.00 %
- **光照强度**: 0 - 100000 lux
- **pH值**: 0.00 - 14.00

## 通知提醒管理API

### 获取通知列表
- **URL**: `GET /api/notifications?page=1&size=10&title=标题&type=FERTILIZING&priority=HIGH&status=PENDING&planId=种植计划ID&recipientId=接收者ID&scheduledTimeFrom=2024-01-01T10:00:00&scheduledTimeTo=2024-12-31T23:59:59`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `title` (可选): 标题模糊查询
  - `type` (可选): 通知类型 (FERTILIZING, WATERING, PESTICIDING, MONITORING, HARVESTING, OTHER)
  - `priority` (可选): 优先级 (HIGH, MEDIUM, LOW)
  - `status` (可选): 状态 (PENDING, SENT, READ)
  - `planId` (可选): 种植计划ID
  - `recipientId` (可选): 接收者ID
  - `scheduledTimeFrom` (可选): 计划发送时间从
  - `scheduledTimeTo` (可选): 计划发送时间到

### 获取通知详情
- **URL**: `GET /api/notifications/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建通知
- **URL**: `POST /api/notifications`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**:
  ```json
  {
    "title": "施肥提醒通知",
    "content": "您的农田需要进行施肥操作",
    "type": "FERTILIZING",
    "priority": "HIGH",
    "scheduledTime": "2024-03-01T10:00:00",
    "status": "PENDING",
    "recipientIds": [1, 2, 3],
    "planId": 1
  }
  ```

### 更新通知
- **URL**: `PUT /api/notifications/{id}`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**: 同创建通知

### 删除通知
- **URL**: `DELETE /api/notifications/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 发送通知
- **URL**: `POST /api/notifications/{id}/send`
- **权限**: ADMIN, FARMER, STAFF
- **说明**: 将通知状态从PENDING更新为SENT，并设置发送时间

### 标记通知为已读
- **URL**: `POST /api/notifications/{id}/read`
- **权限**: ADMIN, FARMER, STAFF
- **说明**: 将通知状态更新为READ

### 批量发送通知
- **URL**: `POST /api/notifications/batch-send`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**: `[1, 2, 3]` (通知ID列表)

### 根据类型获取通知列表
- **URL**: `GET /api/notifications/type/{type}`
- **权限**: ADMIN, FARMER, STAFF

### 根据优先级获取通知列表
- **URL**: `GET /api/notifications/priority/{priority}`
- **权限**: ADMIN, FARMER, STAFF

### 根据状态获取通知列表
- **URL**: `GET /api/notifications/status/{status}`
- **权限**: ADMIN, FARMER, STAFF

### 根据种植计划获取通知列表
- **URL**: `GET /api/notifications/plan/{planId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据接收者获取通知列表
- **URL**: `GET /api/notifications/recipient/{recipientId}`
- **权限**: ADMIN, FARMER, STAFF

### 根据接收者和状态获取通知列表
- **URL**: `GET /api/notifications/recipient/{recipientId}/status/{status}`
- **权限**: ADMIN, FARMER, STAFF

### 获取待发送的通知列表
- **URL**: `GET /api/notifications/pending`
- **权限**: ADMIN, FARMER, STAFF

### 获取待发送的通知列表（计划时间已到）
- **URL**: `GET /api/notifications/to-send`
- **权限**: ADMIN, FARMER, STAFF

### 通知类型说明
- **FERTILIZING**: 施肥提醒，提醒用户进行施肥操作
- **WATERING**: 灌溉提醒，提醒用户进行灌溉操作
- **PESTICIDING**: 施药提醒，提醒用户进行农药施用
- **MONITORING**: 监测提醒，提醒用户进行生长监测
- **HARVESTING**: 收获提醒，提醒用户进行收获操作
- **OTHER**: 其他通知，通用通知类型

### 通知优先级说明
- **HIGH**: 高优先级，紧急处理的通知
- **MEDIUM**: 中优先级，正常处理的通知
- **LOW**: 低优先级，可延后处理的通知

### 通知状态说明
- **PENDING**: 待发送，通知已创建但未到发送时间或未手动发送
- **SENT**: 已发送，通知已推送给接收者
- **READ**: 已阅读，接收者已查看通知

## 数据分析报告管理API

### 获取报告列表
- **URL**: `GET /api/reports?page=1&size=10&title=标题&reportType=CROP_GROWTH&generatedBy=生成者ID&startDateFrom=2024-01-01&startDateTo=2024-12-31`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `title` (可选): 报告标题模糊查询
  - `reportType` (可选): 报告类型 (CROP_GROWTH, FIELD_UTILIZATION, PRODUCTION_ANALYSIS, WEATHER_IMPACT, PREDICTIVE_ANALYSIS)
  - `generatedBy` (可选): 生成者ID
  - `startDateFrom` (可选): 开始日期从 (YYYY-MM-DD)
  - `startDateTo` (可选): 开始日期到 (YYYY-MM-DD)

### 获取报告详情
- **URL**: `GET /api/reports/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建报告
- **URL**: `POST /api/reports`
- **权限**: ADMIN, FARMER, STAFF
- **请求体**:
  ```json
  {
    "title": "春季蔬菜生长分析报告",
    "reportType": "CROP_GROWTH",
    "startDate": "2024-01-01",
    "endDate": "2024-03-31",
    "content": "本报告分析了春季蔬菜的生长情况...",
    "chartData": "{\"heightChart\": [{\"date\": \"2024-01-01\", \"value\": 15.2}]}"
  }
  ```

### 根据报告类型获取报告列表
- **URL**: `GET /api/reports/type/{reportType}`
- **权限**: ADMIN, FARMER, STAFF

### 根据生成者获取报告列表
- **URL**: `GET /api/reports/generated-by/{generatedBy}`
- **权限**: ADMIN, FARMER, STAFF

### 获取当前用户报告列表
- **URL**: `GET /api/reports/my`
- **权限**: ADMIN, FARMER, STAFF

### 根据日期范围获取报告列表
- **URL**: `GET /api/reports/date-range?startDate=2024-01-01&endDate=2024-12-31`
- **权限**: ADMIN, FARMER, STAFF

### 获取最新报告列表
- **URL**: `GET /api/reports/latest?limit=10`
- **权限**: ADMIN, FARMER, STAFF

### 报告类型说明
- **CROP_GROWTH**: 作物生长报告，分析作物生长状况、发育趋势、健康状态等
- **FIELD_UTILIZATION**: 农田利用报告，统计农田利用率、作物分布、土地使用效率等
- **PRODUCTION_ANALYSIS**: 生产分析报告，分析产量、效率、成本效益等生产指标
- **WEATHER_IMPACT**: 天气影响报告，分析天气因素对农业生产的影响
- **PREDICTIVE_ANALYSIS**: 预测分析报告，基于历史数据预测未来趋势和产量

## 作物管理API

### 获取作物列表
- **URL**: `GET /api/crops?page=1&size=10&name=作物名&variety=品种&plantingSeason=春季&minGrowthPeriod=60&maxGrowthPeriod=120`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `page` (可选): 页码，默认1
  - `size` (可选): 每页数量，默认10
  - `name` (可选): 作物名称模糊查询
  - `variety` (可选): 品种模糊查询
  - `plantingSeason` (可选): 种植季节
  - `minGrowthPeriod` (可选): 最小生长期（天）
  - `maxGrowthPeriod` (可选): 最大生长期（天）

### 获取作物详情
- **URL**: `GET /api/crops/{id}`
- **权限**: ADMIN, FARMER, STAFF

### 创建作物
- **URL**: `POST /api/crops`
- **权限**: ADMIN
- **请求体**:
  ```json
  {
    "name": "水稻",
    "variety": "籼稻",
    "plantingSeason": "春季",
    "growthPeriod": 120,
    "expectedYield": 650.50,
    "waterNeeds": "需要充足水分",
    "fertilizerNeeds": "氮磷钾复合肥",
    "diseaseInfo": "稻瘟病、白叶枯病",
    "description": "主要粮食作物"
  }
  ```

### 更新作物
- **URL**: `PUT /api/crops/{id}`
- **权限**: ADMIN
- **请求体**: 同创建作物

### 删除作物
- **URL**: `DELETE /api/crops/{id}`
- **权限**: ADMIN

### 获取所有作物
- **URL**: `GET /api/crops/all`
- **权限**: ADMIN, FARMER, STAFF

### 按季节查询作物
- **URL**: `GET /api/crops/season/{season}`
- **权限**: ADMIN, FARMER, STAFF
- **季节值**: 春季, 夏季, 秋季, 冬季

### 按生长期查询作物
- **URL**: `GET /api/crops/growth-period?minDays=60&maxDays=120`
- **权限**: ADMIN, FARMER, STAFF
- **参数**:
  - `minDays`: 最小生长期（天）
  - `maxDays` (可选): 最大生长期（天）

### 作物字段说明
- **name**: 作物名称（如：水稻、小麦、玉米）
- **variety**: 具体品种（如：籼稻、冬小麦、甜玉米）
- **plantingSeason**: 最佳种植季节
- **growthPeriod**: 生长周期（天）
- **expectedYield**: 预期产量（kg/亩）
- **waterNeeds**: 水分需求描述
- **fertilizerNeeds**: 肥料需求描述
- **diseaseInfo**: 常见病虫害信息
- **description**: 作物详细描述

## 初始数据

项目启动时会自动创建以下测试用户：

| 用户名   | 密码      | 角色     | 描述     |
|----------|-----------|----------|----------|
| admin    | admin123  | ADMIN    | 管理员   |
| farmer1  | farmer123 | FARMER   | 农民     |
| staff1   | staff123  | STAFF    | 工作人员 |

## 项目结构

```
src/main/java/com/agricultural/management/
├── config/           # 配置类
├── controller/       # 控制器层
├── dto/             # 数据传输对象
├── entity/          # 实体类
├── exception/       # 异常处理
├── repository/      # 数据访问层
├── security/        # 安全相关
└── service/         # 业务逻辑层
```

## 开发说明

1. 数据库表结构定义在根目录的 `database_schema.sql` 中
2. API遵循RESTful设计规范
3. 使用Lombok简化代码
4. 集成Swagger UI进行API文档管理（可选）

## 测试

### 单元测试
运行单元测试：
```bash
mvn test
```

### API测试

#### 快速测试
运行快速API测试脚本（推荐用于日常验证）：
```bash
# 确保应用已启动，然后运行测试
./quick-test.sh
```

快速测试包含：
- 管理员登录
- 获取用户列表
- 创建/删除测试用户

#### 完整API测试
运行完整的用户管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-user-api.sh
```

完整测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取用户列表（分页查询）
3. ➕ 创建新用户
4. 🔍 查询用户信息
5. ✏️ 更新用户信息
6. 🚫 禁用用户
7. ✅ 启用用户
8. 🗑️ 删除用户
9. 🔍 条件查询用户（按角色、状态筛选）
10. 🚫 权限控制验证（普通用户无法访问管理员API）

#### 农田管理API测试
运行完整的农田管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-farmland-api.sh
```

农田测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取农田列表（分页查询）
3. ➕ 创建新农田
4. 🔍 查询农田信息
5. ✏️ 更新农田信息
6. 👤 获取当前用户农田列表
7. 🔍 按状态查询农田
8. 🔍 条件查询农田
9. 🗑️ 删除农田
10. 🚫 权限控制验证

#### 作物管理API测试
运行完整的作物管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-crop-api.sh
```

作物测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取作物列表（分页查询）
3. ➕ 创建新作物
4. 🔍 查询作物信息
5. ✏️ 更新作物信息
6. 📋 获取所有作物
7. 🔍 按季节查询作物
8. 🔍 按生长期查询作物
9. 🔍 条件查询作物
10. 🗑️ 删除作物
11. 🚫 权限控制验证

#### 种植计划管理API测试
运行完整的种植计划管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-planting-plan-api.sh
```

种植计划测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取种植计划列表（分页查询）
3. ➕ 创建新种植计划
4. 🔍 查询种植计划信息
5. ✏️ 更新种植计划信息
6. 👤 获取当前用户种植计划列表
7. 🌾 根据农田获取种植计划
8. 🌱 根据作物获取种植计划
9. 📊 根据状态获取种植计划
10. 🔍 条件查询种植计划
11. 🗑️ 删除种植计划
12. 🚫 权限控制验证

#### 种植记录管理API测试
运行完整的种植记录管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-planting-record-api.sh
```

种植记录测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取种植记录列表（分页查询）
3. ➕ 创建种植记录（多种操作类型）
4. 🔍 查询种植记录信息
5. ✏️ 更新种植记录信息
6. 👤 获取当前用户种植记录列表
7. 🌾 根据种植计划获取记录
8. 🔧 根据操作类型获取记录
9. 👨‍🌾 根据操作员获取记录
10. 🔍 根据计划和操作类型获取记录
11. 📅 根据日期范围获取记录
12. 🔍 条件查询种植记录
13. 🗑️ 删除种植记录
14. 🚫 权限控制验证
15. 🚫 重复记录验证

#### 生长监测管理API测试
运行完整的生长监测管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-growth-monitoring-api.sh
```

生长监测测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取生长监测列表（分页查询）
3. ➕ 创建生长监测记录（多种健康状态）
4. 🔍 查询生长监测记录信息
5. ✏️ 更新生长监测记录信息
6. 👤 获取当前用户生长监测记录列表
7. 🌾 根据种植计划获取监测记录
8. 📊 根据健康状态获取监测记录
9. 📅 根据日期范围获取监测记录
10. 🌡️ 根据温度范围获取监测记录
11. 💧 根据土壤湿度范围获取监测记录
12. 🧪 根据pH值范围获取监测记录
13. 🔍 条件查询生长监测记录
14. 🗑️ 删除生长监测记录
15. 🚫 权限控制验证
16. 🚫 重复记录验证

#### 通知提醒管理API测试
运行完整的通知提醒管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-notification-api.sh
```

通知提醒测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取通知列表（分页查询）
3. ➕ 创建不同类型的通知（施肥/灌溉/监测）
4. 🔍 查询通知信息
5. 🔧 根据类型获取通知
6. 📊 根据优先级获取通知
7. 📊 根据状态获取通知
8. ⏰ 获取待发送通知
9. 📤 发送通知
10. 👁️ 标记已读
11. 📦 批量发送通知
12. 🔍 条件查询通知
13. ✏️ 更新通知信息
14. 🗑️ 删除通知
15. 🚫 权限控制验证
16. 🚫 时间验证测试

#### 数据分析报告管理API测试
运行完整的数据分析报告管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-report-api.sh
```

数据分析报告测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取报告列表（分页查询）
3. ➕ 创建作物生长报告
4. ➕ 创建农田利用报告
5. 🔍 查询报告详情
6. 🔧 根据报告类型获取报告列表
7. 👤 获取当前用户报告列表
8. 📅 根据日期范围获取报告列表
9. 🆕 获取最新报告列表
10. 🔍 条件查询报告
11. 📄 分页查询报告列表
12. 🚫 权限控制验证

#### 作物管理API测试
运行完整的作物管理API测试脚本：
```bash
# 确保应用已启动，然后运行测试
./test-crop-api.sh
```

作物测试脚本会自动执行：
1. 🔐 管理员登录获取Token
2. 📋 获取作物列表（分页查询）
3. ➕ 创建新作物
4. 🔍 查询作物信息
5. ✏️ 更新作物信息
6. 📋 获取所有作物
7. 🔍 按季节查询作物
8. 🔍 按生长期查询作物
9. 🔍 条件查询作物
10. 🗑️ 删除作物
11. 🚫 权限控制验证

#### 农田管理工具脚本
```bash
# 创建5个不同类型的农田数据
./create-farmlands.sh

# 查询和显示所有农田信息
./list-farmlands.sh

# 完整农田API功能测试
./test-farmland-api.sh

# 创建作物测试数据
./create-crops.sh        # 10个常见作物数据

# 查询作物列表
./list-crops.sh

# 完整作物API功能测试
./test-crop-api.sh

# 种植计划相关脚本（需要先有农田和作物数据）
# ./create-planting-plans.sh    # 批量创建种植计划（待实现）
# ./list-planting-plans.sh      # 查询种植计划列表（待实现）
# ./test-planting-plan-api.sh   # 种植计划API测试

# 种植记录相关脚本（需要先有种植计划数据）
# ./create-planting-records.sh  # 批量创建种植记录（待实现）
# ./list-planting-records.sh    # 查询种植记录列表（待实现）
# ./test-planting-record-api.sh # 种植记录API测试

# 生长监测相关脚本（需要先有种植计划数据）
# ./create-growth-monitoring.sh # 批量创建生长监测记录（待实现）
# ./list-growth-monitoring.sh   # 查询生长监测记录列表（待实现）
# ./test-growth-monitoring-api.sh # 生长监测API测试

# 通知提醒相关脚本
# ./create-notifications.sh    # 批量创建通知（待实现）
# ./list-notifications.sh      # 查询通知列表（待实现）
# ./test-notification-api.sh   # 通知管理API测试

# 数据分析报告相关脚本
# ./create-reports.sh          # 批量创建报告（待实现）
# ./list-reports.sh            # 查询报告列表（待实现）
# ./test-report-api.sh         # 报告管理API测试

# 系统初始化脚本
./initialize-system-data.sh   # 一键初始化所有表的基础数据
```

包含不同状态的农田：AVAILABLE（可种植）、PLANTED（已种植）、MAINTENANCE（维护中）
包含多种作物：粮食作物（水稻、小麦、玉米等）和蔬菜作物（番茄、黄瓜、白菜等）
种植计划：支持农田和作物的关联管理，包含时间规划和状态跟踪
种植记录：记录种植过程中的各种操作（播种、施肥、灌溉、施药、收获），包含用量、天气、操作员等信息
生长监测：记录作物生长状态和环境参数（高度、宽度、健康状态、温度、湿度、土壤湿度、光照、pH值等），支持照片上传和时间序列分析
通知提醒：智能通知系统，支持施肥、灌溉、施药、监测、收获等各类农业提醒，支持定时发送、多接收者、优先级管理
数据分析报告：专业的数据分析报告系统，支持作物生长、农田利用、生产分析、天气影响、预测分析等各类报告，支持图表数据存储和可视化展示

## 系统数据初始化

### 一键初始化脚本
为了方便测试和演示，系统提供了完整的初始化脚本，可以一键创建所有表的基础数据：

```bash
# 确保Spring Boot应用正在运行，然后执行初始化
./initialize-system-data.sh
```

### 初始化脚本功能
该脚本会按正确的顺序创建以下数据：

#### 1. 用户数据 (Users)
- 管理员用户：admin (已由DataInitializer创建)
- 农民用户：farmer_zhang (张三)
- 工作人员用户：staff_li (李四)

#### 2. 农田数据 (Farmlands)
- 蔬菜种植区A：5000平方米，专门用于蔬菜种植
- 粮食种植区B：10000平方米，用于水稻等粮食作物
- 试验田C：2000平方米，用于农业科研试验

#### 3. 作物数据 (Crops)
- 蔬菜作物：番茄(樱桃番茄)、黄瓜(温室黄瓜)
- 粮食作物：水稻(杂交水稻)、小麦(强筋小麦)
- 经济作物：玉米(甜玉米)

#### 4. 种植计划数据 (Planting Plans)
- 2024年春季樱桃番茄种植计划
- 2024年春季杂交水稻种植计划

#### 5. 种植记录数据 (Planting Records)
- 番茄播种记录、施肥记录
- 水稻播种记录

#### 6. 生长监测数据 (Growth Monitoring)
- 番茄生长监测：高度、宽度、健康状态、环境参数
- 水稻生长监测：秧苗生长数据

#### 7. 通知提醒数据 (Notifications)
- 施肥提醒通知
- 灌溉提醒通知
- 生长监测提醒通知

#### 8. 数据分析报告 (Reports)
- 作物生长分析报告
- 农田利用率分析报告
- 天气影响分析报告

### 初始化数据统计
运行完成后，系统将包含：
- 👥 用户数量: 3个 (管理员+农民+工作人员)
- 🌾 农田数量: 3个 (蔬菜区+粮食区+试验田)
- 🌱 作物数量: 5个 (各类蔬菜和粮食作物)
- 🌾 种植计划: 2个 (春季种植计划)
- 📝 种植记录: 3个 (播种和施肥记录)
- 📏 生长监测: 2个 (生长数据记录)
- 🔔 通知提醒: 3个 (各类农业提醒)
- 📊 分析报告: 3个 (专业分析报告)

### 使用建议
1. **首次运行**：先启动Spring Boot应用，让DataInitializer创建管理员用户
2. **初始化数据**：运行 `./initialize-system-data.sh` 脚本
3. **验证功能**：运行各个API测试脚本验证功能正常
4. **前端开发**：使用初始化数据进行前端界面开发和测试

### 注意事项
- 初始化脚本会跳过已存在的数据，不会重复创建
- 所有数据都是测试数据，可以根据实际需求修改
- 密码都是明文设置，生产环境请使用加密密码
- 日期数据设置为2024年，可以根据当前时间调整

#### 手动测试
使用curl命令手动测试API：
```bash
# 1. 登录获取token
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 2. 获取用户列表
curl -X GET "http://localhost:8080/api/users?page=1&size=5" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

详细的curl命令示例请参考 `API-TEST-COMMANDS.md` 文件。

## 部署

构建生产版本：
```bash
mvn clean package -DskipTests
```

生成的JAR文件位于 `target/` 目录下。
