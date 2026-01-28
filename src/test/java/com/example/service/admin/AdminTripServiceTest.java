package com.example.service.admin;

import com.example.dto.Request.TripRequest;
import com.example.model.Bus;
import com.example.model.Route;
import com.example.repository.BusRepository;
import com.example.repository.RouteRepository;
import com.example.repository.TripRepository;
import com.example.service.AuditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AdminTripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private BusRepository busRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private AdminTripService adminTripService;


    @BeforeEach
    public void before(){
        lenient().doNothing().when(auditService).log(any(), any());
        lenient().doNothing().when(auditService).log(any(),any(),anyString());
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



        adminTripService.createTrip(tripRequest);

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
                adminTripService.createTrip(tripRequest);
            });

            verify(tripRepository,never()).save(any());
        }
        else {
            adminTripService.createTrip(tripRequest);
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

}
