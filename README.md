# media-analyzer

[![REUSE status](https://api.reuse.software/badge/github.com/svt/media-analyzer)](https://api.reuse.software/info/github.com/svt/media-analyzer)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/svt/media-analyzer)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

A media analyzer lib that utilizes [FFprobe](https://ffmpeg.org/ffprobe.html) and [MediaInfo](https://mediaarea.net/en/MediaInfo)
and merges the result into one data model.

- **MediaAnalyzer** — the main service. Given a media file, it analyzes it with MediaInfo
  followed by ffprobe, translates the results into intermediary objects and merges them
  into the final data model.

## Requirements

- Java 17+
- ffprobe and MediaInfo on the PATH (8.x / 26.x or later recommended — the field mappings
  follow current output shapes)
- MediaInfo >= 25.09 for the HDR mastering and light-level fields
  (`masteringPeakNits`, `masteringMinNits`, `maxCllNits`, `maxFallNits`): the split
  `MasteringDisplay_Luminance_Min`/`_Max` fields first appear in that release, and older
  versions report these values as null.

## Usage

Add the lib as a dependency to your `build.gradle.kts`:

```kotlin
implementation("se.svt.oss:media-analyzer:x.y.z")
```

```kotlin
val mediaFile = MediaAnalyzer().analyze("/path/to/file.mxf")
```

Requires Jackson 2 on the classpath (`jackson-module-kotlin` is pulled in transitively);
a Jackson 3 upgrade is planned once all consuming apps are on Spring Boot 4.

## Tests

Run `./gradlew check` for unit tests and code quality checks. Integration tests (which
need real media files) run by default; pass `-PskipIntegrationTests` to exclude them.

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

This project is very much a work in progress so all kinds of feedback are welcome — bug reports,
feature requests etc. Details on how to contribute can be found in [CONTRIBUTING](docs/CONTRIBUTING.adoc).

## License

This software is released under the:

[Apache License 2.0](LICENSE)

Copyright 2020 Sveriges Television AB
