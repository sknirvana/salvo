package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native" , strategy = "native")
    private long id;


    //un jugador (gameplayer) puede tener muchos salvos, esta es su relacion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @Column(name="salvoLocation")
    private List<String> salvoLocations = new ArrayList<>();

    public Salvo(){}

    public Salvo( List<String> location , GamePlayer gamePlayer , int turn ){
        this.salvoLocations = location;
        this.gamePlayer = gamePlayer;
        this.turn = turn;
    }

    public Map<String,Object> makeSalvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn" , this.getTurn());
        dto.put("locations" , this.getSalvoLocations());
        dto.put("player" , getGamePlayer().getPlayer().getId());
        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }
}
