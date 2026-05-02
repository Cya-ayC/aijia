<template>
  <div style="padding: 20px">
    <!-- 操作栏 -->
    <div
      style="margin-bottom: 20px; display: flex; align-items: center; gap: 10px"
    >
      <el-input
        v-model="queryParams.name"
        placeholder="搜索楼栋名称"
        style="width: 200px"
        clearable
        @clear="load"
      />
      <el-button type="primary" @click="load">
        <el-icon><Search /></el-icon> 查询
      </el-button>
      <el-button type="success" @click="handleAdd">
        <el-icon><Plus /></el-icon> 新增楼栋
      </el-button>

      <!-- 导入按钮 -->
      <el-upload
        action="http://localhost:8080/building/import"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        :show-file-list="false"
        accept=".xlsx, .xls"
      >
        <el-button type="warning">
          <el-icon><Upload /></el-icon> 批量导入
        </el-button>
      </el-upload>
    </div>

    <!-- 表格展示 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="name" label="楼栋名称" align="center" />
      <el-table-column
        prop="unitCount"
        label="单元数量"
        width="100"
        align="center"
      />
      <el-table-column
        prop="floorCount"
        label="总层数"
        width="100"
        align="center"
      />
      <el-table-column prop="remark" label="备注" show-overflow-tooltip />
      <el-table-column label="操作" width="180" align="center">
        <template #default="scope">
          <el-button size="small" type="primary" @click="handleEdit(scope.row)"
            >编辑</el-button
          >
          <el-popconfirm
            title="确定删除该楼栋吗？"
            @confirm="handleDelete(scope.row.id)"
          >
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页条 -->
    <el-pagination
      style="margin-top: 20px; justify-content: flex-end"
      v-model:current-page="queryParams.current"
      v-model:page-size="queryParams.size"
      :total="total"
      :page-sizes="[5, 10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="load"
      @current-change="load"
    />

    <!-- 新增/修改弹窗 -->
    <el-dialog
      v-model="dialogVisible"
      :title="form.id ? '编辑楼栋' : '新增楼栋'"
      width="30%"
    >
      <el-form :model="form" label-width="80px">
        <el-form-item label="楼栋名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="单元数量">
          <el-input-number v-model="form.unitCount" :min="1" />
        </el-form-item>
        <el-form-item label="总层数">
          <el-input-number v-model="form.floorCount" :min="1" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from "vue";
import { ElMessage } from "element-plus";
import request from "@/utils/request";

// 1. 数据定义
const tableData = ref([]);
const total = ref(0);
const dialogVisible = ref(false);

const queryParams = reactive({
  current: 1,
  size: 10,
  name: "",
});

const form = ref({});

// 2. 加载列表
const load = () => {
  request.get("/building/page", { params: queryParams }).then((res) => {
    tableData.value = res.data.records;
    total.value = res.data.total;
  });
};

// 3. 新增/修改逻辑
const handleAdd = () => {
  form.value = { unitCount: 1, floorCount: 1 }; // 重置表单
  dialogVisible.value = true;
};

const handleEdit = (row) => {
  form.value = { ...row }; // 浅拷贝，防止直接修改表格显示
  dialogVisible.value = true;
};

const submitForm = () => {
  if (form.value.id) {
    // 有ID是修改
    request.put("/building/update", form.value).then(() => {
      ElMessage.success("更新成功");
      dialogVisible.value = false;
      load();
    });
  } else {
    // 没ID是新增
    request.post("/building/add", form.value).then(() => {
      ElMessage.success("添加成功");
      dialogVisible.value = false;
      load();
    });
  }
};

// 4. 删除逻辑
const handleDelete = (id) => {
  request.delete(`/building/delete/${id}`).then(() => {
    ElMessage.success("删除成功");
    load();
  });
};

// 5. 导入逻辑
const handleImportSuccess = () => {
  ElMessage.success("导入成功");
  load();
};
const handleImportError = (err) => {
  ElMessage.error("导入失败，请检查数据格式或楼栋名是否重复");
};

onMounted(() => {
  load();
});
</script>
