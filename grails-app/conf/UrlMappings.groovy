class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(view:"/static/index")
        "/home"(view:"/static/index")
        "/about"(view:"/static/about")
        "/version"(view:"/static/version")
        "500"(view:'/error')
    }
}
