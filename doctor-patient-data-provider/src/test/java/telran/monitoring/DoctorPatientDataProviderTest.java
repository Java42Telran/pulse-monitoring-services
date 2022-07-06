package telran.monitoring;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.monitoring.dto.NotificationData;
import static telran.monitoring.api.ApiConstants.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DoctorPatientDataProviderTest {
	private static final long PATIENTID = 123;
	@Autowired
	MockMvc mockMvc;
	
	@Test
	@Sql(scripts = "testDB.sql")
	void normalFlowTest() throws Exception{
		NotificationData expected = new NotificationData("vasya@gmail.com", "Vasya", "Shlomo");
		String jsonRes = mockMvc.perform(get(DOCTOR_PATIENT_PROVIDER_URL + PATIENTID))
				.andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		ObjectMapper mapper = new ObjectMapper();
		NotificationData actual = mapper.readValue(jsonRes, NotificationData.class);
		assertEquals(expected, actual);
	}
}
