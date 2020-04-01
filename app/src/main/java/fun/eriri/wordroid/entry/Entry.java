package fun.eriri.wordroid.entry;

public class Entry {
    private int ico;
    private  String text;

    public Entry(){

    }

    public Entry(int ico, String text) {
        this.ico = ico;
        this.text = text;
    }

    public int getIco() {
        return ico;
    }

    public void setIco(int ico) {
        this.ico = ico;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
