package ${packageName}

import dagger.Component

/**
* To inject Dagger in the your application add this code in onCreate() method in your application
* class
* DaggerAppComponent
*      .builder()
*      .application(this)
*      .build()
*      .inject(this)
*/

/**
* Dagger can create a graph of the dependencies in your project that it can use to find out where
* it should get those dependencies when they are needed. To make Dagger do this, you need to
* create an interface and annotate it with @Component. Dagger creates a container as you would
* have done with manual dependency injection.
* More documentation here: https://developer.android.com/training/dependency-injection/dagger-basics#dagger-scopes
*/
@Component(modules = [SampleModule::class])
interface AppComponent {

}