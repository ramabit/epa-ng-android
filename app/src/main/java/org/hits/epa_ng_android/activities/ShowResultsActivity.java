package org.hits.epa_ng_android.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.adapters.ResultsMenuAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowResultsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayoutShowResults)
    TabLayout mTabLayout;

    @BindView(R.id.viewpagerShowResults)
    ViewPager mViewpager;

    public static final String TREE_NAME_KEY = "treeName";
    public static final String QS_TOKEN_KEY = "qsToken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_results);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String treeName = bundle.getString(TREE_NAME_KEY);
        String qsToken = bundle.getString(QS_TOKEN_KEY);

        String tabTitles[] = new String[]{
                getResources().getString(R.string.title_tab_horizontal_graphic),
                getResources().getString(R.string.title_tab_vertical_graphic),
                getResources().getString(R.string.title_tab_circular_graphic),
                getResources().getString(R.string.title_tab_text_result)};

        mViewpager.setAdapter(new ResultsMenuAdapter(treeName, qsToken, getSupportFragmentManager(),
                tabTitles));
        mTabLayout.setupWithViewPager(mViewpager);
    }

}
