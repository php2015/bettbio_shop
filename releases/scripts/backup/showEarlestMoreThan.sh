ls -t| grep "^20.*" | awk -v CNT="$1" '{ if(NR>CNT) {print $0}}'
