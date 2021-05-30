package ${packageName}

import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
* To initialize koin in the application add this method in the Application class in the onCreate() method.
*         startKoin {
*              androidContext(this@App)
*              modules(appModule)
*           }
*/

/*
    Use the module function to declare a module.
*/
val appModule = module {
    single {  }       // Declaring singleton
    factory {  }      // Factory will create a new instance each time that will need one.
    viewModel {  }    // Koin will give a viewModel to the lifecycle ViewModelFactory and help bind it to the current component.
}