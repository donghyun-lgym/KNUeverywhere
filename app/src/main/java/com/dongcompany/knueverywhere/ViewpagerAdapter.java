package com.dongcompany.knueverywhere;

import android.widget.BaseAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;

public class ViewpagerAdapter extends FragmentStatePagerAdapter {

    public ViewpagerAdapter(@NonNull FragmentManager fm) {
        super(fm);

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {  //position 번째 페이지에 어떠한 Fragment(화면) 구성할지 설정
        return null;
    }

    @Override
    public int getCount() {  //생성할 페이지 개수
        return 0;
    }
}
