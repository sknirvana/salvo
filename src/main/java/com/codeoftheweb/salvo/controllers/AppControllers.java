package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AppControllers {

    @Autowired
    GameRepository gameRepository;
    @Autowired
    GamePlayerRepository gamePlayerRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ShipRepository  shipRepository;
    @Autowired
    SalvoRepository salvoRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @RequestMapping(path = "/game/{gameId}/players" , method = RequestMethod.POST)// Metodo para unirse a un juego creado
    public ResponseEntity<Map> joinGame (@PathVariable long gameId , Authentication authentication){

        // Autenticando al invitado
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error" , "Not logged in  - No esta logueado"), HttpStatus.UNAUTHORIZED);}

        // Obtiene desde playerRepository al jugador que se loguee, para autenticarlo
        Player playerLogued = playerRepository.findByEmail(authentication.getName());

        // Obtiene el juego desde gameRepository
        Game game = gameRepository.findById(gameId).get();

        // Juego sin identificacion - Se busca su id, y si no esta almanecenada se toma como null y se rechaza la solicitud
        if (gameRepository.findById(gameId).get() == null){
            return new ResponseEntity<>(makeMap("error", "There is no such game - No existe este juego") , HttpStatus.FORBIDDEN);}

        //Comprueba que haya un jugador en la partida , si hay dos rechaza la solicitud
        if (game.getGamePlayers().size() > 1 ){
            return new ResponseEntity<>(makeMap("error", "El juego esta lleno") , HttpStatus.FORBIDDEN);}

        //cCrea y guarda un nuevo jugador, en este juego junto con el usuario actual
        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(playerLogued, game));

        // Si lo anterior es correcto, la creacion del juego es exitosa
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()) , HttpStatus.CREATED);
    }

    //----------------------------------------------------------------------------------------------------------
    @RequestMapping("/game_view/{gpid}")
    public ResponseEntity<Map<String,Object>> getGames_view(@PathVariable Long gpid , Authentication authentication){

        Map<String, Object> error = new LinkedHashMap<>();

        if (isGuest(authentication)){
            error.put("error", "Necesita Loguearse");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        // Jugador autenticado, obtenemos el nombre por playerRepository
        Player playerLogued = playerRepository.findByEmail(authentication.getName());
        // Asigno al gameplayer(partida actual) en la variable gpid
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null);
        //obtengo el game
        Game game = gamePlayer.getGame();

        if (playerLogued == authentication){
            error.put("error", "Rejected request - Solicitud rechazada");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);}

        if (gamePlayer == authentication){
            error.put("error", "Rejected request - Solicitud rechazada");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);}

        if (gamePlayer.getPlayer().getId() != playerLogued.getId()){
            error.put("error", "Check your steps again - Revise sus pasos nuevamente");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);}

        Map<String, Object> dto = new LinkedHashMap<>();
        Map<String, Object> hits = new LinkedHashMap<>();

        hits.put("self" , new ArrayList<>());
        hits.put("opponent" , new ArrayList<>());

        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gameState" , getState(gamePlayer, gamePlayer.getOpponent()));
        dto.put("hits" , hits);
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(_gamePlayer -> _gamePlayer.makeGamePlayerDTO()));

        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes" , gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(_gamePlayer -> _gamePlayer.getSalvoes() .stream())
                .map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));

        return (new ResponseEntity<>(dto, HttpStatus.OK));
    }

    //----------------------------------------------------------------------------------------------------------

    private boolean isGuest(Authentication authentication) { // Mètodo para autenticar a un visitante
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    public Map makeMap(String key, Object value){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key, value);
        return dto;
    }


    public String getState (GamePlayer gamePlayerSelf , GamePlayer gamePlayerOpponent){
     if( gamePlayerSelf.getShips().isEmpty()){
     return "PLACESHIPS";
     }
     if(gamePlayerSelf.getGame().getGamePlayers().size() == 1){
     return "WAITINGFOROPP";
     }

     if(gamePlayerSelf.getId() < gamePlayerOpponent.getId()){
     return "PLAY";
     }

     if(gamePlayerSelf.getId() > gamePlayerOpponent.getId()){
      return "WAIT";
      }

      return "LOST";
    }

}
