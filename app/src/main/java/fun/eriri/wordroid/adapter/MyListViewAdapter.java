package fun.eriri.wordroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fun.eriri.wordroid.entry.Entry;
import wordroid.model.R;

public class MyListViewAdapter extends BaseAdapter {
    private Context context;
    private List<Entry> entries;
    private LayoutInflater inflater;

    public MyListViewAdapter(Context context , List<Entry> entries){
        this.context  = context;
        this.entries = entries;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int position) {
        return entries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private ViewHolder viewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_me_listview,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.list_me_img);
            viewHolder.textView = convertView.findViewById(R.id.list_me_txt);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Entry entry = entries.get(position);
        viewHolder.imageView.setImageResource(entry.getIco());
        viewHolder.textView.setText(entry.getText());
        return convertView;
    }

    public  static class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
