package com.sgp.spendsync.Model;

public class ModelTransaction {

    String Amount;
    String datentime;
    String Note;
    int image_pos;

    String Transaction_type;

    public String getTransaction_type() {
        return Transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        Transaction_type = transaction_type;
    }

    public String getCategory_type() {
        return Category_type;
    }

    public void setCategory_type(String category_type) {
        Category_type = category_type;
    }

    String Category_type;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDatentime() {
        return datentime;
    }

    public void setDatentime(String datentime) {
        this.datentime = datentime;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getImage_pos() {
        return image_pos;
    }

    public void setImage_pos(int image_pos) {
        this.image_pos = image_pos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
}
