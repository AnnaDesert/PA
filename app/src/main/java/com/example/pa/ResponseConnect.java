package com.example.pa;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URLEncoder;
import java.util.Objects;

import okhttp3.FormBody;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String connMessPage(CookieManager cookieManager, String URL, String in_out, int page){

        OkHttpClient client_mess_page = new OkHttpClient().newBuilder()
                .cookieJar(new JavaNetCookieJar(cookieManager))
                .followRedirects(false)
                .followSslRedirects(false)
                .build();

        RequestBody pagination = new FormBody.Builder()
                .add("type", in_out)
                .add("page", String.valueOf(page))
                .build();

        Request request_person = new Request.Builder()
                .url(URL)
                .post(pagination)
                .build();

        String responseString1 = null;
        try {
            Response response_person = client_mess_page.newCall(request_person).execute();

            //Log.i("PAGE", String.valueOf(response_person.toString()));
            byte[] responseBytes = Objects.requireNonNull(response_person.body()).bytes();
            responseString1 = new String(responseBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.i("PAGE", responseString1);
        return responseString1;
    }
}
