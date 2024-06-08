package com.example.myandroidproject.helpers;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MultipartRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private final File mFile;
    private final String mFileName;
    private final Map<String, String> mParams;

    public MultipartRequest(String url, File file, String fileName, Map<String, String> params,
                            Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
        mFile = file;
        mFileName = fileName;
        mParams = params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        // Add any headers if needed
        return super.getHeaders();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data";
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // Write file data to output stream
            FileInputStream fileInputStream = new FileInputStream(mFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();
            // Write end boundary
            bos.write(("--" + "BOUNDARY" + "\r\n").getBytes());
            bos.write(("Content-Disposition: form-data; name=\"" + "file" +
                    "\"; filename=\"" + mFileName + "\"" + "\r\n").getBytes());
            bos.write(("Content-Type: " + "image/jpeg" + "\r\n").getBytes());
            bos.write(("Content-Transfer-Encoding: binary" + "\r\n").getBytes());
            bos.write("\r\n".getBytes());
            // Convert ByteArrayOutputStream to byte array
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        // Parse network response here if needed
        return Response.success(new String(response.data), getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
