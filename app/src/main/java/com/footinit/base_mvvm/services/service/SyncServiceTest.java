package com.footinit.base_mvvm.services.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.footinit.base_mvvm.R;
import com.footinit.base_mvvm.data.DataManager;
import com.footinit.base_mvvm.ui.main.MainActivity;
import com.footinit.base_mvvm.utils.AppLogger;

import java.util.Random;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SyncServiceTest extends Service {

    public static final String TAG = SyncServiceTest.class.getSimpleName();

    @Inject
    DataManager dataManager;

    private CompositeDisposable compositeDisposable;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncServiceTest.class);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncServiceTest.class);
        context.startService(starter);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, SyncServiceTest.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (compositeDisposable == null)
            compositeDisposable = new CompositeDisposable();

        AndroidInjection.inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        AppLogger.d(TAG, "SyncServiceTest started");

        //startSync();

        return START_NOT_STICKY;
    }

    private void startSync() {

        compositeDisposable.add(
                dataManager.getOpenSourceListObservable()
                        .subscribeOn(Schedulers.io())
                        .subscribe(list -> {
                            notifyUser(list.size());

                            stopSelf();
                        }, throwable -> {
                            stopSelf();
                        })
        );
    }

    @Override
    public void onDestroy() {
        AppLogger.d(TAG, "SyncServiceTest stopped");
        compositeDisposable.dispose();
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void notifyUser(int size) {
        String tempString = "";
        if (size == 0)
            tempString = "No new items to sync";
        else tempString = String.valueOf(size) + " items remaining to sync";


        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Open Source")
                .setContentText(tempString)
                .setAutoCancel(true)
                .setContentIntent(pi)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setShowWhen(true)
                .setColor(Color.RED)
                .setLocalOnly(true)
                .build();

        NotificationManagerCompat.from(getApplicationContext())
                .notify(new Random().nextInt(), notification);
    }
}
