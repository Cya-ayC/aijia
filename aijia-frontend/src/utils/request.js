import axios from "axios";
import { ElMessage } from "element-plus";

// 1. 创建 axios 实例
const request = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 5000,
});

// --- 🚀 新增：请求拦截器 (解决 403 的关键) ---
request.interceptors.request.use(
  (config) => {
    // 从本地缓存获取登录时存入的 token
    const token = localStorage.getItem("token");
    if (token) {
      // 关键：在请求头中注入 Authorization 字段
      // 注意：Bearer 后面有个空格，必须和后端 JwtAuthenticationTokenFilter 里的解析规则一致
      config.headers["Authorization"] = "Bearer " + token;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  },
);

// 2. 响应拦截器：统一处理后端返回的 Result 结构
request.interceptors.response.use(
  (response) => {
    let res = response.data;
    if (typeof res === "string") {
      return res;
    }
    if (res.code === 200) {
      return res;
    } else {
      ElMessage.error(res.msg || "系统错误");
      return Promise.reject(res.msg);
    }
  },
  (error) => {
    // 💡 进阶建议：如果后端返回 403，说明 Token 过期或无效，这里可以自动跳回登录页
    if (
      error.response &&
      (error.response.status === 403 || error.response.status === 401)
    ) {
      ElMessage.error("登录失效，请重新登录");
      localStorage.removeItem("token"); // 清除过期的 Token
      // window.location.href = '/login'; // 可选：强制跳转
    } else {
      ElMessage.error("网络请求失败，请检查后端是否启动");
    }
    return Promise.reject(error);
  },
);

export default request;
