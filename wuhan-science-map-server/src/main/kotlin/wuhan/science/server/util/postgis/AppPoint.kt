package wuhan.science.server.util.postgis

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
class AppPoint {
    var longitude: Double? = null
    var latitude: Double? = null
    var area: Double? = null
    var mapScale: Double? = null
}
