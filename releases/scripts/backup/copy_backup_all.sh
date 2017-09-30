tgt_host=10.9.57.223
bak_folder=/data/clarions/backup


ssh $tgt_host /data/clariones/backup/scripts/dobackup.sh -db -infinispan

folder=`ssh $tgt_host ls -ltr /data/clariones/backup/last_backup | awk '{print $11}' 2>/dev/null`
echo "remote folder is $folder"

scp -r root@$tgt_host:$folder $bak_folder/.
ssh $tgt_host "rm -rf $folder"

