<#ftl strip_whitespace=true strip_text=true>

<#-- Templates for translating Types -->

<#include "L2Misc.ftl">

<#macro PrimitiveType kind>
<@compact>
<#if kind="BOOLEAN"> \boolean
<#elseif kind="BYTE">byte
<#elseif kind="CHAR">char
<#elseif kind="DOUBLE">\real
<#elseif kind="FLOAT">\real
<#elseif kind="INT">int
<#elseif kind="LONG">long
<#elseif kind="SHORT">short
<#else><@Invalid kind/>
</#if>
</@compact>
</#macro>
