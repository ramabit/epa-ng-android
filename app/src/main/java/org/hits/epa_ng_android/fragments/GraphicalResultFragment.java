package org.hits.epa_ng_android.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.hits.epa_ng_android.R;
import org.hits.epa_ng_android.network.EPAngServiceAPI;
import org.hits.epa_ng_android.network.callbacks.RunAnalysisWithGraphicResultCallback;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphicalResultFragment extends Fragment {

    @BindView(R.id.treeImageView)
    ImageView treeImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graphical_result, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public void loadHorizontalImage(String treeName, String uploadedQSFileUUID) {
        EPAngServiceAPI.getInstance()
                .getHorizontalGraphic(treeName, uploadedQSFileUUID, new RunAnalysisWithGraphicResultCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        treeImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadVerticalImage(String treeName, String uploadedQSFileUUID) {
        EPAngServiceAPI.getInstance()
                .getVerticalGraphic(treeName, uploadedQSFileUUID, new RunAnalysisWithGraphicResultCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        treeImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadCircularImage(String treeName, String uploadedQSFileUUID) {
        EPAngServiceAPI.getInstance()
                .getCircularGraphic(treeName, uploadedQSFileUUID, new RunAnalysisWithGraphicResultCallback() {
                    @Override
                    public void onSuccess(Bitmap bitmap) {
                        treeImageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
