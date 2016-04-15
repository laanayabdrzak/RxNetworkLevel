#RxNetworkLevel

Useful library for check network signal level

public class MainActivity extends AppCompatActivity {

    private TextView status;
    private TextView statusNetworkLevel;
    private TextView statusBatteryLevel;
    private Subscription mSubscription;
    private Subscription mSubscriptionNetworkLevel;
    private Subscription mSubscriptionBatteryLevel;
    private RxConnectivityState mRxConnectivityState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        status = (TextView) findViewById(R.id.state);
        statusNetworkLevel = (TextView) findViewById(R.id.state_network_level);
        statusBatteryLevel = (TextView) findViewById(R.id.state_battery_level);
        mRxConnectivityState = new RxConnectivityState();
        mSubscription = mRxConnectivityState.observeConnectivity(getApplicationContext())
                                 .subscribeOn(Schedulers.io())
                                 .observeOn(AndroidSchedulers.mainThread())
                                 .subscribe(new Action1<UtilityNetworkState>() {
                                     @Override
                                     public void call(UtilityNetworkState utilityNetworkState) {

                                            if (utilityNetworkState == UtilityNetworkState.WIFI_CONNECTED){
                                                status.setText("Connected");
                                            }
                                     }
                                 });

        mSubscriptionNetworkLevel = new RxNetworkLevel().observeWifiSignalLevel(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<UtilityNetworkLevel.NetworkSignalLevel>() {
                    @Override
                    public void call(UtilityNetworkLevel.NetworkSignalLevel networkSignalLevel) {
                        statusNetworkLevel.setText("Network Level: "+networkSignalLevel.toString());
                    }
                });
    }

    @Override
    protected void onPause() {
        mRxConnectivityState.safelyUnsubscribeToRxConnectivityState(mSubscription);
        mRxConnectivityState.safelyUnsubscribeToRxConnectivityState(mSubscriptionNetworkLevel);
        super.onPause();
    }
}
