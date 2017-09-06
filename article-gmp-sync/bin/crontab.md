## sync us east request gmp
*/1 * * * * cd /data1/apps/article-gmp-sync/bin && sh -x article-gmp-request-sync-service.sh 1>../log/article-gmp-request-sync-service.log 2>&1

## article gmp impression sync
2-52/10 * * * * cd /data1/apps/article-gmp-sync/bin && sh -x article-gmp-impression-sync-service.sh 2 1>../log/article-gmp-impression-sync-service.log 2>&1
