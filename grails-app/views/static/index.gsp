<html>
    <head>
        <title>Booze <g:meta name="app.version"></g:meta></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="home.headline" /></h1>

      <div class="singleColumn">
        <div style="float: left; width: 45%; padding-right: 5%; text-align: justify;">
          <h3 style="margin-bottom: 1em;">Booze <g:meta name="app.version" />
          <br /><span style="font-size: 0.6em; font-weight: normal">Software zur Verwaltung und Steuerung einer Kleinstbrauanlage</span>
          </h3>
          
          <p>
            <label style="float: left; font-weight: bold; margin-right: 10%; margin-bottom: 0.5em; width: 40%">Aktive Umgebung:</label>
            <span style="float: left; width: 40%; text-align: left"><setting:activeSettingName /></span>
            
            <div class="clear"></div>
            
            <label style="float: left; font-weight: bold; margin-right: 10%; margin-bottom: 0.5em; width: 40%">Verfügbare Rezepte:</label>
            <span style="float: left; width: 40%; text-align: left"><core:availableRecipeCount /></span>
            
            <div class="clear"></div>
            
            <label style="float: left; font-weight: bold; margin-right: 10%; margin-bottom: 0.5em; width: 40%">Brauprotokolle:</label>
            <span style="float: left; width: 40%; text-align: left"><core:availableProtocolCount /></span>
          
            <div class="clear"></div>
          </p>
          
          <p>
            Booze ist unter der <a href="${createLink(uri:'/license')}">GPL (General Public License) Version 3</a> lizensiert. Die Software darf im Rahmen dieser Lizenz vervielfältigt, verbreitet und verändert werden.
          </p>
          <p>Das Handbuch und weitere Informationen, vor allem auch zur Booze Hardware, sind unter http://booze.esnake.de verfügbar.</p>
        </div>
        
        <div style="float: left; width: 50%; text-align: right">
          <img src="${resource(dir:'/images', file:'home_picture.jpg')}" style="width: 400px" />
        </div>
      </div>
    </body>
</html>
