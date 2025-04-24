plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    id("org.jetbrains.kotlin.kapt")
    id("maven-publish")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.rexauth"
    compileSdk = 35

    buildFeatures {
        viewBinding=true
    }

    defaultConfig {
        minSdk = 29

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

dependencies {
    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime)
    implementation(libs.activity)
    implementation(libs.fragment)

    // Biometrics
    implementation(libs.biometric)

    // Security
    implementation(libs.security.crypto)
    implementation(libs.security.identity)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit.v115)
    androidTestImplementation(libs.androidx.espresso.core.v351)

    implementation(libs.pinview)
//    implementation(libs.patternlockview)

}

//afterEvaluate {
//    publishing {
//        publications {
//            create<MavenPublication>("release") {
//                from(components["release"])
//
//                groupId = "com.github.YourGitHubUsername"
//                artifactId = "rexauth"
//                version = "1.0.0"
//
//                pom {
//                    name.set("RexAuth")
//                    description.set("Unified Biometrics, PIN, and Pattern Lock SDK for Android")
//                    url.set("https://github.com/YourGitHubUsername/YourRepo")
//
//                    licenses {
//                        license {
//                            name.set("The Apache License, Version 2.0")
//                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                        }
//                    }
//
//                    developers {
//                        developer {
//                            id.set("yourid")
//                            name.set("Your Name")
//                            email.set("your.email@example.com")
//                        }
//                    }
//                }
//            }
//        }
//    }
//}