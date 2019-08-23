import com.google.protobuf.gradle.*
import org.gradle.kotlin.dsl.provider.gradleKotlinDslOf
import net.researchgate.release.GitAdapter

plugins {
    java
    idea
    id("com.google.protobuf") version "0.8.8"
    id("net.researchgate.release") version "2.8.0"
    `java-library`
}
group = "$group"
version = "$version"

repositories {
    jcenter()
    maven("https://plugins.gradle.org/m2/")
}

dependencies {
    compile("com.google.protobuf:protobuf-java:3.6.1")
    compile("io.grpc:grpc-stub:1.15.1")
    compile("io.grpc:grpc-protobuf:1.15.1")

    protobuf(files("lib/protos.tar.gz"))
    testCompile("junit:junit:4.12")
    testProtobuf(files("lib/protos-test.tar.gz"))

}
protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.6.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.15.1"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}
release {
    failOnSnapshotDependencies = true
    with (propertyMissing("git") as GitAdapter.GitConfig) {
        requireBranch = "master"
    }
}
