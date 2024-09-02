package com.example.model



import kotlinx.serialization.Serializable
import org.jetbrains.exposed.dao.id.IntIdTable

object Tasks : IntIdTable() {
    val name = varchar("name", 255)
    val description = varchar("description", 1024)
    val priority = varchar("priority", 50)
}
@Serializable
data class Task(val id: Int? = null, val name: String, val description: String, val priority: String)
