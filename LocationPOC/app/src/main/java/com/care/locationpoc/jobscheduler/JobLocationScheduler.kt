package com.care.locationpoc.jobscheduler

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context

object JobLocationScheduler {

    fun startJob(context: Context) {
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val job = createJob(context)
        jobScheduler.schedule(job)
    }

    private fun createJob(context: Context): JobInfo {
        val jobInfo = JobInfo.Builder(123, ComponentName(context, LocationJobService::class.java))

        val job = jobInfo.setRequiresCharging(false)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setMinimumLatency(10000)
            .build()

        return job
    }
}