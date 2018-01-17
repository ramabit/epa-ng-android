package org.hits.epa_ng_android.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.models.responses.epa.EPAngData;
import org.hits.epa_ng_android.network.EPAngServiceAPI;
import org.hits.epa_ng_android.network.callbacks.RunAnalysisWithTextResultCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextResultFragment extends Fragment {

    @BindView(R.id.treeTextView)
    TextView treeTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void loadTextResult(String treeName, String uploadedQSFileUUID) {
        EPAngServiceAPI.getInstance().runAnalysisWithTextResult(treeName, uploadedQSFileUUID, new RunAnalysisWithTextResultCallback() {
            @Override
            public void onSuccess(EPAngData data) {
                treeTextView.setText(data.getTree());
            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
