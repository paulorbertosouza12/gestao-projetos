package br.com.paulobarros.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(org.springframework.http.HttpStatus.BAD_REQUEST)
public class BusinessException  extends RuntimeException{

    public BusinessException(String message) {
        super(message);
    }
}
