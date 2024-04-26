package com.example.apigravadora.exception.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.apigravadora.exception.error.ApiErrorDetails;
import com.example.apigravadora.exception.error.ResourceNotFoundDetails;
import com.example.apigravadora.exception.error.ResourceNotFoundException;
import com.example.apigravadora.exception.error.ValidationErrorDetails;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class RestExceptionHandler{

    private final Logger log = LoggerFactory.getLogger(RestExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleSecurityException(RuntimeException ex) {

        ProblemDetail errorDetail = null;

        if (ex instanceof BadCredentialsException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(400), ex.getMessage());
            errorDetail.setProperty("mensagem", "Erro na autenticação.");
        }

        if (ex instanceof AccessDeniedException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
            errorDetail.setProperty("mensagem", "Usuário não autenticado");

            log.error(ex.getMessage(), ex);
        }

        if (ex instanceof JWTVerificationException) {
            errorDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatusCode.valueOf(401), ex.getMessage());
            errorDetail.setProperty("mensagem", "Usuário não autorizado!");
        }
        return errorDetail;
    }
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {

        ApiErrorDetails errorDetails = ApiErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.FORBIDDEN.value())
                .title("Access Denied")
                .detail("Access Denied")
                .developerMessage(ex.getMessage())
                .build();
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

        List<String> violations = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());

        ValidationErrorDetails violationDetails = ValidationErrorDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.BAD_REQUEST.value())
                .title("Constraint Violation Error")
                .detail("Constraint Violation Error")
                .developerMessage(ex.getClass().getName())
                .field("N/A")
                .fieldMessage(String.join(", ", violations))
                .build();
        return new ResponseEntity<>(violationDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException rfnException) {

        //Código para lidar com validação de argumento inválido
        ResourceNotFoundDetails rnfDetails = ResourceNotFoundDetails.Builder
                .newBuilder()
                .timestamp(new Date().getTime())
                .status(HttpStatus.NOT_FOUND.value())
                .title("Resource not found")
                .detail(rfnException.getMessage())
                .developerMessage(rfnException.getClass().getName())
                .build();
        return new ResponseEntity<>(rnfDetails, HttpStatus.NOT_FOUND);
    }

}
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException manvException, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//
//        //Código para lidar com validação de argumento inválido
//        List<FieldError> fieldErrors = manvException.getBindingResult().getFieldErrors();
//        String fields = fieldErrors.stream().map(FieldError::getField).collect(Collectors.joining(","));
//        String fieldMessages = fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(","));
//
//        ValidationErrorDetails rnfDetails = ValidationErrorDetails.Builder
//                .newBuilder()
//                .timestamp(new Date().getTime())
//                .status(HttpStatus.BAD_REQUEST.value())
//                .title("Field Validation Error")
//                .detail("Field Validation Error")
//                .developerMessage(manvException.getClass().getName())
//                .field(fields)
//                .fieldMessage(fieldMessages)
//                .build();
//        return new ResponseEntity<>(rnfDetails, HttpStatus.BAD_REQUEST);
//    }
//
//    @Override
//    public ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
//
//        //Código para lidar com validação de argumento inválido
//        ErrorDetails errorDetails = ErrorDetails.Builder
//                .newBuilder()
//                .timestamp(new Date().getTime())
//                .status(status.value())
//                .title("Internal Exception")
//                .detail(ex.getMessage())
//                .developerMessage(ex.getClass().getName())
//                .build();
//        return new ResponseEntity<>(errorDetails, headers, status);
//    }
//
//    @ExceptionHandler(NullPointerException.class)
//    protected ResponseEntity<Object> handleNullPointerException(NullPointerException ex, WebRequest request) {
//
//        ApiErrorDetails errorDetails = ApiErrorDetails.Builder
//                .newBuilder()
//                .timestamp(new Date().getTime())
//                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                .title("Internal Server Error")
//                .detail("NullPointerException occurred")
//                .developerMessage(ex.getMessage())
//                .build();
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }