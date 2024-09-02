package com.example


//import com.example.model.PostgresTaskRepository
import com.example.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
//    val repository =  PostgresTaskRepository()

    configureSerialization()
//    configureSockets(repository)
    configureDatabases()
//    configureSerialization()
//    configureMonitoring()
//    configureHTTP()
//    configureSecurity()
//    configureRouting(repository)
//    configureDatabases()
}
