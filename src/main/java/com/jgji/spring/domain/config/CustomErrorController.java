package com.jgji.spring.domain.config;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomErrorController implements ErrorController {

    private static String IMAGE_PATH = "images/error/";
    
    @ExceptionHandler(Throwable.class)
    @GetMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String statusMsg = status.toString();
        
        HttpStatus httpStatus = HttpStatus.valueOf(Integer.valueOf(statusMsg));
        
        model.addAttribute("msg", statusMsg + " " + httpStatus.getReasonPhrase());
        model.addAttribute("src", IMAGE_PATH + statusMsg + ".jpg");
        
        return "thymeleaf/error";
    }
    
    @Override
    public String getErrorPath() {
        // TODO Auto-generated method stub
        return "/error";
    }

}
