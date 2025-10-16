package handler;

import entity.AxoSparkResult;
import exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public AxoSparkResult exceptionHandler(BaseException ex){
        log.error("异常信息：{}", ex.getMessage());
        return AxoSparkResult.error(ex.getMessage());
    }
}
