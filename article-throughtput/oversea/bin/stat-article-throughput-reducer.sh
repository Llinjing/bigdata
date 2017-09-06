#!/bin/bash

function prepare()
{
	    chmod +x *
    	source ./project.conf
	    return 0
}

function merge()
{
	    date_mark=`date +%Y%m%d%H%M%S`

        awk 'BEGIN{
                FS = "\t"
                OFS = "\t"
        }{
                key = $1"\t"$2"\t"$3"\t"$4"\t"$5
                if ($NF == "impression")
                {
                        impression[key] += $6
                }
                else if ($NF == "request")
                {
                        request[key] += $6
                }
                else if ($NF == "article-total-feedback")
                {
	            		## content_id<\t>product_id<\t>language<\t>content_type<\t>platform<\t>request<\t>impression<\t>date<\t>first_date
                        article_total_feedback_request[key] = $6
                        article_total_feedback_impression[key] = $7
                        article_total_feedback_date[key] = $8
                        article_total_feedback_first_date[key] = $9
                }
        }END{
                for (key in request)
                {
                        if (key in article_total_feedback_request) #request update
                        {
                                new_request = request[key] + article_total_feedback_request[key]
                                if (article_total_feedback_request[key]<'${REQUEST_THRESHOLD}' && new_request>='${REQUEST_THRESHOLD}')
                                {
                                    print key, article_total_feedback_first_date[key], "request", "#B"
                                }                    
                                article_total_feedback_request[key] = new_request
                                article_total_feedback_date[key] = "'${curr_date}'"
                        }
                        else #request new
                        {
                                new_request = request[key]
                                if (new_request>='${REQUEST_THRESHOLD}')
                                {
                                    print key, "'${date_mark}'", "request", "#B"
                                }
                                article_total_feedback_request[key] = new_request
                                article_total_feedback_first_date[key] = "'${date_mark}'"
                                article_total_feedback_date[key] = "'${curr_date}'"
                        }
                }
                
                for (key in impression) #impression update
                {
                        if (key in article_total_feedback_impression)
                        {
                                new_impression = impression[key] + article_total_feedback_impression[key]
                                if (article_total_feedback_impression[key]<'${IMPRESSION_THRESHOLD}' && new_impression>='${IMPRESSION_THRESHOLD}')
                                {
                                    print key, article_total_feedback_first_date[key], "impression", "#B"
                                }                    
                                article_total_feedback_impression[key] = new_impression
                                article_total_feedback_date[key] = "'${curr_date}'"
                        }
                        else 
                        {
                                new_impression = impression[key]
                                if (new_impression>='${IMPRESSION_THRESHOLD}')
                                {
                                    print key, "'${date_mark}'", "impression", "#B"
                                }
                                article_total_feedback_impression[key] = new_impression
                                article_total_feedback_first_date[key] = "'${date_mark}'"
                                article_total_feedback_date[key] = "'${curr_date}'"
                        }
                }

                for (key in article_total_feedback_first_date) #output article_total_feedback
                {
                        print key, article_total_feedback_request[key]+0, article_total_feedback_impression[key]+0, article_total_feedback_date[key], article_total_feedback_first_date[key], "#A"
                }
        }'

        return 0
}
curr_date=$1

prepare

merge

