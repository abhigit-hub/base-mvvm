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

public class SyncService extends Service {

    public static final String TAG = SyncService.class.getSimpleName();

    @Inject
    DataManager dataManager;

    private int size = 0;

    private CompositeDisposable compositeDisposable;


    public static Intent getStartIntent(Context context) {
        return new Intent(context, SyncService.class);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SyncService.class);
        context.startService(starter);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, SyncService.class));
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
        AppLogger.d(TAG, "SyncService started");

        //startSync();

        return START_NOT_STICKY;
    }

    private void startSync() {

        compositeDisposable.add(
                dataManager.getBlogListObservable()
                        .subscribeOn(Schedulers.io())
                        .subscribe(list -> {
                            notifyUser(list.size());

                            stopSelf();
                        }, throwable -> {
                            stopSelf();
                        })
        );
    }

    /*private void startSync() {
        final int[] counter = {0};
        final int[] counter2 = {0};
        compositeDisposable.add(
                dataManager.getPostQueueUnSyncedItems()
                        .doOnNext(postQueues -> {
                            if (postQueues != null) {
                                setSize(postQueues.size());

                                if (postQueues.size() == 0)
                                    stopSelf();
                            }
                        })
                        .concatMap(postQueues -> Observable.fromIterable(postQueues))
                        .concatMap(postQueue -> {
                            JSONObject jsonObject = new JSONObject(postQueue.getJsonData());
                            HashMap<String, String> headers = new HashMap<>();
                            headers.put("transactionId", postQueue.getTransactionId());
                            headers.put("surveyId", String.valueOf(postQueue.getSurveyId()));
                            headers.put("userId", String.valueOf(postQueue.getUserId()));
                            headers.put("interviewId", String.valueOf(postQueue.getInterviewId()));
                            headers.put("optionId", String.valueOf(postQueue.getOptionId()));

                            return dataManager.doSaveFormCall(
                                    ApiEndPoint.getCorrespondingEndPoint(postQueue.getInterviewId(),
                                            postQueue.getOptionId()),
                                    postQueue.getTransactionId(), jsonObject, headers);

                        })
                        .flatMap(formSaveResponse -> {
                            counter[0]++;
                            if (formSaveResponse.getCode() == NetworkUtils.RESPONSE_OK) {
                                String transactionId = formSaveResponse.getResponseData();
                                counter2[0]++;

                                if (transactionId != null && !transactionId.isEmpty()) {
                                    return dataManager.updatePostQueueSyncStatus(transactionId, CommonUtils.getCurrentTime());
                                }
                            }
                            return Observable.just(0);
                        })
                        .subscribeOn(Schedulers.io())
                        .subscribe(integer -> {
                            if (counter[0] == size) {
                                //notifyUser(counter2[0]);
                                stopSelf();
                            }
                        }, throwable -> {
                            //notifyUser(counter2[0]);
                            stopSelf();
                        })
        );
    }*/

    @Override
    public void onDestroy() {
        AppLogger.d(TAG, "SyncService stopped");
        compositeDisposable.dispose();
        super.onDestroy();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private void notifyUser(int size) {
        String tempString = "";
        if (size == 0)
            tempString = "No new items to sync";
        else tempString = String.valueOf(size) + " items synced";

        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class), 0);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Blog")
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
