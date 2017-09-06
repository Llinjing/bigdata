# -*- coding: UTF-8 -*-
import sys
import redis

# Args Input filter
# if len(sys.argv) < 3:
#    print "python server port  HASH_KEY_NAME"
#    sys.exit()
#else:
#    hashkey = sys.argv[1]

# redis_hash_del online

def redis_hash_del(server, port, hashkey):

    r = redis.Redis(host=server, port=port, db=0)
    if r.exists(hashkey):
        hashkey_all = r.hkeys(hashkey)
        for i in range(len(hashkey_all)):
            r.hdel(hashkey, hashkey_all[i])
            print i
    else:
        print "KEY NOT EXISTS"


if __name__ == "__main__":
    if len(sys.argv) < 3:
        print "python server port HASH_KEY_NAME"
    else:
        server = sys.argv[1]
        port = int(sys.argv[2])
        hashkey = sys.argv[3]
        redis_hash_del(server, port, hashkey)




