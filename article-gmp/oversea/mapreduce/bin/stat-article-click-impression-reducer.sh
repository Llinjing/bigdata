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
		key = $1"\t"$2"\t"$3
		click[key] += $4
		impression[key] += $5
	}END{
		for (key in click)
		{
			print key, (click[key] + 0), (impression[key] + 0)
		}
	}'

	return 0
}


prepare

merge

