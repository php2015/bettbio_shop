#!/bin/bash
FILES=pid.part*
for f in $FILES
do
  echo "process $f"
  lines=`wc -l $f | awk '{print $1}'`
  echo "    had $lines lines"
  cat $f | xargs -I % sed -e 's/P_ID/%/g' ./req.template >> $f.req
done

