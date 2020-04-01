package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import fun.eriri.wordroid.application.MyApplication;
import fun.eriri.wordroid.database.SqlHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import wordroid.model.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button login;
    private TextView sign,forget;
    private EditText username,password;
    private MyApplication application;

    AlertDialog show;

    public static final int intentCode = 1001;
    public static final int GET_DATABASECODE = 1002;
    public static final int LOGIN_ERROR = 1003;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private final OkHttpClient okHttpClient = new OkHttpClient();
    private mHandler mHandler =new mHandler(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_new);
        login =findViewById(R.id.loginbtn);
        sign =  findViewById(R.id.signinbtn);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forget = findViewById(R.id.forget);

        login.setOnClickListener(this);
        sign.setOnClickListener(this);
        forget.setOnClickListener(this);
        application = (MyApplication) this.getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginbtn:
                if (username.getText().toString().equals("")||password.getText().toString().equals("")){
                    Toast.makeText(this,"不能为空！",Toast.LENGTH_LONG).show();
                }else{
                    new Thread(){
                        @Override
                        public void run() {
                            init();
                            super.run();
                        }
                    }.start();
                }
                //这里去服务器查询账号和密码，现在先写死
                break;
            case R.id.signinbtn:
                Intent intent = new Intent(LoginActivity.this,SignInActivity.class);
                startActivityForResult(intent,1);
                break;
            case R.id.forget:
                Intent intent1 = new Intent(this,ForgetPasswordActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1&&resultCode==2){
            final Message message = Message.obtain();
            message.what = LoginActivity.intentCode;
            username.setText(data.getStringExtra("username"));
            password.setText(data.getStringExtra("password"));
            Log.e("11111", "onActivityResult: "+"to" );
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try {
                        Message message1 = Message.obtain();
                        message1.what = LoginActivity.GET_DATABASECODE;
                        mHandler.sendMessage(message1);
                        initDataBaseForNewUser();
                        mHandler.sendMessage(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }


    public void init(){
        String usernameStr = username.getText().toString();
        Map<String,String> map = new HashMap<>();
        map.put("username",usernameStr);
        final Gson gson = new Gson();
        String data = gson.toJson(map);
        RequestBody requestBody = RequestBody.create(JSON,data);
        final Request request = new Request.Builder()
                .post(requestBody)
                .url("https://www.eriri.fun/getOneUser.do")
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("internet", "onFailure: 请求失败" +e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                if (str.equals("")){
                    Message message = Message.obtain();
                    message.what = LOGIN_ERROR;
                    mHandler.sendMessage(message);
                    return;
                }
                Map rMap = gson.fromJson(str, Map.class);
                Message message = Message.obtain();
                if (((String)rMap.get("password")).equals(password.getText().toString())){
                    //获取databas;
                    message.what = LoginActivity.GET_DATABASECODE;
                    mHandler.sendMessage(message);
                    //阻塞式完成对后端的请求
                    String path = (String) rMap.get("database_user");
                    Log.e("path", "onResponse: "+path );
                    Request getdatabase = new Request.Builder()
                            .url("https://www.eriri.fun/file/"+path)
                            .build();
                    Response execute = okHttpClient.newCall(getdatabase).execute();
                    int length = 0;
                    byte[] bytes = new byte[1024];
//                    execute.header("Content-Length");  获取词典大小，将来开可以优化性能
                    ResponseBody responseBody = execute.body();
                    Log.e("222", "onResponse: "+execute.header("Content-Length") );
                    InputStream inputStream = responseBody.byteStream();
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    File file = new File(getExternalFilesDir(null) + "/databases/");
                    if (!file.exists()){
                        file.mkdirs();
                    }
                    FileOutputStream fileOutputStream = new FileOutputStream(new File(getExternalFilesDir(null) + "/databases/wordroid.db"));
                    while ((length=inputStream.read(bytes))!=-1){
                        fileOutputStream.write(bytes,0,length);
                    }
//                    byte[] b = byteArrayOutputStream.toByteArray();

//                    Source source = Okio.source(inputStream);
//                    BufferedSource buffer = Okio.buffer(source);
//                    Sink sink = Okio.sink(new File(getExternalFilesDir(null) + SqlHelper.DB_NAME));
//                    buffer.readAll(sink);
                    Message message1 = Message.obtain();
                    message1.what = LoginActivity.intentCode;
                    mHandler.sendMessage(message1);

                }else{
                    message.what = LoginActivity.LOGIN_ERROR;
                }
            }
        });
    }

    public void getDataBase(){

    }

    public static class mHandler extends Handler{
        WeakReference<LoginActivity> loginActivityWeakReference ;
        public  mHandler(LoginActivity loginActivity){
            loginActivityWeakReference = new WeakReference<>(loginActivity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LoginActivity.LOGIN_ERROR:
                    Toast.makeText(loginActivityWeakReference.get(),"密码或账号错误!!!",Toast.LENGTH_LONG).show();
                    break;
                case LoginActivity.intentCode:
                    if (loginActivityWeakReference.get().show !=null){
                        loginActivityWeakReference.get().show.cancel();
                    }
                    Intent intent = new Intent(loginActivityWeakReference.get(),Main.class);
                    loginActivityWeakReference.get().startActivity(intent);
                    loginActivityWeakReference.get().application.setUsername(loginActivityWeakReference.get().username.getText().toString());
                    loginActivityWeakReference.get().finish();
                    break;
                case LoginActivity.GET_DATABASECODE:
                     loginActivityWeakReference.get().show = new ProgressDialog.Builder(loginActivityWeakReference.get())
                            .setTitle("等待")
                            .setMessage("正在同步用户信息")
                            .setCancelable(false)
                            .show();
            }
        }
    }
    void initDataBaseForNewUser() throws IOException {
        String path = "wordroid.db";
        Request getdatabase = new Request.Builder()
                .url("https://www.eriri.fun/file/"+path)
                .build();
        Response execute = okHttpClient.newCall(getdatabase).execute();
        int length = 0;
        byte[] bytes = new byte[1024];
//                    execute.header("Content-Length");  获取词典大小，将来开可以优化性能
        ResponseBody responseBody = execute.body();
        Log.e("222", "onResponse: "+execute.header("Content-Length") );
        InputStream inputStream = responseBody.byteStream();
//                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        File file = new File(getExternalFilesDir(null) + "/databases/");
        if (!file.exists()){
            file.mkdirs();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(getExternalFilesDir(null) + "/databases/wordroid.db"));
        while ((length=inputStream.read(bytes))!=-1){
            fileOutputStream.write(bytes,0,length);
        }
        fileOutputStream.close();
        execute.close();

    }
}
