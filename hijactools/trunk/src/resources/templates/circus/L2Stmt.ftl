<#ftl strip_whitespace=true strip_text=true>

<#-- Templates for translating Statements -->

<#include "L2Misc.ftl">

<#----------------->
<#-- Assert Tree -->
<#----------------->

<#macro Assert condition>
<#if IsFalse(condition)>
  \Div
<#else>
  \circif ~ \lnot (${TRANS(condition, CTXT.enterExpression())}) \circthen \Div
  \\
  \circelse ~ ~ ${TRANS(condition, CTXT.enterExpression())} \circthen \Skip
  \\
  \circfi
</#if>
</#macro>

<#function IsFalse condition>
  <#return TRANS(condition, CTXT.enterExpression()) = "false">
</#function>

<#--------------------->
<#-- Assignment Tree -->
<#--------------------->

<#macro Assignment variable expression>
<#if variable.type="boolean">
  <@BooleanAssignment variable expression/> ~
<#else>
  <@ValueAssignment variable expression/> ~
</#if>
</#macro>

<#macro BooleanAssignment variable expression>
<@compact>
  ${TRANS(variable, CTXT)} := 
<@LogicToBoolean>
  ${TRANS(expression, CTXT.enterExpression())}
</@LogicToBoolean>
</@compact>
</#macro>

<#macro ValueAssignment variable expression>
<@compact>
  ${TRANS(variable, CTXT)} :=
  ${TRANS(expression, CTXT.enterExpression())}
</@compact>
</#macro>

<#---------------->
<#-- Block Tree -->
<#---------------->


<#macro Block statements>

<#if statements?has_content>
  \circblockopen
<#list statements as statement>
<#assign stmtTrans=TRANS(statement, CTXT)>

<#if stmtTrans="">
<#if statement_has_next>

<#else>
\Skip
</#if>

<#elseif stmtTrans?contains("~?~")>
	${stmtTrans}
<#if statement_has_next>

<#else>
 \Skip<#lt/>
</#if> 
<#else>

${stmtTrans}<#lt/> 

<#if statement_has_next>
   \circseq \\ 
</#if>
</#if>
</#list>
  \circblockclose
<#else>
  <@EmptyStatement/>
</#if>

</#macro>

<#----------------------------->
<#-- CompoundAssignment Tree -->
<#----------------------------->

<#macro CompoundAssignment variable expression>
<@compact>
  ${TRANS(variable, CTXT)} :=
  ${TRANS(variable, CTXT)} <@CompoundAssignmentOp NODE.kind NODE.type/>
  ${TRANS(expression, CTXT.enterExpression())}
</@compact>
</#macro>

<#macro CompoundAssignmentOp kind type>
<@compact>
<#if kind="PLUS_ASSIGNMENT">
  <#if type="java.lang.String"> \cat <#else> + </#if>
<#elseif kind="MINUS_ASSIGNMENT">
  -
<#elseif kind="MULTIPLY_ASSIGNMENT">
  *
<#elseif kind="DIVIDE_ASSIGNMENT">
  \div
<#elseif kind="REMAINDER_ASSIGNMENT">
  \mod
<#else>
  <@Invalid kind/>
</#if>
</@compact>
</#macro>

<#------------------------->
<#-- EmptyStatement Tree -->
<#------------------------->

<#macro EmptyStatement>
  \Skip
</#macro>

<#------------------------------>
<#-- ExpressionStatement Tree -->
<#------------------------------>

<#macro ExpressionStatement expression>
  ${TRANS(expression, CTXT)}<#lt/>
</#macro>

<#------------->
<#-- If Tree -->
<#------------->

<#macro If condition thenstatement elsestatement="">
<#if elsestatement?has_content>
  \circif ~ ${TRANS(condition, CTXT.enterExpression())} ~ \circthen ~
  \\
  \t1
  ${TRANS(thenstatement, CTXT)}<#lt/>
  \\
  \circelse ~ \lnot ${TRANS(condition, CTXT.enterExpression())} ~ \circthen ~
  \\
  \t1
  ${TRANS(elsestatement, CTXT)}<#lt/>
  \\
  \circfi
<#else>
  \circif ~ ${TRANS(condition, CTXT.enterExpression())} ~ \circthen ~
  \\
  \t1
  ${TRANS(thenstatement, CTXT)}<#lt/>
  \\
  \circelse ~ \lnot ${TRANS(condition, CTXT.enterExpression())} \circthen \Skip
  \\
  \circfi
</#if>
</#macro>

<#------------------>
<#-- ForLoop Tree -->
<#------------------>

<#macro ForLoop initializer condition statement update>
  \circblockopen
  <@ForLoopInit initializer/><#lt/>
  \circseq
  \\
  \circblockopen
  \circmu X \circspot
  \\
  \circblockopen
  \circif ~ ${TRANS(condition, CTXT.enterExpression())} ~ \circthen ~
  \\
  \t1
  \circblockopen
  ${TRANS(statement, CTXT)}<#lt/>
  \circseq
  \\
  <@ForLoopUpdate update/>
  \circblockclose
  \circseq X
  \\
  \circelse ~ ~ \lnot ${TRANS(condition, CTXT.enterExpression())} \circthen \Skip
  \\
  \circfi
  \circblockclose
  \circblockclose
  \circblockclose
</#macro>

<#macro ForLoopInit initializer>
<#if initializer?has_content>
<#list initializer as initializer>
  ${TRANS(initializer, CTXT)}<#lt/>
<#if initializer_has_next>
  \circseq
  \\
</#if>
</#list>
<#else>
  \Skip
</#if>
</#macro>

<#macro ForLoopUpdate update>
  \circblockopen
<#if update?has_content>
<#list update as update>
  ${TRANS(update, CTXT)}<#lt/>
<#if update_has_next>
  \circseq
  \\
</#if>
</#list>
<#else>
  \Skip
</#if>
  \circblockclose
</#macro>

<#--------------------------->
<#-- LabeledStatement Tree -->
<#--------------------------->

<#macro LabeledStatement statement>
  ${TRANS(statement, CTXT)}<#lt/>
</#macro>

<#----------------->
<#-- Return Tree -->
<#----------------->

<#macro Return expression>
  ret := ${TRANS(expression, CTXT.enterExpression())}
</#macro>

<#----------------->
<#-- Switch Tree -->
<#----------------->

<#macro Switch expression cases>
  \circif ~ 
<#list cases as case>
  ${TRANS(case, CTXT.enterExpression().setSwitch(NODE))}<#lt/>
  \\
<#if case_has_next>
  \circelse ~ ~ 
</#if>
</#list>
  \circfi
</#macro>

<#--------------->
<#-- Case Tree -->
<#--------------->

<#------------------------->
<#-- REVIEWED UNTIL HERE -->
<#------------------------->

<#-- What about CTXT.enterExpression() below? -->

<#macro Case expression="" statements="">
<#if expression?has_content>
  ${TRANS(CTXT.getSwitch().getExpression(), CTXT)} = ${TRANS(expression, CTXT)} ~ \then
  \\
  \t1
  <@Block statements/>
<#else>
  <@DefaultGuard/>
  ~ \then
  \\
  \t1
  <@Block statements/>
</#if>
</#macro>

<#macro DefaultGuard>
  \lnot
  \circblockopen
<#list CTXT.getSwitch().getCases() as case>
<#if case.getExpression()??>
  ${TRANS(CTXT.getSwitch().getExpression(), CTXT)} = ${TRANS(case.getExpression(), CTXT)}
<#if case_has_next>
  \lor
  \\
</#if>
<#else>
  false
</#if>
</#list>
  \circblockclose
</#macro>

<#---------------->
<#-- Break Tree -->
<#---------------->

<#macro Break>
  \Skip
</#macro>

<#------------------->
<#-- Variable Tree -->
<#------------------->

<#macro Variable name type initializer="">
<#if initializer?has_content>
  <@InitVariable name type initializer/> ~
<#else>
  <@PlainVariable name type/> ~
</#if>
</#macro>

<#macro InitVariable name type initializer>
<@compact>
  \circvar ${name} : ${type} \circspot
  ${name} := ${TRANS(initializer, CTXT.enterExpression())}
</@compact>
</#macro>

<#macro PlainVariable name type>
<@compact>
  \circvar ${name} : ${type} \circspot \Skip<#lt/>
</@compact>
</#macro>

<#-------------------->
<#-- WhileLoop Tree   <@keep_newlines>  </@keep_newlines> -->
<#-------------------- \circblockopen    \circblockclose  -->


<#macro WhileLoop condition statement>
<@compact>

  \circblockopen
  \circmu X \circspot
  \\
  \circblockopen
  \circif ~ ${TRANS(condition, CTXT.enterExpression())} ~ \circthen ~
  \\
  \t1
  
  ${TRANS(statement, CTXT)}

  \circseq X
  \\
  \circelse ~ ~ \lnot ${TRANS(condition, CTXT.enterExpression())} \circthen \Skip
  \\
  \circfi
  \circblockclose
  \circblockclose
	\\
</@compact>
</#macro>
