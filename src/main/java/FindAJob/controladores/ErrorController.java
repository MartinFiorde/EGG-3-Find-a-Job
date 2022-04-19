package FindAJob.controladores;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ErrorController extends ResponseEntityExceptionHandler {
    
//    @ExceptionHandler(Not.class)
//    public ResponseEntity<Object> handleCityNotFoundException(
//        CityNotFoundException ex, WebRequest request) {
//
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("message", "City not found");
//
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }
    
}
