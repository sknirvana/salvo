package com.codeoftheweb.salvo.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;


@Entity
public class Score {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO , generator = "native")
    @GenericGenerator(name = "native" , strategy = "native")
    private long id;

    private double score;

    private Date finishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;


    public Score(){}

    public Score(double score, Game game, Player player){
        this.score=score;
        this.game=game;
        this.player=player;
        this.finishDate=new Date();
    }

    public Score(double score, Game game, Player player, Date finishDate){
        this.score=score;
        this.game=game;
        this.player=player;
        this.finishDate = finishDate;
    }

    public Map<String, Object> makeScoreDTO(){
        Map<String,Object> dto = new LinkedHashMap<>();
        dto.put("player" , this.getPlayer().getId());
        dto.put("score" , this.getScore());
        dto.put("finishDate" , this.getFinishDate());

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
