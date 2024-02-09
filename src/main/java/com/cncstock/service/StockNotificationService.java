package com.cncstock.service;

import com.cncstock.model.dto.StockItemDTO;
import com.cncstock.model.dto.SupplierStockItemsDto;
import com.cncstock.model.entity.stockitem.LowStockItem;
import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.repository.PurchaseRequestRepository;
import com.cncstock.repository.stockitem.LowStockItemRepository;
import com.cncstock.repository.stockitem.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StockNotificationService {

    private final LowStockItemRepository lowStockItemRepository;
    private final StockItemRepository stockItemRepository;
    private final StockCheckingService stockCheckingService;
    private final StockItemService stockItemService;
    private final EmailService emailService;
    private final String[] EMAIL_LIST = {"andy.stevens@gerickegroup.com", "pavelnaumovic@gmail.com"};
    private final String[] CRHOLLANDS_EMAIL_LIST = {"graham.brown@crhollands.co.uk","pavelnaumovic@gmail.com"};

    @Autowired
    public StockNotificationService(LowStockItemRepository lowStockItemRepository, StockItemRepository stockItemRepository, StockCheckingService stockCheckingService, StockItemService stockItemService, EmailService emailService) {
        this.lowStockItemRepository = lowStockItemRepository;
        this.stockItemRepository = stockItemRepository;
        this.stockCheckingService = stockCheckingService;
        this.stockItemService = stockItemService;
        this.emailService = emailService;
    }

    public List<SupplierStockItemsDto> findTodaysItemsGroupedBySupplier() {
        Optional<List<String>> uniqueSuppliersListForToday = lowStockItemRepository.findUniqueSuppliersForItemsAddedToday();
        Optional<List<LowStockItem>> itemsAddedToday = lowStockItemRepository.findAllItemsAddedToday();
        List<SupplierStockItemsDto> supplierStockItemsDtoList = new ArrayList<>();

        if (uniqueSuppliersListForToday.isPresent() && itemsAddedToday.isPresent()) {
            for (String supplier : uniqueSuppliersListForToday.get()) {
                SupplierStockItemsDto supplierStockItemsDto = new SupplierStockItemsDto();
                supplierStockItemsDto.setSupplier(supplier);
                List<StockItemDTO> stockItemDTOS = new ArrayList<>();
                for (LowStockItem lowStockItem : itemsAddedToday.get()) {
                    String lowStockItemSupplier = lowStockItem.getStockItem().getSupplier().toLowerCase();
                    if (lowStockItemSupplier.equals(supplier.toLowerCase())) {
                        StockItemDTO stockItemDTO = stockItemService.toDTO(lowStockItem.getStockItem());
                        stockItemDTOS.add(stockItemDTO);
                    }
                }
                supplierStockItemsDto.setStockItemDTO(stockItemDTOS);
                supplierStockItemsDtoList.add(supplierStockItemsDto);
            }
        }

        return supplierStockItemsDtoList;
    }

    @Scheduled(cron = "0 0 15 ? * MON-FRI")
    public void stockTakeAndEmail() {
        stockCheckingService.checkItemStock();
        String requiredItems = printStockItemsBySupplier();
        String emailSubject = "Low Stock Items";
        for (String email : EMAIL_LIST) {
            emailService.sendEmailWithSignature(email, emailSubject, requiredItems);
        }
    }

    @Scheduled(cron = "0 0 9 ? * MON")
    public void emailCRHConstockLevel() {
        List<StockItem> constockItemListBySupplier = stockItemRepository.findBySupplierIgnoreCaseAndIsConstantStockTrue("crhollands");
        String stockItemsString = buildStockItemsString(constockItemListBySupplier);
        String emailSubject = "RotaVal CRHollands ConStock Levels";
        for (String email : CRHOLLANDS_EMAIL_LIST) {
            emailService.sendEmailWithSignature(email, emailSubject, stockItemsString);
        }
    }

    public String printStockItemsBySupplier() {
        List<SupplierStockItemsDto> supplierStockItemsDtoList = findTodaysItemsGroupedBySupplier();
        StringBuilder builder = new StringBuilder();

        for (SupplierStockItemsDto itemsDto : supplierStockItemsDtoList) {
            String supplierAndItemsList = buildSupplierStockItemsString(itemsDto);
            builder.append(supplierAndItemsList);
        }

        return builder.toString();
    }

    private String buildSupplierStockItemsString(SupplierStockItemsDto supplierStockItemsDto) {
        //Find a list of unique subcategories
        Set<String> uniqueSubCategories = new HashSet<>();
        for (StockItemDTO stockItemDTO : supplierStockItemsDto.getStockItemDTO()) {
            String subCatName = stockItemDTO.getSubCategoryName();
            uniqueSubCategories.add(subCatName);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<h1>").append(supplierStockItemsDto.getSupplier()).append("</h1>");

        for (String subCategoryName : uniqueSubCategories) {
            builder.append("<h4>").append(subCategoryName).append("</h4>");
            builder.append("<ul>");
            for (StockItemDTO stockItemDTO : supplierStockItemsDto.getStockItemDTO()) {
                if (subCategoryName.equalsIgnoreCase(stockItemDTO.getSubCategoryName())) {
                    builder.append("<ul>");
                    builder.append("<li>");
                    builder.append(stockItemDTO.getTitle()).append(" ");
                    builder.append(stockItemDTO.getBrand());
                    builder.append(" (Items left: ").append(stockItemDTO.getStockQty());
                    builder.append(" Box: ").append(stockItemDTO.getLocation());
                    builder.append(" Usual restock Qty:").append(stockItemDTO.getRestockQty()).append(") ");
                    builder.append(stockItemDTO.getSupplierItemCode());
                    builder.append("</li>");
                    builder.append("</ul>");
                }
            }
            builder.append("</ul>");
        }
        builder.append("<hr/>");

        return builder.toString();
    }

    private String buildStockItemsString(List<StockItem> stockItemList) {
        // Find a list of unique subcategories
        Set<String> uniqueSubCategories = new HashSet<>();
        for (StockItem stockItem : stockItemList) {
            StockItemDTO stockItemDTO = stockItemService.toDTO(stockItem);
            String subCatName = stockItemDTO.getSubCategoryName();
            uniqueSubCategories.add(subCatName);
        }

        StringBuilder builder = new StringBuilder();

        builder.append("<style>")
                .append("table { border-collapse: collapse; }")
                .append("table, th, td { border: 1px solid black; }") // This ensures all tables, th, and td elements have a border
                .append("tr.checked { text-decoration: line-through; }") // Class to be applied when checkbox is checked
                .append("</style>");

        // Start of the HTML table
        builder.append("<table border='1'>"); // Add styling as needed

        // Headers for the table
        builder.append("<tr>");
        builder.append("<th>Subcategory</th>");
        builder.append("<th>Title</th>");
        builder.append("<th>Brand</th>");
        builder.append("<th>Quantity</th>");
        builder.append("<th>Location</th>");
        builder.append("</tr>");

        // Iterate over each unique subcategory
        for (String subCategoryName : uniqueSubCategories) {
            // Check if we have added subcategory name for this iteration
            boolean isSubCategoryAdded = false;

            // Iterate over each stock item
            for (StockItem stockItem : stockItemList) {
                StockItemDTO stockItemDTO = stockItemService.toDTO(stockItem);
                // Check if the stock item belongs to the current subcategory
                if (subCategoryName.equalsIgnoreCase(stockItemDTO.getSubCategoryName())) {
                    builder.append("<tr>");

                    // Add subcategory name to the first column only if it has not been added yet
                    if (!isSubCategoryAdded) {
                        builder.append("<td>").append(subCategoryName).append("</td>");
                        isSubCategoryAdded = true;
                    } else {
                        builder.append("<td></td>"); // Subsequent rows do not repeat the subcategory name
                    }

                    // Add item details
                    builder.append("<td>").append(stockItemDTO.getTitle()).append("</td>");
                    builder.append("<td>").append(stockItemDTO.getBrand()).append("</td>");
                    builder.append("<td>").append(stockItemDTO.getStockQty()).append("</td>");
                    builder.append("<td>").append(stockItemDTO.getLocation()).append("</td>");
                    builder.append("</tr>");
                }
            }
        }

        // End of the HTML table
        builder.append("</table>");
        builder.append("<hr/>");

        return builder.toString();
    }



}
