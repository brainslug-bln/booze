package de.booze.backend.grails

import grails.converters.JSON;

class RecipeController {

    /**
     * List all available recipes
     * @responseType HTML
     */
    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [recipeInstanceList: Recipe.list(params), recipeInstanceTotal: Recipe.count()]
    }

    def create = { RecipeGeneralCommand co ->
        def recipe = new Recipe()

        if(params.save && co.validate()) {
            recipe.properties = co
            try {
                recipe.save(flush: true)
                flash.message = g.message(code: "recipe.saved")
                forward(action: "edit", id: recipe.id, params:[category: "general"])
            }
            catch(Exception e) {
                flash.message = g.message(code:"recipe.save.failed")
                return [objectInstance: recipe]
            }
        }
        else {
            return [objectInstance: co]
        }
    }

    /**
     * Edit an existing recipe
     * @responseType mixed
     */
    def editFlow = {
        init {
            action {
                // Fetch recipe
                flow.recipe = Recipe.get(params.id)
                if(!flow.recipe) {
                    recipeNotFound()
                }
            }
            on("recipeNotFound").to "recipeNotFound"
            on(Exception).to "handleException"
        }

        general {
            action {
                def generalCO = RecipeGeneralCommand(params)
                if(generalCO.hasErrors()) {
                    render([success: true,
                            errors: [generalCO.errors.collect {
                                        [field: it.getObjecName(), 
                                         message: g.message(error: it)] 
                                    }],
                            dialog: g.message(code:"recipe.edit.pleaseCorrectErrors")] as JSON)
                }
                else {
                    recipe.properties = generalCO

                    try {
                        if(recipe.save()) {
                            render([success: true,
                                    dialog: g.message(code:"recipe.edit.saved")] as JSON)
                        }
                        else {
                           throw new Exception("Saving failed although validation was successful")
                        }
                    }
                    catch(Exception e) {
                        render([success: false,
                                dialog: g.message(code:"recipe.edit.save.failed", args:[e])])
                    }
                }
            }
            on(Exception).to "handleException"
        }

        fills {
            action {
                def fillsCO = RecipeFillsCommand(params)
                if(fillsCO.hasErrors()) {
                    render([errors: []] as JSON)
                }
                else {
                    recipe.properties = fillsCO
                    if(!recipe.save()) {
                        render([message: "test"] as JSON)
                    }
                }
            }
            on(Exception).to "handleException"
        }

        rests {
            action {
                def restsCO = RecipeRestsCommand(params)
                if(restsCO.hasErrors()) {
                    render([errors: []] as JSON)
                }
                else {
                    recipe.properties = restsCO
                    if(!recipe.save()) {
                        render([message: "test"] as JSON)
                    }
                }
            }
            on(Exception).to "handleException"
        }

        cooking {
            action {
                def cookingCO = RecipeCookingCommand(params)
                if(cookingCO.hasErrors()) {
                    render([errors: []] as JSON)
                }
                else {
                    recipe.properties = cookingCO
                    if(!recipe.save()) {
                        render([message: "test"] as JSON)
                    }
                }
            }
            on(Exception).to "handleException"
        }

        recipeNotFound {
            // Render template
        }

        handleException {
           action {
              log.error("Webflow Exception occurred: ${flash.stateException}", flash.stateException)
           }
           on("restart").to "init"
        }
    }


    /**
     * Create a new recipe
     *
     * This action is a grails web flow
     *
     * Steps:
     * 1. General recipe data
     * 2. Fills
     * 3. Rests
     * 4. Cooking / Hop
     *
     * @responseType HTML
     */
    def createFlow = {
        init {
            action {
                // Init recipe and command objects
                flow.recipe = new Recipe()
                flow.generalCO = new RecipeGeneralCommand()
                flow.fillsCO = new RecipeFillsCommand()
                flow.restsCO = new RecipeRestsCommand()
                flow.cookingCO = new RecipeCookingCommand()
            }.to "general"
            on(Exception).to "handleException"
        }

        general {
            on("proceed") {
                flow.generalCO.properties = params
                if(flow.generalCO.hasErrors()) {
                    error()
                }
            }.to "fills"
            on(Exception).to "handleException"
        }

        fills {
            on("back").to "general"
            on("proceed") {
                flow.fillsCO.properties = params
                if(flow.fillsCO.hasErrors()) {
                    error()
                }
            }.to "rests"
            on(Exception).to "handleException"
        }

        rests {
            on("back").to "fills"
            on("proceed") {
                flow.restsCO.properties = params
                if(flow.restsCO.hasErrors()) {
                    error()
                }
            }.to "cooking"
            on(Exception).to "handleException"
        }

        cooking {
            on("back").to "rests"
            on("proceed") {
                flow.cookingCO.properties = params
                if(flow.cookingCO.hasErrors()) {
                    error()
                }
            }.to "finish"
            on(Exception).to "handleException"
        }

        finish {
            flash.message = "Recipe created"
            redirect(controller: "recipe", action: "edit", id: flow.recipe.id)
        }

        handleException {
           action {
              log.error("Webflow Exception occurred: ${flash.stateException}", flash.stateException)
           }
           on("restart").to "init"
        }
    }

    /**
     * Delete a recipe
     * @responseType HTML
     */
    def delete = {

    }

    /**
     * Updates an existing recipe (
     * @responseType JSON
     */
    def update = {
        
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
