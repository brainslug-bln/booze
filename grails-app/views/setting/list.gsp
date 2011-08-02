<html>
    <head>
        <title><g:message code="setting.list.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="setting.list.headline" /></h1>
      <div class="itemList scrollList">
        <ul>
          <g:each in="${settings}" var="setting">
          <li id="settingList_item_${setting.id}">
            <div class="name" style="width: 81%"><g:fieldValue bean="${setting}" field="name" /></div>
            <div class="active" style="width: 7%"><g:if test="${setting.active == true}">aktiv</g:if></div>
            <div class="delete">
              <a class="booze-icon booze-icon-delete" href="#" onclick="return false;"></a>
            </div>
          </li>
          
          <script type="text/javascript" language="javascript">
              var li = $('#settingList_item_${setting.id}');
              li.click(function() {window.location.href='${createLink(controller:"setting", action:"edit", id:setting.id)}'});
              li.find('.delete').first().click(function(e) { 
                e.stopPropagation();
                booze.notifier.confirm('Soll die Umgebung \'${setting.name.encodeAsHTML()}\' wirklich gelöscht werden?', {modal: true, proceedCallback: function() {window.location.href='${createLink(controller:'setting', action: 'delete', id:setting.id)}'}})
              
              });
            </script>
          </g:each>
          <g:if test="${settings.size() < 1}">
            <li class="emptyList" onclick="window.location.href='${createLink(controller:'setting', action:'create')}'"><div><g:message code="setting.list.noSettingsAvailable" /></div></li>
          </g:if>
          
          <li></li>
        </ul>
      </div>
    </body>
</html>
