package ru.kostyanoy.service.penaltyevent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kostyanoy.api.dto.ReportDto;
import ru.kostyanoy.entity.*;
import ru.kostyanoy.exception.InvalidParametersException;
import ru.kostyanoy.exception.ObjectNotFoundException;
import ru.kostyanoy.mapper.PenaltyEventMapper;
import ru.kostyanoy.mapper.ReportMapper;
import ru.kostyanoy.repository.car.CarRepository;
import ru.kostyanoy.repository.car.JdbcCarRepository;
import ru.kostyanoy.repository.fine.FineRepository;
import ru.kostyanoy.repository.fine.JdbcFineRepository;
import ru.kostyanoy.repository.penaltyevent.JdbcPenaltyEventRepository;
import ru.kostyanoy.repository.penaltyevent.PenaltyEventRepository;
import ru.kostyanoy.repository.statenumber.JdbcStateNumberRepository;
import ru.kostyanoy.repository.statenumber.StateNumberRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static ru.kostyanoy.service.validation.Validator.checkNotNull;
import static ru.kostyanoy.service.validation.Validator.checkNull;

public class DefaultPenaltyEventService implements PenaltyEventService {

    private static final DefaultPenaltyEventService INSTANCE = new DefaultPenaltyEventService();

    private final FineRepository fineRepository = JdbcFineRepository.getInstance();

    private final PenaltyEventRepository penaltyEventRepository = JdbcPenaltyEventRepository.getInstance();

    private final CarRepository carRepository = JdbcCarRepository.getInstance();

    private final StateNumberRepository stateNumberRepository = JdbcStateNumberRepository.getInstance();

    private final PenaltyEventMapper penaltyEventMapper = PenaltyEventMapper.getInstance();

    private final ReportMapper reportMapper = ReportMapper.getInstance();

    private static final Logger log = LoggerFactory.getLogger(DefaultPenaltyEventService.class);

    private static final String CONFIG_FILE_NAME = "/localization.properties";

    private static final StateNumberValidator validator = setLocalization();

    private DefaultPenaltyEventService() {

    }

    private static StateNumberValidator setLocalization() {
        Properties properties = new Properties();
        try (InputStream propertiesStream = DefaultPenaltyEventService.class.getResourceAsStream(CONFIG_FILE_NAME)) {
            properties.load(propertiesStream);
        } catch (IOException e) {
            log.error("Could not load properties", e);
            throw new RuntimeException(e);
        }
        if (properties.getProperty("stateNumberValidator") == null) {
            throw new RuntimeException("StateNumberValidator property has not specified");
        }

        if (properties.getProperty("stateNumberValidator").equals("rus")) {
            return new StateNumberValidatorRus();
        } else {
            throw new RuntimeException("Invalid stateNumberValidator property: " + properties.getProperty("stateNumberValidator"));
        }
    }

    public static PenaltyEventService getInstance() {
        return INSTANCE;
    }

    @Override
    public ru.kostyanoy.api.dto.PenaltyEventDto create(ru.kostyanoy.api.dto.PenaltyEventDto penaltyEventDto) {
        validate(penaltyEventDto);
        PenaltyEvent penaltyEvent = add(null, penaltyEventDto);
        return penaltyEventMapper.toDto(penaltyEvent);
    }

    @Override
    public ru.kostyanoy.api.dto.PenaltyEventDto getById(Long id) {
        checkNotNull("id", id);
        PenaltyEvent penaltyEvent = getPenaltyEvent(id);
        return penaltyEventMapper.toDto(penaltyEvent);
    }

    @Override
    public List<ru.kostyanoy.api.dto.ReportDto> get(String firstName, String middleName,
                                                    String lastName, String fullStateNumber) {
        StateNumber stateNumber = new StateNumber();
        if (stateNumber.setStateNumber(fullStateNumber, validator)) {
            Optional<StateNumber> newStateNumber = stateNumberRepository.get(stateNumber);
            if (newStateNumber.isPresent()) {
                List<PenaltyEvent> penaltyEvents = penaltyEventRepository.get(newStateNumber.get());
                if (penaltyEvents.isEmpty()) {
                    return new ArrayList<ReportDto>();
                }
                return reportMapper.toDto(penaltyEvents, validator);
            }
        }

        checkNotNull("lastName", lastName);
        checkNotNull("firstName", firstName);
        checkNotNull("middleName", middleName);
        if (lastName.isEmpty()) {
            throw new InvalidParametersException(String.format("Cannot find by the state number %s and entered owner last name",
                    fullStateNumber));
        }
        return reportMapper.toDto(getPenaltyEventsByOwnerName(firstName, middleName, lastName), validator);
    }

    @Override
    public ru.kostyanoy.api.dto.PenaltyEventDto merge(Long id, ru.kostyanoy.api.dto.PenaltyEventDto penaltyEventDto) {
        checkNotNull("id", id);
        validate(penaltyEventDto);

        PenaltyEvent penaltyEvent = penaltyEventRepository.getById(id)
                .map(existing -> update(existing, penaltyEventDto))
                .orElseGet(() -> add(id, penaltyEventDto));

        return penaltyEventMapper.toDto(penaltyEvent);
    }

    @Override
    public void delete(Long id) {
        checkNotNull("id", id);

        PenaltyEvent penaltyEvent = getPenaltyEvent(id);

        penaltyEventRepository.delete(penaltyEvent);
    }


    private void validate(ru.kostyanoy.api.dto.PenaltyEventDto penaltyEvent) {
        checkNull("penaltyEvent.id", penaltyEvent.getId());
        checkNotNull("penaltyEvent.timeStamp", penaltyEvent.getTimeStamp());
        checkNotNull("penaltyEvent.carID", penaltyEvent.getCarID());
        checkNotNull("penaltyEvent.FineID", penaltyEvent.getFineID());
    }

    private PenaltyEvent add(Long id, ru.kostyanoy.api.dto.PenaltyEventDto penaltyEventDto) {
        Car car = getCar(penaltyEventDto.getCarID());
        Fine fine = getFine(penaltyEventDto.getFineID());

        PenaltyEvent penaltyEvent = new PenaltyEvent();
        penaltyEvent.setId(id);
        penaltyEvent.setTimeStamp(penaltyEventDto.getTimeStamp());
        penaltyEvent.setCar(car);
        penaltyEvent.setFine(fine);

        penaltyEventRepository.add(penaltyEvent);

        return penaltyEvent;
    }

    private Fine getFine(Long id) {
        return fineRepository.getById(id)
                .orElseThrow(() -> new InvalidParametersException(String.format("Fine with id %s has not found", id)));
    }

    private Car getCar(Long id) {
        return carRepository.getById(id)
                .orElseThrow(() -> new InvalidParametersException(String.format("Car with id %s has not found", id)));
    }

    private PenaltyEvent getPenaltyEvent(Long id) {
        return penaltyEventRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("PenaltyEvent with id %s has not found", id)));
    }

    private List<PenaltyEvent> getPenaltyEventsByOwnerName(String firstName, String middleName, String lastName) {
        List<PenaltyEvent> penaltyEvents = penaltyEventRepository.get(firstName, middleName, lastName);
        if (penaltyEvents == null || penaltyEvents.isEmpty()) {
            throw new ObjectNotFoundException(String.format("PenaltyEvent for car owner %s %s %s has not found",
                    firstName, middleName, lastName));
        }
        return penaltyEvents;
    }

    private PenaltyEvent update(PenaltyEvent penaltyEvent, ru.kostyanoy.api.dto.PenaltyEventDto penaltyEventDto) {

        penaltyEvent.setTimeStamp(penaltyEventDto.getTimeStamp());

        if (!penaltyEventDto.getCarID().equals(penaltyEvent.getCar().getId())) {
            Car newCar = getCar(penaltyEventDto.getCarID());
            penaltyEvent.setCar(newCar);
        }
        if (!penaltyEventDto.getFineID().equals(penaltyEvent.getFine().getId())) {
            Fine newFine = getFine(penaltyEventDto.getFineID());
            penaltyEvent.setFine(newFine);
        }

        penaltyEventRepository.update(penaltyEvent);

        return penaltyEvent;
    }
}
