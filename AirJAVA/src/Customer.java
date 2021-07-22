import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Customer implements Serializable {
	private static final long serialVersionUID = 159L;
	private String name; // 이름
	private String birth; // 생년월일
	private String cellNum; // 휴대폰번호
	private String uid; // id
	private char[] upwd; // pw
	private int grade; // 등급, 0:실버, 1:골드, 2:다이아
	private int point; // 포인트
	private int star; // 스티커 0 ~ 12
	private List<UserSchedule> schedules; // 예약했던 목록
	private int[] encryption;
	private boolean[] encryptionCode;

	public Customer(String name, String birth, String cellNum, String uid, char[] upwd) {
		this.name = name;
		this.birth = birth;
		this.cellNum = cellNum;
		this.uid = uid;
		this.upwd = upwd;
		this.grade = 0;
		this.point = 1000;
		this.star = 0;
		this.schedules = new ArrayList<UserSchedule>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getCellNum() {
		return cellNum;
	}

	public void setCellNum(String cellNum) {
		this.cellNum = cellNum;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public char[] getUpwd() {
		return upwd;
	}

	public void setUpwd(char[] upwd) {
		this.upwd = upwd;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getStar() {
		return star;
	}

	public void setStar(int star) {
		this.star = star;
	}

	public List<UserSchedule> getSchedules() {
		return schedules;
	}

	public void setSchedules(List<UserSchedule> schedules) {
		this.schedules = schedules;
	}

	public void setPassword() {
		encryption = new int[upwd.length];
		encryptionCode = new boolean[upwd.length];
		for (int i = 0; i < upwd.length; i++) {
			int n = (int) (Math.random() * 100) % 68 + 1;
			if (upwd[i] > 100) {
				upwd[i] = (char) (upwd[i] - n);
				encryptionCode[i] = true;
			} else {
				upwd[i] = (char) (upwd[i] + n);
				encryptionCode[i] = false;
			}
			encryption[i] = n;
		}
	}

	public void getPassword() {
		for (int i = 0; i < encryption.length; i++) {
			if (encryptionCode[i])
				upwd[i] = (char) (upwd[i] + encryption[i]);
			else
				upwd[i] = (char) (upwd[i] - encryption[i]);
		}
	}

	@Override
	public String toString() {
		return "Customer [name=" + name + ", birth=" + birth + ", cellNum=" + cellNum + ", uid=" + uid + ", upwd="
				+ upwd + ", grade=" + grade + ", point=" + point + ", star=" + star + ", schedules=" + schedules + "]";
	}

}
