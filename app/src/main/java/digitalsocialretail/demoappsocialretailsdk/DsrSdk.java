package digitalsocialretail.demoappsocialretailsdk;

import android.app.Application;

import com.socialretail.sdk.SRBeaconManager;

import org.altbeacon.beacon.startup.RegionBootstrap;

public class DsrSdk extends Application {
    private RegionBootstrap regionBootstrap;

    @Override
    public void onCreate() {
        super.onCreate();
        SRBeaconManager srBeaconManager = SRBeaconManager.getInstance();
        srBeaconManager.SetContext(this.getApplicationContext());
        srBeaconManager.setSettings(this);
    }
}