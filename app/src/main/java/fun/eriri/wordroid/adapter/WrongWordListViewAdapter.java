package fun.eriri.wordroid.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import fun.eriri.wordroid.model.Word;
import wordroid.model.R;

public class WrongWordListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Word> words;
    private  LayoutInflater layoutInflater;
    public WrongWordListViewAdapter(Context context,List<Word> words){
        this.context = context ;
        this.words = words;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setData(){
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    WrongViewHolder wrongViewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.wrong_item,parent,false);
            wrongViewHolder = new WrongViewHolder();
            wrongViewHolder.meaning = convertView.findViewById(R.id.meaning_);
            wrongViewHolder.spelling = convertView.findViewById(R.id.spelling);
            wrongViewHolder.yb = convertView.findViewById(R.id.yb);
            convertView.setTag(wrongViewHolder);
        }else{
            wrongViewHolder = (WrongViewHolder) convertView.getTag();
        }
        Word word = words.get(position);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(),"fonts/1.TTF");
        wrongViewHolder.spelling.setTypeface(typeface);
        wrongViewHolder.yb.setTypeface(typeface);
        wrongViewHolder.meaning.setTypeface(typeface);

        wrongViewHolder.meaning.setText(word.getMeanning());
        wrongViewHolder.yb.setText(word.getPhonetic_alphabet());
        wrongViewHolder.spelling.setText(word.getSpelling());

        return convertView;
    }
    public static class WrongViewHolder {
        private TextView spelling;
        private TextView meaning;
        private TextView yb;
        public WrongViewHolder(){

        }
    }
}
