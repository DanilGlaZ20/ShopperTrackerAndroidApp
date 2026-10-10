package com.glazdan.shopperTrackerAndroidApp.presentation.utils

import kotlinx.coroutines.Job
import java.util.concurrent.ConcurrentHashMap


 val deletionJobs = ConcurrentHashMap<Long, Job>() //Хранилище таймеров
fun cancelDelation(id: Long) {
	deletionJobs[id]?.cancel()
	deletionJobs.remove(id)
}