package fun.eriri.wordroid.util;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;

import fun.eriri.wordroid.activitys.Activity;
import fun.eriri.wordroid.application.MyApplication;
import fun.eriri.wordroid.database.SqlHelper;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public  class MyAysncTask extends AsyncTask {
    WeakReference<Application> weakReference;
    public MyAysncTask(Application context){
        weakReference = new WeakReference<>(context);
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //同步数据库文件到服务器
        try {
            MediaType mediaType = MediaType.parse("multipart/form-data");
            MediaType mediaType1 = MediaType.parse("text/x-markdown; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            Source source = Okio.source(new File(weakReference.get().getExternalFilesDir(null)+ SqlHelper.DB_NAME));
            BufferedSource bufferedSource = Okio.buffer(source);
            byte[] bytes = bufferedSource.readByteArray();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("file", "wordroid.db",
                            RequestBody.create(MediaType.parse("multipart/form-data"),bytes ))
                    .build();
            MyApplication myApplication = (MyApplication) weakReference.get();
            Request request = new Request.Builder()
                    .header("id",myApplication.getUsername())
                    .url("https://www.eriri.fun/add.do")
                    .post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            Log.e("string", "doInBackground: "+response.body().string() );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Log.e("asycn", "onPostExecute: success" );
    }
}
