package wuhan.science.server.util.result

import com.fasterxml.jackson.annotation.JsonInclude

data class ResultBean(val code: Int, val message: String = "响应成功", val data: Any? = null, val count: Number? = null, val timestamp: Number? = null)