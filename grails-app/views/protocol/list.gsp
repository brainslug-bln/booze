<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>
  <title><g:message code="protocol.list.headline"/></title>
</head>
<body>

<div class="body">
  <h1>
    <g:message code="protocol.list.headline"/>
  </h1>
  
  <div class="protocolList itemList scrollList">
    <ul>
      <g:each in="${protocolInstanceList}" var="protocol">
      <li id="protocolList_item_${protocol.id}">
        <div class="recipeName" style="width: 78%">${fieldValue(bean: protocol, field: 'recipeName')}</div>
        <div class="dateCreated" style="width: 10%"><g:formatDate formatName="default.date.formatter" date="${protocol.dateStarted}"/></div>
        <div class="delete">
          <a class="booze-icon booze-icon-delete" href="#" onclick="return false;"></a>
        </div>
      </li>
      
      <script type="text/javascript" language="javascript">
        var li = $('#protocolList_item_${protocol.id}');
        li.click(function() {window.location.href='${createLink(controller:"protocol", action:"edit", id:protocol.id)}'});
        li.find('.delete').first().click(function(e) { 
          e.stopPropagation();
          booze.notifier.confirm('Soll das Protokoll \'${protocol.recipeName.encodeAsHTML()}\' wirklich gelöscht werden?', {modal: true, proceedCallback: function() {window.location.href='${createLink(controller:'protocol', action: 'delete', id:protocol.id)}'}})

        });
      </script>
      </g:each>
      
      <g:if test="${protocolInstanceList.size() < 1}">
          <li class="emptyList"><div><g:message code="protocol.list.noProtocolsAvailable" /></div></li>
        </g:if>
      <li></li>
    </ul>
  </div>
</div>

</body>
</html>
