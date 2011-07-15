<div id="navigation" class="ui-corner-all">
  <ul>
    <li>
      <strong>Rezepte</strong>
      <ul>
        <li onclick="window.location.href='${createLink(controller:"recipe", action:"list")}'">Liste</li>
        <li onclick="window.location.href='${createLink(controller:"recipe", action:"create")}'">Rezept erstellen</li>
        <!--<core:communityEnabled>
          <li><g:link controller="recipe" action="database">Rezeptdatenbank (online)</g:link></li>
        </core:communityEnabled>-->
      </ul>
    </li>

    <li>
      <strong>Lager</strong>
      <ul>
        <li onclick="window.location.href='${createLink(controller:"protocol", action:"list")}'">Biere</li>
        <!--<li><g:link controller="stock" action="shoppingList">Einkaufsliste</g:link></li>-->
      </ul>
    </li>

    <li>
      <strong>Werkzeuge</strong>
      <ul>
        <li onclick="window.location.href='${createLink(controller:"tools", action:"calculator")}'">Braurechner</li>
        <!--<li><g:link controller="tools" action="manualMode">Manuelle Steuerung</g:link></li>-->
      </ul>
    </li>

    <li>
      <strong>Einstellungen</strong>
      <ul>
        <li onclick="window.location.href='${createLink(controller:"setting", action:"list")}'">Umgebungen</li>
        <li onclick="window.location.href='${createLink(controller:"setting", action:"create")}'">Umgebung erstellen</li>
        <li onclick="window.location.href='${createLink(controller:"setting", action:"editActive")}'">Aktive Umgebung</li>
        <!--<li><g:link controller="tools" action="update">Updates</g:link></li>-->
      </ul>
    </li>
<!--
    <li>
      <strong>Community</strong>
      <ul>
        <li><g:link controller="community" action="account">Mein Account</g:link></li>
        <core:communityEnabled>
          <li><g:link controller="community" action="links">Linksammlung</g:link></li>
          <li><g:link controller="recipe" action="database">Rezeptdatenbank</g:link></li>
          <li><g:link controller="community" action="news">Neuigkeiten</g:link></li>
        </core:communityEnabled>
      </ul>
    </li>
-->
    <li>
      <strong>Über</strong>
      <ul>
        <li><g:link uri="/version">Diese Version</g:link></li>
        <li><g:link uri="/about">Booze</g:link></li>
      </ul>
    </li>
  </ul>
</div>
