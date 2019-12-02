package com.codeoftheweb.salvo.controllers;

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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShipController {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    GamePlayerRepository gamePlayerRepository;
    @Autowired
    ShipRepository shipRepository;
    @Autowired
    PasswordEncoder passwordEncoder;


    @RequestMapping (path = "/games/players/{gamePlayerId}/ships" , method = RequestMethod.POST) // --> Mètodo para la ubicacion de los barcos
    public ResponseEntity<Map<String , Object>> shipList(@PathVariable long gamePlayerId , @RequestBody List<Ship> ships, Authentication authentication){

        // Autenticando al invitado
        if (isGuest(authentication)){
            return new ResponseEntity<>(makeMap("error", "Not logged in  - No esta logueado") , HttpStatus.UNAUTHORIZED);}

        // Buscar en playerRepository al jugador que se loguee, para autenticarlo
        Player playerLogued = playerRepository.findByEmail(authentication.getName());

        // Obtener la partida y se lo asigna a la variable temporal creada en este mètodo
        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();

        // Partida sin identificacion - Se busca su id, y si no esta almanecenada se toma como null. Se rechaza la solicitud
        if (gamePlayerRepository.findById(gamePlayerId).get() == null){
            return new ResponseEntity<>(makeMap("error" , "May need your Id - Necesita su identificacion") , HttpStatus.UNAUTHORIZED);}

        // Si el jugador logueado es diferente del jugador actual conectado, rechaza la solicitud
        if (gamePlayer.getPlayer().getId() != playerLogued.getId() ){
            return new ResponseEntity<>(makeMap("error" , "Chek your steps again - Revise sus pasos nuevamente") , HttpStatus.UNAUTHORIZED);}

        // Respuesta prohibida cuando el jugador ya tenga naves colocadas
        if (!gamePlayer.getShips().isEmpty()) {
            return new ResponseEntity<>(makeMap("error" , "You already have your ships locations - Sus barcos ya tienen ubicaciones asignadas") , HttpStatus.FORBIDDEN);}

        // Recorre una set de barcos y se lo asigna al parametro creado en este mètodo
        ships.forEach(ship -> {
            ship.setGamePlayer(gamePlayer);});
        shipRepository.saveAll(ships);

        // Si los pasos anteriores fueron exitosos, entonces se agregan los barcos
        return new ResponseEntity<>(makeMap("OK","Ships added - Barcos agregados"), HttpStatus.CREATED);
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
