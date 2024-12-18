package com.yaeby.np_lab_2.controller;

import com.yaeby.np_lab_2.exception.ResourceNotFoundException;
import com.yaeby.np_lab_2.model.Car;
import com.yaeby.np_lab_2.response.ApiResponse;
import com.yaeby.np_lab_2.service.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/interauto")
@RequiredArgsConstructor
public class CarController {

    private final ICarService carService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCars(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Car> carsPage = carService.getCars(pageable);
            return ResponseEntity.ok(new ApiResponse("Success", carsPage));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<ApiResponse> getCarById(@PathVariable Long id) {
        try {
            Car car = carService.getCarById(id);
            return ResponseEntity.ok(new ApiResponse("Success", car));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/car/add")
    public ResponseEntity<ApiResponse> addCar(@RequestBody Car car) {
        try {
            Car theCar = carService.addCar(car);
            return ResponseEntity.ok(new ApiResponse("Success", theCar));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/cars/add")
    public ResponseEntity<ApiResponse> addCars(@RequestBody List<Car> cars) {
        try {
            List<Car> theCars = carService.addCars(cars);
            return ResponseEntity.ok(new ApiResponse("Success", theCars));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("/car/{id}/update")
    public ResponseEntity<ApiResponse> updateCar(@RequestBody Car car, @PathVariable Long id) {
        try {
            Car theCar = carService.updateCar(car, id);
            return ResponseEntity.ok(new ApiResponse("Success", theCar));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/car/{id}/delete")
    public ResponseEntity<ApiResponse> deleteCar(@PathVariable Long id) {
        try {
            carService.deleteCar(id);
            return ResponseEntity.ok(new ApiResponse("Success", id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
