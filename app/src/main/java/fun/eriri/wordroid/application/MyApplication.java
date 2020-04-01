package fun.eriri.wordroid.application;

import android.app.Application;
import android.content.IntentFilter;
import android.util.Log;

import fun.eriri.wordroid.business.makeNotify;
import fun.eriri.wordroid.util.MyAysncTask;

public class MyApplication extends Application {
    private String username = "";

    public void setUsername(String username){
        this.username = username;
    }
    public String getUsername(){
        return username;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        makeNotify makeNotify = new makeNotify();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("shownotify");
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(makeNotify,intentFilter);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.e("username", "onTrimMemory: "+username );
        MyAysncTask myAysncTask = new MyAysncTask(this);
        myAysncTask.execute();
    }
}
