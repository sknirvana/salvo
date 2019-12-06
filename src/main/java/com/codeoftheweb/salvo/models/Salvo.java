package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;


    //un jugador (gameplayer) puede tener muchos salvos, esta es su relacion
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    private int turn;

    @ElementCollection
    @Column(name = "salvoLocation")
    private List<String> salvoLocations = new ArrayList<>();

    public Salvo() {
    }

    public Salvo(List<String> location, GamePlayer gamePlayer, int turn) {
        this.salvoLocations = location;
        this.gamePlayer = gamePlayer;
        this.turn = turn;
    }

    public Map<String, Object> makeSalvoDTO() { //Metedo para obtener un salvo
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.getTurn());
        dto.put("locations", this.getSalvoLocations());
        dto.put("player", getGamePlayer().getPlayer().getId());
        return dto;
    }


    public Map<String, Object> hitsDTO() { //Metodo para contar los hits generados al disparar los tiros

        Map<String, Object> dto = new LinkedHashMap<>();

        //Aca obtiene la cantidad de shipsLocation y los pongo al mismo nivel con flatmap para recorrerlo cuando sea necesario
        List<String> shipLocations = gamePlayer.getOpponent().getShips()
                .stream()
                .flatMap(ship -> ship.getShipLocations().stream())
                .collect(Collectors.toList());

        //---Filtrar shipLocations para agarrar solo las que existan en SalvoLocations y añadirlas a hitLocations
        //---preguntamos a shipLocation si contiene el hit de una vuelta (hace el ciclo de for para cada shipLocation)

        //  1 forma de hacerlo es: ->> List<String> hitLocations = salvoLocations.stream().filter(shipLocations::contains).collect(Collectors.toList());
        /*
            2 Otra forma de hacerlo es: ->>
            for(int i=0; i<salvoLocations.size(); i++){
            if(shipLocations.contains(salvoLocations.get(i)))
                hitLocations.add(salvoLocations.get(i));} */


        dto.put("turn", turn);
        //---Añadir hitLocations al dto
        dto.put("hitLocations", getHitLocations(shipLocations));
        dto.put("damages", getDamage());
        dto.put("missed", salvoLocations.size() - getHitLocations(shipLocations).size());

        return dto;
    }

    public List<String> getHitLocations(List<String> shipLocations) {
        List<String> hitLocations = new ArrayList<>();
        for (String s : salvoLocations) {
            if (shipLocations.contains(s))
                hitLocations.add(s);
        }
        return hitLocations;
    }

    public Map<String, Integer> getDamage() {   // si rompe fue aca

        Map<String, Integer> hitsPerTurn = new LinkedHashMap<>();

        hitsPerTurn.put("carrierHits", hitPerShip("carrier", salvoLocations));
        hitsPerTurn.put("destroyerHits", hitPerShip("destroyer", salvoLocations));
        hitsPerTurn.put("battleshipHits", hitPerShip("battleship", salvoLocations));
        hitsPerTurn.put("submarineHits", hitPerShip("submarine", salvoLocations));
        hitsPerTurn.put("patrolboatHits", hitPerShip("patrolboat", salvoLocations));


        hitsPerTurn.put("carrier", hitAcumulatedPerShip("carrier"));
        hitsPerTurn.put("destroyer", hitAcumulatedPerShip("destroyer"));
        hitsPerTurn.put("battleship", hitAcumulatedPerShip("battleship"));
        hitsPerTurn.put("submarine", hitAcumulatedPerShip("submarine"));
        hitsPerTurn.put("patrolboat", hitAcumulatedPerShip("patrolboat"));

        return hitsPerTurn;
    }


    // Aca quiero la cantidad de veces que le pegaron a un ship con este salvo (por que estoy en salvo)
    public int hitPerShip(String type, List<String> salvoLocations1) {

        //Esto es solo para el ship en funcion del tipo
        Ship ship = gamePlayer.getOpponent().getShipPerType(type);

        // El contador suma si el ship fue golpeado (lo obtenemos de getShipLocation)
        int hits = 0;
        for (String location : salvoLocations1) {
            if (ship.getShipLocations().contains(location))
                hits++;
        }

        return hits;
    }


    //Aca lo que quiero es saber la cantidad de veces que fue golpeado mi ship hasta el turno dado
    public int hitAcumulatedPerShip(String type) {

        // Obtengo de los salvos de los turnos anteriores
        List<Salvo> salvos = gamePlayer.getSalvoes().stream().filter(salvo -> salvo.getTurn() <= turn).collect(Collectors.toList());

        // Bajo (flatmap) los salvos utilizados hasta el momento
        List<String> saLocation = salvos.stream().flatMap(salvo -> salvo.getSalvoLocations().stream()).collect(Collectors.toList());

        // Tipo de barco y las location de los salvos de los turnos anteriores y devuelve esos hits
        return hitPerShip(type, saLocation);

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
