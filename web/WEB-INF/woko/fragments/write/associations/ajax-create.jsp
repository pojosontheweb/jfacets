<%@ include file="/WEB-INF/woko/taglibs.jsp"%>

<div id="daAjaxMessagesZone">
    <div class="messages">
        <ul class="messages">
            <li><stripes:label for="woko.edit.association.create.msg2"/></li>
        </ul>
    </div>
</div>

<div class="edited-object">
    <stripes:form beanclass="net.sf.woko.actions.CreateForAssociation" id="createForm">
        <woko:execAndInclude facetName="editProperties" obj="${actionBean.object}"/>
        <stripes:hidden name="id" value="${actionBean.id}"/>
        <stripes:hidden name="className" value="${actionBean.className}"/>
        <stripes:hidden name="propertyName" value="${actionBean.propertyName}"/>
        <stripes:hidden name="newObjectClass" value="${actionBean.newObjectClass}"/>
        <stripes:hidden name="_eventName" value="createAndAdd"/>
        <stripes:button name="create" onclick="submitFormAjax();"/>
    </stripes:form>
</div>



