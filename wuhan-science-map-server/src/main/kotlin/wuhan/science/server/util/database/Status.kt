package wuhan.science.server.util.database

import com.baomidou.mybatisplus.annotation.EnumValue
import com.fasterxml.jackson.annotation.JsonValue

enum class Status(value: Int, var desc: String) {
    DEFAULT(0, "默认状态"),
    NORMAL(1, "正常状态"),
    BANNED(2, "禁用状态");

    @EnumValue
    @JsonValue
    val status = value
}
