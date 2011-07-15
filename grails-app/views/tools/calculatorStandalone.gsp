<html>
    <head>
        <title><g:message code="calculator.headline" /></title>
        <meta name="layout" content="main" />
        
        <g:javascript src="booze.leftnav.js" />
    </head>
    <body>
      <h1><g:message code="calculator.headline" /></h1>

      <div class="leftColumn">
        <div id="leftColumn_content" class="leftColumn_content">
          <div class="leftNav" id="calculatorNav">
            <ul>
              <li class="active"><a href="#" rel="#calculateFare"><g:message code="calculator.selector.calculateFare" /></a></li>
              <li><a href="#" rel="#correctWort"><g:message code="calculator.selector.correctWort" /></a></li>
              <li><a href="#" rel="#calculateAlcohol"><g:message code="calculator.selector.calculateAlcohol" /></a></li>
              <li><a href="#" rel="#calculatePressure"><g:message code="calculator.selector.calculatePressure" /></a></li>
              <li><a href="#" rel="#calculateFermentationRatio"><g:message code="calculator.selector.calculateFermentationRatio" /></a></li>
              <li><a href="#" rel="#calculateVolume"><g:message code="calculator.selector.calculateVolume" /></a></li>
              <li><a href="#" rel="#calculateMix"><g:message code="calculator.selector.calculateMix" /></a></li>
              <li><a href="#" rel="#calculateYield"><g:message code="calculator.selector.calculateYield" /></a></li>
              <li><a href="#" rel="#calculateWaterHardness"><g:message code="calculator.selector.calculateWaterHardness" /></a></li>
              <li><a href="#" rel="#calculateAlternativeHop"><g:message code="calculator.selector.calculateAlternativeHop" /></a></li>
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
      
      <script type="text/javascript">
        $(document).ready(function() {
          booze.calculatorNav = new BoozeLeftNav("calculatorNav");
        });
      </script>
    </body>
</html>
