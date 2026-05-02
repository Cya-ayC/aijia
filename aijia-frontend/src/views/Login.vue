<template>
  <div class="login-container">
    <!-- 背景装饰 -->
    <div class="background-decor"></div>

    <el-card class="login-card">
      <div class="login-header">
        <img src="https://elemecdn.com" alt="logo" class="logo" />
        <h2 class="title">爱家物业管理系统</h2>
        <p class="subtitle">Aijia Property Management System</p>
      </div>

      <el-form
        :model="loginForm"
        :rules="rules"
        ref="loginFormRef"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="User"
            placeholder="请输入用户名"
            size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            prefix-icon="Lock"
            placeholder="请输入密码"
            show-password
            size="large"
            @keyup.enter="handleLogin"
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            class="login-button"
            @click="handleLogin"
            :loading="loading"
            size="large"
          >
            登 录
          </el-button>
          <!-- 🚀 新增：注册链接 -->
          <div class="login-footer">
            <span>还没有账号？</span>
            <el-link type="primary" :underline="false" @click="goToRegister">
              立即注册
            </el-link>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import request from "../utils/request";

const router = useRouter();
const loading = ref(false);
const loginFormRef = ref(null);

const loginForm = reactive({ username: "", password: "" });
const rules = {
  username: [{ required: true, message: "账号不能为空", trigger: "blur" }],
  password: [{ required: true, message: "密码不能为空", trigger: "blur" }],
};

// 跳转到注册页
const goToRegister = () => {
  router.push("/register");
};
const handleLogin = async () => {
  loginFormRef.value.validate(async (valid) => {
    if (!valid) return;
    loading.value = true;
    try {
      const res = await request.post("/bus/user/login", loginForm);
      if (res.code === 200) {
        localStorage.clear();
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("pms_menus", JSON.stringify(res.data.menus));
        localStorage.setItem("pms_user", JSON.stringify(res.data.user));

        ElMessage.success("欢迎回来，登录成功");

        const roleKey = res.data.user.roleKey;

        // 🚀 重新设计的跳转逻辑
        if (roleKey === "REPAIR_MAN") {
          router.push("/repair"); // 维修员去报修管理
        } else if (roleKey === "OWNER") {
          router.push("/notice"); // 建议业主先看公告，或者按你之前的改回 /bill
        } else {
          router.push("/dashboard"); // 只有管理员才去仪表盘
        }
      }
    } finally {
      loading.value = false;
    }
  });
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); /* 渐变背景 */
  overflow: hidden;
  position: relative;
}

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

.login-card {
  width: 420px;
  padding: 20px;
  border-radius: 15px;
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
  z-index: 10;
  border: none;
  background: rgba(255, 255, 255, 0.95);
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.logo {
  width: 60px;
  margin-bottom: 10px;
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

.login-button {
  width: 100%;
  border-radius: 8px;
  margin-top: 10px;
  background: #764ba2;
  border: none;
}
.login-button:hover {
  background: #667eea;
}
</style>
