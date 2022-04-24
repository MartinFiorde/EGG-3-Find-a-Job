package FindAJob.servicios;

import FindAJob.entidades.Archivo;
import FindAJob.excepciones.ErrorServicio;
import FindAJob.repositorios.ArchivoRepositorio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ArchivoServicio {
    
    
    private final ArchivoRepositorio archivoRepositorio;

    public ArchivoServicio(ArchivoRepositorio archivoRepositorio) {
        this.archivoRepositorio = archivoRepositorio;
    }
    
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
        
        
        
    }

