package netology.diplom.filestorage.exception.advice;

import lombok.extern.slf4j.Slf4j;
import netology.diplom.filestorage.exception.FileNotFoundException;
import netology.diplom.filestorage.exception.IncorrectDataEntry;
import netology.diplom.filestorage.exception.InternalServerError;
import netology.diplom.filestorage.exception.UserNotFoundException;
import netology.diplom.filestorage.dto.ErrorMessageDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> fileNotFoundExceptionHandler(FileNotFoundException exc) {
        ErrorMessageDto msg = new ErrorMessageDto();
        msg.setMessage(exc.getMessage());
        msg.setId(exc.getId());
        log.error("(ERROR) Ошибка: {}, id: {}", exc.getMessage(), exc.getId());
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessageDto> fileNotFoundExceptionHandler(UserNotFoundException exc) {
        ErrorMessageDto msg = new ErrorMessageDto();
        msg.setMessage(exc.getMessage());
        msg.setId(exc.getId());
        log.error("(ERROR) Ошибка: {}, id: {}", exc.getMessage(), exc.getId());
        return new ResponseEntity<>(msg, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectDataEntry.class)
    public ResponseEntity<ErrorMessageDto> fileNotFoundExceptionHandler(IncorrectDataEntry exc) {
        ErrorMessageDto msg = new ErrorMessageDto();
        msg.setMessage(exc.getMessage());
        msg.setId(exc.getId());
        log.error("(ERROR) Ошибка: {}, id: {}", exc.getMessage(), exc.getId());
        return new ResponseEntity<>(msg, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<ErrorMessageDto> internalServerErrorHandler(InternalServerError exc) {
        ErrorMessageDto msg = new ErrorMessageDto();
        msg.setMessage(exc.getMessage());
        msg.setId(exc.getId());
        log.error("(ERROR) Ошибка: {}, id: {}", exc.getMessage(), exc.getId());
        return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
