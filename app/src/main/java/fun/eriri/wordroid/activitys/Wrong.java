package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import fun.eriri.wordroid.adapter.WrongWordListViewAdapter;
import fun.eriri.wordroid.database.WrongDataUtil;
import fun.eriri.wordroid.model.Word;
import wordroid.model.R;

public class Wrong extends Activity {
    private ListView listView;
    private final List<Word> wrongWord= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrong);
        WrongDataUtil.selectAllWord(this,wrongWord);

        listView = findViewById(R.id.wrong_list);
        final WrongWordListViewAdapter wrongWordListViewAdapter = new WrongWordListViewAdapter(this,wrongWord);
        listView.setAdapter(wrongWordListViewAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               TextView textView =  (TextView)view.findViewById(R.id.spelling);
               final String spelling =  textView.getText().toString();
                AlertDialog alertDialog = new AlertDialog.Builder(Wrong.this)
                        .setIcon(R.drawable.ic_edit_black_24dp)
                        .setTitle("删除")
                        .setMessage("是否删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                WrongDataUtil.deleteWord(Wrong.this,spelling);
                                Toast.makeText(Wrong.this,"已删除",Toast.LENGTH_LONG).show();
                                wrongWord.remove(position);
                                wrongWordListViewAdapter.setData();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                       .create();
                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(Wrong.this,R.drawable.white_btn));
            }
        });
    }

}
