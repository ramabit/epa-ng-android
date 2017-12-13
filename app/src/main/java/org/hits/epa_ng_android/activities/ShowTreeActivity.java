package org.hits.epa_ng_android.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
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

    @BindView(R.id.showTreeImageView)
    ImageView imageView;

    public static final int TEXT_RESULT_TYPE = 900;
    public static final int GRAPHICAL_RESULT_TYPE = 901;

    public static final String EPA_DATA_KEY = "EPAngDataKey";
    public static final String TYPE_KEY = "TypeKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tree);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();

        int type = bundle.getInt(TYPE_KEY);

        switch (type) {
            case TEXT_RESULT_TYPE:
                EPAngData epaData = bundle.getParcelable(EPA_DATA_KEY);
                showTextResult(epaData);
                break;

            case GRAPHICAL_RESULT_TYPE:
                Bitmap bitmap = getBitmapFromBundle(bundle);
                showImageResult(bitmap);
                break;
        }

    }

    private void showImageResult(Bitmap bitmap) {
        textView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(bitmap);
    }

    private void showTextResult(EPAngData epaData) {
        imageView.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setText(epaData.getTree());
    }

}
