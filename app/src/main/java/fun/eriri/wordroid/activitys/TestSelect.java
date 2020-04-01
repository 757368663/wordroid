package fun.eriri.wordroid.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import wordroid.model.R;

public class TestSelect extends Activity {
    private CardView cardView;
    private RadioGroup radioGroup;
    private String position;
//    private RadioButton radioButton1;
//    private RadioButton radioButton2;
//    private RadioButton radioButton3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTitle("");
        setContentView(R.layout.test_select);

        radioGroup = (RadioGroup) findViewById(R.id.radio);
        position = getIntent().getStringExtra("wordList");

//        radioButton1 = (RadioButton) findViewById(R.id.tiankong);
//        radioButton2 = (RadioButton) findViewById(R.id.EtoC);
//        radioButton3 =(RadioButton) findViewById(R.id.CtoE);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.tiankong:
                        Intent intent = new Intent(TestSelect.this,TestTian.class);
                        intent.putExtra("wordList",position);
                        TestSelect.this.startActivity(intent);
                        break;
                    case R.id.EtoC:
                        Intent intent1 = new Intent(TestSelect.this,Test.class);
                        intent1.putExtra("wordList",position);
                        TestSelect.this.startActivity(intent1);
                        break;
                    case R.id.CtoE:
                        Intent intent2 = new Intent(TestSelect.this,TestCtoE.class);
                        intent2.putExtra("wordList",position);
                        TestSelect.this.startActivity(intent2);
                        break;
                }
            }
        });
    }
}
