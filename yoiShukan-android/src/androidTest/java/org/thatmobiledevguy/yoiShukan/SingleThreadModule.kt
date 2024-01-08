package org.thatmobiledevguy.yoiShukan

import dagger.Module
import dagger.Provides
import org.thatmobiledevguy.yoiShukan.core.AppScope
import org.thatmobiledevguy.yoiShukan.core.tasks.SingleThreadTaskRunner
import org.thatmobiledevguy.yoiShukan.core.tasks.TaskRunner

@Module
internal object SingleThreadModule {
    @JvmStatic
    @Provides
    @AppScope
    fun provideTaskRunner(): TaskRunner {
        return SingleThreadTaskRunner()
    }
}
