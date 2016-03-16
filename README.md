# UmaLibrary

Android app that I made with my team for the Hackers Week 3.0 Hackaton that we won :)

# Purpose

With the app you can see the photos of some libraries of Malaga that the users can upload. 

# Dependencies

Just add this line to your gradle build.local file:

    dependencies {
        compile 'com.firebase:firebase-client-android:2.5.1+'
    }

If you are getting a build error complaining about duplicate files you can choose to exclude those files by adding the packagingOptions directive to your build.gradle file: 

    android {
        ...
        packagingOptions {
            exclude 'META-INF/LICENSE'
            exclude 'META-INF/LICENSE-FIREBASE.txt'
            exclude 'META-INF/NOTICE'
        }
    }
