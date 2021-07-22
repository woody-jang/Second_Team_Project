import java.io.*;
import java.util.List;

public class CustomerIO {
	static final File CUSTOMER_LIST = new File("customer.pla");
	
	private CustomerIO() {}
	
	static void save(List<Customer> customers) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(CUSTOMER_LIST))) {
			oos.writeObject(customers);
			oos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static List<Customer> load() {
		List<Customer> read = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(CUSTOMER_LIST))) {
			read = (List<Customer>) ois.readObject();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return read;
	}
}

//Student student = new Student("asdf", 10, 3.3);
//Student student1 = new Student("asdf1", 10, 3.3);
//Student student2 = new Student("asdf2", 10, 3.3);
//List<Student> list = new ArrayList<Student>();
//list.add(student);
//list.add(student1);
//list.add(student2);
//
//try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("my-obj.ser"))) {
//	oos.writeObject(list);
//	oos.flush();
//} catch (FileNotFoundException e) {
//	e.printStackTrace();
//} catch (IOException e) {
//	e.printStackTrace();
//}
//
//List<Student> read = null;
//try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("my-obj.ser"))) {
//	read = (List<Student>) ois.readObject();
//} catch (FileNotFoundException e) {
//	e.printStackTrace();
//} catch (IOException e) {
//	e.printStackTrace();
//} catch (ClassNotFoundException e) {
//	e.printStackTrace();
//}
//for (Student z : read) {
//	System.out.println(z.getName());
//}
//System.out.println(read);