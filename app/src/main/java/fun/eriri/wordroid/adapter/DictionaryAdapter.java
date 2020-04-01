package fun.eriri.wordroid.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import fun.eriri.wordroid.activitys.study;
import fun.eriri.wordroid.bean.DictionaryBean;
import wordroid.model.R;

public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.mViewHolder>{

    private Context context ;
    private final List<DictionaryBean> data ; //data里放着一个图片的链接和一个描述文字
    private LayoutInflater layoutInflater;

    private dictionaryListenr dictionaryListenr;
    public DictionaryAdapter(Context context, List<DictionaryBean> data ){
        this.context = context;
        this.data = data;
        layoutInflater = LayoutInflater.from(context);
    }
    /**
     * 这个方法可以用原生代码解决image读取，但是不用这个
     * */
//    public void initData(){
//        String[] strings = new String[data.size()];
//        DictionaryAysncTask.startTask(new DictionaryAysncTask.DictionaryListner() {
//            @Override
//            public void onSucesss() {
//
//            }
//
//            @Override
//            public void onProgress(InputStream inputStream) {
//
//            }
//        },strings);
//    }
    public void setListner(dictionaryListenr dictionaryListenr){
        this.dictionaryListenr = dictionaryListenr;
    }

    @NonNull
    @Override
    public mViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new mViewHolder(layoutInflater.inflate(R.layout.dictionary_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull mViewHolder holder, final int position) {
        final DictionaryBean dictionaryBean = data.get(position);
        holder.textView.setText(dictionaryBean.getName());
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.loading)
                .error(R.mipmap.erro);
        Glide.with(context)
            .load(dictionaryBean.getUrl())
             .apply(options)
            .into(holder.img);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context)
                        .setTitle("是否下载此词库")
                        .setIcon(R.drawable.ic_edit_black_24dp)
                        .setMessage("同步此词库")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //点击确定之后去下载词典，且同步词典，但是不能卡ui
                                dictionaryListenr.download(data.get(position).getTxtUrl(),data.get(position).getName());
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).create();

                alertDialog.show();
                alertDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.white_btn));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class mViewHolder extends RecyclerView.ViewHolder{

        private CardView cardView;
        private ImageView img;
        private TextView textView;
       public mViewHolder(@NonNull View itemView) {
           super(itemView);
           cardView = itemView.findViewById(R.id.all);
           img = itemView.findViewById(R.id.img_ico);
           textView = itemView.findViewById(R.id.msg);
       }
   }
   public interface dictionaryListenr {
        //点击一本书时，应该返回一个链接共下载，或直接开始下载
       void download(String url,String name);
   }
}
