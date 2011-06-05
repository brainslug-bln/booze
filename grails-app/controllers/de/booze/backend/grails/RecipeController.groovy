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
   */
  def update = {
    Recipe recipe = Recipe.get(params.id)
    if(!recipe) {
      render([success:false, error:g.message(code:"recipe.update.notFound")] as JSON)
      return
    }
    
    if(params.tab == "cooking") {
      recipe.hops.clear()
    }
    
    if(params.tab == 'mashing') {
      recipe.malts.clear()
      recipe.rests.clear()
    }
    
    recipe.properties = params
      
    if(recipeService.validateRecipe(recipe, params.tab)) {
      if(recipe.save(flush: true)) {
        
        render([success:true, 
                html: g.render(template:params.tab, bean:recipe),
                message: g.message(code:"recipe.update.saved")] as JSON)
        return
      }
      else {
        render([success:false, error:g.message(code:"recipe.update.failed"), html: g.render(template:params.tab, bean:recipe)] as JSON)
      }
    }
      
    render([success:false, html: g.render(template:params.tab, bean:recipe)] as JSON)
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
