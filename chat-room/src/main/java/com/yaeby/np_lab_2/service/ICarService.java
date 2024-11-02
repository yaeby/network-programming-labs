package com.yaeby.np_lab_2.service;

import com.yaeby.np_lab_2.model.Car;

import java.util.List;

public interface ICarService {
    List<Car> getCars();
    Car getCarById(Long id);
    Car addCar(Car car);

    List<Car> addCars(List<Car> cars);

    Car updateCar(Car car, Long id);

    void deleteCar(Long id);
}
