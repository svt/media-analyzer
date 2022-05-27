# media-analyzer

[![REUSE status](https://api.reuse.software/badge/github.com/svt/media-analyzer)](https://api.reuse.software/info/github.com/svt/media-analyzer)
![GitHub tag (latest SemVer)](https://img.shields.io/github/v/tag/svt/flum)
![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)

A media analyzer lib that utilizes [FFprobe](https://ffmpeg.org/ffprobe.html) and [Mediainfo](https://mediaarea.net/en/MediaInfo)
and merges the result into one data model.

- **MediaAnalyzer**
 The main service, that given a media file attempts to analyze it with using Mediainfo followed by FFprobe, translating
 the results in intermediary objects before merging into a final data model.

## Usage

Make sure FFprobe and MediaInfo are installed on your machine or image.

Add the lib as a dependency to your build.gradle

```kotlin

implementation("se.svt.oss:media-analyzer:x.y.z")

```


## Tests

run `./gradlew check` for unit tests and code quality checks

Integration tests can be skipped by running `./gradlew check -PrunIntegrationTest=false` if you do not have ffmpeg & mediainfo installed.

## Getting help

If you have questions, concerns, bug reports, etc, please file an issue in this repository's Issue Tracker.

## Getting involved

This project is very much a work in progress so all kinds of feedback are welcome, bug reports,
feature requests etc are welcome. Details on how to contribute can be found in [CONTRIBUTING](docs/CONTRIBUTING.adoc).

## License

This software is released under the:

[Apache License 2.0](LICENSE)

Copyright 2020 Sveriges Television AB

## Primary Maintainers

SVT Videocore team <videocore@teams.svt.se>
