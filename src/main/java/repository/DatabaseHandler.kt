package main.java.repository

import java.io.File
import java.lang.System.getProperty
import java.sql.Connection
import java.sql.DriverManager


object DatabaseHandler {
    private val DATABASE_FILE_PATH = getProperty("user.home", "") + "/social_cinema/social_cinema.db"
    private val CONNECTION_STR = "jdbc:sqlite:$DATABASE_FILE_PATH"

    val connection: Connection

    init {
        createFileIfNotExists()
        connection = DriverManager.getConnection(CONNECTION_STR)
        initDatabaseStructureIfNotExists()
    }

    private fun createFileIfNotExists() {
        val file = File(DATABASE_FILE_PATH)
        if (!file.exists()) {
            file.parentFile.mkdir()
            file.createNewFile()
        }
    }

    private fun initDatabaseStructureIfNotExists() {
        connection.prepareStatement("""
            CREATE TABLE IF NOT EXISTS client_groups(
                group_id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                discount DECIMAL(10, 2) NOT NULL
            );
            
            CREATE TABLE IF NOT EXISTS clients(
                client_id INTEGER PRIMARY KEY,
                category_id INTEGER NOT NULL
                FOREIGN KEY (category_id)
                    REFERENCES client_groups(group_id) 
            );
            
            CREATE TABLE IF NOT EXISTS admins(
                admin_id INTEGER PRIMARY KEY
            );
            
            CREATE TABLE IF NOT EXISTS sessions(
                session_id INTEGER PRIMARY KEY
            );
            """.trimIndent())
            .execute()
    }
}
