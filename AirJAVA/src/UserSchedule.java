import java.util.List;

public class UserSchedule {
	private String planeNo; // 편명
	private String deptPlace; //출발지
	private String arrvPlace; //도착지
	private String deptTime; //출발시간
	private List<String> selectedSeat; //선택한 좌석 - 여러명 예약한 경우에 List에 추가
}
