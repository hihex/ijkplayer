ijkplayer, specialized for HexLink
==================================

Please refer to https://github.com/Bilibili/ijkplayer for the standard usage. This repository includes a specialized version to implement the Video Player in HexLink.

## Build environment

- Mac OS X, with Homebrew installed.
- Install Android SDK, Android NDK and Gradle through Homebrew.

        $ brew install android-sdk android-ndk gradle

## How to build

Set up environment variables:

```bash
export ANDROID_SDK=/usr/local/Cellar/android-sdk/24.3.3
export ANDROID_NDK=/usr/local/Cellar/android-ndk/r10e
export ANDROID_HOME="$ANDROID_SDK"
```

Clone the repo

```bash
git clone https://github.com/hihex/ijkplayer.git
cd ijkplayer
git checkout self
```

Perform the standard ijkplayer build instruction

```bash
./init-android.sh

cd android/contrib
./compile-ffmpeg.sh clean
./compile-ffmpeg.sh all

cd ..
./compile-ijk.sh all

cd ijkplayer
gradle assembleRelease
```

Copy the following 4 files to `libs/`:

* `android/ijkplayer/player-armv7a/build/outputs/aar/player-armv7a-release.aar`
* `android/ijkplayer/player-x86/build/outputs/aar/player-x86-release.aar`
* `android/ijkplayer/player-java/build/outputs/aar/player-java-release.aar`
* `android/ijkplayer/sample/build/outputs/aar/sample-release.aar`

## License

ijkplayer is licensed under LGPLv2.1 or later.

