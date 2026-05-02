import { createRouter, createWebHistory } from "vue-router";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: () => import("../views/Login.vue"),
  },
  // 🚀 核心修改 1：将注册页移到最外层，与 Login 平级
  {
    path: "/register",
    name: "Register",
    component: () => import("../views/Register.vue"),
  },
  {
    path: "/",
    name: "Layout",
    component: () => import("../layout/Layout.vue"),
    redirect: () => {
      const userStr = localStorage.getItem("user");
      if (userStr) {
        const userInfo = JSON.parse(userStr);
        if (userInfo.roleKey === "REPAIR_MAN") return "/repair";
        if (userInfo.roleKey === "OWNER") return "/notice";
      }
      return "/dashboard";
    },
    children: [
      {
        path: "building",
        name: "Building",
        component: () => import("../views/Building.vue"),
      },
      {
        path: "unit",
        name: "Unit",
        component: () => import("../views/Unit.vue"),
      },
      {
        path: "room",
        name: "room",
        component: () => import("../views/Room.vue"),
      },
      {
        path: "owner",
        name: "owner",
        component: () => import("../views/Owner.vue"),
      },
      {
        path: "bill",
        name: "bill",
        component: () => import("../views/Bill.vue"),
      },
      {
        path: "repair",
        name: "repair",
        component: () => import("../views/Repair.vue"),
      },
      {
        path: "notice",
        name: "notice",
        component: () => import("../views/Notice.vue"),
      },
      {
        path: "dashboard",
        name: "dashboard",
        component: () => import("../views/Dashboard.vue"),
      },
      // 🚀 核心修改 2：删掉了原本在这里的 /register
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// 2. 🚀 增强版路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem("token");
  const userStr = localStorage.getItem("user");
  const userInfo = userStr ? JSON.parse(userStr) : null;

  // 🚀 核心修改 3：定义白名单，不需要 token 也能进的页面
  const whiteList = ["/login", "/register"];

  // A. 白名单直接放行
  if (whiteList.includes(to.path)) {
    next();
    return;
  }

  // B. 未登录拦截（排除掉白名单后，没 token 的全部去登录）
  if (!token) {
    next("/login");
    return;
  }

  // C. 权限拦截逻辑（已登录后的逻辑）
  if (userInfo) {
    const roleKey = userInfo.roleKey;
    const adminPaths = [
      "/dashboard",
      "/building",
      "/unit",
      "/room",
      "/owner",
      "/bill",
    ];
    const isAdminPage = adminPaths.some((path) => to.path.includes(path));

    if (roleKey === "REPAIR_MAN" && isAdminPage) {
      next("/repair");
    } else if (
      roleKey === "OWNER" &&
      (to.path.includes("/dashboard") || to.path.includes("/building"))
    ) {
      next("/notice");
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
