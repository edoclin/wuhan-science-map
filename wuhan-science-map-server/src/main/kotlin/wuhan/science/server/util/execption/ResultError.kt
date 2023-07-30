package wuhan.science.server.util.execption

class ResultError(override val message: String?, val params: Any?): Exception(message) {
}