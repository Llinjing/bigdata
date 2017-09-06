cat article-gmp.list | awk 'BEGIN{FS=OFS="\t"}{if($1>1016116717 && $NF>15 && $5>0.7){print $0}}END{}'
