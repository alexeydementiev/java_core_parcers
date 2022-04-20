import java.awt.event.TextEvent;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MainTest {
	Main sut;

	@Test
	public void testCSV_parser() {
		String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
		List<Employee> employeeList = createEmployees();
		List<Employee> listCsv = sut.parseCSV(columnMapping, "data.csv");
		Assertions.assertNotNull(listCsv);
		Assertions.assertEquals(2, listCsv.size());
		Assertions.assertEquals(employeeList.get(0).id,listCsv.get(0).id);
	}

	@Test
	public void testListToJson() {
		List<Employee> employeeList = createEmployees();
		String stringJson = sut.listToJson(employeeList);
		Assertions.assertNotNull(stringJson);
		String expected = "[{\"id\":1,\"firstName\":\"John\",\"lastName\":\"Smith\",\"country\":\"USA\",\"age\":25},{\"id\":2,\"firstName\":\"Inav\",\"lastName\":\"Petrov\",\"country\":\"RU\",\"age\":23}]";
		Assertions.assertEquals(expected, stringJson);
	}

	//Harmcrest
	@Test
	public void testCSV_parser_harmcrest() {
		String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
		List<Employee> employeeList = createEmployees();
		List<Employee> listCsv = sut.parseCSV(columnMapping, "data.csv");
		assertThat(2, equalTo(listCsv.size()));
		assertThat(employeeList.get(0).id, equalTo(listCsv.get(0).id));
	}


	public List<Employee> createEmployees() {
		List<Employee> list = new ArrayList<>();
		list.add(new Employee(1,"John","Smith","USA",25));
		list.add(new Employee(2,"Inav","Petrov","RU",23));
		return list;
	}


}
