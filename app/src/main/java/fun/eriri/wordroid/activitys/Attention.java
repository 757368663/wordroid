package fun.eriri.wordroid.activitys;

import java.util.ArrayList;

import fun.eriri.wordroid.database.DataAccess;
import fun.eriri.wordroid.model.Word;
import wordroid.model.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.core.content.ContextCompat;


//生词本页面
public class Attention extends ListActivity {
	private ArrayList<Word> list;
	public static final int MENU_ADD = 1;

	 
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setTitle("生词本");
		this.setContentView(R.layout.attention);
		setAdapter();
	}

	 
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_ADD, 0, "添加新单词");
		return true;
	}

	 //右上角的设置按钮 添加新单词
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case MENU_ADD:{
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("action", "add");
			intent.putExtras(bundle);
			intent.setClass(Attention.this, EditWord.class);
			startActivity(intent);
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}

	 //item点击事件
	protected void onListItemClick(ListView l, View v, final int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Dialog dialog = new AlertDialog.Builder(Attention.this)
        .setIcon(R.drawable.ic_edit_black_24dp)
        .setTitle("操作")
        .setItems(new String[]{"编辑该单词","从生词本中删除"}, new DialogInterface.OnClickListener(){

			 
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
					//修改单词信息
				case 0:{
					Intent intent = new Intent();
					intent.setClass(Attention.this, EditWord.class);
					Bundle bundle = new Bundle();
					bundle.putString("action", "edit");
					bundle.putString("id", list.get(position).getID());
					intent.putExtras(bundle);
					startActivity(intent);
					break;
				}
				case 1:{
					DataAccess data = new DataAccess(Attention.this);
					data.DeleteFromAttention(list.get(position));
					setAdapter();
					break;
				}
				}
				
			}
        	
        })
        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			 
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		})
        .create();
		
		dialog.show();
		dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(Attention.this,R.drawable.white_btn));
	}

	protected void setAdapter() {
		// TODO Auto-generated method stub
		DataAccess data = new DataAccess(this);
		list=data.QueryAttention(null, null);
		ArrayList<String> showlist = new ArrayList<String>();
		for (int i=0;i<list.size();i++){
			showlist.add((i+1)+"."+list.get(i).getSpelling()+"\n"+list.get(i).getMeanning());
		}
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.file_row, showlist));
	}

	 
	protected void onResume() {
		// TODO Auto-generated method stub
		this.setAdapter();
		super.onResume();
	}
	

}
