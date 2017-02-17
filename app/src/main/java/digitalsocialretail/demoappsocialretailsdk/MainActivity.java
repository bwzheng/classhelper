package digitalsocialretail.demoappsocialretailsdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.socialretail.sdk.SRBeaconManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST = 26;
    private static boolean arePermissionGranted = false;
    SRBeaconManager srBeaconManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //  Managing of permissions
        if (android.os.Build.VERSION.SDK_INT < 23) {
            arePermissionGranted = true;
            //call load beacon scan
            this.LoadBeaconScan();

        } else {
            List<String> permissionsList = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH);
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED)
                permissionsList.add(Manifest.permission.BLUETOOTH_ADMIN);

            if (permissionsList.size() > 0)
                ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), MY_PERMISSIONS_REQUEST);
            else {
                arePermissionGranted = true;
                //call load beacon scan
                this.LoadBeaconScan();
            }
        }
    }

    public void LoadBeaconScan() {
        srBeaconManager = SRBeaconManager.getInstance();
        srBeaconManager.mContext = MainActivity.this;
        srBeaconManager.fragmentActivity = this;
        srBeaconManager.loadBeaconScan();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (arePermissionGranted)
            srBeaconManager.SetForeGroundModePause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arePermissionGranted) {
            srBeaconManager.SetForeGroundModeOnResume();
            srBeaconManager = SRBeaconManager.getInstance();
            srBeaconManager.mContext = MainActivity.this;
            srBeaconManager.fragmentActivity = this;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (arePermissionGranted)
            srBeaconManager.cleanup();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean tmpPermissionGranted = true;
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST:
                if (grantResults.length > 0) {
                    for (int permission : grantResults) {
                        if (permission != PackageManager.PERMISSION_GRANTED) {
                            tmpPermissionGranted = false;
                            break;
                        }
                    }
                } else {
                    //  Denied
                    tmpPermissionGranted = false;
                }

                if (!tmpPermissionGranted) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                    builder.setTitle("Permission");
                    builder.setMessage("This permission is required to use the Social Retail SDK.");
                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                } else {
                    //call load beacon scan
                    this.LoadBeaconScan();
                    arePermissionGranted = true;
                }
                break;
            default:
        }
    }
}
