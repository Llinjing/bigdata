#!/bin/bash

function prepare()
{
	chmod +x *
	return 0
}

function merge()
{
	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		key = $1"\t"$2"\t"$3"\t"$4
		distinct[key]
	}END{
		for (key in distinct)
		{
			n = split(key, arr, "\t")
			if(arr[4] == "impression")
			{
				impression[arr[1]"\t"arr[2]] += 1
			}
			else if(arr[4] == "click")
			{
				click[arr[1]"\t"arr[2]] += 1
			}
		}

		for (key in impression)
		{
			print key, click[key]+0, impression[key]+0
			delete click[key]
		}

		for (key in click)
		{
			print key, click[key]+0, impression[key]+0
		}
	}'

	return 0
}


prepare
merge

