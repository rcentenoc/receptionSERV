package pe.mm.reception;
import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import pe.mm.reception.core.dto.DataSendDTO;
import pe.mm.reception.core.dto.Memory;
import pe.mm.reception.core.dto.MemoryVariablesLine;
import pe.mm.reception.core.model.Files;
import pe.mm.reception.core.model.Plant;
import pe.mm.reception.core.service.DataServiceImpl;
import pe.mm.reception.core.service.MemoryRedisServiceImpl;
@RunWith(MockitoJUnitRunner.class)
public class ReceptionApplicationTests {
	
	@InjectMocks
	private static DataServiceImpl serviceIWantToTest = new DataServiceImpl();

	@Mock
	private MemoryRedisServiceImpl memoryRedisServiceImpl= new MemoryRedisServiceImpl();

	// @Mock
    // private MemoryRedisServiceImpl memoryRedisServiceImpl;

	
 	
	@Test
	public void greetingsTest() {
		String mockGreetings = "mentor";
		String expected = "Hello ".concat(mockGreetings);
		String response = serviceIWantToTest.greetingsUser(mockGreetings);
		assertEquals(expected, response);
	}

	@Ignore
	@Test
	public void insertDataTest() {

		
						 
		long timestamp = 1695849300000L; 
		Date time = new Date();
		time.setTime(timestamp);

		String codeTest = "ISM_MEDIDOR_L5_PM5100";
		//List<String> data = new ArrayList<>();
		//List<String> headData = new ArrayList<>();
		List<String> headData = Arrays.asList("Current_A","Current_B","Current_C","Voltage_AB","Voltage_BC","Voltage_CA","Real_Power_Total_kW","Reactive_Power_Total_kVAR","Apparent_Power_Total_kVA","Power_Factor","Frequency","THD_Current_A_","THD_Current_B_","THD_Current_C_","Apparent_Energy_kVAh","Real_Energy_kWh","Reactive_Energy_kVARh");
		List<String> data = Arrays.asList("173.94","186.96","183.16","447.16","445.91","446.16","139.31","8.1.23","0.3","14099","59.96","13.02","9.20","7.70","7.70","295419.155","492293.265");
		DataSendDTO dataDTO = new DataSendDTO();
		dataDTO.setCode(codeTest);
		dataDTO.setTime(time);
		dataDTO.setData(data);
		dataDTO.setHead(headData);
		Files mockFile = new Files();
		Plant mockPlant = new Plant();
		mockPlant.setId(1);
		mockFile.setPlant(mockPlant);
		mockFile.setSendFrequency(300);
		mockFile.setSecondName("secondName");
		Memory mockMemory = new Memory();
		mockMemory.setFile(mockFile);
		mockMemory.setIdToColumnName(null);
		mockMemory.setProcessing(false);
		mockMemory.setTime(time);
		mockMemory.setTotalCount(17);

		Mockito.when(memoryRedisServiceImpl.isAvailableMemory()).thenReturn(true);
		Mockito.when(memoryRedisServiceImpl.getFileStatus(codeTest)).thenReturn(mockMemory);
		Mockito.when(memoryRedisServiceImpl.isPlantStatusEnable(1)).thenReturn(true);
		Mockito.when(memoryRedisServiceImpl.isFileIsProcessing(codeTest)).thenReturn(true);
		Mockito.doNothing().when(memoryRedisServiceImpl).updateMemoryStatusProcessingRedis(true,codeTest); // twice false

		MemoryVariablesLine mockMemoryVariablesLine = new MemoryVariablesLine();
		mockMemoryVariablesLine.setContinuing(null);
		mockMemoryVariablesLine.setData(null);
		mockMemoryVariablesLine.setDerives(null);
		mockMemoryVariablesLine.setLine(null);
		
		long mockMemoryTime =1695849000000L;
		Memory fileMemory = new Memory();
		fileMemory.setFile(mockFile);
		Date timeMemory = new Date();
		timeMemory.setTime(mockMemoryTime);
		fileMemory.setTime(timeMemory);
		Mockito.when(memoryRedisServiceImpl.getFileMemory(codeTest)).thenReturn(fileMemory);
		
		//  given(memoryRedisServiceImpl.isAvailableMemory())
        //          .willReturn(true);

		serviceIWantToTest.insert(dataDTO);
		//assertEquals(expected, response);
	}

}
