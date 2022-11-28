package ie.eoin.controllers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public void messageNotReadableException(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),"JSON Format is malformed");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolationException(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.CONFLICT.value(),"Confliction causing request");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void dataExistsInDatabase(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.CONFLICT.value(),"Object already exists in the database.");
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public void dataDoesNotExist(HttpServletResponse response) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(),"Object does not exist in the database.");
    }
}
