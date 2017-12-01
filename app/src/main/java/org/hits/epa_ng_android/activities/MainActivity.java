package org.hits.epa_ng_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
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
import org.hits.epa_ng_android.models.responses.epa.EPAngData;
import org.hits.epa_ng_android.network.EPAngServiceAPI;
import org.hits.epa_ng_android.network.callbacks.GetSupportedTreesCallback;
import org.hits.epa_ng_android.network.callbacks.RunAnalysisCallback;
import org.hits.epa_ng_android.network.callbacks.UploadQSFileCallback;
import org.hits.epa_ng_android.utils.FileManager;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        EPAngServiceAPI.getInstance().getSupportedTrees(new GetSupportedTreesCallback() {
            @Override
            public void onSuccess(List<String> trees) {
                updateTrees(trees);
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
        EPAngServiceAPI.getInstance().uploadQSFile(mAttachedFile, new UploadQSFileCallback() {
            @Override
            public void onSuccess() {
                qsFileUploaded();
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void runAnalysis() {
        String treeName = treesListSpinner.getSelectedItem().toString();
        String uploadedQSFileUUID = mAttachedFile.getUUIDToken();

        EPAngServiceAPI.getInstance().runAnalysis(treeName, uploadedQSFileUUID, new RunAnalysisCallback() {
            @Override
            public void onSuccess(EPAngData data) {
                // TODO show response data (new tree)
                Intent intent = new Intent(MainActivity.this, ShowTreeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ShowTreeActivity.EPA_DATA_KEY, data);
                intent.putExtras(bundle);
                MainActivity.this.startActivity(intent);
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
