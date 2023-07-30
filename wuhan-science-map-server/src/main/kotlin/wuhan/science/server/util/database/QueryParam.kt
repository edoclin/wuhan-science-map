package wuhan.science.server.util.database

import java.time.LocalDateTime

class QueryParam {
    var isAsc: Boolean = false
    var orderColumns: MutableList<String>? = null
    var conditions: MutableList<WebQueryCondition>? = null
    var createdBetween: MutableMap<String, LocalDateTime>? = null
    var updatedBetween: MutableMap<String, LocalDateTime>? = null
    var selector: MutableMap<String, Any>? = null
}
