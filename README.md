# Lokker

Simple Kotlin library for Android to load images. Based on [coroutines](https://kotlinlang.org/docs/reference/coroutines.html).

## API

### Initialization

```Kotlin
Lokker.initWith(fetcher) { // Specify how to get a Bitmap fron an url
  cache = ...
  ...
}
```

### Usage

```Kotlin
val cancellableRequest = imageview
  .withLokker(this) // See below for lokker executor
  .setImageFromUrl(url)

cancellableRequest.cancel() // Cancel the lokker loading if needed
```

### Lokker Executor

TODO

## Remaining

* Provide some builtin/tested fetcher: Fuel, OkHttp,...
* Provide some builtin/tested cache: memory, disk based
* Allow to transform the Bitmap before rendering
* Make it multiplatform (separate core from Android dependencies and add Js implementation)
* Allow for local Lokker instance (currently only one instance [one cache, one fetcher] can be used for the whole app)