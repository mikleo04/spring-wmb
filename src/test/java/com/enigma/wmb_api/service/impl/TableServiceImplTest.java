package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchTableRequest;
import com.enigma.wmb_api.dto.request.TableRequest;
import com.enigma.wmb_api.dto.response.TableResponse;
import com.enigma.wmb_api.entity.DiningTable;
import com.enigma.wmb_api.repository.TableRepository;
import com.enigma.wmb_api.service.TableService;
import com.enigma.wmb_api.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TableServiceImplTest {

    @Mock
    private TableRepository tableRepository;
    @Mock
    private ValidationUtil validationUtil;
    private TableService tableService;

    @BeforeEach
    void setUp() {
        tableService = new TableServiceImpl(tableRepository, validationUtil);
    }

    @Test
    void shouldReturnTableResponseWhenCreate() {
        //Given
        TableRequest request = TableRequest.builder()
                .id("Table-01")
                .name("T01")
                .build();

        DiningTable table = DiningTable.builder()
                .id("Table-01")
                .name("T01")
                .build();

        //stubbing
        validationUtil.validate(request);
        Mockito.when(tableRepository.saveAndFlush(Mockito.any(DiningTable.class)))
                .thenReturn(table);

        //When
        TableResponse actualTableresponse = tableService.creat(request);

        //Then
        assertEquals(request.getName(), actualTableresponse.getName());
    }

    @Test
    void shouldReturnTableResponseWhenGetById() {
        //Given
        String id = "Table-01";
        DiningTable table = DiningTable.builder()
                .id("Table-01")
                .name("T01")
                .build();

        //stubbing
        Mockito.when(tableRepository.findById(id))
                .thenReturn(Optional.ofNullable(table));

        //When
        TableResponse actualTableResponse = tableService.getById(id);

        //Then
        assertEquals(id, actualTableResponse.getId());
    }

    @Test
    void shoulThrowResponseStatusExceptionWhenNotFound() {
        //Given
        String id = "Table-01";

        //stubbing
        Mockito.when(tableRepository.findById(id))
                .thenThrow(ResponseStatusException.class);

        //When//Then
        assertThrows(ResponseStatusException.class, () -> {
            tableService.getById(id);
        });
    }

    @Test
    void shouldReturnListTableResponseWhenGetAll() {
        //Given
        List<DiningTable> diningTables = List.of(
                DiningTable.builder()
                        .id("Table-01")
                        .name("T01")
                        .build(),
                DiningTable.builder()
                        .id("Table-02")
                        .name("T02")
                        .build()
        );

        SearchTableRequest tableRequest = SearchTableRequest.builder()
                .direction("ASC")
                .size(10)
                .page(1)
                .sortBy("name")
                .build();

        Page<DiningTable> returnTable = new PageImpl<>(diningTables);

        //Stubbing
        Mockito.when(tableRepository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(returnTable);

        //When
        Page<TableResponse> actualTableResponses = tableService.getAll(tableRequest);

        //Then
        assertEquals(returnTable.getSize(), actualTableResponses.getSize());
        assertNotNull(actualTableResponses);
    }

    @Test
    void shouldCalldeleteOnce() {
        //Given
        String id = "Table-01";

        DiningTable returnTable = DiningTable.builder()
                .id("Table-02")
                .name("T02")
                .build();

        //Stubbing
        Mockito.when(tableRepository.findById(id)).thenReturn(Optional.ofNullable(returnTable));
        Mockito.doNothing().when(tableRepository).deleteById(id);

        //When
        tableService.delete(id);

        //Then
        Mockito.verify(tableRepository, Mockito.times(1)).deleteById(id);
    }
}