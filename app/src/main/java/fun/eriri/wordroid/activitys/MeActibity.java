package fun.eriri.wordroid.activitys;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fun.eriri.wordroid.adapter.MyListViewAdapter;
import fun.eriri.wordroid.entry.Entry;
import wordroid.model.R;

public class MeActibity extends Activity {

    private ListView listView;
    private final List<Entry> entries = new ArrayList<>();

    private int[] ico = {R.mipmap.attention,R.mipmap.wrong,R.mipmap.list_word};
    private String[] message ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.me);
        listView = findViewById(R.id.listview);
        init();
        listView.setAdapter(new MyListViewAdapter(this,entries));


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intent = new Intent(MeActibity.this,Attention.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1 = new Intent(MeActibity.this,Wrong.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(MeActibity.this,Dictionary.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }




    private void init() {
        message = this.getResources().getStringArray(R.array.message);
        for (int i=0;i<message.length;i++){
            Entry entry = new Entry(ico[i],message[i]);
            entries.add(entry);
        }
    }

}
