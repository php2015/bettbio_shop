部署方法：
A-在生产环境上
1。 建立目录 /data/clariones/backup/scripts
2. 将两个文件放在这个目录下：config.properties dobackup.sh

B-在文档环境上
1. 建立目录 /data/clariones/backup
2. 将2个文件放在这个目录下：copy_backup_*.sh

执行方法：
1. 修改配置文件 config.properties,将数据库IP等参数正确设置
2. 执行 dobackup.sh 做备份
3. 然后去文档服务器上执行 copy_backup.sh 把备份拷贝过去

定时执行的调度计划确定
在文档服务器上，增加定时任务：
0 0 * * 6 /data/clarions/backup/copy_backup_all.sh >> /data/clarions/backup/backup.log
0 0 * * 0-5 /data/clarions/backup/copy_backup_db.sh >> /data/clarions/backup/backup.log
意思就是定时执行：
周6凌晨0时，执行copy_backup_all.sh
周日到周5凌晨0时，执行copy_backup_db.sh