package fun.eriri.wordroid.activitys;


import java.util.ArrayList;

import fun.eriri.wordroid.business.OperationOfBooks;
import fun.eriri.wordroid.database.DataAccess;
import fun.eriri.wordroid.model.BookList;
import fun.eriri.wordroid.model.WordList;
import wordroid.model.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.*;

import androidx.core.content.ContextCompat;


public class Main extends Activity implements OnClickListener{
	public  static String SETTING_BOOKID="bookID";
	public  static String BOOKNAME = "BOOKNAME";
	private Spinner pickBook;
	private TextView info;
	private ImageButton deleteBu;
	private ImageView resetBu;
	private Button learnBu;
	private Button reviewBu;
	private Button testBu;
	private Button attentionBu;
	private ProgressBar learn_progress;
	private ProgressBar review_progress;
	private TextView learn_text;
	private TextView review_text;
	public static final int MENU_SETTING = 1;
	public static final int MENU_ABOUT = MENU_SETTING+1;
	public static final int MENU_CONTACT = MENU_SETTING+2;
	View myView;
	private Typeface typeface;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.setTitle("安卓背单词--Wordroid");
		super.onCreate(savedInstanceState);
		typeface = Typeface.createFromAsset(getAssets(),"fonts/1.TTF");
		LayoutInflater mInflater = LayoutInflater.from(this);
		myView = mInflater.inflate(R.layout.main, null);
		this.setContentView(myView);


//		这里启动主程序
//		Thread thread = new Thread(){
//			public void run(){
//				//					Thread.sleep(2000);
//				Message m = new Message();
//				m.what=1;
//				Main.this.mHandler.sendMessage(m);
//			}
//		};
//		thread.start();



		OperationOfBooks OOB = new OperationOfBooks();
		SharedPreferences setting = getSharedPreferences("wordroid.model_preferences", MODE_PRIVATE);
		String time = setting.getString("time", "18:00 下午");
		OOB.setNotify(time,Main.this);
//		File dir = new File(getExternalFilesDir(null)+"/databases");
//
//		if (!dir.exists())
//			dir.mkdir();
//		if (!(new File(getExternalFilesDir(null)+SqlHelper.DB_NAME)).exists()) {
//
//
//			FileOutputStream fos;
//			try {
//				fos = new FileOutputStream(getExternalFilesDir(null)+SqlHelper.DB_NAME);
//
//				byte[] buffer = new byte[8192];
//				int count = 0;
//				//这里是一个数据库
//				InputStream is = getResources().openRawResource(
//						R.raw.wordorid);
//				while ((count = is.read(buffer)) > 0) {
//					fos.write(buffer, 0, count);
//				}
//
//				fos.close();
//				is.close();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		SharedPreferences settings=getSharedPreferences(SETTING_BOOKID, 0);
		DataAccess.bookID=settings.getString(BOOKNAME, "");
		//这里是修改已经学习过的单词
		OOB.UpdateListInfo(Main.this);
		initWidgets();
//		new MyAysncTask(getApplication()).execute();
	}

	private void initSpinner(final boolean isRsume) {
		DataAccess data = new DataAccess(this);
		final ArrayList<BookList> bookList = data.QueryBook(null, null);
		//Log.i("size", String.valueOf(bookList.size()));
		String[] books = new String[bookList.size()+1];
		int i=0;
		for (;i<bookList.size();i++){
			books[i]=bookList.get(i).getName();
		}
		books[i]="导入词库";
		ArrayAdapter< CharSequence > adapter = new ArrayAdapter< CharSequence >(this, R.layout.spiner, books);

			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		pickBook.setAdapter(adapter);

		pickBook.setOnItemSelectedListener(new OnItemSelectedListener(){

				//修改书库的时候 界面的改变

				public void onItemSelected(AdapterView<?> arg0, View arg1,
										   int arg2, long arg3) {
					if (arg2<bookList.size()){
							DataAccess.bookID=bookList.get(arg2).getID();
						info.setText("\n词库名称:\n    "+bookList.get(arg2).getName()+"\n总词汇量:\n    "+bookList.get(arg2).getNumOfWord());
						info.setTypeface(typeface);
						deleteBu.setEnabled(true);
						learnBu.setEnabled(true);
						resetBu.setEnabled(true);
						reviewBu.setEnabled(true);
						testBu.setEnabled(true);
						DataAccess data2 = new DataAccess(Main.this);
						ArrayList<WordList> lists = data2.QueryList("BOOKID ='"+DataAccess.bookID+"'", null);
						learn_progress.setMax(lists.size());
						review_progress.setMax(lists.size());
						//初始化
						learn_progress.setProgress(0);
						review_progress.setProgress(0);
						review_progress.setSecondaryProgress(0);
						int learned=0,reviewed=0;
						//			修改进度 已经学习的进度
						for (int k=0;k<lists.size();k++){
							if (lists.get(k).getLearned().equals("1")){
								learn_progress.incrementProgressBy(1);
								learned++;
							}
							//  修改已经复习的进度
							if (Integer.parseInt(lists.get(k).getReview_times())>=5){
								review_progress.incrementProgressBy(1);
								reviewed++;
							}

							if (Integer.parseInt(lists.get(k).getReview_times())>0)
								review_progress.incrementSecondaryProgressBy(1);
							review_text.setText("已复习"+reviewed+"/"+lists.size());
							review_text.setTypeface(typeface);
							learn_text.setText("已学习"+learned+"/"+lists.size());
							learn_text.setTypeface(typeface);
						}
					}
					else if(arg2==bookList.size()){
						Intent intent = new Intent();
						intent.setClass(Main.this, ImportBook.class);
						startActivity(intent);
					}
				}


				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub

				}

			});

		if (isRsume&&DataAccess.bookID!=null&&!DataAccess.bookID.equals("")){
			for (int x=0;x<bookList.size();x++){
				if(bookList.get(x).getID().equals(DataAccess.bookID)){
					pickBook.setSelection(x);
				}
			}
		}

		//如果没有词库 按钮失效
		if (bookList.size()==0) {
			pickBook.setSelection(1);
			info.setText("请先从上方选择一个词库！");
			this.deleteBu.setEnabled(false);
			this.learnBu.setEnabled(false);
			this.resetBu.setEnabled(false);
			this.reviewBu.setEnabled(false);
			this.testBu.setEnabled(false);
			this.learn_progress.setProgress(0);
			this.review_progress.setProgress(0);
			return;
		}
		int j=0;
		Log.i("BookID", DataAccess.bookID);
		//显示已选中的
//		for (;j<bookList.size();j++){
//			if (DataAccess.bookID.equals(bookList.get(j).getID())){
//				pickBook.setSelection(j);
//				break;
//			}
//
//		}
	}

	//初始化数据
	private void initWidgets() {
		// TODO Auto-generated method stub
		this.deleteBu=(ImageButton) myView.findViewById(R.id.delete);
		deleteBu.setOnClickListener(this);
		this.info=(TextView) myView.findViewById(R.id.bookinfo);
		this.learnBu=(Button) myView.findViewById(R.id.learn);
		learnBu.setOnClickListener(this);
		this.pickBook=(Spinner) myView.findViewById(R.id.pickBook);
		this.resetBu=(ImageView) myView.findViewById(R.id.reset);
		resetBu.setOnClickListener(this);
		this.reviewBu=(Button) myView.findViewById(R.id.review);
		reviewBu.setOnClickListener(this);
		this.testBu=(Button) myView.findViewById(R.id.test);
		testBu.setOnClickListener(this);
		this.attentionBu=(Button) myView.findViewById(R.id.attention);
		attentionBu.setOnClickListener(this);
		this.learn_progress= (ProgressBar) myView.findViewById(R.id.learn_progress);
		this.review_progress= (ProgressBar) myView.findViewById(R.id.review_progress);
		this.review_text=(TextView) myView.findViewById(R.id.review_text);
		this.learn_text=(TextView) myView.findViewById(R.id.learn_text);
		//这里是修改导航栏的布局大小
		DisplayMetrics dm = new DisplayMetrics();
		dm = getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int padding = (screenWidth-200);
		this.learnBu.setPadding(padding/5, 0, padding/10, 0);
		this.resetBu.setPadding(padding/10, 0, padding/10, 0);
		this.testBu.setPadding(padding/10, 0, padding/10, 0);
		this.attentionBu.setPadding(padding/10, 0, padding/5, 0);
		//初始化下滑栏
		initSpinner(false);
//        new MyAysncTask(this.getApplication()).execute();
	}

	protected void onDestroy() {
		// TODO Auto-generated method stub
		SharedPreferences settings=getSharedPreferences(SETTING_BOOKID, 0);
		settings.edit()
				.putString(BOOKNAME, DataAccess.bookID)
				.apply();
		Log.e("setting", "onDestroy: "+ settings.getString(BOOKNAME,"123"));
		super.onDestroy();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		//复习
		if (v==reviewBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, ReviewMain.class);
			this.startActivity(intent);
		}
		//考试
		if (v==testBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, TestList.class);
			this.startActivity(intent);
		}
		if (v==deleteBu){
			Dialog dialog = new AlertDialog.Builder(this)
					.setIcon(R.drawable.dialog_icon)
					.setTitle("删除当前词库")
					.setMessage("确定要将这个词库删除吗？")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							DataAccess data = new DataAccess(Main.this);
							data.DeleteBook();
							DataAccess.bookID="";
							Toast.makeText(Main.this, "该词库已删除", Toast.LENGTH_SHORT).show();
							initSpinner(false);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).create();
			dialog.show();
			dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(Main.this,R.drawable.white_btn));
		}
		if (v==this.resetBu){
			Dialog dialog = new AlertDialog.Builder(this)
					.setIcon(R.drawable.dialog_icon)
					.setTitle("重置当前词库")
					.setMessage("确定要将这个词库重置吗？它将失去所有学习记录")
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							DataAccess data = new DataAccess(Main.this);
							data.ResetBook();
							Toast.makeText(Main.this, "该词库已被重置", Toast.LENGTH_SHORT).show();
							initSpinner(false);
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					}).create();
			dialog.show();
			dialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(Main.this,R.drawable.white_btn));
		}
		if (v==this.attentionBu){
			//生词本换成我的
			Intent intent = new Intent();
			intent.setClass(Main.this, MeActibity.class);
			startActivity(intent);
		}
		//学习本
		if (v==learnBu){
			Intent intent = new Intent();
			intent.setClass(Main.this, study.class);
			this.startActivity(intent);
		}

	}

//onresume在onstart之后暂停之后恢复还可以刷新
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.i("In Resume", DataAccess.bookID);
		this.initSpinner(true);
		super.onResume();
	}


	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		menu.add(0, MENU_SETTING, 0, "设置");
		menu.add(0, MENU_ABOUT, 1, "说明");
		menu.add(0, MENU_CONTACT, 2, "关于");
		return super.onCreateOptionsMenu(menu);
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
			case MENU_SETTING:{
				Intent intent = new Intent();
				intent.setClass(this, Preference.class);
				startActivity(intent);
				break;
			}
			case MENU_ABOUT:{
				Intent intent = new Intent();
				intent.setClass(this, Help.class);
				startActivity(intent);
				break;
			}
			case MENU_CONTACT:{
				Intent intent = new Intent();
				intent.setClass(this, about.class);
				startActivity(intent);
				break;
			}
		}
		return super.onOptionsItemSelected(item);
	}

//	private Handler mHandler = new Handler(){
//		public void handleMessage(Message msg) {
//			if (msg.what==1)
//				setContentView(myView);
//		}
//	};
}

