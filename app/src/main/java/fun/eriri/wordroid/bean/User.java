package fun.eriri.wordroid.bean;

import androidx.annotation.NonNull;

import java.util.Date;

public class User {
    private String id;
    private String username;
    private String password;
    private String phone_number;
    private String email;
    private String question;
    private String answer;
    private Date create_date;
    private String database_user;
    private String school;

    public User(){

    }

    public User(String id, String username, String password, String phone_number, String email, String question, String answer, Date create_date, String database_user,String school) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.phone_number = phone_number;
        this.email = email;
        this.question = question;
        this.answer = answer;
        this.create_date = create_date;
        this.database_user = database_user;
        this.school = school;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getDatabase_user() {
        return database_user;
    }

    public void setDatabase_user(String database_user) {
        this.database_user = database_user;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

}