package com.inveno.news.log

import org.slf4j.LoggerFactory

/**
 * Created by dory on 2016/11/28.
 */
trait LogSupport {
    protected val log = LoggerFactory.getLogger(this.getClass.getSimpleName)
}
