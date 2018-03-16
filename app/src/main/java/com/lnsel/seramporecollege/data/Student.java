package com.lnsel.seramporecollege.data;

/**
 * Created by db on 1/22/2018.
 */

public class Student {

    public String id="";
    public String name="";
    public String roll="";
    public Boolean status=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }



}
