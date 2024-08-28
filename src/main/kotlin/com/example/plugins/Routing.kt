//package com.example.plugins
//
//import com.example.model.Priority
//import com.example.model.Task
//import com.example.model.FakeTaskRepository
//
//import io.github.smiley4.ktorswaggerui.SwaggerUI
//import io.ktor.http.*
//import io.ktor.resources.*
//import io.ktor.serialization.gson.*
//import io.ktor.server.application.*
//import io.ktor.server.application.Application
//import io.ktor.server.application.install
//import io.ktor.server.plugins.contentnegotiation.*
//import io.ktor.server.plugins.statuspages.*
//import io.ktor.server.request.*
//import io.ktor.server.resources.*
//import io.ktor.server.resources.Resources
//import io.ktor.server.response.*
//import io.ktor.server.routing.*
//
//import kotlinx.serialization.Serializable
//
//fun Application.configureRouting( repository: FakeTaskRepository) {
//    install(StatusPages) {
//        exception<IllegalStateException> { call, cause ->
//            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
//        }
//    }
//    install(Resources)
//    install(ContentNegotiation) {
//        gson {
//            setPrettyPrinting()
//        }
//    }
//    install(SwaggerUI) {
//        swagger {
//            swaggerUrl = "swagger-ui"
//            forwardRoot = true
//        }
//        info {
//            title = "Example API"
//            version = "latest"
//            description = "Example API for testing and demonstration purposes."
//        }
//        server {
//            url = "http://localhost:8080"
//            description = "Development Server"
//        }
//    }
//    routing {
//        get("/) {
////           " call.respondText("Hello World!")
//        }
//        get<Articles> { article ->
//            // Get all articles ...
//            call.respond("List of articles sorted starting from ${article.sort}")
//        }
//        route("/tasks") {
//            get {
//                val tasks = repository.allTasks()
//
//                call.respond(tasks)
//            }
//
//            get("/byPriority/{priority}") {
//                val priorityAsText = call.parameters["priority"]
//                if (priorityAsText == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@get
//                }
//
//                try {
//                    val priority = Priority.valueOf(priorityAsText)
//                    val tasks = repository.tasksByPriority(priority)
//
//                    if (tasks.isEmpty()) {
//                        call.respond(HttpStatusCode.NotFound)
//                        return@get
//                    }
//
//                    call.respond(
//                        tasks
//                    )
//                } catch (ex: IllegalArgumentException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//            post {
//                val task = call.receive<Task>()
//                if (task.name.isEmpty() || task.description.isEmpty()) {
//                    call.respond(HttpStatusCode.BadRequest, "Invalid task data")
//                    return@post
//                }
//
//                try {
//                    repository.addTask(task)
//                    call.respond(repository.allTasks())
//                } catch (ex: IllegalArgumentException) {
//                    call.respond(HttpStatusCode.BadRequest, "Invalid priority value")
//                } catch (ex: IllegalStateException) {
//                    call.respond(HttpStatusCode.BadRequest, ex.message!!)
//                }
//            }
//        }
//    }
//}
//
//@Serializable
//@Resource("/articles")
//class Articles(val sort: String? = "new")
