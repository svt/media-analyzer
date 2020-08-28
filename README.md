[![REUSE status](https://api.reuse.software/badge/git.fsfe.org/reuse/api)](https://api.reuse.software/info/git.fsfe.org/reuse/api)

media-analyzer
===

A media analyzer lib that utilizes [FFprobe](https://ffmpeg.org/ffprobe.html) and [Mediainfo](https://mediaarea.net/en/MediaInfo) 
and merges the result into one data model. 
 
 - **MediaAnalyzer**
 The main service, that given a media file attempts to analyze it with using Mediainfo followed by FFprobe, translating
 the results in intermediary objects before merging into a final data model. 
 
This library is written in kotlin.

### Usage ###
Make sure ffprobe and mediainfo are installed on your machine or image.

Add the lib as a dependency to your build.gradle


```
implementation 'se.svt.oss:media-analyzer:1.1.0'
```

### Contributing ###

#### Tests ####

run `./gradlew check` for unit tests and code quality checks

Integration tests can be skipped by running `./gradlew check -PrunIntegrationTest=false` if you do not have ffmpeg & mediainfo installed.

This is used currently in the github pipelines, so make sure to run integration tests locally. 
  
## Update version and release to jcenter/bintray (for maintainers)

1. Make sure you are on master branch and that everything is pushed to master
2. `./gradlew release` to tag a new version (this uses Axion release plugin). The new tag is automatically pushed to github,
   and the github pipeline will build the project and publish the new version to bintray.

## License

Copyright 2020 Sveriges Television AB

This software is released under the Apache 2.0 License.

## Primary Maintainers

SVT Videocore team <videocore@teams.svt.se>
