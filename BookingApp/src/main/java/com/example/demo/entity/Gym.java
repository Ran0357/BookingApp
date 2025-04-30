package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "gym")
public class Gym {

    @Id
    @Column(name = "managementnumber", length = 16)
    private String managementnumber;

    @Column(name = "model", length = 32)
    private String model;

    @Column(name = "usagestatus")
    private int usagestatus;

    // --- Getter & Setter ---
    public String getManagementnumber() {
        return managementnumber;
    }

    public void setManagementnumber(String managementnumber) {
        this.managementnumber = managementnumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getUsagestatus() {
        return usagestatus;
    }

    public void setUsagestatus(int usagestatus) {
        this.usagestatus = usagestatus;
    }

    // 利用中かどうかの補助メソッド
    public boolean isInUse() {
        return this.usagestatus == 1;
    }
}
