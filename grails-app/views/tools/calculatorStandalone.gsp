<html>
    <head>
        <title><g:message code="calculator.headline" /></title>
        <meta name="layout" content="main" />
    </head>
    <body>
      <h1><g:message code="calculator.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div id="leftnav">
            <ul>
              <li class="active"><a href="#" rel="#calculateFare"><g:message code="calculator.selector.calculateFare" /></a></li>
              <li><a href="#" rel="display:#correctWort"><g:message code="calculator.selector.correctWort" /></a></li>
              <li><a href="#" rel="display:#calculateAlcohol"><g:message code="calculator.selector.calculateAlcohol" /></a></li>
              <li><a href="#" rel="display:#calculatePressure"><g:message code="calculator.selector.calculatePressure" /></a></li>
              <li><a href="#" rel="display:#calculateFermentationRatio"><g:message code="calculator.selector.calculateFermentationRatio" /></a></li>
              <li><a href="#" rel="display:#calculateVolume"><g:message code="calculator.selector.calculateVolume" /></a></li>
              <li><a href="#" rel="display:#calculateMix"><g:message code="calculator.selector.calculateMix" /></a></li>
              <li><a href="#" rel="display:#calculateYield"><g:message code="calculator.selector.calculateYield" /></a></li>
              <li><a href="#" rel="display:#calculateWaterHardness"><g:message code="calculator.selector.calculateWaterHardness" /></a></li>
              <li><a href="#" rel="display:#calculateAlternativeHop"><g:message code="calculator.selector.calculateAlternativeHop" /></a></li>
            </ul>
          </div>
          <div class="clearfix"></div>
        </div>
      </div>

      <div class="rightColumn">
        <div class="rightColumn_content">
          <g:render template="/tools/calculator" />
        </div>
      </div>
    </body>
</html>
