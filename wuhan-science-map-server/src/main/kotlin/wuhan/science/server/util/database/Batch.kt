package wuhan.science.server.util.database

import javax.validation.constraints.NotNull

class Batch {
    @NotNull
    var ids: MutableList<String?>? = null
}