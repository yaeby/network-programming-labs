package com.yaeby.np_lab_2.service;

import com.yaeby.np_lab_2.model.Car;

import java.util.List;

public interface ICarService {
    List<Car> getCars();
    Car getCarById(Long id);
    void addCar(Car car);

    void updateCar(Car car, Long id);

    void deleteCar(Long id);
}
