curl -XDELETE http://localhost:9200/product_zh_default/product_zh/_query -d '{ "query" : {  "match_all" : {}  }}'

