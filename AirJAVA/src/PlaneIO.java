import java.io.*;
import java.util.List;

public class PlaneIO {
	static final File PLANE_LIST = new File("Plane.pla");
	
	private PlaneIO() {}
	
	static void save(List<Plane> planes) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PLANE_LIST))) {
			oos.writeObject(planes);
			oos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	static List<Plane> load() {
		List<Plane> read = null;
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PLANE_LIST))) {
			read = (List<Plane>) ois.readObject();
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
