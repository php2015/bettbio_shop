V3.0.0

新增功能：
	1. 使用环境变量控制配置数据
	2. 直接WAR包部署
	3. 非生产环境，邮件，email和domain通过配置控制
	
Bugfix：
	1. sina邮件收到HTML源码的问题
	2. 部分邮件客户端乱码的问题

升级说明：
	1. 首先对V2.1.0做备份 （重要！！！）
	2. 停止服务
	3. 删除原有webapps下的旧应用
	4. 将新的WAR包放到tomcat的webapps下
	5. 确定部署环境，然后确定tomcat的启动命令中，环境变量 env_conf_path 指向正确的环境配置目录
	6. 启动tomcat

回滚说明：
	1. 停止服务
	2. 部署备份的旧版本web应用
	3. 启动旧版本