<template>
  <div class="app-container">
    <!-- 1. 搜索与操作栏 -->
    <el-card class="filter-container" style="margin-bottom: 15px">
      <el-form :inline="true" :model="queryDTO">
        <el-form-item label="公告标题">
          <el-input
            v-model="queryDTO.title"
            placeholder="请输入关键词"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery"
            >查询</el-button
          >
          <el-button
            v-if="roleKey === 'ADMIN'"
            type="success"
            @click="openAddDialog"
          >
            发布公告
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 2. 公告表格 -->
    <el-table :data="tableData" border style="width: 100%" v-loading="loading">
      <el-table-column
        prop="title"
        label="标题"
        min-width="200"
        show-overflow-tooltip
      />

      <el-table-column prop="type" label="类型" width="100">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? '' : 'warning'">
            {{ row.type === 1 ? "全体" : "楼栋" }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="状态" width="120">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status].tag">
            {{ statusMap[row.status].text }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="发布时间" width="180" />

      <!-- 2. 表格操作列的权限控制 -->
      <el-table-column label="操作">
        <template #default="{ row }">
          <!-- 查看按钮：所有人可见 -->
          <el-button type="text" @click="viewDetail(row)">查看</el-button>

          <!-- 🚀 撤回/发布/删除：只有管理员可见 -->
          <template v-if="roleKey === 'ADMIN'">
            <el-button
              v-if="row.status === 'PUBLISHED'"
              type="text"
              style="color: #e6a23c"
              @click="handleRecall(row)"
            >
              撤回
            </el-button>

            <el-button
              v-if="row.status === 'DRAFT'"
              type="text"
              @click="handlePublish(row)"
            >
              发布
            </el-button>

            <el-button
              type="text"
              style="color: #f56c6c"
              @click="handleDelete(row)"
            >
              删除
            </el-button>
          </template>
        </template>
      </el-table-column>
    </el-table>

    <!-- 3. 分页 -->
    <div style="margin-top: 20px; display: flex; justify-content: flex-end">
      <el-pagination
        v-model:current-page="queryDTO.current"
        v-model:page-size="queryDTO.size"
        :total="total"
        background
        layout="total, prev, pager, next"
        @current-change="handleQuery"
      />
    </div>

    <!-- 4. 新增/编辑弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '修改公告' : '发布新公告'"
      width="500px"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="form.type">
            <el-radio :label="1">全体公告</el-radio>
            <el-radio :label="2">楼栋公告</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="6"
            placeholder="请输入公告内容..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <!-- 5. 查看详情弹窗 -->
    <el-dialog v-model="viewVisible" title="公告详情" width="600px">
      <div style="padding: 0 20px">
        <h2 style="text-align: center">{{ activeNotice.title }}</h2>
        <div style="color: #999; text-align: center; margin-bottom: 20px">
          发布时间：{{ activeNotice.createTime }} | 类型：{{
            activeNotice.type === 1 ? "全体" : "楼栋"
          }}
        </div>
        <el-divider />
        <div style="line-height: 1.8; white-space: pre-wrap">
          {{ activeNotice.content }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from "vue";
import { ElMessage, ElMessageBox } from "element-plus";
import axios from "@/utils/request";

const loading = ref(false);
const tableData = ref([]);
const total = ref(0);
const queryDTO = ref({ current: 1, size: 10, title: "" });

// 状态映射表
const statusMap = {
  DRAFT: { text: "草稿", tag: "info" },
  PUBLISHED: { text: "已发布", tag: "success" },
  RECALLED: { text: "已撤回", tag: "warning" },
};

// 弹窗与表单
const dialogVisible = ref(false);
const viewVisible = ref(false);
const form = ref({ title: "", content: "", type: 1, status: "PUBLISHED" });
const activeNotice = ref({});

const handleQuery = async () => {
  loading.value = true;
  const { data } = await axios.get("/notice/page", {
    params: queryDTO.value,
  });
  tableData.value = data.records;
  total.value = data.total;
  loading.value = false;
};

const handleAdd = () => {
  form.value = { title: "", content: "", type: 1, status: "PUBLISHED" };
  dialogVisible.value = true;
};

const submitForm = async () => {
  if (!form.value.title || !form.value.content)
    return ElMessage.warning("请填写完整信息");
  await axios.post("/notice/add", form.value);
  ElMessage.success("操作成功");
  dialogVisible.value = false;
  handleQuery();
};

const handleView = (row) => {
  activeNotice.value = row;
  viewVisible.value = true;
};

const changeStatus = async (row, statusNum) => {
  // 调用后端 status/{id}/{status} 接口
  await axios.put(`/notice/status/${row.id}/${statusNum}`);
  ElMessage.success("操作成功");
  handleQuery();
};

const handleDelete = (id) => {
  ElMessageBox.confirm("确定要删除这条公告吗？", "提示").then(async () => {
    await axios.delete(`/notice/${id}`);
    ElMessage.success("删除成功");
    handleQuery();
  });
};

// 🚀 新增：从本地缓存获取当前用户的角色
const roleKey = computed(() => {
  const userStr = localStorage.getItem("pms_user");
  if (userStr) {
    const user = JSON.parse(userStr);
    return user.roleKey;
  }
  return "";
});
onMounted(() => {
  handleQuery();
});
</script>
