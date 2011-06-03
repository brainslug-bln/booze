<div id="navigation" class="ui-corner-all">
  <ul>
    <li>
      <strong>Rezepte</strong>
      <ul>
        <li><g:link controller="recipe" action="list">Liste/Suchen</g:link></li>
        <li><g:link controller="recipe" action="create">Rezept erstellen</g:link></li>
        <core:communityEnabled>
          <li><g:link controller="recipe" action="database">Rezeptdatenbank (online)</g:link></li>
        </core:communityEnabled>
      </ul>
    </li>

    <li>
      <strong>Lager</strong>
      <ul>
        <li><g:link controller="stock" action="beers">Biere</g:link></li>
        <li><g:link controller="stock" action="ingredients">Rohstoffe</g:link></li>
        <li><g:link controller="stock" action="shoppingList">Einkaufsliste</g:link></li>
      </ul>
    </li>

    <li>
      <strong>Werkzeuge</strong>
      <ul>
        <li><g:link controller="tools" action="calculator">Braurechner</g:link></li>
        <li><g:link controller="tools" action="manualMode">Manuelle Steuerung</g:link></li>
      </ul>
    </li>

    <li>
      <strong>Einstellungen</strong>
      <ul>
        <li><g:link controller="setting" action="list">Umgebungen</g:link></li>
        <li><g:link controller="setting" action="editActive">Aktive Umgebung</g:link></li>
        <li><g:link controller="tools" action="update">Updates</g:link></li>
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
