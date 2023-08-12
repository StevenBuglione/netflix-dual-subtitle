plugins {
  kotlin("js") version "1.8.0"
}

description = "The background.js file for chrome extension."

repositories {
  mavenCentral()
}

dependencies {
  implementation(project(":chrome-platform"))
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

configurations {
    create("content")
}

artifacts {
    add("content", file("$buildDir/distributions/${name}.js")) {
        builtBy("browserProductionWebpack")
    }
}
