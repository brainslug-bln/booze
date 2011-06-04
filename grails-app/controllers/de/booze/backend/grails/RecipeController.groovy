package de.booze.backend.grails

import grails.converters.JSON;
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class RecipeController {
  
  def recipeService

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
    session.recipe = new Recipe()
    [recipeInstance: new Recipe()]
  }
    
  
  /**
   * Due to the way grails handles commandObjects
   * we cannot bind and validate them manually.
   * So we need a controller action for every CO
   */
  def save = {
    session.recipe.properties = params
      
    if(recipeService.validateRecipe(session.recipe, params.tab)) {
      
      // If finalSave is set redirect to the edit dialog after saving
      if(params.finalSave && params.finalSave == "1") {
        session.recipe.validate()

        // Generate a pseudorandom (hopefully) unique global ID for community 
        // upload
        Random random = new Random(new Date().getTime())
        session.recipe.globalId = (random.next(40) + session.recipe.name + session.recipe.originalWort).encodeAsMD5()
        
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
      
      render([success:true, html: g.render(template:params.tab, bean:session.recipe)] as JSON)
      return
    }
      
    render([success:false, html: g.render(template:params.tab, bean:session.recipe)] as JSON)
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
