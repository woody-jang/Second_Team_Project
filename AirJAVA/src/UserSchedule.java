import java.util.List;

public class UserSchedule {
	private String planeNo; // 편명
	private String deptPlace; //출발지
	private String arrvPlace; //도착지
	private String deptTime; //출발시간
	private List<String> selectedSeat; //선택한 좌석 - 여러명 예약한 경우에 List에 추가
	
	public String getPlaneNo() {
		return planeNo;
	}
	public void setPlaneNo(String planeNo) {
		this.planeNo = planeNo;
	}
	public String getDeptPlace() {
		return deptPlace;
	}
	public void setDeptPlace(String deptPlace) {
		this.deptPlace = deptPlace;
	}
	public String getArrvPlace() {
		return arrvPlace;
	}
	public void setArrvPlace(String arrvPlace) {
		this.arrvPlace = arrvPlace;
	}
	public String getDeptTime() {
		return deptTime;
	}
	public void setDeptTime(String deptTime) {
		this.deptTime = deptTime;
	}
	public List<String> getSelectedSeat() {
		return selectedSeat;
	}
	public void setSelectedSeat(List<String> selectedSeat) {
		this.selectedSeat = selectedSeat;
	}
	
}
