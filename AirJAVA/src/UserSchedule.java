import java.util.ArrayList;
import java.util.List;

public class UserSchedule {
	String planeNo; // 편명
	String deptPlace; //출발지
	String arrvPlace; //도착지
	String deptTime; //출발시간
	String deptDate; // 출발일
	List<String> selectedSeat = new ArrayList<String>(); //선택한 좌석 - 여러명 예약한 경우에 List에 추가
}
