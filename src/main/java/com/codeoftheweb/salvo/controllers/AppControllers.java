package com.codeoftheweb.salvo.controllers;


import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
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
    PasswordEncoder passwordEncoder;


    @RequestMapping("/games")
        public Map<String,Object> getGameAll(Authentication authentication){
        Map<String, Object> dto =new LinkedHashMap<>();
        if (isGuest(authentication)){
            dto.put("player" , "guest");
        }
        else {
            Player playerAutenticado = playerRepository.findByEmail((authentication.getName()));
            dto.put("player", playerAutenticado.makePlayerDTO());
        }

         dto.put("games", gameRepository.findAll()
                .stream()
                .map(game -> game.makeGameDTO())
                .collect(Collectors.toList()));

        return dto;
        }

    @RequestMapping("/game_view/{nn}")
    public Map<String,Object> getGames_view(@PathVariable long nn){

        //busco el gameplayer por la variable nn
        GamePlayer gamePlayer = gamePlayerRepository.findById(nn).get();
        //busco el game
        Game game = gamePlayer.getGame();

        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("id", game.getId());
        dto.put("created", game.getCreationDate());
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

        return dto;
    }

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register (@RequestParam String username, @RequestParam String password){

        if (username.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>("Faltan datos", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(username) !=  null) {
            return new ResponseEntity<>("El nombre esta en uso", HttpStatus.FORBIDDEN);
        }

        playerRepository.save(new Player(username, passwordEncoder.encode(password)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

}
