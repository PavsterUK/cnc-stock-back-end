package com.cncstock;

import com.cncstock.model.entity.stockitem.StockItem;
import com.cncstock.model.entity.User;
import com.cncstock.model.entity.stockitem.StockItemCategory;
import com.cncstock.model.entity.stockitem.StockItemSubCategory;
import com.cncstock.repository.stockitem.StockItemCategoryRepository;
import com.cncstock.repository.stockitem.StockItemRepository;
import com.cncstock.repository.UserRepository;
import com.cncstock.repository.stockitem.StockItemSubCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
public class DataInitializer implements CommandLineRunner {


    private final StockItemRepository stockItemRepository;

    private final UserRepository userRepository;

    private final StockItemCategoryRepository stockItemCategoryRepository;

    private final StockItemSubCategoryRepository stockItemSubCategoryRepository;

    @Autowired
    public DataInitializer(StockItemRepository stockItemRepository, UserRepository userRepository, StockItemCategoryRepository stockItemCategoryRepository, StockItemSubCategoryRepository stockItemSubCategoryRepository) {
        this.stockItemRepository = stockItemRepository;
        this.userRepository = userRepository;
        this.stockItemCategoryRepository = stockItemCategoryRepository;
        this.stockItemSubCategoryRepository = stockItemSubCategoryRepository;
    }



    @Override
    public void run(String... args) {
        persistCategories();
        persistSubCategories();
        persistStockItems();
    }

    private void persistStockItems() {
        Object[][] itemsData = {
                {1, "CNMG Carbide Insert", "ToolTech", "ToolMaster", 10, "Carbide insert for turning operations", "Turning", new String[]{"K", "N", "H", "S"}, true, 40},
                {2, "DNMG Carbide Insert", "CNC Tools", "CNC Mart", 8, "Carbide insert for turning operations", "Turning", new String[]{"K", "N", "S"}, false, 25},
                {3, "Solid Carbide End Mill", "EndMaster", "EndMill Supplies", 15, "High-performance end mill for milling operations", "Milling", new String[]{"M"}, true, 50},
                {4, "HSS Twist Drill Bit", "DrillPro", "DrillBits Inc.", 20, "High-speed steel twist drill bit", "Hole Making", new String[]{"K", "S"}, false, 15},
                {5, "Indexable Face Mill", "FaceMaster", "ToolZone", 6, "Indexable face mill for milling applications", "Milling", new String[]{"M", "S"}, true, 30},
                {6, "Boring Bar", "BoreTech", "CNC Tools", 8, "Boring bar for internal turning operations", "Turning", new String[]{"K", "P"}, false, 18},
                {7, "Milling Cutter", "MillCraft", "ToolGuru", 10, "Milling cutter for precise milling", "Milling", new String[]{"M", "S"}, true, 20},
                {8, "Drill Chuck", "DrillMaster", "DrillingSupplies", 12, "Keyed drill chuck for holding drill bits", "Hole Making", new String[]{"M", "K"}, false, 22},
                {9, "Thread Turning Insert", "ThreadTech", "ThreadTools", 7, "Insert for threading operations", "Threading (Inserts, Taps, Thread Mills)", new String[]{"K", "S"}, true, 35},
                {10, "Spotting Drill", "SpotOn", "SpotDrills", 5, "Spotting drill for centering holes", "Hole Making", new String[]{"P"}, false, 28},
                {11, "Grooving Tool", "GrooveMaster", "ToolMart", 9, "Tool for grooving and parting off", "Turning", new String[]{"M", "K"}, true, 15},
                {12, "Face Grooving Insert", "GrooveTech", "ToolPro", 6, "Insert for face grooving operations", "Turning", new String[]{"M", "S"}, false, 17},
                {13, "Solid Carbide Drill Bit", "CarbideDrill", "CarbideTools", 25, "Solid carbide drill bit for precision drilling", "Hole Making", new String[]{"M"}, true, 12},
                {14, "Threading Tap", "TapTech", "ToolZone", 11, "Threading tap for creating internal threads", "Threading (Inserts, Taps, Thread Mills)", new String[]{"M", "S"}, false, 20},
                {15, "Corner Rounding End Mill", "RoundingTech", "MillingTools", 8, "End mill for corner rounding", "Milling", new String[]{"P", "K"}, true, 10},
                {16, "Indexable Turning Tool", "TurnMaster", "ToolGuru", 20, "Indexable tool for turning operations", "Turning", new String[]{"M", "K"}, false, 30},
                {17, "Spot Face Cutter", "SpotFace", "ToolPro", 6, "Cutter for spot facing operations", "Milling", new String[]{"M"}, true, 25},
                {18, "CNC Lathe Tool Holder", "LatheMaster", "CNCMart", 15, "Tool holder for CNC lathe", "Turning", new String[]{"M", "K", "P", "S"}, false, 18},
                {19, "Indexable Drill", "IndexDrill", "DrillBits Inc.", 10, "Indexable drill for various drilling tasks", "Hole Making", new String[]{"M"}, true, 35},
                {20, "Grooving Insert", "GrooveIt", "ToolZone", 12, "Insert for grooving operations", "Turning", new String[]{"K", "S"}, false, 14},
                {21, "Custom Milling Cutter", "CustomTools", "CustomTech", 5, "Custom milling cutter for specific applications", "Milling", new String[]{"P", "K", "M"}, true, 8},
                {22, "Thread Mill", "ThreadMillTech", "ThreadMasters", 7, "Thread mill for machining internal threads", "Threading (Inserts, Taps, Thread Mills)", new String[]{"S"}, false, 18},
                {23, "Center Drill", "CenterIt", "ToolZone", 15, "Center drill for creating accurate center holes", "Hole Making", new String[]{"P"}, true, 12},
                {24, "Parting Off Tool", "PartMaster", "ToolMart", 9, "Parting off tool for separating finished parts", "Turning", new String[]{"M", "K"}, false, 10},
                {25, "Face Milling Cutter", "FaceMillTech", "ToolGuru", 12, "Cutter for facing and milling operations", "Milling", new String[]{"M", "S"}, true, 22},
                {26, "Milling Insert", "MillInsert", "MillingSupplies", 20, "Insert for milling operations", "Milling", new String[]{"M", "K"}, false, 40},
                {27, "Spotting Center", "SpotCenter", "SpotDrills", 6, "Spotting center for accurate drilling", "Hole Making", new String[]{"P"}, true, 35},
                {28, "Thread Insert", "ThreadInsertTech", "ThreadTools", 8, "Insert for thread repair and reinforcement", "Threading (Inserts, Taps, Thread Mills)", new String[]{"K", "S"}, false, 18},
                {29, "Solid Carbide End Mill", "EndMaster", "EndMill Supplies", 15, "Solid carbide end mill for precise milling", "Milling", new String[]{"M"}, true, 30},
                {30, "Taper Shank Drill", "TaperDrill", "DrillPro", 10, "Taper shank drill bit for machining tapered holes", "Hole Making", new String[]{"K"}, false, 15},
                {31, "Boring Head", "BoreHead", "CNC Tools", 7, "Boring head for precision machining", "Turning", new String[]{"K", "P"}, true, 20},
                {32, "Corner Rounding Insert", "CornerRound", "ToolZone", 6, "Insert for corner rounding", "Milling", new String[]{"P", "K"}, false, 25},
                {33, "Solid Carbide Drill Bit", "CarbideDrill", "CarbideTools", 25, "Solid carbide drill bit for precision drilling", "Hole Making", new String[]{"M"}, true, 20},
                {34, "Threading Die", "ThreadingDie", "ToolZone", 11, "Threading die for creating external threads", "Threading (Inserts, Taps, Thread Mills)", new String[]{"M", "S"}, false, 30},
                {35, "Lathe Carbide Insert", "LatheCarbide", "LatheMasters", 8, "Carbide insert for lathe operations", "Turning", new String[]{"M", "K"}, true, 10},
                {36, "Face Mill Arbor", "FaceArbor", "ToolPro", 6, "Arbor for mounting face milling cutters", "Milling", new String[]{"M"}, false, 18},
                {37, "Indexable Grooving Tool", "GrooveMaster", "ToolMart", 10, "Indexable tool for grooving operations", "Turning", new String[]{"K", "S"}, true, 25},
                {38, "Countersink Bit", "Countersink", "ToolZone", 5, "Countersink bit for creating a chamfer on holes", "Hole Making", new String[]{"P"}, false, 28},
                {39, "Thread Milling Cutter", "ThreadMillTech", "ThreadMasters", 9, "Cutter for milling internal threads", "Threading (Inserts, Taps, Thread Mills)", new String[]{"S"}, true, 15},
                {40, "Spot Drill", "SpotDrill", "SpotDrills", 8, "Spot drill for accurate drilling", "Hole Making", new String[]{"P"}, false, 22},
                {41, "Carbide Threading Insert", "CarbideThread", "ThreadTools", 12, "Carbide insert for threading operations", "Threading (Inserts, Taps, Thread Mills)", new String[]{"K", "S"}, true, 18},
                {42, "End Mill Adapter", "EndMillAdapter", "ToolGuru", 7, "Adapter for mounting end mills", "Milling", new String[]{"M", "S"}, false, 25},
                {43, "Milling Insert Holder", "MillInsertHolder", "MillingSupplies", 15, "Holder for milling inserts", "Milling", new String[]{"M", "K"}, true, 30},
                {44, "Spotting Center Drill", "SpotCenterDrill", "SpotDrills", 10, "Spotting center drill for accurate centering", "Hole Making", new String[]{"P"}, false, 20},
                {45, "Thread Repair Kit", "ThreadRepair", "ThreadTools", 6, "Kit for repairing damaged threads", "Threading (Inserts, Taps, Thread Mills)", new String[]{"K", "S"}, true, 35},
                {46, "Solid Carbide Chamfer Mill", "ChamferMill", "MillingTools", 8, "Solid carbide mill for chamfering", "Milling", new String[]{"P", "K"}, false, 12},
                {47, "Carbide Boring Bar", "CarbideBore", "CNC Tools", 12, "Carbide boring bar for precision internal turning", "Turning", new String[]{"K", "P"}, true, 22},
                {48, "Threading Insert Holder", "ThreadInsertHolder", "ThreadTools", 7, "Holder for threading inserts", "Threading (Inserts, Taps, Thread Mills)", new String[]{"S"}, false, 18},
                {49, "Solid Carbide Drill Bit", "CarbideDrill", "CarbideTools", 25, "Solid carbide drill bit for precision drilling", "Hole Making", new String[]{"M"}, true, 10},
                {50, "Indexable Thread Mill", "ThreadMillTech", "ThreadMasters", 10, "Indexable tool for milling internal threads", "Threading (Inserts, Taps, Thread Mills)", new String[]{"S"}, false, 20},
        };


        Optional<StockItemCategory> optionalStockItemCategory = stockItemCategoryRepository.findById(1L);
        if (optionalStockItemCategory.isEmpty()) {
            throw new RuntimeException("No categories");
        }

        Optional<StockItemSubCategory> optionalStockItemSubCategory = stockItemSubCategoryRepository.findById(1L);
        if (optionalStockItemSubCategory.isEmpty()) {
            throw new RuntimeException("No sub categories");
        }


        for (Object[] itemData : itemsData) {
            StockItem stockItem = new StockItem();
            stockItem.setLocation((int) itemData[0]);
            stockItem.setTitle((String) itemData[1]);
            stockItem.setBrand((String) itemData[2]);
            stockItem.setSupplier((String) itemData[3]);
            stockItem.setMinQty((int) itemData[4]);
            stockItem.setDescription((String) itemData[5]);
            stockItem.setMaterials((String[]) itemData[7]);
            stockItem.setConstantStock((Boolean) itemData[8]);
            stockItem.setStockQty((Integer) itemData[9]);
            stockItem.setCategory(optionalStockItemCategory.get());
            stockItem.setSubCategory(optionalStockItemSubCategory.get());

            stockItemRepository.save(stockItem);
        }


        Object[][] users = {
                {107, "Pavel", "Naumovic", "1234"},
                {108, "Bob", "Marley", "1234"},
                {109, "Bill", "Jones", "1234"},
                {110, "Steve", "Dingham", "1234"},
                {111, "Gary", "Shearman", "1234"},
        };

        for (Object[] user : users) {
            User newUser = new User();
            newUser.setRotavalID((int) user[0]);
            newUser.setName((String) user[1]);
            newUser.setSurname((String) user[2]);
            newUser.setPassword((String) user[3]);

            userRepository.save(newUser);
        }
    }
    private void persistCategories() {
        String[] categories = {
                "All Categories",
                "Turning",
                "Milling",
                "Threading",
                "Hole Making",
                "Description",
                "Supplier",
                "Location",
                "Brand",
                "ConStock",
                "Spare Parts For Tools"};

        for (String category : categories) {
            StockItemCategory stockItemCategory = new StockItemCategory();
            stockItemCategory.setCategoryName(category);
            stockItemCategoryRepository.save(stockItemCategory);
        }

    }
    private void persistSubCategories() {
        String[] subCategories = {
                "All Sub Categories",
                "Face Mills",
                "Disc Mills",
                "Helical Mills",
                "Chamfer Mills",
                "High Feed Mills",
                "Plunge Mills",
                "Copy Mills",};

        for (String subCategory : subCategories ) {
            StockItemCategory stockItemCategory = stockItemCategoryRepository.findByCategoryName("Milling");
            StockItemSubCategory stockItemSubCategory = new StockItemSubCategory();
            stockItemSubCategory.setSubCategoryName(subCategory);
            stockItemSubCategory.setStockItemCategory(stockItemCategory);
            stockItemSubCategoryRepository.save(stockItemSubCategory);
        }
    }

}