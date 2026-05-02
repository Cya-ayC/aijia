<template>
  <div class="register-container">
    <!-- 旋转背景装饰 -->
    <div class="background-decor"></div>
    <el-card class="register-card">
      <template #header>
        <div class="register-title">新用户注册</div>
      </template>

      <el-form
        ref="registerFormRef"
        :model="registerForm"
        :rules="registerRules"
        label-width="80px"
      >
        <!-- 账号 -->
        <el-form-item label="账号" prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入登录账号"
          />
        </el-form-item>

        <!-- 密码 -->
        <el-form-item label="密码" prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            show-password
            placeholder="请输入密码"
          />
        </el-form-item>

        <!-- 确认密码 -->
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            show-password
            placeholder="请再次输入密码"
          />
        </el-form-item>

        <!-- 昵称/姓名 -->
        <el-form-item label="姓名" prop="nickname">
          <el-input
            v-model="registerForm.nickname"
            placeholder="请输入您的真实姓名"
          />
        </el-form-item>

        <!-- 身份选择 -->
        <el-form-item label="身份" prop="roleKey">
          <el-radio-group v-model="registerForm.roleKey">
            <el-radio label="OWNER">我是业主</el-radio>
            <el-radio label="REPAIR_MAN">我是维修员</el-radio>
          </el-radio-group>
        </el-form-item>

        <!-- 手机号（如果是业主，通常必填） -->
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="registerForm.phone"
            placeholder="用于关联物业信息"
          />
        </el-form-item>

        <div style="margin-top: 20px">
          <el-button
            type="primary"
            style="width: 100%"
            :loading="loading"
            @click="handleRegister"
          >
            立即注册
          </el-button>
          <div class="login-link">
            已有账号？<el-link type="primary" @click="router.push('/login')"
              >去登录</el-link
            >
          </div>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import axios from "@/utils/request"; // 确保指向你的 axios 实例

const router = useRouter();
const registerFormRef = ref(null);
const loading = ref(false);

const registerForm = ref({
  username: "",
  password: "",
  confirmPassword: "",
  nickname: "",
  phone: "",
  roleKey: "OWNER", // 默认选中业主
});

// 表单验证规则
const registerRules = {
  username: [{ required: true, message: "请输入账号", trigger: "blur" }],
  password: [
    { required: true, message: "请输入密码", trigger: "blur" },
    { min: 6, message: "密码长度不能少于6位", trigger: "blur" },
  ],
  confirmPassword: [
    { required: true, message: "请再次输入密码", trigger: "blur" },
    {
      validator: (rule, value, callback) => {
        if (value !== registerForm.value.password) {
          callback(new Error("两次输入的密码不一致"));
        } else {
          callback();
        }
      },
      trigger: "blur",
    },
  ],
  nickname: [{ required: true, message: "请输入姓名", trigger: "blur" }],
  phone: [
    { required: true, message: "请输入手机号", trigger: "blur" },
    { pattern: /^1[3-9]\d{9}$/, message: "手机号格式不正确", trigger: "blur" },
  ],
};

const handleRegister = async () => {
  registerFormRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      // 🚀 调用你刚才写的后端接口
      const res = await axios.post("/sys/user/register", registerForm.value);
      if (res.code === 200) {
        ElMessage.success("注册成功！请登录");
        router.push("/login");
      }
    } catch (error) {
      console.error(error);
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  /* 🚀 同步登录页的渐变背景 */
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  overflow: hidden;
  position: relative;
}

/* 🚀 同步登录页的旋转装饰块 */
.background-decor {
  position: absolute;
  width: 1000px;
  height: 1000px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 40%;
  top: -500px;
  right: -300px;
  animation: rotate 20s linear infinite;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

.register-card {
  width: 480px; /* 🚀 注册表单字段多，宽度比登录页稍微调大一点 */
  padding: 15px 25px;
  border-radius: 15px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  z-index: 10;
  border: none;
  background: rgba(255, 255, 255, 0.95);
}

.register-header {
  text-align: center;
  margin-bottom: 20px;
}

.title {
  color: #333;
  margin: 10px 0;
  font-size: 24px;
  font-weight: bold;
}

.subtitle {
  color: #999;
  font-size: 12px;
  letter-spacing: 1px;
}

/* 🚀 按钮颜色同步登录页的深紫色 */
.register-button {
  width: 100%;
  border-radius: 8px;
  margin-top: 10px;
  background: #764ba2;
  border: none;
}

.register-button:hover {
  background: #667eea;
}

.login-link {
  text-align: center;
  margin-top: 15px;
  font-size: 14px;
  color: #666;
}
</style>
