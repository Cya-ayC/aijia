<template>
  <el-container style="height: 100vh">
    <!-- 左侧菜单 -->
    <el-aside width="200px" style="background-color: #304156">
      <el-menu
        active-text-color="#ffd04b"
        background-color="#304156"
        text-color="#fff"
        router
        :default-active="$route.path"
        style="border: none"
      >
        <div
          style="
            height: 60px;
            color: white;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
          "
        >
          爱家物业管理
        </div>

        <!-- 动态菜单渲染 -->
        <!-- 找到你 el-menu 里的循环部分 -->
        <el-menu-item
          v-for="menu in filteredMenuList"
          :key="menu.id"
          :index="menu.path"
        >
          <i :class="menu.icon"></i>
          <span>{{ menu.menuName }}</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧主体 -->
    <el-container>
      <el-header
        style="
          border-bottom: 1px solid #ccc;
          display: flex;
          align-items: center;
          justify-content: space-between;
          background-color: #fff;
        "
      >
        <span style="font-weight: bold; color: #333"
          >欢迎使用爱家物业后台系统</span
        >

        <!-- 退出登录区域 -->
        <div style="display: flex; align-items: center">
          <!-- Layout.vue 的 template -->
          <el-avatar :size="30" :src="userInfo.avatar">
            <!-- 如果没头像，显示昵称第一个字 -->
            {{ userInfo.nickname?.substring(0, 1) }}
          </el-avatar>
          <span style="margin-right: 20px; font-size: 14px; color: #666">
            {{ userInfo.nickname }}
          </span>

          <el-button type="danger" size="small" plain @click="handleLogout"
            >退出登录</el-button
          >
        </div>
      </el-header>

      <el-main style="background-color: #f0f2f5">
        <!-- 路由出口 -->
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from "element-plus";
import { computed } from "vue";

const userInfo = computed(() => {
  const userStr = localStorage.getItem("pms_user");
  // 增加判断，如果为空返回默认值
  return userStr ? JSON.parse(userStr) : { nickname: "管理员", avatar: "" };
});

const router = useRouter();
const menuList = ref([]);

// 初始化加载菜单
onMounted(() => {
  const menuStr = localStorage.getItem("pms_menus");
  if (menuStr) {
    try {
      menuList.value = JSON.parse(menuStr);
    } catch (e) {
      console.error("菜单数据解析失败", e);
    }
  }
  const userStr = localStorage.getItem("pms_user");
  if (userStr) userInfo.value = JSON.parse(userStr);
});

// 退出登录逻辑
const handleLogout = () => {
  ElMessageBox.confirm("确定要退出系统吗？", "提示", {
    confirmButtonText: "确定",
    cancelButtonText: "取消",
    type: "warning",
  })
    .then(() => {
      localStorage.clear(); // 清空本地缓存
      ElMessage.success("已安全退出");
      router.push("/login"); // 跳回登录页
    })
    .catch(() => {});
};

// 🚀 新增：过滤后的菜单列表
const filteredMenuList = computed(() => {
  const user = userInfo.value;
  const roleKey = user.roleKey;

  // 如果是维修员，过滤掉不需要的菜单
  if (roleKey === "REPAIR_MAN") {
    // 这里根据你菜单对象里的 path 或者 name 来过滤
    // 假设仪表盘的路径是 /dashboard，楼栋管理是 /building
    const forbiddenPaths = [
      "/dashboard",
      "/building",
      "/unit",
      "/room",
      "/owner",
      "/bill",
    ];
    return menuList.value.filter((item) => !forbiddenPaths.includes(item.path));
  }

  // 如果是业主，也可以做类似过滤
  if (roleKey === "OWNER") {
    const forbiddenPaths = ["/dashboard", "/building", "/owner", "/bill"];
    return menuList.value.filter((item) => !forbiddenPaths.includes(item.path));
  }

  // 管理员返回全部
  return menuList.value;
});
</script>

<style scoped>
.el-header {
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  z-index: 10;
}
</style>
