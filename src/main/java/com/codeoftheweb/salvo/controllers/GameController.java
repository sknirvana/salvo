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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class GameController {

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/games" , method = RequestMethod.POST)// Metodo para crear un juego
    public ResponseEntity<Object> getGame_All(Authentication authentication){

        if (isGuest(authentication)) {// Usuario no autenticado
            return new ResponseEntity<>("Rejected request - Solicitud rechazada", HttpStatus.UNAUTHORIZED);}

        Player player = playerRepository.findByEmail(authentication.getName());

        if (player == null){ // Jugador no autenticado
            return new ResponseEntity<>("Rejected request - Solicitud rechazada" , HttpStatus.UNAUTHORIZED);}

        Game game = gameRepository.save(new Game()); //creando y guardando un nuevo juego
        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(player, game));// guardando jugador y juego actual

        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()) , HttpStatus.CREATED);

    }

    @RequestMapping ("/games")//Mètodo que muestra los juegos actuales
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
                .map(Game::makeGameDTO)
                .collect(Collectors.toList()));

        return dto;
    }

    private boolean isGuest(Authentication authentication) { // Mètodo para autenticar a un visitante
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    public Map makeMap(String key, Object value){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key, value);
        return dto;
    }

}
