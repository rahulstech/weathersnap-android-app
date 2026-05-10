package rahulstech.android.weathersnap.data.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rahulstech.android.weathersnap.data.local.AppDatabase
import rahulstech.android.weathersnap.data.local.dao.SavedWeatherReportDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "weathersnap_db"
        ).build()
    }

    @Provides
    fun provideSavedWeatherReportDao(database: AppDatabase): SavedWeatherReportDao {
        return database.savedWeatherReportDao()
    }
}
