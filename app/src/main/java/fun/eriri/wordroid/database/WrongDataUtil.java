package fun.eriri.wordroid.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import fun.eriri.wordroid.model.Word;

public class WrongDataUtil  {

    public static void  insertWrong(Context context,Word word){
        String sql = "replace into wrong_word(ID,spelling,meaning,PHONETIC_ALPHABET) values(?,?,?,?)";

        SQLiteDatabase instance = GetSqlHelper.getInstance(context);
        instance.execSQL(sql,new String[]{word.getID(),word.getSpelling(),word.getMeanning(),word.getPhonetic_alphabet()});
    }
    public static void selectAllWord(Context context,List<Word> list){
        String sql = "select * from wrong_word";
        SQLiteDatabase instance = GetSqlHelper.getInstance(context);
        Cursor cursor = instance.rawQuery(sql, null);
        while (cursor.moveToNext()){
            Word word = new Word();
            word.setID(cursor.getString(cursor.getColumnIndex("ID")));
            word.setSpelling(cursor.getString(cursor.getColumnIndex("spelling")));
            word.setMeanning(cursor.getString(cursor.getColumnIndex("meaning")));
            word.setPhonetic_alphabet(cursor.getString(cursor.getColumnIndex("PHONETIC_ALPHABET")));
            list.add(word);
        }
    }

    public static void deleteWord(Context context,String  word){
        String sql = "delete from wrong_word where spelling = ?";
        SQLiteDatabase instance = GetSqlHelper.getInstance(context);
        instance.execSQL(sql,new String[]{word});
    }

    public static class GetSqlHelper{
        private static   SQLiteDatabase sqLiteDatabase;
        private GetSqlHelper(){

        }
        public static SQLiteDatabase getInstance(Context context){
            if (sqLiteDatabase == null){
                synchronized (GetSqlHelper.class){
                    if (sqLiteDatabase == null){
                         sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
                                context.getExternalFilesDir(null) + SqlHelper.DB_NAME, null);
                    }
                }
            }
            return sqLiteDatabase;
        }
    }
}
