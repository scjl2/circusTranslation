<#ftl strip_whitespace=true strip_text=true>

<#-- Templates for translating Expressions -->

<#include "L2Misc.ftl">

<#-- ArrayAccess Tree -->

<#macro ArrayAccess expression index>
<@compact>
  ${TRANS(expression, CTXT)} ~ (${index})
</@compact>
</#macro>

<#-- Binary Tree -->

<#macro Binary expression1 expression2>
<@compact>
  (${TRANS(expression1, CTXT)}
  <@BinaryOp NODE.kind NODE.type/>
  ${TRANS(expression2, CTXT)})
</@compact>
</#macro>

<#macro BinaryOp kind type>
<@compact>
<#if kind="PLUS">
  <#if type="java.lang.String"> \cat <#else> + </#if>
<#elseif kind="MINUS">
  -
<#elseif kind="MULTIPLY">
  *
<#elseif kind="DIVIDE">
  \div
<#elseif kind="REMAINDER">
  \mod
<#elseif kind="CONDITIONAL_AND">
  \land
<#elseif kind="CONDITIONAL_OR">
  \lor
<#elseif kind="EQUAL_TO">
  =
<#elseif kind="NOT_EQUAL_TO">
  \neq
<#elseif kind="LESS_THAN">
  <
<#elseif kind="LESS_THAN_EQUAL">
  \leq
<#elseif kind="GREATER_THAN">
  >
<#elseif kind="GREATER_THAN_EQUAL">
  \geq
<#elseif kind="AND">
  \land
<#else>
<@Invalid kind/>
</#if>
</@compact>
</#macro>

<#------------------------->
<#-- REVIEWED UNTIL HERE -->
<#------------------------->

<#-- ConditionalExpression Tree -->

<#macro ConditionalExpression condition true_expression false_expression>
<@compact>
  \IF ~ ${TRANS(condition, CTXT)} ~ \THEN ~ ${TRANS(true_expression, CTXT)} ~ \ELSE ~ ${TRANS(false_expression, CTXT)} \circseq \\
</@compact>
</#macro>

<#-- Identifier Tree -->

<#-- Is this identifier wihtin an expression? -->

<#macro Identifier name>
<@compact>
<#if NODE.type="boolean" && CTXT.isInsideExpression()>
  <@BooleanToLogic>${name}</@BooleanToLogic>
<#else>
  ${name}
</#if>
</@compact>
</#macro>

<#-- MemberSelect Tree -->

<#macro MemberSelect expression identifier>
<@compact>
  ${TRANS(expression, CTXT)}.${identifier}
</@compact>
</#macro>

<#-- MethodInvocation Tree -->

<#-- What about calls to methods with action models? -->

<#macro MethodInvocation method_select arguments>
  ${TRANS(method_select, CTXT)}(<@ParamList arguments/>)  
</#macro>

<#macro ParamList arguments="">
<@compact>
  <@PrintList arguments/>
</@compact>
</#macro>

<#-- NewClass Tree -->

<#macro NewClass identifier arguments="">
<@compact>
<#if arguments?has_content>
  \circnew ${TRANS(identifier, CTXT)}Class(<@ParamList arguments/>)
<#else>
  \circnew ${TRANS(identifier, CTXT)}Class
</#if>
  <#-- <@Register/> -->
</@compact>
</#macro>

<#-- There is a problem. We are not in the right data model anymore.  -->

<#macro Register>
<@compact>
<#if class.isHandler()>
  \circseq register~.~${handlerId} \then \Skip
</#if>
</@compact>
</#macro>

<#-- Parenthesized Tree -->

<#macro Parenthesized expression>
<@compact>
  ${TRANS(expression, CTXT)}
</@compact>
</#macro>

<#-- Unary Tree -->

<#macro Unary expression>
<#if NODE.kind="PREFIX_INCREMENT">
  ${TRANS(expression, CTXT)} := ${TRANS(expression, CTXT)} + 1
<#elseif NODE.kind="PREFIX_DECREMENT">
  ${TRANS(expression, CTXT)} := ${TRANS(expression, CTXT)} - 1
<#elseif NODE.kind="POSTFIX_INCREMENT">
  ${TRANS(expression, CTXT)} := ${TRANS(expression, CTXT)} + 1
<#elseif NODE.kind="POSTFIX_DECREMENT">
  ${TRANS(expression, CTXT)} := ${TRANS(expression, CTXT)} - 1
<#else>
  (<@UnaryOp NODE.kind NODE.type/> ${TRANS(expression, CTXT.enterExpression())})
</#if>
</#macro>

<#macro UnaryOp kind type>
<#compress>
<#if kind="UNARY_PLUS">
<#elseif kind="UNARY_MINUS"> \negate
<#elseif kind="LOGICAL_COMPLEMENT"> \lnot
<#else><@Invalid kind/>
</#if>
</#compress>
</#macro>
