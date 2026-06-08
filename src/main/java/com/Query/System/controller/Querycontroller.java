package com.Query.System.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
public class Querycontroller {
  
  @GetMapping("/")
  public String startAPI(){
    return "index.html";
  }

}
