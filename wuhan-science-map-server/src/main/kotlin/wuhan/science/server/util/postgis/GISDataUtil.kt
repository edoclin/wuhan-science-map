package wuhan.science.server.util.postgis

import org.geotools.geometry.jts.WKBReader
import kotlin.math.abs


object GISDataUtil {
    private var mapScale: Map<Int, Double>? = null

    init {
        val mutableMapOf = mutableMapOf<Int, Double>()
        // 微信小程序  <map> 组件高度为 300px 时上下的纬度差
        mutableMapOf[6] = 5.986983526
        mutableMapOf[7] = 3.015011495
        mutableMapOf[8] = 1.495405003
        mutableMapOf[9] = 0.752778484
        mutableMapOf[10] = 0.374875338
        mutableMapOf[11] = 0.187691579
        mutableMapOf[12] = 0.093340926
        mutableMapOf[13] = 0.047048971
        mutableMapOf[14] = 0.023587563
        mutableMapOf[15] = 0.011699172
        mutableMapOf[16] = 0.005849587
        mutableMapOf[17] = 0.002936619
        mutableMapOf[18] = 0.00146831
        mutableMapOf[19] = 0.000734155
        mutableMapOf[20] = 0.000369048

        mapScale = mutableMapOf.toMap()
    }

    fun list2PolygonString(list: List<List<Double>>): String {
        val builder = StringBuilder()
        builder.append("POLYGON((")

        list.forEach {
            builder.append("${it[0]} ${it[1]},")
        }
        builder.append("${list[0][0]} ${list[0][1]}")
        builder.append("))")

        return builder.toString()
    }

    fun list2PolylineString(list: List<List<Double>>): String {
        val builder = StringBuilder()
        builder.append("LINESTRING(")

        list.forEach {
            builder.append("${it[0]} ${it[1]},")
        }
        builder.append("${list[0][0]} ${list[0][1]}")
        builder.append(")")

        return builder.toString()
    }

    fun listPoint2PointString(list: List<Double>): String {
        return "POINT(${list[0]} ${list[1]})"
    }

    fun ewkb2Polygon(hex: String): List<Point> {
        val result = mutableListOf<Point>()
        val reader = WKBReader()
        val geometry = reader.read(WKBReader.hexToBytes(hex))
        geometry.coordinates.mapTo(result) {
            val item = Point()
            item.lng = it.x
            item.lat = it.y
            item
        }
        //        val geometryJson = GeometryJSON(6)
        return result
    }

    fun ewkb2Polygon4App(hex: String): List<AppPoint> {
        val result = mutableListOf<AppPoint>()
        val reader = WKBReader()
        val geometry = reader.read(WKBReader.hexToBytes(hex))
        geometry.coordinates.mapTo(result) {
            val item = AppPoint()
            item.longitude = it.x
            item.latitude = it.y
            item
        }
        return result
    }

    fun ewkb2MultiPolygon(hex: String?): List<List<Point>> {
        val result = mutableListOf<List<Point>>()
        val reader = WKBReader()
        val geometry = reader.read(WKBReader.hexToBytes(hex))

        for (i in 0 until geometry.numGeometries) {
            val points = mutableListOf<Point>()
            geometry.getGeometryN(i).coordinates.mapTo(points) {
                val item = Point()
                item.lng = it.x
                item.lat = it.y
                item
            }
            result.add(points.toList())
        }
        return result
    }

    fun ewkb2Array(hex: String): List<List<Double>> {
        val result = mutableListOf<List<Double>>()
        val reader = WKBReader()
        val geometry = reader.read(WKBReader.hexToBytes(hex))

        geometry.coordinates.mapTo(result) {
            val item = mutableListOf<Double>()
            item.add(it.x)
            item.add(it.y)
            item
        }
        //        val geometryJson = GeometryJSON(6)
        return result
    }

    fun ewkb2Point(hex: String, isCentroid: Boolean = false): Point {
        val result = Point()
        val reader = WKBReader()
        val geometry = reader.read(WKBReader.hexToBytes(hex))
        isCentroid.takeIf { it }?.let {
            result.lng = geometry.centroid.x
            result.lat = geometry.centroid.y
        } ?: let {
            result.lng = geometry.coordinate.x
            result.lat = geometry.coordinate.y
        }
        return result
    }

    fun ewkb2AppPoint(hex: String, isCentroid: Boolean = false): AppPoint {
        val result = AppPoint()
        val reader = WKBReader()
        val geometry = reader.read(WKBReader.hexToBytes(hex))
        var minLatitude = 91.0
        var maxLatitude = -91.0
        isCentroid.takeIf { it }?.let {
            result.longitude = geometry.centroid.x
            result.latitude = geometry.centroid.y
            result.area = geometry.area
            geometry.boundary.coordinates.forEach { coordinate ->
                if (coordinate.y < minLatitude) minLatitude = coordinate.y
                if (coordinate.y > maxLatitude) maxLatitude = coordinate.y
            }

            var delta = maxLatitude - minLatitude
            var delta0 = 181.0
            var scale = 0
            mapScale?.forEach { item ->
                val abs = abs(item.value - delta)
                if (delta0 > abs) {
                    delta0 = abs
                    scale = item.key
                }
            }
            result.mapScale = scale - 0.5

        } ?: let {
            result.longitude = geometry.coordinate.x
            result.latitude = geometry.coordinate.y
            result.area = geometry.area
        }
        return result
    }
}