package com.example.plugins


import com.example.model.Priority
import com.example.model.Task
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RoutingKtTest {


    @Test
    fun testGetTasksBypriorityPriority() = testApplication {

        client.get("/tasks/byPriority/Medium").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertContains(body(), "Mow the lawn")
            assertContains(body(), "Paint the fence")
        }
    }

    @Test
    fun invalidPriorityProduces400() = testApplication {

        client.get("/tasks/byPriority/Invalid").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertContains(body(), "Mow the lawn")
            assertContains(body(), "Paint the fence")
        }

    }

    @Test
    fun unusedPriorityProduces404() = testApplication {

        client.get("/tasks/byPriority/Vital").apply {
            assertEquals(HttpStatusCode.NotFound, status)

        }

    }

    @Test
    fun testPostTasks() = testApplication {
//        val client = createClient {
//            install(ContentNegotiation) {
//                json()
//            }
//        }
       val response= client.post("/tasks") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )
           val task = Task(name = "swimming", description = "Go to the beach", priority = Priority.Low)

//val jsonBody= Json.encodeToString(task)
           setBody(
               Json.encodeToString(task)
            )
        }
        assertEquals(HttpStatusCode.OK, response.status)

        val response2 = client.get("/tasks")
        println(response2 )
        assertEquals(HttpStatusCode.OK, response2.status)
        val body = response2.bodyAsText()

        assertContains(body, "swimming")
        assertContains(body, "Go to the beach")
    }
}
