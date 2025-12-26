-- 农业管理系统数据库设计
-- 用户表
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码',
    real_name VARCHAR(100) NOT NULL COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    role ENUM('ADMIN', 'FARMER', 'STAFF') NOT NULL COMMENT '用户角色',
    status TINYINT DEFAULT 1 COMMENT '用户状态：1-启用，0-禁用',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 农田表
CREATE TABLE farmlands (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '农田名称',
    area DECIMAL(10,2) NOT NULL COMMENT '面积（平方米）',
    location VARCHAR(255) COMMENT '位置',
    status ENUM('AVAILABLE', 'PLANTED', 'MAINTENANCE') DEFAULT 'AVAILABLE' COMMENT '农田状态',
    description TEXT COMMENT '农田描述',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- 作物表
CREATE TABLE crops (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '作物名称',
    variety VARCHAR(100) COMMENT '作物品种',
    crop_category ENUM('VEGETABLES', 'GRAINS', 'ECONOMIC_CROPS', 'OTHER') DEFAULT 'OTHER' COMMENT '作物类别：VEGETABLES-蔬菜类，GRAINS-粮食类，ECONOMIC_CROPS-经济作物，OTHER-其它',
    planting_season VARCHAR(50) COMMENT '种植季节',
    growth_period INT COMMENT '生长期（天）',
    expected_yield DECIMAL(10,2) COMMENT '预期产量',
    water_needs VARCHAR(255) COMMENT '水分需求',
    fertilizer_needs VARCHAR(255) COMMENT '肥料需求',
    disease_info TEXT COMMENT '病虫害信息',
    description TEXT COMMENT '作物描述',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

-- 种植计划表
CREATE TABLE planting_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    farmland_id BIGINT NOT NULL COMMENT '农田ID',
    crop_id BIGINT NOT NULL COMMENT '作物ID',
    plan_name VARCHAR(100) NOT NULL COMMENT '计划名称',
    planned_start_date DATE NOT NULL COMMENT '计划开始日期',
    planned_end_date DATE NOT NULL COMMENT '计划结束日期',
    expected_harvest_date DATE COMMENT '预期收获日期',
    sowing_density VARCHAR(255) COMMENT '播种密度',
    notes TEXT COMMENT '备注',
    status ENUM('PLANNED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') DEFAULT 'PLANNED' COMMENT '计划状态',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (farmland_id) REFERENCES farmlands(id),
    FOREIGN KEY (crop_id) REFERENCES crops(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- 种植记录表
CREATE TABLE planting_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL COMMENT '种植计划ID',
    operation_type ENUM('SOWING', 'FERTILIZING', 'WATERING', 'PESTICIDING', 'HARVESTING') NOT NULL COMMENT '操作类型',
    operation_date DATE NOT NULL COMMENT '操作日期',
    quantity_used DECIMAL(10,2) COMMENT '用量',
    operator_id BIGINT COMMENT '操作员ID',
    weather_conditions VARCHAR(255) COMMENT '天气条件',
    notes TEXT COMMENT '备注',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (plan_id) REFERENCES planting_plans(id),
    FOREIGN KEY (operator_id) REFERENCES users(id)
);

-- 生长监控表
CREATE TABLE growth_monitoring (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL COMMENT '种植计划ID',
    monitoring_date DATE NOT NULL COMMENT '监测日期',
    height_measurement DECIMAL(8,2) COMMENT '高度测量（厘米）',
    width_measurement DECIMAL(8,2) COMMENT '宽度测量（厘米）',
    health_status ENUM('EXCELLENT', 'GOOD', 'FAIR', 'POOR') COMMENT '健康状态',
    soil_moisture DECIMAL(5,2) COMMENT '土壤湿度（%）',
    temperature DECIMAL(5,2) COMMENT '温度（摄氏度）',
    humidity DECIMAL(5,2) COMMENT '湿度（%）',
    light_intensity INT COMMENT '光照强度（lux）',
    ph_level DECIMAL(3,2) COMMENT 'pH值',
    notes TEXT COMMENT '备注',
    photo_url VARCHAR(500) COMMENT '照片URL',
    created_by BIGINT COMMENT '创建人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (plan_id) REFERENCES planting_plans(id),
    FOREIGN KEY (created_by) REFERENCES users(id)
);

-- 通知提醒表
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    type ENUM('FERTILIZING', 'WATERING', 'PESTICIDING', 'MONITORING', 'HARVESTING', 'OTHER') NOT NULL COMMENT '通知类型',
    priority ENUM('LOW', 'MEDIUM', 'HIGH') DEFAULT 'MEDIUM' COMMENT '优先级',
    scheduled_time DATETIME NOT NULL COMMENT '计划发送时间',
    sent_time TIMESTAMP NULL COMMENT '实际发送时间',
    status ENUM('PENDING', 'SENT', 'READ') DEFAULT 'PENDING' COMMENT '状态',
    recipient_ids JSON COMMENT '接收者ID列表',
    plan_id BIGINT COMMENT '关联的种植计划ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (plan_id) REFERENCES planting_plans(id)
);

-- 数据分析报告表
CREATE TABLE reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL COMMENT '报告标题',
    report_type ENUM('CROP_GROWTH', 'FIELD_UTILIZATION', 'PRODUCTION_ANALYSIS', 'WEATHER_IMPACT', 'PREDICTIVE_ANALYSIS') NOT NULL COMMENT '报告类型',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    content LONGTEXT COMMENT '报告内容',
    chart_data JSON COMMENT '图表数据',
    generated_by BIGINT COMMENT '生成人ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    FOREIGN KEY (generated_by) REFERENCES users(id)
);

-- 创建索引以提高查询性能
CREATE INDEX idx_farmlands_status ON farmlands(status);
CREATE INDEX idx_planting_plans_status ON planting_plans(status);
CREATE INDEX idx_planting_plans_dates ON planting_plans(planned_start_date, planned_end_date);
CREATE INDEX idx_planting_records_plan_id ON planting_records(plan_id);
CREATE INDEX idx_growth_monitoring_plan_id ON growth_monitoring(plan_id);
CREATE INDEX idx_growth_monitoring_date ON growth_monitoring(monitoring_date);
CREATE INDEX idx_notifications_scheduled ON notifications(scheduled_time, status);