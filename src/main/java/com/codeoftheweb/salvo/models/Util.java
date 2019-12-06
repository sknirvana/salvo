package com.codeoftheweb.salvo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {



    public static String getState (GamePlayer gamePlayerSelf , GamePlayer gamePlayerOpponent){

        // Si self no coloco sus barcos, le avisa que lo haga
        if(gamePlayerSelf.getShips().isEmpty())
            return "PLACESHIPS";

        // Si hay un jugador en la partida (gamePlayer), entonces espera al oponente     Espera hasta que el oponente ubique sus salvos
        if(gamePlayerSelf.getGame().getGamePlayers().size() == 1 || gamePlayerOpponent.getShips().size() == 0)
            return "WAITINGFOROPP";

        List<Ship> selfShips = new ArrayList<>(gamePlayerSelf.getShips());
        List<Salvo> oppSalvos = new ArrayList<>(gamePlayerOpponent.getSalvoes());

        List<Ship> oppShips = new ArrayList<>(gamePlayerOpponent.getShips());
        List<Salvo> selfSalvos = new ArrayList<>(gamePlayerSelf.getSalvoes());

        // Si la cantidad de salvos de self  es menor a la de opponent, estonces podes jugar
        if(gamePlayerSelf.getSalvoes().size() < gamePlayerOpponent.getSalvoes().size()){
            return "PLAY";}

        // Calcula el empate, comparando la igualdad entre los salvos de cada uno
        if (gamePlayerSelf.getSalvoes().size() == gamePlayerOpponent.getSalvoes().size()){
            Boolean allSelfShipsSunk = allShipsSunk(gamePlayerSelf);
            Boolean allOppShipsSunk = allShipsSunk(gamePlayerOpponent);

            if(allSelfShipsSunk && allOppShipsSunk)
                return "TIE";

            if(allOppShipsSunk)
                return "WON";

            if(allSelfShipsSunk)
                return "LOST";

            // Si la cantidad de salvos es igual en ambas partes, entonces jugara el de menor ID
            if (gamePlayerSelf.getId() < gamePlayerOpponent.getId())
                return "PLAY";

        }

        return "WAIT";
    }


    public static Boolean allShipsSunk (GamePlayer gamePlayerSelf){

        //shipLocations en el mismo nivel
        List<String> shipLocations = gamePlayerSelf.getShips()
                .stream()
                .flatMap(ship -> ship.getShipLocations().stream())
                .collect(Collectors.toList());

        //hitLocations en el mismo nivel
        List<String> totalHitLocations = gamePlayerSelf.getOpponent().getSalvoes()
                .stream()
                .flatMap(salvo -> salvo.getHitLocations(shipLocations).stream())
                .collect(Collectors.toList());

        return totalHitLocations.size() == shipLocations.size();

    }
}
