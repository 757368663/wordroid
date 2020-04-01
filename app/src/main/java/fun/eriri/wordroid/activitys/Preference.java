package fun.eriri.wordroid.activitys;


import java.util.HashMap;

import fun.eriri.wordroid.business.OperationOfBooks;
import fun.eriri.wordroid.widget.timePreference;
import wordroid.model.R;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;

public class Preference extends PreferenceActivity implements OnPreferenceChangeListener {
	ListPreference listpre;
	timePreference timepre;
	OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preference);
		timepre = (timePreference) this.findPreference("time");
		timepre.setOnPreferenceChangeListener(this);
		SharedPreferences settings = getSharedPreferences("fun.eriri.wordroid.model_preferences", MODE_PRIVATE);
		timepre.setSummary(settings.getString("time", "18:00 下午"));
		CharSequence[] list={"英语","美语"};
		listpre=(ListPreference) this.findPreference("category");
		listpre.setEntries(list);
		CharSequence[] list2={"1","2"};
		listpre.setEntryValues(list2);
		listpre.setSummary(listpre.getEntry());
		 onSharedPreferenceChangeListener = new OnSharedPreferenceChangeListener() {
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				Log.e("1", "onSharedPreferenceChanged: is checked" );
				listpre.setSummary(listpre.getEntry());
			}
		};
		SharedPreferences sharedPreferences = this.getPreferenceScreen().getSharedPreferences();
		sharedPreferences.registerOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
	}

	@SuppressWarnings("unchecked")

	public boolean onPreferenceChange(android.preference.Preference preference,
									  Object newValue) {
		if (preference.getKey().equals("category")){
			listpre.setSummary(listpre.getEntry());
		}
		if(preference.getKey().equals("time")){
			HashMap<String,Integer> map =(HashMap<String, Integer>) newValue;
			String ifAm="上午";
			if(map.get("hour")>11)ifAm=" 下午";
			int minute=map.get("minute");
			String mi=String.valueOf(minute);
			if (minute<10)mi="0"+minute;
			timepre.setSummary(""+map.get("hour")+":"+mi+" "+ifAm);
			OperationOfBooks OOB = new OperationOfBooks();
			SharedPreferences settings = getSharedPreferences("fun.eriri.wordroid.model_preferences", MODE_PRIVATE);
			OOB.setNotify(settings.getString("time", "18:00 下午"),this);
		}


		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener );
	}
}