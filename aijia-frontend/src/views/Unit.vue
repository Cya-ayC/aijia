<template>
  <div style="padding: 20px">
    <!-- 顶部搜索区域 -->
    <div
      style="margin-bottom: 20px; display: flex; align-items: center; gap: 10px"
    >
      <el-select
        v-model="queryParams.buildingId"
        placeholder="请选择楼栋进行筛选"
        clearable
        style="width: 250px"
        @change="load"
      >
        <el-option
          v-for="item in buildingList"
          :key="item.id"
          :label="item.name"
          :value="item.id"
        />
      </el-select>
      <el-button type="primary" @click="load">
        <el-icon><Search /></el-icon> 查询
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="编号" width="100" align="center" />
      <el-table-column prop="buildingName" label="所属楼栋" align="center">
        <template #default="scope">
          <!-- 绿色标签展示联查出来的楼栋名称 -->
          <el-tag type="success" effect="plain">{{
            scope.row.buildingName
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="单元名称" align="center" />
      <el-table-column prop="createTime" label="创建时间" align="center" />

      <!-- 操作列 -->
      <el-table-column label="操作" width="150" align="center">
        <template #default="scope">
          <el-popconfirm
            title="确定要删除这个单元吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      style="margin-top: 20px; justify-content: flex-end"
      v-model:current-page="queryParams.current"
      v-model:page-size="queryParams.size"
      :total="total"
      layout="total, prev, pager, next, jumper"
      @current-change="load"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import request from "../utils/request"; // 请确保路径指向你的 axios 封装文件

// --- 数据定义 ---
const tableData = ref([]);
const total = ref(0);
const buildingList = ref([]); // 存储楼栋列表，用于下拉框

const queryParams = reactive({
  current: 1,
  size: 10,
  buildingId: null,
});

// --- 核心方法 ---

// 1. 加载单元列表（调用后端的 VO 联查分页接口）
const load = () => {
  request.get("/unit/page", { params: queryParams }).then((res) => {
    tableData.value = res.data.records;
    total.value = res.data.total;
  });
};

// 2. 加载楼栋列表（供搜索下拉框使用）
const getBuildingList = () => {
  request.get("/building/list").then((res) => {
    buildingList.value = res.data;
  });
};

// 3. 删除单元
const handleDelete = (id) => {
  request.delete(`/unit/delete/${id}`).then(() => {
    ElMessage.success("单元删除成功");
    load();
  });
};

// --- 生命周期钩子 ---
onMounted(() => {
  getBuildingList(); // 初始化先加载楼栋，方便下拉框选择
  load(); // 加载表格数据
});
</script>
