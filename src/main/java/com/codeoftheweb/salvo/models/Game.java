package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;



import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private Date creationDate;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<GamePlayer> gamePlayers;

    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    private Set<Score> scores;


    public Map<String,Object> makeGameDTO(){

        Map<String,Object> dto= new LinkedHashMap<>();

        dto.put("id" , this.getId());
        dto.put("created", this.getCreationDate());
        dto.put("gamePlayers", this.getGamePlayersList());
        dto.put("scores" , this.getScoresList());
        return dto;
    }

    public List<Map<String,Object>> getGamePlayersList(){
        return this.getGamePlayers()
                .stream()
                .map(gamePlayer -> gamePlayer.makeGamePlayerDTO())
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getScoresList(){
        return this.getScores()
                .stream()
                .map(score -> score.makeScoreDTO())
                .collect(Collectors.toList());

    }

    public Game(){
        this.creationDate= new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
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
}
