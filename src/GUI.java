
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Dan
 */
public class GUI extends javax.swing.JFrame {

    /**
     *
     */
    public SetOfFarms theFarms;

    /**
     *
     */
    public SetOfFarmers theFarmers;

    /**
     *
     */
    public Data[] resultData;

    /**
     *
     */
    public ConnectionHandler handler;
    
    public Sensor sensor;

    /**
     *
     * @throws ParseException
     */
    public GUI() throws ParseException {
        initComponents();
        loadFarms();
        loadFarmers();
        populateCmbFarms();
    }

    private void loadFarmers() {
        try {
            ObjectInputStream farmersIn = new ObjectInputStream(new FileInputStream("tmp\\farmers.ser"));
            while (true) {
                try {
                    SetOfFarmers farmers = (SetOfFarmers) farmersIn.readObject();
                    theFarmers = farmers;
                } catch (EOFException e) {
                    break;
                } catch (IOException ex) {
                    return;
                } catch (ClassNotFoundException ex) {
                    System.out.println("SetOfFarmers class not found");
                    return;
                }
            }
            farmersIn.close();
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadFarms() {
        try {
            ObjectInputStream farmsIn = new ObjectInputStream(new FileInputStream("tmp\\farms.ser"));
            while (true) {
                try {
                    SetOfFarms farms = (SetOfFarms) farmsIn.readObject();
                    theFarms = farms;
                } catch (EOFException e) {
                    break;
                } catch (IOException ex) {
                    break;
                } catch (ClassNotFoundException ex) {
                    System.out.println("SetOfFarms class not found");
                    return;
                }
            }
            farmsIn.close();
        } catch (IOException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (theFarms == null) {
            theFarms = new SetOfFarms();
            theFarms.addFarm("Farm 1", new Location(0, 0, "Outdoors"), 0);
        }
    }

    private void save() {
        try {
            FileOutputStream fileOut = new FileOutputStream("tmp\\farmers.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(theFarmers);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        try {
            FileOutputStream fileOut1 = new FileOutputStream("tmp\\farms.ser");
            ObjectOutputStream out1 = new ObjectOutputStream(fileOut1);
            out1.writeObject(theFarms);
            out1.close();
            fileOut1.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Returns a farm by ID
     *
     * @param id
     * @return
     */
    public Farm selectFarm(int id) {
        Farm tmp = theFarms.getFarmByNumber(id);
        return tmp;
    }

    /**
     * Returns a field by ID
     *
     * @param farm
     * @param id
     * @return
     */
    public Field selectField(Farm farm, int id) {
        Field tmp = farm.getFieldByNumber(id);
        return tmp;
    }

    /**
     * Returns a fieldStation by ID
     *
     * @param field
     * @param id
     * @return
     */
    public FieldStation selectFieldStation(Field field, int id) {
        FieldStation tmp = field.getFieldStationByNumber(id);
        return tmp;
    }

    /**
     * Output the array of resultData in a formatted way (E.g Table, Graph)
     * based on the user requested output type
     *
     * Params: type (e.g. Graph, Table, etc)
     *
     * @param in
     */
    public void showResultsPopup(String in) {
    }

    /**
     * calls getSelectedFieldStationData(), if a single field station(s) is not
     * selected then walk down the tree of farms,fields,fieldStations to
     * retrieve the fieldSations.
     *
     * Once the data has been retrieved the selectedFarms/Fields/FieldStations
     * arrays would be cleared
     *
     * E.g If the user has selected to view rainfall data across the entire farm
     * the method would iterate over all the fields in that farm and add the
     * fieldStations within those fields to the selecteFieldStations array. Once
     * all fields have been iterated over we have a complete list of all the
     * fieldStations we need to call getFieldStationData() on
     *
     * @return
     */
    public Data[] selectData() {
        // TODO implement here
        return null;
    }

    /**
     * triggers a "backup" in a real system, but here could just trigger
     * serialization at a specific interval e.g. daily
     *
     */
    public void backupData() {
    }

    /**
     * displays the results in a map view
     *
     */
    public void showDevices() {
    }

    public void showHomeView() {
        this.pack();
        this.setVisible(true);
    }

    /**
     * Shows the Farm View
     */
    public void showFarmView() {
        farmView.pack();
        farmView.setVisible(true);
    }

    /**
     * Shows the Field View
     */
    public void showFieldView() {
        fieldsDialog.pack();
        fieldsDialog.setVisible(true);
    }

    /**
     * Shows the FieldStation View
     */
    public void showFieldStationView() {
    }

    /**
     * Shows the Farmers View
     */
    public void showFarmersView() {
        farmerDialog.pack();
        farmerDialog.setVisible(true);
    }

    /**
     * Shows the Farmer Popup
     *
     * @param farmer
     */
    public void showFarmerPopup(Farmer farmer) {
        if (farmer == null) {
            confirmAddFarmerBtn.setVisible(true);
            confirmEditFarmerBtn.setVisible(false);
            Object[] data = new Object[1];
            DefaultTableModel model1 = (DefaultTableModel) allFarmsTable.getModel();
            model1.setRowCount(0);
            for (int i = 0; i < theFarms.getAllFarms().size(); i++) {
                data[0] = theFarms.getAllFarms().get(i).getFarmNo();
                model1.addRow(data);
            }
            addFarmerDialog.pack();
            addFarmerDialog.setVisible(true);
        } else {
            confirmAddFarmerBtn.setVisible(false);
            confirmEditFarmerBtn.setVisible(true);
            addFarmerName.setText(farmer.getName());
            addFarmerEmail.setText(farmer.getEmail());
            addFarmerPhone.setText(farmer.getTelephone());
            addFarmerId.setValue(farmer.getId());

            SetOfFarms tmpFarms = theFarmers.getFarmerByNumber(farmer.getId()).getAssociatedFarms();
            Object[] data = new Object[1];
            DefaultTableModel model = (DefaultTableModel) associatedFarmsTable.getModel();
            model.setRowCount(0);
            for (int i = 0; i < tmpFarms.getAllFarms().size(); i++) {
                data[0] = tmpFarms.getAllFarms().get(i).getFarmNo();
                model.addRow(data);
            }

            DefaultTableModel model1 = (DefaultTableModel) allFarmsTable.getModel();
            model1.setRowCount(0);
            for (int i = 0; i < theFarms.getAllFarms().size(); i++) {
                data[0] = theFarms.getAllFarms().get(i).getFarmNo();
                model1.addRow(data);
            }
            addFarmerDialog.pack();
            addFarmerDialog.setVisible(true);
        }
    }

    /**
     * Shows the Farm Popup
     *
     * @param farm
     */
    public void showFarmPopup(Farm farm) {
        if (farm == null) {
            confirmEditFarmBtn1.setVisible(false);
            createFarmBtn.setVisible(true);
            addFarmDialog.pack();
            addFarmDialog.setVisible(true);
        } else {
            confirmEditFarmBtn1.setVisible(true);
            createFarmBtn.setVisible(false);
            nameInput.setText(farm.getName());
            xCoordSpin.setValue(farm.getLocation().getXCoord());
            yCoordSpin.setValue(farm.getLocation().getYCoord());
            typeInput.setText(farm.getLocation().getType());
            fieldIdSpin.setValue(farm.getFarmNo());
            addFarmDialog.pack();
            addFarmDialog.setVisible(true);
        }
    }

    /**
     * Shows the Field Popup
     *
     * @param field
     */
    public void showFieldPopup(Field field) {
        if (field == null) {
            confirmAddBtn.setVisible(true);
            editBtn.setVisible(false);
            addFieldDialog.pack();
            addFieldDialog.setVisible(true);
        } else {
            addFieldName.setText(field.getName());
            addFieldType.setText(field.getType());
            addFieldNo.setValue(field.getFieldNumber());
            addFieldCrop.setText(field.getCrop().getName());
            addFieldArea.setValue(field.getCrop().getAreaRequired());
            confirmAddBtn.setVisible(false);
            editBtn.setVisible(true);
            addFieldDialog.pack();
            addFieldDialog.setVisible(true);
        }
    }

    /**
     * Shows the FieldStation Popup
     *
     * @param station
     */
    public void showFieldStationPopup(FieldStation station) {
        if (station == null) {
            addFieldStationDialog.pack();
            addFieldStationDialog.setVisible(true);
        } else {

        }
    }

    /**
     * Shows the Sensor Popup
     *
     * @param Sensor
     */
    public void showSensorPopup() {
        
        addSensorDialog.pack();
        addSensorDialog.setVisible(true);
    }

    /**
     * Exports to PDF
     */
    public void exportToPDF() {

    }

    /**
     * Populate ComboBox for Fields
     *
     * @param farm
     */
    public void populateCmbFields(Farm farm) {
        cmbFields.removeAllItems();

        for (int i = 0; i < farm.getAllFields().length; i++) {
            cmbFields.addItem(farm.getAllFields()[i].getFieldNumber());
        }

    }

    /**
     * Populate ComboBox for Farms
     */
    private void populateCmbFarms() {
        cmbFarms.removeAllItems();
        for (int i = 0; i < theFarms.getAllFarms().size(); i++) {
            cmbFarms.addItem(theFarms.getAllFarms().get(i).getFarmNo());
        }
    }

    /**
     * Populate ComboBox for Farmers
     */
    public void populateCmbFarmers() {
        cmbFarmers.removeAllItems();
        for (int i = 0; i < theFarmers.getAllFarmers().length; i++) {
            cmbFarmers.addItem(Integer.toString(theFarmers.getAllFarmers()[i].getId()));
        }
    }

    /**
     * Populate ComboBox for FieldStations
     *
     * @param field
     */
    public void populateCmbFieldStations(Field field) {
        cmbFieldStations.removeAllItems();
        for (int i = 0; i < field.getAllFieldStations().length; i++) {
            cmbFieldStations.addItem(Integer.toString(field.getAllFieldStations()[i].getFieldStationNo()));
        }
    }

    public void populateCmbSensors(FieldStation fieldStation) {
        cmbSensors.removeAllItems();
        if (fieldStation.getAllSensors().length == 0) {
                JOptionPane.showMessageDialog(fieldsDialog,"No Sensor Data");
        } else {
            for (int i = 0; i < fieldStation.getAllSensors().length; i++) {
                cmbSensors.addItem(Integer.toString(fieldStation.getAllSensors()[i].getSerialNo()));
            }
        }
    }

    /**
     * Creates the Farmer Table with farmer ID
     *
     * @param farmerID
     */
    public void createFarmerTable(int farmerID) {

        SetOfFarms tmp = theFarmers.getFarmerByNumber(farmerID).getAssociatedFarms();
        Object[] data = new Object[1];
        DefaultTableModel model = (DefaultTableModel) farmerTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < tmp.getAllFarms().size(); i++) {
            data[0] = tmp.getAllFarms().get(i).getName();
            model.addRow(data);
        }
    }

    /**
     * Populates the Search ComboBox
     */
    public void populateCmbSearch() {
        cmbSearch.removeAllItems();
        cmbSearch.addItem("Name");
        cmbSearch.addItem("ID");
        cmbSearch.addItem("Telephone");
        cmbSearch.addItem("Email");
    }

    /**
     * Populate Details of the Farmer
     *
     * @param tmp
     */
    public void populateFarmerDetails(Farmer tmp) {
        farmerNameLbl.setText("Farmer Name: " + tmp.getName());
        farmerEmailLbl.setText("Farmer Email: " + tmp.getEmail());
        farmerIdLbl.setText("Farmer ID: " + tmp.getId());
        farmerPhoneLbl.setText("Farmer Phone: " + tmp.getTelephone());
        createFarmerTable(tmp.getId());
    }

    /**
     * Searches for Farmer with Name,ID,Tel or Email
     *
     * @param type
     */
    public void farmerSearch(String type) {
        String search = "";
        Farmer farmer = null;
        if (type.equals("name")) {
            search = searchField.getText();
            farmer = theFarmers.getFarmerByName(search);
        } else {
            if (type.equals("id")) {
                search = searchField.getText();
                farmer = theFarmers.getFarmerByNumber(Integer.parseInt(search));
            } else {
                if (type.equals("tel")) {
                    search = searchField.getText();
                    farmer = theFarmers.getFarmerByTelephone(search);
                } else {
                    if (type.equals("email")) {
                        search = searchField.getText();
                        farmer = theFarmers.getFarmerByEmail(search);
                    }
                }
            }
        }
        if (farmer == null) {
            searchFailed.pack();
            searchFailed.setVisible(true);
        } else {
            populateFarmerDetails(farmer);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        farmView = new javax.swing.JDialog();
        cmbFields = new javax.swing.JComboBox();
        fieldLbl = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        addFieldBtn = new javax.swing.JButton();
        removeFieldBtn = new javax.swing.JButton();
        editFieldBtn = new javax.swing.JButton();
        showFieldBtn = new javax.swing.JButton();
        homeBtn = new javax.swing.JButton();
        jLabel49 = new javax.swing.JLabel();
        farmerDialog = new javax.swing.JDialog();
        farmerNameLbl = new javax.swing.JLabel();
        farmerEmailLbl = new javax.swing.JLabel();
        cmbFarmers = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        farmerIdLbl = new javax.swing.JLabel();
        showFarmerDetailsBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        farmerTable = new javax.swing.JTable();
        addFarmerBtn = new javax.swing.JButton();
        editFarmerBtn = new javax.swing.JButton();
        deleteFarmerBtn = new javax.swing.JButton();
        farmerPhoneLbl = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        farmerSearchBtn = new javax.swing.JButton();
        cmbSearch = new javax.swing.JComboBox();
        homeBtn1 = new javax.swing.JButton();
        fieldsDialog = new javax.swing.JDialog();
        lblFieldType = new javax.swing.JLabel();
        lblFieldCrop = new javax.swing.JLabel();
        lblFieldName = new javax.swing.JLabel();
        addFieldStationBtn = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        btnEditFieldstation = new javax.swing.JButton();
        deleteFieldStationBtn = new javax.swing.JButton();
        cmbFieldStations = new javax.swing.JComboBox();
        jLabel24 = new javax.swing.JLabel();
        updateFieldStationDetails = new javax.swing.JButton();
        jLabel44 = new javax.swing.JLabel();
        jLabel45 = new javax.swing.JLabel();
        cmbSensors = new javax.swing.JComboBox();
        fieldStationNameLbl = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        xLocationLbl = new javax.swing.JLabel();
        yLocationLbl = new javax.swing.JLabel();
        typeLocationLbl = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        plantCropBtn = new javax.swing.JButton();
        harvestCropBtn = new javax.swing.JButton();
        showCropHistoryBtn = new javax.swing.JButton();
        addSensorBtn = new javax.swing.JButton();
        editSensorBtn = new javax.swing.JButton();
        removeSensorBtn = new javax.swing.JButton();
        jLabel50 = new javax.swing.JLabel();
        backBtn = new javax.swing.JButton();
        addFarmDialog = new javax.swing.JDialog();
        nameInput = new javax.swing.JTextField();
        nameLblAddFarm = new javax.swing.JLabel();
        xLabelAddFarm = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        xCoordSpin = new javax.swing.JSpinner();
        yCoordSpin = new javax.swing.JSpinner();
        typeInput = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        fieldIdSpin = new javax.swing.JSpinner();
        createFarmBtn = new javax.swing.JButton();
        farmCancelBtn = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        deleteFarmDialog = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        delFarmNameLbl = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        ConfirmDelBtn = new javax.swing.JButton();
        cancelDelBtn = new javax.swing.JButton();
        addFieldDialog = new javax.swing.JDialog();
        jLabel1 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        addFieldName = new javax.swing.JTextField();
        addFieldType = new javax.swing.JTextField();
        addFieldNo = new javax.swing.JSpinner();
        addFieldCrop = new javax.swing.JTextField();
        addFieldArea = new javax.swing.JSpinner();
        confirmAddBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        deleteFieldDialog = new javax.swing.JDialog();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        fieldNameLbl = new javax.swing.JLabel();
        confirmFieldDelete = new javax.swing.JButton();
        cancelFieldDelete = new javax.swing.JButton();
        addFarmerDialog = new javax.swing.JDialog();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        addFarmerName = new javax.swing.JTextField();
        addFarmerEmail = new javax.swing.JTextField();
        addFarmerPhone = new javax.swing.JTextField();
        confirmAddFarmerBtn = new javax.swing.JButton();
        confirmEditFarmerBtn = new javax.swing.JButton();
        cancelAddFarmerBtn = new javax.swing.JButton();
        addFarmerId = new javax.swing.JSpinner();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        allFarmsTable = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        associatedFarmsTable = new javax.swing.JTable();
        addToAssociatedBtn = new javax.swing.JButton();
        removeFromAssociatedBtn = new javax.swing.JButton();
        deleteFarmerDialog = new javax.swing.JDialog();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        deleteFarmerName = new javax.swing.JLabel();
        confirmFarmerDelete = new javax.swing.JButton();
        cancelFarmerDelete = new javax.swing.JButton();
        searchFailed = new javax.swing.JDialog();
        jLabel23 = new javax.swing.JLabel();
        editFieldStationDialog = new javax.swing.JDialog();
        jLabel25 = new javax.swing.JLabel();
        fieldStationName = new javax.swing.JTextField();
        fieldStationXCoord = new javax.swing.JSpinner();
        fieldStationYCoord = new javax.swing.JSpinner();
        fieldStationType = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        confirmEditFieldstation = new javax.swing.JButton();
        cancelFieldStation = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        fieldStationId = new javax.swing.JSpinner();
        addFieldStationDialog = new javax.swing.JDialog();
        jLabel30 = new javax.swing.JLabel();
        fieldStationName1 = new javax.swing.JTextField();
        fieldStationXCoord1 = new javax.swing.JSpinner();
        fieldStationYCoord1 = new javax.swing.JSpinner();
        fieldStationType1 = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        confirmAddFieldstation = new javax.swing.JButton();
        cancelFieldStation1 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        fieldStationId1 = new javax.swing.JSpinner();
        editFarmDialog = new javax.swing.JDialog();
        nameInput1 = new javax.swing.JTextField();
        nameLblAddFarm1 = new javax.swing.JLabel();
        xLabelAddFarm1 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        xCoordSpin1 = new javax.swing.JSpinner();
        yCoordSpin1 = new javax.swing.JSpinner();
        typeInput1 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        fieldIdSpin1 = new javax.swing.JSpinner();
        confirmEditFarmBtn1 = new javax.swing.JButton();
        farmCancelBtn1 = new javax.swing.JButton();
        jLabel39 = new javax.swing.JLabel();
        deleteFieldStationDialog = new javax.swing.JDialog();
        deleteFieldStation = new javax.swing.JButton();
        cancelDeleteFieldStationBtn = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        deleteFieldStationLbl = new javax.swing.JLabel();
        showFieldHistory = new javax.swing.JDialog();
        jScrollPane4 = new javax.swing.JScrollPane();
        fieldHistoryTable = new javax.swing.JTable();
        closeHistoryBtn = new javax.swing.JButton();
        addSensorDialog = new javax.swing.JDialog();
        sensorDialogLocationX = new javax.swing.JLabel();
        sensorDialogSerialNumberLbl = new javax.swing.JLabel();
        sensorDialogSerialNumber = new javax.swing.JTextField();
        sensorDialogIntervalLbl = new javax.swing.JLabel();
        sensorDialogInterval = new javax.swing.JTextField();
        sensorDialogAddBtn = new javax.swing.JButton();
        sensorDialogCancelBtn = new javax.swing.JButton();
        sensorDialogLocationY = new javax.swing.JLabel();
        sensorDialogLocationXSpinner = new javax.swing.JSpinner();
        sensorDialogLocationYSpinner = new javax.swing.JSpinner();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        sensorDialogLocationType = new javax.swing.JTextField();
        editSensorDialog = new javax.swing.JDialog();
        sensorType = new javax.swing.JTextField();
        sensorID = new javax.swing.JSpinner();
        sensorYcord = new javax.swing.JSpinner();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        sensorXcord = new javax.swing.JSpinner();
        jLabel56 = new javax.swing.JLabel();
        sensorName = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        confirmEditSensor = new javax.swing.JButton();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        cancelSensor = new javax.swing.JButton();
        removeSensorDialog = new javax.swing.JDialog();
        jLabel53 = new javax.swing.JLabel();
        deleteSensorName = new javax.swing.JLabel();
        sensorDeleteConfirm = new javax.swing.JButton();
        sensorDeleteCancel = new javax.swing.JButton();
        cmbFarms = new javax.swing.JComboBox();
        farmsLbl = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        showFarmsBtn = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        addFarmBtn = new javax.swing.JButton();
        editFarmBtn = new javax.swing.JButton();
        delFarmBtn = new javax.swing.JButton();
        viewFarmersBtn = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));

        farmView.setMinimumSize(new java.awt.Dimension(490, 270));
        farmView.setResizable(false);
        farmView.getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbFields.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        farmView.getContentPane().add(cmbFields, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 200, -1));

        fieldLbl.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        fieldLbl.setText("Farm View:");
        farmView.getContentPane().add(fieldLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        farmView.getContentPane().add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 40, 474, 10));

        addFieldBtn.setText("Add Field");
        addFieldBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFieldBtnActionPerformed(evt);
            }
        });
        farmView.getContentPane().add(addFieldBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 150, -1));

        removeFieldBtn.setText("Remove Field");
        removeFieldBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFieldBtnActionPerformed(evt);
            }
        });
        farmView.getContentPane().add(removeFieldBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 150, -1));

        editFieldBtn.setText("Edit Field");
        editFieldBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFieldBtnActionPerformed(evt);
            }
        });
        farmView.getContentPane().add(editFieldBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 150, -1));

        showFieldBtn.setText("Show Selected Field");
        showFieldBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFieldBtnActionPerformed(evt);
            }
        });
        farmView.getContentPane().add(showFieldBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 200, -1));

        homeBtn.setText("Home");
        homeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtnActionPerformed(evt);
            }
        });
        farmView.getContentPane().add(homeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 170, 97, -1));

        jLabel49.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel49.setText("Field ID");
        farmView.getContentPane().add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, -1, -1));

        farmerNameLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        farmerNameLbl.setText("Farmer Name: Click Show Details");

        farmerEmailLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        farmerEmailLbl.setText("Farmer Email:");

        cmbFarmers.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cmbFarmers.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("Select a Farmer:");

        farmerIdLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        farmerIdLbl.setText("Farmer ID:");

        showFarmerDetailsBtn.setText("Show Details");
        showFarmerDetailsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFarmerDetailsBtnActionPerformed(evt);
            }
        });

        farmerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Farms"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(farmerTable);

        addFarmerBtn.setText("Add Farmer");
        addFarmerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFarmerBtnActionPerformed(evt);
            }
        });

        editFarmerBtn.setText("Edit Farmer");
        editFarmerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFarmerBtnActionPerformed(evt);
            }
        });

        deleteFarmerBtn.setText("Delete Farmer");
        deleteFarmerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFarmerBtnActionPerformed(evt);
            }
        });

        farmerPhoneLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        farmerPhoneLbl.setText("Farmer Phone: ");

        searchField.setText("Search");

        farmerSearchBtn.setText("Search");
        farmerSearchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                farmerSearchBtnActionPerformed(evt);
            }
        });

        cmbSearch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        homeBtn1.setText("Home");
        homeBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                homeBtn1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout farmerDialogLayout = new javax.swing.GroupLayout(farmerDialog.getContentPane());
        farmerDialog.getContentPane().setLayout(farmerDialogLayout);
        farmerDialogLayout.setHorizontalGroup(
            farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(farmerDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)
                    .addGroup(farmerDialogLayout.createSequentialGroup()
                        .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(farmerDialogLayout.createSequentialGroup()
                                .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 447, Short.MAX_VALUE)
                                    .addComponent(cmbFarmers, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, farmerDialogLayout.createSequentialGroup()
                                        .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(farmerNameLbl)
                                            .addComponent(farmerEmailLbl))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(searchField)
                                            .addComponent(cmbSearch, 0, 122, Short.MAX_VALUE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(showFarmerDetailsBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(farmerSearchBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(farmerIdLbl)
                            .addComponent(farmerPhoneLbl))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, farmerDialogLayout.createSequentialGroup()
                        .addGap(0, 45, Short.MAX_VALUE)
                        .addComponent(addFarmerBtn)
                        .addGap(46, 46, 46)
                        .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(homeBtn1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(editFarmerBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(51, 51, 51)
                        .addComponent(deleteFarmerBtn)
                        .addGap(80, 80, 80)))
                .addContainerGap())
        );
        farmerDialogLayout.setVerticalGroup(
            farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(farmerDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(farmerDialogLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbFarmers, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showFarmerDetailsBtn))
                        .addGap(26, 26, 26)
                        .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(farmerNameLbl)
                            .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(farmerSearchBtn))
                        .addGap(18, 18, 18)
                        .addComponent(farmerEmailLbl))
                    .addComponent(cmbSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addComponent(farmerIdLbl)
                .addGap(26, 26, 26)
                .addComponent(farmerPhoneLbl)
                .addGap(35, 35, 35)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(farmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addFarmerBtn)
                    .addComponent(editFarmerBtn)
                    .addComponent(deleteFarmerBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(homeBtn1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblFieldType.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFieldType.setText("Type:");

        lblFieldCrop.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFieldCrop.setText("Crop:");

        lblFieldName.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblFieldName.setText("Name: ");

        addFieldStationBtn.setText("Add FieldStation");
        addFieldStationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFieldStationBtnActionPerformed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel22.setText("Field");

        btnEditFieldstation.setText("Edit FieldStation");
        btnEditFieldstation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditFieldstationActionPerformed(evt);
            }
        });

        deleteFieldStationBtn.setText("Remove FieldStation");
        deleteFieldStationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFieldStationBtnActionPerformed(evt);
            }
        });

        cmbFieldStations.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel24.setText("Field Stations");

        updateFieldStationDetails.setText("Show Details");
        updateFieldStationDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateFieldStationDetailsActionPerformed(evt);
            }
        });

        jLabel44.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel44.setText("Name: ");

        jLabel45.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel45.setText("Sensors: ");

        cmbSensors.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        fieldStationNameLbl.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        fieldStationNameLbl.setText("Click Show Details");

        jLabel46.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel46.setText("Location:");

        xLocationLbl.setText("x: ");

        yLocationLbl.setText("y: ");

        typeLocationLbl.setText("Type:");

        jLabel47.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel47.setText("Plantings");

        plantCropBtn.setText("Plant Crop");
        plantCropBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                plantCropBtnActionPerformed(evt);
            }
        });

        harvestCropBtn.setText("Harvest Crop");
        harvestCropBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                harvestCropBtnActionPerformed(evt);
            }
        });

        showCropHistoryBtn.setText("Show History");
        showCropHistoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showCropHistoryBtnActionPerformed(evt);
            }
        });

        addSensorBtn.setText("Add Sensor");
        addSensorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addSensorBtnActionPerformed(evt);
            }
        });

        editSensorBtn.setText("Edit Sensor");
        editSensorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editSensorBtnActionPerformed(evt);
            }
        });

        removeSensorBtn.setText("Remove Sensor");
        removeSensorBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSensorBtnActionPerformed(evt);
            }
        });

        jLabel50.setText("ID");

        backBtn.setText("Back");
        backBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fieldsDialogLayout = new javax.swing.GroupLayout(fieldsDialog.getContentPane());
        fieldsDialog.getContentPane().setLayout(fieldsDialogLayout);
        fieldsDialogLayout.setHorizontalGroup(
            fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fieldsDialogLayout.createSequentialGroup()
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(fieldsDialogLayout.createSequentialGroup()
                                .addComponent(jLabel22)
                                .addGap(154, 154, 154)
                                .addComponent(jLabel47))
                            .addGroup(fieldsDialogLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                                        .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(fieldsDialogLayout.createSequentialGroup()
                                                .addComponent(lblFieldType)
                                                .addGap(13, 315, Short.MAX_VALUE))
                                            .addGroup(fieldsDialogLayout.createSequentialGroup()
                                                .addComponent(jLabel24)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(fieldsDialogLayout.createSequentialGroup()
                                                .addComponent(lblFieldName)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(plantCropBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(18, 18, 18))
                                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                                        .addComponent(lblFieldCrop)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(harvestCropBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(showCropHistoryBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))))
                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(fieldsDialogLayout.createSequentialGroup()
                                .addComponent(jLabel50)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbFieldStations, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(updateFieldStationDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(fieldsDialogLayout.createSequentialGroup()
                                    .addComponent(addSensorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(editSensorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(removeSensorBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(fieldsDialogLayout.createSequentialGroup()
                                    .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(fieldsDialogLayout.createSequentialGroup()
                                            .addComponent(jLabel44)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(fieldStationNameLbl))
                                        .addGroup(fieldsDialogLayout.createSequentialGroup()
                                            .addComponent(jLabel45)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(cmbSensors, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(fieldsDialogLayout.createSequentialGroup()
                                            .addComponent(jLabel46)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(xLocationLbl)
                                            .addGap(18, 18, 18)
                                            .addComponent(yLocationLbl)
                                            .addGap(18, 18, 18)
                                            .addComponent(typeLocationLbl)))
                                    .addGap(179, 179, 179)
                                    .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(fieldsDialogLayout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(deleteFieldStationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(fieldsDialogLayout.createSequentialGroup()
                                            .addGap(17, 17, 17)
                                            .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(addFieldStationBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnEditFieldstation, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator6)
                            .addComponent(jSeparator7))))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fieldsDialogLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(backBtn)
                .addGap(243, 243, 243))
        );
        fieldsDialogLayout.setVerticalGroup(
            fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fieldsDialogLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(jLabel22))
                .addGap(2, 2, 2)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(plantCropBtn)
                        .addComponent(harvestCropBtn))
                    .addComponent(lblFieldName))
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(showCropHistoryBtn))
                    .addGroup(fieldsDialogLayout.createSequentialGroup()
                        .addComponent(lblFieldType)
                        .addGap(18, 18, 18)
                        .addComponent(lblFieldCrop)))
                .addGap(42, 42, 42)
                .addComponent(jLabel24)
                .addGap(2, 2, 2)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addFieldStationBtn)
                    .addComponent(updateFieldStationDetails)
                    .addComponent(cmbFieldStations, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50))
                .addGap(5, 5, 5)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEditFieldstation)
                    .addComponent(jLabel44)
                    .addComponent(fieldStationNameLbl))
                .addGap(5, 5, 5)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteFieldStationBtn)
                    .addComponent(jLabel45)
                    .addComponent(cmbSensors, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46)
                    .addComponent(xLocationLbl)
                    .addComponent(yLocationLbl)
                    .addComponent(typeLocationLbl))
                .addGap(18, 18, 18)
                .addGroup(fieldsDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(addSensorBtn)
                    .addComponent(editSensorBtn)
                    .addComponent(removeSensorBtn))
                .addGap(18, 18, 18)
                .addComponent(backBtn)
                .addContainerGap(27, Short.MAX_VALUE))
        );

        addFarmDialog.setMinimumSize(new java.awt.Dimension(300, 320));
        addFarmDialog.setPreferredSize(new java.awt.Dimension(300, 290));

        nameLblAddFarm.setText("Name");

        xLabelAddFarm.setText("x Coord");

        jLabel3.setText("y Coord");

        jLabel4.setText("Type");

        jLabel5.setText("Farm ID");

        createFarmBtn.setText("Create Farm");
        createFarmBtn.setName(""); // NOI18N
        createFarmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createFarmBtnActionPerformed(evt);
            }
        });

        farmCancelBtn.setText("Cancel");
        farmCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                farmCancelBtnActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel35.setText("Add Farm");

        javax.swing.GroupLayout addFarmDialogLayout = new javax.swing.GroupLayout(addFarmDialog.getContentPane());
        addFarmDialog.getContentPane().setLayout(addFarmDialogLayout);
        addFarmDialogLayout.setHorizontalGroup(
            addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFarmDialogLayout.createSequentialGroup()
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(90, 90, 90)
                        .addComponent(jLabel35))
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel3)
                        .addGap(21, 21, 21)
                        .addComponent(yCoordSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(createFarmBtn)
                        .addGap(21, 21, 21)
                        .addComponent(farmCancelBtn))
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4))
                        .addGap(21, 21, 21)
                        .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(typeInput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fieldIdSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(xLabelAddFarm)
                            .addComponent(nameLblAddFarm))
                        .addGap(20, 20, 20)
                        .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(xCoordSpin, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        addFarmDialogLayout.setVerticalGroup(
            addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFarmDialogLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel35)
                .addGap(15, 15, 15)
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLblAddFarm)
                    .addComponent(nameInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(xLabelAddFarm))
                    .addComponent(xCoordSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel3))
                    .addComponent(yCoordSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(14, 14, 14)
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(typeInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFarmDialogLayout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(jLabel5))
                    .addComponent(fieldIdSpin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addGroup(addFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(createFarmBtn)
                    .addComponent(farmCancelBtn))
                .addContainerGap(57, Short.MAX_VALUE))
        );

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("Are You Sure You Want To Delete");

        delFarmNameLbl.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        delFarmNameLbl.setText("jLabel7");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("This Decision Is Irreversable");

        ConfirmDelBtn.setText("Confirm");
        ConfirmDelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfirmDelBtnActionPerformed(evt);
            }
        });

        cancelDelBtn.setText("Cancel");
        cancelDelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout deleteFarmDialogLayout = new javax.swing.GroupLayout(deleteFarmDialog.getContentPane());
        deleteFarmDialog.getContentPane().setLayout(deleteFarmDialogLayout);
        deleteFarmDialogLayout.setHorizontalGroup(
            deleteFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, deleteFarmDialogLayout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(deleteFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
            .addGroup(deleteFarmDialogLayout.createSequentialGroup()
                .addGroup(deleteFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deleteFarmDialogLayout.createSequentialGroup()
                        .addGap(165, 165, 165)
                        .addComponent(delFarmNameLbl))
                    .addGroup(deleteFarmDialogLayout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addComponent(ConfirmDelBtn)
                        .addGap(87, 87, 87)
                        .addComponent(cancelDelBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        deleteFarmDialogLayout.setVerticalGroup(
            deleteFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteFarmDialogLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(delFarmNameLbl)
                .addGap(18, 18, 18)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(deleteFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConfirmDelBtn)
                    .addComponent(cancelDelBtn))
                .addContainerGap(60, Short.MAX_VALUE))
        );

        jLabel1.setText("Name");

        jLabel8.setText("Type");

        jLabel9.setText("Field No.");

        jLabel10.setText("Crop");

        jLabel11.setText("Crop Area");

        confirmAddBtn.setText("Add Field");
        confirmAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAddBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit Field");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addFieldDialogLayout = new javax.swing.GroupLayout(addFieldDialog.getContentPane());
        addFieldDialog.getContentPane().setLayout(addFieldDialogLayout);
        addFieldDialogLayout.setHorizontalGroup(
            addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFieldDialogLayout.createSequentialGroup()
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFieldDialogLayout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(addFieldDialogLayout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(addFieldArea))
                            .addComponent(jLabel8)
                            .addComponent(jLabel1)
                            .addGroup(addFieldDialogLayout.createSequentialGroup()
                                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, addFieldDialogLayout.createSequentialGroup()
                                        .addGap(17, 17, 17)
                                        .addComponent(addFieldCrop, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(addFieldDialogLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(addFieldName, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)
                                                .addComponent(addFieldType))
                                            .addComponent(addFieldNo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(addFieldDialogLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(editBtn)
                            .addComponent(confirmAddBtn))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelBtn)))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        addFieldDialogLayout.setVerticalGroup(
            addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFieldDialogLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(addFieldName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(addFieldType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(addFieldNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(addFieldCrop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(addFieldArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53)
                .addGroup(addFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmAddBtn)
                    .addComponent(cancelBtn)
                    .addComponent(editBtn))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("Are You Sure You Want To Delete");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("This Decision Is Irreversable");

        fieldNameLbl.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        fieldNameLbl.setText("jLabel14");

        confirmFieldDelete.setText("Confirm");
        confirmFieldDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmFieldDeleteActionPerformed(evt);
            }
        });

        cancelFieldDelete.setText("Cancel");
        cancelFieldDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelFieldDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout deleteFieldDialogLayout = new javax.swing.GroupLayout(deleteFieldDialog.getContentPane());
        deleteFieldDialog.getContentPane().setLayout(deleteFieldDialogLayout);
        deleteFieldDialogLayout.setHorizontalGroup(
            deleteFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteFieldDialogLayout.createSequentialGroup()
                .addGroup(deleteFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deleteFieldDialogLayout.createSequentialGroup()
                        .addGap(51, 51, 51)
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(deleteFieldDialogLayout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(fieldNameLbl))
                    .addGroup(deleteFieldDialogLayout.createSequentialGroup()
                        .addGap(62, 62, 62)
                        .addGroup(deleteFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(deleteFieldDialogLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(confirmFieldDelete)
                                .addGap(76, 76, 76)
                                .addComponent(cancelFieldDelete))
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        deleteFieldDialogLayout.setVerticalGroup(
            deleteFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteFieldDialogLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(fieldNameLbl)
                .addGap(28, 28, 28)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addGroup(deleteFieldDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmFieldDelete)
                    .addComponent(cancelFieldDelete))
                .addGap(34, 34, 34))
        );

        jLabel14.setText("Name");

        jLabel15.setText("Email");

        jLabel16.setText("Phone");

        jLabel17.setText("ID");

        confirmAddFarmerBtn.setText("Add Farmer");
        confirmAddFarmerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAddFarmerBtnActionPerformed(evt);
            }
        });

        confirmEditFarmerBtn.setText("Edit Farmer");
        confirmEditFarmerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEditFarmerBtnActionPerformed(evt);
            }
        });

        cancelAddFarmerBtn.setText("Cancel");
        cancelAddFarmerBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelAddFarmerBtnActionPerformed(evt);
            }
        });

        jLabel20.setText("Associated Farms");

        allFarmsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(allFarmsTable);

        jLabel21.setText("All Farms");

        associatedFarmsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(associatedFarmsTable);

        addToAssociatedBtn.setText("Add");
        addToAssociatedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addToAssociatedBtnActionPerformed(evt);
            }
        });

        removeFromAssociatedBtn.setText("Remove");
        removeFromAssociatedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeFromAssociatedBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addFarmerDialogLayout = new javax.swing.GroupLayout(addFarmerDialog.getContentPane());
        addFarmerDialog.getContentPane().setLayout(addFarmerDialogLayout);
        addFarmerDialogLayout.setHorizontalGroup(
            addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFarmerDialogLayout.createSequentialGroup()
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFarmerDialogLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(addFarmerDialogLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(36, 36, 36)
                                .addComponent(addToAssociatedBtn)
                                .addGap(52, 52, 52)
                                .addComponent(removeFromAssociatedBtn))
                            .addComponent(jLabel20)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14)
                            .addGroup(addFarmerDialogLayout.createSequentialGroup()
                                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel15)
                                    .addComponent(jLabel16)
                                    .addComponent(jLabel17))
                                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(addFarmerDialogLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(addFarmerPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(addFarmerEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(addFarmerName, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(addFarmerDialogLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(addFarmerId))))))
                    .addGroup(addFarmerDialogLayout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(confirmEditFarmerBtn)
                        .addGap(45, 45, 45)
                        .addComponent(confirmAddFarmerBtn)
                        .addGap(18, 18, 18)
                        .addComponent(cancelAddFarmerBtn)))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        addFarmerDialogLayout.setVerticalGroup(
            addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFarmerDialogLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(addFarmerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(addFarmerEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(addFarmerPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(addFarmerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFarmerDialogLayout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel21))
                    .addGroup(addFarmerDialogLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addToAssociatedBtn)
                            .addComponent(removeFromAssociatedBtn))))
                .addGap(11, 11, 11)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addGroup(addFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmEditFarmerBtn)
                    .addComponent(confirmAddFarmerBtn)
                    .addComponent(cancelAddFarmerBtn))
                .addGap(70, 70, 70))
        );

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("Are You Sure You Want To Delete");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel19.setText("This Decision Is Irreversable");

        deleteFarmerName.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        deleteFarmerName.setText("jLabel20");

        confirmFarmerDelete.setText("Confirm");
        confirmFarmerDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmFarmerDeleteActionPerformed(evt);
            }
        });

        cancelFarmerDelete.setText("Cancel");
        cancelFarmerDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelFarmerDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout deleteFarmerDialogLayout = new javax.swing.GroupLayout(deleteFarmerDialog.getContentPane());
        deleteFarmerDialog.getContentPane().setLayout(deleteFarmerDialogLayout);
        deleteFarmerDialogLayout.setHorizontalGroup(
            deleteFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteFarmerDialogLayout.createSequentialGroup()
                .addGroup(deleteFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deleteFarmerDialogLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(deleteFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(deleteFarmerDialogLayout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(deleteFarmerDialogLayout.createSequentialGroup()
                                .addComponent(confirmFarmerDelete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(cancelFarmerDelete)
                                .addGap(28, 28, 28))))
                    .addGroup(deleteFarmerDialogLayout.createSequentialGroup()
                        .addGap(155, 155, 155)
                        .addComponent(deleteFarmerName)))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        deleteFarmerDialogLayout.setVerticalGroup(
            deleteFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteFarmerDialogLayout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(deleteFarmerName)
                .addGap(38, 38, 38)
                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(deleteFarmerDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmFarmerDelete)
                    .addComponent(cancelFarmerDelete))
                .addGap(26, 26, 26))
        );

        jLabel23.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel23.setText("Your search returned no results");

        javax.swing.GroupLayout searchFailedLayout = new javax.swing.GroupLayout(searchFailed.getContentPane());
        searchFailed.getContentPane().setLayout(searchFailedLayout);
        searchFailedLayout.setHorizontalGroup(
            searchFailedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, searchFailedLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        searchFailedLayout.setVerticalGroup(
            searchFailedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(searchFailedLayout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(134, Short.MAX_VALUE))
        );

        jLabel25.setText("Name");

        jLabel26.setText("xCoord");

        jLabel27.setText("yCoord");

        jLabel28.setText("Type");

        confirmEditFieldstation.setText("Edit");
        confirmEditFieldstation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEditFieldstationActionPerformed(evt);
            }
        });

        cancelFieldStation.setText("Cancel");
        cancelFieldStation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelFieldStationActionPerformed(evt);
            }
        });

        jLabel29.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel29.setText("Edit Field Station");

        jLabel41.setText("ID");

        javax.swing.GroupLayout editFieldStationDialogLayout = new javax.swing.GroupLayout(editFieldStationDialog.getContentPane());
        editFieldStationDialog.getContentPane().setLayout(editFieldStationDialogLayout);
        editFieldStationDialogLayout.setHorizontalGroup(
            editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editFieldStationDialogLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editFieldStationDialogLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel29))
                    .addGroup(editFieldStationDialogLayout.createSequentialGroup()
                        .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel25)
                            .addComponent(jLabel26)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28)
                            .addComponent(jLabel41))
                        .addGap(18, 18, 18)
                        .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fieldStationYCoord, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                            .addComponent(fieldStationXCoord)
                            .addComponent(fieldStationName)
                            .addComponent(fieldStationType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(fieldStationId))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(editFieldStationDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(confirmEditFieldstation, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                .addComponent(cancelFieldStation)
                .addGap(57, 57, 57))
        );
        editFieldStationDialogLayout.setVerticalGroup(
            editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editFieldStationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel29)
                .addGap(21, 21, 21)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(fieldStationName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldStationXCoord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26))
                .addGap(18, 18, 18)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldStationYCoord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldStationType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(fieldStationId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(editFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmEditFieldstation)
                    .addComponent(cancelFieldStation))
                .addGap(52, 52, 52))
        );

        jLabel30.setText("Name");

        jLabel31.setText("xCoord");

        jLabel32.setText("yCoord");

        jLabel33.setText("Type");

        confirmAddFieldstation.setText("Add");
        confirmAddFieldstation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmAddFieldstationActionPerformed(evt);
            }
        });

        cancelFieldStation1.setText("Cancel");
        cancelFieldStation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelFieldStation1ActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel34.setText("Add Field Station");

        jLabel40.setText("ID");

        javax.swing.GroupLayout addFieldStationDialogLayout = new javax.swing.GroupLayout(addFieldStationDialog.getContentPane());
        addFieldStationDialog.getContentPane().setLayout(addFieldStationDialogLayout);
        addFieldStationDialogLayout.setHorizontalGroup(
            addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFieldStationDialogLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addFieldStationDialogLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel34))
                    .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addFieldStationDialogLayout.createSequentialGroup()
                            .addGap(24, 24, 24)
                            .addComponent(confirmAddFieldstation, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                            .addComponent(cancelFieldStation1))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, addFieldStationDialogLayout.createSequentialGroup()
                            .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel30)
                                .addComponent(jLabel31)
                                .addComponent(jLabel32)
                                .addComponent(jLabel33)
                                .addComponent(jLabel40))
                            .addGap(18, 18, 18)
                            .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(fieldStationType1)
                                .addComponent(fieldStationYCoord1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                                .addComponent(fieldStationXCoord1)
                                .addComponent(fieldStationName1)
                                .addComponent(fieldStationId1)))))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        addFieldStationDialogLayout.setVerticalGroup(
            addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addFieldStationDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel34)
                .addGap(21, 21, 21)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(fieldStationName1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldStationXCoord1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel31))
                .addGap(18, 18, 18)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldStationYCoord1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(18, 18, 18)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldStationType1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33))
                .addGap(18, 18, 18)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40)
                    .addComponent(fieldStationId1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(addFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmAddFieldstation)
                    .addComponent(cancelFieldStation1))
                .addGap(52, 52, 52))
        );

        nameLblAddFarm1.setText("Name");

        xLabelAddFarm1.setText("x Coord");

        jLabel36.setText("y Coord");

        jLabel37.setText("Type");

        jLabel38.setText("Farm ID");

        confirmEditFarmBtn1.setText("Edit Farm");
        confirmEditFarmBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEditFarmBtn1ActionPerformed(evt);
            }
        });

        farmCancelBtn1.setText("Cancel");
        farmCancelBtn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                farmCancelBtn1ActionPerformed(evt);
            }
        });

        jLabel39.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel39.setText("Edit Farm");

        javax.swing.GroupLayout editFarmDialogLayout = new javax.swing.GroupLayout(editFarmDialog.getContentPane());
        editFarmDialog.getContentPane().setLayout(editFarmDialogLayout);
        editFarmDialogLayout.setHorizontalGroup(
            editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editFarmDialogLayout.createSequentialGroup()
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editFarmDialogLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editFarmDialogLayout.createSequentialGroup()
                                .addComponent(nameLblAddFarm1)
                                .addGap(18, 18, 18)
                                .addComponent(nameInput1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editFarmDialogLayout.createSequentialGroup()
                                .addComponent(xLabelAddFarm1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xCoordSpin1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(farmCancelBtn1)
                                .addGroup(editFarmDialogLayout.createSequentialGroup()
                                    .addComponent(jLabel36)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(fieldIdSpin1)
                                        .addComponent(typeInput1, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                                        .addComponent(yCoordSpin1))))))
                    .addGroup(editFarmDialogLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel37)
                            .addComponent(jLabel38)))
                    .addGroup(editFarmDialogLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(confirmEditFarmBtn1)))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        editFarmDialogLayout.setVerticalGroup(
            editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editFarmDialogLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameInput1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLblAddFarm1))
                .addGap(18, 18, 18)
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(xLabelAddFarm1)
                    .addComponent(xCoordSpin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(yCoordSpin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeInput1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel37))
                .addGap(18, 18, 18)
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(fieldIdSpin1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addGroup(editFarmDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmEditFarmBtn1)
                    .addComponent(farmCancelBtn1))
                .addGap(24, 24, 24))
        );

        deleteFieldStation.setText("Delete");
        deleteFieldStation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteFieldStationActionPerformed(evt);
            }
        });

        cancelDeleteFieldStationBtn.setText("Cancel");
        cancelDeleteFieldStationBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelDeleteFieldStationBtnActionPerformed(evt);
            }
        });

        jLabel42.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel42.setText("Are You Sure You Want To Delete");

        jLabel43.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel43.setText("This Decision Is Irreversable");

        deleteFieldStationLbl.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        deleteFieldStationLbl.setText("jLabel44");

        javax.swing.GroupLayout deleteFieldStationDialogLayout = new javax.swing.GroupLayout(deleteFieldStationDialog.getContentPane());
        deleteFieldStationDialog.getContentPane().setLayout(deleteFieldStationDialogLayout);
        deleteFieldStationDialogLayout.setHorizontalGroup(
            deleteFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(deleteFieldStationDialogLayout.createSequentialGroup()
                .addGap(65, 65, 65)
                .addGroup(deleteFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(deleteFieldStationDialogLayout.createSequentialGroup()
                        .addComponent(deleteFieldStation)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cancelDeleteFieldStationBtn)
                        .addGap(90, 90, 90))
                    .addGroup(deleteFieldStationDialogLayout.createSequentialGroup()
                        .addGroup(deleteFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(49, Short.MAX_VALUE))))
            .addGroup(deleteFieldStationDialogLayout.createSequentialGroup()
                .addGap(149, 149, 149)
                .addComponent(deleteFieldStationLbl)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        deleteFieldStationDialogLayout.setVerticalGroup(
            deleteFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, deleteFieldStationDialogLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(deleteFieldStationLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 41, Short.MAX_VALUE)
                .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(deleteFieldStationDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteFieldStation)
                    .addComponent(cancelDeleteFieldStationBtn))
                .addGap(53, 53, 53))
        );

        fieldHistoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Crop", "Date Planted", "Date Harvested", "Yield"
            }
        ));
        jScrollPane4.setViewportView(fieldHistoryTable);

        closeHistoryBtn.setText("Close");
        closeHistoryBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeHistoryBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout showFieldHistoryLayout = new javax.swing.GroupLayout(showFieldHistory.getContentPane());
        showFieldHistory.getContentPane().setLayout(showFieldHistoryLayout);
        showFieldHistoryLayout.setHorizontalGroup(
            showFieldHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showFieldHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4)
                .addContainerGap())
            .addGroup(showFieldHistoryLayout.createSequentialGroup()
                .addGap(405, 405, 405)
                .addComponent(closeHistoryBtn)
                .addContainerGap(433, Short.MAX_VALUE))
        );
        showFieldHistoryLayout.setVerticalGroup(
            showFieldHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(showFieldHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(closeHistoryBtn)
                .addContainerGap(29, Short.MAX_VALUE))
        );

        sensorDialogLocationX.setText("Location X");

        sensorDialogSerialNumberLbl.setText("Serial Number");

        sensorDialogIntervalLbl.setText("Interval");

        sensorDialogAddBtn.setText("Add");
        sensorDialogAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensorDialogAddBtnActionPerformed(evt);
            }
        });

        sensorDialogCancelBtn.setText("Cancel");
        sensorDialogCancelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensorDialogCancelBtnActionPerformed(evt);
            }
        });

        sensorDialogLocationY.setText("Location Y");

        jLabel51.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel51.setText("Add a Sensor");

        jLabel52.setText("Location Type");

        sensorDialogLocationType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensorDialogLocationTypeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout addSensorDialogLayout = new javax.swing.GroupLayout(addSensorDialog.getContentPane());
        addSensorDialog.getContentPane().setLayout(addSensorDialogLayout);
        addSensorDialogLayout.setHorizontalGroup(
            addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSensorDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(addSensorDialogLayout.createSequentialGroup()
                        .addComponent(sensorDialogCancelBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                        .addComponent(sensorDialogAddBtn))
                    .addGroup(addSensorDialogLayout.createSequentialGroup()
                        .addComponent(sensorDialogSerialNumberLbl)
                        .addGap(18, 18, 18)
                        .addComponent(sensorDialogSerialNumber))
                    .addGroup(addSensorDialogLayout.createSequentialGroup()
                        .addComponent(jLabel52)
                        .addGap(18, 18, 18)
                        .addComponent(sensorDialogLocationType))
                    .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(addSensorDialogLayout.createSequentialGroup()
                            .addComponent(sensorDialogIntervalLbl)
                            .addGap(46, 46, 46)
                            .addComponent(sensorDialogInterval))
                        .addGroup(addSensorDialogLayout.createSequentialGroup()
                            .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(addSensorDialogLayout.createSequentialGroup()
                                    .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sensorDialogLocationX)
                                        .addComponent(sensorDialogLocationY))
                                    .addGap(35, 35, 35)
                                    .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(sensorDialogLocationYSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                                        .addComponent(sensorDialogLocationXSpinner)))
                                .addComponent(jLabel51))
                            .addGap(0, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        addSensorDialogLayout.setVerticalGroup(
            addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(addSensorDialogLayout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(jLabel51)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorDialogLocationX)
                    .addComponent(sensorDialogLocationXSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorDialogLocationY)
                    .addComponent(sensorDialogLocationYSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(sensorDialogLocationType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorDialogSerialNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sensorDialogSerialNumberLbl))
                .addGap(18, 18, 18)
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorDialogInterval, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sensorDialogIntervalLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(addSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorDialogAddBtn)
                    .addComponent(sensorDialogCancelBtn))
                .addContainerGap())
        );

        jLabel54.setText("yCoord");

        jLabel55.setText("xCoord");

        jLabel56.setText("ID");

        jLabel57.setText("Name");

        confirmEditSensor.setText("Edit");
        confirmEditSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmEditSensorActionPerformed(evt);
            }
        });

        jLabel58.setText("Type");

        jLabel59.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel59.setText("Edit Sensor");

        cancelSensor.setText("Cancel");
        cancelSensor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelSensorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editSensorDialogLayout = new javax.swing.GroupLayout(editSensorDialog.getContentPane());
        editSensorDialog.getContentPane().setLayout(editSensorDialogLayout);
        editSensorDialogLayout.setHorizontalGroup(
            editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editSensorDialogLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(editSensorDialogLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel59))
                    .addGroup(editSensorDialogLayout.createSequentialGroup()
                        .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel57)
                            .addComponent(jLabel55)
                            .addComponent(jLabel54)
                            .addComponent(jLabel58)
                            .addComponent(jLabel56))
                        .addGap(18, 18, 18)
                        .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(sensorYcord, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                            .addComponent(sensorXcord)
                            .addComponent(sensorName)
                            .addComponent(sensorType, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sensorID))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(editSensorDialogLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(confirmEditSensor, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 185, Short.MAX_VALUE)
                .addComponent(cancelSensor)
                .addGap(57, 57, 57))
        );
        editSensorDialogLayout.setVerticalGroup(
            editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editSensorDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel59)
                .addGap(21, 21, 21)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel57)
                    .addComponent(sensorName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorXcord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel55))
                .addGap(18, 18, 18)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorYcord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54))
                .addGap(18, 18, 18)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58))
                .addGap(18, 18, 18)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel56)
                    .addComponent(sensorID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(editSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmEditSensor)
                    .addComponent(cancelSensor))
                .addGap(52, 52, 52))
        );

        jLabel53.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel53.setText("Are You Sure You Want To Delete");

        deleteSensorName.setText("jLabel54");

        sensorDeleteConfirm.setText("Confirm");
        sensorDeleteConfirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensorDeleteConfirmActionPerformed(evt);
            }
        });

        sensorDeleteCancel.setText("Cancel");
        sensorDeleteCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sensorDeleteCancelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout removeSensorDialogLayout = new javax.swing.GroupLayout(removeSensorDialog.getContentPane());
        removeSensorDialog.getContentPane().setLayout(removeSensorDialogLayout);
        removeSensorDialogLayout.setHorizontalGroup(
            removeSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeSensorDialogLayout.createSequentialGroup()
                .addGroup(removeSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(removeSensorDialogLayout.createSequentialGroup()
                        .addGap(169, 169, 169)
                        .addComponent(deleteSensorName))
                    .addGroup(removeSensorDialogLayout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addGroup(removeSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(removeSensorDialogLayout.createSequentialGroup()
                                .addComponent(sensorDeleteConfirm)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sensorDeleteCancel))
                            .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(57, Short.MAX_VALUE))
        );
        removeSensorDialogLayout.setVerticalGroup(
            removeSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(removeSensorDialogLayout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(deleteSensorName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addGroup(removeSensorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sensorDeleteConfirm)
                    .addComponent(sensorDeleteCancel))
                .addGap(29, 29, 29))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(490, 270));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbFarms.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbFarms, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 200, -1));

        farmsLbl.setFont(new java.awt.Font("Arial", 0, 24)); // NOI18N
        farmsLbl.setText("Welcome");
        farmsLbl.setName(""); // NOI18N
        getContentPane().add(farmsLbl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        getContentPane().add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 440, -1));

        showFarmsBtn.setText("Show Selected Farm");
        showFarmsBtn.setMaximumSize(new java.awt.Dimension(101, 23));
        showFarmsBtn.setMinimumSize(new java.awt.Dimension(101, 23));
        showFarmsBtn.setPreferredSize(new java.awt.Dimension(101, 23));
        showFarmsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showFarmsBtnActionPerformed(evt);
            }
        });
        getContentPane().add(showFarmsBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 100, 200, -1));

        btnSave.setText("Save");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        getContentPane().add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 200, 75, -1));

        addFarmBtn.setText("Add Farm");
        addFarmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFarmBtnActionPerformed(evt);
            }
        });
        getContentPane().add(addFarmBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 70, 150, -1));

        editFarmBtn.setText("Edit Farm");
        editFarmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFarmBtnActionPerformed(evt);
            }
        });
        getContentPane().add(editFarmBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 100, 150, -1));

        delFarmBtn.setText("Delete Farm");
        delFarmBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delFarmBtnActionPerformed(evt);
            }
        });
        getContentPane().add(delFarmBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 150, -1));

        viewFarmersBtn.setText("View Farmers");
        viewFarmersBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewFarmersBtnActionPerformed(evt);
            }
        });
        getContentPane().add(viewFarmersBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 150, -1));

        jLabel48.setFont(new java.awt.Font("Arial", 0, 16)); // NOI18N
        jLabel48.setText("Farm ID");
        getContentPane().add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 80, 20));
        getContentPane().add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 185, 144, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void showFarmsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFarmsBtnActionPerformed
        if (theFarms.getAllFarms().size() == 0) {
            JOptionPane.showMessageDialog(this, "There is no Farm to show");
        } else {
            int tmp = (Integer) cmbFarms.getSelectedItem();
            Farm tmpFarm = selectFarm(tmp);
            populateCmbFields(tmpFarm);
            fieldLbl.setText("Farm View: " + tmpFarm.getName());
            showFarmView();
            this.setVisible(false);
        }
    }//GEN-LAST:event_showFarmsBtnActionPerformed

    private void addFarmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFarmBtnActionPerformed
        showFarmPopup(null);
    }//GEN-LAST:event_addFarmBtnActionPerformed

    private void createFarmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createFarmBtnActionPerformed
        // TODO add your handling code here:
        String name = nameInput.getText();
        int x = (int) xCoordSpin.getValue();
        int y = (int) yCoordSpin.getValue();
        String type = typeInput.getText();
        int id = (int) fieldIdSpin.getValue();
        theFarms.addFarm(name, new Location(x, y, type), id);
        populateCmbFarms();
        addFarmDialog.setVisible(false);
        nameInput.setText("");
        xCoordSpin.setValue(0);
        yCoordSpin.setValue(0);
        typeInput.setText("");
        fieldIdSpin.setValue(0);

    }//GEN-LAST:event_createFarmBtnActionPerformed

    private void editFarmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFarmBtnActionPerformed
        if (theFarms.getAllFarms().size() == 0) {
            JOptionPane.showMessageDialog(this, "There is no Farm to edit");
        } else {
            Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
            nameInput1.setText(tmp.getName());
            xCoordSpin1.setValue(tmp.getLocation().getXCoord());
            yCoordSpin1.setValue(tmp.getLocation().getYCoord());
            typeInput1.setText(tmp.getLocation().getType());
            fieldIdSpin.setValue(tmp.getFarmNo());
            editFarmDialog.pack();
            editFarmDialog.setVisible(true);
        }
    }//GEN-LAST:event_editFarmBtnActionPerformed

    private void farmCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_farmCancelBtnActionPerformed
        // TODO add your handling code here:
        addFarmDialog.setVisible(false);
        nameInput.setText("");
        xCoordSpin.setValue(0);
        yCoordSpin.setValue(0);
        typeInput.setText("");
        fieldIdSpin.setValue(0);
    }//GEN-LAST:event_farmCancelBtnActionPerformed

    private void delFarmBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delFarmBtnActionPerformed
        if (theFarms.getAllFarms().size() == 0) {
            JOptionPane.showMessageDialog(this, "There is no Farm to delete");
        } else {
            Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
            delFarmNameLbl.setText(tmpFarm.getName());
            deleteFarmDialog.pack();
            deleteFarmDialog.setVisible(true);
        }
    }//GEN-LAST:event_delFarmBtnActionPerformed

    private void ConfirmDelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfirmDelBtnActionPerformed
        // TODO add your handling code here:
        Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
        theFarms.removeFarm(tmp);
        populateCmbFarms();
        deleteFarmDialog.setVisible(false);
    }//GEN-LAST:event_ConfirmDelBtnActionPerformed

    private void cancelDelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDelBtnActionPerformed
        // TODO add your handling code here:
        deleteFarmDialog.setVisible(false);
    }//GEN-LAST:event_cancelDelBtnActionPerformed

    private void addFieldBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFieldBtnActionPerformed
        showFieldPopup(null);
    }//GEN-LAST:event_addFieldBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
        String name = addFieldName.getText();
        String type = addFieldType.getText();
        int fieldNo = (int) addFieldNo.getValue();
        String crop = addFieldCrop.getText();
        float area;
        if (addFieldArea.getValue() instanceof Float) {
            area = (float) addFieldArea.getValue();

        } else {
            int area1 = (int) addFieldArea.getValue();
            area = (float) area1;
        }
        Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmp, (Integer) cmbFields.getSelectedItem());
        tmpField.updateFieldInfo(name, type, fieldNo, crop, area);
        populateCmbFields(tmp);
        addFieldDialog.setVisible(false);
        addFieldName.setText("");
        addFieldType.setText("");
        addFieldNo.setValue(0);
        addFieldCrop.setText("");
        addFieldArea.setValue(0);
    }//GEN-LAST:event_editBtnActionPerformed

    private void editFieldBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFieldBtnActionPerformed
        // TODO add your handling code here:
        Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
        if (tmp.getAllFields().length == 0) {
            JOptionPane.showMessageDialog(farmView, "There is no Field to edit");
        } else {
            Field tmpField = selectField(tmp, (Integer) cmbFields.getSelectedItem());
            showFieldPopup(tmpField);
        }
    }//GEN-LAST:event_editFieldBtnActionPerformed

    private void confirmAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAddBtnActionPerformed
        // TODO add your handling code here:
        String name = addFieldName.getText();
        String type = addFieldType.getText();
        int fieldNo = (int) addFieldNo.getValue();
        String crop = addFieldCrop.getText();
        float area = ((int) addFieldArea.getValue() / (float) 1);
        Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
        tmp.addField(name, type, fieldNo, crop, area);
        populateCmbFields(tmp);
        addFieldDialog.setVisible(false);
        addFieldName.setText("");
        addFieldType.setText("");
        addFieldNo.setValue(0);
        addFieldCrop.setText("");
        addFieldArea.setValue(0);
    }//GEN-LAST:event_confirmAddBtnActionPerformed

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelBtnActionPerformed
        // TODO add your handling code here:
        addFieldDialog.setVisible(false);
        addFieldName.setText("");
        addFieldType.setText("");
        addFieldNo.setValue(0);
        addFieldCrop.setText("");
        addFieldArea.setValue(0);
    }//GEN-LAST:event_cancelBtnActionPerformed

    private void removeFieldBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFieldBtnActionPerformed
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        if (tmpFarm.getAllFields().length == 0) {
            JOptionPane.showMessageDialog(farmView, "There is no Field to remove");
        } else {
            Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
            fieldNameLbl.setText(tmpField.getName());
            deleteFieldDialog.pack();
            deleteFieldDialog.setVisible(true);
        }
    }//GEN-LAST:event_removeFieldBtnActionPerformed

    private void cancelFieldDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelFieldDeleteActionPerformed
        deleteFieldDialog.setVisible(false);
    }//GEN-LAST:event_cancelFieldDeleteActionPerformed

    private void confirmFieldDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmFieldDeleteActionPerformed
        // TODO add your handling code here:
        Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
        int id = (Integer) cmbFields.getSelectedItem();
        tmp.removeField(id);
        populateCmbFields(tmp);
        deleteFieldDialog.setVisible(false);
    }//GEN-LAST:event_confirmFieldDeleteActionPerformed

    private void viewFarmersBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewFarmersBtnActionPerformed

        populateCmbFarmers();
        populateCmbSearch();
        this.setVisible(false);
        showFarmersView();

    }//GEN-LAST:event_viewFarmersBtnActionPerformed

    private void showFarmerDetailsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFarmerDetailsBtnActionPerformed
        if (theFarmers.getAllFarmers().length == 0) {
            JOptionPane.showMessageDialog(this, "There is no Farmer to show");
        } else {
            int id = Integer.parseInt(cmbFarmers.getSelectedItem().toString());
            Farmer tmp = theFarmers.getFarmerByNumber(id);
            populateFarmerDetails(tmp);
        }
    }//GEN-LAST:event_showFarmerDetailsBtnActionPerformed

    private void addFarmerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFarmerBtnActionPerformed
        // TODO add your handling code here:
        showFarmerPopup(null);
    }//GEN-LAST:event_addFarmerBtnActionPerformed

    private void editFarmerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFarmerBtnActionPerformed
        if (theFarmers.getAllFarmers().length == 0) {
            JOptionPane.showMessageDialog(this, "There is no Farmer to edit");
        } else {
            int id = Integer.parseInt(cmbFarmers.getSelectedItem().toString());
            Farmer tmp = theFarmers.getFarmerByNumber(id);
            showFarmerPopup(tmp);
        }
    }//GEN-LAST:event_editFarmerBtnActionPerformed

    private void confirmAddFarmerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAddFarmerBtnActionPerformed
        // TODO add your handling code here:
        String name = addFarmerName.getText();
        String email = addFarmerEmail.getText();
        String phone = addFarmerPhone.getText();
        int id = (int) addFarmerId.getValue();
        SetOfFarms farms = new SetOfFarms();
        for (int i = 0; i < associatedFarmsTable.getRowCount(); i++) {
            Farm tmp = selectFarm((Integer) associatedFarmsTable.getValueAt(i, 0));
            farms.addFarmAlreadyInSystem(tmp);
        }
        theFarmers.addFarmer(name, email, phone, id, farms);
        populateCmbFarmers();
        DefaultTableModel model = (DefaultTableModel) associatedFarmsTable.getModel();
        model.setRowCount(0);
        DefaultTableModel model1 = (DefaultTableModel) allFarmsTable.getModel();
        model1.setRowCount(0);
        addFarmerDialog.setVisible(false);
    }//GEN-LAST:event_confirmAddFarmerBtnActionPerformed

    private void confirmEditFarmerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEditFarmerBtnActionPerformed
        // TODO add your handling code here:
        String name = addFarmerName.getText();
        String email = addFarmerEmail.getText();
        String phone = addFarmerPhone.getText();
        int id = (int) addFarmerId.getValue();

        int ida = Integer.parseInt(cmbFarmers.getSelectedItem().toString());
        Farmer tmp = theFarmers.getFarmerByNumber(ida);
        tmp.updateFarmerInfo(name, email, phone, id, tmp.getAssociatedFarms());
        populateCmbFarmers();
        DefaultTableModel model = (DefaultTableModel) associatedFarmsTable.getModel();
        model.setRowCount(0);
        DefaultTableModel model1 = (DefaultTableModel) allFarmsTable.getModel();
        model1.setRowCount(0);
        addFarmerDialog.setVisible(false);
    }//GEN-LAST:event_confirmEditFarmerBtnActionPerformed

    private void cancelAddFarmerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelAddFarmerBtnActionPerformed
        // TODO add your handling code here:
        addFarmerName.setText("");
        addFarmerEmail.setText("");
        addFarmerPhone.setText("");
        addFarmerId.setValue(0);
        DefaultTableModel model = (DefaultTableModel) associatedFarmsTable.getModel();
        model.setRowCount(0);
        DefaultTableModel model1 = (DefaultTableModel) allFarmsTable.getModel();
        model1.setRowCount(0);
        addFarmerDialog.setVisible(false);
    }//GEN-LAST:event_cancelAddFarmerBtnActionPerformed

    private void deleteFarmerBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteFarmerBtnActionPerformed
        if (theFarmers.getAllFarmers().length == 0) {
            JOptionPane.showMessageDialog(this, "There is no Farmer to delete");
        } else {
            deleteFarmerName.setText(cmbFarmers.getSelectedItem().toString());
            deleteFarmerDialog.pack();
            deleteFarmerDialog.setVisible(true);
        }
    }//GEN-LAST:event_deleteFarmerBtnActionPerformed

    private void confirmFarmerDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmFarmerDeleteActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(cmbFarmers.getSelectedItem().toString());
        Farmer tmp = theFarmers.getFarmerByNumber(id);
        theFarmers.removeFarmer(tmp.getId());
        populateCmbFarmers();
        deleteFarmerDialog.setVisible(false);
    }//GEN-LAST:event_confirmFarmerDeleteActionPerformed

    private void cancelFarmerDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelFarmerDeleteActionPerformed
        deleteFarmerDialog.setVisible(false);
    }//GEN-LAST:event_cancelFarmerDeleteActionPerformed

    private void addToAssociatedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToAssociatedBtnActionPerformed
        int row = allFarmsTable.getSelectedRow();
        Farm tmp = selectFarm((Integer) allFarmsTable.getValueAt(row, 0));
        int ida = Integer.parseInt(cmbFarmers.getSelectedItem().toString());
        Farmer tmpFarmer = theFarmers.getFarmerByNumber(ida);
        tmpFarmer.getAssociatedFarms().addFarmAlreadyInSystem(tmp);

        SetOfFarms tmpFarms = theFarmers.getFarmerByNumber(tmpFarmer.getId()).getAssociatedFarms();
        Object[] data = new Object[1];
        DefaultTableModel model = (DefaultTableModel) associatedFarmsTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < tmpFarms.getAllFarms().size(); i++) {
            data[0] = tmpFarms.getAllFarms().get(i).getFarmNo();
            model.addRow(data);
        }
    }//GEN-LAST:event_addToAssociatedBtnActionPerformed

    private void removeFromAssociatedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeFromAssociatedBtnActionPerformed
        // TODO add your handling code here:
        int row = associatedFarmsTable.getSelectedRow();
        Farm tmp = selectFarm((Integer) associatedFarmsTable.getValueAt(row, 0));
        int ida = Integer.parseInt(cmbFarmers.getSelectedItem().toString());
        Farmer tmpFarmer = theFarmers.getFarmerByNumber(ida);
        tmpFarmer.getAssociatedFarms().removeFarm(tmp);

        SetOfFarms tmpFarms = tmpFarmer.getAssociatedFarms();
        Object[] data = new Object[1];
        DefaultTableModel model = (DefaultTableModel) associatedFarmsTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < tmpFarms.getAllFarms().size(); i++) {
            data[0] = tmpFarms.getAllFarms().get(i).getName();
            model.addRow(data);
        }
    }//GEN-LAST:event_removeFromAssociatedBtnActionPerformed

    private void showFieldBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showFieldBtnActionPerformed
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        if (tmpFarm.getAllFields().length == 0) {
            JOptionPane.showMessageDialog(this, "There Are No Fields In the System");
        } else {
            Field tmp = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());

            lblFieldName.setText("Name: " + tmp.getName());
            lblFieldType.setText("Type: " + tmp.getType());
            lblFieldCrop.setText("Crop: " + tmp.getCrop().getName());
            cmbSensors.removeAllItems();
            populateCmbFieldStations(tmp);
            farmView.setVisible(false);
            showFieldView();
        }
    }//GEN-LAST:event_showFieldBtnActionPerformed

    private void farmerSearchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_farmerSearchBtnActionPerformed
        if (cmbSearch.getSelectedIndex() == 0) {
            farmerSearch("name");
        } else {
            if (cmbSearch.getSelectedIndex() == 1) {
                farmerSearch("id");
            } else {
                if (cmbSearch.getSelectedIndex() == 2) {
                    farmerSearch("tel");
                } else {
                    if (cmbSearch.getSelectedIndex() == 3) {
                        farmerSearch("email");
                    }
                }
            }
        }
    }//GEN-LAST:event_farmerSearchBtnActionPerformed

    private void addFieldStationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFieldStationBtnActionPerformed
        showFieldStationPopup(null);
    }//GEN-LAST:event_addFieldStationBtnActionPerformed

    private void confirmAddFieldstationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmAddFieldstationActionPerformed
        // Capture variables from the Form
        int x = (Integer) fieldStationXCoord1.getValue();
        int y = (Integer) fieldStationYCoord1.getValue();
        String name = fieldStationName1.getText();
        int id = (Integer) fieldStationId1.getValue();

        // Create a New Location and FieldStation
        Location location = new Location(x, y, fieldStationType1.getText());

        // Get the selected Farm and Field
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        tmpField.addFieldStation(location, name, id);
        populateCmbFieldStations(tmpField);

        addFieldStationDialog.setVisible(false);
    }//GEN-LAST:event_confirmAddFieldstationActionPerformed

    private void confirmEditFieldstationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEditFieldstationActionPerformed
        // Capture variables from the Form

        String name = fieldStationName.getText();
        int id = (Integer) fieldStationId.getValue();
        float x;
        if (fieldStationXCoord.getValue() instanceof Float) {
            x = (Float) fieldStationXCoord.getValue();

        } else {
            int area1 = (int) fieldStationXCoord.getValue();
            x = (float) area1;
        }
        float y;
        if (fieldStationYCoord.getValue() instanceof Float) {
            y = (Float) fieldStationYCoord.getValue();

        } else {
            int area1 = (int) fieldStationYCoord.getValue() / 1;
            y = (float) area1;
        }
        // Create a New Location and FieldStation
        Location location = new Location(x, y, fieldStationType1.getText());

        // Get the selected Farm and Field
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        FieldStation fieldStation = selectFieldStation(tmpField, Integer.parseInt((String) cmbFieldStations.getSelectedItem()));
        fieldStation.updateFieldStationInfo(name, location, id);

        // Refresh The Combo Box
        populateCmbFieldStations(tmpField);

        editFieldStationDialog.setVisible(false);
    }//GEN-LAST:event_confirmEditFieldstationActionPerformed

    private void confirmEditFarmBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEditFarmBtn1ActionPerformed
        String name = nameInput1.getText();
        float x;
        if (xCoordSpin1.getValue() instanceof Float) {
            x = (Float) xCoordSpin1.getValue();

        } else {
            int area1 = (int) xCoordSpin1.getValue();
            x = (float) area1;
        }
        float y;
        if (yCoordSpin1.getValue() instanceof Float) {
            y = (Float) yCoordSpin1.getValue();

        } else {
            int area1 = (int) yCoordSpin1.getValue() / 1;
            y = (float) area1;
        }
        String type = typeInput1.getText();
        int id = (Integer) fieldIdSpin.getValue();

        Farm tmp = selectFarm((Integer) cmbFarms.getSelectedItem());
        tmp.updateFarmInfo(name, new Location(x, y, type), id);

        nameInput1.setText("");
        xCoordSpin1.setValue(0);
        yCoordSpin1.setValue(0);
        typeInput1.setText("");
        fieldIdSpin.setValue(0);
        editFarmDialog.setVisible(false);
    }//GEN-LAST:event_confirmEditFarmBtn1ActionPerformed

    private void farmCancelBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_farmCancelBtn1ActionPerformed
        nameInput1.setText("");
        xCoordSpin1.setValue(0);
        yCoordSpin1.setValue(0);
        typeInput1.setText("");
        fieldIdSpin.setValue(0);
        editFarmDialog.setVisible(false);
    }//GEN-LAST:event_farmCancelBtn1ActionPerformed

    private void btnEditFieldstationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditFieldstationActionPerformed
        // Get the Currently selected Farm, Field and Fieldstation
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        if (tmpField.getAllFieldStations().length == 0) {
            JOptionPane.showMessageDialog(fieldsDialog, "There are no Field Stations in the System");
        } else {
            // Second parameter is Integer conversion of a String casted from an Object
            FieldStation fieldStation = selectFieldStation(tmpField, Integer.valueOf((String) cmbFieldStations.getSelectedItem()));

            // Fill in the Form with Field Station's data
            fieldStationName.setText(fieldStation.getName());
            fieldStationXCoord.setValue(fieldStation.getLocation().getYCoord());
            fieldStationYCoord.setValue(fieldStation.getLocation().getXCoord());
            fieldStationType.setText(fieldStation.getLocation().getType());
            fieldStationId.setValue(fieldStation.getFieldStationNo());

            // Show Dialog for editing
            editFieldStationDialog.setVisible(true);
            editFieldStationDialog.pack();
        }
    }//GEN-LAST:event_btnEditFieldstationActionPerformed

    private void cancelFieldStationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelFieldStationActionPerformed
        fieldStationName.setText("");
        fieldStationXCoord.setValue(0);
        fieldStationYCoord.setValue(0);
        fieldStationType.setText("");
        fieldStationId.setValue(0);
        editFieldStationDialog.setVisible(false);
    }//GEN-LAST:event_cancelFieldStationActionPerformed

    private void cancelFieldStation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelFieldStation1ActionPerformed
        fieldStationName1.setText("");
        fieldStationXCoord1.setValue(0);
        fieldStationYCoord1.setValue(0);
        fieldStationType1.setText("");
        fieldStationId1.setValue(0);
        addFieldStationDialog.setVisible(false);
    }//GEN-LAST:event_cancelFieldStation1ActionPerformed

    private void deleteFieldStationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteFieldStationBtnActionPerformed
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        if (tmpField.getAllFieldStations().length == 0) {
            JOptionPane.showMessageDialog(fieldsDialog, "There are no Field Stations in the System");
        } else {
            // Second parameter is Integer conversion of a String casted from an Object
            FieldStation fieldStation = selectFieldStation(tmpField, Integer.valueOf((String) cmbFieldStations.getSelectedItem()));
            deleteFieldStationLbl.setText(fieldStation.getName());
            deleteFieldStationDialog.pack();
            deleteFieldStationDialog.setVisible(true);
        }
    }//GEN-LAST:event_deleteFieldStationBtnActionPerformed

    private void deleteFieldStationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteFieldStationActionPerformed
        // Get the Currently selected Farm, Field and Fieldstation
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());

        // Second parameter is Integer conversion of a String casted from an Object
        FieldStation fieldStation = selectFieldStation(tmpField, Integer.valueOf((String) cmbFieldStations.getSelectedItem()));

        tmpField.removeFieldStation(fieldStation.getFieldStationNo());
    }//GEN-LAST:event_deleteFieldStationActionPerformed

    private void cancelDeleteFieldStationBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelDeleteFieldStationBtnActionPerformed
        deleteFieldStationDialog.setVisible(false);
    }//GEN-LAST:event_cancelDeleteFieldStationBtnActionPerformed

    private void plantCropBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_plantCropBtnActionPerformed
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        if (tmpField.setCurrentPlanting(new Date())) {
            JOptionPane.showMessageDialog(fieldsDialog, "Your Crop Was Planted!");
        } else {
            JOptionPane.showMessageDialog(fieldsDialog, "There Is Already A Crop Planted In This Field");
        }

    }//GEN-LAST:event_plantCropBtnActionPerformed

    private void harvestCropBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_harvestCropBtnActionPerformed
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        if (tmpField.getCurrentPlanting() != null) {
            String yield = JOptionPane.showInputDialog("Enter the yield of the harvest");
            if (tmpField.harvestCurrentPlanting(new Date(), Integer.parseInt(yield))) {
                JOptionPane.showMessageDialog(fieldsDialog, "Your Crop Was Harvested!");
            }
        } else {
            JOptionPane.showMessageDialog(fieldsDialog, "There Is No Crop Planted In This Field");
        }


    }//GEN-LAST:event_harvestCropBtnActionPerformed

    private void showCropHistoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showCropHistoryBtnActionPerformed
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        Planting[] plantings = tmpField.getPlantings();
        Object[] data = new Object[4];
        DefaultTableModel model = (DefaultTableModel) fieldHistoryTable.getModel();
        model.setRowCount(0);
        for (int i = 0; i < plantings.length; i++) {
            data[0] = plantings[i].getCropName();
            data[1] = plantings[i].getPlantDate();
            data[2] = plantings[i].getHarvestDate();
            data[3] = plantings[i].getYield();
            model.addRow(data);
        }

        showFieldHistory.pack();
        showFieldHistory.setVisible(true);
    }//GEN-LAST:event_showCropHistoryBtnActionPerformed

    private void updateFieldStationDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateFieldStationDetailsActionPerformed
        // Get the Currently selected Farm, Field and Fieldstation
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbFields.getSelectedItem());
        // Second parameter is Integer conversion of a String casted from an Object
        if (tmpField.getAllFieldStations().length != 0) {
            FieldStation fieldStation = selectFieldStation(tmpField, Integer.valueOf((String) cmbFieldStations.getSelectedItem()));
            fieldStationNameLbl.setText(fieldStation.getName());
            xLocationLbl.setText(Float.toString(fieldStation.getLocation().getXCoord()));
            yLocationLbl.setText(Float.toString(fieldStation.getLocation().getYCoord()));
            typeLocationLbl.setText(fieldStation.getLocation().getType());
            populateCmbSensors(fieldStation);
        } else {
            JOptionPane.showMessageDialog(fieldsDialog, "There are no Field Stations in the System");
        }

    }//GEN-LAST:event_updateFieldStationDetailsActionPerformed

    private void addSensorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addSensorBtnActionPerformed
        showSensorPopup();
    }//GEN-LAST:event_addSensorBtnActionPerformed

    private void editSensorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editSensorBtnActionPerformed
        editSensorDialog.pack();
        editSensorDialog.setVisible(true);
    }//GEN-LAST:event_editSensorBtnActionPerformed

    private void removeSensorBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSensorBtnActionPerformed
       removeSensorDialog.pack();
       removeSensorDialog.setVisible(true);
       removeSensor();
    }//GEN-LAST:event_removeSensorBtnActionPerformed
    private void removeSensor() {
        FieldStation fieldStation = new FieldStation();
        if (fieldStation.getAllSensors().length == 0) {
            JOptionPane.showMessageDialog(this, "There is no Sensor to delete");
        } else {
            deleteSensorName.setText(cmbSensors.getSelectedItem().toString());
            removeSensorDialog.pack();
            removeSensorDialog.setVisible(true);
        }

    }
    private void closeHistoryBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeHistoryBtnActionPerformed
        showFieldHistory.setVisible(false);
    }//GEN-LAST:event_closeHistoryBtnActionPerformed

    private void homeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtnActionPerformed
        farmView.setVisible(false);
        showHomeView();
    }//GEN-LAST:event_homeBtnActionPerformed

    private void homeBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_homeBtn1ActionPerformed
        farmerDialog.setVisible(false);
        showHomeView();
    }//GEN-LAST:event_homeBtn1ActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        fieldsDialog.setVisible(false);
        farmView.setVisible(true);
    }//GEN-LAST:event_backBtnActionPerformed

    private void sensorDialogAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensorDialogAddBtnActionPerformed
        
        int xspinner = (int) sensorDialogLocationXSpinner.getValue();
        int yspinner = (int) sensorDialogLocationYSpinner.getValue();
        int serialNo = Integer.parseInt(sensorDialogSerialNumber.getText());
        int interval = Integer.parseInt(sensorDialogInterval.getText());
        String locType = sensorDialogLocationType.getText();
        
        Farm tmpFarm = selectFarm((Integer) cmbFarms.getSelectedItem());
        Field tmpField = selectField(tmpFarm, (Integer) cmbSensors.getSelectedItem());
        FieldStation tmpFS =  selectFieldStation(tmpField,(Integer) cmbSensors.getSelectedItem());
        tmpFS.addSensor(new Location(xspinner, yspinner, locType), interval, serialNo, tmpFS);
    }//GEN-LAST:event_sensorDialogAddBtnActionPerformed

    private void sensorDialogCancelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensorDialogCancelBtnActionPerformed
        // TODO add your handling code here:
        sensorDialogLocationXSpinner.setValue(0);
        sensorDialogLocationYSpinner.setValue(0);
        sensorDialogSerialNumber.setText("");
        sensorDialogInterval.setText("");
        addSensorDialog.setVisible(false);
    }//GEN-LAST:event_sensorDialogCancelBtnActionPerformed

    private void sensorDialogLocationTypeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensorDialogLocationTypeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sensorDialogLocationTypeActionPerformed

    private void sensorDeleteConfirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensorDeleteConfirmActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sensorDeleteConfirmActionPerformed

    private void sensorDeleteCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sensorDeleteCancelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sensorDeleteCancelActionPerformed

    private void confirmEditSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmEditSensorActionPerformed
        // TODO add your handling code here:
        String snsrName = sensorName.getText();
        int sensorXcoord = (int)sensorXcord.getValue();
        int sensorYcoord = (int)sensorYcord.getValue();
        String snsrType = sensorType.getText();
        int sensorId = (int) sensorID.getValue();
        
    }//GEN-LAST:event_confirmEditSensorActionPerformed

    private void cancelSensorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelSensorActionPerformed
        // TODO add your handling code here:
        sensorName.setText("");
        sensorXcord.setValue(0);
        sensorYcord.setValue(0);
        sensorType.setText("");
        sensorID.setValue(0);
        editSensorDialog.setVisible(false);
    }//GEN-LAST:event_cancelSensorActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new GUI().setVisible(true);
                } catch (ParseException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ConfirmDelBtn;
    private javax.swing.JButton addFarmBtn;
    private javax.swing.JDialog addFarmDialog;
    private javax.swing.JButton addFarmerBtn;
    private javax.swing.JDialog addFarmerDialog;
    private javax.swing.JTextField addFarmerEmail;
    private javax.swing.JSpinner addFarmerId;
    private javax.swing.JTextField addFarmerName;
    private javax.swing.JTextField addFarmerPhone;
    private javax.swing.JSpinner addFieldArea;
    private javax.swing.JButton addFieldBtn;
    private javax.swing.JTextField addFieldCrop;
    private javax.swing.JDialog addFieldDialog;
    private javax.swing.JTextField addFieldName;
    private javax.swing.JSpinner addFieldNo;
    private javax.swing.JButton addFieldStationBtn;
    private javax.swing.JDialog addFieldStationDialog;
    private javax.swing.JTextField addFieldType;
    private javax.swing.JButton addSensorBtn;
    private javax.swing.JDialog addSensorDialog;
    private javax.swing.JButton addToAssociatedBtn;
    private javax.swing.JTable allFarmsTable;
    private javax.swing.JTable associatedFarmsTable;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton btnEditFieldstation;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton cancelAddFarmerBtn;
    private javax.swing.JButton cancelBtn;
    private javax.swing.JButton cancelDelBtn;
    private javax.swing.JButton cancelDeleteFieldStationBtn;
    private javax.swing.JButton cancelFarmerDelete;
    private javax.swing.JButton cancelFieldDelete;
    private javax.swing.JButton cancelFieldStation;
    private javax.swing.JButton cancelFieldStation1;
    private javax.swing.JButton cancelSensor;
    private javax.swing.JButton closeHistoryBtn;
    private javax.swing.JComboBox<String> cmbFarmers;
    private javax.swing.JComboBox cmbFarms;
    private javax.swing.JComboBox cmbFieldStations;
    private javax.swing.JComboBox cmbFields;
    private javax.swing.JComboBox cmbSearch;
    private javax.swing.JComboBox cmbSensors;
    private javax.swing.JButton confirmAddBtn;
    private javax.swing.JButton confirmAddFarmerBtn;
    private javax.swing.JButton confirmAddFieldstation;
    private javax.swing.JButton confirmEditFarmBtn1;
    private javax.swing.JButton confirmEditFarmerBtn;
    private javax.swing.JButton confirmEditFieldstation;
    private javax.swing.JButton confirmEditSensor;
    private javax.swing.JButton confirmFarmerDelete;
    private javax.swing.JButton confirmFieldDelete;
    private javax.swing.JButton createFarmBtn;
    private javax.swing.JButton delFarmBtn;
    private javax.swing.JLabel delFarmNameLbl;
    private javax.swing.JDialog deleteFarmDialog;
    private javax.swing.JButton deleteFarmerBtn;
    private javax.swing.JDialog deleteFarmerDialog;
    private javax.swing.JLabel deleteFarmerName;
    private javax.swing.JDialog deleteFieldDialog;
    private javax.swing.JButton deleteFieldStation;
    private javax.swing.JButton deleteFieldStationBtn;
    private javax.swing.JDialog deleteFieldStationDialog;
    private javax.swing.JLabel deleteFieldStationLbl;
    private javax.swing.JLabel deleteSensorName;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton editFarmBtn;
    private javax.swing.JDialog editFarmDialog;
    private javax.swing.JButton editFarmerBtn;
    private javax.swing.JButton editFieldBtn;
    private javax.swing.JDialog editFieldStationDialog;
    private javax.swing.JButton editSensorBtn;
    private javax.swing.JDialog editSensorDialog;
    private javax.swing.JButton farmCancelBtn;
    private javax.swing.JButton farmCancelBtn1;
    private javax.swing.JDialog farmView;
    private javax.swing.JDialog farmerDialog;
    private javax.swing.JLabel farmerEmailLbl;
    private javax.swing.JLabel farmerIdLbl;
    private javax.swing.JLabel farmerNameLbl;
    private javax.swing.JLabel farmerPhoneLbl;
    private javax.swing.JButton farmerSearchBtn;
    private javax.swing.JTable farmerTable;
    private javax.swing.JLabel farmsLbl;
    private javax.swing.JTable fieldHistoryTable;
    private javax.swing.JSpinner fieldIdSpin;
    private javax.swing.JSpinner fieldIdSpin1;
    private javax.swing.JLabel fieldLbl;
    private javax.swing.JLabel fieldNameLbl;
    private javax.swing.JSpinner fieldStationId;
    private javax.swing.JSpinner fieldStationId1;
    private javax.swing.JTextField fieldStationName;
    private javax.swing.JTextField fieldStationName1;
    private javax.swing.JLabel fieldStationNameLbl;
    private javax.swing.JTextField fieldStationType;
    private javax.swing.JTextField fieldStationType1;
    private javax.swing.JSpinner fieldStationXCoord;
    private javax.swing.JSpinner fieldStationXCoord1;
    private javax.swing.JSpinner fieldStationYCoord;
    private javax.swing.JSpinner fieldStationYCoord1;
    private javax.swing.JDialog fieldsDialog;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton harvestCropBtn;
    private javax.swing.JButton homeBtn;
    private javax.swing.JButton homeBtn1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JLabel lblFieldCrop;
    private javax.swing.JLabel lblFieldName;
    private javax.swing.JLabel lblFieldType;
    private javax.swing.JTextField nameInput;
    private javax.swing.JTextField nameInput1;
    private javax.swing.JLabel nameLblAddFarm;
    private javax.swing.JLabel nameLblAddFarm1;
    private javax.swing.JButton plantCropBtn;
    private javax.swing.JButton removeFieldBtn;
    private javax.swing.JButton removeFromAssociatedBtn;
    private javax.swing.JButton removeSensorBtn;
    private javax.swing.JDialog removeSensorDialog;
    private javax.swing.JDialog searchFailed;
    private javax.swing.JTextField searchField;
    private javax.swing.JButton sensorDeleteCancel;
    private javax.swing.JButton sensorDeleteConfirm;
    private javax.swing.JButton sensorDialogAddBtn;
    private javax.swing.JButton sensorDialogCancelBtn;
    private javax.swing.JTextField sensorDialogInterval;
    private javax.swing.JLabel sensorDialogIntervalLbl;
    private javax.swing.JTextField sensorDialogLocationType;
    private javax.swing.JLabel sensorDialogLocationX;
    private javax.swing.JSpinner sensorDialogLocationXSpinner;
    private javax.swing.JLabel sensorDialogLocationY;
    private javax.swing.JSpinner sensorDialogLocationYSpinner;
    private javax.swing.JTextField sensorDialogSerialNumber;
    private javax.swing.JLabel sensorDialogSerialNumberLbl;
    private javax.swing.JSpinner sensorID;
    private javax.swing.JTextField sensorName;
    private javax.swing.JTextField sensorType;
    private javax.swing.JSpinner sensorXcord;
    private javax.swing.JSpinner sensorYcord;
    private javax.swing.JButton showCropHistoryBtn;
    private javax.swing.JButton showFarmerDetailsBtn;
    private javax.swing.JButton showFarmsBtn;
    private javax.swing.JButton showFieldBtn;
    private javax.swing.JDialog showFieldHistory;
    private javax.swing.JTextField typeInput;
    private javax.swing.JTextField typeInput1;
    private javax.swing.JLabel typeLocationLbl;
    private javax.swing.JButton updateFieldStationDetails;
    private javax.swing.JButton viewFarmersBtn;
    private javax.swing.JSpinner xCoordSpin;
    private javax.swing.JSpinner xCoordSpin1;
    private javax.swing.JLabel xLabelAddFarm;
    private javax.swing.JLabel xLabelAddFarm1;
    private javax.swing.JLabel xLocationLbl;
    private javax.swing.JSpinner yCoordSpin;
    private javax.swing.JSpinner yCoordSpin1;
    private javax.swing.JLabel yLocationLbl;
    // End of variables declaration//GEN-END:variables

}
