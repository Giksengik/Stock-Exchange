package com.ru.stockexchange.ui.company_info;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ru.stockexchange.R;
import com.ru.stockexchange.ui.main.PlaceholderFragment;

public class SectionsPagerAdapterCompany extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.company_tab_text_1,  R.string.company_tab_text_2,
            R.string.company_tab_text_3,R.string.company_tab_text_4,R.string.company_tab_text_5};
    private final Context mContext;

    public SectionsPagerAdapterCompany(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceHolderFragmentCompany.newInstance(position + 1);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
