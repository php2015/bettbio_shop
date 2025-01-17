bak_folder=/data/clarions/backup
tgt_host=10.9.57.223

echo "`date`: start to backup DB"
ssh $tgt_host /data/clariones/backup/scripts/dobackup.sh -db

folder=`ssh $tgt_host ls -ltr /data/clariones/backup/last_backup | awk '{print $11}' 2>/dev/null`
echo "remote folder is $folder"

scp -r root@$tgt_host:$folder $bak_folder/.
ssh $tgt_host "rm -rf $folder"
echo "`date`: backup DB finished"


