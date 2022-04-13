package FindAJob.controladores;

import FindAJob.entidades.Posteo;
import FindAJob.entidades.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/posteo")
public class PosteoControlador {

    //      Desde el navvar se selecciona "trabajar" o "contratar", segun lo seleccionado
    //      lo llevara a /formCliente o /formTrabajador
    
   @GetMapping("/formCliente")
   public String enviarPosteoCliente(ModelMap model){
       
       model.addAttribute("posteo", new Posteo());  //envio un nuevo posteo a la vista
       
       //completado del formulario para crear un posteo de una oferta de trabajo
       
       //retornar vista de posteo de cliente buscando trabajadores
       return null;
   }
   
   @PostMapping("/formCliente")
   public String traerPosteoCliente(){
       
       
       //retornar vista
       return null;
   }
    
}
