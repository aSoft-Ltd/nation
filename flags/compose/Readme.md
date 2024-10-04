# Compose Flags

Flag composable to use in kotlin multiplatform

## Platform Support
- Android
- Desktop (JVM)
- iOS
- MacOs Native
- Web (JSCanvas)
- Web (Wasm)

## Setup (Gradle)

Add in your depency
```groovy
dependencies {
  implementation("tz.co.asoft:nation-flags-compose:3.0.14")
}
```

## Usage
```kotlin
Flag(
  country = Country.US,
  modifier = Modifier.padding(5.dp)
)
```
