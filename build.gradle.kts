import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  application
  kotlin("jvm") version "1.7.10"
}

application {
  mainClass.set("com.github.borrys.MainKt")
}

group = "com.github.borrys"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val retrofitVersion = "2.9.0"
dependencies {
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
  implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
  implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")

  testImplementation(kotlin("test"))
  testImplementation("org.junit.jupiter:junit-jupiter:5.9.0")
  testImplementation("io.mockk:mockk:1.12.8")
}

tasks.test {
  useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

tasks {
  val fatJar = register<Jar>("fatJar") {
    dependsOn.addAll(
      listOf(
        "compileJava",
        "compileKotlin",
        "processResources"
      )
    )
    archiveClassifier.set("standalone")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest { attributes(mapOf("Main-Class" to application.mainClass)) }
    val sourcesMain = sourceSets.main.get()
    val contents = configurations.runtimeClasspath.get()
      .map { if (it.isDirectory) it else zipTree(it) } +
        sourcesMain.output
    from(contents)
  }
  build {
    dependsOn(fatJar) // Trigger fat jar creation during build
  }
}
