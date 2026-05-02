<template>
  <div class="app-container">
    <!-- 1. 顶部操作栏 -->
    <el-card class="filter-container" style="margin-bottom: 15px">
      <el-form :inline="true" :model="queryDTO" class="search-form">
        <el-form-item label="房号">
          <el-input
            v-model="queryDTO.roomNum"
            placeholder="房号"
            clearable
            style="width: 150px"
          />
        </el-form-item>
        <el-form-item label="状态">
          <el-select
            v-model="queryDTO.status"
            placeholder="全部状态"
            clearable
            style="width: 150px"
          >
            <el-option label="待处理" value="PENDING" />
            <el-option label="处理中" value="PROCESSING" />
            <el-option label="已完成" value="COMPLETED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"
            >查询</el-button
          >
          <!-- 🚀 只有业主显示“我要报修” -->
          <el-button
            v-if="roleKey === 'OWNER'"
            type="success"
            icon="Plus"
            @click="openApplyDialog"
          >
            我要报修
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 2. 数据表格 -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column prop="buildingName" label="楼栋" width="100" />
      <el-table-column prop="roomNum" label="房号" width="100" />
      <el-table-column prop="ownerName" label="报修业主" width="120" />
      <!-- 🚀 新增：业主电话列 -->
      <el-table-column prop="ownerPhone" label="联系电话" width="130">
        <template #default="{ row }">
          <!-- 如果有电话则显示，没有则显示横杠 -->
          <span>{{ row.ownerPhone || row.contactPhone || "-" }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="content" label="报修内容" show-overflow-tooltip />
      <el-table-column label="报修图片" width="120">
        <template #default="{ row }">
          <el-image
            v-if="row.photos"
            style="width: 50px; height: 50px"
            :src="row.photos"
            :preview-src-list="[row.photos]"
            preview-teleported
          />
          <span v-else>无图</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusTag(row.status)">{{
            getStatusLabel(row.status)
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="handlerName" label="维修人" width="120" />
      <el-table-column prop="createTime" label="报修时间" width="160" />
      <el-table-column prop="finishTime" label="完成时间" width="160">
        <template #default="{ row }">
          <span v-if="row.status === 'COMPLETED'">{{
            row.finishTime || "-"
          }}</span>
          <el-tag v-else type="info" size="small">处理中</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="操作" fixed="right" width="180">
        <template #default="{ row }">
          <!-- 1. 只有管理员能看到派单和完成 -->
          <template v-if="roleKey !== 'OWNER'">
            <!-- 待处理状态：显示派单 -->
            <el-button
              v-if="row.status === 'PENDING'"
              type="primary"
              link
              @click="openHandleDialog(row)"
              >派单</el-button
            >

            <!-- 处理中状态：显示确认完成 -->
            <el-button
              v-if="row.status === 'HANDLING'"
              type="success"
              link
              @click="completeRepair(row)"
              >确认完成</el-button
            >
          </template>

          <!-- 2. 业主和管理员都能看到的通用操作 -->
          <el-button type="info" link @click="viewDetail(row)">详情</el-button>

          <!-- 3. 如果状态是已完成，可以显示一个文本提示 -->
          <span
            v-if="row.status === 'COMPLETED'"
            style="color: #67c23a; font-size: 12px; margin-left: 10px"
          >
            已结单
          </span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      v-model:current-page="queryDTO.current"
      v-model:page-size="queryDTO.size"
      :total="total"
      layout="total, prev, pager, next"
      style="margin-top: 20px; justify-content: flex-end"
      @current-change="handleQuery"
    />

    <!-- 3. 业主报修弹窗 -->
    <el-dialog v-model="showApplyDialog" title="在线报修申请" width="500px">
      <el-form :model="applyForm" label-width="80px">
        <!-- 自动回显姓名，设为禁用状态，防止误改 -->
        <el-form-item label="报修业主">
          <el-input v-model="applyForm.ownerName" disabled />
        </el-form-item>

        <!-- 自动回显手机号，但允许业主修改 -->
        <el-form-item label="联系电话">
          <el-input
            v-model="applyForm.contactPhone"
            placeholder="请输入联系电话"
          />
        </el-form-item>

        <el-form-item label="报修内容">
          <el-input
            v-model="applyForm.content"
            type="textarea"
            :rows="3"
            placeholder="请描述具体故障"
          />
        </el-form-item>
        <el-form-item label="上传图片">
          <!-- 简单起见，这里假设直接输入URL，实际项目中应使用 el-upload -->
          <el-input
            v-model="applyForm.photos"
            placeholder="图片URL(模拟上传)"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showApplyDialog = false">取消</el-button>
        <el-button type="primary" @click="submitApply">提交申请</el-button>
      </template>
    </el-dialog>

    <!-- 4. 管理员派单弹窗 -->
    <el-dialog v-model="showHandleDialog" title="维修派单" width="400px">
      <el-form :model="handleForm" label-width="80px">
        <el-form-item label="选择师傅">
          <el-select
            v-model="handleForm.handlerId"
            placeholder="请选择维修师傅"
            @change="syncHandlerName"
          >
            <el-option
              v-for="item in repairMen"
              :key="item.id"
              :label="item.nickname"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showHandleDialog = false">取消</el-button>
        <el-button type="primary" @click="submitHandle">确认派单</el-button>
      </template>
    </el-dialog>

    <!-- 报修详情弹窗 -->
    <el-dialog
      v-model="showDetailDialog"
      title="报修单详情"
      width="600px"
      center
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报修编号">{{
          detailData.id
        }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="getStatusTag(detailData.status)">{{
            getStatusLabel(detailData.status)
          }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报修业主">{{
          detailData.ownerName
        }}</el-descriptions-item>
        <el-descriptions-item label="联系电话">{{
          detailData.contactPhone
        }}</el-descriptions-item>
        <el-descriptions-item label="所属位置" :span="2">
          {{ detailData.buildingName }} - {{ detailData.unitName }} -
          {{ detailData.roomNum }}
        </el-descriptions-item>
        <el-descriptions-item label="报修内容" :span="2">{{
          detailData.content
        }}</el-descriptions-item>
        <el-descriptions-item label="现场照片" :span="2">
          <el-image
            v-if="detailData.photos"
            :src="detailData.photos"
            :preview-src-list="[detailData.photos]"
            style="width: 150px; border-radius: 4px"
          />
          <span v-else style="color: #999">未上传照片</span>
        </el-descriptions-item>
        <el-descriptions-item label="维修师傅">{{
          detailData.handlerName || "待指派"
        }}</el-descriptions-item>
        <el-descriptions-item label="完成时间">{{
          detailData.finishTime || "-"
        }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "@/utils/request";

// 权限控制
const userStr = localStorage.getItem("pms_user");
const userInfo = userStr ? JSON.parse(userStr) : {};
const roleKey = userInfo.roleKey;

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const queryDTO = ref({ current: 1, size: 10, roomNum: "", status: null });

// 在管理端增加获取师傅列表的方法
const repairMen = ref([]);
const getRepairMen = async () => {
  try {
    const { data } = await axios.get("/sys/user/listByRole/REPAIR_MAN");
    repairMen.value = data;
  } catch (error) {
    console.error("加载维修师傅失败", error);
  }
};

const syncHandlerName = (id) => {
  // 选完 ID 后，顺便把名字也存进去，方便直接展示
  const selected = repairMen.value.find((u) => u.id === id);
  if (selected) {
    handleForm.value.handlerName = selected.nickname;
  }
};

// 报修申请数据
const showApplyDialog = ref(false);
const applyForm = ref({
  content: "",
  contactPhone: "",
  photos: "",
  roomId: null,
});

const showDetailDialog = ref(false);
const detailData = ref({});

// 点击详情按钮
const handleDetail = (row) => {
  detailData.value = { ...row }; // 浅拷贝行数据
  showDetailDialog.value = true;
};

// 派单处理数据
const showHandleDialog = ref(false);
const handleForm = ref({ id: null, handlerName: "" });

// 1. 查询列表
const handleQuery = async () => {
  loading.value = true;
  const { data } = await axios.get("/repair/page", {
    params: queryDTO.value,
  });
  tableData.value = data.records;
  total.value = data.total;
  loading.value = false;
};

// 2. 状态格式化
const getStatusLabel = (status) => {
  const map = { PENDING: "待处理", PROCESSING: "处理中", COMPLETED: "已完成" };
  return map[status] || status;
};
const getStatusTag = (status) => {
  const map = { PENDING: "info", PROCESSING: "warning", COMPLETED: "success" };
  return map[status] || "";
};

// 3. 业主报修逻辑
const openApplyDialog = async () => {
  // 1. 获取我的房间信息
  const { data } = await axios.get("/room/my");
  console.log("当前用户信息:", userInfo);

  if (data && data.length > 0) {
    applyForm.value.roomId = data[0].id; // 默认取第一套房

    // 2. 🚀 关键：回显当前登录业主的姓名和电话
    // 从我们之前定义的 userInfo (从 localStorage 解析出的) 中拿数据
    applyForm.value.ownerName = userInfo.nickname; // 自动填充姓名
    applyForm.value.contactPhone = userInfo.phone; // 自动填充手机号

    showApplyDialog.value = true;
  } else {
    ElMessage.error("未找到您的房产信息，无法报修");
  }
};

const submitApply = async () => {
  await axios.post("/repair/apply", applyForm.value);
  ElMessage.success("报修提交成功");
  showApplyDialog.value = false;
  handleQuery();
};

// 4. 管理员处理逻辑
const openHandleDialog = (row) => {
  handleForm.value.id = row.id;
  handleForm.value.handlerId = null; // 清空上次选择的 ID
  handleForm.value.handlerName = ""; // 清空上次选择的名字

  getRepairMen(); // 🚀 关键：打开弹窗时去后端拿最新的师傅名单
  showHandleDialog.value = true;
};

const submitHandle = async () => {
  if (!handleForm.value.handlerId) {
    return ElMessage.warning("请选择维修师傅");
  }

  // 选中的师傅名字同步一下 (为了展示方便)
  const selectedMan = repairMen.value.find(
    (m) => m.id === handleForm.value.handlerId,
  );
  if (selectedMan) {
    handleForm.value.handlerName = selectedMan.nickname;
  }

  // 🚀 调用后端的 handle 接口，后端 updateById 会把 handlerId 存入 bus_repair
  await axios.put("/repair/handle", handleForm.value);

  ElMessage.success("派单成功，工单已下发至师傅端");
  showHandleDialog.value = false;
  handleQuery(); // 刷新列表
};

const completeRepair = (row) => {
  ElMessageBox.confirm(
    "确认该项目已维修完成？系统将记录当前时间为完成时间。",
    "操作提示",
    {
      confirmButtonText: "确认",
      cancelButtonText: "取消",
      type: "success",
    },
  ).then(async () => {
    try {
      // 🚀 调用后端的 /complete/{id} 接口
      await axios.put(`/repair/complete/${row.id}`);
      ElMessage.success("操作成功，状态已更新");
      handleQuery(); // 刷新列表
    } catch (error) {
      console.error("提交失败", error);
    }
  });
};

onMounted(() => handleQuery());
</script>
