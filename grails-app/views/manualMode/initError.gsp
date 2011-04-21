<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>

  <title><g:message code="brew.initError.headline"/></title>

  <jawr:script src="/bundles/brew.js"/>
</head>
<body>

<div class="body">
  <div class="brew">
    <div class="initError">
      <h3><g:message code="brew.initError.message"/></h3>

      <p><g:message code="brew.initError.startupFailed"/></p>
      <div class="container">
        <pre>
          ${msg.encodeAsHTML()}
        </pre>
      </div>

    </div>
  </div>
</div>
</body>
</html>
