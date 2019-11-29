package com.codeoftheweb.salvo.repository;

import com.codeoftheweb.salvo.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



@RepositoryRestResource
public interface PlayerRepository extends JpaRepository <Player , Long>  {
   Player findByEmail(@Param("email") String email);
   //Esta es nuestra "base de datos" , para esto se necesita un ID y un EMAIL ò USERNAME (en este caso es email)
   //Aca lo que hacemos es buscar personas por su nombre
   //cuando inician o se crean una cuenta nueva, con el metodo de arriba se logra todo esto
}
