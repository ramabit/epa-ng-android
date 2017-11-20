package org.hits.epa_ng_android.models;

import android.net.Uri;

import java.io.File;

public class QSFile {

    private File file;
    private String name;
    private Uri uri;
    private String token;

    public QSFile(File file, String name, Uri uri) {
        this.file = file;
        this.name = name;
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
