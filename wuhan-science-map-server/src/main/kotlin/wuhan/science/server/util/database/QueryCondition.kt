package wuhan.science.server.util.database


data class WebQueryCondition(var column: String, var value: String, var isAnd: Boolean = false)
