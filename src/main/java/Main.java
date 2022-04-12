
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main {

	public static List<Employee> employeeList = new ArrayList<>();

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
		String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};

		//CSV parser to JSON
		List<Employee> listCSV = parseCSV(columnMapping, "data.csv");
		String jsonCSV = listToJson(listCSV);
		saveStringToFile(jsonCSV, "dataCSV.json");
		// END --- CSV parser to JSON

		//XML parser to JSON
		List<Employee> listXML = parseXML(columnMapping, "data.xml");
		String jsonXML = listToJson(listCSV);
		saveStringToFile(jsonXML, "dataXML.json");

		//JSON parser
		List<Employee> listJson = parseJSON("data.json");
		for (Employee employee : listJson) {
			System.out.println(employee);
		}

		// END JSON parser
	}

	private static List<Employee> parseJSON(String filename) {

		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String s;
			List<Employee> list = new ArrayList<>();
			try {
				s = br.readLine();
				list = jsonToList(s);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			return list;
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}

		return null;
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
			return staff;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	public static String listToJson(List<Employee> list) {
		Type listType = new TypeToken<List<Employee>>() {
		}.getType();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		String jsonString = gson.toJson(list, listType);

		return jsonString;
	}

	public static List<Employee> jsonToList(String json) {
		Type listType = new TypeToken<List<Employee>>() {
		}.getType();
		GsonBuilder builder = new GsonBuilder();
		Gson gson = builder.create();
		List<Employee> list = gson.fromJson(json, listType);
		return list;
	}

	public static void saveStringToFile(String json, String filename) {
		try (FileWriter writer = new FileWriter(filename, true)) {
			writer.write(json);
		} catch (IOException exception) {
			System.out.println(exception.getMessage());
		}
	}

	public static List<Employee> parseXML(String[] columnMapping, String filename) throws ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(filename));
		Node root = doc.getDocumentElement();
		read(root);

		return employeeList;
	}

	private static void read(Node node) {
		NodeList nodeList = node.getChildNodes();
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node_ = nodeList.item(i);
			if (Node.ELEMENT_NODE == node_.getNodeType()) {
				Element element = (Element) node_;
				if (node_.getNodeName() == "employee") {
					String id = element.getElementsByTagName("id").item(0).getTextContent();
					String firstname = element.getElementsByTagName("firstName").item(0).getTextContent();
					String lastname = element.getElementsByTagName("lastName").item(0).getTextContent();
					String country = element.getElementsByTagName("country").item(0).getTextContent();
					String age = element.getElementsByTagName("age").item(0).getTextContent();
					Employee employee = new Employee(Integer.valueOf(id), firstname, lastname, country, Integer.valueOf(age));
					employeeList.add(employee);
				}
			}
			read(node_);
		}
	}
}






