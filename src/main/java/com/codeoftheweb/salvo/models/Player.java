package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Entity
public class Player {
     @Id
     @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
     @GenericGenerator(name = "native", strategy = "native")
     private long id;

     private String email;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
     private Set <GamePlayer> gamePlayers;


   public Player(){};

   public Player(String email){
       this.email = email;
   }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<GamePlayer> getGamePlayers() {
        return gamePlayers;
    }

    public void setGamePlayers(Set<GamePlayer> gamePlayers) {
        this.gamePlayers = gamePlayers;
    }

    public Map<String,Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getEmail());

        return dto;
    }
}



