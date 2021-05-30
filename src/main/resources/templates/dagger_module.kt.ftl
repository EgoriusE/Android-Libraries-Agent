package ${packageName}

import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

/**
* This class demonstrate how to inject activity into dagger
*/
@Module
abstract class ActivityInjectionsModule {

    @ContributesAndroidInjector(modules = [/* You activity class here */])
    abstract fun appActivityInjector(): /* You activity class here */
}

@Module
abstract class SampleModule {

    @Provides
    fun provideSome() {
    }
}