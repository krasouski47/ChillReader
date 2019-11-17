package com.evg47.myapplication_03_11.app.data;

public class MyBooks {

    private String bookName;
    private int lastPage;

    public MyBooks(String bookName, int lastPage) {
        this.bookName = bookName;
        this.lastPage = lastPage;
    }
    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public String getBookName() {
        return bookName;
    }

    public int getLastPage() {
        return lastPage;
    }
}
