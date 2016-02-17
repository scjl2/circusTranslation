<#-- Template for generation of Modifiers -->

<#macro FieldModifiers field>
  <@AccessModifier field.modifiers/>
</#macro>

<#macro MethodModifiers method>
  <@AccessModifier method.modifiers/>
  <@SyncModifier method.modifiers/>
</#macro>

<#macro AccessModifier modifiers>
  <#if modifiers.isPublic()>\circpublic ~ </#if><#lt><@it/>
  <#if modifiers.isProtected()>\circprotected ~ </#if><#lt><@it/>
  <#if modifiers.isPrivate()>\circprivate ~ </#if><#lt><@it/>
</#macro>

<#macro SyncModifier modifiers>
  <#if modifiers.isSync()>\circsync ~ </#if><#lt><@it/>
</#macro>
