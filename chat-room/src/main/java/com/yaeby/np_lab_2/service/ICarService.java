package com.yaeby.np_lab_2.service;

import com.yaeby.np_lab_2.model.Car;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICarService {
    Page<Car> getCars(Pageable pageable);
    Car getCarById(Long id);
    Car addCar(Car car);
    List<Car> addCars(List<Car> cars);
    Car updateCar(Car car, Long id);
    void deleteCar(Long id);
}
