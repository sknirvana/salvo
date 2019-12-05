package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;


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

     private String password;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
     private Set <GamePlayer> gamePlayers;

    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    private Set<Score> scores;

    public Map<String,Object> makePlayerDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", this.getId());
        dto.put("email", this.getEmail());
        return dto;
    }

    public Score getScore(long gameId) {
        return scores.stream()
                .filter(_score -> _score.getGame().getId() == gameId)
                .findFirst()
                .orElse(null);
    }

    public Player(){};

    public Player(String email , String password){
        this.email = email;
        this.password = password;
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

    public Set<Score> getScores() {
        return scores;
    }

    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    public String getPassword() {
        return password;
    }


}



