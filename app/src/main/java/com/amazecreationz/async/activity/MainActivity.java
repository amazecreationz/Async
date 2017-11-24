package com.amazecreationz.async.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amazecreationz.async.R;
import com.amazecreationz.async.fragments.HomeFragment;
import com.amazecreationz.async.fragments.LogFragment;
import com.amazecreationz.async.fragments.ProfileFragment;
import com.amazecreationz.async.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        new User(this).deleteUser();
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private HomeFragment homeFragment;
        private LogFragment logFragment;
        private ProfileFragment profileFragment;

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            homeFragment = HomeFragment.newInstance("hello", "there");
            logFragment = LogFragment.newInstance("hello", "log");
            profileFragment = ProfileFragment.newInstance("profile", "fragment");
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:return logFragment;
                case 2:return profileFragment;
                default: return homeFragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
