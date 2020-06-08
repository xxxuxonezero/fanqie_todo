package com.android.sql;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.sql.activity.R;
import com.google.android.material.tabs.TabLayout;

public class BottomTabLayoutActivity extends AppCompatActivity {
    private TabLayout mTabLayout;
    private Fragment[]mFragmensts;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success_activity);
        mFragmensts = DataGenerator.getFragments("TabLayout Tab");

        initView();

    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_bottom);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                onTabItemSelected(tab.getPosition());

                //改变Tab 状态
                for(int i=0;i< mTabLayout.getTabCount();i++){
                    if(i == tab.getPosition()){
                        mTabLayout.getTabAt(i).setIcon(DataGenerator.mTabResPressed[i]);
                    }else{
                        mTabLayout.getTabAt(i).setIcon(DataGenerator.mTabRes[i]);
                    }
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.todo1).setText(DataGenerator.mTabTitle[0]));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.data).setText(DataGenerator.mTabTitle[1]));

    }

    private void onTabItemSelected(int position){
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = mFragmensts[0];
                break;
            case 1:
                fragment = mFragmensts[1];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_self,fragment).commit();
        }
    }
}
