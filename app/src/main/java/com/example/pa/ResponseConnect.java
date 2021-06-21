package com.example.pa;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.internal.JavaNetCookieJar;

public class ResponseConnect {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String conn(CookieManager cookieManager, String URL){

        OkHttpClient client_person = new OkHttpClient().newBuilder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        Request request_person = new Request.Builder()
                .url(URL)
                .build();

        String responseString1 = null;
        try {
            Response response_person = client_person.newCall(request_person).execute();

            Log.i("TAG", String.valueOf(response_person.code()));
            byte[] responseBytes = Objects.requireNonNull(response_person.body()).bytes();
            responseString1 = new String(responseBytes, "windows-1251");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString1;
    }
///////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void PostMess(CookieManager cookieManager, String adres, String text){
        String textWin = null;
        try {
            textWin = new String(text.getBytes("UTF-8"), "windows-1251");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        RequestBody form = null;
        form = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("sender", adres)
                .addFormDataPart("message", textWin)
                .addFormDataPart("message_file_name", "")
                .addFormDataPart("MAX_FILE_SIZE", "1048576000")
                .addPart(Headers.of("Content-Disposition", "form-data; name=\"message_file\"; filename=\"\""),
                        RequestBody.create(MediaType.parse("application/octet-stream"), new byte[0]))
                .addFormDataPart("submitMessage", "")
                .build();

        Request request = new Request.Builder()
                .url("http://oreluniver.ru/chat")
                .post(form)
                .build();
        Call call = client.newCall(request);


        Response response = null;
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("Code Mess", String.valueOf(response.code()));
    }
}
