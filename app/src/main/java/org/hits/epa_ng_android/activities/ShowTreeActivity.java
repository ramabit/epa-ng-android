package org.hits.epa_ng_android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.models.responses.epa.EPAngData;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowTreeActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.showTreeTextView)
    TextView textView;

    public static final String EPA_DATA_KEY = "EPAngDataKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tree);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        EPAngData epaData = bundle.getParcelable(EPA_DATA_KEY);
        textView.setText(epaData.getTree());
    }

}
