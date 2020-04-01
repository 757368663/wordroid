//package fun.eriri.wordroid.util;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.List;
//
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.Response;
//
///***
// * 这个里面写adapter解析data里的图片
// * 解析完成后设置到AdapterImageView里
// */
//public class DictionaryAysncTask  {
//
//    public static void startTask(DictionaryListner dictionaryListner,String[] strings){
//        DictionaryAysncTaskInfo dictionaryAysncTask = new DictionaryAysncTaskInfo(dictionaryListner);
//        dictionaryAysncTask.execute(strings);
//    }
//
//    private static class DictionaryAysncTaskInfo extends AsyncTask<String,InputStream,Boolean>{
//        private static final String TAG = "DICTIONARY WRONG";
//        private DictionaryListner dictionaryListner;
//        private DictionaryAysncTaskInfo(DictionaryListner dictionaryListner){
//
//        }
//        OkHttpClient httpClient = new OkHttpClient();
//        @Override
//        protected Boolean doInBackground(String ...strings) {
//
//            for (int i=0;i<strings.length;i++){
//                Request request = new Request.Builder()
//                        .url(strings[0])
//                        .build();
//                try {
//                    Response execute = httpClient.newCall(request).execute();
//                    InputStream inputStream = execute.body().byteStream();
//                    publishProgress(inputStream);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//            return true;
//        }
//
//        @Override
//        protected void onProgressUpdate(InputStream... values) {
//            super.onProgressUpdate(values);
//            dictionaryListner.onProgress(values[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            if (aBoolean){
//                dictionaryListner.onSucesss();
//            }else{
//                Log.e(TAG, "onPostExecute: 失败" );
//            }
//        }
//    }
//
//
//    public interface DictionaryListner{
//        void onSucesss();
//        void onProgress(InputStream inputStream);
//
//    }
//}
