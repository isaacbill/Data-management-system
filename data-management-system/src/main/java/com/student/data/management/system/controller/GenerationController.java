package com.student.data.management.system.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.student.data.management.system.service.ExcelGeneratorService;

import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GenerationController {

  private final ExcelGeneratorService generatorService;

  public GenerationController(ExcelGeneratorService generatorService) {
    this.generatorService = generatorService;
  }

  @PostMapping("/generate")
  public ResponseEntity<?> generate(@RequestParam @Min(1) @Max(2000000) long records) throws Exception {
    Path file = generatorService.generateExcel(records);
    return ResponseEntity.ok(Map.of(
        "message", "Excel generated",
        "path", file.toString()
    ));
  }
}