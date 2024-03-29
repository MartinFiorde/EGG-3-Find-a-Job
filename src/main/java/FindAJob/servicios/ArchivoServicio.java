package FindAJob.servicios;

import FindAJob.entidades.Archivo;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ArchivoRepositorio;
import java.io.IOException;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoServicio {
    
    @Autowired
    private final ArchivoRepositorio archivoRepositorio;

    public ArchivoServicio(ArchivoRepositorio archivoRepositorio) {
        this.archivoRepositorio = archivoRepositorio;
    }
    @Transactional
    public Archivo guardar(MultipartFile foto) throws IOException, ErrorServicio{
        if(foto != null){
            Archivo archivo = new Archivo();
            archivo.setMime(foto.getContentType());
            archivo.setNombre(foto.getName());
            archivo.setContenido(foto.getBytes());
            return archivoRepositorio.save(archivo);
        }else{
            
            throw new ErrorServicio("Error al cargar la imagen");  
            }
        }
    @Transactional
    public Archivo actualizarFoto(String idFoto,MultipartFile foto) throws ErrorServicio, IOException{
        
         if(foto != null){
            Archivo archivo = new Archivo();
            
            if(idFoto != null){
                Optional<Archivo> opArchivo = archivoRepositorio.findById(idFoto);
                if(opArchivo.isPresent()){
                    archivo = opArchivo.get();
            }
            archivo.setMime(foto.getContentType());
            archivo.setNombre(foto.getName());
            archivo.setContenido(foto.getBytes());
            
            archivoRepositorio.save(archivo);
        }else{
            throw new ErrorServicio("Error al actualizar la imagen");            
            }
        }
        return null;
        
    }
  
        
     public Archivo buscarPorId (String id) throws ErrorServicio {
        Optional<Archivo> res = archivoRepositorio.findById(id);
        if (!res.isPresent()) {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
        return res.get();
    }
        
    }
        
        
    


