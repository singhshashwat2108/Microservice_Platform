package com.Query.System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class Querycontroller {
  
  @GetMapping("/")
  public String startAPI(){
    return "index.html";
  }

  @PostMapping("/login")
  public void login(){
    
  }
}
