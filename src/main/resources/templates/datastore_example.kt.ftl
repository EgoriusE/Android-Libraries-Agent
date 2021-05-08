package ${packageName}

import android.content.Context
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

// Instance for access to Datastore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 *  Examples of store key-value pairs with Preferences DataStore.
 *  If you want to store typed objects with Proto DataStore
 *  @see [link](https://developer.android.com/topic/libraries/architecture/datastore#proto-datastore)
 */

// Example of reading from a Preferences DataStore
val EXAMPLE_COUNTER = intPreferencesKey("example_counter")
val Context.exampleCounterFlow: Flow<Int>
    get() = this.dataStore.data
        .map { preferences ->
            // No type safety.
            preferences[EXAMPLE_COUNTER] ?: 0
        }

// Example of writing to a Preferences DataStore
suspend fun Context.incrementCounter() {
    this.dataStore.edit { settings ->
        val currentCounterValue = settings[EXAMPLE_COUNTER] ?: 0
        settings[EXAMPLE_COUNTER] = currentCounterValue + 1
    }
}

/**
 * You can use DataStore in synchronous code.
 * Caution: Avoid blocking threads on DataStore data reads whenever possible. Blocking the UI thread can cause
 * ANRs or UI jank, and blocking other threads can result in deadlock.
 */

val exampleData = runBlocking { context.dataStore.data.first() }