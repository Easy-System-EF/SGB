package model.exception;
/*
 * msg de erros na defesa da digitação
 */
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

 	private static final long serialVersionUID = 1L;

 // coleção contendo erros - key + vlr (cpo e msg erro)
 	private Map<String, String> erros = new HashMap<>();
 	
 	public ValidationException(String msg) {
 		super(msg);
 	}
 	public Map<String, String> getErros() {
 		return erros;
 	}
 	
 	public void addErros(String fieldName, String errorMessage) {
 		erros.put(fieldName, errorMessage);
 	}
}
