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

	## article_id<\t>product_id<\t>language<\t>click<\t>impression
	## article_id<\t>product_id<\t>click<\t>impression<\t>gmp<\t>update_date<\t>total_impression<\t>language
	touch article-click-impression
	touch latest-article-gmp

	awk 'BEGIN{
		FS = "\t"
		OFS = "\t"
		OFMT = "%.8f"
	}{
		key = $1"\t"$2

		if (ARGIND == 1)
		{
			language[key] = $3
			click[key] = $4
			impression[key] = $5
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
				## remenber, this is just temporary
				##if (ctr==-1 && imprssion_total>15 && click_num>impression_num)
				##{
				##	print key, 8, 20, 0.4, "'${curr_date}'", imprssion_total
				##}
				##else
				##{
				##	print key, click_num, impression_num, ctr, "'${curr_date}'", imprssion_total
				##}
				print key, click_num, impression_num, ctr, "'${curr_date}'", imprssion_total, language[key]
				# count gmp article num
				if ($7<'${IMPRESSION_THRESHOLD}' && imprssion_total>='${IMPRESSION_THRESHOLD}')
				{
					print key, "#A"
				}
				delete click[key]
				delete impression[key]
				delete language[key]
			}
			else
			{
                                click_num = $3 * '${ARTICLE_DECAY}' + 0
                                impression_num = $4 * '${ARTICLE_DECAY}' + 0
				## remenber, this is just temporary
				##if (ctr>=0.4 && $7>200)
				##{
                                ##	print key, 4, 20, 0.4, $6, 20
				##}
				##else
				##{
                                ##	print key, click_num, impression_num, ctr, $6, $7
				##}				
				if ($8 == "latest-article-gmp")
				{
                              		print key, click_num, impression_num, $5, $6, $7
				}
				else
				{
					print key, click_num, impression_num, $5, $6, $7, $8
				}
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
			print key, click_num, impression_num, ctr, "'${curr_date}'", impression_num, language[key]
                        # count gmp article num
                        if (impression_num >= '${IMPRESSION_THRESHOLD}')
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

