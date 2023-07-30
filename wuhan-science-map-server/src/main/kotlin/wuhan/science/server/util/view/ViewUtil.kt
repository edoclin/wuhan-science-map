package wuhan.science.server.util.view

import cn.hutool.core.annotation.AnnotationUtil
import cn.hutool.core.util.ClassUtil
import cn.hutool.extra.template.Template
import cn.hutool.extra.template.TemplateConfig
import cn.hutool.extra.template.TemplateConfig.ResourceMode
import cn.hutool.extra.template.TemplateEngine
import cn.hutool.extra.template.TemplateUtil
import wuhan.science.server.entity.ZhihuiTripItem
import wuhan.science.server.util.date.DateUtil
import wuhan.science.server.util.view.annotation.*
import java.io.File
import java.time.LocalDateTime


object ViewUtil {

    @JvmStatic
    fun main(args: Array<String>) {
//        generateViewEntity("wuhan.science.server.entity")
        generateViewEntity(ZhihuiTripItem::class.java)
//        generateViewEntity(TrainCourseCategory::class.java)
    }

    inline fun <reified T> tableColumns(): List<TableColumn> {
        val result = mutableListOf<TableColumn?>()
        T::class.java.declaredFields.mapTo(result) { field ->
            if (field.getDeclaredAnnotation(Ignored::class.java) == null) {
                TableColumn(
                    field.name,
                    field.getDeclaredAnnotation(Label::class.java)?.value ?: let { "" },
                    field.getDeclaredAnnotation(Sortable::class.java) != null,
                    field.getDeclaredAnnotation(Fixed::class.java) != null,
                )
            } else {
                null
            }
        }
        return result.filterNotNull()
    }

    inline fun <reified T> queryCondition(): List<QueryCondition> {
        val result = mutableListOf<QueryCondition?>()
        T::class.java.declaredFields.mapTo(result) { field ->
            if (field.getDeclaredAnnotation(Condition::class.java) != null) {
                QueryCondition(
                    field.getDeclaredAnnotation(Label::class.java)?.value ?: let { "" },
                    field.name,
                )
            } else {
                null
            }
        }
        return result.filterNotNull()
    }

    private fun generateViewEntity(basePackage: String) {
        ClassUtil.scanPackage(basePackage)?.forEach { clazz ->
            generateViewEntity(clazz, basePackage)
        }
    }

    private fun <T> generateViewEntity(clazz: Class<T>, basePackage: String = clazz.packageName) {

        var path = System.getProperty("user.dir") + "/src/generator"
        basePackage.replace(".", "/").split("/").dropLast(1).forEach {
            path = path.plus("/$it")
        }
        path = path.plus("/view")

        File(path).takeIf {
            it.exists().not()
        }?.mkdirs()
        val map = mutableMapOf<String, Any?>()

        map["date"] = DateUtil.yyyyMMdd(LocalDateTime.now())
        map["package"] = basePackage
        map["author"] = "edoclin"

        val table = mutableMapOf<String, Any>()

        table["importPackages"] = mutableListOf(
            "kotlin.String", "${basePackage.subSequence(0, basePackage.lastIndexOf("."))}.util.view.annotation.*"
        )

        val fields = mutableListOf<TableField>()
        val engine: TemplateEngine = TemplateUtil.createEngine(TemplateConfig("templates", ResourceMode.CLASSPATH))
        val template: Template = engine.getTemplate("view.ftl")
        takeIf { AnnotationUtil.hasAnnotation(clazz, WebView::class.java) }?.let {
            fields.clear()
            clazz.declaredFields.forEach { field ->
                takeIf { AnnotationUtil.hasAnnotation(field, View::class.java) }?.let {
                    fields.add(
                        TableField(
                            field.name,
                            "String",
                            AnnotationUtil.getAnnotation(field, Label::class.java)?.value,
                            AnnotationUtil.hasAnnotation(field, Sortable::class.java),
                            AnnotationUtil.hasAnnotation(field, Fixed::class.java),
                            AnnotationUtil.hasAnnotation(field, Condition::class.java),
                            AnnotationUtil.hasAnnotation(field, Ignored::class.java),
                        )
                    )
                }
            }

            map["entity"] = "Web${clazz.simpleName}"
            table["fields"] = fields
            map["table"] = table
            map["json"] = true
            template.render(map, File(path, "${map["entity"].toString()}.kt"))


            map["entity"] = "WebPost${clazz.simpleName}"
            map["table"] = table
            map["json"] = false
            template.render(map, File(path, "${map["entity"].toString()}.kt"))


            map["entity"] = "WebPut${clazz.simpleName}"
            map["table"] = table
            map["json"] = false
            template.render(map, File(path, "${map["entity"].toString()}.kt"))

            map["entity"] = "App${clazz.simpleName}"
            map["table"] = table
            map["json"] = true
            template.render(map, File(path, "${map["entity"].toString()}.kt"))
        }
    }
}