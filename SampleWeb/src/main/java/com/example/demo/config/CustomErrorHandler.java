package com.example.demo.config;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class CustomErrorHandler {

    /**
     * 403エラー（ページが見つからない場合）の処理
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute("message", "お探しのページは存在しないか、削除された可能性があります。");
        return "error/403"; // 403.html を返す
    }

    /**
     * 500エラー（サーバーエラー）の処理
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public String handleServerError(Exception ex, Model model) {
        model.addAttribute("message", "申し訳ありません。サーバーでエラーが発生しました。");
        return "error/500"; // 500.html を返す
    }
}
