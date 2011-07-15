<html>
    <head>
        <title>Booze <g:meta name="app.version"> - Versionsinformationen</g:meta></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="version.headline" /></h1>
      <div class="singleColumn">
        <h3>Über Version <g:meta name="app.version"></g:meta></h3>
        
        <h5>Änderungen in Version 2.0</h5>
          <ul>
            <li>
              Die komplette grafische Oberfläche wurde überarbeitet und für die Bedienung mit einem Touchdisplay optimiert.
            </li>
            <li>
              Das Treiberframework wurde komplett neu geschrieben. Treiber können jetzt beliebig viele Argumente erhalten, welche über die grafische Oberfläche eingestellt werden können.
            </li>
            <li>
              Die Temperaturregelung wurde optimiert. Jedes Heizelement kann jetzt einen Regler zugewiesen bekommen, welcher z.B. über eine Phasenanschnittssteuerung das Heizelement regeln kann. Möglich ist z.B. auch die Regulierung eines Gasventils.
            </li>
            <li>
              Die Motordrehzahl kann jetzt über einen Regler eingestellt werden. Möglich ist auch die eine Druck- oder Temperaturadaptierte Regelung.
            </li>
            <li>
              Druck- und Temperaturverlauf können jetzt bereits während des Brauvorgangs angesehen werden.
            </li>
            <li>
              Der komplette Javascript-Teil wurde auf jQuery umgestellt. Daraus resultiert eine wesentlich höhere Geschwindigkeit und ein deutlich niedrigerer Speicherbedarf des Frontends.
            </li>
            <li>
              Das Backend wurde auf Grails 1.4 umgestellt.
            </li>
          </ul>
      </div>
    </body>
</html>
