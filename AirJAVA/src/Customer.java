import java.util.List;

public class Customer {
	private String name; //이름
	private int birth; //생년월일
	private String cellNum; //휴대폰번호
	private String uid; // id
	private String upwd; // pw
	private int grade; // 등급, 0:실버, 1:골드, 2:다이아
	private int point; //포인트
	private int star; //스티커 0 ~ 12
	private List<UserSchedule> schedules; // 예약했던 목록
}
