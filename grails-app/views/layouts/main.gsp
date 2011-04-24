<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
  <title><g:layoutTitle default="Booze" /></title>

  <link href="${resource(dir:'css',file:'main.css')}" rel="stylesheet" />
  <link href="${resource(dir:'/css/yaml/core/',file:'base.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/',file:'navigation.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/yaml/',file:'basemod.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/yaml/',file:'content.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/',file:'jquery-ui.css')}" rel="stylesheet" type="text/css" />

  <link href="${resource(dir:'/css/',file:'calculator.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/',file:'recipe.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/',file:'form.css')}" rel="stylesheet" type="text/css" />

  <g:javascript src="jquery.js" />
  <g:javascript src="jquery-ui.js" />
  <g:javascript src="booze.js" />
  <g:javascript src="booze.leftnav.js" />
  <g:javascript src="booze.calculator.js" />

  <g:layoutHead />

</head>
<body>
  <div class="page_margins">
    <div class="page">

      <g:render template="/navigation" />

      <div class="clearfix"></div>

      <div id="main" class="contentbox">
        <g:layoutBody />
      </div>

      <div class="clearfix"></div>
      <!-- begin: #footer -->
      <div id="footer" class="ui-widget ui-widget-content ui-corner-all">
        <div class="content">&copy;2011 Andreas Kotsias</div>
      </div>
      
      <div class="clearfix">&nbsp;</div>
    </div>
    <div class="clearfix">&nbsp;</div>
  </div>
</body>
</html>
