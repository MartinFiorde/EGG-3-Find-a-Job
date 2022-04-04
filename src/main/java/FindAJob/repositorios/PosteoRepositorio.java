/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FindAJob.repositorios;

import FindAJob.entidades.Posteo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Martin F - PC Desk
 */
@Repository
public interface PosteoRepositorio extends JpaRepository<Posteo, String> {

}
