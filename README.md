# Lokker

Simple Kotlin library for Android to load images. Based on [coroutines](https://kotlinlang.org/docs/reference/coroutines.html).

## API

### Initialization

```Kotlin
Lokker.initWith(fetcher) { // Fetcher specifies how to get a Bitmap fron an url
  cache = ...
  ...
}
```

### Usage

```Kotlin
val cancellableRequest = imageview
  .withLokker(this) // See below for lokker executor
  .setImageFromUrl(url) { // Can configure the request
    // errorListener = { ... }
    // ....
  }

cancellableRequest.cancel() // Cancel the request if needed
```

## <a name="executor"></a> Lokker Executor

A request depends on an executor whose role is to execute the different actions needed to load and render the image.
The library provides an executor based on the [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines) library: CoroutinesExecutor.

Besides, this executor is aware of the request context to prevent any potential memory leak due to the app/UI lifecycle. It is currently possible to bind the provided executor to a [Android LifecycleOwner](https://developer.android.com/reference/android/arch/lifecycle/LifecycleOwner) object or to any [CoroutineScope](https://github.com/Kotlin/kotlinx.coroutines/blob/master/docs/basics.md#structured-concurrency)

However, having the execution flow and the lifecycle binding defined in the same class is not to be desired in the future. Internals will probably be reworked to decouple execution definition from lifecycle without changing the API.

## Remaining

* Provide some builtin/tested fetcher: Fuel, OkHttp,...
* Provide some builtin/tested cache: memory, disk based
* Allow to transform the Bitmap before rendering
* Make it multiplatform (separate core from Android dependencies and add Js implementation)
* Allow for local Lokker instance (currently only one instance [one cache, one fetcher] can be used for the whole app)