#!/bin/bash

function prepare()
{
	chmod +x *

	source ./decay.conf

	return 0
}

function update()
{
	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		print $0 >$NF
	}'

	touch article-click-impression
	touch latest-article-gmp

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
	}{
		key = $1"\t"$2

		if (ARGIND == 1)
		{
			click[key] = $3
			impression[key] = $4
		}
		else
		{
			if (key in click)
			{
				click_num = $3 * '${ARTICLE_DECAY}' + click[key]
				impression_num = $4 * '${ARTICLE_DECAY}' + impression[key]
				ctr = -1
                                if (click_num<=impression_num && impression_num!=0)
                                {
                                        ctr = click_num / impression_num
                                }
				imprssion_total = $7+impression[key]
				print key, click_num, impression_num, ctr, "'${curr_date}'", imprssion_total, "#B"
				# count gmp article num
				if ($7<50 && imprssion_total>=50)
				{
					print key, "#A"
				}
				delete click[key]
				delete impression[key]
			}
			else
			{
                                click_num = $3 * '${ARTICLE_DECAY}' + 0
                                impression_num = $4 * '${ARTICLE_DECAY}' + 0
                                ctr = -1
                                if (click_num<=impression_num && impression_num!=0)
                                {
                                        ctr = click_num / impression_num
                                }
                                print key, click_num, impression_num, ctr, $6, $7, "#B"
			}
		}
	}END{
		for (key in click)
		{
			click_num = click[key]+0
			impression_num = impression[key]+0
			ctr = -1
			if (click_num<=impression_num && impression_num!=0)
			{
				ctr = click_num / impression_num
			}
			print key, click_num, impression_num, ctr, "'${curr_date}'", impression_num, "#B"
                        # count gmp article num
                        if (impression_num>=50)
                        {
				print key, "#A"
			}
		}
	}' article-click-impression latest-article-gmp

	rm -f article-click-impression latest-article-gmp

	return 0
}


curr_date=$1

prepare

update

