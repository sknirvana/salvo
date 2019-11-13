package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.Game;
import com.codeoftheweb.salvo.models.GamePlayer;
import com.codeoftheweb.salvo.models.Player;
import com.codeoftheweb.salvo.repository.GamePlayerRepository;
import com.codeoftheweb.salvo.repository.GameRepository;
import com.codeoftheweb.salvo.repository.PlayerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GamePlayerRepository gamePlayerRepository , GameRepository gameRepository) {
		return (args) -> {

			// creacion de jugadores

			/*playerRepository.save(new Player("Mayra@gmail.com"));
			playerRepository.save(new Player("Agustina@gmail.com"));
			playerRepository.save(new Player("Melody@gmail.com"));
			playerRepository.save(new Player("Cristina@gmail.com"));*/

			Player player1 = (new Player( "Mayra@gmail.com"));
			Player player2 = (new Player( "Agustina@gmail.com"));

			playerRepository.save(player1);
			playerRepository.save(player2);


			Game game1 = (new Game());
			gameRepository.save(game1);


			/*DENTO DE NEW INDICAR SIEMPRE A QUE REFIERO, EN ESTE CASO "GAMEPLAYER", AUNQUE EN EL
			* PRENTESIS NO VAYA NADA , COMO EL CASO DE ARRIBA -GAME-, ESTA REFERENCIADO*/

			GamePlayer gamePlayer1 = (new GamePlayer(player1 , game1));
			GamePlayer gamePlayer2 = (new GamePlayer(player2  ,game1));

			gamePlayerRepository.save(gamePlayer1);
			gamePlayerRepository.save(gamePlayer2);


		};
	}
}
