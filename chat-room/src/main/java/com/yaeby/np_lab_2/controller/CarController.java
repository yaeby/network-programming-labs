package com.yaeby.np_lab_2.controller;

import com.yaeby.np_lab_2.model.Car;
import com.yaeby.np_lab_2.service.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/interauto")
@RequiredArgsConstructor
public class CarController {

    private final ICarService carService;

    @PostMapping("/multipart")
    public ResponseEntity<String> uploadMultipart(
            @RequestParam("files") List<MultipartFile> files,
            @RequestParam("otherData") String otherData) {
        files.forEach(file -> {
            System.out.println("Uploaded file: " + file.getOriginalFilename());
        });

        System.out.println("Other data: " + otherData);

        return ResponseEntity.ok("Files uploaded successfully");
    }

    @GetMapping("/cars/{}")
    public List<Car> getAllCars() {
        return carService.getCars();
    }

    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable Long id) {
        Car car = carService.getCarById(id);
        return car != null ? ResponseEntity.ok(car) : ResponseEntity.notFound().build();
    }

    @PostMapping("/cars/add")
    public ResponseEntity<Car> addCar(@RequestBody Car car) {
        carService.addCar(car);
        return ResponseEntity.ok(car);
    }

    @PutMapping("/cars/{id}/update")
    public ResponseEntity<Car> updateCar(@RequestBody Car car, @PathVariable Long id) {
        Car existingCar = carService.getCarById(id);
        if (existingCar != null) {
            carService.updateCar(car, id);
            return ResponseEntity.ok(car);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/cars/{id}/delete")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        if (carService.getCarById(id) != null) {
            carService.deleteCar(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
