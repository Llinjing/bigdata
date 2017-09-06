package inveno.bigdata.log

import org.slf4j.LoggerFactory

trait LogSupport {
  protected val log = LoggerFactory.getLogger(this.getClass.getSimpleName)
}
