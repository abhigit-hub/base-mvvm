package com.footinit.base_mvvm.services.jobs;

import android.support.annotation.NonNull;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.footinit.base_mvvm.services.service.SyncService;

import java.util.concurrent.TimeUnit;

public class SyncJob extends Job {

    public static final String TAG = "job_sync";


    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {
        // run your job here

        SyncService.start(getContext());

        return Job.Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(SyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(15), TimeUnit.MINUTES.toMillis(5))
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }
}