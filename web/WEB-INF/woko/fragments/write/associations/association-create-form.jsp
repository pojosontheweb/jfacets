<h2><stripes:label for="woko.edit.association.create.title"/></h2>

<stripes:form beanclass="net.sf.woko.actions.CreateForAssociation" id="listForm">
    <p>
        <stripes:label for="woko.edit.association.create.msg"/>
        <stripes:select name="newObjectClass" id="newObjectClass">
            <stripes:options-collection collection="${actionBean.candidateClassesForCreation}"
                   label="simpleName" value="name"/>
        </stripes:select>
        <stripes:button name="create"
                    onclick="showFormAjax();"/>
        <stripes:hidden name="id" value="${actionBean.id}"/>
        <stripes:hidden name="className" value="${actionBean.className}"/>
        <stripes:hidden name="propertyName" value="${actionBean.propertyName}"/>
        <stripes:hidden name="propertyClass" value="${actionBean.propertyClass}"/>
    </p>
</stripes:form>