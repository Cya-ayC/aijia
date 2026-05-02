# 🏘️ 爱家物业管理系统 (Aijia Property Management)

这是一个基于 **Spring Boot** 和 **Vue 3** 开发的前后端分离全栈项目。

## 🌟 项目亮点
- **权限控制**：完整的路由守卫，支持管理员、维修工、业主多种角色登录拦截。
- **功能完备**：包含楼宇、单元、房间、业主管理，以及报修和公告系统。

## 🛠️ 快速启动

### 1. 数据库准备
- 创建名为 `aijia` 的 MySQL 数据库。
- 导入 `/db` 目录下的 `.sql` 脚本。
- 检查 `aijia-backend` 中的 `application.yml`，确保数据库账号密码正确。

### 2. 启动后端
- 进入 `aijia-backend`，使用 IDEA 运行 `AijiaApplication`。

### 3. 启动前端
- 进入 `aijia-frontend`，执行以下命令：
  ```bash
  npm install
  npm run dev
  ```

### 🔑 测试账号
- **管理员**: `admin` / `123456`
- *(注：如需模拟首次登录，请先手动清理浏览器 LocalStorage)*

---
感谢关注！如果对你有帮助，欢迎点个 **Star** ⭐
