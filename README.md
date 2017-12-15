# Android Architecture : base-mvvm

Inspiration mostly from the good works of [Mindorks Open Source Project](https://github.com/MindorksOpenSource/android-mvvm-architecture). But this app is tweaked a little on the navigation front and also houses a few more features, compared to the original app.

This repo is a sample application that implements **MVVM** architecture using **ViewModel**, **RxJava2**, **Dagger2**, **Retrofit2**, **Room**, **LiveData** and **Butterknife**.
 While there are several open sourced examples on MVVM using Data Binding library, this repo doesn't use Data Binding, instead uses Butterknife and LiveData to bind View and ViewModel together. _(Original App uses Google's Data Binding library)_  
 This repo uses Retrofit and Room library for network calls and database operations respectively _(Original App uses FastAndroidNetworking and GreenDao)_.
 
 Another highlight is that this repo also has **Data Persistency** unlike the original app. 

## Topics
- [What does this app do?](#what-does-this-app-do)
- [Quick Glimpse](#quick-glimpse)
- [Naive Attempt at Animated Vector Drawable](#naive-attempt-at-animated-vector-drawable)
- [APIs and Architectures Used](#apis-and-architectures-used)
- [Library reference resources](#library-reference-resources)

## What does this app do?
To sum up in one line, this app shows news feed(Open Source codes and Developer Blogs) to the consumer. 
Now, the idea is essentially the same but the manner in which it is served to the Consumer, differs(precisely 3).

## Quick Glimpse

![Home Screen](https://imgur.com/3dePdEw.gif)
![Feed Screen](https://imgur.com/IHM7S10.gif)
![Blog Details Screen](https://imgur.com/duga0Y4.gif)
![Open Source Details Screen](https://imgur.com/hr1jVK8.gif)

## Naive Attempt at Animated Vector Drawable
![Chrome](https://imgur.com/o6iSFz1.gif)

## APIs and Architectures Used

1. Model View ViewModel Architecture
1. RxJava/RxAndroid 2 for concurrency
1. Dagger2 for managing the dependency tree and also for dependency injection
1. Room library from AAC(from Google), for database operation
1. ViewModel from AAC(from Google), for acting as intermediate stateholder for the View and Model in a lifecycle conscious way.
1. LiveData from AAC(from Google), used in ViewModel and Room, so that the data flowing from Data layer to View layer happens due to the react nature of the LiveData.
1. Retrofit2 library for network calls
1. ButterKnife for View Injections
1. Glide for Image Loading


I'm also looking at adding the following Libraries for future iterations:

1. LeakCanary
1. Mockito
1. JUnit
1. Bottom Sheets
1. Shared Element Transition and Content Transition




## Library reference resources
1. RxJava2: https://blog.kaush.co/2017/06/21/rxjava1-rxjava2-migration-understanding-changes/

