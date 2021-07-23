import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class PlaneSchedule implements Serializable {
	private static final long serialVersionUID = 7771L;
	String deptPlace; //출발지
	String arrvPlace; //도착지
	String deptDate; //출발날짜
	String deptTime; //출발시간
	
	List<JButton>[] seatV = new ArrayList[3]; //G좌석을 담을 리스트의 배열
	List<JButton>[] seatG = new ArrayList[3]; //S좌석을 담을 리스트의 배열
	List<JButton>[] seatS = new ArrayList[3]; //D좌석을 담을 리스트의 배열
	
	List<Boolean>[] getSeatSelectedV = new ArrayList[3]; //G좌석의 예약 유무를 담을 리스트의 배열
	List<Boolean>[] getSeatSelectedG = new ArrayList[3]; //S좌석의 예약 유무를 담을 리스트의 배열
	List<Boolean>[] getSeatSelectedS = new ArrayList[3]; //D좌석의 예약 유무를 담을 리스트의 배열
}
