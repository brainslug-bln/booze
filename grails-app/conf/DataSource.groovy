dataSource {
    pooled = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
}
// environment specific settings
environments {
    /*development {
        dataSource {
            dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:h2:file:devDB"
        }
    }*/
    development {
        dataSource {
        	dbCreate = "update" // one of 'create', 'create-drop','update'
            url = "jdbc:mysql://localhost:3600/booze?useUnicode=true&characterEncoding=UTF-8"
            driverClassName = "com.mysql.jdbc.Driver"
            loggingSql = false
            pooled = false
            username = "root"
            password = "root"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:file:testDB"
        }
    }
    production {
        dataSource {
            dbCreate = "update"
            "jdbc:h2:file:prodDB"
        }
    }
}
