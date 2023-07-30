package wuhan.science.server.util.view

data class TableColumn(
    var column: String,
    var label: String,
    var sortable: Boolean = false,
    var fixed: Boolean = false
)
