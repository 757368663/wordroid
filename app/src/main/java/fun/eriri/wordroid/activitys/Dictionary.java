package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import fun.eriri.wordroid.adapter.DictionaryAdapter;
import fun.eriri.wordroid.bean.DictionaryBean;
import fun.eriri.wordroid.business.OperationOfBooks;
import fun.eriri.wordroid.database.DataAccess;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import wordroid.model.R;

public class Dictionary extends Activity {


    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dictionary);

        recyclerView = findViewById(R.id.dictionary);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));

        MyAsycnTask myAsycnTask = new MyAsycnTask(this);
        myAsycnTask.execute();
    }
    public static class MyAsycnTask extends AsyncTask<String,Void,List<DictionaryBean>>{

        WeakReference<Dictionary> weakReference;
        MyAsycnTask(Dictionary dictionary){
            weakReference = new WeakReference<>(dictionary);
        }
        private OkHttpClient okHttpClient = new OkHttpClient();
        @Override
        protected List<DictionaryBean> doInBackground(String... strings) {
            Request request = new Request.Builder()
                        .url("https://www.eriri.fun/getAllCidian.do")
                    .build();
            try {
                Response execute = okHttpClient.newCall(request).execute();
                String string = execute.body().string();
                Gson gson = new Gson();
               List<DictionaryBean> list = gson.fromJson(string, new TypeToken<List<DictionaryBean>>() {
                }.getType());
                final List<DictionaryBean> data = new ArrayList<>(list);
//                Log.e("tag11", "doInBackground: "+data.get(1) );
                return data;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<DictionaryBean> dictionaryBeans) {
            super.onPostExecute(dictionaryBeans);
            if (dictionaryBeans!=null){

                DictionaryAdapter adapter = new DictionaryAdapter(weakReference.get(),dictionaryBeans);
                weakReference.get().recyclerView.setAdapter(adapter);

                adapter.setListner(new DictionaryAdapter.dictionaryListenr() {
                    @Override
                    public void download(String url, String name) {
                        DictionaryTask task = new DictionaryTask(weakReference.get());
                        task.execute(url,name);
                    }

                });
            }else{
                Log.e("internetError", "onPostExecute: data是空的" );
            }
        }
    }
    public static class DictionaryTask extends AsyncTask<String,Void,Boolean>{

        WeakReference<Dictionary> weakReference;
        AlertDialog dialog;
        DictionaryTask(Dictionary dictionary){
            weakReference = new WeakReference<>(dictionary);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new AlertDialog.Builder(weakReference.get())
                    .setTitle("")
                    .setIcon(R.drawable.ic_edit_black_24dp)
                    .setMessage("正在同步")
                    .create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(weakReference.get(),R.drawable.white_btn));
        }

        @Override
        protected Boolean doInBackground(String... strings) {

            OkHttpClient okHttpClient = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();
            try {
                Response execute = okHttpClient.newCall(request).execute();
                BufferedSource source = execute.body().source();
                Sink sink = Okio.sink(new File(weakReference.get().getExternalFilesDir(null)+"/f.txt"));
                BufferedSink buffer = Okio.buffer(sink);
                source.readAll(buffer);

                OperationOfBooks operationOfBooks = new OperationOfBooks();
                DataAccess.ifContinue = true;
                operationOfBooks.ImportBook(weakReference.get(),"f.txt",strings[1],20,"luanxu");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            dialog.cancel();
        }
    }
}
