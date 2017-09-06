#/bin/bash
# huanghaifeng

function prepare()
{
        source ../conf/project.conf
}

function parse()
{
	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		if(article_gmp[$1] == "")
		{
			article_gmp[$1] = $2","$3","$4","$5
		}
		else
		{
			article_gmp[$1] = article_gmp[$1]";"$2","$3","$4","$5
		}
	}END{
		for(key in article_gmp)
		{
			print key":"article_gmp[key]
		}
	}' ${LOCAL_DATA_PATH}/valid-article-gmp.list > ${LOCAL_DATA_PATH}/valid-article-gmp-kafka.list

}

function write_to_kafka()
{
	cat ${LOCAL_DATA_PATH}/valid-article-gmp-kafka.list | /usr/local/kafka/bin/kafka-console-producer.sh --broker-list 172.31.4.53:9092 --topic article-gmp-synchronization
}


prepare
parse
write_to_kafka
