plugins {
    kotlin("js")
}

description = "Common external references to chrome platform, as well as common kotlin js functionality"

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
