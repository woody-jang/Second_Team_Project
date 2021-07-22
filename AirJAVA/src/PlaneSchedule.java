import java.io.Serializable;
import java.util.List;

import javax.swing.JButton;

public class PlaneSchedule implements Serializable {
	private static final long serialVersionUID = 7771L;
	String deptPlace; //출발지
	String arrvPlace; //도착지
	String deptTime; //출발시간
	
	List<JButton>[] seatG = new List[3]; //G좌석을 담을 리스트의 배열
	List<JButton>[] seatS = new List[3]; //S좌석을 담을 리스트의 배열
	List<JButton>[] seatD = new List[3]; //D좌석을 담을 리스트의 배열
	
	List<Boolean>[] getSeatSelectedG = new List[3]; //G좌석의 예약 유무를 담을 리스트의 배열
	List<Boolean>[] getSeatSelectedS = new List[3]; //S좌석의 예약 유무를 담을 리스트의 배열
	List<Boolean>[] getSeatSelectedD = new List[3]; //D좌석의 예약 유무를 담을 리스트의 배열
}
