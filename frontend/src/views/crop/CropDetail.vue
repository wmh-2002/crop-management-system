<template>
  <div class="crop-detail-container">
    <div class="page-title">
      <h2>作物详情</h2>
      <el-button @click="goBack" type="primary" plain>返回列表</el-button>
    </div>

    <el-card v-loading="loading" class="detail-card">
      <template #header>
        <div class="card-header">
          <span>{{ crop.name }}</span>
          <el-tag type="success" v-if="crop.plantingSeason">{{ crop.plantingSeason }}</el-tag>
        </div>
      </template>

      <el-row :gutter="20">
        <el-col :span="12">
          <div class="detail-section">
            <h3>基本信息</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="作物名称">
                {{ crop.name || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="作物品种">
                {{ crop.variety || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="种植季节">
                {{ crop.plantingSeason || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="生长期">
                {{ crop.growthPeriod ? crop.growthPeriod + ' 天' : '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="预期产量">
                {{ crop.expectedYield ? crop.expectedYield + ' kg' : '暂无' }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>

        <el-col :span="12">
          <div class="detail-section">
            <h3>生长条件</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="水分需求">
                {{ crop.waterNeeds || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="肥料需求">
                {{ crop.fertilizerNeeds || '暂无' }}
              </el-descriptions-item>
              <el-descriptions-item label="病虫害信息">
                {{ crop.diseaseInfo || '暂无' }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="24">
          <div class="detail-section">
            <h3>作物描述</h3>
            <el-card shadow="never">
              <p style="line-height: 1.6; margin: 0;">
                {{ crop.description || '暂无描述' }}
              </p>
            </el-card>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" style="margin-top: 20px;">
        <el-col :span="12">
          <div class="detail-section">
            <h3>时间信息</h3>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="创建时间">
                {{ formatDate(crop.createdAt) }}
              </el-descriptions-item>
              <el-descriptions-item label="更新时间">
                {{ formatDate(crop.updatedAt) }}
              </el-descriptions-item>
            </el-descriptions>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="detail-section">
            <h3>操作</h3>
            <el-space>
              <el-button type="primary" @click="handleEdit">编辑作物</el-button>
              <el-button type="danger" @click="handleDelete">删除作物</el-button>
            </el-space>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCropById, deleteCrop } from '@/api/crop'

export default {
  name: 'CropDetail',
  setup() {
    const route = useRoute()
    const router = useRouter()
    const loading = ref(false)
    const crop = ref({})

    // 获取作物详情
    const fetchCropDetail = async () => {
      const cropId = route.params.id
      if (!cropId) {
        ElMessage.error('作物ID不存在')
        router.push('/crops')
        return
      }

      loading.value = true
      try {
        console.log('获取作物详情:', cropId)
        const response = await getCropById(cropId)
        console.log('作物详情响应:', response)

        if (response.data && response.data.code === 200 && response.data.data) {
          crop.value = response.data.data
        } else {
          ElMessage.error('获取作物详情失败')
          router.push('/crops')
        }
      } catch (error) {
        console.error('获取作物详情失败:', error)
        ElMessage.error('获取作物详情失败')
        router.push('/crops')
      } finally {
        loading.value = false
      }
    }

    // 格式化日期
    const formatDate = (dateString) => {
      if (!dateString) return '暂无'
      const date = new Date(dateString)
      return date.toLocaleString('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
        second: '2-digit'
      })
    }

    // 返回列表
    const goBack = () => {
      router.push('/crops')
    }

    // 编辑作物
    const handleEdit = () => {
      router.push(`/crops/edit/${crop.value.id}`)
    }

    // 删除作物
    const handleDelete = () => {
      ElMessageBox.confirm('此操作将永久删除该作物, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteCrop(crop.value.id)
          ElMessage.success('删除成功')
          router.push('/crops')
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败: ' + (error.response?.data?.message || error.message || '网络错误'))
        }
      })
    }

    onMounted(() => {
      fetchCropDetail()
    })

    return {
      loading,
      crop,
      formatDate,
      goBack,
      handleEdit,
      handleDelete
    }
  }
}
</script>

<style scoped>
.page-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.detail-card {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.detail-section {
  margin-bottom: 20px;
}

.detail-section h3 {
  margin: 0 0 15px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

:deep(.el-descriptions__title) {
  font-size: 14px;
  font-weight: 600;
}
</style>
