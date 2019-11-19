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

    private String shipType;

    public Ship(){}

    public Ship(String shipType){}

    public Ship(List<String> shipLocation ,String  shipType,  GamePlayer gamePlayer){
        this.gamePlayer = gamePlayer;
        this.shipType = shipType;
        this.shipLocations = shipLocation;
    }

    @ElementCollection
    @Column(name="location")
    private List<String> shipLocations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public List<String> getShipLocation() {
        return shipLocations;
    }

    public void setShipLocation(List<String> shipLocation) {
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
        dto.put("type", this.shipType);
        dto.put("locations", this.shipLocations);

        return dto;
    }

}
