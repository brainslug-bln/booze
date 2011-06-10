<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
  <meta http-equiv="Content-type" content="text/html; charset=utf-8" />
  <title><g:layoutTitle default="Booze" /></title>

  <core:settingsFontSize />
  <link href="${resource(dir:'css',file:'main.css')}" rel="stylesheet" />
  <link href="${resource(dir:'/css/yaml/core/',file:'base.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/yaml/',file:'basemod.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/yaml/',file:'content.css')}" rel="stylesheet" type="text/css" />
  <link href="${resource(dir:'/css/',file:'jquery-ui.css')}" rel="stylesheet" type="text/css" />

  <link href="${resource(dir:'/css/',file:'calculator.css')}" rel="stylesheet" type="text/css" />
   <link href="${resource(dir:'/css/',file:'brew.css')}" rel="stylesheet" type="text/css" />

  <g:javascript>
      var APPLICATION_ROOT = "${resource(dir: '', absolute: true)}";      
      var APPLICATION_HOME = "${createLink(uri:'/home')}";
  </g:javascript>

  <g:javascript src="jquery.js" />
  <g:javascript src="jquery-ui.js" />
  <g:javascript src="jquery.tmpl.js" />
  <g:javascript src="jquery.sound.js" />
  <g:javascript src="booze.js" />
  <g:javascript src="booze.messageSource.js" />
  <g:javascript src="booze.logger.js" />
  <g:javascript src="booze.notifier.js" />
  <g:javascript src="booze.calculator.js" />
  <g:javascript src="booze.brew.js" />
  
  <g:layoutHead />
</head>
<body>
  <div class="page_margins">
    <div class="page">

      <div id="main" class="contentbox">
        <g:layoutBody />
      </div>

      <div class="clearfix">&nbsp;</div>
    </div>
    <div class="clearfix">&nbsp;</div>
  </div>
</body>
</html>
