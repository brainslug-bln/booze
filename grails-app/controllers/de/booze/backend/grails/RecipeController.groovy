package de.booze.backend.grails

import grails.converters.JSON;
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class RecipeController {

  /**
   * List all available recipes
   * @responseType HTML
   */
  def list = {
    params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
    [recipeInstanceList: Recipe.list(params), recipeInstanceTotal: Recipe.count()]
  }

  /**
   * Display the setting create dialog
   * @responseType HTML
   */
  def create = {
    [recipeInstance: new Recipe()]
  }
    
  def save = {
    session.recipe = new Recipe()
      
    def commandObject
    
    try {
      def validCOs = ['RecipeMainDataCommand', 'RecipeMashingCommand', 'RecipeCookingCommand', 'RecipeFermentationCommand']
      if(!params.validate || !validCOs.contains(params.validate)) {
        throw new Exception("Requested command object not found")
      }
      def myClassLoader = AH.application.mainContext.getClassLoader()
      Class myClass = Class.forName('de.booze.backend.grails.'+params.validate, false, myClassLoader)
      commandObject = myClass.newInstance()
      
      bindData(commandObject, params.recipe)
    }
    catch(Exception e) {
      render([success:false, error:g.message(code:"recipe.save.commandObjectNotFound", args:[params.validate.encodeAsHTML(), e])] as JSON)
      return
    }
      
    if(!commandObject.hasErrors()) {
      session.recipe.properties = commandObject;
      
      // If finalSave is set redirect to the edit dialog after saving
      if(params.finalSave) {
        if(session.recipe.save(flush: true)) {
          render([success:true, 
              message: g.message(code:"recipe.save.saved"), 
              redirect: g.createLink(controller:'recipe', action:'edit', id:session.recipe.id)] as JSON)
          return
        }
        else {
          render([success:false, error:g.message(code:"recipe.save.failed"), html: g.render(template:params.tab, bean:session.recipe)] as JSON)
        }
      }
      
      render([success:true, html: g.render(template:params.tab, bean:commandObject)] as JSON)
      return
    }
      
    render([success:false, html: g.render(template:params.tab, bean:commandObject)] as JSON)
  }

    
  /**
   * Updates an existing recipe (
   * @responseType JSON
   */
  def update = {

    // Create an instance of the command object we are going
    // to validate
    try {
      def validCOs = ['RecipeMainDataCommand', 'RecipeMashingCommand', 'RecipeCookingCommand', 'RecipeFermentationCommand']
      if(!params.validate || !validCOs.contains(params.validate)) {
        throw new Exception("Requested command object not found")
      }
      Class myClass = Class.forName(params.validate)
      def co = myClass.newInstance()
      co.properties = params
    }
    catch(Exception e) {
      render([success:false, error:g.message(code:"recipe.save.commandObjectNotFound", args:[params.validate.encodeAsHTML(), e])] as JSON)
      return
    }

    if(co.validate()) {
      session.recipe.properties = co;

      if(params.finalSave) {
        // Finally save the whole recipe
        try {
          session.recipe.save()
          render([success:true, redirect: createLink(controller:"recipe", action:"edit", id:session.recipe.id)] as JSON)
        }
        catch(Exception e) {
          render([success:false, error:g.message(code:"recipe.save.finalSaveFailed", args:[e])] as JSON)
        }
      }
      else {
        // Render the  next dialog, persist recipe changes to session
        try {
          render([success:true, html: g.render(template:params.next, bean:session.recipe)] as JSON)
        }
        catch(Exception e) {
          render([success:false, error:g.message(code:"recipe.save.nextDialogFailed", args:[params.next, e])] as JSON)
        }
      }
    }
    else {
      render([success:false, html: g.render(template:params.validate, bean:co)] as JSON)
    }
  }

  /**
   * Edit an existing recipe
   * @responseType mixed
   */
  def edit = {
    if(!params.id || !Recipe.exists(params.id)) {
      flash.message = g.message(code:"recipe.edit.notFound")
      redirect(action: "list")
    }

    Recipe recipe = Recipe.get(params.id)
    [recipeInstance: recipe]
  }

  /**
   * Delete a recipe
   * @responseType HTML
   */
  def delete = {

  }


  /**
   * Adds an image to a recipe
   * @responseType JSON
   */
  def addImage = {

  }

  /**
   * Deletes an image
   * @responseType JSON
   */
  def deleteImage = {

  }

  /**
   * Uploads a recipe to the community page
   * @responseType JSON
   */
  def communityUpload = {

  }
}


class RecipeMainDataCommand implements Serializable {
  String name
  String description

  static constraints = {
    name(size: 3..255, nullable: false, blank: false)
    description(size: 0..5000, nullable: true, blank: true)
  }
}

class RecipeMashingCommand implements Serializable {
  Double preSpargingWort
  Double postSpargingWort
  Double mashingWaterVolume
  Double spargingWaterVolume
  Double lauterTemperature
  Double mashingTemperature
  Double spargingTemperature
  
  boolean doColdMashing
  
  SortedSet rests

  static hasMany = [rests: RecipeRest, malts: RecipeMalt]
  
  static constraints = {
    preSpargingWort(min: 0.0 as Double, max: 50.0 as Double, nullable: true)
    postSpargingWort(min: 0.0 as Double, max: 50.0 as Double, nullable: true)
    originalWort(min: 0.0 as Double, max: 50.0 as Double, nullable: false)
    mashingTemperature(min: 0.0 as Double, max: 100 as Double, nullable: true, validator: {val, obj ->
        if (val == null && obj.doColdMashing == false) {
          ['recipe.mashingTemperature.nullable']
        }
      })
    mashingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: false)
    spargingWaterVolume(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    spargingTemperature(min: 0.0 as Double, max: 100 as Double, nullable: true,  validator: {val, obj ->
        if (val == null && obj.spargingWaterVolume != null ) {
          ['recipe.spargingTemperature.nullable']
        }
      })
    lauterTemperature(min: 0.0 as Double, max: 100 as Double, nullable: false)
  }
}

class RecipeCookingCommand implements Serializable {
  Double originalWort
  Double cookingTime
  Double postIsomerization
  
  SortedSet rests

  static hasMany = [hops: RecipeHop]
  
  static constraints = {
    originalWort(min: 0.0 as Double, max: 50.0 as Double, nullable: false)
    postIsomerization(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    cookingTime(min: 0.0 as Double, max: 1000 as Double, nullable: false)
  }  
}

class RecipeFermentationCommand implements Serializable {
  Double fermentationTemperature
  Double storingTime
  Double storingTemperature
  String yeast
  Double bottlingWort
  Double alcohol
    
  static constraints = {
    alcohol(min: 0.0 as Double, max: 40.0 as Double, nullable: true)
    bottlingWort(min: 0.0 as Double, max: 20.0 as Double, nullable: true)
    fermentationTemperature(min: 0.0 as Double, max: 50 as Double, nullable: false)
    storingTime(min: 0.0 as Double, max: 1000 as Double, nullable: true)
    storingTemperature(min: 0.0 as Double, max: 50 as Double, nullable: true)
    yeast(nullable: false, size: 5..2000)
  }
}