package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;


import fun.eriri.wordroid.bean.User;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import wordroid.model.R;

public class ForgetPasswordActivity extends Activity {
    private EditText username,password,question,answer;
    private Button btn;
    private ForgetAsynctask asynctask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);
        btn = findViewById(R.id.loginbtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(username.getText().toString().trim().equals("")||answer.getText().toString().trim().equals(""))){
                    click();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    void click(){
        asynctask = new ForgetAsynctask(username.getText().toString(),answer.getText().toString(),password.getText().toString(),this);
        asynctask.execute();
    }

    public static class ForgetAsynctask extends AsyncTask<String,Void,String>{
        private OkHttpClient okHttpClient = new OkHttpClient();
        private String text,answer,password;
        WeakReference<ForgetPasswordActivity> weakReference;
        public ForgetAsynctask(String text,String answer,String password,ForgetPasswordActivity forgetPasswordActivity){
            this.text = text;
            this.answer =answer;
            this.password = password;
            weakReference = new WeakReference<>(forgetPasswordActivity);
        }
        @Override
        protected String doInBackground(String[] objects) {
            Gson gson = new Gson();
            Map<String,String> usermap = new HashMap<>();
            usermap.put("username",text);
            RequestBody requestBody = RequestBody.create(LoginActivity.JSON,gson.toJson(usermap));
            final String string;
            Request request = new Request.Builder()
                    .post(requestBody)
                    .url("https://www.eriri.fun/getOneUser.do")
                    .build();
            try {
                Response execute = okHttpClient.newCall(request).execute();
                String string1 = execute.body().string();
                Map map = gson.fromJson(string1, Map.class);
                String answerStr = (String) map.get("answer");
                if (answerStr.equals(answer)){
                    User user = new User();
                    user.setUsername(text);
                    user.setPassword(password);
                    RequestBody requestBody1 = RequestBody.create(LoginActivity.JSON, gson.toJson(user));
                    Log.e("name", "doInBackground: "+gson.toJson(user) );
                    Request request1 = new Request.Builder()
                            .post(requestBody1)
                            .url("https://www.eriri.fun/updateUser.do")
                            .build();
                    Response execute1 = okHttpClient.newCall(request1).execute();
                    Map map1 = gson.fromJson(execute1.body().string(), Map.class);
                    if (map1.get("info").equals("success")){
                        return "success";
                    }
                }
                return (String) map.get("answer");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("success")){
                Toast.makeText(weakReference.get(),"修改成功",Toast.LENGTH_LONG).show();
                weakReference.get().finish();
            }
        }
    }
}
