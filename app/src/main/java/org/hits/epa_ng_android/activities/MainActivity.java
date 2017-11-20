package org.hits.epa_ng_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.models.QSFile;
import org.hits.epa_ng_android.models.responses.QSFileUploadResponse;
import org.hits.epa_ng_android.models.responses.TreesResponse;
import org.hits.epa_ng_android.network.EPAngServiceAPI;
import org.hits.epa_ng_android.utils.FileManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

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

    private final static int CHOOSE_FILE_CODE = 100;

    private QSFile mAttachedFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportedTrees();

        selectQSFileButton.setOnClickListener(view -> {
            notQSFileSelected();
            chooseFile();
        });

        uploadQSFileButton.setOnClickListener(view -> uploadQSFile());
        disableButton(uploadQSFileButton);

        sendRequestFloatingButton.setOnClickListener(view -> runAnalysis());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_FILE_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();

                    String fullPath = FileManager.getPath(this, uri);
                    File file = new File(fullPath);

                    String displayName = FileManager.getFileDisplayName(this, uri, file, uriString);

                    QSFile qsFile = new QSFile(file, displayName, uri);
                    qsFileSelected(qsFile);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getSupportedTrees() {
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

    private void runAnalysis() {
        String treeName = treesListSpinner.getSelectedItem().toString();
        String uploadedQSFileUUID = mAttachedFile.getUUIDToken();
        Call<Object> call = EPAngServiceAPI.getInstance().getService().runAnalysis(treeName, uploadedQSFileUUID);
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("Run analysis", response.message());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("Run analysis", t.getMessage());
            }
        });
    }

    public void chooseFile() {
        String mimeType = "*/*";

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType(mimeType);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // special intent for Samsung file manager
        Intent sIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        // if you want any file type, you can skip next line
        sIntent.putExtra("CONTENT_TYPE", mimeType);
        sIntent.addCategory(Intent.CATEGORY_DEFAULT);

        Intent chooserIntent;
        if (getPackageManager().resolveActivity(sIntent, 0) != null) {
            // it is device with Samsung file manager
            chooserIntent = Intent.createChooser(sIntent, "Open file");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{intent});
        } else {
            chooserIntent = Intent.createChooser(intent, "Open file");
        }

        try {
            startActivityForResult(chooserIntent, CHOOSE_FILE_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "No suitable File Manager was found.", Toast.LENGTH_SHORT).show();
        }
    }

    private void qsFileSelected(QSFile qsFile) {
        mAttachedFile = qsFile;
        selectedQSFileNameTextView.setText(qsFile.getName());
        selectedQSFileNameTextView.setVisibility(View.VISIBLE);
        enableButton(uploadQSFileButton);
    }

    private void notQSFileSelected() {
        mAttachedFile = null;
        selectedQSFileNameTextView.setVisibility(View.INVISIBLE);
        disableButton(uploadQSFileButton);
        noQSFileUploaded();
    }

    private void qsFileUploaded() {
        Toast.makeText(this, "Query Sequence file uploaded successfully!", Toast.LENGTH_SHORT).show();
        disableButton(uploadQSFileButton);
        uploadedQSFileTextView.setVisibility(View.VISIBLE);
        sendRequestFloatingButton.setVisibility(View.VISIBLE);
    }

    private void noQSFileUploaded() {
        disableButton(uploadQSFileButton);
        uploadedQSFileTextView.setVisibility(View.INVISIBLE);
        sendRequestFloatingButton.setVisibility(View.INVISIBLE);
    }

    private void uploadQSFile() {
        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create(MediaType.parse("text"), mAttachedFile.getFile());

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("qs", mAttachedFile.getName(), requestFile);

        Call<QSFileUploadResponse> uploadFileCall = EPAngServiceAPI.getInstance().getService().uploadQSFile(body);
        uploadFileCall.enqueue(new Callback<QSFileUploadResponse>() {
            @Override
            public void onResponse(Call<QSFileUploadResponse> call, Response<QSFileUploadResponse> response) {
                Log.d("Upload QS file", response.message());

                QSFileUploadResponse qsFileUploadResponse = response.body();
                if (qsFileUploadResponse != null) {
                    mAttachedFile.setUUIDToken(qsFileUploadResponse.getToken());
                    qsFileUploaded();
                }
            }

            @Override
            public void onFailure(Call<QSFileUploadResponse> call, Throwable t) {
                Log.d("Upload QS file", t.getMessage());
            }
        });
    }

}
