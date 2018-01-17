package org.hits.epa_ng_android.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import org.hits.epa_ng_android.fragments.GraphicalResultFragment;
import org.hits.epa_ng_android.fragments.TextResultFragment;

public class ResultsMenuAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 4;

    public static final int HORIZONTAL_GRAPHIC_FRAGMENT_POSITION = 0;
    public static final int VERTICAL_GRAPHIC_FRAGMENT_POSITION = 1;
    public static final int CIRCULAR_GRAPHIC_FRAGMENT_POSITION = 2;
    public static final int TEXT_RESULT_FRAGMENT_POSITION = 3;

    private FragmentManager mFragmentManager;

    private String mTabTitles[];

    private GraphicalResultFragment mHorizontalGraphicFragment;
    private GraphicalResultFragment mVerticalGraphicFragment;
    private GraphicalResultFragment mCircularGraphicFragment;
    private TextResultFragment mTextFragment;

    public Fragment mFragmentList[] = new Fragment[]{
            mHorizontalGraphicFragment,
            mVerticalGraphicFragment,
            mCircularGraphicFragment,
            mTextFragment
    };

    private String mHorizontalGraphicFragmentTag;
    private String mVerticalGraphicFragmentTag;
    private String mCircularGraphicFragmentTag;
    private String mTextFragmentTag;

    private String mFragmentListTags[] = new String[]{
            mHorizontalGraphicFragmentTag,
            mVerticalGraphicFragmentTag,
            mCircularGraphicFragmentTag,
            mTextFragmentTag
    };

    private String mTreeName;
    private String mUploadedQSFileUUID;

    public ResultsMenuAdapter(String treeName, String uploadedQSFileUUID, FragmentManager fm, String tabTitles[]) {
        super(fm);
        this.mTreeName = treeName;
        this.mUploadedQSFileUUID = uploadedQSFileUUID;
        this.mFragmentManager = fm;
        this.mTabTitles = tabTitles;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        if (mFragmentList[position] == null) {

            switch (position) {
                case HORIZONTAL_GRAPHIC_FRAGMENT_POSITION:
                    GraphicalResultFragment horizontalGraphicResultFragment = new GraphicalResultFragment();
                    horizontalGraphicResultFragment.loadHorizontalImage(mTreeName, mUploadedQSFileUUID);
                    mFragmentList[position] = horizontalGraphicResultFragment;
                    break;

                case VERTICAL_GRAPHIC_FRAGMENT_POSITION:
                    GraphicalResultFragment verticalGraphicResultFragment = new GraphicalResultFragment();
                    verticalGraphicResultFragment.loadVerticalImage(mTreeName, mUploadedQSFileUUID);
                    mFragmentList[position] = verticalGraphicResultFragment;
                    break;

                case CIRCULAR_GRAPHIC_FRAGMENT_POSITION:
                    GraphicalResultFragment circularGraphicResultFragment = new GraphicalResultFragment();
                    circularGraphicResultFragment.loadCircularImage(mTreeName, mUploadedQSFileUUID);
                    mFragmentList[position] = circularGraphicResultFragment;
                    break;

                case TEXT_RESULT_FRAGMENT_POSITION:
                    TextResultFragment textResultFragment = new TextResultFragment();
                    textResultFragment.loadTextResult(mTreeName, mUploadedQSFileUUID);
                    mFragmentList[position] = textResultFragment;
                    break;
            }

            fragment = mFragmentList[position];
        } else {
            fragment = this.mFragmentManager.findFragmentByTag(mFragmentListTags[position]);
        }

        Bundle bundle = new Bundle();
        bundle.putString("title", mTabTitles[position]);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitles[position];
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        mFragmentListTags[position] = createdFragment.getTag();
        return createdFragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    public Fragment getRegisteredFragment(int position) {
        return mFragmentList[position];
    }

    public void clearFragmentsList() {
        mFragmentList = new Fragment[]{};
    }
}
