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


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @OneToMany(mappedBy="gamePlayer", fetch=FetchType.EAGER)
    private Set<Ship> ships;

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

    @RequestMapping// --> Mètodo que devuelve a un Jugador y su Id
    public Map<String,Object> makeGamePlayerDTO(){
        Map<String, Object> dto= new LinkedHashMap<>();
        dto.put("id", this.getId());// El metodo put quedan directamente asociados a la K y la V del Map
        dto.put("player", this.getPlayer().makePlayerDTO());

        return dto;
    }

    public GamePlayer getOpponent(){
        return this.getGame().getGamePlayers()
                .stream()
                .filter(gamePlayer -> gamePlayer.getId() != this.getId())
                .findFirst().orElse(new GamePlayer());
    }

    public Score getScore(){
        Score score = this.player.getScore(this.game.getId());
        return score;
    }

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
