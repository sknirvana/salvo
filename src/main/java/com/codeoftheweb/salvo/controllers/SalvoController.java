package com.codeoftheweb.salvo.controllers;

import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Salvo;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    GamePlayerRepository gamePlayerRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SalvoRepository salvoRepository;


    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes" , method = RequestMethod.POST)// Metodo para almacenar salvos
    public ResponseEntity<Map> addSalvos (@PathVariable long gamePlayerId , @RequestBody Salvo salvoShot, Authentication authentication){

        // Autenticando al invitado
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "Not logged in  - No esta logueado") , HttpStatus.UNAUTHORIZED);}

        //Calcula la cantidad de salvos y si es menor a 5, significa que no uso todos sus tiros
        if (salvoShot.getSalvoLocations().size() != 5)
            return new ResponseEntity<>(makeMap("error" , "Utiliza todos los salvos"), HttpStatus.FORBIDDEN);

        // Buscar en playerRepository al jugador que se loguee, para autenticarlo
        Player playerLogued = playerRepository.findByEmail(authentication.getName());

        // Obtener la partida y se lo pasa por parametro a la variable temporal creada en este mètodo
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();

        // Si el jugador logueado es diferente del jugador actual conectado, rechaza la solicitud
        if (gamePlayer.getPlayer().getId() != playerLogued.getId()){
            return new ResponseEntity<>(makeMap("error" , "Check your steps again - Revise sus pasos nuevamente") , HttpStatus.UNAUTHORIZED);}

        // Condicion para que itere los salvos tirados
        //Esta funcion compara la cantidad de turnos que esta en la base de datos
        if(gamePlayer.getSalvoes().size() <= gamePlayer.getOpponent().getSalvoes().size()){

            //cantidad de salvos <= cantidad de salvos oponente ->
           // -> es true: se guarda el salvo
            salvoShot.setTurn(gamePlayer.getSalvoes().size()+1);

            // Guardando salvos (turnos) en gamePlayer
            salvoShot.setGamePlayer(gamePlayer);
            salvoRepository.save(salvoShot);
            return new ResponseEntity<>(makeMap("OK" , "Salvo send - Salvo enviado"), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(makeMap("error" , "Ya utilizo su turno"), HttpStatus.FORBIDDEN);
    }

    private boolean isGuest(Authentication authentication) { // Mètodo para autenticar a un visitante
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    private Map makeMap(String key, Object value){
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put(key, value);
        return dto;
    }
}
