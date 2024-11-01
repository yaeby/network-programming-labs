package com.yaeby.np_lab_2.service;

import com.yaeby.np_lab_2.model.Car;
import com.yaeby.np_lab_2.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService implements ICarService {
    private final CarRepository carRepository;

    @Override
    public List<Car> getCars() {
        return carRepository.findAll();
    }

    @Override
    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    @Override
    public void addCar(Car car) {
        carRepository.save(car);
    }

    @Override
    public void updateCar(Car car, Long id) {
        Car existingCar = carRepository.findById(id).orElse(null);
        if (existingCar != null) {
            existingCar.setName(car.getName());
            existingCar.setLink(car.getLink());
            existingCar.setPrice(car.getPrice());
            existingCar.setPriceOld(car.getPriceOld());
            existingCar.setYear(car.getYear());
            existingCar.setMileage(car.getMileage());
            existingCar.setGearbox(car.getGearbox());
            existingCar.setFuel(car.getFuel());
            existingCar.setEngine(car.getEngine());
            existingCar.setPower(car.getPower());
            existingCar.setColor(car.getColor());
            existingCar.setTraction(car.getTraction());
            existingCar.setBodyType(car.getBodyType());
            existingCar.setSeats(car.getSeats());
            existingCar.setConsumption(car.getConsumption());
            carRepository.save(existingCar);
        }
    }

    @Override
    public void deleteCar(Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
        }
    }
}
