package com.yaeby.np_lab_2.service;

import com.yaeby.np_lab_2.exception.ResourceNotFoundException;
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
        return carRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
    }

    @Override
    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    @Override
    public List<Car> addCars(List<Car> cars) {
        return carRepository.saveAll(cars);
    }

    @Override
    public Car updateCar(Car car, Long id) {
        return carRepository.findById(id)
                .map(existingCar -> updateExistingCar(existingCar, car))
                .map(carRepository::save)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
    }

    private Car updateExistingCar(Car existingCar, Car car) {
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
        return existingCar;
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.findById(id)
                .ifPresentOrElse(carRepository::delete,
                        () -> {throw new ResourceNotFoundException("Car not found");});
    }
}
