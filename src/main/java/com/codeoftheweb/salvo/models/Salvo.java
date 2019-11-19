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
    private List<String> location = new ArrayList<>();

    public Salvo(){}

    public Salvo(int turn , GamePlayer gamePlayer , List<String> location){
        this.turn = turn;
        this.gamePlayer = gamePlayer;
        this.location = location;
    }

    public Map<String,Object> makeSalvoDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("turn" , this.getTurn());
        dto.put("locations" , this.getLocation());
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

    public List<String> getLocation() {
        return location;
    }

    public void setLocation(List<String> location) {
        this.location = location;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

}
