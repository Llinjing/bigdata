#! /usr/bin
#
# huanghaifeng
#

function supplement_data()
{
        echo '1' > ./mark
        i=1
        for file in `ls ../data/input/`
        do
                if [ ${i} -gt 1 ]
                then
                        break
                fi
                mv ../data/input/${file} ../data/waiting-running/
                let i=${i}+1
        done

        sh -x ../bin/schedule-manager.sh 1>../log/schedule-manage.log 2>&1

        echo 'finish once task'
        echo '0' > ./mark
}


function schedule()
{
	touch ./mark
	for (( j=1; j<=2; j++ ))
	do
		local text=`cat ./mark`
		echo ${text}
		if [ ${text} -eq 1 ]
		then
			echo 'job already started, sleep'
			sleep 10s
		else
			echo 'start a news job'
			supplement_data
		fi
		echo 'finish loop-'$j
	done

}

schedule


