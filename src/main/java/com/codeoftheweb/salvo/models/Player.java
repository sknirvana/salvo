package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Player {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
     @GenericGenerator(name = "native", strategy = "native")
     private long id;

     private String userName;
     private String name;


   public Player(){};

    public Player (String userName){
            this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}



