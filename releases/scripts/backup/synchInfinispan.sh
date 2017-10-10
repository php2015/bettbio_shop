echo "Start sync infinispan"
date
rsync -a root@123.59.134.4:/udisk/infinispan/ /data/backup/bettbio_shop/infinispan_data/
date
echo "Done"
