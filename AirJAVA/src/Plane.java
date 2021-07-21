import java.util.List;

import javax.swing.JButton;

public class Plane {
	String planeName; // 비행기 이름
	List<JButton>[] seatG = new List[3]; //G좌석을 담을 리스트의 배열
	List<JButton>[] seatS = new List[3]; //S좌석을 담을 리스트의 배열
	List<JButton>[] seatD = new List[3]; //D좌석을 담을 리스트의 배열
	//seatD[0] => 1구역의 D좌석 리스트가 들어있음
	//seatD[0] : 1구역,  seatD[1] : 2구역,  seatD[2] : 3구역
	//seatD[0].get(0) => 1구역의 D등급의 첫번째 좌석인 버튼
	//seatD[0].add(new JButton("D1")) => 1구역에 D등급의 좌석1개 추가
	//seatS[0] == null
	List<PlaneSchedule> schedules; //현 비행기의 스케쥴정보
}