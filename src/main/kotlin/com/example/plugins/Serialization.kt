package com.example.plugins

import com.example.model.Priority
import com.example.model.Task

import io.ktor.server.application.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.SchemaUtils.drop

import io.ktor.server.response.*
import io.ktor.server.routing.*

import com.example.model.TaskRepository
import com.example.model.Tasks
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.gson.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*

import io.ktor.server.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.selectBatched
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.random.nextInt


fun Application.configureSerialization( ) {
    val random = Random(System.currentTimeMillis().toInt())

    install(StatusPages) {
        exception<IllegalStateException> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {
        route("/tasks") {
            val id = (1..800000).random()
            get {
                val tasks = withContext(Dispatchers.IO) {
                    transaction {

                        Tasks.select { Tasks.id greaterEq id }
                            .orderBy(Tasks.id, SortOrder.ASC)
                            .limit(10).map {
                            Task(
                                id =  it[Tasks.id].value ,
                                name = it[Tasks.name],
                                description =  it[Tasks.description] ,
                                priority =  it[Tasks.priority]
                            )
                        }}
                    }

                call.respond(tasks)
            }


            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@get
                }
                val task = transaction {
                    // Select only the necessary columns
                    Tasks.select { Tasks.id eq id }
                        .mapNotNull {
                            Task(
                                id = it[Tasks.id].value,
                                name = it[Tasks.name],
                                description = it[Tasks.description],
                                priority = it[Tasks.priority]
                            )
                        }
                        .singleOrNull() // Return a single result or null
                }

                if (task == null) {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                } else {
                    call.respond(task)
                }
            }
            post {
                val tasks = call.receive<List<Task>>()
                withContext(Dispatchers.IO) {
                    transaction {
                        Tasks.batchInsert(tasks) { task ->
                            this[Tasks.name] = task.name
                            this[Tasks.description] = task.description
                            this[Tasks.priority] = task.priority
                        }
                    }
                }

                call.respond(HttpStatusCode.Created)
            }
            put("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
                    return@put
                }

                val updatedTask = call.receive<Task>()
                val updatedRows = withContext(Dispatchers.IO) {
                    transaction {
                        Tasks.update({ Tasks.id eq id }) {
                            it[name] = updatedTask.name
                            it[description] = updatedTask.description
                            it[priority] = updatedTask.priority
                        }
                    }
                }

                if (updatedRows > 0) {
                    call.respond(HttpStatusCode.OK, "Task updated")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Task not found")
                }
            }

//            delete("/{id}") {
//                val id = call.parameters["id"]?.toIntOrNull()
//                if (id == null) {
//                    call.respond(HttpStatusCode.BadRequest, "Invalid ID")
//                    return@delete
//                }
//
//                val deletedRows = withContext(Dispatchers.IO) {
//                    transaction {
//                        Tasks.deleteWhere { Tasks.id eq id }
//                    }
//                }
//
//                if (deletedRows > 0) {
//                    call.respond(HttpStatusCode.OK, "Task deleted")
//                } else {
//                    call.respond(HttpStatusCode.NotFound, "Task not found")
//                }
//        }
//            get {
//                val tasks = repository.allTasks()
//
//                val task = withContext(Dispatchers.IO) {
//                    transaction {
//                        repository.allTasks()
//                    }
//                }
//
//                call.respond(tasks)
//            }
//
//            get("/byName/{taskName}") {
//                val name = call.parameters["taskName"]
//                if (name == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@get
//                }
//                val task = repository.taskByName(name)
//                if (task == null) {
//                    call.respond(HttpStatusCode.NotFound)
//                    return@get
//                }
//                call.respond(task)
//            }
//
//            get("/byPriority/{priority}") {
//                val priorityAsText = call.parameters["priority"]
//                if (priorityAsText == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@get
//                }
//                try {
//                    val priority = Priority.valueOf(priorityAsText)
//                    val tasks = repository.tasksByPriority(priority)
//
//
//                    if (tasks.isEmpty()) {
//                        call.respond(HttpStatusCode.NotFound)
//                        return@get
//                    }
//                    call.respond(tasks)
//                } catch (ex: IllegalArgumentException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//            post {
//                try {
//                    val task = call.receive<Task>()
//                    repository.addTask(task)
//                    call.respond(HttpStatusCode.NoContent)
//                } catch (ex: IllegalStateException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                } catch (ex: JsonConvertException) {
//                    call.respond(HttpStatusCode.BadRequest)
//                }
//            }
//
//            delete("/{taskName}") {
//                val name = call.parameters["taskName"]
//                if (name == null) {
//                    call.respond(HttpStatusCode.BadRequest)
//                    return@delete
//                }
//                if (repository.removeTask(name)) {
//                    call.respond(HttpStatusCode.NoContent)
//                } else {
//                    call.respond(HttpStatusCode.NotFound)
//                }
//            }
        }

    }
}
