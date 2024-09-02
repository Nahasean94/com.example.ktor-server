package com.example.plugins
//
//import com.example.model.Task
//import com.example.model.FakeTaskRepository
//import io.ktor.serialization.kotlinx.*
//import io.ktor.server.application.*
//import io.ktor.server.routing.*
//import io.ktor.server.websocket.*
//import io.ktor.websocket.*
//import kotlinx.coroutines.delay
//import kotlinx.serialization.json.Json
//import java.time.Duration
//import java.util.*
//import kotlin.collections.ArrayList
//
//fun Application.configureSockets(repository: FakeTaskRepository) {
//    install(WebSockets) {
//        pingPeriod = Duration.ofSeconds(15)
//        timeout = Duration.ofSeconds(15)
//        maxFrameSize = Long.MAX_VALUE
//        masking = false
//        contentConverter = KotlinxWebsocketSerializationConverter(Json)
//
//    }
//    routing {
//        val sessions =
//            Collections.synchronizedList<WebSocketServerSession>(ArrayList())
//
//        webSocket("/tasks") {
//            for (task in repository.allTasks()) {
//                sendSerialized(task)
//                delay(1000)
//            }
//
//            close(CloseReason(CloseReason.Codes.NORMAL, "All done"))
//        }
//        webSocket("/tasks2") {
//            sessions.add(this)
//            for (task in repository.allTasks()) {
//                sendSerialized(task)
////                delay(1000)
//            }
//
//
//            while(true) {
//                val newTask = receiveDeserialized<Task>()
//                repository.addTask(newTask)
//                for(session in sessions) {
//                    session.sendSerialized(newTask)
//                }
//            }
//        }
//    }
//}
