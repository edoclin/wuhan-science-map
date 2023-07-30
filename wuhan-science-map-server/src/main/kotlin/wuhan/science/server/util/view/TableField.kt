package wuhan.science.server.util.view

data class TableField(
    val name: String,
    val type: String,
    val label: String? = null,
    val fixed: Boolean? = false,
    val sortable: Boolean? = false,
    val condition: Boolean? = false,
    val ignored: Boolean? = false,
)