<template>
  <div style="padding: 20px;">
    <!-- 顶部操作栏 -->
    <div style="margin-bottom: 20px; display: flex; align-items: center; gap: 10px; flex-wrap: wrap;">
      <!-- 联动筛选：选楼栋 -->
      <el-select v-model="queryParams.buildingId" placeholder="选择楼栋" clearable @change="handleBuildingChange" style="width: 180px;">
        <el-option v-for="item in buildingList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      
      <!-- 联动筛选：选单元 -->
      <el-select v-model="queryParams.unitId" placeholder="选择单元" clearable @change="load" :disabled="!queryParams.buildingId" style="width: 150px;">
        <el-option v-for="item in unitList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      

      <el-button type="primary" @click="load"><el-icon><Search /></el-icon> 查询</el-button>
      
      <el-divider direction="vertical" />
      
      <!-- 功能按钮 -->
      <el-button type="success" @click="openInitDialog" :disabled="!queryParams.unitId">
        <el-icon><MagicStick /></el-icon> 一键生成房间
      </el-button>
      
      <el-upload
        action="http://localhost:8080/room/import"
        :on-success="() => { ElMessage.success('导入成功'); load() }"
        :show-file-list="false"
        accept=".xlsx, .xls"
      >
        <el-button type="warning"><el-icon><Upload /></el-icon> 批量导入</el-button>
      </el-upload>

      <!-- Room.vue 搜索栏 -->
<el-input 
  v-model="queryParams.ownerName" 
  placeholder="输入业主姓名查询" 
  style="width: 180px;" 
  clearable 
  @keyup.enter="load" 
/>
<el-button type="primary" @click="load">查询</el-button>

    </div>

    <!-- 数据表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="roomNum" label="房号" width="100" align="center" fixed />
      <el-table-column label="所属位置" width="200">
        <template #default="scope">
          {{ scope.row.buildingName }} - {{ scope.row.unitName }}
        </template>
      </el-table-column>
      <el-table-column prop="floor" label="楼层" width="80" align="center" />
      <el-table-column prop="area" label="面积(㎡)" width="120" align="center" />
      
      <!-- 状态显示：根据不同状态显示不同颜色 -->
      <el-table-column label="状态" width="100" align="center">
        <template #default="scope">
          <el-tag :type="getStatusTag(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>

      <!-- 业主信息：预留列 -->
      <el-table-column prop="ownerName" label="业主" width="120" align="center">
        <template #default="scope">
          {{ scope.row.ownerName || '---' }}
        </template>
      </el-table-column>
      <el-table-column prop="ownerPhone" label="联系电话" align="center" />

      <el-table-column label="操作" width="150" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      style="margin-top: 20px; justify-content: flex-end;"
      v-model:current-page="queryParams.current"
      v-model:page-size="queryParams.size"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="load"
    />

    <!-- 初始化房间弹窗 -->
    <el-dialog v-model="initVisible" title="一键初始化房间" width="400px">
      <el-form :model="initForm" label-width="100px">
        <el-form-item label="每层户数">
          <el-input-number v-model="initForm.roomsPerFloor" :min="1" />
        </el-form-item>
        <el-form-item label="默认面积">
          <el-input v-model="initForm.area" placeholder="请输入默认面积">
            <template #append>㎡</template>
          </el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="initVisible = false">取消</el-button>
        <el-button type="primary" @click="submitInit">开始生成</el-button>
      </template>
    </el-dialog>

    <!-- 编辑房间弹窗 -->
    <el-dialog v-model="editVisible" title="编辑房间信息" width="30%">
  <el-form :model="editForm" label-width="80px">
    <el-form-item label="房号">
      <el-input v-model="editForm.roomNum" disabled /> <!-- 房号通常不让改 -->
    </el-form-item>
    <el-form-item label="建筑面积">
      <el-input v-model="editForm.area"><template #append>㎡</template></el-input>
    </el-form-item>
    <el-form-item label="房间状态">
      <el-select v-model="editForm.status" style="width: 100%">
        <el-option label="闲置" value="VACANT" />
        <el-option label="已售" value="SOLD" />
        <el-option label="入住" value="OCCUPIED" />
      </el-select>
    </el-form-item>
    <el-form-item label="备注">
      <el-input v-model="editForm.remark" type="textarea" />
    </el-form-item>
    <!-- --- 新增：业主信息展示 (只读) --- -->
    <el-form-item label="当前业主">
      <span style="font-weight: bold; color: #409EFF">
        {{ editForm.ownerName || '暂无业主' }}
      </span>
    </el-form-item>
    <el-form-item label="联系电话">
      <span>{{ editForm.ownerPhone || '---' }}</span>
    </el-form-item>
    <el-divider /> <!-- 加个分割线美观一点 -->
  </el-form>
  <template #footer>
    <el-button @click="editVisible = false">取消</el-button>
    <el-button type="primary" @click="submitUpdate">确定</el-button>
  </template>
</el-dialog>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { useRoute } from 'vue-router' // 1. 引入 useRoute

const route = useRoute() // 2. 初始化 route 实例


const tableData = ref([])
const total = ref(0)
const buildingList = ref([])
const unitList = ref([])
const initVisible = ref(false)

const queryParams = reactive({ current: 1, size: 10, buildingId: null, unitId: null })
const initForm = reactive({ roomsPerFloor: 4, area: 120.0 })

// 1. 加载数据
const load = () => {
  request.get('/room/page', { params: queryParams }).then(res => {
    tableData.value = res.data.records
    total.value = res.data.total
  })
}

// 2. 联动逻辑：楼栋改变，加载对应单元
const handleBuildingChange = (val) => {
  queryParams.unitId = null
  unitList.value = []
  if (val) {
    request.get('/unit/list', { params: { buildingId: val } }).then(res => {
      unitList.value = res.data
    })
  }
  load()
}

const getBuildingList = () => {
  request.get('/building/list').then(res => { buildingList.value = res.data })
}

// 3. 初始化逻辑
const openInitDialog = () => { initVisible.value = true }
const submitInit = () => {
  request.post('/room/init', null, { 
    params: { unitId: queryParams.unitId, roomsPerFloor: initForm.roomsPerFloor, area: initForm.area } 
  }).then(() => {
    ElMessage.success('初始化成功')
    initVisible.value = false
    load()
  })
}

// 4. 辅助：状态颜色映射
const getStatusTag = (status) => {
  if (status === '闲置' || status === 'VACANT') return 'info'
  if (status === '已售' || status === 'SOLD') return 'primary'
  if (status === '入住' || status === 'OCCUPIED') return 'success'
  return ''
}

const handleDelete = (id) => {
  request.delete(`/room/delete/${id}`).then(() => { ElMessage.success('删除成功'); load() })
}


const editVisible = ref(false)
const editForm = ref({}) // 用于存储当前编辑的那行数据

// 点击“编辑”按钮触发
const handleEdit = (row) => {
  // 使用浅拷贝，防止在弹窗里改的时候，表格里的数据也跟着跳
  editForm.value = { ...row } 
  editVisible.value = true
}

// 点击弹窗“确定”按钮触发
const submitUpdate = () => {
  request.put('/room/update', editForm.value).then(() => {
    ElMessage.success('修改成功')
    editVisible.value = false
    load() // 重新刷新列表
  })
}

onMounted(() => {
  getBuildingList()
  if (route.query.ownerName) {
    queryParams.ownerName = route.query.ownerName
  }
  load()
})
</script>
