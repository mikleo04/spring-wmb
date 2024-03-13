package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.TransactionType;
import com.enigma.wmb_api.dto.response.TransTypeResponse;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repository.TransTypeRepository;
import com.enigma.wmb_api.service.TransTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TransTypeServiceImplTest {

    @Mock
    private TransTypeRepository transTypeRepository;

    TransTypeService transTypeService;

    @BeforeEach
    void setUp() {
        transTypeService = new TransTypeServiceImpl(transTypeRepository);
    }

    @Test
    void shouldReturnTransTypeResponseWhenGetById() {
        // Given
        TransactionType id = TransactionType.EI;
        TransType transType = TransType.builder()
                .id(TransactionType.EI)
                .description("Eat In")
                .build();

        // stubbing
        Mockito.when(transTypeRepository.findById(id))
                .thenReturn(Optional.ofNullable(transType));

        //When
        TransTypeResponse actualTransTypeResponse = transTypeService.getById(id);

        //Then
        assertEquals(id, actualTransTypeResponse.getId());
    }

    @Test
    void shoulThrowResponseStatusExceptionWhenNotFound() {
        // Given
        TransactionType id = TransactionType.EI;

        // stubbing
        Mockito.when(transTypeRepository.findById(id))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction type not found"));

        //When //Then
       assertThrows(ResponseStatusException.class, () -> {
           transTypeService.getById(id);
        });
    }

    @Test
    void shouldReturnListTransTypeResponseWhenGetAll() {
        //Given
        List<TransType> transTypes = List.of(
                TransType.builder()
                        .id(TransactionType.EI)
                        .description("Eat In")
                        .build(),
                TransType.builder()
                        .id(TransactionType.TA)
                        .description("Take Away")
                        .build()
        );

        // stubbing
        Mockito.when(transTypeRepository.findAll())
                .thenReturn(transTypes);

        //When
        List<TransTypeResponse> actualTransTypeResponses = transTypeService.getAll();

        //Then
        assertNotNull(actualTransTypeResponses);
        assertEquals(transTypes.size(), actualTransTypeResponses.size());
    }
}