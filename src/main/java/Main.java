
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
		List<Employee> list = parseCSV(columnMapping, "data.csv");
		String json = listToJson(list);
		saveStringToFile(json);

//		unzipSavedGames(PATH + "saved_games.zip", PATH);

	}

	public static void saveStringToFile(String json) {
		try (FileWriter writer = new FileWriter("data.json", true)) {
			writer.write(json);
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}
	}

	public static List<Employee> parseCSV(String[] columnMapping, String filename) {
		try (CSVReader reader = new CSVReader(new FileReader(filename))) {
			ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
			strategy.setType(Employee.class);
			strategy.setColumnMapping("id", "firstName", "lastName", "country", "age");
			CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
					.withMappingStrategy(strategy)
					.build();
			List<Employee> staff = csv.parse();
			return  staff;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static String listToJson(List<Employee> list) {
		Type listType = new TypeToken<List<Employee>>() {}.getType();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String jsonString = gson.toJson(list, listType);

		return jsonString;
	}

}
