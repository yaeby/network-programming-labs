package com.yaeby.np_lab_2.controller;

import com.yaeby.np_lab_2.exception.ResourceNotFoundException;
import com.yaeby.np_lab_2.model.Car;
import com.yaeby.np_lab_2.response.ApiResponse;
import com.yaeby.np_lab_2.service.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/interauto")
@RequiredArgsConstructor
public class CarController {

    private final ICarService carService;

//    @PostMapping("/multipart")
//    public ResponseEntity<ApiResponse> uploadMultipart(
//            @RequestParam("files") List<MultipartFile> files,
//            @RequestParam("otherData") String otherData) {
//        files.forEach(file -> {
//            System.out.println("Uploaded file: " + file.getOriginalFilename());
//        });
//
//        System.out.println("Other data: " + otherData);
//
//        return ResponseEntity.ok("Files uploaded successfully");
//    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> getAllCars() {
        try {
            List<Car> cars = carService.getCars();
            return ResponseEntity.ok(new ApiResponse("Success", cars));
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
