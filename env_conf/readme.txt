1. 应用新配置方式的说明

    在tomcat中，确保启动命令中有新的环境配置变量。
    例如，在我的本地开发环境中，有如下变量定义被加入启动参数：

set ENV_CONF="F:/software/apache-tomcat-8.0.23-forBaituNew/env_conf/dev"
set "JAVA_OPTS=-Denv_conf_path=%ENV_CONF%"
 
    这个保证了启动时，加载F:/software/apache-tomcat-8.0.23-forBaituNew/env_conf/dev目录下的几个properties文件
    在linux上，应该是在catalina.sh中对应位置，增加对应的启动参数。
    参数指向的目录可以是绝对路径。