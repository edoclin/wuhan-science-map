package ${package}

<#list table.importPackages as pkg>
import ${pkg}
</#list>
<#if json??>
import com.fasterxml.jackson.annotation.JsonInclude
</#if>
/**
* @author ${author}
* @since ${date}
*/

<#if json>
@JsonInclude(JsonInclude.Include.NON_EMPTY)
</#if>
class ${entity} {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.label??>
    @Label("${field.label}")
    </#if>
    <#if field.sortable>
    @Sortable
    </#if>
    <#if field.fixed>
    @Fixed
    </#if>
    <#if field.condition>
    @Condition
    </#if>
    <#if field.ignored>
    @Ignored
    </#if>
    var ${field.name}: ${field.type}? = null

</#list>
}
