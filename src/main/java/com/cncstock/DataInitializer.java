package com.cncstock;

import com.cncstock.model.StockItem;
import com.cncstock.repository.StockItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final StockItemRepository stockItemRepository;

    @Autowired
    public DataInitializer(StockItemRepository stockItemRepository) {
        this.stockItemRepository = stockItemRepository;
    }

    @Override
    public void run(String... args) {

        // Data for 20 items in the stock table
        Object[][] itemsData = {
                {1, "CNMG Carbide Insert", "ToolTech", "ToolMaster", 10, "Carbide insert for turning operations", "Turning", new String[]{"CNMG"}},
                {2, "DNMG Carbide Insert", "CNC Tools", "CNC Mart", 8, "Carbide insert for turning operations", "Turning", new String[]{"DNMG"}},
                {3, "Solid Carbide End Mill", "EndMaster", "EndMill Supplies", 15, "High-performance end mill for milling operations", "Milling", new String[]{"M"}},
                {4, "HSS Twist Drill Bit", "DrillPro", "DrillBits Inc.", 20, "High-speed steel twist drill bit", "Drilling", new String[]{"K", "S"}},
                {5, "Indexable Face Mill", "FaceMaster", "ToolZone", 6, "Indexable face mill for milling applications", "Milling", new String[]{"M", "S"}},
                {6, "Boring Bar", "BoreTech", "CNC Tools", 8, "Boring bar for internal turning operations", "Turning", new String[]{"K", "P"}},
                {7, "Milling Cutter", "MillCraft", "ToolGuru", 10, "Milling cutter for precise milling", "Milling", new String[]{"M", "S"}},
                {8, "Drill Chuck", "DrillMaster", "DrillingSupplies", 12, "Keyed drill chuck for holding drill bits", "Drilling", new String[]{"M", "K"}},
                {9, "Thread Turning Insert", "ThreadTech", "ThreadTools", 7, "Insert for threading operations", "Turning", new String[]{"K", "S"}},
                {10, "Spotting Drill", "SpotOn", "SpotDrills", 5, "Spotting drill for centering holes", "Drilling", new String[]{"P"}},
                {11, "Grooving Tool", "GrooveMaster", "ToolMart", 9, "Tool for grooving and parting off", "Turning", new String[]{"M", "K"}},
                {12, "Face Grooving Insert", "GrooveTech", "ToolPro", 6, "Insert for face grooving operations", "Turning", new String[]{"M", "S"}},
                {13, "Solid Carbide Drill Bit", "CarbideDrill", "CarbideTools", 25, "Solid carbide drill bit for precision drilling", "Drilling", new String[]{"M"}},
                {14, "Threading Tap", "TapTech", "ToolZone", 11, "Threading tap for creating internal threads", "Turning", new String[]{"M", "S"}},
                {15, "Corner Rounding End Mill", "RoundingTech", "MillingTools", 8, "End mill for corner rounding", "Milling", new String[]{"P", "K"}},
                {16, "Indexable Turning Tool", "TurnMaster", "ToolGuru", 20, "Indexable tool for turning operations", "Turning", new String[]{"M", "K"}},
                {17, "Spot Face Cutter", "SpotFace", "ToolPro", 6, "Cutter for spot facing operations", "Milling", new String[]{"M"}},
                {18, "CNC Lathe Tool Holder", "LatheMaster", "CNCMart", 15, "Tool holder for CNC lathe", "Turning", new String[]{"M", "K", "P", "S"}},
                {19, "Indexable Drill", "IndexDrill", "DrillBits Inc.", 10, "Indexable drill for various drilling tasks", "Drilling", new String[]{"M"}},
                {20, "Grooving Insert", "GrooveIt", "ToolZone", 12, "Insert for grooving operations", "Turning", new String[]{"K", "S"}}
        };

        for (Object[] itemData : itemsData) {
            StockItem stockItem = new StockItem();
            stockItem.setLocation((int) itemData[0]);
            stockItem.setTitle((String) itemData[1]);
            stockItem.setBrand((String) itemData[2]);
            stockItem.setSupplier((String) itemData[3]);
            stockItem.setMinQty((int) itemData[4]);
            stockItem.setDescription((String) itemData[5]);
            stockItem.setCategory((String) itemData[6]);
            stockItem.setMaterials((String[]) itemData[7]);

            stockItemRepository.save(stockItem);
        }


    }
}