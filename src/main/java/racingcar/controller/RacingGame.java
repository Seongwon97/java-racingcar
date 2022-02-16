package racingcar.controller;

import racingcar.models.Car;
import racingcar.views.Input;
import racingcar.views.Output;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RacingGame {

    private final Input input;
    private final Output output;
    private final List<Car> cars = new ArrayList<>();
    private int repeats;

    public RacingGame(final Input input, final Output output) {
        this.input = input;
        this.output = output;
        repeats = 0;
    }

    public void startGame() {
        inputValidData();
        startRacing();
    }

    private void inputValidData() {
        createCar();
        repeats = input.inputRepeats();
    }

    private void createCar() {
        final List<String> carNames = input.inputValidNames();
        carNames.forEach((carName) -> cars.add(new Car(carName)));
    }

    private void startRacing() {
        output.printResultMessage();
        while (repeats-- > 0) {
            cars.forEach(Car::goForward);
            output.printTurnResult(cars);
        }
        output.printWinners(findWinner());
    }

    private String findWinner() {
        final int farthestPosition = findFarthestPosition();
        return getWinners(farthestPosition);
    }

    private int findFarthestPosition() {
        final List<Integer> sortedCars = cars.stream()
                .sorted(Comparator.comparing(Car::getPosition).reversed())
                .map(Car::getPosition)
                .collect(Collectors.toList());

        if (!sortedCars.isEmpty()) {
            return sortedCars.get(0);
        }
        return 0;
    }

    private String getWinners(final int farthestPosition) {
        return cars.stream()
                .filter((car) -> car.isWinner(farthestPosition))
                .map(Car::getName)
                .collect(Collectors.joining(", "));
    }
}
