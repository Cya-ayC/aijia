<template>
  <div style="padding: 20px;">
    <!-- 1. 多条件搜索栏 -->
    <el-card shadow="never" style="margin-bottom: 20px;">
      <div style="display: flex; gap: 10px; flex-wrap: wrap;">
        <el-input v-model="queryParams.name" placeholder="业主姓名" style="width: 150px" clearable @keyup.enter="load" />
        <el-input v-model="queryParams.phone" placeholder="手机号" style="width: 150px" clearable @keyup.enter="load" />
        <el-input v-model="queryParams.idCard" placeholder="身份证号" style="width: 200px" clearable @keyup.enter="load" />
        
        <el-select v-model="queryParams.sex" placeholder="性别" clearable style="width: 100px" @change="load">
          <el-option label="男" :value="0" />
          <el-option label="女" :value="1" />
        </el-select>

        <el-button type="primary" @click="load">
          <el-icon><Search /></el-icon> 查询
        </el-button>
        <el-button @click="resetQuery">重置</el-button>
        <el-button type="success" @click="handleAdd" style="margin-left: auto;">
          <el-icon><Plus /></el-icon> 登记业主
        </el-button>
      </div>
    </el-card>

    <!-- 2. 数据表格 -->
    <el-table :data="tableData" border stripe style="width: 100%">
      <el-table-column prop="name" label="姓名">
  <template #default="scope">
    <!-- 点击姓名跳转到房屋管理，并带上参数 -->
    <el-link type="primary" @click="viewOwnerRooms(scope.row.name)">
      {{ scope.row.name }}
    </el-link>
  </template>
</el-table-column>

      <el-table-column prop="sex" label="性别" width="80" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.sex === 0 ? '' : 'danger'">
            {{ scope.row.sex === 0 ? '男' : '女' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="联系电话" width="150" align="center" />
      <el-table-column prop="idCard" label="身份证号" align="center" />
      <el-table-column prop="createTime" label="登记时间" align="center" width="180" />
      
      <el-table-column label="操作" width="240" align="center" fixed="right">
        <template #default="scope">
          <el-button size="small" type="warning" @click="openBindDialog(scope.row)">
            <el-icon><House /></el-icon> 办理入住
          </el-button>
          <el-button size="small" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
          <el-popconfirm title="确定删除该业主及其所有关联信息吗？" @confirm="handleDelete(scope.row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 3. 分页 -->
    <el-pagination
      style="margin-top: 20px; justify-content: flex-end;"
      v-model:current-page="queryParams.current"
      v-model:page-size="queryParams.size"
      :total="total"
      layout="total, prev, pager, next"
      @current-change="load"
    />

    <!-- 4. 登记/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '修改业主' : '登记业主'" width="35%">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="90px">
        <el-form-item label="业主姓名" prop="name">
          <el-input v-model="form.name" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="性别">
          <el-radio-group v-model="form.sex">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="联系电话"  prop="phone">
          <el-input v-model="form.phone"  maxlength="11" placeholder="请输入11位手机号" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" maxlength="18" placeholder="请输入身份证号"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定保存</el-button>
      </template>
    </el-dialog>

    <!-- 5. 办理入住弹窗（人房绑定） -->
    <el-dialog v-model="bindVisible" title="办理入住 - 分配房屋" width="450px" destroy-on-close>
      <el-form label-width="80px">
        <el-form-item label="当前业主">
          <b style="color: #409EFF">{{ currentOwner.name }}</b> ({{ currentOwner.phone }})
        </el-form-item>
        <el-form-item label="选择楼栋">
          <el-select v-model="bindData.buildingId" placeholder="请选择楼栋" @change="onBuildingChange" style="width: 100%">
            <el-option v-for="b in buildingList" :key="b.id" :label="b.name" :value="b.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择单元">
          <el-select v-model="bindData.unitId" placeholder="请选择单元" @change="onUnitChange" :disabled="!bindData.buildingId" style="width: 100%">
            <el-option v-for="u in unitList" :key="u.id" :label="u.name" :value="u.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="选择房间">
          <el-select v-model="bindData.roomId" placeholder="请选择房间 (仅显示闲置房)" :disabled="!bindData.unitId" style="width: 100%">
            <el-option v-for="r in roomList" :key="r.id" :label="r.roomNum" :value="r.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="bindVisible = false">取消</el-button>
        <el-button type="primary" @click="submitBind" :disabled="!bindData.roomId">确认办理</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '../utils/request'
import { useRouter } from 'vue-router' // 1. 引入 useRouter

//  初始化 router 实例
const router = useRouter() 

const formRef = ref(null)

// 数据定义
const tableData = ref([])
const total = ref(0)
const queryParams = reactive({ current: 1, size: 10, name: '', phone: '', idCard: '', sex: null })

// 表单弹窗
const dialogVisible = ref(false)
const form = ref({ sex: 0 })

// 绑定相关
const bindVisible = ref(false)
const currentOwner = ref({})
const buildingList = ref([])
const unitList = ref([])
const roomList = ref([])
const bindData = reactive({ buildingId: null, unitId: null, roomId: null })

// 校验规则
const rules = {
  name: [
    { required: true, message: '姓名不能为空', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度应在 2-20 位之间', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '手机号不能为空', trigger: 'blur' },
    { len: 11, message: '手机号必须是 11 位', trigger: 'blur' }
  ],
  idCard: [
    // 身份证如果不强制必填，可以不加 required
    { len: 18, message: '身份证号必须是 18 位', trigger: 'blur' }
  ]
}

// --- 基础查询方法 ---
const load = () => {
  request.get('/owner/page', { params: queryParams }).then(res => {
    tableData.value = res.data.records
    total.value = res.data.total
  })
}

const resetQuery = () => {
  Object.assign(queryParams, { name: '', phone: '', idCard: '', sex: null })
  load()
}

// --- 业主CRUD ---
const handleAdd = () => { form.value = { sex: 0 }; dialogVisible.value = true }
const handleEdit = (row) => { form.value = { ...row }; dialogVisible.value = true }
const submitForm = () => {
  // 1. 获取表单引用并调用 validate 方法
  formRef.value.validate((valid) => {
    if (valid) {
      // 2. 只有校验通过 (valid 为 true) 才执行原有逻辑
      const method = form.value.id ? 'put' : 'post'
      const url = form.value.id ? '/owner/update' : '/owner/add'
      
      request[method](url, form.value).then(() => {
        ElMessage.success('操作成功')
        dialogVisible.value = false
        load()
      })
    } else {
      // 3. 校验失败时的提示（可选）
      ElMessage.error('表单输入不合法，请检查红字提示')
      return false
    }
  })
}

const handleDelete = (id) => {
  request.delete(`/owner/delete/${id}`).then(() => { ElMessage.success('删除成功'); load() })
}

// --- 绑定房屋联动逻辑 (重点) ---
const openBindDialog = (row) => {
  currentOwner.value = row
  // 重置联动下拉框
  Object.assign(bindData, { buildingId: null, unitId: null, roomId: null })
  bindVisible.value = true
  // 加载全量楼栋给第一个下拉框
  request.get('/building/list').then(res => buildingList.value = res.data)
}

const onBuildingChange = (bid) => {
  bindData.unitId = null; bindData.roomId = null
  request.get('/unit/list', { params: { buildingId: bid } }).then(res => {
    unitList.value = res.data
  })
}

const onUnitChange = (uid) => {
  bindData.roomId = null
  // 建议：只查询状态为闲置的房间供绑定
  request.get('/room/page', { params: { unitId: uid, size: 1000 } }).then(res => {
    // 前端过滤：只显示状态为“闲置”的房源 (如果是枚举，匹配文字或Code)
    roomList.value = res.data.records.filter(r => r.status === '闲置' || r.status === 'VACANT')
  })
}

const submitBind = () => {
  request.post('/room-owner/bind', null, { 
    params: { ownerId: currentOwner.value.id, roomId: bindData.roomId } 
  }).then(() => {
    ElMessage.success(`为业主 [${currentOwner.value.name}] 办理入住成功！`)
    bindVisible.value = false
    // 绑定后可以重新刷下列表，或者跳转到房屋管理看一眼
    load()
  })
}
const viewOwnerRooms = (name) => {
  // 携带 query 参数跳转到房间页面
  router.push({ path: '/room', query: { ownerName: name } })
}


onMounted(() => load())
</script>

<style scoped>
.el-card { border-radius: 8px; }
b { font-size: 16px; }
</style>
