<script type="text/javascript"
  src="${pageContext.request.contextPath}/woko/js/prototype.js"></script>
<script type="text/javascript" xml:space="preserve">
  function showFormAjax() {
      var elementHTML = document.getElementById('daAjaxZone');
      var form = document.getElementById("listForm")
      var params = Form.serialize(form);
      new Ajax.Updater(elementHTML, form.action, {method:'post', parameters:params});
  }
  function submitFormAjax() {
      var elementHTML = document.getElementById('daAjaxMessagesZone');
      var form = document.getElementById("createForm");
      var params = Form.serialize(form);
      new Ajax.Updater(elementHTML, form.action, {method:'post', evalScripts:true, parameters:params});
  }
</script>