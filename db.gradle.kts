
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.postgresql:postgresql:42.6.2")
    }
}

tasks.register("queryDb") {
    doLast {
        val url = "jdbc:postgresql://aws-0-us-west-2.pooler.supabase.com:6543/postgres?prepareThreshold=0"
        val user = "postgres.jroqekmiznjsygokvxzv"
        val password = "Sa3147861166."
        
        java.sql.DriverManager.getConnection(url, user, password).use { conn ->
            conn.createStatement().use { stmt ->
                println("=== CONSTRAINTS EN TRABAJADORES ===")
                val rs = stmt.executeQuery("SELECT conname FROM pg_constraint WHERE conrelid = (SELECT oid FROM pg_class WHERE relname = 'trabajadores')")
                while (rs.next()) {
                    println(rs.getString(1))
                }
                
                println("=== COLUMNAS EN TRABAJADORES ===")
                val rs2 = stmt.executeQuery("SELECT column_name, data_type FROM information_schema.columns WHERE table_name = 'trabajadores'")
                while (rs2.next()) {
                    println(rs2.getString(1) + " : " + rs2.getString(2))
                }
            }
        }
    }
}

