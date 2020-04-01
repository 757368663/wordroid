package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import fun.eriri.wordroid.database.DataAccess;
import fun.eriri.wordroid.database.WrongDataUtil;
import fun.eriri.wordroid.model.Word;
import fun.eriri.wordroid.model.WordList;
import wordroid.model.R;

public class TestTian extends Activity implements View.OnClickListener {
    private TextView meanning;
    private EditText spelling;
    private Button next,end,into,sure;
    Toast toast;
    private ArrayList<Word> list;
    int wordListInt;
    DataAccess dataAccess;
    int count = 0;  //当前答了多少题
    int right = 0; //对了多少题

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_tian);
        meanning = (TextView) findViewById(R.id.Meanning);
        spelling = (EditText) findViewById(R.id.spelling);

        Intent intent = getIntent();
        String value = intent.getStringExtra("wordList");
        wordListInt = Integer.parseInt(value) + 1;
        this.setTitle("测试LIST-"+wordListInt);
        dataAccess = new DataAccess(this);
        //当前单元的list
        list = dataAccess.QueryWord("LIST='" + wordListInt + "'", null);

        next = (Button) findViewById(R.id.NextButton);
        end = (Button) findViewById(R.id.OverButton);
        into = (Button) findViewById(R.id.test_addintoattention);
        sure = (Button) findViewById(R.id.sure);
//        end.setVisibility(View.GONE);
//        next.setVisibility(View.GONE);
//        into.setVisibility(View.GONE);

        meanning.setText(list.get(count).getMeanning());


        next.setOnClickListener(this);
        end.setOnClickListener(this);
        into.setOnClickListener(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.NextButton:
//                end.setVisibility(View.GONE);
//                next.setVisibility(View.GONE);
//                into.setVisibility(View.GONE);
                sure.setEnabled(true);
                setNextQue(count);
                break;
            case R.id.OverButton:
                endQue();
                break;
            case R.id.test_addintoattention:
                intoForget();
                break;
            case R.id.sure:
                //回答正确自动下一个，不正确返回正确值然后可以手动点下一个
                if (spelling.getText().toString().trim().equals("")){
                    DisplayToast("不要填空哦~");
                    return;
                }
                if (spelling.getText().toString().trim().equals(list.get(count).getSpelling())) {
                    right++;
                    count++;
                    DisplayToast("回答正确");
                    setNextQue(count);
                }else{
                    //这里可以写个dialog来提示
                    DisplayToast("回答错误 正确答案是"+list.get(count).getSpelling());
                    Word word = list.get(count);
                    WrongDataUtil.insertWrong(TestTian.this,word);
                    count++;
                    sure.setEnabled(false);
                    next.setVisibility(View.VISIBLE);
                    into.setVisibility(View.VISIBLE);
                    end.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
    public void setNextQue(int position){
        spelling.setText("");
        if (position<list.size()){
            meanning.setText(list.get(position).getMeanning());
        }else{
            endQue();
        }
    }
    public void endQue(){
        ArrayList<WordList> wordLists = dataAccess.QueryList("BOOKID = '"+DataAccess.bookID+"'and LIST='" + wordListInt + "'",null);
        WordList wordList = wordLists.get(0);
        String bestScore = wordList.getBestScore();

        System.out.println("bestScore" + bestScore);

        if (bestScore.equals("")) {
            wordList.setBestScore(""+(right*100/list.size()));
        } else {
            int bestScoreInt = Integer.parseInt(bestScore);
            if (bestScoreInt < (right*100/list.size())) {
                wordList.setBestScore(""+(right*100/list.size()));
            }
        }
        dataAccess.UpdateList(wordList);

        Dialog dlg = new AlertDialog.Builder(TestTian.this)
                .setTitle("测试结果")
                .setMessage("共" + list.size() + "题，做对" + right + "题， 正确率" + (right*100/list.size()) + "%")
                .setPositiveButton("返回", new DialogInterface.OnClickListener() {


                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//						Intent intent = new	Intent();
//						intent.putExtra("testIntent", "123");
//			    		intent.setClass(Test.this, Main.class);
//			    		Test.this.startActivity(intent);
                        finish();
                    }
                }).create();

        dlg.show();

    }
    public void intoForget(){
        DataAccess data = new DataAccess(TestTian.this);
        ArrayList<Word> attention = new ArrayList<Word>();
        attention=data.QueryAttention("SPELLING ='"+list.get(count).getSpelling()+"'", null);
        if (attention.size()==0){
            data.InsertIntoAttention(list.get(count));
            Toast.makeText(TestTian.this, "已加入生词本", Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(TestTian.this, "生词本中已包含这个单词！", Toast.LENGTH_SHORT).show();
    }

    public void DisplayToast(String str) {
        toast = Toast.makeText(this, str, Toast.LENGTH_SHORT);
        toast.show();
    }
}
