package com.example.scdemo.storageservice;

import com.example.scdemo.helper.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    // Class helps us to act when exception occurs.
    // Here we track max file size exceed exception.
    // File size set in `application.properties`.
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        return new ResponseEntity<>(new ResponseMessage("File too large."), HttpStatus.EXPECTATION_FAILED);
    }
}
