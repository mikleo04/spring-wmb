package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.dto.request.TransactionRequest;
import com.enigma.wmb_api.dto.request.UpdateStatusTransactionRequest;
import com.enigma.wmb_api.dto.response.*;
import com.enigma.wmb_api.entity.*;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.repository.TransactionRepository;
import com.enigma.wmb_api.service.*;
import com.enigma.wmb_api.specification.TransactionSpecification;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final CustomerService customerService;
    private final TableService tableService;
    private final TransTypeService transTypeService;
    private final TransactionDetailService transactionDetailService;
    private final MenuService menuService;
    private final PaymentService paymentService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public TransactionResponse create(TransactionRequest request) {

        Customer customer = customerService.getById(request.getCustomerId());

        // table validation
        DiningTable table = null;
        if (request.getTableId()  != null) {
            TableResponse tableResponse = tableService.getById(request.getTableId());
            table = DiningTable.builder()
                    .id(tableResponse.getId())
                    .name(tableResponse.getName())
                    .build();
        }

        // save transaction
        TransTypeResponse transTypeResponse = transTypeService.getById(request.getTransTypeid());
        TransType transType = TransType.builder()
                .id(transTypeResponse.getId())
                .description(transTypeResponse.getDescription())
                .build();

        Transaction trx = Transaction.builder()
                .customer(customer)
                .transDate(new Date())
                .table(table)
                .transType(transType)
                .build();
        repository.saveAndFlush(trx);

        //save transactionDetail
        List<TransactionDetail> trxDetails = request.getDetailRequests().stream()
                .map(detailRequest -> {

                    MenuResponse menuResponse = menuService.getOneById(detailRequest.getMenuId());

                    Menu menu = Menu.builder()
                            .id(menuResponse.getId())
                            .name(menuResponse.getName())
                            .price(menuResponse.getPrice())
                            .status(menuResponse.getStatus())
                            .build();

                    return TransactionDetail.builder()
                            .transaction(trx)
                            .menu(menu)
                            .menuPrice(menu.getPrice())
                            .quantity(detailRequest.getQuantity())
                            .build();
                }).toList();

        transactionDetailService.createBulk(trxDetails);
        trx.setTransactionDetails(trxDetails);

        Payment payment = paymentService.createPayment(trx);
        trx.setPayment(payment);

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .id(payment.getId())
                .token(payment.getToken())
                .transactionStatus(payment.getTransactionStatus())
                .redirectUrl(payment.getRedirectUrl())
                .build();

        List<TransactionDetailResponse> trxDetailResponse =trxDetails.stream()
                .map(trxDetail -> {
                    return TransactionDetailResponse.builder()
                            .id(trxDetail.getId())
                            .menuId(trxDetail.getMenu().getId())
                            .menuPrice(trxDetail.getMenuPrice())
                            .quantity(trxDetail.getQuantity())
                            .build();
                }).toList();

        // table validation
        String tableResponse = null;
        if (trx.getTable() != null) {
            tableResponse = trx.getTable().getId();
        }

        return TransactionResponse.builder()
                .id(trx.getId())
                .customerId(trx.getCustomer().getId())
                .tableId(tableResponse)
                .transTypeId(trx.getTransType().getId())
                .detailTransaction(trxDetailResponse)
                .transDate(trx.getTransDate())
                .payment(paymentResponse)
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<TransactionResponse> getAll(SearchTransactionRequest request) {

        if (request.getPage() <= 0) request.setPage(1);
        Sort sorting = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), sorting);

        Specification<Transaction> specification = TransactionSpecification.getSpecification(request);

        Page<Transaction> transactions = repository.findAll(specification, pageable);

        return transactions.map(transaction -> {
            List<TransactionDetailResponse> transactionDetailResponses = transaction.getTransactionDetails().stream()
                    .map(transDetail -> {
                        return TransactionDetailResponse.builder()
                                .id(transDetail.getId())
                                .menuId(transDetail.getMenu().getId())
                                .menuPrice(transDetail.getMenuPrice())
                                .quantity(transDetail.getQuantity())
                                .build();
                    }).toList();

            Payment payment = transaction.getPayment();
            PaymentResponse paymentResponse = PaymentResponse.builder()
                    .id(payment.getId())
                    .token(payment.getToken())
                    .transactionStatus(payment.getTransactionStatus())
                    .redirectUrl(payment.getRedirectUrl())
                    .build();

            String tableId = null;
            if(transaction.getTable() != null) {
                tableId = transaction.getTable().getId();
            }

            return TransactionResponse.builder()
                    .id(transaction.getId())
                    .transTypeId(transaction.getTransType().getId())
                    .customerId(transaction.getCustomer().getId())
                    .transDate(transaction.getTransDate())
                    .tableId(tableId)
                    .detailTransaction(transactionDetailResponses)
                    .payment(paymentResponse)
                    .build();
        });
    }

    @Override
    public List<ReportResponse> getReportCsv(SearchTransactionRequest request) {
        Specification<Transaction> specification = TransactionSpecification.getSpecification(request);
        List<Transaction> transactions = repository.findAll(specification);

        List<ReportResponse> responses = new ArrayList<>();

        transactions.forEach(transaction -> {
            transaction.getTransactionDetails().forEach(transactionDetail -> {

                String tableName = null;

                if (transaction.getTable() != null) {
                    tableName = transaction.getTable().getName();
                }

                responses.add(ReportResponse.builder()
                        .id(transaction.getId())
                        .customer(transaction.getCustomer().getName())
                        .transType(transaction.getTransType().getId().toString())
                        .menu(transactionDetail.getMenu().getName())
                        .price(transactionDetail.getMenuPrice().toString())
                        .table(tableName)
                        .quantity(transactionDetail.getQuantity().toString())
                        .transactionStatus(transaction.getPayment().getTransactionStatus().toString())
                        .transDate(transaction.getTransDate().toString())
                        .build());
            });
        });
        return responses;
    }

    @Transactional(readOnly = true)
    @Override
    public TransactionResponse getById(String id) {

        Optional<Transaction> transaction = repository.findById(id);

        if (transaction.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found");

        List<TransactionDetailResponse> detailResponses = transaction.get().getTransactionDetails().stream()
                .map(transactionDetail -> {
                    return TransactionDetailResponse.builder()
                            .id(transactionDetail.getId())
                            .menuId(transactionDetail.getMenu().getId())
                            .menuPrice(transactionDetail.getMenuPrice())
                            .quantity(transactionDetail.getQuantity())
                            .build();
                }).toList();

        String tableId = null;
        if (transaction.get().getTable() != null) {
            tableId = transaction.get().getTable().getId();
        }

        return TransactionResponse.builder()
                .id(transaction.get().getId())
                .transDate(transaction.get().getTransDate())
                .transTypeId(transaction.get().getTransType().getId())
                .tableId(tableId)
                .detailTransaction(detailResponses)
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateStatus(UpdateStatusTransactionRequest request) {
        Transaction transaction = repository.findById(request.getOrderId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction not found")
        );

        Payment payment = transaction.getPayment();
        payment.setTransactionStatus(request.getTransactionStatus());
    }

    @Override
    public void getReportPdf(HttpServletResponse response, SearchTransactionRequest request) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.lightGray);

        Paragraph p = new Paragraph("List Transactions", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {3.5f, 3.5f, 3.0f, 1.5f, 1.0f, 2.0f, 1.5f, 2.0f, 1.5f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table, request);

        document.add(table);

        document.close();
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("ID", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Customer", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Table", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Menu", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Quantity", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Transaction Status", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table, SearchTransactionRequest request) {
        List<ReportResponse> reports = getReportCsv(request);

        for (ReportResponse report : reports) {
            table.addCell(String.valueOf(report.getId()));
            table.addCell(report.getTransDate());
            table.addCell(report.getCustomer());
            table.addCell(report.getTable());
            table.addCell(report.getTransType());
            table.addCell(report.getMenu());
            table.addCell(report.getQuantity());
            table.addCell(report.getPrice());
            table.addCell(report.getTransactionStatus());
        }
    }

}
