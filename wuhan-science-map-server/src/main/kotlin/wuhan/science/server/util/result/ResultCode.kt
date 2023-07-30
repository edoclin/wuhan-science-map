package wuhan.science.server.util.result

enum class ResultCode(val code: Int, val message: String) {
    SUCCESS(2000, "响应成功"),
    ERROR(5000, "服务器繁忙"),
    NOT_LOGIN(5100, "用户未登录"),
    TIMEOUT_LOGIN_FORM(5101, "长时间未登录身份已过期"),
    NEED_REGISTER_APP(5102, "用户尚未注册"),
    PARAM_ERROR(5200, "参数错误"),
}