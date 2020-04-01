package fun.eriri.wordroid.bean;

import java.util.Date;

public class DictionaryBean {
    private String id;
    private String name;
    private String numoflist;
    private String wordoflist;
    private String createDate;
    private String url;
    private String txtUrl;

    public DictionaryBean(){

    }

    public DictionaryBean(String id, String name, String numoflist, String wordoflist, String createDate, String url, String txtUrl) {
        this.id = id;
        this.name = name;
        this.numoflist = numoflist;
        this.wordoflist = wordoflist;
        this.createDate = createDate;
        this.url = url;
        this.txtUrl = txtUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumoflist() {
        return numoflist;
    }

    public void setNumoflist(String numoflist) {
        this.numoflist = numoflist;
    }

    public String getWordoflist() {
        return wordoflist;
    }

    public void setWordoflist(String wordoflist) {
        this.wordoflist = wordoflist;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTxtUrl() {
        return txtUrl;
    }

    public void setTxtUrl(String txtUrl) {
        this.txtUrl = txtUrl;
    }
}
