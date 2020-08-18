<div  style="text-align:center"><img src="/puzzl-logo.png" alt="Puzzl" width="200"/></div>

# Welcome to Puzzl's Android SDK
Puzzl Android SDK for rendering Puzzl's Employee Onboarding flow

## Add Puzzl SDK to a project

This repo is a sample application that includes the Puzzl Android SDK. To use the Puzzl SDK, copy the puzzl_library directory to your project. You can also run this repo on any device that runs Android 5.0 (Lollipop) or above.

That being said, add the puzzl library (puzzl_library) as a new model into your app.

1. Add `:puzzl_library` into `settings.gradle` :

  ```gradle
    include':puzzl_library'
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
3. Add the puzzl SDK dependency to the application `build.gradle` file:

`implementation project(':puzzl_library')`

## Implementing the Puzzl SDK

1. To use puzzl SDK, you should open `VeriffActivity` from the Activity that you want to launch from and transmit the parameters `<api_key>`, `<employeeID>`, and `<companyID>` there using `startActivityForResult`: 

    In the sample provided, we launch the Puzzl SDK from a button  

  ```Kotlin
    val REQUEST_CODE  = 1
    val PUZZL_LIVE_API_KEY = "<YOUR_PUZZL_LIVE_API_KEY>"
    val PUZZL_EMPLOYEE_ID = "<YOUR_PUZZL_EMPLOYEE_ID>"
    val PUZZL_COMPANY_ID = "<YOUR_PUZZL_COMPANY_ID>"


    check_library.setOnClickListener {
        val intent = Intent(this,VeriffActivity::class.java)
        intent.putExtra("api_key",PUZZL_LIVE_API_KEY)
        intent.putExtra("employeeID", PUZZL_EMPLOYEE_ID)
        intent.putExtra("companyID", PUZZL_COMPANY_ID)
        startActivityForResult(intent,REQUEST_CODE)
    }
  ```

2. The puzzl SDK will send a response code to `onActivityResult`:

  ```Kotlin
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null ) {
            Toast.makeText(this,data.getStringExtra("result"),Toast.LENGTH_LONG).show()
        }
    }
  ```

There will be two response codes:

  `data.getStringExtra("result") == “success”` - *success*       
  `data.getStringExtra("result") == “User cancelled the session.”` - *error*