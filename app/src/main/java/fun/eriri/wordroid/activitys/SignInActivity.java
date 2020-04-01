package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Map;

import fun.eriri.wordroid.application.MyApplication;
import fun.eriri.wordroid.bean.User;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wordroid.model.R;

public class SignInActivity extends Activity {
    private Button signup;
    private EditText username,password,checkpassword,phoneNumber,email,school,question,answer;
    public static final int INSERT_SUCCESS_CODE = 1004;
    public static final int INSERT_WRONG_CODE = 1005;

    private SHandler sHandler = new SHandler(this);
    private OkHttpClient okHttpClient = new OkHttpClient();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_new);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        checkpassword = findViewById(R.id.surepassword);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        school = findViewById(R.id.school);
        question = findViewById(R.id.question);
        answer = findViewById(R.id.answer);

        signup = findViewById(R.id.signupbtn);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.signupbtn:
                        String p1 = checkpassword.getText().toString();
                        String p2 = password.getText().toString();
                        String phoneNumberstr  = phoneNumber.getText().toString();
                        String usernamestr = username.getText().toString();
                        String emailstr = email.getText().toString();
                        if (p1.equals("")||p2.equals("")||phoneNumberstr.equals("")||usernamestr.equals("")||emailstr.equals("")){
                            Toast.makeText(SignInActivity.this,"不能输入空数值!",Toast.LENGTH_LONG).show();
                        }else if(!p1.equals(p2)){
                            Toast.makeText(SignInActivity.this,"输入的密码不一致！!",Toast.LENGTH_LONG).show();
                        }else{
                            new Thread(){
                                @Override
                                public void run() {
                                    signIn();
                                    super.run();
                                }
                            }.start();
                        }
                        break;
                }
            }
        });
    }
    public void signIn(){
        String usernameStr = username.getText().toString();
        String passwordStr = password.getText().toString();
        String phoneNumberStr = phoneNumber.getText().toString();
        String emailStr = email.getText().toString();
        String schoolStr = school.getText().toString();
        String questionStr = question.getText().toString();
        String answerStr = answer.getText().toString();
        String id = (Integer.parseInt(getLastUserId())+1)+"";
        String database_user = "wordroid_"+usernameStr+".db";
        User user = new User(id,usernameStr,passwordStr,phoneNumberStr,emailStr,questionStr,answerStr,null,database_user,schoolStr);
        final Gson gson = new Gson();
        String data = gson.toJson(user);
        Log.e("what", "signIn: "+data );
        RequestBody body = RequestBody.create(LoginActivity.JSON,data);
        Request request = new Request.Builder()
                .post(body)
                .url("https://www.eriri.fun/insertUser.do")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("f", "onFailure: 插入用户失败" );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//                Log.e("tag", "onResponse: "+response.body().string());
                Map map = gson.fromJson(response.body().string(), Map.class);
                if (((String)map.get("info")).equals("success")){
                    Message message = Message.obtain();
                    message.what = SignInActivity.INSERT_SUCCESS_CODE;
                    sHandler.sendMessage(message);
                }else{
                    Message message = Message.obtain();
                    message.what = SignInActivity.INSERT_WRONG_CODE;
                    sHandler.sendMessage(message);
                }
            }
        });
    }
    public String getLastUserId(){
        Request request = new Request.Builder()
                .url("https://www.eriri.fun/getLastUser.do")
                .build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            Gson gson = new Gson();
            String id = (String)gson.fromJson(execute.body().string(), Map.class).get("id");
            Log.e("TheLastId", "getLastUserId: "+id );
            return id;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static class SHandler extends Handler {
        WeakReference<SignInActivity> weakReference;
        public SHandler(SignInActivity signInActivity){
            weakReference = new WeakReference<>(signInActivity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == SignInActivity.INSERT_SUCCESS_CODE){
                Intent i = weakReference.get().getIntent();
                i.putExtra("username",weakReference.get().username.getText().toString());
                i.putExtra("password",weakReference.get().password.getText().toString());
                weakReference.get().setResult(2,i);
                weakReference.get().finish();
            }
            if (msg.what == SignInActivity.INSERT_WRONG_CODE){
                Toast.makeText(weakReference.get(),"服务器出错，请联系管理员",Toast.LENGTH_LONG).show();
            }
        }
    }
}
