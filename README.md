<div  style="text-align:center"><img src="/puzzl-logo.png" alt="Puzzl" width="200"/></div>

# Welcome to Puzzl's Android SDK
Puzzl iOS SDK for rendering Puzzl's Employee Onboarding flow

## Add Puzzl SDK to a project

###Add the puzzle library (puzzle_library) as a new model into your app. 

1. Add `:puzzle_library` into `settings.gradle` :

  ```
    include':puzzle_library'
  ```
2. Add the maven `{ url "https://cdn.veriff.me/android/" }`  and maven `{ url 'https://jitpack.io' }` to the `build.gradle`:


  ```gradle
    allprojects{
      repositories { 
        ...        
        maven { url "https://cdn.veriff.me/android/" }        
        maven { url 'https://jitpack.io' }    
      }
    }
  ```
3. Add the Puzzle SDK dependency to the application `build.gradle` file:

`implementation project(':puzzle_library')`