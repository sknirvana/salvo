package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
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
    PasswordEncoder passwordEncoder;

    //-----------------------------------------LISTA DE JUEGOS ACTUALES (2 /games)-----------------------------------------------------------------

    @RequestMapping(path = "/games" , method = RequestMethod.POST)
        public ResponseEntity<Object> getGame_All(Authentication authentication){

        if (isGuest(authentication)) {
            return new ResponseEntity<>("No esta autorizado", HttpStatus.UNAUTHORIZED);
        }

        Player player = playerRepository.findByEmail(authentication.getName());

        if (player == null){
            return new ResponseEntity<>("No esta autorizado" , HttpStatus.UNAUTHORIZED);
        }

        Game game = gameRepository.save(new Game());
        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));

        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()) , HttpStatus.CREATED);

        }
        @RequestMapping ("/games")
        public Map<String,Object> getGameAll(Authentication authentication){
        Map<String, Object> dto =new LinkedHashMap<>();
        if (isGuest(authentication)){
            dto.put("player" , "Guest");
        } else {
            Player playerAutenticado = playerRepository.findByEmail((authentication.getName()));
            dto.put("player", playerAutenticado.makePlayerDTO());
        }
         dto.put("games", gameRepository.findAll()
                .stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));

        return dto;
        }

    //----------------------------------------------------------------------------------------------------------
    @RequestMapping("/game_view/{gpid}")
    public ResponseEntity<Map<String,Object>> getGames_view(@PathVariable Long gpid , Authentication authentication){

        Map<String, Object> error = new LinkedHashMap<>();

        if (isGuest(authentication)){
            error.put("error", "Paso algo");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        Player playerLogued = playerRepository.findByEmail(authentication.getName());
        GamePlayer gamePlayer = gamePlayerRepository.findById(gpid).orElse(null); //busco el gameplayer por la variable nn
        Game game = gamePlayer.getGame();//busco el game

        if (playerLogued == authentication){
            error.put("error", "Paso algo");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        if (gamePlayer == authentication){
            error.put("error", "Paso algo");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().getId() != playerLogued.getId()){
            error.put("error", "Paso algo");
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        }

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

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register (@RequestParam String email, @RequestParam String password){

        if (email.isEmpty() || password.isEmpty()) {//si faltan datos o hay espacios, corre esta accion
            return new ResponseEntity<>("Faltan datos", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(email) !=  null) {//si ya existe el nombre, corre esta accion
            return new ResponseEntity<>("El nombre esta en uso", HttpStatus.FORBIDDEN);
        }

        //jugador creado
        playerRepository.save(new Player(email, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //----------------------------------------------------------------------------------------------------------

    @RequestMapping(path = "/game/{gameId}/players" , method = RequestMethod.POST)
    public ResponseEntity<Map> joinGame (@PathVariable long gameId , Authentication authentication){
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error" , "No esta logueado"),HttpStatus.UNAUTHORIZED);
        }
        Player playerLogued = playerRepository.findByEmail(authentication.getName());

        if (gameRepository.findById(gameId).get() == null){
            return new ResponseEntity<>(makeMap("error", "No esta en un juego") , HttpStatus.FORBIDDEN);
        }
         Game game = gameRepository.findById(gameId).get();
        if (game.getGamePlayers().size() > 1 ){
            return new ResponseEntity<>(makeMap("error", "El juego esta lleno") , HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(playerLogued, game));

        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()) , HttpStatus.CREATED);
    }

    //--------------------------------------------UBACANDO BARCOS--------------------------------------------------------------

    @RequestMapping (path = "/games/players/{gamePlayerId}/ships" , method = RequestMethod.POST)
    public ResponseEntity<Map<String , Object>> shipList(@PathVariable long gamePlayerId , @RequestBody List<Ship> ships, Authentication authentication){

        if (isGuest(authentication)){ //validando que el jugador se loguee
            return new ResponseEntity<>(makeMap("error", "No esta logueado") , HttpStatus.UNAUTHORIZED);
        }

        Player playerLogued = playerRepository.findByEmail(authentication.getName());

        if (gamePlayerRepository.findById(gamePlayerId).get() == null){ //jugador sin identificacion
            return new ResponseEntity<>(makeMap("error" , "No esta en un juego") , HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();

        if (gamePlayer.getPlayer().getId() != playerLogued.getId() ){ //jugador logueado diferente del jugador actual conectado
            return new ResponseEntity<>(makeMap("error" , "No esta en un juego") , HttpStatus.UNAUTHORIZED);
        }

        //Respuesta prohibida cuando el jugador ya tenga naves colocadas
        if (!gamePlayer.getShips().isEmpty()) {
            return new ResponseEntity<>(makeMap("error" , "Ya tiene cargado sus barcos") , HttpStatus.FORBIDDEN);
        }
        //FALTA UNA PARTE, TERMINAR HOY
        ships.forEach(ship -> {
            ship.setGamePlayer(gamePlayer);
        });
        shipRepository.saveAll(ships);

        return new ResponseEntity<>(makeMap("OK","ships added"), HttpStatus.CREATED);
    }

    //----------------------------------------------------------------------------------------------------------

    private boolean isGuest(Authentication authentication) {
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
