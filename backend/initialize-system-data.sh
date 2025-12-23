#!/bin/bash

BASE_URL="http://localhost:8080"
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin123"

echo "=== 🌱 农业管理系统系统数据初始化脚本 ==="
echo "此脚本将为所有表创建初始化数据，确保按正确的顺序创建以维护数据关联性"
echo

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 打印带颜色的消息
print_success() {
    echo -e "${GREEN}✅ $1${NC}"
}

print_error() {
    echo -e "${RED}❌ $1${NC}"
}

print_info() {
    echo -e "${BLUE}ℹ️  $1${NC}"
}

print_warning() {
    echo -e "${YELLOW}⚠️  $1${NC}"
}

# 检查响应是否成功
check_response() {
    local response="$1"
    local expected_code="$2"
    local actual_code=$(echo "$response" | grep -o '"code":[0-9]*' | head -1 | cut -d':' -f2)

    if [ "$actual_code" = "$expected_code" ]; then
        print_success "响应代码正确: $actual_code"
        return 0
    else
        print_error "响应代码错误，期望: $expected_code，实际: $actual_code"
        return 1
    fi
}

# 1. 管理员登录获取token
echo "1. 🔐 管理员登录获取Token..."
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/login" \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"$ADMIN_USERNAME\",\"password\":\"$ADMIN_PASSWORD\"}")

if [ $? -eq 0 ] && echo "$LOGIN_RESPONSE" | grep -q '"code":200'; then
    print_success "登录成功"
    TOKEN=$(echo "$LOGIN_RESPONSE" | grep -o '"token":"[^"]*' | cut -d'"' -f4)
    echo "Token: ${TOKEN:0:30}..."
else
    print_error "登录失败，请确保Spring Boot应用正在运行且已创建初始用户"
    echo "如果还没有创建用户，请先运行应用让DataInitializer创建初始用户"
    echo "启动命令：cd backend && mvn spring-boot:run"
    exit 1
fi
echo

AUTH_HEADER="Authorization: Bearer $TOKEN"

# 2. 创建测试用户（除了已有的admin）
echo "2. 👥 创建测试用户..."
print_info "创建农民用户..."
FARMER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"farmer_zhang\",
    \"password\": \"farmer123\",
    \"realName\": \"张三\",
    \"email\": \"zhangsan@farm.com\",
    \"phone\": \"13800138001\",
    \"role\": \"FARMER\",
    \"status\": true
  }")

if [ $? -eq 0 ] && echo "$FARMER_RESPONSE" | grep -q '"code":200'; then
    print_success "创建农民用户成功"
    FARMER_ID=$(echo "$FARMER_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "农民用户ID: $FARMER_ID"
else
    print_warning "创建农民用户失败，可能已存在"
fi

print_info "创建工作人员用户..."
STAFF_RESPONSE=$(curl -s -X POST "$BASE_URL/api/users" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"username\": \"staff_li\",
    \"password\": \"staff123\",
    \"realName\": \"李四\",
    \"email\": \"lisi@farm.com\",
    \"phone\": \"13800138002\",
    \"role\": \"STAFF\",
    \"status\": true
  }")

if [ $? -eq 0 ] && echo "$STAFF_RESPONSE" | grep -q '"code":200'; then
    print_success "创建工作人员用户成功"
    STAFF_ID=$(echo "$STAFF_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "工作人员用户ID: $STAFF_ID"
else
    print_warning "创建工作人员用户失败，可能已存在"
fi

# 如果创建失败，尝试获取现有用户ID
if [ -z "$FARMER_ID" ]; then
    FARMER_LIST=$(curl -s -X GET "$BASE_URL/api/users?page=1&size=10&role=FARMER" -H "$AUTH_HEADER")
    FARMER_ID=$(echo "$FARMER_LIST" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    if [ -n "$FARMER_ID" ]; then
        print_info "使用现有农民用户ID: $FARMER_ID"
    fi
fi

if [ -z "$STAFF_ID" ]; then
    STAFF_LIST=$(curl -s -X GET "$BASE_URL/api/users?page=1&size=10&role=STAFF" -H "$AUTH_HEADER")
    STAFF_ID=$(echo "$STAFF_LIST" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    if [ -n "$STAFF_ID" ]; then
        print_info "使用现有工作人员用户ID: $STAFF_ID"
    fi
fi
echo

# 3. 创建农田数据
echo "3. 🌾 创建农田数据..."
print_info "创建蔬菜种植区农田..."
FARMLAND1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/farmlands" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"蔬菜种植区A\",
    \"area\": 5000.00,
    \"location\": \"农场东区蔬菜种植基地\",
    \"status\": \"AVAILABLE\",
    \"description\": \"专门用于蔬菜种植的现代化农田，土壤肥沃，水源充足，交通便利。面积5000平方米，目前可用于春季蔬菜种植。\"
  }")

if [ $? -eq 0 ] && echo "$FARMLAND1_RESPONSE" | grep -q '"code":200'; then
    print_success "创建蔬菜种植区农田成功"
    FARMLAND1_ID=$(echo "$FARMLAND1_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "农田ID: $FARMLAND1_ID"
else
    print_error "创建蔬菜种植区农田失败"
fi

print_info "创建粮食作物种植区农田..."
FARMLAND2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/farmlands" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"粮食种植区B\",
    \"area\": 10000.00,
    \"location\": \"农场西区粮食作物基地\",
    \"status\": \"AVAILABLE\",
    \"description\": \"大型粮食作物种植区，适合水稻、小麦等作物种植。面积10000平方米，土壤深厚，灌溉设施完善。\"
  }")

if [ $? -eq 0 ] && echo "$FARMLAND2_RESPONSE" | grep -q '"code":200'; then
    print_success "创建粮食种植区农田成功"
    FARMLAND2_ID=$(echo "$FARMLAND2_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "农田ID: $FARMLAND2_ID"
else
    print_error "创建粮食种植区农田失败"
fi

print_info "创建试验田..."
FARMLAND3_RESPONSE=$(curl -s -X POST "$BASE_URL/api/farmlands" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"试验田C\",
    \"area\": 2000.00,
    \"location\": \"农场南区试验基地\",
    \"status\": \"AVAILABLE\",
    \"description\": \"农业科研试验田，用于新品种试验和农业技术研究。面积2000平方米，配备现代化监测设备。\"
  }")

if [ $? -eq 0 ] && echo "$FARMLAND3_RESPONSE" | grep -q '"code":200'; then
    print_success "创建试验田成功"
    FARMLAND3_ID=$(echo "$FARMLAND3_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "农田ID: $FARMLAND3_ID"
else
    print_error "创建试验田失败"
fi
echo

# 4. 创建作物数据
echo "4. 🌱 创建作物数据..."
print_info "创建蔬菜作物..."
TOMATO_RESPONSE=$(curl -s -X POST "$BASE_URL/api/crops" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"番茄\",
    \"variety\": \"樱桃番茄\",
    \"plantingSeason\": \"春季\",
    \"growthPeriod\": 90,
    \"expectedYield\": 8000.00,
    \"waterNeeds\": \"每周2-3次灌溉，保持土壤湿润但不积水\",
    \"fertilizerNeeds\": \"有机肥+复合肥，每月施肥1次\",
    \"diseaseInfo\": \"易患晚疫病和病毒病，注意通风和病害防治\",
    \"description\": \"樱桃番茄，果实小巧甜美，适合鲜食和加工。生长周期约90天，对土壤和气候要求较高。\"
  }")

if [ $? -eq 0 ] && echo "$TOMATO_RESPONSE" | grep -q '"code":200'; then
    print_success "创建番茄作物成功"
    TOMATO_ID=$(echo "$TOMATO_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "作物ID: $TOMATO_ID"
else
    print_error "创建番茄作物失败"
fi

CUCUMBER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/crops" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"黄瓜\",
    \"variety\": \"温室黄瓜\",
    \"plantingSeason\": \"春季\",
    \"growthPeriod\": 75,
    \"expectedYield\": 6000.00,
    \"waterNeeds\": \"每日适量灌溉，保持空气湿度60-70%\",
    \"fertilizerNeeds\": \"氮肥为主，每周施肥1次\",
    \"diseaseInfo\": \"易患白粉病和细菌性角斑病，注意温度控制\",
    \"description\": \"温室黄瓜，品质优良，适合全年种植。生长周期约75天，需要较高的温度和湿度。\"
  }")

if [ $? -eq 0 ] && echo "$CUCUMBER_RESPONSE" | grep -q '"code":200'; then
    print_success "创建黄瓜作物成功"
    CUCUMBER_ID=$(echo "$CUCUMBER_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "作物ID: $CUCUMBER_ID"
else
    print_error "创建黄瓜作物失败"
fi

print_info "创建粮食作物..."
RICE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/crops" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"水稻\",
    \"variety\": \"杂交水稻\",
    \"plantingSeason\": \"春季\",
    \"growthPeriod\": 120,
    \"expectedYield\": 800.00,
    \"waterNeeds\": \"需水量大，生长前期保持浅水层，后期适当晾田\",
    \"fertilizerNeeds\": \"基肥+追肥，以氮磷钾复合肥为主\",
    \"diseaseInfo\": \"易患稻瘟病和纹枯病，注意田间管理\",
    \"description\": \"优质杂交水稻，高产稳产。生长周期约120天，对水分和温度要求严格。\"
  }")

if [ $? -eq 0 ] && echo "$RICE_RESPONSE" | grep -q '"code":200'; then
    print_success "创建水稻作物成功"
    RICE_ID=$(echo "$RICE_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "作物ID: $RICE_ID"
else
    print_error "创建水稻作物失败"
fi

WHEAT_RESPONSE=$(curl -s -X POST "$BASE_URL/api/crops" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"小麦\",
    \"variety\": \"强筋小麦\",
    \"plantingSeason\": \"秋季\",
    \"growthPeriod\": 240,
    \"expectedYield\": 600.00,
    \"waterNeeds\": \"冬春灌溉为主，生长后期控制水分\",
    \"fertilizerNeeds\": \"磷钾肥为主，氮肥适量\",
    \"diseaseInfo\": \"易患白粉病和锈病，注意种子处理\",
    \"description\": \"优质强筋小麦，适合制作面粉。生长周期约240天，需经历冬春季节。\"
  }")

if [ $? -eq 0 ] && echo "$WHEAT_RESPONSE" | grep -q '"code":200'; then
    print_success "创建小麦作物成功"
    WHEAT_ID=$(echo "$WHEAT_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "作物ID: $WHEAT_ID"
else
    print_error "创建小麦作物失败"
fi

print_info "创建经济作物..."
CORN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/crops" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"name\": \"玉米\",
    \"variety\": \"甜玉米\",
    \"plantingSeason\": \"春季\",
    \"growthPeriod\": 100,
    \"expectedYield\": 1200.00,
    \"waterNeeds\": \"生育前期需水较多，后期控制水分\",
    \"fertilizerNeeds\": \"氮磷钾均衡，重视磷钾肥\",
    \"diseaseInfo\": \"易患大斑病和丝黑穗病，注意种子消毒\",
    \"description\": \"优质甜玉米，口感鲜美。生长周期约100天，适应性强，产量较高。\"
  }")

if [ $? -eq 0 ] && echo "$CORN_RESPONSE" | grep -q '"code":200'; then
    print_success "创建玉米作物成功"
    CORN_ID=$(echo "$CORN_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "作物ID: $CORN_ID"
else
    print_error "创建玉米作物失败"
fi
echo

# 5. 创建种植计划数据
echo "5. 🌾 创建种植计划数据..."
if [ -n "$FARMLAND1_ID" ] && [ -n "$TOMATO_ID" ]; then
    print_info "创建番茄种植计划..."
    PLAN1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/planting-plans" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"farmlandId\": $FARMLAND1_ID,
        \"cropId\": $TOMATO_ID,
        \"planName\": \"2024年春季樱桃番茄种植计划\",
        \"plannedStartDate\": \"2024-04-01\",
        \"plannedEndDate\": \"2024-06-30\",
        \"expectedHarvestDate\": \"2024-07-15\",
        \"sowingDensity\": \"每亩4000-5000株\",
        \"notes\": \"采用温室大棚种植，预计总产量40000kg\",
        \"status\": \"COMPLETED\"
      }")

    if [ $? -eq 0 ] && echo "$PLAN1_RESPONSE" | grep -q '"code":200'; then
        print_success "创建番茄种植计划成功"
        PLAN1_ID=$(echo "$PLAN1_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        echo "种植计划ID: $PLAN1_ID"
    else
        print_error "创建番茄种植计划失败"
        echo "响应内容: $PLAN1_RESPONSE"
    fi
fi

if [ -n "$FARMLAND2_ID" ] && [ -n "$RICE_ID" ]; then
    print_info "创建水稻种植计划..."
    PLAN2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/planting-plans" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"farmlandId\": $FARMLAND2_ID,
        \"cropId\": $RICE_ID,
        \"planName\": \"2024年春季杂交水稻种植计划\",
        \"plannedStartDate\": \"2024-04-15\",
        \"plannedEndDate\": \"2024-08-15\",
        \"expectedHarvestDate\": \"2024-08-30\",
        \"sowingDensity\": \"每亩20000-25000穴\",
        \"notes\": \"采用抛秧机种植，预计总产量8000kg\",
        \"status\": \"COMPLETED\"
      }")

    if [ $? -eq 0 ] && echo "$PLAN2_RESPONSE" | grep -q '"code":200'; then
        print_success "创建水稻种植计划成功"
        PLAN2_ID=$(echo "$PLAN2_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        echo "种植计划ID: $PLAN2_ID"
    else
        print_error "创建水稻种植计划失败"
        echo "响应内容: $PLAN2_RESPONSE"
    fi
fi
echo

# 6. 创建种植记录数据
echo "6. 📝 创建种植记录数据..."
if [ -n "$PLAN1_ID" ]; then
    print_info "创建番茄播种记录..."
    RECORD1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/planting-records" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"planId\": $PLAN1_ID,
        \"operationType\": \"SOWING\",
        \"operationDate\": \"2024-04-05\",
        \"quantityUsed\": 2000.00,
        \"weatherConditions\": \"晴天，温度18°C\",
        \"notes\": \"使用营养钵育苗，种子质量良好，发芽率95%\"
      }")

    if [ $? -eq 0 ] && echo "$RECORD1_RESPONSE" | grep -q '"code":200'; then
        print_success "创建番茄播种记录成功"
        RECORD1_ID=$(echo "$RECORD1_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        echo "种植记录ID: $RECORD1_ID"
    else
        print_error "创建番茄播种记录失败"
        echo "响应内容: $RECORD1_RESPONSE"
    fi

    print_info "创建番茄施肥记录..."
    RECORD2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/planting-records" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"planId\": $PLAN1_ID,
        \"operationType\": \"FERTILIZING\",
        \"operationDate\": \"2024-04-10\",
        \"quantityUsed\": 150.00,
        \"weatherConditions\": \"多云，温度22°C\",
        \"notes\": \"施用有机复合肥，促进幼苗生长\"
      }")

    if [ $? -eq 0 ] && echo "$RECORD2_RESPONSE" | grep -q '"code":200'; then
        print_success "创建番茄施肥记录成功"
    else
        print_error "创建番茄施肥记录失败"
        echo "响应内容: $RECORD2_RESPONSE"
    fi
fi

if [ -n "$PLAN2_ID" ]; then
    print_info "创建水稻播种记录..."
    RECORD3_RESPONSE=$(curl -s -X POST "$BASE_URL/api/planting-records" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"planId\": $PLAN2_ID,
        \"operationType\": \"SOWING\",
        \"operationDate\": \"2024-04-20\",
        \"quantityUsed\": 500.00,
        \"weatherConditions\": \"晴天，温度25°C\",
        \"notes\": \"使用抛秧机进行机插秧，秧苗质量优良\"
      }")

    if [ $? -eq 0 ] && echo "$RECORD3_RESPONSE" | grep -q '"code":200'; then
        print_success "创建水稻播种记录成功"
    else
        print_error "创建水稻播种记录失败"
        echo "响应内容: $RECORD3_RESPONSE"
    fi
fi
echo

# 7. 创建生长监测数据
echo "7. 📏 创建生长监测数据..."
if [ -n "$PLAN1_ID" ]; then
    print_info "创建番茄生长监测记录..."
    MONITORING1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/growth-monitoring" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"planId\": $PLAN1_ID,
        \"monitoringDate\": \"2024-04-15\",
        \"heightMeasurement\": 15.5,
        \"widthMeasurement\": 8.2,
        \"healthStatus\": \"EXCELLENT\",
        \"soilMoisture\": 68.5,
        \"temperature\": 24.5,
        \"humidity\": 65.2,
        \"lightIntensity\": 85000,
        \"phLevel\": 6.8,
        \"notes\": \"幼苗生长良好，叶片肥厚绿润，无病虫害迹象\",
        \"photoUrl\": \"https://example.com/monitoring/tomato-001.jpg\"
      }")

    if [ $? -eq 0 ] && echo "$MONITORING1_RESPONSE" | grep -q '"code":200'; then
        print_success "创建番茄生长监测记录成功"
        MONITORING1_ID=$(echo "$MONITORING1_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        echo "生长监测ID: $MONITORING1_ID"
    else
        print_error "创建番茄生长监测记录失败"
        echo "响应内容: $MONITORING1_RESPONSE"
    fi
fi

if [ -n "$PLAN2_ID" ]; then
    print_info "创建水稻生长监测记录..."
    MONITORING2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/growth-monitoring" \
      -H "$AUTH_HEADER" \
      -H "Content-Type: application/json" \
      -d "{
        \"planId\": $PLAN2_ID,
        \"monitoringDate\": \"2024-04-25\",
        \"heightMeasurement\": 12.8,
        \"widthMeasurement\": 6.5,
        \"healthStatus\": \"GOOD\",
        \"soilMoisture\": 72.3,
        \"temperature\": 26.8,
        \"humidity\": 78.5,
        \"lightIntensity\": 92000,
        \"phLevel\": 6.9,
        \"notes\": \"秧苗返青良好，分蘖开始，根系发育正常\",
        \"photoUrl\": \"https://example.com/monitoring/rice-001.jpg\"
      }")

    if [ $? -eq 0 ] && echo "$MONITORING2_RESPONSE" | grep -q '"code":200'; then
        print_success "创建水稻生长监测记录成功"
    else
        print_error "创建水稻生长监测记录失败"
        echo "响应内容: $MONITORING2_RESPONSE"
    fi
fi
echo

# 8. 创建通知提醒数据
echo "8. 🔔 创建通知提醒数据..."
print_info "创建施肥提醒通知..."
NOTIFICATION1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/notifications" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"樱桃番茄施肥提醒\",
    \"content\": \"亲爱的用户，您的樱桃番茄种植计划需要进行施肥操作，请及时安排氮磷钾复合肥施用工作。\",
    \"type\": \"FERTILIZING\",
    \"priority\": \"MEDIUM\",
    \"scheduledTime\": \"2026-04-25 09:00:00\",
    \"status\": \"PENDING\",
    \"recipientIds\": [1, 4, 5]
  }")

if [ $? -eq 0 ] && echo "$NOTIFICATION1_RESPONSE" | grep -q '"code":200'; then
    print_success "创建施肥提醒通知成功"
    NOTIFICATION1_ID=$(echo "$NOTIFICATION1_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "通知ID: $NOTIFICATION1_ID"
else
    print_error "创建施肥提醒通知失败"
    echo "响应内容: $NOTIFICATION1_RESPONSE"
fi

print_info "创建灌溉提醒通知..."
NOTIFICATION2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/notifications" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"水稻灌溉提醒\",
    \"content\": \"检测到土壤湿度偏低，建议对水稻种植区进行适量灌溉，保持适宜水分条件。\",
    \"type\": \"WATERING\",
    \"priority\": \"HIGH\",
    \"scheduledTime\": \"2026-04-26 08:00:00\",
    \"status\": \"PENDING\",
    \"recipientIds\": [1, 4]
  }")

if [ $? -eq 0 ] && echo "$NOTIFICATION2_RESPONSE" | grep -q '"code":200'; then
    print_success "创建灌溉提醒通知成功"
else
    print_error "创建灌溉提醒通知失败"
    echo "响应内容: $NOTIFICATION2_RESPONSE"
fi

print_info "创建生长监测提醒通知..."
NOTIFICATION3_RESPONSE=$(curl -s -X POST "$BASE_URL/api/notifications" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"作物生长监测提醒\",
    \"content\": \"请及时对种植作物进行生长状况监测，记录生长数据和健康状态。\",
    \"type\": \"MONITORING\",
    \"priority\": \"LOW\",
    \"scheduledTime\": \"2026-04-30 10:00:00\",
    \"status\": \"PENDING\",
    \"recipientIds\": [1, 5]
  }")

if [ $? -eq 0 ] && echo "$NOTIFICATION3_RESPONSE" | grep -q '"code":200'; then
    print_success "创建生长监测提醒通知成功"
else
    print_error "创建生长监测提醒通知失败"
    echo "响应内容: $NOTIFICATION3_RESPONSE"
fi
echo

# 9. 创建数据分析报告数据
echo "9. 📊 创建数据分析报告数据..."
print_info "创建作物生长分析报告..."
REPORT1_RESPONSE=$(curl -s -X POST "$BASE_URL/api/reports" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"2024年第一季度作物生长分析报告\",
    \"reportType\": \"CROP_GROWTH\",
    \"startDate\": \"2024-01-01\",
    \"endDate\": \"2024-03-31\",
    \"content\": \"本报告分析了第一季度各类作物的生长情况。樱桃番茄生长状况良好，平均高度15.5cm，健康状态优秀。水稻秧苗返青正常，分蘖开始，整体生长符合预期。建议继续加强田间管理和病虫害防治工作。\",
    \"chartData\": \"{\\\"growthTrend\\\": [{\\\"date\\\": \\\"2024-01-01\\\", \\\"tomato\\\": 12.5, \\\"rice\\\": 10.2}, {\\\"date\\\": \\\"2024-02-01\\\", \\\"tomato\\\": 14.8, \\\"rice\\\": 11.5}, {\\\"date\\\": \\\"2024-03-01\\\", \\\"tomato\\\": 15.5, \\\"rice\\\": 12.8}], \\\"healthDistribution\\\": [{\\\"status\\\": \\\"EXCELLENT\\\", \\\"count\\\": 8}, {\\\"status\\\": \\\"GOOD\\\", \\\"count\\\": 5}, {\\\"status\\\": \\\"FAIR\\\", \\\"count\\\": 2}]}\"
  }")

if [ $? -eq 0 ] && echo "$REPORT1_RESPONSE" | grep -q '"code":200'; then
    print_success "创建作物生长分析报告成功"
    REPORT1_ID=$(echo "$REPORT1_RESPONSE" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    echo "报告ID: $REPORT1_ID"
else
    print_error "创建作物生长分析报告失败"
    echo "响应内容: $REPORT1_RESPONSE"
fi

print_info "创建农田利用率分析报告..."
REPORT2_RESPONSE=$(curl -s -X POST "$BASE_URL/api/reports" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"2024年农田利用率分析报告\",
    \"reportType\": \"FIELD_UTILIZATION\",
    \"startDate\": \"2024-01-01\",
    \"endDate\": \"2024-03-31\",
    \"content\": \"截至2024年3月底，农场总耕地面积17000平方米，已利用面积15000平方米，利用率达88.2%。其中蔬菜种植区利用率95%，粮食种植区利用率85%，试验田利用率90%。整体利用情况良好，但仍需提高粮食作物的种植效率。\",
    \"chartData\": \"{\\\"utilizationRate\\\": [{\\\"field\\\": \\\"蔬菜种植区A\\\", \\\"rate\\\": 95}, {\\\"field\\\": \\\"粮食种植区B\\\", \\\"rate\\\": 85}, {\\\"field\\\": \\\"试验田C\\\", \\\"rate\\\": 90}], \\\"cropDistribution\\\": [{\\\"crop\\\": \\\"番茄\\\", \\\"area\\\": 5000}, {\\\"crop\\\": \\\"水稻\\\", \\\"area\\\": 10000}, {\\\"crop\\\": \\\"其他\\\", \\\"area\\\": 2000}]}\"
  }")

if [ $? -eq 0 ] && echo "$REPORT2_RESPONSE" | grep -q '"code":200'; then
    print_success "创建农田利用率分析报告成功"
else
    print_error "创建农田利用率分析报告失败"
    echo "响应内容: $REPORT2_RESPONSE"
fi

print_info "创建天气影响分析报告..."
REPORT3_RESPONSE=$(curl -s -X POST "$BASE_URL/api/reports" \
  -H "$AUTH_HEADER" \
  -H "Content-Type: application/json" \
  -d "{
    \"title\": \"2024年第一季度天气对农业生产影响分析报告\",
    \"reportType\": \"WEATHER_IMPACT\",
    \"startDate\": \"2024-01-01\",
    \"endDate\": \"2024-03-31\",
    \"content\": \"第一季度整体天气状况良好，降雨适中，温度适宜，对作物生长有利。1月份低温期对番茄生长有一定影响，但通过温室调控得到有效控制。水稻育秧期间天气条件适宜，发芽率达到95%以上。预计第二季度天气将继续保持良好态势。\",
    \"chartData\": \"{\\\"temperatureTrend\\\": [{\\\"month\\\": \\\"1月\\\", \\\"avgTemp\\\": 8.5}, {\\\"month\\\": \\\"2月\\\", \\\"avgTemp\\\": 12.3}, {\\\"month\\\": \\\"3月\\\", \\\"avgTemp\\\": 18.7}], \\\"rainfall\\\": [{\\\"month\\\": \\\"1月\\\", \\\"rainfall\\\": 25.5}, {\\\"month\\\": \\\"2月\\\", \\\"rainfall\\\": 35.2}, {\\\"month\\\": \\\"3月\\\", \\\"rainfall\\\": 45.8}]}\"
  }")

if [ $? -eq 0 ] && echo "$REPORT3_RESPONSE" | grep -q '"code":200'; then
    print_success "创建天气影响分析报告成功"
else
    print_error "创建天气影响分析报告失败"
    echo "响应内容: $REPORT3_RESPONSE"
fi
echo

# 10. 统计和验证
echo "10. 📈 数据初始化统计..."

echo
print_info "=== 初始化数据统计 ==="

# 用户统计
USER_COUNT=$(curl -s -X GET "$BASE_URL/api/users?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "👥 用户数量: $USER_COUNT"

# 农田统计
FARMLAND_COUNT=$(curl -s -X GET "$BASE_URL/api/farmlands?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "🌾 农田数量: $FARMLAND_COUNT"

# 作物统计
CROP_COUNT=$(curl -s -X GET "$BASE_URL/api/crops?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "🌱 作物数量: $CROP_COUNT"

# 种植计划统计
PLAN_COUNT=$(curl -s -X GET "$BASE_URL/api/planting-plans?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "🌾 种植计划数量: $PLAN_COUNT"

# 种植记录统计
RECORD_COUNT=$(curl -s -X GET "$BASE_URL/api/planting-records?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "📝 种植记录数量: $RECORD_COUNT"

# 生长监测统计
MONITORING_COUNT=$(curl -s -X GET "$BASE_URL/api/growth-monitoring?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "📏 生长监测数量: $MONITORING_COUNT"

# 通知统计
NOTIFICATION_COUNT=$(curl -s -X GET "$BASE_URL/api/notifications?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "🔔 通知数量: $NOTIFICATION_COUNT"

# 报告统计
REPORT_COUNT=$(curl -s -X GET "$BASE_URL/api/reports?page=1&size=100" -H "$AUTH_HEADER" | grep -o '"id":[0-9]*' | wc -l)
echo "📊 报告数量: $REPORT_COUNT"

echo
print_success "🎉 系统数据初始化完成！"
echo
print_info "初始化数据包括："
echo "  👥 用户数据：管理员、农民、工作人员"
echo "  🌾 农田数据：蔬菜区、粮食区、试验田"
echo "  🌱 作物数据：番茄、黄瓜、水稻、小麦、玉米"
echo "  🌾 种植计划：春季蔬菜和粮食种植计划"
echo "  📝 种植记录：播种、施肥等操作记录"
echo "  📏 生长监测：作物生长数据和环境参数"
echo "  🔔 通知提醒：施肥、灌溉、监测等提醒"
echo "  📊 分析报告：生长分析、利用率分析、天气影响分析"
echo
print_info "现在您可以运行各个API测试脚本来验证功能："
echo "  ./test-user-api.sh           # 用户管理API测试"
echo "  ./test-crop-api.sh           # 作物管理API测试"
echo "  ./test-farmland-api.sh       # 农田管理API测试"
echo "  ./test-planting-plan-api.sh  # 种植计划API测试"
echo "  ./test-planting-record-api.sh # 种植记录API测试"
echo "  ./test-growth-monitoring-api.sh # 生长监测API测试"
echo "  ./test-notification-api.sh   # 通知管理API测试"
echo "  ./test-report-api.sh         # 报告管理API测试"
echo
print_success "🚀 系统已准备就绪，可以开始使用！"
