package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;


@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native" , strategy = "native")
    private long id;

    private Date joinDate;


    //Muchos partidas a un solo player
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    //Muchos juegos  a un solo player
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    //En este caso solo le pertenece un solo gameplayer a ships
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships;

    //un solo gameplayer a salvos
    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Salvo> salvoes;

    public GamePlayer(){
        this.joinDate = new Date();
    }

    public GamePlayer (Player player , Game game){
        this.joinDate = new Date();
        this.player = player;
        this.game = game;
    }

    @RequestMapping// --> Mètodo que devuelve a un player y su Id
    public Map<String,Object> makeGamePlayerDTO(){
        Map<String, Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());// El metodo put quedan directamente asociados a la K y la V del Map
        dto.put("player", this.getPlayer().makePlayerDTO());

        return dto;
    }


    // Devuelve la partida del oponente
    public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers()
                .stream()
                .filter(gamePlayer -> gamePlayer.getId() != this.getId())
                .findFirst().orElse(null);
    }

    public Score getScore(){
        return this.player.getScore(this.game.getId());
    }


    //Recibe el pedido de un tipo de ship, y lo devuelve
    public Ship getShipPerType(String type){
        return ships.stream().filter(ship -> ship.getType().equals(type)).findFirst().get();
    }

    public void addSalvo(Salvo salvo){this.salvoes.add(salvo);}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public Set<Salvo> getSalvoes() {
        return salvoes;
    }

    public void setSalvoes(Set<Salvo> salvoes) {
        this.salvoes = salvoes;
    }


}
