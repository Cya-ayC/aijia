<template>
  <div class="dashboard-container">
    <!-- 1. 顶部统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="always" class="status-card blue">
          <div class="label">总房产数</div>
          <div class="value">
            {{ stats.totalRooms }} <span class="unit">套</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always" class="status-card green">
          <div class="label">入住业主</div>
          <div class="value">
            {{ stats.activeOwners }} <span class="unit">户</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always" class="status-card orange">
          <div class="label">本年累计收入</div>
          <div class="value">¥ {{ stats.monthIncome || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="always" class="status-card red">
          <div class="label">待处理报修</div>
          <div class="value">
            {{ stats.pendingRepairs }} <span class="unit">条</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. 中间内容区 -->
    <el-row :gutter="20" style="margin-top: 20px">
      <!-- 左侧：最新报修动态 -->
      <el-col :span="16">
        <el-card header="最新报修动态">
          <el-table :data="recentRepairs" stripe style="width: 100%">
            <el-table-column prop="createTime" label="时间" width="180" />
            <el-table-column prop="roomNum" label="房号" width="100" />
            <el-table-column
              prop="content"
              label="报修内容"
              show-overflow-tooltip
            />
            <el-table-column label="状态" width="100">
              <template #default="{ row }">
                <el-tag type="danger" effect="dark" size="small">待处理</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- 右侧：快捷入口 -->
      <el-col :span="8">
        <el-card header="快捷操作">
          <div class="quick-links">
            <el-button type="primary" @click="$router.push('/bill')"
              >物业费收缴</el-button
            >
            <el-button type="success" @click="$router.push('/repair')"
              >报修派单</el-button
            >
            <el-button type="warning" @click="$router.push('/notice')"
              >发布公告</el-button
            >
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>
<script setup>
import { ref, onMounted } from "vue";
import axios from "@/utils/request"; // 确保你的 axios 路径正确

// 1. 初始化统计数据，给个默认值防止页面报错
const stats = ref({
  totalRooms: 0,
  activeOwners: 0,
  monthIncome: 0,
  pendingRepairs: 0,
});

// 2. 最新报修列表数据
const recentRepairs = ref([]);

// 获取顶部四个卡片的汇总数据
const getStats = async () => {
  try {
    const { data } = await axios.get("/dashboard/status");
    stats.value = data;
  } catch (error) {
    console.error("获取统计数据失败", error);
  }
};

// 获取最新的待处理报修（复用之前的报修分页接口，只要第一页的前5条）
const getRecentRepairs = async () => {
  try {
    const { data } = await axios.get("/repair/page", {
      params: {
        current: 1,
        size: 5,
        status: "PENDING", // 只看待处理的
      },
    });
    recentRepairs.value = data.records;
  } catch (error) {
    console.error("获取最新报修失败", error);
  }
};

// 页面加载时执行
onMounted(() => {
  getStats();
  getRecentRepairs();
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}
.status-card {
  color: #fff;
  border: none;
}
.status-card .label {
  font-size: 14px;
  opacity: 0.8;
}
.status-card .value {
  font-size: 28px;
  font-weight: bold;
  margin-top: 10px;
}
.status-card .unit {
  font-size: 14px;
}

.blue {
  background: linear-gradient(135deg, #1890ff 0%, #36cfc9 100%);
}
.green {
  background: linear-gradient(135deg, #52c41a 0%, #95de64 100%);
}
.orange {
  background: linear-gradient(135deg, #faad14 0%, #ffec3d 100%);
}
.red {
  background: linear-gradient(135deg, #f5222d 0%, #ff7875 100%);
}

.quick-links {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
</style>
