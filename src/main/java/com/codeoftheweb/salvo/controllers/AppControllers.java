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

import java.util.*;
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

        if (Objects.isNull(gamePlayer.getOpponent())){
            hits.put("self", new LinkedList<>());
            hits.put("opponent" , new LinkedList<>());
        }
        else{
            hits.put("self" , this.getHits(gamePlayer));
            hits.put("opponent" , this.getHits(gamePlayer.getOpponent()));
        }



        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
        dto.put("gameState" , Util.getState(gamePlayer, gamePlayer.getOpponent()));
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers()
                .stream()
                .map(GamePlayer::makeGamePlayerDTO)); // .map(_gamePlayer -> _gamePlayer.makeGamePlayerDTO()) esto es lo mismo
        dto.put("ships", gamePlayer.getShips()
                .stream()
                .map(Ship::makeShipDTO)// .map(ship -> ship.makeShipDTO())
                .collect(Collectors.toList()));
        dto.put("salvoes" , gamePlayer.getGame().getGamePlayers()
                .stream()
                .flatMap(_gamePlayer -> _gamePlayer.getSalvoes() .stream())
                .map(Salvo::makeSalvoDTO)// .map(salvo -> salvo.makeSalvoDTO())
                .collect(Collectors.toList()));
        dto.put("hits" , hits);

        return (new ResponseEntity<>(dto, HttpStatus.OK));
    }

    //----------------------------------------------------------------------------------------------------------

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

    private boolean isGuest(Authentication authentication) { // Mètodo para autenticar a un visitante
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    private List<Object> getHits (GamePlayer gamePlayer){

        GamePlayer gpOpponent = gamePlayer.getOpponent();

        //hacer dto para hits , con las locations de los salvos
        List<Object> dtoHits = new ArrayList<>();

        // Ordena los salvos segun el turno
        List<Salvo> salvosInOrder = gpOpponent.getSalvoes()
                .stream()
                .sorted(Comparator.comparingInt(Salvo::getTurn))
                .collect(Collectors.toList());

        // Aplico hitsDTO a los salvos y los guardo en dtoHits
        salvosInOrder.forEach(salvo -> dtoHits.add(salvo.hitsDTO()));

        return dtoHits;
    }


    public Map makeMap(String key, Object value){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key, value);
        return dto;
    }




}
