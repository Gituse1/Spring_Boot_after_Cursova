package com.example.service;

import com.example.dto.Request.TripRequest;
import com.example.model.Bus;
import com.example.model.Route;
import com.example.model.Trip;
import com.example.repository.BusRepository;
import com.example.repository.RouteRepository;
import com.example.repository.TripRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private  TripRepository tripRepository;

    @Mock
    private  RouteRepository routeRepository;

    @Mock
    private  BusRepository busRepository;

    @Mock
    private  AuditService auditService;

    @InjectMocks
    private  TripService tripService;


    @BeforeEach
    public void before(){
        lenient().doNothing().when(auditService).createNewLog(any(), anyBoolean());
        lenient().doNothing().when(auditService).createNewLog(any(),anyBoolean(),any(),anyString());
    }

    @Test
    public void createTripTest(){
         Long testRouteId=2L;
         Long testBusId=5L;
         LocalTime startTime=LocalTime.of(9,0);
         LocalTime endTime = LocalTime.of(20,0);

        Route route = Route.builder()
                .idRoute(testRouteId)
                .nameRoute("TestName")
                .startTime(startTime)
                .endTime(endTime)
                .build();

        Bus bus = Bus.builder()
                .idBus(testBusId)
                .plateNumber("testPlate")
                .totalSeats(32)
                .build();

        TripRequest tripRequest = TripRequest.builder().routeId(testRouteId).busId(testBusId).departureTime(LocalDateTime.now()).build();

         when(routeRepository.findById(tripRequest.getRouteId())).thenReturn(Optional.ofNullable(route));
         when(busRepository.findById(tripRequest.getBusId())).thenReturn(Optional.ofNullable(bus));
         when(tripRepository.checkBusIsBusy(eq(testBusId), any(), any())).thenReturn(false);



        tripService.createTrip(tripRequest);

        verify(tripRepository).save(any());

    }
    @ParameterizedTest
    @MethodSource("provideTripsForTest")
    void tripProcessingTest(TripRequest tripRequest,boolean shouldBeBusy) {

        lenient().when(routeRepository.findById(tripRequest.getRouteId())).thenReturn(Optional.ofNullable(new Route()));

        lenient(). when(busRepository.findById(tripRequest.getBusId())).thenReturn(Optional.ofNullable(new Bus()));

        when(tripRepository.checkBusIsBusy(anyLong(), any(), any())).thenReturn(shouldBeBusy);

        if(shouldBeBusy){
            assertThrows(IllegalArgumentException.class ,()->{
                tripService.createTrip(tripRequest);
            });

            verify(tripRepository,never()).save(any());
        }
        else {
            tripService.createTrip(tripRequest);
            verify(tripRepository).save(any());
        }
    }


    static Stream<Arguments> provideTripsForTest() {
        LocalDateTime testDateTime1 = LocalDateTime.of(2020, 2, 1, 4, 2);
        LocalDateTime testDateTime2 = LocalDateTime.of(2021,2,1,4,2);

        TripRequest trip1 = TripRequest.builder()
                .routeId(1L)
                .busId(-5L)
                .departureTime(testDateTime1)
                .build();

        TripRequest trip2 = TripRequest.builder()
                .routeId(-1L)
                .busId(2L)
                .departureTime(testDateTime2)
                .build();

        return Stream.of(
                Arguments.of(trip1, true),
                Arguments.of(trip2, false)
        );
    }


    @Test
    public void searchTripsTest(){

        Trip mockTrip = new Trip();
        List<Trip> mockResult = List.of(mockTrip);

        when(tripRepository.findTripsByRouteAndDate(eq(1L), eq(2L), any(LocalDate.class)))
                .thenReturn(mockResult);

        List<Trip> actual = tripService.searchTrips(1L,2L,"2025-01-20");
        assertEquals(mockResult, actual);

        verify(tripRepository).findTripsByRouteAndDate(eq(1L), eq(2L), any(LocalDate.class));

    }


    @ParameterizedTest
    @ValueSource(strings = {"invalid-date", "20-12-2025", "", "   ", "2025.01.20"})
    void searchTrips_ThrowException(String invalidDateStr) {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            tripService.searchTrips(1L, 2L, invalidDateStr);
        });
    }


    @ParameterizedTest
    @CsvSource({
            "1, 2, '2025-01-20'", // Звичайний рейс
            "5, 10, '2025-02-15'", // Інший напрямок
            "0, 0, '2025-12-31'"   // Граничні значення
    })
    void searchTrips_ShouldCallRepositoryWithCorrectIds(Long fromId, Long toId, String date) {
        tripService.searchTrips(fromId, toId, date);

        verify(tripRepository).findTripsByRouteAndDate(eq(fromId), eq(toId), any(LocalDate.class));
    }


}