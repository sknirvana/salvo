package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.models.Ship;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import com.codeoftheweb.salvo.repository.ShipRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GamePlayerRepository gamePlayerRepository , GameRepository gameRepository , ShipRepository shipRepository) {
		return (args) -> {

			//Instanciando y guardando jugadores
			Player player1 = new Player("jugador-1@gmail.com");
			playerRepository.save(player1);

			Player player2 = new Player("jugador-2@gmail.com");
			playerRepository.save(player2);

			Player player3 = new Player("jugador-3@gmail.com");
			playerRepository.save(player3);

			Player player4 = new Player("jugador-4@gmail.com");
			playerRepository.save(player4);


			//-----------------------------------------------------------------------------------

			//Instanciando y guardando juegos
			Game game1 = new Game();
			gameRepository.save(game1);

			Game game2 = new Game();
			gameRepository.save(game2);

			Game game3 = new Game();
			gameRepository.save(game3);


			//----------------------------------------------------------------------------------

			//Instanciando y guardando partidas

			//primer partida
			GamePlayer gamePlayer1 = new GamePlayer(player1 , game1);
			gamePlayerRepository.save(gamePlayer1);
			GamePlayer gamePlayer2 = new GamePlayer(player2 , game1);
			gamePlayerRepository.save(gamePlayer2);

			//segunda partida
			GamePlayer gamePlayer3 = new GamePlayer(player3 , game2);
			gamePlayerRepository.save(gamePlayer3);
			GamePlayer gamePlayer4 = new GamePlayer(player4 , game2);
			gamePlayerRepository.save(gamePlayer4);

			//tercera partida
			GamePlayer gamePlayer5 = new GamePlayer(player2 , game3);
			gamePlayerRepository.save(gamePlayer5);
			GamePlayer gamePlayer6 = new GamePlayer(player4 , game3);
			gamePlayerRepository.save(gamePlayer6);


			//----------------------------------------------------------------------------------

			//Instanciando y guardando shipLocation's
			List<String> shipLocation1 = new ArrayList<>();
			shipLocation1.add("A1");
			shipLocation1.add("A2");
			shipLocation1.add("A3");


			List<String> shipLocation2 = new ArrayList<>();
			shipLocation2.add("J4");
			shipLocation2.add("J5");
			shipLocation2.add("J6");
			shipLocation2.add("J7");


			List<String> shipLocation3 = new ArrayList<>();
			shipLocation3.add("E8");
			shipLocation3.add("E7");
			shipLocation3.add("E6");


			List<String> shipLocation4 =new ArrayList<>();
			shipLocation4.add("C4");
			shipLocation4.add("D4");
			shipLocation4.add("E4");
			shipLocation4.add("F4");


			//Instanciando y guardando ship's
			Ship ship1 = new Ship(shipLocation1,"submarino",gamePlayer1);
			Ship ship2 = new Ship(shipLocation2,"destructor",gamePlayer1);
			Ship ship3 = new Ship(shipLocation3,"barquito",gamePlayer1);
			Ship ship4 = new Ship(shipLocation4, "carrie",gamePlayer1);

			//declaro a que GamePlayer va a ir este ship
			ship1.setGamePlayer(gamePlayer1);
			shipRepository.save(ship1);
			ship2.setGamePlayer(gamePlayer1);
			shipRepository.save(ship2);
			ship3.setGamePlayer(gamePlayer1);
			shipRepository.save(ship3);
			ship4.setGamePlayer(gamePlayer1);
			shipRepository.save(ship4);

		};
	}
}
