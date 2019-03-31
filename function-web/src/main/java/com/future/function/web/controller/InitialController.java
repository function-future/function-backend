package com.future.function.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Use {@code @RestController} and {@code @RequestMapping(value = API_PATH)}
 * annotations before class name
 */
@RestController
@RequestMapping(value = "/api/test")
public class InitialController {
  
  /**
   * Specify method for mapping.
   * <p>
   * Only when necessary, specify what the mapping should consume or produce.
   *
   * @return Object from function-web-model (response)
   */
  @GetMapping
  public String test() {
    
    return "Hello";
  }
  
}
