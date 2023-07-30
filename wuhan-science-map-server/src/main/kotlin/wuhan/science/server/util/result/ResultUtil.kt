package wuhan.science.server.util.result


import wuhan.science.server.util.execption.ResultError

object ResultUtil {
    private fun build(code: Int, message: String, data: Any?, count: Number? = null, timestamp: Number? = null): ResultBean {
        return ResultBean(code, message, data, count, timestamp)
    }

    fun ok(): ResultBean {
        return build(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message, null)
    }


    fun ok(data: Any): ResultBean {
        return build(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message, data)
    }

    fun ok(data: Any, count: Number): ResultBean {
        return build(ResultCode.SUCCESS.code, ResultCode.SUCCESS.message, data, count)
    }

    fun ok(message: String, data: Any): ResultBean {
        return build(ResultCode.SUCCESS.code, message, data)
    }

    fun needRegister(data: Any?): ResultBean {
        return build(ResultCode.NEED_REGISTER_APP.code, ResultCode.NEED_REGISTER_APP.message, data)
    }

    fun error(message: String): ResultBean {
        throw RuntimeException(message)
    }

    fun error(message: String, params: Any?): ResultBean {
        throw ResultError(message, params)
    }

    fun exception(message: String, timestamp: Number): ResultBean {
        return build(ResultCode.ERROR.code, message, null, 0, timestamp)
    }

    fun error(): ResultBean {
        return error(ResultCode.ERROR.message)
    }

    fun notLogin(): ResultBean {
        return build(ResultCode.NOT_LOGIN.code, ResultCode.NOT_LOGIN.message, null)
    }

    fun timeoutLoginForm(): ResultBean {
        return build(ResultCode.TIMEOUT_LOGIN_FORM.code, ResultCode.TIMEOUT_LOGIN_FORM.message, null)
    }
}