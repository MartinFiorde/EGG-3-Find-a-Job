package FindAJob.controladores;

import FindAJob.entidades.Usuario;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.servicios.UsuarioServicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/foto")
public class ArchivoControlador {

    private UsuarioServicio usuarioServicio;

    @Autowired
    public ArchivoControlador(UsuarioServicio usuarioServicio) {
        this.usuarioServicio = usuarioServicio;
    }

    @GetMapping("foto{id}")
    public ResponseEntity<byte[]> FotoUsuario(@PathVariable String id) {

        try {
            Usuario usuario = usuarioServicio.buscarMailEqual(id);
            if (usuario.getFoto() == null) {
                throw new ErrorServicio("El usuaio no pose foto");
            }
            byte[] foto = usuario.getFoto().getContenido();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);

            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (ErrorServicio ex) {
            Logger.getLogger(ArchivoControlador.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        
          //para llamar la foto desde la vista 
          //th:src="${'/foto/play' + play.id}"
          
          //th:if="${play.photo}"


    }
}
