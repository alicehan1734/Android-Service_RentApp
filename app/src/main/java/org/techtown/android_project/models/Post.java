package org.techtown.android_project.models;

import com.google.firebase.firestore.ServerTimestamp;

import org.techtown.android_project.FirebaseID;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Post {

    private String documentId;
    private String title;
    private String contents;
    private String nickname;
    @ServerTimestamp
    private Date date;
    private String currentdate;
    private String document_userID;
    private  String deposit ; // 새로 추가한것들 (09.27)
    private  String rentfee ; // 새로 추가한것들 (09.27)
    private  String location; // 새로 추가한것들 (09.27)
    private String image;

    public Post() {
    }

    public Post(String documentId, String nickname, String title, String currentdate, String deposit, String rentfee, String location, String image) {
        this.documentId = documentId;  //문서자체의 번호
        this.title = title; //제목
        //this.contents = contents; //해당 콘텐츠 내용들
        this.nickname = nickname; //글쓴자의 닉네임
        this.currentdate = currentdate; //문서 쓴 날짜
        //this.document_userID = document_userID; //문서쓴이의 해당 아이디
        this.deposit = deposit; // 보증금
        this.rentfee = rentfee; //렌트비 (1일)
        this.location = location; //지역
        this.image = image;

      //  this.image = image; //이미지들

    }

    public String getDocument_userID() {
        return document_userID;
    }

    public void setDocument_userID(String document_userID) {
        this.document_userID = document_userID;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDeposit() {
        return deposit;
    }

    public void setDeposit(String deposit) {
        this.deposit = deposit;
    }

    public String getRentfee() {
        return rentfee;
    }

    public void setRentfee(String rentfee) {
        this.rentfee = rentfee;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public String getCurrentdate() {

        date = Calendar.getInstance().getTime();
        currentdate = new SimpleDateFormat("MM월 dd일(EE요일) ", Locale.getDefault()).format(date);
        return currentdate;

    }

    public void setCurrentdate(String currentdate) {
        this.currentdate = currentdate;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Post{" +
                "documentId='" + documentId + '\'' +
                ", title='" + title + '\'' +
                ", nickname='" + nickname + '\'' +
                ", date=" + date +
                ", currentdate='" + currentdate + '\'' +
                ", deposit='" + deposit + '\'' +
                ", rentfee='" + rentfee + '\'' +
                ", location='" + location + '\'' +
                ", image=" + image +
                '}';
    }
}
