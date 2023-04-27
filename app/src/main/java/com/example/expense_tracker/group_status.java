package com.example.expense_tracker;

public class group_status {

    private boolean status;
    private String group_code;

    public group_status(){

    }
    public boolean getStatus(){
        return status;
    }
    public void setStatus(boolean status){
        this.status = status;
    }

    public String getGroup_code(){
        return group_code;
    }
    public void setGroup_code(String group_code){
        this.group_code = group_code;
    }


}
