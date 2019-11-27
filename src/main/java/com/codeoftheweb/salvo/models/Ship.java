package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;


@Entity
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native" , strategy = "native")
    private long id;

    private String type;

    public Ship(){}

    public Ship(String type){}

    @ElementCollection
    @Column(name="location")
    private List<String> shipLocations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    public Ship(List<String> shipLocation , String type, GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.type = type;
        this.shipLocations = shipLocation;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getShipLocations() {
        return shipLocations;
    }

    public void setShipLocations(List<String> shipLocation) {
        this.shipLocations = shipLocation;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }


    public Map<String,Object> makeShipDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("type", this.type);
        dto.put("locations", this.shipLocations);

        return dto;
    }

}
