<h2><g:message code="setting.edit.mainData" /></h2>

<form id="mainForm" method="post" action="${createLink(controller:'setting', action:'save')}">

  <div class="form">  
    <div class="column50percent">
      <div class="row">
          <label for="setting.name"><g:message code="setting.name.label" /></label>
            <div class="errors" id="errors_name">
               <g:renderErrors bean="${it}" field="name" as="list" />
            </div>
          <input type="text" name="setting.name" value="${fieldValue(bean: it, field:'name')}" maxlength="254" onkeyup="$('#errors_name').slideUp(100)" />
      </div>

      <div class="row">
          <label for="setting.dateCreated"><g:message code="setting.dateCreated.label" /></label>
          <span class="immutable"><g:formatDate format="dd.MM.yyyy" date="${it?.dateCreated?it.dateCreated:(new Date())}" /></span>
      </div>

      <div class="row">
        <label for="setting.description"><g:message code="setting.description.label" /></label>
            <div class="errors" id="errors_description">
               <g:renderErrors bean="${it}" field="description" as="list" />
            </div>
        <textarea maxlength="5000" rows="3" name="setting.description" onkeyup="$('#errors_description').slideUp(100)"><g:fieldValue bean="${it}" field="description" /></textarea>
      </div>
    </div>

    <div class="column50percent">
      <div class="row">
        <label for="setting.active"><g:message code="setting.active.label" /></label>
            <div class="errors" id="errors_active">
               <g:renderErrors bean="${it}" field="active" as="list" />
            </div>
        <div class="radioset" id="active">
          <input type="radio" id="activeTrue" name="setting.active" value="${true}" <g:if test="${it?.active}">checked</g:if> /> <label for="activeTrue"><g:message code="setting.active.true" /></label>
          <input type="radio" id="activeFalse" name="setting.active" value="${false}" <g:if test="${!it?.active}">checked</g:if> /> <label for="activeFalse"><g:message code="setting.active.false" /></label>
        </div>
      </div>

      <div class="row">
        <label for="setting.cookingTemperature"><g:message code="setting.cookingTemperature.label" /> <span id="cookingTemperatureIndicator" class="sliderIndicator"></span></label> 
            <div class="errors" id="errors_cookingTemperature">
               <g:renderErrors bean="${it}" field="cookingTemperature" as="list" />
            </div>
          <div class="booze-icon sliderButton booze-icon-arrow-left" onclick="$('#cookingTemperatureSlider').slider('value', $('#cookingTemperatureSlider').slider('value') - $('#cookingTemperatureSlider').slider('option', 'step')); return false;"></div>
          <div class="slider">
            <div id="cookingTemperatureSlider"></div>
          </div>
          <div class="booze-icon sliderButton booze-icon-arrow-right" onclick="$('#cookingTemperatureSlider').slider('value', $('#cookingTemperatureSlider').slider('value') + $('#cookingTemperatureSlider').slider('option', 'step')); return false;"></div>
          <input type="hidden" id="cookingTemperatureField" name="setting.cookingTemperature" value="${formatNumber(format:'####.##', number:it?.cookingTemperature)}" maxlength="10" />
      </div>

      <div class="row">
          <label for="setting.hysteresis"><g:message code="setting.hysteresis.label" /> <span id="hysteresisIndicator" class="sliderIndicator"></span></label>
            <div class="errors" id="errors_hysteresis">
               <g:renderErrors bean="${it}" field="hysteresis" as="list" />
            </div>
          <div class="booze-icon sliderButton booze-icon-arrow-left" onclick="$('#hysteresisSlider').slider('value', $('#hysteresisSlider').slider('value') - $('#hysteresisSlider').slider('option', 'step')); return false;"></div>
          <div class="slider">
            <div id="hysteresisSlider"></div>
          </div>
          <div class="booze-icon sliderButton booze-icon-arrow-right" onclick="$('#hysteresisSlider').slider('value', $('#hysteresisSlider').slider('value') + $('#hysteresisSlider').slider('option', 'step')); return false;"></div>
          <input type="hidden" id="hysteresisField" name="setting.hysteresis" value="${formatNumber(format:'##.##', number:it?.hysteresis)}" maxlength="10" />
      </div>

      <div class="row">
          <label for="setting.heatingRamp"><g:message code="setting.heatingRamp.label" /> <span id="heatingRampIndicator" class="sliderIndicator"></span></label>
            <div class="errors" id="errors_heatingRamp">
               <g:renderErrors bean="${it}" field="heatingRamp" as="list" />
            </div>
          <div class="booze-icon sliderButton booze-icon-arrow-left" onclick="$('#heatingRampSlider').slider('value', $('#heatingRampSlider').slider('value') - $('#heatingRampSlider').slider('option', 'step')); return false;"></div>
          <div class="slider">
            <div id="heatingRampSlider"></div>
          </div>
          <div class="booze-icon sliderButton booze-icon-arrow-right" onclick="$('#heatingRampSlider').slider('value', $('#heatingRampSlider').slider('value') + $('#heatingRampSlider').slider('option', 'step')); return false;"></div>
          <input type="hidden" id="heatingRampField" name="setting.heatingRamp" value="${formatNumber(format:'##.##', number:it?.heatingRamp)}" maxlength="10" />
      </div>
    </div>
    
    <div class="buttonbar">
      <g:if test="${it?.id}">
        <input class="ui-button ui-state-default" type="button" id="saveMainDataButton" onclick="booze.setting.update(this.form)" value="${message(code:'setting.edit.save')}" />
      </g:if>
      <g:else>
        <input class="ui-button ui-state-default" type="submit" id="createSettingButton" value="${message(code:'setting.create.save')}" />
      </g:else>
    </div>
    
    <input type="hidden" name="setting.id" value="${it?.id}" />
    <input type="hidden" name="tab" value="main" />
  </div>
  
</form>

<script type="text/javascript">
$(function() {
    $( "#cookingTemperatureSlider" ).slider({
        value:${it.cookingTemperature?:102},
        min: 98,
        max: 106,
        step: 0.5,
        change: function( event, ui ) {
            $( "#cookingTemperatureField" ).val( String.replace(ui.value, ".", ",") );
            $( "#cookingTemperatureIndicator").html(String.replace(ui.value, ".", ","));
        },
        slide: function( event, ui ) {
            $( "#cookingTemperatureIndicator").html(String.replace(ui.value, ".", ","));
        }
    });
    $( "#cookingTemperatureField" ).val( String.replace($( "#cookingTemperatureSlider" ).slider( "value" ), ".", ",") );
    $( "#cookingTemperatureIndicator").html(String.replace($( "#cookingTemperatureSlider" ).slider( "value" ), ".", ","));
});

$(function() {
    $( "#hysteresisSlider" ).slider({
        value:${it.hysteresis?:5},
        min: 0.5,
        max: 10,
        step: 0.5,
        change: function( event, ui ) {
            $( "#hysteresisField" ).val( String.replace(ui.value, ".", ",") );
            $( "#hysteresisIndicator").html(String.replace(ui.value, ".", ","));
        },
        slide: function( event, ui ) {
            $( "#hysteresisIndicator").html(String.replace(ui.value, ".", ","));
        }
    });
    $( "#hysteresisField" ).val( String.replace($( "#hysteresisSlider" ).slider( "value" ), ".", ",") );
    $( "#hysteresisIndicator").html(String.replace($( "#hysteresisSlider" ).slider( "value" ), ".", ","));
});

$(function() {
    $( "#heatingRampSlider" ).slider({
        value:${it.heatingRamp?:1},
        min: 0.1,
        max: 5.0,
        step: 0.1,
        change: function( event, ui ) {
            $( "#heatingRampField" ).val( String.replace(ui.value, ".", ",") );
            $( "#heatingRampIndicator").html(String.replace(ui.value, ".", ","));
        },
        slide: function( event, ui ) {
            $( "#heatingRampIndicator").html(String.replace(ui.value, ".", ","));
        }
    });
    $( "#heatingRampField" ).val( String.replace($( "#heatingRampSlider" ).slider( "value" ), ".", ",") );
    $( "#heatingRampIndicator").html(String.replace($( "#heatingRampSlider" ).slider( "value" ), ".", ","));
});

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

$(function() {
        $( "#active" ).buttonset();
});


$(document).ready(function() {
    <g:if test="${it?.id}">
$("#mainForm").submit({form: "#mainForm"}, booze.setting.formSubmit);
</g:if>
});


</script>