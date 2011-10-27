<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <meta name="layout" content="main"/>

  <link href="${resource(dir:'/css/',file:'brew.css')}" rel="stylesheet" type="text/css" />
  
  <title><g:message code="brew.initError.headline"/></title>
</head>
<body>

<div class="body">
  <div class="brew">
    <div class="initError">
      <h1>Fehler</h1>
      <div class="singleColumn">
        <h3><g:message code="brew.initError.message"/></h3>
      
        <p><g:message code="brew.initError.startupFailed"/></p>
        <div class="serverErrorMessage">
            ${msg.encodeAsHTML()}
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
