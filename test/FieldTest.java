/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

public class FieldTest {
    Field instance = new Field("test","testType", 0, "cropname", 0.0f);
    Location location = new Location(10, 20, "Fake Location");
    
    public FieldTest() {
    }       
    @Before
    public void setUp() {
        instance = new Field("Field Test", "Test Type", 45, "Crop Name", 0);
        instance.addFieldStation(location, "Field 1");
        int tmp = instance.getAllFieldStations()[0].getFieldStationNo();
        
    }
    @Ignore
    @Test
    public void testGetFieldStationByName() { 
        //Exact
        String name = "Field 1";
        FieldStation tmp = instance.getFieldStationByName(name);
        String fieldName = tmp.getName(); 
        assertEquals(name, fieldName); 
        //Upper case        
        FieldStation tmp2 = instance.getFieldStationByName("FIELD 1");
        String fieldName2 = tmp2.getName(); 
        assertEquals(name, fieldName2); 
        //Lower Case        
        FieldStation tmp3 = instance.getFieldStationByName("field 1");
        String fieldName3 = tmp3.getName(); 
        assertEquals(name, fieldName3); 

    }    
    @Ignore
    @Test
    public void testAddFieldStation() {
        //Act
        FieldStation result = instance.getFieldStationByNumber(202);
        //Assert
        assertNull(result);      
        //then remove
        instance.addFieldStation(location, "Test");            
        //Arrange
        FieldStation result1 = instance.getFieldStationByNumber(202);
        //Assert
        assertNotNull(result1);        
        //return tmp in the if and then return null outside of it

    }
    @Ignore
    @Test
    public void testRemoveFieldStation() {
        //Arrange
        instance.addFieldStation(location, "Test"); //Remember to update this
        //constructor when jonas is done
        //Act
        FieldStation result = instance.getFieldStationByNumber(101);
        //Assert
        assertNotNull(result);      
        //then remove
        instance.removeFieldStation(result);            
        //Arrange
        FieldStation result1 = instance.getFieldStationByNumber(101);
        //Assert
        assertNull(result1);
    }
    @Test
    public void testUpdateFieldInfo() {
        int fieldNo = 10101;
        String name = "FieldLocation";
        String type = "Crop type";
        String cropName = "Crop name";
        Float cropArea = 0.5f;
        
        //Get the name       
        assertEquals("test", instance.getName());
        //Get the type
        assertEquals("testType", instance.getType());
        //Get the field number
        assertEquals("0", Integer.toString((int) instance.getFieldNumber()));
        //Get the crop name
        assertEquals("0", instance.getName());        
        //get the crop area
        assertEquals(0.5f, 0,instance.getArea());
      
        instance.updateFieldInfo(name, type, fieldNo, cropName, cropArea);
       //Get the name       
        assertEquals(name, instance.getName());
        //Get the type
        assertEquals(type, instance.getType());
        //Get the field number
        assertEquals("10101", Integer.toString((int) instance.getFieldNumber()));
        //Get the crop name
        assertEquals("0", instance.getName());        
        //get the crop area
        assertEquals(0.5f, 0,instance.getArea());
  
    }
}
