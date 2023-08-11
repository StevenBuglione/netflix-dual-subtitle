plugins {
    kotlin("js") version "1.8.0"
}

group = "com.buglione.chrome.extensions"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-js
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.7.3")
}

kotlin {
    js {
        binaries.executable()
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
        }
    }
}