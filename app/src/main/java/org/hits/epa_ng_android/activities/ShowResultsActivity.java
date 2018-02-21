package org.hits.epa_ng_android.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.adapters.ResultsMenuAdapter;
import org.hits.epa_ng_android.network.EPAngServiceAPI;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowResultsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabLayoutShowResults)
    TabLayout mTabLayout;

    @BindView(R.id.viewpagerShowResults)
    ViewPager mViewpager;

    @BindView(R.id.resultSendEmailFloatingButton)
    FloatingActionButton sendEmailFloatingButton;

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

        sendEmailFloatingButton.setOnClickListener(view -> showEmailDialog(qsToken));
    }

    private void showEmailDialog(String qsToken) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter your email account");

        final EditText emailEditText = new EditText(this);
        builder.setView(emailEditText);

        builder.setPositiveButton("Send", (dialog, which) -> {
            String email = emailEditText.getText().toString();
            EPAngServiceAPI.getInstance().sendResultsEmail(qsToken, email);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

}
