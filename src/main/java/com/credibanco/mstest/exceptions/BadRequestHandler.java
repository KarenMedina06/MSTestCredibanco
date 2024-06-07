package com.credibanco.mstest.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.credibanco.mstest.dtos.ResponseDTO;

import brave.Tracer;


@ControllerAdvice
public class BadRequestHandler {

	@Autowired
	private Tracer tracer;
	
	@ExceptionHandler
	public ResponseEntity<ResponseDTO<String>> exceptionHandler(BadRequestException ex){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ResponseDTO<>(HttpStatus.BAD_REQUEST, ex.getMessage(), "Peticion Rechazada", tracer.currentSpan().context().traceIdString())
        );
	}
}
