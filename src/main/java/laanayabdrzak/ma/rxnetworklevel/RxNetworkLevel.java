package laanayabdrzak.ma.rxnetworklevel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Looper;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.subscriptions.Subscriptions;

/**
 * Created by abderrazak on 14/04/2016.
 */
public class RxNetworkLevel {

    /**
     * Observes WiFi signal level with predefined max num levels.
     * Returns WiFi signal level as enum with information about current level
     *
     * @param context Context of the activity or an application
     * @param maxLevel
     * @return WifiSignalLevel as an enum
     */
    public Observable<UtilityNetworkLevel.NetworkSignalLevel> observeWifiSignalLevel(final Context context) {
        return observeWifiSignalLevel(context, UtilityNetworkLevel.NetworkSignalLevel.getMaxLevel()).map(new Func1<Integer, UtilityNetworkLevel.NetworkSignalLevel>() {
            @Override
            public UtilityNetworkLevel.NetworkSignalLevel call(Integer integer) {
                return UtilityNetworkLevel.fromLevel(integer);
            }
        });
    }
    /**
     * Observes Network signal level.
     * Returns Network signal level as an integer
     *
     * @param context Context of the activity or an application
     * @param numLevels The number of levels to consider in the calculated level as Integer
     * @return RxJava Observable with WiFi signal level
     */
    public Observable<Integer> observeWifiSignalLevel(final Context context, final int numLevels) {
        final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        final IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);

        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override public void call(final Subscriber<? super Integer> subscriber) {
                final BroadcastReceiver receiver = new BroadcastReceiver() {
                    @Override public void onReceive(Context context, Intent intent) {
                        final int rssi = wifiManager.getConnectionInfo().getRssi();
                        final int level = WifiManager.calculateSignalLevel(rssi, numLevels);
                        subscriber.onNext(level);
                    }
                };

                context.registerReceiver(receiver, filter);

                subscriber.add(unsubscribeInUiThread(new Action0() {
                    @Override public void call() {
                        context.unregisterReceiver(receiver);
                    }
                }));
            }
        }).defaultIfEmpty(0);
    }

    private Subscription unsubscribeInUiThread(final Action0 unsubscribe) {
        return Subscriptions.create(new Action0() {

            @Override public void call() {
                if (Looper.getMainLooper() == Looper.myLooper()) {
                    unsubscribe.call();
                } else {
                    final Scheduler.Worker inner = AndroidSchedulers.mainThread().createWorker();
                    inner.schedule(new Action0() {
                        @Override public void call() {
                            unsubscribe.call();
                            inner.unsubscribe();
                        }
                    });
                }
            }
        });
    }
}
