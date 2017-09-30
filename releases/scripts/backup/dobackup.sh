#!/bin/bash  

logstr()
{
    echo "`date +%D\ %T`: $1";
}

getCfg(){
    cat $backup_base/scripts/config.properties | grep $1 | awk -F= '{print $2}'
}

backup_db()
{
   logstr "backup DB to $backup_folder/db"
   mkdir db
   cd db
   logstr "  start to backup DB"
   db_user=`getCfg "db_user"`
   db_password=`getCfg "db_password"`
   db_schema=`getCfg "db_schema"`
   mysqldump -u${db_user} -p${db_password} -h ${db_host} --hex-blob --skip-lock-tables ${db_schema} > db_backup.sql
   logstr "  package backup result"
   tar -czvf db_backup.tar.gz db_backup.sql
   rm -f db_backup.sql
   logstr "  DB backup done"
   cd ..
}

backup_infinispan()
{
   logstr "backup infinipan file storage to $backup_folder/infinispan"
   mkdir infinispan
   cd infinispan
   fcp=`getCfg "infinispan_path"`
   fcf=`getCfg "infinispan_folder"`
   logstr "  start to backup infinispan from $fcf"
   tar -czvf infinispan.tar.gz -C $fcp $fcf 1>/dev/null
   logstr "  Infinispan backup done"
   cd ..
}


create_backup_folder()
{
   mkdir $backup_folder
   logstr "create $backup_folder"
   cd $backup_folder  
   cd ..
   rm -f last_backup
   ln -s $backup_folder last_backup
   cd $backup_folder
}

backup_base="/data/clariones/backup"
backup_date=$(date +%Y%m%d_%H_%M_%S)
backup_folder=$backup_base/$backup_date
db_host=`getCfg "db_host"` 
echo "DB host at $db_host"
echo $backup_folder
echo "`date +%D\ %T`: start to backcup..."
if [ -f $backup_base/backup.doing ]; then
  logstr "Another backup is ongoing. Abort."
  exit 1
fi

touch $backup_base/backup.doing
rm -f $backup_base/backup.done
create_backup_folder

while [ $# != 0 ];do  
    logstr "Got instrument $1"
    case $1 in
       "-db")
           backup_db
           ;;
       "-infinispan")
           backup_infinispan
           ;;
    esac
    shift  
done  

touch $backup_base/backup.done
rm -f $backup_base/backup.doing
