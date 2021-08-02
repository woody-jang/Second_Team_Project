import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.DimensionUIResource;

public class Main extends JFrame {
	static List<Customer> customers;
	static List<Plane> planes;
	static int checkAdmin = -1;
	static int checkAddPlane = 0;
	static int checkModPlaneSche = 0;
	static Main main;
	static String loginId;
	Customer loginCustomer;
	List<JPanel> planePnlList;
	JTabbedPane adminTabPane;
	JPanel planeManPnl;
	JPanel scheduleManPnl;
	JPanel customerManPnl;
	JPanel reserveManPnl;
	JPanel addBtnPnl;
	List<JButton> planeBtnList;
	JPanel planeSchModPnl;
	JScrollPane scrlPlaneManPnl;
	CardLayout card;
	JPanel cardPnl;
	JPanel planeSchModByDatePnl;
	Calendar calendar;
	List<JButton> modBtnList;
	List<JButton> delBtnList;
	int selectedDay;
	JPanel calPnl;
	JPanel scheLblPnl;
	private boolean oneWayChk;
	private JPanel selectSchedPnl;
	private JPanel cusReservePnl;
	private JPanel reserveCardPnl;
	List<JLabel> reservePlaneDeptLblList;
	List<JButton> reservePlaneDeptBtnList;
	List<JLabel> reservePlaneArrvLblList;
	List<JButton> reservePlaneArrvBtnList;
	Plane deptPlane;
	Plane arrvPlane;
	PlaneSchedule deptPlaneSched;
	PlaneSchedule arrvPlaneSched;
	private CardLayout reserveCard;
	private JPanel cusSelectSeatPnl;
	List<JButton> deptSeatButton;
	List<JButton> arrvSeatButton;
	private JLabel person1DeptSelLbl;
	private JLabel person1ArrvSelLbl;
	private JLabel person2DeptSelLbl;
	private JLabel person2ArrvSelLbl;
	int price = 0;
	private JLabel payPriceLbl;
	private JLabel totalPriceLbl;

	public Main() {
		if (CustomerIO.CUSTOMER_LIST.exists())
			customers = CustomerIO.load();
		else
			customers = new ArrayList<Customer>();

		if (PlaneIO.PLANE_LIST.exists())
			planes = PlaneIO.load();
		else
			planes = new ArrayList<Plane>();

		sortPlanes();

		addAdmin();
//		addPlane();

		card = new CardLayout();
		cardPnl = new JPanel(card);

		addAdminTabPane();
		JPanel adminTotalPnl = new JPanel();
		adminTotalPnl.setLayout(new BoxLayout(adminTotalPnl, BoxLayout.Y_AXIS));
		JPanel cancelBtnPnl = new JPanel();
		JButton cancelBtn = new JButton("끝내기");
		cancelBtn.addActionListener(new myCancelBtnActionLis());
		cancelBtn.setFont(new Font(cancelBtn.getFont().getName(), Font.PLAIN, 20));
		cancelBtnPnl.add(cancelBtn);
		adminTotalPnl.add(adminTabPane);
		adminTotalPnl.add(cancelBtnPnl);

		JPanel mainPnl = addMainPnl(card, cardPnl);

		JTabbedPane customerTotalPnl = new JTabbedPane();
		addCustomerTotalPnl(customerTotalPnl);

		cardPnl.add(mainPnl, "메인");
		cardPnl.add(customerTotalPnl, "예약");
		cardPnl.add(adminTotalPnl, "관리자");

		add(cardPnl);

		setTitle("Hello World :-)");
		setLocation(450, 300);
		setSize(840, 500);
//		setSize(1100, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void addCustomerTotalPnl(JTabbedPane customerTotalPnl) {
		reserveCard = new CardLayout();
		reserveCardPnl = new JPanel(reserveCard);
		JPanel cusMyPagePnl = new JPanel();

		cusReservePnl = new JPanel();
		cusReservePnl.setLayout(new FlowLayout(FlowLayout.LEFT));
		cusSelectSeatPnl = new JPanel();
		cusSelectSeatPnl.setLayout(new FlowLayout(FlowLayout.LEFT));
		addCusReservePnl();

		reserveCardPnl.add(cusReservePnl, "비행기선택");
		reserveCardPnl.add(cusSelectSeatPnl, "좌석선택");

		customerTotalPnl.add(reserveCardPnl, "예약");
		customerTotalPnl.add(cusMyPagePnl, "마이페이지");
	}

	private void addCusSelectSeatPnl(String[] inform) {
		JTabbedPane cusSelectSeatTab = new JTabbedPane();

		JPanel deptSelectSeatPnl = new JPanel();
		deptSelectSeatPnl.setLayout(new BoxLayout(deptSelectSeatPnl, BoxLayout.Y_AXIS));

		JPanel cusSelectSeatPnl = new JPanel();
		JLabel fistClassLbl = new JLabel("V : 퍼스트클래스");
		fistClassLbl.setPreferredSize(new Dimension(165, 30));
		fistClassLbl.setFont(new Font(fistClassLbl.getFont().getName(), Font.PLAIN, 20));
		fistClassLbl.setHorizontalAlignment(JLabel.CENTER);
		JLabel buisinessClassLbl = new JLabel("G : 비지니스클래스");
		buisinessClassLbl.setPreferredSize(new Dimension(180, 30));
		buisinessClassLbl.setFont(new Font(buisinessClassLbl.getFont().getName(), Font.PLAIN, 20));
		buisinessClassLbl.setHorizontalAlignment(JLabel.CENTER);
		JLabel economyClassLbl = new JLabel("S : 이코노미클래스");
		economyClassLbl.setPreferredSize(new Dimension(180, 30));
		economyClassLbl.setFont(new Font(economyClassLbl.getFont().getName(), Font.PLAIN, 20));
		economyClassLbl.setHorizontalAlignment(JLabel.CENTER);
		cusSelectSeatPnl.add(fistClassLbl);
		cusSelectSeatPnl.add(buisinessClassLbl);
		cusSelectSeatPnl.add(economyClassLbl);
		deptSelectSeatPnl.add(cusSelectSeatPnl);

		JPanel seatPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JPanel seat1Pnl = new JPanel();
		seat1Pnl.setLayout(new BoxLayout(seat1Pnl, BoxLayout.Y_AXIS));
		seat1Pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "1구역",
				TitledBorder.CENTER, TitledBorder.TOP));
		seat1Pnl.setPreferredSize(new Dimension(230, 300));
		seatPnl.add(seat1Pnl);

		JPanel seat2Pnl = new JPanel();
		seat2Pnl.setLayout(new BoxLayout(seat2Pnl, BoxLayout.Y_AXIS));
		seat2Pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "2구역",
				TitledBorder.CENTER, TitledBorder.TOP));
		seat2Pnl.setPreferredSize(new Dimension(230, 300));
		seatPnl.add(seat2Pnl);

		JPanel seat3Pnl = new JPanel();
		seat3Pnl.setLayout(new BoxLayout(seat3Pnl, BoxLayout.Y_AXIS));
		seat3Pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "3구역",
				TitledBorder.CENTER, TitledBorder.TOP));
		seat3Pnl.setPreferredSize(new Dimension(230, 300));
		seatPnl.add(seat3Pnl);
		addSeatPnl(new JPanel[] { seat1Pnl, seat2Pnl, seat3Pnl }, 0);
		deptSelectSeatPnl.add(seatPnl);

		cusSelectSeatTab.add(deptSelectSeatPnl, "가는편");

		if (!oneWayChk) {
			JPanel arrvSelectSeatPnl = new JPanel();
			arrvSelectSeatPnl.setLayout(new BoxLayout(arrvSelectSeatPnl, BoxLayout.Y_AXIS));

			JPanel cusSelectArrvSeatPnl = new JPanel();
			JLabel fistClassArrvLbl = new JLabel("V : 퍼스트클래스");
			fistClassArrvLbl.setPreferredSize(new Dimension(165, 30));
			fistClassArrvLbl.setFont(new Font(fistClassArrvLbl.getFont().getName(), Font.PLAIN, 20));
			fistClassArrvLbl.setHorizontalAlignment(JLabel.CENTER);
			JLabel buisinessClassArrvLbl = new JLabel("G : 비지니스클래스");
			buisinessClassArrvLbl.setPreferredSize(new Dimension(180, 30));
			buisinessClassArrvLbl.setFont(new Font(buisinessClassArrvLbl.getFont().getName(), Font.PLAIN, 20));
			buisinessClassArrvLbl.setHorizontalAlignment(JLabel.CENTER);
			JLabel economyClassArrvLbl = new JLabel("S : 이코노미클래스");
			economyClassArrvLbl.setPreferredSize(new Dimension(180, 30));
			economyClassArrvLbl.setFont(new Font(economyClassArrvLbl.getFont().getName(), Font.PLAIN, 20));
			economyClassArrvLbl.setHorizontalAlignment(JLabel.CENTER);
			cusSelectArrvSeatPnl.add(fistClassArrvLbl);
			cusSelectArrvSeatPnl.add(buisinessClassArrvLbl);
			cusSelectArrvSeatPnl.add(economyClassArrvLbl);
			arrvSelectSeatPnl.add(cusSelectArrvSeatPnl);

			JPanel seatArrvPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JPanel seat1ArrvPnl = new JPanel();
			seat1ArrvPnl.setLayout(new BoxLayout(seat1ArrvPnl, BoxLayout.Y_AXIS));
			seat1ArrvPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "1구역",
					TitledBorder.CENTER, TitledBorder.TOP));
			seat1ArrvPnl.setPreferredSize(new Dimension(230, 300));
			seatArrvPnl.add(seat1ArrvPnl);

			JPanel seat2ArrvPnl = new JPanel();
			seat2ArrvPnl.setLayout(new BoxLayout(seat2ArrvPnl, BoxLayout.Y_AXIS));
			seat2ArrvPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "2구역",
					TitledBorder.CENTER, TitledBorder.TOP));
			seat2ArrvPnl.setPreferredSize(new Dimension(230, 300));
			seatArrvPnl.add(seat2ArrvPnl);

			JPanel seat3ArrvPnl = new JPanel();
			seat3ArrvPnl.setLayout(new BoxLayout(seat3ArrvPnl, BoxLayout.Y_AXIS));
			seat3ArrvPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "3구역",
					TitledBorder.CENTER, TitledBorder.TOP));
			seat3ArrvPnl.setPreferredSize(new Dimension(230, 300));
			seatArrvPnl.add(seat3ArrvPnl);
			addSeatPnl(new JPanel[] { seat1ArrvPnl, seat2ArrvPnl, seat3ArrvPnl }, 1);
			arrvSelectSeatPnl.add(seatArrvPnl);

			cusSelectSeatTab.add(arrvSelectSeatPnl, "오는편");
		}

		this.cusSelectSeatPnl.add(cusSelectSeatTab);
		
		addPaymentPnl(inform);
	}

	private void addPaymentPnl(String[] inform) {
		JPanel paymentPnl = new JPanel();
//		JScrollPane scrlPaymentPnl = new JScrollPane(paymentPnl);
		paymentPnl.setLayout(new BoxLayout(paymentPnl, BoxLayout.Y_AXIS));
		paymentPnl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		
		JPanel person1Pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel person1Lbl = new JLabel("탑승자 1");
		person1Lbl.setFont(new Font(person1Lbl.getFont().getName(), Font.PLAIN, 20));
		person1Pnl.add(person1Lbl);
		paymentPnl.add(person1Pnl);
		
		JPanel person1DeptPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel person1DeptLbl = new JLabel("가는편 좌석");
		person1DeptLbl.setFont(new Font(person1DeptLbl.getFont().getName(), Font.PLAIN, 20));
		person1DeptPnl.add(person1DeptLbl);
		
		person1DeptSelLbl = new JLabel("미정");
		person1DeptSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
		person1DeptSelLbl.setHorizontalAlignment(JLabel.CENTER);
		person1DeptSelLbl.setFont(new Font(person1DeptLbl.getFont().getName(), Font.PLAIN, 20));
		person1DeptSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		person1DeptPnl.add(person1DeptSelLbl);
		
		paymentPnl.add(person1DeptPnl);
		
		if (!oneWayChk) {
			JPanel person1ArrvPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel person1ArrvLbl = new JLabel("오는편 좌석");
			person1ArrvLbl.setFont(new Font(person1ArrvLbl.getFont().getName(), Font.PLAIN, 20));
			person1ArrvPnl.add(person1ArrvLbl);
			person1ArrvSelLbl = new JLabel("미정");
			person1ArrvSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
			person1ArrvSelLbl.setHorizontalAlignment(JLabel.CENTER);
			person1ArrvSelLbl.setFont(new Font(person1DeptLbl.getFont().getName(), Font.PLAIN, 20));
			person1ArrvSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			person1ArrvPnl.add(person1ArrvSelLbl);
			
			paymentPnl.add(person1ArrvPnl);
			
		}
		
		if (inform[4].equals("2")) {
			JPanel person2Pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel person2Lbl = new JLabel("탑승자 2");
			person2Lbl.setFont(new Font(person2Lbl.getFont().getName(), Font.PLAIN, 20));
			person2Pnl.add(person2Lbl);
			paymentPnl.add(person2Pnl);
			
			JPanel person2DeptPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel person2DeptLbl = new JLabel("가는편 좌석");
			person2DeptLbl.setFont(new Font(person2DeptLbl.getFont().getName(), Font.PLAIN, 20));
			person2DeptPnl.add(person2DeptLbl);
			
			person2DeptSelLbl = new JLabel("미정");
			person2DeptSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
			person2DeptSelLbl.setHorizontalAlignment(JLabel.CENTER);
			person2DeptSelLbl.setFont(new Font(person2DeptLbl.getFont().getName(), Font.PLAIN, 20));
			person2DeptSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			person2DeptPnl.add(person2DeptSelLbl);
			
			paymentPnl.add(person2DeptPnl);
			
			if (!oneWayChk) {
				JPanel person2ArrvPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JLabel person2ArrvLbl = new JLabel("오는편 좌석");
				person2ArrvLbl.setFont(new Font(person2ArrvLbl.getFont().getName(), Font.PLAIN, 20));
				person2ArrvPnl.add(person2ArrvLbl);
				person2ArrvSelLbl = new JLabel("미정");
				person2ArrvSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
				person2ArrvSelLbl.setHorizontalAlignment(JLabel.CENTER);
				person2ArrvSelLbl.setFont(new Font(person2DeptLbl.getFont().getName(), Font.PLAIN, 20));
				person2ArrvSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				person2ArrvPnl.add(person2ArrvSelLbl);
				
				paymentPnl.add(person2ArrvPnl);
				
			}
		}
		
		JPanel payInformLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel payInformLbl = new JLabel("결제 금액");
		payInformLbl.setFont(new Font(payInformLbl.getFont().getName(), Font.PLAIN, 20));
		payInformLblPnl.add(payInformLbl);
		paymentPnl.add(payInformLblPnl);
		
		JPanel payPriceLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		payPriceLbl = new JLabel("총 0원");
		payPriceLbl.setFont(new Font(payPriceLbl.getFont().getName(), Font.PLAIN, 20));
		payInformLblPnl.add(payPriceLbl);
		paymentPnl.add(payPriceLblPnl);
		
		JPanel mileageLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel mileageLbl = new JLabel("마일리지 사용");
		mileageLbl.setFont(new Font(mileageLbl.getFont().getName(), Font.PLAIN, 20));
		JButton mileageBtn = new JButton("조회");
		mileageBtn.setFont(new Font(mileageBtn.getFont().getName(), Font.PLAIN, 20));
		mileageLblPnl.add(mileageLbl);
		mileageLblPnl.add(mileageBtn);
		paymentPnl.add(mileageLblPnl);
		
		JPanel mileageUseLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// 여기에 로그인사람의 정보를 불러와서 포인트를 넣어야함
		JLabel mileageUseLbl = new JLabel();
		mileageUseLbl.setFont(new Font(mileageUseLbl.getFont().getName(), Font.PLAIN, 20));
		mileageUseLblPnl.add(mileageUseLbl);
		paymentPnl.add(mileageUseLblPnl);
		
		mileageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Customer cus : customers) {
					if (cus.getUid().equals(loginId)) {
						mileageUseLbl.setText(cus.getPoint() + "java 사용가능");
						loginCustomer = cus;
					}
				}
			}
		});
		
		JPanel mileageUseTfPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextField mileageUseTf = new JTextField(5);
		mileageUseTf.setText("0");
		mileageUseTf.setFont(new Font(mileageUseTf.getFont().getName(), Font.PLAIN, 20));
		mileageUseTfPnl.add(mileageUseTf);
		JButton mileageUseBtn = new JButton("사용");
		mileageUseBtn.setFont(new Font(mileageUseBtn.getFont().getName(), Font.PLAIN, 20));
		mileageUseTfPnl.add(mileageUseBtn);
		paymentPnl.add(mileageUseTfPnl);
		
		JPanel totalPayLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel totalPayLbl = new JLabel("최종 결제 금액");
		totalPayLbl.setFont(new Font(totalPayLbl.getFont().getName(), Font.PLAIN, 20));
		totalPayLblPnl.add(totalPayLbl);
		paymentPnl.add(totalPayLblPnl);
		
		JPanel totalPriceLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		totalPriceLbl = new JLabel("총 0원");
		totalPriceLbl.setFont(new Font(totalPriceLbl.getFont().getName(), Font.PLAIN, 20));
		totalPriceLblPnl.add(totalPriceLbl);
		JButton finalPayBtn = new JButton("결제");
		finalPayBtn.setFont(new Font(finalPayBtn.getFont().getName(), Font.PLAIN, 20));
		totalPriceLblPnl.add(finalPayBtn);
		paymentPnl.add(totalPriceLblPnl);
		
		mileageUseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String temp = mileageUseTf.getText();
				for (int i = 0; i < temp.length(); i++) {
					if (!Character.isDigit(temp.charAt(i))) {
						JOptionPane.showMessageDialog(null, "숫자만 입력하세요");
						return;
					}
				}
				if (Integer.parseInt(temp) > loginCustomer.getPoint()) {
					JOptionPane.showMessageDialog(null, "보유량보다 큰값을 입력했습니다");
					return;
				}
				if (Integer.parseInt(temp) > price) {
					JOptionPane.showMessageDialog(null, "결제금액보다 큰값을 입력했습니다");
					return;
				}
				totalPriceLbl.setText("총 " + (price - Integer.parseInt(mileageUseTf.getText()) + "원"));
			}
		});
		
		cusSelectSeatPnl.add(paymentPnl);
	}

	private void addSeatPnl(JPanel[] jPanels, int type) {
		for (int i = 0; i < 3; i++) {
			if (deptPlaneSched.seatV[i] != null) {
				setSeatPosLbl(jPanels, deptPlaneSched.seatV[i], "V", i, type);
			} else if (deptPlaneSched.seatG[i] != null) { // V, G, S 중에 좌석이 이미 있다면
				setSeatPosLbl(jPanels, deptPlaneSched.seatG[i], "G", i, type);
			} else if (deptPlaneSched.seatS[i] != null) { // 아래에 있는 메소드를 실행해서 좌석 배치
				setSeatPosLbl(jPanels, deptPlaneSched.seatS[i], "S", i, type);
			}
		}
	}

	private void setSeatPosLbl(JPanel[] jPanels, List<JButton> seat, String seatGrade, int idx, int type) {
		deptSeatButton = new ArrayList<JButton>();
		arrvSeatButton = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) { // 좌석배치에서 윗열, 아랫열 2개의 패널로 구성됨
			JPanel tempPnl = new JPanel();
			for (int j = 0; j < seat.size() / 2; j++) { // 윗열 아랫열로 나누어져서 반복횟수는 시트갯수 / 2
				int temp = (i * 1) + (j * 2) + 1; // 좌석 번호를 저장할 임시 변수
				JButton tempBtn = new JButton();
				tempBtn.setText(seatGrade + " 0" + (temp));
				tempBtn.setFont(new Font(tempBtn.getFont().getName(), Font.PLAIN, 20));
				tempBtn.setMargin(new Insets(0, 0, 0, 0));
				tempBtn.setPreferredSize(new Dimension(50, 100));
				tempPnl.add(tempBtn); // 패널에 윗열의 좌석을 추가함
				if (type == 0) {
					deptSeatButton.add(tempBtn);
				} else {
					arrvSeatButton.add(tempBtn);
				}
				
				tempBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						JButton clickedBtn = (JButton) e.getSource();
						if (person2DeptSelLbl == null) {
							if (type == 0) {
								if (person1DeptSelLbl.getText().equals(idx + 1 + "구역 : " + clickedBtn.getText())) {
									person1DeptSelLbl.setText("미정");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (!person1DeptSelLbl.getText().equals("미정")) {
									JOptionPane.showMessageDialog(null, "기존좌석을 취소후 선택하세요");
									return;
								}
								person1DeptSelLbl.setText(idx + 1 + "구역 : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("총 " + price + "원");
								totalPriceLbl.setText("총 " + price + "원");
							} else if (type == 1) {
								if (person1ArrvSelLbl.getText().equals(idx + 1 + "구역 : " + clickedBtn.getText())) {
									person1ArrvSelLbl.setText("미정");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (!person1ArrvSelLbl.getText().equals("미정")) {
									JOptionPane.showMessageDialog(null, "기존좌석을 취소후 선택하세요");
									return;
								}
								person1ArrvSelLbl.setText(idx + 1 + "구역 : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("총 " + price + "원");
								totalPriceLbl.setText("총 " + price + "원");
							}
						} else {
							if (type == 0) {
								if (person1DeptSelLbl.getText().equals(idx + 1 + "구역 : " + clickedBtn.getText())) {
									person1DeptSelLbl.setText("미정");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (person2DeptSelLbl.getText().equals(idx + 1 + "구역 : " + clickedBtn.getText())) {
									person2DeptSelLbl.setText("미정");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (!person1DeptSelLbl.getText().equals("미정") && person2DeptSelLbl.getText().equals("미정")) {
									person2DeptSelLbl.setText(idx + 1 + "구역 : " + clickedBtn.getText());
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price += 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price += 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price += 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (!person1DeptSelLbl.getText().equals("미정") && !person2DeptSelLbl.getText().equals("미정")) {
									JOptionPane.showMessageDialog(null, "기존좌석을 취소후 선택하세요");
									return;
								}
								person1DeptSelLbl.setText(idx + 1 + "구역 : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("총 " + price + "원");
								totalPriceLbl.setText("총 " + price + "원");
							} else if (type == 1) {
								if (person1ArrvSelLbl.getText().equals(idx + 1 + "구역 : " + clickedBtn.getText())) {
									person1ArrvSelLbl.setText("미정");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (person2ArrvSelLbl.getText().equals(idx + 1 + "구역 : " + clickedBtn.getText())) {
									person2ArrvSelLbl.setText("미정");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (!person1ArrvSelLbl.getText().equals("미정") && person2ArrvSelLbl.getText().equals("미정")) {
									person2ArrvSelLbl.setText(idx + 1 + "구역 : " + clickedBtn.getText());
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price += 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price += 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price += 100000;
									}
									payPriceLbl.setText("총 " + price + "원");
									totalPriceLbl.setText("총 " + price + "원");
									return;
								}
								if (!person1ArrvSelLbl.getText().equals("미정") && !person2ArrvSelLbl.getText().equals("미정")) {
									JOptionPane.showMessageDialog(null, "기존좌석을 취소후 선택하세요");
									return;
								}
								person1ArrvSelLbl.setText(idx + 1 + "구역 : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("총 " + price + "원");
								totalPriceLbl.setText("총 " + price + "원");
							}
						}
					}
				});
			}
			jPanels[idx].add(tempPnl);
		}
	}

	private void addCusReservePnl() {
		selectSchedPnl = new JPanel();
		selectSchedPnl.setLayout(new BoxLayout(selectSchedPnl, BoxLayout.Y_AXIS));
		selectSchedPnl.setBorder(BorderFactory.createLineBorder(Color.black, 2));

		JPanel btnPnl = new JPanel();
		JButton roundTripBtn = new JButton("왕복");
		JButton oneWayBtn = new JButton("편도");
		btnPnl.add(roundTripBtn);
		btnPnl.add(oneWayBtn);
		roundTripBtn.setBackground(new Color(101, 255, 94));
		selectSchedPnl.add(btnPnl);
		oneWayChk = false;

		JPanel deptPnl = new JPanel();
		JLabel deptLbl = new JLabel("출발");
		JComboBox<String> deptComb = new JComboBox<String>(PlaneScheAddDialog.airportPlaceDept);
		deptPnl.add(deptLbl);
		deptPnl.add(deptComb);
		selectSchedPnl.add(deptPnl);

		JPanel arrvPnl = new JPanel();
		JLabel arrvLbl = new JLabel("도착");
		JComboBox<String> arrvComb = new JComboBox<String>(PlaneScheAddDialog.airportPlaceArrv);
		arrvPnl.add(arrvLbl);
		arrvPnl.add(arrvComb);
		selectSchedPnl.add(arrvPnl);

		JPanel cntPnl = new JPanel();
		JLabel cntLbl = new JLabel("인원");
		JComboBox<Integer> cntComb = new JComboBox<Integer>(new Integer[] { 1, 2 });
		cntComb.setSelectedIndex(0);
		cntPnl.add(cntLbl);
		cntPnl.add(cntComb);
		selectSchedPnl.add(cntPnl);

		JPanel deptDatePnl = new JPanel();
		JLabel deptDateLbl = new JLabel("출발하는 날짜");
		deptDatePnl.add(deptDateLbl);
		selectSchedPnl.add(deptDatePnl);

		JPanel deptDateBtnPnl = new JPanel();
		JButton deptDateBtn = new JButton("미정");
		deptDateBtnPnl.add(deptDateBtn);
		selectSchedPnl.add(deptDateBtnPnl);

		JPanel arrvDatePnl = new JPanel();
		JLabel arrvDateLbl = new JLabel("돌아오는 날짜");
		arrvDatePnl.add(arrvDateLbl);
		selectSchedPnl.add(arrvDatePnl);

		JPanel arrvDateBtnPnl = new JPanel();
		JButton arrvDateBtn = new JButton("미정");
		arrvDateBtnPnl.add(arrvDateBtn);
		selectSchedPnl.add(arrvDateBtnPnl);

		JPanel checkBtnPnl = new JPanel();
		JButton checkBtnComb = new JButton("조회하기");
		checkBtnPnl.add(checkBtnComb);
		selectSchedPnl.add(checkBtnPnl);

		cusReservePnl.add(selectSchedPnl);

		checkBtnComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deptPlane = null;
				deptPlaneSched = null;
				arrvPlane = null;
				arrvPlaneSched = null;
				if (!deptComb.getSelectedItem().equals("선택")) {
					if (!arrvComb.getSelectedItem().equals("선택")) {
						if (!deptDateBtn.getText().equals("미정")) {
							if (arrvDateBtn.getText().equals("미정") && !oneWayChk) {
								JOptionPane.showMessageDialog(null, "돌아오는 날짜를 선택하세요");
							} else {
								// 항공편과 좌석 예약 패널 만들기
								String[] inform = new String[5];
								inform[0] = (String) deptComb.getSelectedItem();
								inform[1] = (String) arrvComb.getSelectedItem();
								inform[2] = deptDateBtn.getText();
								inform[3] = arrvDateBtn.getText();
								inform[4] = String.valueOf(cntComb.getSelectedIndex() + 1);
								setSelectPlainSeat(cusReservePnl, inform);
							}
						} else {
							JOptionPane.showMessageDialog(null, "가는 날짜를 선택하세요");
						}
					} else {
						JOptionPane.showMessageDialog(null, "도착지를 선택하세요");
					}
				} else {
					JOptionPane.showMessageDialog(null, "출발지를 선택하세요");
				}
			}
		});

		deptComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arrvComb.removeAllItems();
				for (int i = 0; i < PlaneScheAddDialog.airportPlaceArrv.length; i++) {
					if (!PlaneScheAddDialog.airportPlaceArrv[i].equals(deptComb.getSelectedItem()) || i == 0) {
						arrvComb.addItem(PlaneScheAddDialog.airportPlaceArrv[i]);
					}
				}
			}
		});

		oneWayBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arrvDateBtn.setEnabled(false);
				arrvDateBtn.setText("미정");
				oneWayChk = true;
				oneWayBtn.setBackground(new Color(101, 255, 94));
				roundTripBtn.setBackground(cusReservePnl.getBackground());
			}
		});

		roundTripBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				arrvDateBtn.setEnabled(true);
				oneWayChk = false;
				roundTripBtn.setBackground(new Color(101, 255, 94));
				oneWayBtn.setBackground(cusReservePnl.getBackground());
			}
		});

		deptDateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Calendar(0, deptDateBtn.getText());
				if (Calendar.selectedDate.equals("None")) {
					deptDateBtn.setText("미정");
					return;
				}
				deptDateBtn.setText(Calendar.selectedDate);
			}
		});

		arrvDateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deptDateBtn.getText().equals("미정")) {
					JOptionPane.showMessageDialog(null, "출발날짜를 먼저 선택하세요", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new Calendar(0, arrvDateBtn.getText());
				if (Calendar.selectedDate.equals("None")) {
					arrvDateBtn.setText("미정");
					return;
				}
				LocalDate deptDate = LocalDate.of(Integer.parseInt(deptDateBtn.getText().split("[.]")[0]) + 2000,
						Integer.parseInt(deptDateBtn.getText().split("[.]")[1]),
						Integer.parseInt(deptDateBtn.getText().split("[.]")[2]));
				LocalDate arrvDate = LocalDate.of(Integer.parseInt(Calendar.selectedDate.split("[.]")[0]) + 2000,
						Integer.parseInt(Calendar.selectedDate.split("[.]")[1]),
						Integer.parseInt(Calendar.selectedDate.split("[.]")[2]));
				if (deptDate.getDayOfYear() > arrvDate.getDayOfYear() || deptDate.getYear() > arrvDate.getYear()) {
					JOptionPane.showMessageDialog(null, "가는날짜가 돌아오는 날짜보다 빨라야 합니다", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				arrvDateBtn.setText(Calendar.selectedDate);
			}
		});
	}

	protected void setSelectPlainSeat(JPanel cusReservePnl, String[] inform) {
		reservePlaneDeptLblList = new ArrayList<JLabel>();
		reservePlaneDeptBtnList = new ArrayList<JButton>();
		cusReservePnl.removeAll();
		cusReservePnl.add(selectSchedPnl);
		JTabbedPane selectPlainSeatPnl = new JTabbedPane();
		JScrollPane scrlPlainSeatPnl = new JScrollPane(selectPlainSeatPnl);

		JPanel deptSelectPnl = new JPanel();
		deptSelectPnl.setLayout(new BoxLayout(deptSelectPnl, BoxLayout.Y_AXIS));

		for (int i = 0; i < planes.size(); i++) {
			Plane plane = planes.get(i);
			for (int j = 0; j < plane.schedules.size(); j++) {
				PlaneSchedule sched = plane.schedules.get(j);
				if (sched.deptDate.equals(inform[2]) && sched.deptPlace.equals(inform[0])
						&& sched.arrvPlace.equals(inform[1])) {
					JPanel plainListPnl = new JPanel();
					plainListPnl.setBorder(BorderFactory.createLineBorder(Color.black));

					JLabel planeLbl = new JLabel();
					int remainV = 0;
					int remainG = 0;
					int remainS = 0;
					for (int k = 0; k < 3; k++) {
						if (sched.seatV[k] != null) {
							remainV += sched.seatV[k].size();
							if (sched.getSeatSelectedV[k] != null) {
								for (int k2 = 0; k2 < sched.getSeatSelectedV[k].size(); k2++) {
									if (sched.getSeatSelectedV[k].get(k2)) {
										remainV--;
									}
								}
							}
						}
						if (sched.seatG[k] != null) {
							remainG += sched.seatG[k].size();
							if (sched.getSeatSelectedG[k] != null) {
								for (int k2 = 0; k2 < sched.getSeatSelectedG[k].size(); k2++) {
									if (sched.getSeatSelectedG[k].get(k2)) {
										remainG--;
									}
								}
							}
						}
						if (sched.seatS[k] != null) {
							remainS += sched.seatS[k].size();
							if (sched.getSeatSelectedS[k] != null) {
								for (int k2 = 0; k2 < sched.getSeatSelectedS[k].size(); k2++) {
									if (sched.getSeatSelectedS[k].get(k2)) {
										remainS--;
									}
								}
							}
						}
					}
					planeLbl.setText("편명: " + plane.planeName + " / 출발시간: " + sched.deptTime + " / 소요시간: 1시간 / 잔여좌석: V-"
							+ remainV + ", G-" + remainG + ", S-" + remainS);
					planeLbl.setToolTipText("편명 / 출발시간 / 소요시간 / 잔여좌석");
					planeLbl.setPreferredSize(new Dimension(800, 60));
					planeLbl.setFont(new Font(planeLbl.getFont().getName(), Font.PLAIN, 23));
					plainListPnl.add(planeLbl);
					reservePlaneDeptLblList.add(planeLbl);

					JButton selectBtn = new JButton("선택");
					selectBtn.setPreferredSize(new Dimension(60, 50));
					selectBtn.setMargin(new Insets(0, 0, 0, 0));
					selectBtn.setFont(new Font(selectBtn.getFont().getName(), Font.PLAIN, 23));
					plainListPnl.add(selectBtn);
					reservePlaneDeptBtnList.add(selectBtn);

					selectBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JButton clickedBtn = (JButton) e.getSource();
							int idx = reservePlaneDeptBtnList.indexOf(clickedBtn);
							reservePlaneDeptLblList.get(idx).setForeground(Color.red);
							for (int l = 0; l < reservePlaneDeptLblList.size(); l++) {
								if (idx != l)
									reservePlaneDeptLblList.get(l).setForeground(Color.black);
							}
							for (Plane tempPlane : planes) {
								if (tempPlane.planeName.equals(
										reservePlaneDeptLblList.get(idx).getText().split("/")[0].substring(4).trim())) {
									deptPlane = tempPlane;
								}
							}
							for (int l = 0; l < deptPlane.schedules.size(); l++) {
								if (deptPlane.schedules.get(l).deptTime.equals(
										reservePlaneDeptLblList.get(idx).getText().split("/")[1].substring(7).trim())) {
									deptPlaneSched = deptPlane.schedules.get(l);
								}
							}
							if (oneWayChk) {
								reserveCard.show(reserveCardPnl, "좌석선택");
								addCusSelectSeatPnl(inform);
							}
						}
					});

					deptSelectPnl.add(plainListPnl);
				}
			}
		}
		selectPlainSeatPnl.add(deptSelectPnl, "가는편");

		if (!oneWayChk) {
			reservePlaneArrvLblList = new ArrayList<JLabel>();
			reservePlaneArrvBtnList = new ArrayList<JButton>();
			JPanel arrvSelectPnl = new JPanel();
			for (int i = 0; i < planes.size(); i++) {
				Plane plane = planes.get(i);
				for (int j = 0; j < plane.schedules.size(); j++) {
					PlaneSchedule sched = plane.schedules.get(j);
					if (sched.deptDate.equals(inform[2]) && sched.deptPlace.equals(inform[1])
							&& sched.arrvPlace.equals(inform[0])) {
						JPanel plainListPnl = new JPanel();
						plainListPnl.setBorder(BorderFactory.createLineBorder(Color.black));

						JLabel planeLbl = new JLabel();
						int remainV = 0;
						int remainG = 0;
						int remainS = 0;
						for (int k = 0; k < 3; k++) {
							if (sched.seatV[k] != null) {
								remainV += sched.seatV[k].size();
								if (sched.getSeatSelectedV[k] != null) {
									for (int k2 = 0; k2 < sched.getSeatSelectedV[k].size(); k2++) {
										if (sched.getSeatSelectedV[k].get(k2)) {
											remainV--;
										}
									}
								}
							}
							if (sched.seatG[k] != null) {
								remainG += sched.seatG[k].size();
								if (sched.getSeatSelectedG[k] != null) {
									for (int k2 = 0; k2 < sched.getSeatSelectedG[k].size(); k2++) {
										if (sched.getSeatSelectedG[k].get(k2)) {
											remainG--;
										}
									}
								}
							}
							if (sched.seatS[k] != null) {
								remainS += sched.seatS[k].size();
								if (sched.getSeatSelectedS[k] != null) {
									for (int k2 = 0; k2 < sched.getSeatSelectedS[k].size(); k2++) {
										if (sched.getSeatSelectedS[k].get(k2)) {
											remainS--;
										}
									}
								}
							}
						}
						planeLbl.setText("편명: " + plane.planeName + " / 출발시간: " + sched.deptTime
								+ " / 소요시간: 1시간 / 잔여좌석: V-" + remainV + ", G-" + remainG + ", S-" + remainS);
						planeLbl.setToolTipText("편명 / 출발시간 / 소요시간 / 잔여좌석");
						planeLbl.setPreferredSize(new Dimension(800, 60));
						planeLbl.setFont(new Font(planeLbl.getFont().getName(), Font.PLAIN, 23));
						plainListPnl.add(planeLbl);
						reservePlaneArrvLblList.add(planeLbl);

						JButton selectBtn = new JButton("선택");
						selectBtn.setPreferredSize(new Dimension(60, 50));
						selectBtn.setMargin(new Insets(0, 0, 0, 0));
						selectBtn.setFont(new Font(selectBtn.getFont().getName(), Font.PLAIN, 23));
						plainListPnl.add(selectBtn);
						reservePlaneArrvBtnList.add(selectBtn);

						selectBtn.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (deptPlane == null) {
									JOptionPane.showMessageDialog(null, "가는편을 먼저 선택하세요");
									return;
								}
								JButton clickedBtn = (JButton) e.getSource();
								int idx = reservePlaneArrvBtnList.indexOf(clickedBtn);
								reservePlaneArrvLblList.get(idx).setForeground(Color.red);
								for (int l = 0; l < reservePlaneArrvLblList.size(); l++) {
									if (idx != l)
										reservePlaneArrvLblList.get(l).setForeground(Color.black);
								}
								for (Plane tempPlane : planes) {
									if (tempPlane.planeName
											.equals(reservePlaneArrvLblList.get(idx).getText().split("/")[0]
													.substring(4).trim())) {
										arrvPlane = tempPlane;
									}
								}
								for (int l = 0; l < deptPlane.schedules.size(); l++) {
									if (arrvPlane.schedules.get(l).deptTime
											.equals(reservePlaneArrvLblList.get(idx).getText().split("/")[1]
													.substring(7).trim())) {
										arrvPlaneSched = arrvPlane.schedules.get(l);
									}
								}
								reserveCard.show(reserveCardPnl, "좌석선택");
								addCusSelectSeatPnl(inform);
							}
						});

						arrvSelectPnl.add(plainListPnl);
					}
				}
			}

			selectPlainSeatPnl.add(arrvSelectPnl, "오는편");
		}

		cusReservePnl.add(scrlPlainSeatPnl);
		revalidate();
	}

	private void sortPlanes() {
		if (planes.size() != 0) {
			Collections.sort(planes, new Comparator<Plane>() {
				@Override
				public int compare(Plane o1, Plane o2) {
					return o1.planeName.compareTo(o2.planeName);
				}
			});
		}

		for (int i = 0; i < planes.size(); i++) {
			if (planes.get(i).schedules.size() > 1) {
				Collections.sort(planes.get(i).schedules, new Comparator<PlaneSchedule>() {
					@Override
					public int compare(PlaneSchedule o1, PlaneSchedule o2) {
						if (o1.deptDate.equals(o2.deptDate)) {
							return o1.deptTime.compareTo(o2.deptTime);
						}
						return o1.deptDate.compareTo(o2.deptDate);
					}
				});
			}
		}
	}

	private JPanel addMainPnl(CardLayout card, JPanel cardPnl) {
		JPanel mainPnl = new JPanel();
		URL mainLblImage = Main.class.getClassLoader().getResource("mainpage.jpg");
		ImageIcon mainLblIcon = new ImageIcon(mainLblImage);
		JLabel mainLbl = new JLabel(mainLblIcon);
		mainLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new Login();
				if (checkAdmin == 1) {
					card.show(cardPnl, "관리자");
					setSize(1050, 500);
					setLocation(300, 250);
					checkAdmin = -1;
					revalidate();
					repaint();
				} else if (checkAdmin == 0){
					card.show(cardPnl, "예약");
					setSize(1100, 600);
					checkAdmin = -1;
					revalidate();
					repaint();
				}
			}
		});
		mainPnl.add(mainLbl);
		return mainPnl;
	}

	private void addAdminTabPane() {
		adminTabPane = new JTabbedPane();
		customerManPnl = new JPanel();
		JScrollPane scrlCustomerManPnl = new JScrollPane(customerManPnl);
		addPlaneManPnl();
		addScheduleManPnl();
		addCustomerManPnl();
		addReserveManPnl();

		adminTabPane.add(scrlPlaneManPnl, "비행기 관리");
		adminTabPane.add(scheduleManPnl, "스케쥴 관리");
		adminTabPane.add(scrlCustomerManPnl, "회원 관리");
		adminTabPane.add(reserveManPnl, "예약 관리");

	}

	private void addReserveManPnl() {
		reserveManPnl = new JPanel();
	}

	void addCustomerManPnl() {
		customerManPnl.removeAll();
		customerManPnl.setLayout(new BoxLayout(customerManPnl, BoxLayout.Y_AXIS));

		JPanel searchPnl = new JPanel();
		searchPnl.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JLabel searchLbl = new JLabel("회원검색");
		searchLbl.setFont(new Font(searchLbl.getFont().getName(), Font.PLAIN, 20));
		searchLbl.setHorizontalAlignment(JLabel.RIGHT);
		searchPnl.add(searchLbl);

		String[] searchType = { "아이디", "이름" };
		JComboBox<String> searchTypeComb = new JComboBox<String>(searchType);
		searchTypeComb.setPreferredSize(new Dimension(90, 30));
		searchTypeComb.setFont(new Font(searchTypeComb.getFont().getName(), Font.PLAIN, 20));
		searchTypeComb.setSelectedIndex(1);
		searchPnl.add(searchTypeComb);

		JTextField inputSearch = new JTextField(6);
		inputSearch.setFont(new Font(inputSearch.getFont().getName(), Font.PLAIN, 20));
		searchPnl.add(inputSearch);

		JButton searchBtn = new JButton("검색");
		searchBtn.setPreferredSize(new Dimension(90, 30));
		searchBtn.setFont(new Font(searchBtn.getFont().getName(), Font.PLAIN, 20));
		searchPnl.add(searchBtn);
		customerManPnl.add(searchPnl);

		searchBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String inputData = inputSearch.getText();
				int choice = searchTypeComb.getSelectedIndex();
				customerManPnl.removeAll();
				customerManPnl.add(searchPnl);
				addSearchResult(inputData, choice);
				revalidate();
				repaint();
			}
		});

		addSearchResult("", -1);
	}

	private void addSearchResult(String search, int choice) {

		String[] cusGrade = { "선택", "실버", "골드", "다이아" };
		for (int i = 1; i < customers.size(); i++) {
			JPanel tempPnl = new JPanel();
			tempPnl.setPreferredSize(new Dimension(800, 45));
			tempPnl.setBorder(BorderFactory.createLineBorder(Color.black, 2));

			JLabel cusUidLbl = new JLabel("   아이디");
			cusUidLbl.setFont(new Font(cusUidLbl.getFont().getName(), Font.PLAIN, 20));
			cusUidLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusUidLbl);

			JTextField cusUidTf = new JTextField(6);
			cusUidTf.setText(customers.get(i).getUid());
			cusUidTf.setEditable(false);
			cusUidTf.setFont(new Font(cusUidTf.getFont().getName(), Font.PLAIN, 20));
			cusUidTf.setHorizontalAlignment(JTextField.CENTER);
			tempPnl.add(cusUidTf);

			JLabel cusNameLbl = new JLabel("   이름");
			cusNameLbl.setFont(new Font(cusNameLbl.getFont().getName(), Font.PLAIN, 20));
			cusNameLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusNameLbl);

			JTextField cusNameTf = new JTextField(5);
			cusNameTf.setText(customers.get(i).getName());
			cusNameTf.setEditable(false);
			cusNameTf.setFont(new Font(cusNameTf.getFont().getName(), Font.PLAIN, 20));
			cusNameTf.setHorizontalAlignment(JTextField.CENTER);
			tempPnl.add(cusNameTf);

			JLabel cusGradeLbl = new JLabel("   등급");
			cusGradeLbl.setFont(new Font(cusGradeLbl.getFont().getName(), Font.PLAIN, 20));
			cusGradeLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusGradeLbl);

			JComboBox<String> cusGradeComb = new JComboBox<String>(cusGrade);
			cusGradeComb.setEnabled(false);
			cusGradeComb.setPreferredSize(new Dimension(90, 30));
			cusGradeComb.setFont(new Font(cusGradeLbl.getFont().getName(), Font.PLAIN, 20));
			cusGradeComb.setSelectedIndex(customers.get(i).getGrade() + 1);
			tempPnl.add(cusGradeComb);

			JLabel cusMileageLbl = new JLabel("   마일리지");
			cusMileageLbl.setFont(new Font(cusMileageLbl.getFont().getName(), Font.PLAIN, 20));
			cusMileageLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusMileageLbl);

			JTextField cusMileageTf = new JTextField(7);
			cusMileageTf.setEditable(false);
			cusMileageTf.setText(String.valueOf(customers.get(i).getPoint()));
			cusMileageTf.setFont(new Font(cusMileageTf.getFont().getName(), Font.PLAIN, 20));
			cusMileageTf.setHorizontalAlignment(JTextField.CENTER);
			tempPnl.add(cusMileageTf);

			JLabel blankLbl1 = new JLabel("     ");
			tempPnl.add(blankLbl1);

			JButton cusModBtn = new JButton("수정");
			cusModBtn.setPreferredSize(new Dimension(90, 30));
			cusModBtn.setFont(new Font(cusModBtn.getFont().getName(), Font.PLAIN, 20));
			tempPnl.add(cusModBtn);

			JButton cusDelBtn = new JButton("삭제");
			cusDelBtn.setPreferredSize(new Dimension(90, 30));
			cusDelBtn.setFont(new Font(cusDelBtn.getFont().getName(), Font.PLAIN, 20));
			tempPnl.add(cusDelBtn);

			boolean chk = false;
			if (choice == 0) {
				if (search.equals(customers.get(i).getUid())) {
					chk = true;
				}
			} else if (choice == 1) {
				if (search.equals(customers.get(i).getName())) {
					chk = true;
				}
			}
			if (chk || search.equals("")) {
				customerManPnl.add(tempPnl);
			}

			cusModBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tempBtn = (JButton) e.getSource();
					JComboBox<String> tempComb = null;
					JTextField tempTf = null;
					JTextField cusUidTf = null;
					JButton tempDelBtn = null;
					for (int i = 1; i < customerManPnl.getComponentCount(); i++) {
						JPanel tempPnl = (JPanel) customerManPnl.getComponent(i);
						if (tempPnl.getComponent(9).equals(e.getSource())) {
							cusUidTf = (JTextField) tempPnl.getComponent(1);
							tempComb = (JComboBox<String>) tempPnl.getComponent(5);
							tempTf = (JTextField) tempPnl.getComponent(7);
							tempDelBtn = (JButton) tempPnl.getComponent(10);
							break;
						}
					}
					Customer cus = null;
					for (int i = 1; i < customers.size(); i++) {
						cus = customers.get(i);
						if (cus.getUid().equals(cusUidTf.getText())) {
							break;
						}
					}
					if (tempBtn.getText().equals("수정")) {
						tempBtn.setText("저장");
						tempComb.setEnabled(true);
						tempTf.setEditable(true);
						tempDelBtn.setEnabled(false);
					} else if (tempBtn.getText().equals("저장")) {
						if (((String) tempComb.getSelectedItem()).equals("선택")) {
							JOptionPane.showMessageDialog(null, "등급을 확인하세요", "등급확인", JOptionPane.WARNING_MESSAGE);
							return;
						}
						int selectedGrade = tempComb.getSelectedIndex() - 1;
						int selectedPoint = Integer.parseInt(tempTf.getText());
						cus.setGrade(selectedGrade);
						cus.setPoint(selectedPoint);
						CustomerIO.save(customers);
						tempComb.setEnabled(false);
						tempTf.setEditable(false);
						tempDelBtn.setEnabled(true);
						tempBtn.setText("수정");
					}
				}
			});
		}
	}

	private void addScheduleManPnl() {
		scheduleManPnl = new JPanel();
		JTabbedPane scheduleManTabPane = new JTabbedPane();
		scheduleManTabPane.setPreferredSize(new Dimension(1000, 380));

		planeSchModPnl = new JPanel();
		JScrollPane scrlPlaneSchModPnl = new JScrollPane(planeSchModPnl);
		planeSchModPnl.setLayout(new BoxLayout(planeSchModPnl, BoxLayout.Y_AXIS));

		planeBtnList = new ArrayList<JButton>();
		addPlaneBtns();

		scheduleManTabPane.add(scrlPlaneSchModPnl, "비행기별");

		planeSchModByDatePnl = new JPanel();
		JScrollPane scrlPlaneSchModByDatePnl = new JScrollPane(planeSchModByDatePnl);
		planeSchModByDatePnl.setLayout(new BoxLayout(planeSchModByDatePnl, BoxLayout.Y_AXIS));

		scheduleManTabPane.add(scrlPlaneSchModByDatePnl, "날짜별");
		addPlaneCalPnl();

		scheduleManPnl.add(scheduleManTabPane);
	}

	private void addPlaneCalPnl() {
		planeSchModByDatePnl.setLayout(new BoxLayout(planeSchModByDatePnl, BoxLayout.Y_AXIS));
		calendar = new Calendar(1, "미정");
		calPnl = calendar.getCalendarPnl();
		planeSchModByDatePnl.add(calPnl);
		modBtnList = new ArrayList<JButton>();
		delBtnList = new ArrayList<JButton>();
	}

	void setPlaneCalAction(int tempDay) {
		planeSchModByDatePnl.removeAll();
		planeSchModByDatePnl.add(calPnl);
		JPanel schedListMainPnl = new JPanel();
		schedListMainPnl.setLayout(new BoxLayout(schedListMainPnl, BoxLayout.Y_AXIS));

		JPanel scheAddBtnPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton addBtn = new JButton("스케쥴 추가");
		addBtn.setFont(new Font(addBtn.getFont().getName(), Font.PLAIN, 20));
		addBtn.setPreferredSize(new Dimension(160, 45));
		scheAddBtnPnl.add(addBtn);
		schedListMainPnl.add(scheAddBtnPnl);

		scheLblPnl = new JPanel();
		scheLblPnl.setLayout(new BoxLayout(scheLblPnl, BoxLayout.Y_AXIS));
		selectedDay = tempDay;
		getscheLblPnl(scheLblPnl);

		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DayScheAddDialog(calendar.today.getYear() - 2000, calendar.today.getMonthValue(), selectedDay,
						null);
				planeManPnl.removeAll();
				planeManPnl.add(addBtnPnl);
				addPlanePnlList();
				for (int k = 0; k < planePnlList.size(); k++) {
					planeManPnl.add(planePnlList.get(k));
				}
				calendar.repaintCalendar();
				scheLblPnl.removeAll();
				getscheLblPnl(scheLblPnl);
				revalidate();
				repaint();
			}
		});

		schedListMainPnl.add(scheLblPnl);
		planeSchModByDatePnl.add(schedListMainPnl);
	}

	private void getscheLblPnl(JPanel scheLblPnl) {
		List<int[]> schedules = new ArrayList<int[]>();
		for (int i = 0; i < planes.size(); i++) {
			if (planes.get(i).schedules.size() != 0) {
				for (int j = 0; j < planes.get(i).schedules.size(); j++) {
					String[] date = planes.get(i).schedules.get(j).deptDate.split("[.]");
					int year = Integer.parseInt(date[0]) + 2000;
					int month = Integer.parseInt(date[1]);
					int day = Integer.parseInt(date[2]);
					if (calendar.today.getYear() == year && calendar.today.getMonthValue() == month
							&& selectedDay == day) {
						schedules.add(new int[] { i, j });
					}
				}
			}
		}
		for (int i = 0; i < schedules.size(); i++) {
			scheLblPnl.add(addPlaSchds(schedules.get(i)));
		}
	}

	private JPanel addPlaSchds(int[] scheds) {
		JPanel tempSchdPnl = new JPanel();
		int idx1 = scheds[0];
		int idx2 = scheds[1];
		Plane plane = planes.get(idx1);
		PlaneSchedule tempPlaSchd = plane.schedules.get(idx2);
		String planeName = plane.planeName;
		String deptPlace = tempPlaSchd.deptPlace;
		String arrvPlace = tempPlaSchd.arrvPlace;
		String deptTime = tempPlaSchd.deptTime;
		int[] countSeatReserve = new int[3];
		for (int j = 0; j < 3; j++) {
			if (tempPlaSchd.getSeatSelectedV[j] != null) {
				for (int k = 0; k < tempPlaSchd.getSeatSelectedV[j].size(); k++) {
					if (!tempPlaSchd.getSeatSelectedV[j].get(k))
						countSeatReserve[0]++;
				}
			}
			if (tempPlaSchd.getSeatSelectedG[j] != null) {
				for (int k = 0; k < tempPlaSchd.getSeatSelectedG[j].size(); k++) {
					if (!tempPlaSchd.getSeatSelectedG[j].get(k))
						countSeatReserve[1]++;
				}
			}
			if (tempPlaSchd.getSeatSelectedS[j] != null) {
				for (int k = 0; k < tempPlaSchd.getSeatSelectedS[j].size(); k++) {
					if (!tempPlaSchd.getSeatSelectedS[j].get(k))
						countSeatReserve[2]++;
				}
			}
		}

		JLabel tempSchdLbl = new JLabel(
				"  " + planeName + ",  " + deptPlace + " → " + arrvPlace + ",  " + deptTime + ",  예약좌석: "
						+ countSeatReserve[0] + "(V), " + countSeatReserve[1] + "(G), " + countSeatReserve[2] + "(S)");
		tempSchdLbl.setPreferredSize(new Dimension(780, 80));
		tempSchdLbl.setHorizontalAlignment(JLabel.LEFT);
		tempSchdLbl.setFont(new Font(tempSchdLbl.getFont().getName(), Font.PLAIN, 25));
		tempSchdLbl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		tempSchdPnl.add(tempSchdLbl);

		JButton modBtn = new JButton("수정");
		modBtn.setFont(new Font(modBtn.getFont().getName(), Font.PLAIN, 20));
		modBtn.setPreferredSize(new Dimension(80, 80));
		tempSchdPnl.add(modBtn);
		modBtnList.add(modBtn);

		modBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlaneSchedule schedule = null;
				for (int i = 0; i < scheLblPnl.getComponentCount(); i++) {
					if (((JPanel) scheLblPnl.getComponent(i)).getComponent(1).equals(e.getSource())) {
						String planeName = ((JLabel) ((JPanel) scheLblPnl.getComponent(i)).getComponent(0)).getText()
								.trim().split(",")[0];
						for (int j = 0; j < planes.size(); j++) {
							if (planes.get(j).planeName.contentEquals(planeName)) {
								schedule = planes.get(j).schedules.get(i);
								break;
							}
						}
						break;
					}
				}
				new DayScheAddDialog(calendar.today.getYear() - 2000, calendar.today.getMonthValue(), selectedDay,
						schedule);
				planeManPnl.removeAll();
				planeManPnl.add(addBtnPnl);
				addPlanePnlList();
				for (int i = 0; i < planePnlList.size(); i++) {
					planeManPnl.add(planePnlList.get(i));
				}
				calendar.repaintCalendar();
				scheLblPnl.removeAll();
				getscheLblPnl(scheLblPnl);
				revalidate();
				repaint();
			}
		});

		JButton delBtn = new JButton("삭제");
		delBtn.setFont(new Font(delBtn.getFont().getName(), Font.PLAIN, 20));
		delBtn.setPreferredSize(new Dimension(80, 80));
		tempSchdPnl.add(delBtn);
		delBtnList.add(delBtn);

		delBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getChoiceOfQuestion() == 0) {
					for (int i = 0; i < scheLblPnl.getComponentCount(); i++) {
						if (((JPanel) scheLblPnl.getComponent(i)).getComponent(2).equals(e.getSource())) {
							String planeName = ((JLabel) ((JPanel) scheLblPnl.getComponent(i)).getComponent(0))
									.getText().trim().split(",")[0];
							for (int j = 0; j < planes.size(); j++) {
								if (planes.get(j).planeName.contentEquals(planeName)) {
									planes.get(j).schedules.remove(i);
									PlaneIO.save(planes);
									break;
								}
							}
							break;
						}
					}
					planeManPnl.removeAll();
					planeManPnl.add(addBtnPnl);
					addPlanePnlList();
					for (int i = 0; i < planePnlList.size(); i++) {
						planeManPnl.add(planePnlList.get(i));
					}
					calendar.repaintCalendar();
					scheLblPnl.removeAll();
					getscheLblPnl(scheLblPnl);
					revalidate();
					repaint();
				}
			}
		});
		return tempSchdPnl;
	}

	private void addPlaneBtns() {
		for (int i = 0; i < planes.size(); i++) {
			JPanel tempPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			String tempPlaName = planes.get(i).planeName;
			JButton tempBtn = new JButton(tempPlaName + " 편");
			tempBtn.setPreferredSize(new Dimension(800, 80));
			tempBtn.setHorizontalAlignment(JButton.LEFT);
			tempBtn.setFont(new Font(tempBtn.getFont().getName(), Font.PLAIN, 30));
			planeBtnList.add(tempBtn);
			tempPnl.add(tempBtn);
			planeSchModPnl.add(tempPnl);
			tempBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tempBtn = (JButton) e.getSource();
					int j = 0;
					for (; j < planeBtnList.size(); j++) {
						if (planeBtnList.get(j).equals(tempBtn))
							break;
					}
					new PlaneScheModDialog(planes.get(j));
					if (checkModPlaneSche == 1) {
						planeManPnl.removeAll();
						planeManPnl.add(addBtnPnl);
						addPlanePnlList();
						for (int k = 0; k < planePnlList.size(); k++) {
							planeManPnl.add(planePnlList.get(k));
						}
						calendar.repaintCalendar();
						revalidate();
						repaint();
					}
				}
			});
		}
	}

	private void addPlaneManPnl() {
		planeManPnl = new JPanel();
		planeManPnl.setLayout(new BoxLayout(planeManPnl, BoxLayout.Y_AXIS));
		scrlPlaneManPnl = new JScrollPane(planeManPnl);

		addBtnPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton planeAddBtn = new JButton("비행기 추가");
		planeAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Plane tempPla = new Plane();
				new PlaneAddDialog(tempPla);
				if (checkAddPlane == 1) {
					planes.add(tempPla);
					planeManPnl.removeAll();
					planeManPnl.add(addBtnPnl);
					addPlanePnlList();
					for (int i = 0; i < planePnlList.size(); i++) {
						planeManPnl.add(planePnlList.get(i));
					}
					JPanel tempPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
					JButton tempBtn = new JButton(tempPla.planeName + " 편");
					tempBtn.setPreferredSize(new Dimension(800, 80));
					tempBtn.setHorizontalAlignment(JButton.LEFT);
					tempBtn.setFont(new Font(tempBtn.getFont().getName(), Font.PLAIN, 30));
					tempBtn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							JButton tempBtn = (JButton) e.getSource();
							int j = 0;
							for (; j < planeBtnList.size(); j++) {
								if (planeBtnList.get(j).equals(tempBtn))
									break;
							}
							new PlaneScheModDialog(planes.get(j));
						}
					});
					planeBtnList.add(tempBtn);
					tempPnl.add(tempBtn);
					planeSchModPnl.add(tempPnl);
					planeSchModPnl.revalidate();
					planeSchModPnl.repaint();
					revalidate();
					repaint();
					checkAddPlane = 0;
					PlaneIO.save(planes);
				}
			}
		});
		planeAddBtn.setFont(new Font(planeAddBtn.getFont().getName(), Font.PLAIN, 20));
		addBtnPnl.add(planeAddBtn);
		planeManPnl.add(addBtnPnl);

		addPlanePnlList();
		for (int i = 0; i < planePnlList.size(); i++) {
			planeManPnl.add(planePnlList.get(i));
		}

	}

	private void addPlanePnlList() {
		planePnlList = new ArrayList<JPanel>();
		for (int i = 0; i < planes.size(); i++) {
			Plane tempPlane = planes.get(i);
			JPanel tempPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			JLabel informLbl = new JLabel(getPlaneLblText(tempPlane));
			informLbl.setPreferredSize(new Dimension(800, 80));
			informLbl.setHorizontalAlignment(JLabel.LEFT);
			informLbl.setFont(new Font(informLbl.getFont().getName(), Font.PLAIN, 30));
			informLbl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			informLbl.setToolTipText("편명  /  등급별 좌석갯수  /  현재 스케쥴 수");
			tempPnl.add(informLbl);

			JButton modBtn = new JButton("수정");
			modBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tempBtn = (JButton) e.getSource();
					int i = 0;
					for (; i < planePnlList.size(); i++) {
						if (planePnlList.get(i).getComponent(1).equals(tempBtn))
							break;
					}
					if (planes.get(i).schedules.size() != 0) {
						JOptionPane.showMessageDialog(null, "스케쥴이 있어 수정이 불가합니다", "수정 불가", JOptionPane.ERROR_MESSAGE);
						return;
					}
					new PlaneAddDialog(planes.get(i));
					planeManPnl.removeAll();
					planeManPnl.add(addBtnPnl);
					addPlanePnlList();
					for (int j = 0; j < planePnlList.size(); j++) {
						planeManPnl.add(planePnlList.get(j));
					}
					tempBtn = planeBtnList.get(i);
					tempBtn.setText(planes.get(i).planeName);
					planeSchModPnl.revalidate();
					planeSchModPnl.repaint();

					PlaneIO.save(planes);
				}
			});
			modBtn.setFont(new Font(modBtn.getFont().getName(), Font.PLAIN, 20));
			modBtn.setPreferredSize(new Dimension(80, 80));
			tempPnl.add(modBtn);

			JButton delBtn = new JButton("삭제");
			delBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (getChoiceOfQuestion() == 0) {
						JButton tempBtn = (JButton) e.getSource();
						int i = 0;
						for (; i < planePnlList.size(); i++) {
							if (planePnlList.get(i).getComponent(2).equals(tempBtn))
								break;
						}
						if (planes.get(i).schedules.size() != 0) {
							JOptionPane.showMessageDialog(null, "스케쥴이 있어 삭제가 불가합니다", "삭제 불가",
									JOptionPane.ERROR_MESSAGE);
						} else {
							planeSchModPnl.remove(i);
							planeBtnList.remove(i);
							planes.remove(i);
							planeManPnl.remove(i + 1);
							planePnlList.remove(i);
							PlaneIO.save(planes);
						}
					}
				}
			});
			delBtn.setFont(new Font(delBtn.getFont().getName(), Font.PLAIN, 20));
			delBtn.setPreferredSize(new Dimension(80, 80));
			tempPnl.add(delBtn);

			planePnlList.add(tempPnl);
		}
	}

	private String getPlaneLblText(Plane tempPlane) {
		StringBuffer informSB = new StringBuffer();
		String planeName = tempPlane.planeName;
		informSB.append("  ");
		informSB.append(planeName);
		informSB.append("  /  ");

		int cntSeatV = getCntSeat(tempPlane.seatV);
		informSB.append(" V : ");
		informSB.append(String.valueOf(cntSeatV));
		informSB.append("석, ");

		int cntSeatG = getCntSeat(tempPlane.seatG);
		informSB.append(" G : ");
		informSB.append(String.valueOf(cntSeatG));
		informSB.append("석, ");

		int cntSeatS = getCntSeat(tempPlane.seatS);
		informSB.append(" S : ");
		informSB.append(String.valueOf(cntSeatS));
		informSB.append("석  /  ");

		int cntSchedules = tempPlane.schedules.size();
		informSB.append(" 스케쥴 : ");
		informSB.append(String.valueOf(cntSchedules));
		informSB.append(" 건");

		return informSB.toString();
	}

	private int getCntSeat(List<JButton>[] seat) {
		int cnt = 0;
		for (int i = 0; i < 3; i++) {
			if (seat[i] != null) {
				cnt += seat[i].size();
			}
		}
		return cnt;
	}

	int getChoiceOfQuestion() {
		return JOptionPane.showConfirmDialog(null, "되돌릴 수 없습니다\n삭제 하시겠습니까?", "삭제", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE);
	}

	private void addPlane() {
		Plane temp = new Plane();
		temp.planeName = "AA222";
		List<JButton> tempList = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) {
			tempList.add(new JButton("V 0" + (i + 1)));
		}
		temp.seatV[1] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) {
			tempList.add(new JButton("G 0" + (i + 1)));
		}
		temp.seatG[0] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) {
			tempList.add(new JButton("S 0" + (i + 1)));
		}
		temp.seatS[2] = tempList;

		Plane temp1 = new Plane();
		temp1.planeName = "BA222";
		tempList = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) {
			tempList.add(new JButton("V 0" + (i + 1)));
		}
		temp1.seatV[0] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) {
			tempList.add(new JButton("G 0" + (i + 1)));
		}
		temp1.seatG[2] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) {
			tempList.add(new JButton("S 0" + (i + 1)));
		}
		temp1.seatS[1] = tempList;
		planes.add(temp);
		planes.add(temp1);
	}

	private void addAdmin() {
		boolean chkAdmin = true;
		for (int i = 0; i < customers.size(); i++) {
			if (customers.get(i).getUid().equals("admin")) {
				chkAdmin = false;
				break;
			}
		}
		if (chkAdmin) {
			Customer admin = new Customer("관리자", "", "", "admin", new char[] { 'a', 'd', 'm', 'i', 'n', '!' });
			admin.setPassword();
			customers.add(admin);
		}
		CustomerIO.save(customers);
	}

	public static void main(String[] args) {
		main = new Main();
	}

	class myCancelBtnActionLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "관리자모드 종료", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (choice == 0) {
				card.show(cardPnl, "메인");
				setSize(840, 500);
			}
		}
	}
}
