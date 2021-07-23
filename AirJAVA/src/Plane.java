import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

public class Plane implements Serializable{
	private static final long serialVersionUID = 777L;
	String planeName; // 비행기 이름
	List<JButton>[] seatV = new ArrayList[3]; //G좌석을 담을 리스트의 배열
	List<JButton>[] seatG = new ArrayList[3]; //S좌석을 담을 리스트의 배열
	List<JButton>[] seatS = new ArrayList[3]; //D좌석을 담을 리스트의 배열
	//seatD[0] => 1구역의 D좌석 리스트가 들어있음
	//seatD[0] : 1구역,  seatD[1] : 2구역,  seatD[2] : 3구역
	//seatD[0].get(0) => 1구역의 D등급의 첫번째 좌석인 버튼
	//seatD[0].add(new JButton("D1")) => 1구역에 D등급의 좌석1개 추가
	//seatS[0] == null
	List<PlaneSchedule> schedules = new ArrayList<PlaneSchedule>(); //현 비행기의 스케쥴정보
	PlaneAddDialog plaAddDia;
	
	public int getCntOfSeatV() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (seatV[i] != null)
				count += seatV[i].size();
		}
		return count;
	}
	
	public int getCntOfSeatG() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (seatG[i] != null)
				count += seatG[i].size();
		}
		return count;
	}
	
	public int getCntOfSeatS() {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (seatS[i] != null)
				count += seatS[i].size();
		}
		return count;
	}
}