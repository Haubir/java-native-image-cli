# java-native-image-cli

An example of how to create a platform independent CLI written in Java that runs on a GraalVM Native Image instead of the JVM (to improve runtime performance).

## Building a native executable

### Install GraalVM Community Edition

This project uses the GraalVM Community Edition, which is downloaded from [here](https://github.com/graalvm/graalvm-ce-builds/releases/).

After downloading, please follow the instructions for:
- [MacOS](https://www.graalvm.org/latest/docs/getting-started/macos/)
- [Windows](https://www.graalvm.org/latest/docs/getting-started/windows/)
- [Linux](https://www.graalvm.org/latest/docs/getting-started/linux/)

### Build the native image
Run the gradle task `nativeCompile`, either from your IDE or from the terminal:

`./gradlew nativeCompile`

A binary will be compiled and outputted to: `build/native/nativeCompile/omegapoint-cli`

Run it using`./build/native/nativeCompile/omegapoint-cli`

### Install gum

The CLI uses Gum for some neat UI rendering. Install it before running the CLI by following the instructions relevant to your OS and environment [here](https://github.com/charmbracelet/gum#installation).
