package io.comet.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import io.comet.Fragment.BarcodeScanFragment;
import io.comet.Fragment.BroadcastFragment;
import io.comet.Fragment.CapturePhotoFragment;
import io.comet.Fragment.LoginFragment;
import io.comet.Fragment.LogoutFragment;
import io.comet.Fragment.StartupFragment;
import io.comet.Fragment.WeatherFragment;
import io.comet.Listener.NetworkBroadcast;
import io.comet.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final public int FRAGMENT_ID_STARTUP = 0;
    final public int FRAGMENT_ID_LOGIN = 1;
    final public int FRAGMENT_ID_LOGOUT = 2;
    final public int FRAGMENT_ID_WEATHER = 3;
    final public int FRAGMENT_ID_BROADCAST = 4;
    final public int FRAGMENT_ID_BARCODE = 5;
    final public int FRAGMENT_ID_CAPTURE = 6;
    final private long FINISH_INTERVAL_TIME = 2000L;

    private IntentFilter networkFilter = null;
    private NetworkBroadcast networkReceiver = null;
    private StartupFragment startupFragment = null;
    private LoginFragment loginFragment = null;
    private WeatherFragment weatherFragment = null;
    private BroadcastFragment broadcastFragment = null;
    private BarcodeScanFragment barcodeScanFragment = null;
    private CapturePhotoFragment capturePhotoFragment = null;
    private SparseArray<Fragment> mFragments = new SparseArray<Fragment>();
    private DrawerLayout mDrawerLayout = null;
    private Fragment mCurrentFragment = null;
    private int mCurrentFragmentIdx = FRAGMENT_ID_STARTUP;
    private long backPressedTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragments.put(FRAGMENT_ID_STARTUP, new StartupFragment());
        mFragments.put(FRAGMENT_ID_LOGIN, new LoginFragment());
        mFragments.put(FRAGMENT_ID_LOGOUT, new LogoutFragment());
        mFragments.put(FRAGMENT_ID_WEATHER, new WeatherFragment());
        mFragments.put(FRAGMENT_ID_BROADCAST, new BroadcastFragment());
        mFragments.put(FRAGMENT_ID_BARCODE, new BarcodeScanFragment());
        mFragments.put(FRAGMENT_ID_CAPTURE, new CapturePhotoFragment());

        replaceFragment(FRAGMENT_ID_STARTUP);
        networkFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkReceiver = new NetworkBroadcast(this);
    }

    public void replaceFragment(int index) {
        Fragment fragment = null;
        fragment = mFragments.get(index);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.mainContainer, fragment).commit();

        mCurrentFragment = fragment;
        mCurrentFragmentIdx = index;
        if (mDrawerLayout != null) {
            mDrawerLayout.closeDrawers();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        replaceFragment(mCurrentFragmentIdx);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navRest1) {
            replaceFragment(FRAGMENT_ID_LOGIN);
        } else if (id == R.id.navRest2) {
            replaceFragment(FRAGMENT_ID_WEATHER);
        } else if (id == R.id.navBarcode) {
            replaceFragment(FRAGMENT_ID_BROADCAST);
        } else if (id == R.id.navCapture) {
            replaceFragment(FRAGMENT_ID_BARCODE);
        } else if (id == R.id.navCapture) {
            replaceFragment(FRAGMENT_ID_CAPTURE);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            long tmpTime = System.currentTimeMillis();
            long intervalTime = tmpTime - backPressedTime;
            if (intervalTime > 0 && intervalTime < FINISH_INTERVAL_TIME) {
                super.onBackPressed();
            } else {
                backPressedTime = tmpTime;
                Toast.makeText(this, R.string.back_again, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
