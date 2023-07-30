package pinhui.trip.server.entity

import net.postgis.jdbc.PGgeometry
import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import wuhan.science.server.WuHanScienceServerApplication
import wuhan.science.server.mapper.ScienceBaseMapper
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

class GeometryTypeHandler : BaseTypeHandler<String>() {
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: String?, jdbcType: JdbcType?) {
        WuHanScienceServerApplication.ac?.getBean(ScienceBaseMapper::class.java)?.let { mapper ->
            ps!!.setObject(i, PGgeometry(parameter?.let {
                mapper.text2Geometry(it)
            }))
        }
    }

    override fun getNullableResult(rs: ResultSet?, columnName: String?): String {
        return rs!!.getString(columnName)
    }

    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): String {
        return rs!!.getString(columnIndex)
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): String {
        return cs!!.getString(columnIndex)
    }

}