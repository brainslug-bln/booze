<h2><g:message code="setting.edit.frontend" /></h2>

<form id="frontendForm" method="post" action="${createLink(controller:'setting', action:'save')}">
  
  <div class="form">  
    <div class="column50percent">
      <div class="row">
          <label for="setting.frontendFontSize"><g:message code="setting.frontendFontSize.label" /> <span id="frontendFontSizeIndicator" class="sliderIndicator"></span></label> 
            <div class="errors" id="errors_frontendFontSize">
               <g:renderErrors bean="${it}" field="frontendFontSize" as="list" />
            </div>
          <div class="sliderButton ui-icon-circle-arrow-w ui-icon" onclick="$('#frontendFontSizeSlider').slider('value', $('#frontendFontSizeSlider').slider('value') - $('#frontendFontSizeSlider').slider('option', 'step')); return false;"></div>
          <div class="slider">
            <div id="frontendFontSizeSlider"></div>
          </div>
          <div class="sliderButton ui-icon-circle-arrow-e ui-icon" onclick="$('#frontendFontSizeSlider').slider('value', $('#frontendFontSizeSlider').slider('value') + $('#frontendFontSizeSlider').slider('option', 'step')); return false;"></div>
          <input type="hidden" id="frontendFontSizeField" name="setting.frontendFontSize" value="${formatNumber(format:'####', number:it?.frontendFontSize)}" maxlength="10" />
      </div>
    </div>
    
    <div class="column50percent">
      
    </div>
    
    <div class="buttonbar">
        <input class="ui-button ui-state-default" type="button" id="saveFrontendButton" onclick="booze.setting.update(this.form)" value="${message(code:'setting.edit.save')}" />
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="frontend" />
  </div>
  
</form>

<script type="text/javascript">


$(function() {
    $( "#frontendFontSizeSlider" ).slider({
        value:${it.frontendFontSize?:1},
        min: 0,
        max: 5,
        step: 1,
        change: function( event, ui ) {
            $( "#frontendFontSizeField" ).val( String.replace(ui.value, ".", ",") );
            $( "#frontendFontSizeIndicator").html(String.replace(ui.value, ".", ","));
        },
        slide: function( event, ui ) {
            $( "#frontendFontSizeIndicator").html(String.replace(ui.value, ".", ","));
        }
    });
    $( "#frontendFontSizeField" ).val( String.replace($( "#frontendFontSizeSlider" ).slider( "value" ), ".", ",") );
    $( "#frontendFontSizeIndicator").html(String.replace($( "#frontendFontSizeSlider" ).slider( "value" ), ".", ","));
});

</script>