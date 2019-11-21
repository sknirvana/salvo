package com.codeoftheweb.salvo;

import com.codeoftheweb.salvo.models.*;
import com.codeoftheweb.salvo.repository.*;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.support.SimpleTriggerContext;

import javax.xml.stream.Location;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(PlayerRepository playerRepository , GamePlayerRepository gamePlayerRepository , GameRepository gameRepository , ShipRepository shipRepository , SalvoRepository salvoRepository , ScoreRepository scoreRepository) {
		return (args) -> {

			//Instanciando y guardando jugadores

			Player Jbauer = new Player("j.bauer@gmail.com");
			Player Obrian = new Player("c.obrian@gmail.com");
			Player Kbauer = new Player("k.bauer@gmail.com");
			Player Almeida = new Player("t.almeida@gmail.com");

			playerRepository.save(Jbauer);
			playerRepository.save(Obrian);
			playerRepository.save(Kbauer);
			playerRepository.save(Almeida);


			//-----------------------------------------------------------------------------------

			//Instanciando y guardando juegos
			Game game1 = new Game();
			Game game2 = new Game();
			Game game3 = new Game();
			Game game4 = new Game();
			Game game5 = new Game();
			Game game6 = new Game();
			Game game7 = new Game();
			Game game8 = new Game();

			gameRepository.save(game1);
			gameRepository.save(game2);
			gameRepository.save(game3);
			gameRepository.save(game4);
			gameRepository.save(game5);
			gameRepository.save(game6);
			gameRepository.save(game7);
			gameRepository.save(game8);


			//----------------------------------------------------------------------------------

			//Instanciando y guardando partidas

			List<GamePlayer> gameplayers = new ArrayList<>();
			//contar cada add apartir de 0, para llamar (get."numero") al jugador que necesitemos
			gameplayers.add(new GamePlayer(Jbauer, game1)); //0
			gameplayers.add(new GamePlayer(Obrian , game1));//1

			gameplayers.add(new GamePlayer(Jbauer,game2));//2
			gameplayers.add(new GamePlayer(Obrian, game2));//3

			gameplayers.add(new GamePlayer(Obrian , game3));//4
			gameplayers.add(new GamePlayer(Almeida , game3));//5

			gameplayers.add(new GamePlayer(Obrian, game4));//6
			gameplayers.add(new GamePlayer(Jbauer, game4));//7


			gameplayers.add(new GamePlayer(Almeida, game5));//8
			gameplayers.add(new GamePlayer(Jbauer, game5));//9

			gameplayers.add(new GamePlayer(Kbauer, game6));//10
			gameplayers.add(new GamePlayer(Obrian, game6));//11

			gameplayers.add(new GamePlayer(Almeida, game7));//12
			gameplayers.add(new GamePlayer(Jbauer, game7));//13

			gameplayers.add(new GamePlayer(Kbauer, game8));//14
			gameplayers.add(new GamePlayer(Almeida, game8));//15

			gamePlayerRepository.saveAll(gameplayers);


			//----------------------------------------------------------------------------------

			//Instanciando y guardando shipLocation's

			List<Ship> ships = new ArrayList<>();

			//Game 1
			ships.add(new Ship(Arrays.asList("H2","H3","H4"),"Destroyer", gameplayers.get(0)));
			ships.add(new Ship(Arrays.asList("E1","F1","G1"),"Submarine", gameplayers.get(0)));
			ships.add(new Ship(Arrays.asList("B4","B5"),"Patrol Boat", gameplayers.get(0)));
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(1)));
			ships.add(new Ship(Arrays.asList("F1","F2"),"Patrol Boat", gameplayers.get(1)));

			//Game 2
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(2)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(2)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(3)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(3)));

			//Game 3
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(4)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(4)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(5)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(5)));

			//Game 4
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(6)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(6)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(7)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(7)));

			//Game 5
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(8)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(8)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(9)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(9)));

			//Game 6
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(10)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(10)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(11)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(11)));

			//Game 7
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(12)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(12)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(13)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(13)));

			//Game 8
			ships.add(new Ship(Arrays.asList("B5","C5","D5"),"Destroyer", gameplayers.get(14)));
			ships.add(new Ship(Arrays.asList("C6","C7"),"Patrol Boat", gameplayers.get(14)));
			ships.add(new Ship(Arrays.asList("A2","A3","A4"),"Subrmarine", gameplayers.get(15)));
			ships.add(new Ship(Arrays.asList("G6","H6"),"Patrol Boat", gameplayers.get(15)));

			shipRepository.saveAll(ships);


			//----------------------------------------------------------------------------------

			//Instanciando y guardando salvo location's

			List<Salvo> salvo_loc = new ArrayList<>();

			//Turnos, game 1
			salvo_loc.add(new Salvo(Arrays.asList("B5","C5","F1"), gameplayers.get(0), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B4","B5","B6"), gameplayers.get(1), 1));
			salvo_loc.add(new Salvo(Arrays.asList("F2","D5"), gameplayers.get(0), 2));
			salvo_loc.add(new Salvo(Arrays.asList("E1","H3","A2"), gameplayers.get(1), 2));

			//Turnos, game 2
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(2), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(3), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(2), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(3), 2));

			//Turnos, game 3
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(4), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(5), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(4), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(5), 2));

			//Turnos, game 4
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(6), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(7), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(6), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(7), 2));
			//Turnos, game 5
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(8), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(9), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(4), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(9), 2));
			//Turnos, game 6
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(10), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(11), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(10), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(11), 2));
			//Turnos, game 7
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(12), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(13), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(12), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(13), 2));
			//Turnos, game 8
			salvo_loc.add(new Salvo(Arrays.asList("A2","A4","G6"), gameplayers.get(14), 1));
			salvo_loc.add(new Salvo(Arrays.asList("B5","D5","C7"), gameplayers.get(15), 1));
			salvo_loc.add(new Salvo(Arrays.asList("A3","H6"), gameplayers.get(14), 2));
			salvo_loc.add(new Salvo(Arrays.asList("C5","C6"), gameplayers.get(15), 2));

			salvoRepository.saveAll(salvo_loc);


			//----------------------------------------------------------------------------------

			//instanciando y guardando score's

			List<Score> scoreList = new ArrayList<>();

			//Game 1
			 scoreList.add(new Score(1,game1,Jbauer));
			 scoreList.add(new Score(0,game1,Obrian));
			 //Game 2
			 scoreList.add(new Score(0.5,game2,Jbauer));
			 scoreList.add(new Score(0.5,game2,Obrian));
			 //Game 3
			scoreList.add(new Score(0,game3,Obrian));
			scoreList.add(new Score(1,game3,Almeida));
			//Game 4
			scoreList.add(new Score(0.5,game4,Obrian));
			scoreList.add(new Score(0.5,game4,Jbauer));
			//Game 5
			scoreList.add(new Score(1,game5,Almeida));
			scoreList.add(new Score(0,game5,Jbauer));
			//Game 6
			scoreList.add(new Score(0.5,game6,Kbauer));
			scoreList.add(new Score(0.5,game6,Obrian));
			//Game 7
			scoreList.add(new Score(0,game7,Almeida));
			scoreList.add(new Score(1,game7,Jbauer));
			//Game 8
			scoreList.add(new Score(0.5,game8,Kbauer));
			scoreList.add(new Score(0.5,game8,Almeida));

			scoreRepository.saveAll(scoreList);

		};
	}
}
