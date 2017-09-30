update PRODUCT p left join PRODUCT_CATEGORY c on p.PRODUCT_ID=c.PRODUCT_ID left join CATEGORY d on c.CATEGORY_ID=d.CATEGORY_ID set p.AVAILABLE=1  where (d.LINEAGE like '/1/%' or c.CATEGORY_ID=1) and p.AVAILABLE=0;

