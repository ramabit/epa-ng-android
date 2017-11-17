package org.hits.epa_ng_android.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.models.responses.TreesResponse;
import org.hits.epa_ng_android.network.EPAngServiceAPI;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.mainLoadingTreesProgressBar)
    ProgressBar loadingTreesProgressBar;

    @BindView(R.id.mainLoadingTreesTextView)
    TextView loadingTreesTextView;

    @BindView(R.id.mainTreesListSpinner)
    Spinner treesListSpinner;

    @BindView(R.id.mainSelectQSFileButton)
    Button selectQSFileButton;

    @BindView(R.id.mainSelectedQSFileNameTextView)
    TextView selectedQSFileNameTextView;

    @BindView(R.id.mainUploadQSFileButton)
    Button uploadQSFileButton;

    @BindView(R.id.mainUploadedQSFileTextView)
    TextView uploadedQSFileTextView;

    @BindView(R.id.mainSendRequestFloatingButton)
    FloatingActionButton sendRequestFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportedTrees();

        sendRequestFloatingButton.setOnClickListener(view ->
                Snackbar.make(view, "Here I will send the request", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void getSupportedTrees() {
        treesListSpinner.setEnabled(false);
        loadingTreesProgressBar.setVisibility(View.VISIBLE);
        loadingTreesTextView.setVisibility(View.VISIBLE);

        Call<TreesResponse> getTreesCall = EPAngServiceAPI.getInstance().getService().getTrees();
        getTreesCall.enqueue(new Callback<TreesResponse>() {
            @Override
            public void onResponse(Call<TreesResponse> call, retrofit2.Response<TreesResponse> response) {
                TreesResponse treesResponse = response.body();
                if (treesResponse != null) {
                    updateTrees(treesResponse.getTrees());
                }
            }

            @Override
            public void onFailure(Call<TreesResponse> call, Throwable t) {
                Log.e("ERROR", t.getMessage());
            }
        });
    }

    private void updateTrees(List<String> trees) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, trees);
        treesListSpinner.setAdapter(adapter);

        loadingTreesProgressBar.setVisibility(View.GONE);
        loadingTreesTextView.setVisibility(View.GONE);
        treesListSpinner.setEnabled(true);
    }

}
