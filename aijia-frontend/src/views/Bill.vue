<template>
  <div class="app-container">
    <!-- 🚀 新增：业主房屋信息卡片 (仅业主可见) -->
    <el-row :gutter="20" v-if="roleKey === 'OWNER'" style="margin-bottom: 20px">
      <el-col :span="24">
        <el-card shadow="never" class="my-room-card">
          <template #header>
            <div class="card-header">
              <el-icon><House /></el-icon>
              <span style="margin-left: 10px; font-weight: bold"
                >我的房产信息</span
              >
            </div>
          </template>
          <el-descriptions :column="3" border>
            <el-descriptions-item label="所属楼栋">{{
              myRoom.buildingName
            }}</el-descriptions-item>
            <el-descriptions-item label="单元房号"
              >{{ myRoom.unitName }} -
              {{ myRoom.roomNum }}</el-descriptions-item
            >
            <el-descriptions-item label="建筑面积"
              >{{ myRoom.area }} m²</el-descriptions-item
            >
            <el-descriptions-item label="起收日期">
              <el-tag size="small">{{
                myRoom.deliveryDate || "未设置"
              }}</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="当前状态">
              <el-tag type="success" size="small">已入住</el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="备注">{{
              myRoom.remark || "无"
            }}</el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>

    <!-- 1. 顶部财务统计卡片 (仅管理员可见) -->
    <el-row
      :gutter="20"
      class="report-cards"
      v-if="roleKey !== 'OWNER'"
      style="margin-bottom: 20px"
    >
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>应收总额 ({{ queryDTO.year }}年)</template>
          <div class="money">¥ {{ reportData.totalAmount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>实收总额</template>
          <div class="money success" style="color: #67c23a">
            ¥ {{ reportData.paidAmount || 0 }}
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <template #header>收缴率</template>
          <el-progress
            type="circle"
            :percentage="reportData.payRate || 0"
            :width="80"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. 搜索栏 -->
    <el-card class="filter-container" style="margin-bottom: 15px">
      <el-form :inline="true" :model="queryDTO" class="search-form">
        <el-form-item label="年度">
          <el-date-picker
            v-model="queryDTO.year"
            type="year"
            placeholder="选择年份"
            value-format="YYYY"
            @change="handleQuery"
            style="width: 130px"
            :clearable="false"
          />
        </el-form-item>

        <el-form-item label="楼栋">
          <el-select
            v-model="queryDTO.buildingId"
            placeholder="请选择"
            style="width: 120px"
            clearable
            @change="handleBuildingChange"
          >
            <el-option
              v-for="item in buildingList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="单元">
          <el-select
            v-model="queryDTO.unitId"
            placeholder="单元"
            style="width: 100px"
            clearable
          >
            <el-option
              v-for="item in unitList"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryDTO.payStatus"
            placeholder="状态"
            style="width: 110px"
            clearable
          >
            <el-option label="待缴费" value="UNPAID" />
            <el-option label="已缴费" value="PAID" />
          </el-select>
        </el-form-item>
        <el-form-item label="房号">
          <el-input
            v-model="queryDTO.roomNum"
            placeholder="房号"
            style="width: 100px"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"
            >查询</el-button
          >
          <template v-if="roleKey !== 'OWNER'">
            <el-button type="success" icon="Plus" @click="showGenDialog = true"
              >生成账单</el-button
            >
            <el-button
              type="warning"
              icon="CreditCard"
              :disabled="!multipleSelection.length"
              @click="handleBatchPay"
              >批量缴费</el-button
            >
          </template>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 3. 数据表格 -->
    <el-table
      :data="tableData"
      border
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" v-if="roleKey !== 'OWNER'" />
      <el-table-column prop="buildingName" label="楼栋" width="100" />
      <el-table-column prop="unitName" label="单元" width="80" />
      <el-table-column prop="roomNum" label="房号" width="100" />
      <el-table-column prop="ownerName" label="业主" width="120" />
      <el-table-column label="账期" width="120">
        <template #default="{ row }"
          >{{ row.year }}年{{ row.month }}月</template
        >
      </el-table-column>
      <!-- 🚀 新增计费周期列，展示折算详情 -->
      <el-table-column label="计费周期" width="220">
        <template #default="{ row }">
          <span style="font-size: 12px; color: #666"
            >{{ row.startDate }} 至 {{ row.endDate }}</span
          >
        </template>
      </el-table-column>
      <!-- 🚀 新增：单价展示列 -->
      <el-table-column label="计费单价" width="120">
        <template #default="{ row }">
          <span style="color: #606266">{{ row.unitPrice }}</span>
          <span style="font-size: 11px; color: #909399; margin-left: 4px"
            >元/m²</span
          >
        </template>
      </el-table-column>

      <el-table-column label="应缴金额" width="120">
        <template #default="{ row }">
          <span style="font-weight: bold; color: #f56c6c"
            >¥ {{ row.totalAmount }}</span
          >
        </template>
      </el-table-column>
      <el-table-column prop="deadline" label="截止日期" width="160">
        <template #default="{ row }">
          <span :style="getDeadlineStyle(row)">{{ row.deadline || "-" }}</span>
        </template>
      </el-table-column>
      <!-- 🚀 根据状态动态切换 tag 类型 -->
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.payStatus)">
            {{ row.payStatus === "PAID" ? "已缴费" : "待缴费" }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right">
        <template #default="{ row }">
          <!-- 管理员看到的：线下登记 -->
          <el-button
            v-if="row.payStatus !== '已缴费' && roleKey !== 'OWNER'"
            type="primary"
            link
            @click="handlePay(row.id)"
          >
            确认缴费
          </el-button>

          <!-- 🚀 业主看到的：模拟在线支付 -->
          <el-button
            v-if="row.payStatus !== '已缴费' && roleKey === 'OWNER'"
            type="success"
            link
            @click="handleOnlinePay(row)"
          >
            立即缴费
          </el-button>

          <span v-else-if="row.payStatus === '已缴费'" style="color: #67c23a"
            >已缴齐</span
          >
        </template>
      </el-table-column>
    </el-table>

    <div
      class="pagination-container"
      style="margin-top: 20px; display: flex; justify-content: flex-end"
    >
      <el-pagination
        v-model:current-page="queryDTO.current"
        v-model:page-size="queryDTO.size"
        :page-sizes="[10, 20, 50]"
        background
        layout="total, sizes, prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 4. 修改后的生成月度账单弹窗 -->
    <el-dialog v-model="showGenDialog" title="生成月度账单" width="420px">
      <el-form :model="genForm" label-width="100px">
        <el-form-item label="目标月份">
          <el-date-picker
            v-model="genForm.targetMonth"
            type="month"
            value-format="YYYY-MM-01"
            placeholder="选择月份"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="月单价">
          <el-input-number
            v-model="genForm.monthlyPrice"
            :precision="2"
            :step="0.1"
            :min="0"
            style="width: 150px"
          />
          <span style="margin-left: 10px">元/m²/月</span>
        </el-form-item>
        <div style="font-size: 12px; color: #e6a23c; padding-left: 100px">
          * 系统将根据交房日期自动计算折算金额
        </div>
      </el-form>
      <template #footer>
        <el-button @click="showGenDialog = false">取消</el-button>
        <el-button type="primary" :loading="genLoading" @click="submitGenerate"
          >开始生成</el-button
        >
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "@/utils/request";

// 1. 初始化变量
const queryDTO = ref({
  year: new Date().getFullYear(),
  buildingId: null,
  unitId: null,
  roomNum: "",
  payStatus: null,
  current: 1,
  size: 10,
});

const userStr = localStorage.getItem("pms_user");
const userInfo = userStr ? JSON.parse(userStr) : {};
const roleKey = userInfo.roleKey;

const tableData = ref([]);
const total = ref(0);
const reportData = ref({});
const buildingList = ref([]);
const unitList = ref([]);
const multipleSelection = ref([]);
const showGenDialog = ref(false);
const genLoading = ref(false);
const myRoom = ref({}); // 存储业主房间信息

// 获取我的房产详情
const getMyRoomInfo = async () => {
  if (roleKey === "OWNER") {
    try {
      // 注意：这里改为你后端的真实路径，比如 /bus/room/my
      const { data } = await axios.get("/room/my");
      // 如果一个业主名下有多套房，这里取第一套，或者你可以用 v-for 循环展示
      if (data && data.length > 0) {
        myRoom.value = data[0];
      }
    } catch (error) {
      console.error("获取房产信息失败", error);
    }
  }
};
const genForm = ref({
  targetMonth: new Date().toISOString().substring(0, 7) + "-01",
  monthlyPrice: 2.5,
});

// 2. 核心方法
const handleQuery = async () => {
  try {
    const { data } = await axios.get("/bill/page", { params: queryDTO.value });
    tableData.value = data.records;
    total.value = data.total;
    if (roleKey !== "OWNER") fetchReport();
  } catch (e) {
    ElMessage.error("查询失败");
  }
};

const fetchReport = async () => {
  const { data } = await axios.get("/bill/report", {
    params: { year: queryDTO.value.year },
  });
  reportData.value = data;
};

const submitGenerate = async () => {
  if (!genForm.value.targetMonth) return ElMessage.warning("请选择月份");
  genLoading.value = true;
  try {
    // 🚀 对应后端的月度折算接口
    await axios.post("/bill/generate/month", genForm.value);
    ElMessage.success("生成成功");
    showGenDialog.value = false;
    handleQuery();
  } finally {
    genLoading.value = false;
  }
};

const handlePay = (id) => {
  ElMessageBox.confirm("确认完成线下缴费登记?", "提示").then(async () => {
    await axios.put("/bill/pay", [id]);
    ElMessage.success("登记成功");
    handleQuery();
  });
};

const handleBatchPay = async () => {
  const ids = multipleSelection.value.map((i) => i.id);
  await axios.put("/bill/pay", ids);
  ElMessage.success("批量登记成功");
  handleQuery();
};

// 3. 联动与分页
const getBuildingList = async () => {
  const { data } = await axios.get("/building/list");
  buildingList.value = data;
};

const handleBuildingChange = async (val) => {
  queryDTO.value.unitId = null;
  unitList.value = [];
  if (val) {
    const { data } = await axios.get("/unit/list", {
      params: { buildingId: val },
    });
    unitList.value = data;
  }
};

const getDeadlineStyle = (row) => {
  if (row.payStatus === "已缴费") return {};
  const isOverdue = new Date(row.deadline) < new Date();
  return isOverdue ? { color: "#F56C6C", fontWeight: "bold" } : {};
};

const handleSelectionChange = (val) => (multipleSelection.value = val);
const handleSizeChange = (val) => {
  queryDTO.value.size = val;
  handleQuery();
};
const handleCurrentChange = (val) => {
  queryDTO.value.current = val;
  handleQuery();
};
// 1. 确保定义了这个函数
const getStatusType = (status) => {
  if (status === "PAID" || status === "已缴费") {
    return "success"; // 绿色
  }
  return "danger"; // 红色
};

// 2. 如果你想让显示文字也变漂亮，可以顺便加一个文字转换函数
const getStatusLabel = (status) => {
  if (status === "PAID" || status === "已缴费") {
    return "已缴费";
  }
  return "待缴费";
};

// 🚀 业主在线缴费逻辑
const handleOnlinePay = (row) => {
  ElMessageBox.confirm(
    `您确定要缴纳 ${row.year}年${row.month}月 的物业费吗？金额：¥${row.totalAmount}`,
    "在线支付确认",
    {
      confirmButtonText: "立即支付",
      cancelButtonText: "取消",
      type: "success",
    },
  )
    .then(async () => {
      try {
        // 调用后端缴费接口，将 id 包装成数组发送
        await axios.put("/bill/pay", [row.id]);
        ElMessage.success("支付成功！");
        handleQuery(); // 支付成功后刷新列表，状态会变成“已缴费”
      } catch (error) {
        console.error("支付失败", error);
        ElMessage.error("支付失败，请重试");
      }
    })
    .catch(() => {
      // 点击取消，不执行任何操作
    });
};

onMounted(() => {
  getBuildingList();
  handleQuery();
  getMyRoomInfo();
});
</script>

<style scoped>
.money {
  font-size: 24px;
  font-weight: bold;
  margin-top: 10px;
}
.money.success {
  color: #67c23a;
}
.report-cards .el-card {
  text-align: center;
}
</style>
