<#ftl strip_whitespace=true strip_text=true>

<#-- Miscellaneous Templates -->

<#-- Conversion between Logic and Boolean Values -->

<#macro LogicToBoolean>
<@compact>
  \IF ~ <#nested> ~ \THEN ~ jtrue ~ \ELSE ~ jfalse
</@compact>
</#macro>

<#macro BooleanToLogic>
<@compact>
  <#nested> = jtrue
</@compact>
</#macro>

<#-- Directives for Lists -->

<#macro PrintList expressions separator=", ">
<@compact>
<#list expressions as expression>
  ${TRANS(expression, CTXT)}<#if expression_has_next>${separator}</#if>
</#list>
</@compact>
</#macro>

<#-- Compact Blocks -->

<#macro compact>
<@compress single_line=true>
  <#nested>
</@compress>
</#macro>

<#-- Invalid Nodes -->

<#-- The following ought record a translation error too (TODO). -->

<#macro Invalid hint="">
<@compact>
<#if hint="">
  \invalid
<#else>
  \invalidpara{\texttt{${ENCNAME(hint)}}}
</#if>
</@compact>
</#macro>
