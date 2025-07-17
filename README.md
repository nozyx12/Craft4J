# Craft4J

**Craft4J** is a simple and lightweight Minecraft Launcher library.

## Features

- Simple and powerful
- Microsoft and offline auth
- Advanced updates (thanks to [FlowUpdater](https://github.com/FlowArg/FlowUpdater) by [FlowArg](https://github.com/FlowArg))
- Mod loaders support
- Easy game launch with custom JVM arguments

## Installation

### Repository

For Maven:

```xml
<repositories>
    <repository>
        <id>nozyx</id>
        <url>https://maven.nozyx.dev</url>
    </repository>
</repositories>
```

For Gradle (Groovy DSL):

```groovy
repositories {
    maven {
        url "https://maven.nozyx.dev"
    }
}
```

For Gradle (Kotlin DSL):

```kotlin
repositories {
    maven {
        url = uri("https://maven.nozyx.dev")
    }
}
```

### Dependency

For Maven:

```xml
<dependency>
    <groupId>dev.nozyx</groupId>
    <artifactId>craft4j</artifactId>
    <version>1.0.0</version>
</dependency>
```

For Gradle (Groovy DSL):

```groovy
implementation "dev.nozyx:craft4j:1.0.0"
```

For Gradle (Kotlin DSL):

```kotlin
implementation("dev.nozyx:craft4j:1.0.0")
```

## Special Thanks

- [FlowArg](https://github.com/FlowArg) – for his library `FlowUpdater` (used to update and download game files) and for maintaining a fork of `OpenLauncherLib` (originally by [Litarvan](https://github.com/Litarvan), used to launch the game)
- [Litarvan](https://github.com/Litarvan) – for his library `OpenAuth` (used to authenticate with Microsoft)


## License

**NPL (v1) – [See LICENSE file](./LICENSE)**
