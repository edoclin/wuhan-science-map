package wuhan.science.server.view.common

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class Region {
    var code: Int? = null
    var regionName: String? = null
    var parentCode: Int? = null
    var polygonGeometry: Any? = null
    var children: List<Region>? = null
}