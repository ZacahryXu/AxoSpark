# AxoSpark 音视频分享平台

AxoSpark 是一个多模块的音视频分享平台，包含后端 Spring Boot 服务与前端 Vue3 + Vite 单页应用。该仓库使用 Maven 聚合管理多后端模块，并提供前端子项目以支撑完整的产品体验。

## 目录结构
- `axospark-common`：后端公共模块（Web、验证、MyBatis、分页、MySQL、Lombok、OpenAPI 等通用依赖与配置）
- `axospark-home`：首页/入口服务
- `axospark-video`：视频相关服务
- `axospark-creation`：创作/投稿相关服务
- `axospark-search`：搜索相关服务
- `axospark-collection`：收藏/稍后再看等收藏体系
- `axospark-history`：观看历史
- `axospark-me`：个人中心
- `axospark-updates`：动态/通知
- `axospark-ui`：前端应用（Vue3 + Vite）
- `sql/`：数据库初始化脚本


## 技术栈
- 后端：
  - Java 17、Spring Boot 3.5.x
  - MyBatis、PageHelper 分页
  - MySQL 8.x 驱动
  - Lombok
  - SpringDoc OpenAPI（API 文档）
- 前端：
  - Vue 3、Vite 7、@vitejs/plugin-vue
  - Node.js 20+（或 >=22.12.0）

## 环境要求
- JDK 17+
- Maven 3.9+
- Node.js 20.19+ 或 >=22.12.0
- MySQL 8.0+

## 快速开始
### 1. 克隆项目
```bash
git clone <your-repo-url> AxoSpark
cd AxoSpark
```

### 2. 数据库初始化
- 创建数据库（示例）：
  ```sql
  CREATE DATABASE axospark CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
  ```
- 导入初始化脚本：`sql/axospark_v1-202510042040.sql`
- 在各后端服务的 `application.yml`/`application.properties` 中配置数据库连接（URL、用户名、密码）。

### 3. 构建后端
在仓库根目录执行：
```bash
mvn clean install -DskipTests
```
> 根级 `pom.xml` 为聚合工程，包含所有后端子模块的依赖管理。

### 4. 启动后端服务
进入需要运行的子模块（示例以 `axospark-home` 为例）：
```bash
cd axospark-home
mvn spring-boot:run
```
其他模块（如 `axospark-video` 等）同理进入模块目录后运行。

### 5. 启动前端
```bash
cd axospark-ui
npm install
npm run dev
```
默认使用 Vite 启动本地开发服务，按终端地址访问前端页面。

## API 文档
集成 SpringDoc OpenAPI，后端服务启动后（示例端口以 `8080` 为例）：
- Swagger UI：`http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON：`http://localhost:8080/v3/api-docs`
> 实际端口与路径以各模块的应用配置为准。

## 常见问题
- 端口冲突：多个后端模块同时启动时，请为不同服务配置不同端口。
- 数据库连接失败：确认 MySQL 已启动、账号权限正确、驱动与连接字符串匹配。
- Lombok 报错：确保 IDE 安装 Lombok 插件并开启注解处理。
- Node 版本：前端要求 Node 20+（或 >=22.12.0），建议使用 `nvm` 管理多版本。

## 贡献
欢迎提交 Issue 与 PR。提交 PR 前请：
- 保持代码风格与现有一致
- 通过单元测试并确保构建通过
- 附上必要的说明与截图（如为前端 UI 变更）

## 许可证
本项目未显式声明许可证，默认保留所有权利。若需商用或二开，请先与作者沟通。
