plugins {
    kotlin("js")
}

description = "Code for the popup when pressing the chrome extensions icon."

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
    add("content", file("$buildDir/distributions/popup.html"))
    add("content", file("$buildDir/distributions/button.css"))
}
