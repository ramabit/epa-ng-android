package org.hits.epa_ng_android.activities;

import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
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
import android.widget.Toast;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.models.QSFile;
import org.hits.epa_ng_android.models.responses.TreesResponse;
import org.hits.epa_ng_android.network.EPAngServiceAPI;

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

        sendRequestFloatingButton.setOnClickListener(view ->
                Snackbar.make(view, "Here I will send the request", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show());
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
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File file = new File(uriString);

                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = file.getName();
                    }

                    QSFile qsFile = new QSFile(file, displayName, uri);
                    qsFileSelected(qsFile);
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
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

    public void chooseFile() {
        String mimeType = "*/*";

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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
        uploadQSFileButton.setEnabled(true);
        noQSFileUploaded();
    }

    private void notQSFileSelected() {
        mAttachedFile = null;
        selectedQSFileNameTextView.setVisibility(View.INVISIBLE);
        uploadQSFileButton.setEnabled(false);
    }

    private void qsFileUploaded() {
        uploadQSFileButton.setVisibility(View.INVISIBLE);
        uploadedQSFileTextView.setVisibility(View.VISIBLE);
        sendRequestFloatingButton.setVisibility(View.VISIBLE);
    }

    private void noQSFileUploaded() {
        uploadQSFileButton.setVisibility(View.VISIBLE);
        uploadedQSFileTextView.setVisibility(View.INVISIBLE);
        sendRequestFloatingButton.setVisibility(View.INVISIBLE);
    }

    private void uploadQSFile() {
        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse(getContentResolver().getType(mAttachedFile.getUri())),
                        mAttachedFile.getFile());

        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("qs", mAttachedFile.getName(), requestFile);

        Call<Object> uploadFileCall = EPAngServiceAPI.getInstance().getService().uploadQSFile(body);
        uploadFileCall.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("LOG", response.message());
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("LOG", t.getMessage());
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj,
                null, null, null);
        Cursor cursor = loader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(columnIndex);
        cursor.close();
        return result;
    }

}
