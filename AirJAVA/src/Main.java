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
		JButton cancelBtn = new JButton("?????????");
		cancelBtn.addActionListener(new myCancelBtnActionLis());
		cancelBtn.setFont(new Font(cancelBtn.getFont().getName(), Font.PLAIN, 20));
		cancelBtnPnl.add(cancelBtn);
		adminTotalPnl.add(adminTabPane);
		adminTotalPnl.add(cancelBtnPnl);

		JPanel mainPnl = addMainPnl(card, cardPnl);

		JTabbedPane customerTotalPnl = new JTabbedPane();
		addCustomerTotalPnl(customerTotalPnl);

		cardPnl.add(mainPnl, "??????");
		cardPnl.add(customerTotalPnl, "??????");
		cardPnl.add(adminTotalPnl, "?????????");

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

		reserveCardPnl.add(cusReservePnl, "???????????????");
		reserveCardPnl.add(cusSelectSeatPnl, "????????????");

		customerTotalPnl.add(reserveCardPnl, "??????");
		customerTotalPnl.add(cusMyPagePnl, "???????????????");
	}

	private void addCusSelectSeatPnl(String[] inform) {
		cusSelectSeatPnl.removeAll();
		JTabbedPane cusSelectSeatTab = new JTabbedPane();

		JPanel deptSelectSeatPnl = new JPanel();
		deptSelectSeatPnl.setLayout(new BoxLayout(deptSelectSeatPnl, BoxLayout.Y_AXIS));

		JPanel cusSelectSeatPnl = new JPanel();
		JLabel fistClassLbl = new JLabel("V : ??????????????????");
		fistClassLbl.setPreferredSize(new Dimension(165, 30));
		fistClassLbl.setFont(new Font(fistClassLbl.getFont().getName(), Font.PLAIN, 20));
		fistClassLbl.setHorizontalAlignment(JLabel.CENTER);
		JLabel buisinessClassLbl = new JLabel("G : ?????????????????????");
		buisinessClassLbl.setPreferredSize(new Dimension(180, 30));
		buisinessClassLbl.setFont(new Font(buisinessClassLbl.getFont().getName(), Font.PLAIN, 20));
		buisinessClassLbl.setHorizontalAlignment(JLabel.CENTER);
		JLabel economyClassLbl = new JLabel("S : ?????????????????????");
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
		seat1Pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "1??????",
				TitledBorder.CENTER, TitledBorder.TOP));
		seat1Pnl.setPreferredSize(new Dimension(230, 300));
		seatPnl.add(seat1Pnl);

		JPanel seat2Pnl = new JPanel();
		seat2Pnl.setLayout(new BoxLayout(seat2Pnl, BoxLayout.Y_AXIS));
		seat2Pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "2??????",
				TitledBorder.CENTER, TitledBorder.TOP));
		seat2Pnl.setPreferredSize(new Dimension(230, 300));
		seatPnl.add(seat2Pnl);

		JPanel seat3Pnl = new JPanel();
		seat3Pnl.setLayout(new BoxLayout(seat3Pnl, BoxLayout.Y_AXIS));
		seat3Pnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "3??????",
				TitledBorder.CENTER, TitledBorder.TOP));
		seat3Pnl.setPreferredSize(new Dimension(230, 300));
		seatPnl.add(seat3Pnl);
		addSeatPnl(new JPanel[] { seat1Pnl, seat2Pnl, seat3Pnl }, 0);
		deptSelectSeatPnl.add(seatPnl);

		cusSelectSeatTab.add(deptSelectSeatPnl, "?????????");

		if (!oneWayChk) {
			JPanel arrvSelectSeatPnl = new JPanel();
			arrvSelectSeatPnl.setLayout(new BoxLayout(arrvSelectSeatPnl, BoxLayout.Y_AXIS));

			JPanel cusSelectArrvSeatPnl = new JPanel();
			JLabel fistClassArrvLbl = new JLabel("V : ??????????????????");
			fistClassArrvLbl.setPreferredSize(new Dimension(165, 30));
			fistClassArrvLbl.setFont(new Font(fistClassArrvLbl.getFont().getName(), Font.PLAIN, 20));
			fistClassArrvLbl.setHorizontalAlignment(JLabel.CENTER);
			JLabel buisinessClassArrvLbl = new JLabel("G : ?????????????????????");
			buisinessClassArrvLbl.setPreferredSize(new Dimension(180, 30));
			buisinessClassArrvLbl.setFont(new Font(buisinessClassArrvLbl.getFont().getName(), Font.PLAIN, 20));
			buisinessClassArrvLbl.setHorizontalAlignment(JLabel.CENTER);
			JLabel economyClassArrvLbl = new JLabel("S : ?????????????????????");
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
			seat1ArrvPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2),
					"1??????", TitledBorder.CENTER, TitledBorder.TOP));
			seat1ArrvPnl.setPreferredSize(new Dimension(230, 300));
			seatArrvPnl.add(seat1ArrvPnl);

			JPanel seat2ArrvPnl = new JPanel();
			seat2ArrvPnl.setLayout(new BoxLayout(seat2ArrvPnl, BoxLayout.Y_AXIS));
			seat2ArrvPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2),
					"2??????", TitledBorder.CENTER, TitledBorder.TOP));
			seat2ArrvPnl.setPreferredSize(new Dimension(230, 300));
			seatArrvPnl.add(seat2ArrvPnl);

			JPanel seat3ArrvPnl = new JPanel();
			seat3ArrvPnl.setLayout(new BoxLayout(seat3ArrvPnl, BoxLayout.Y_AXIS));
			seat3ArrvPnl.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2),
					"3??????", TitledBorder.CENTER, TitledBorder.TOP));
			seat3ArrvPnl.setPreferredSize(new Dimension(230, 300));
			seatArrvPnl.add(seat3ArrvPnl);
			addSeatPnl(new JPanel[] { seat1ArrvPnl, seat2ArrvPnl, seat3ArrvPnl }, 1);
			arrvSelectSeatPnl.add(seatArrvPnl);

			cusSelectSeatTab.add(arrvSelectSeatPnl, "?????????");
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
		JLabel person1Lbl = new JLabel("????????? 1");
		person1Lbl.setFont(new Font(person1Lbl.getFont().getName(), Font.PLAIN, 20));
		person1Pnl.add(person1Lbl);
		paymentPnl.add(person1Pnl);

		JPanel person1DeptPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel person1DeptLbl = new JLabel("????????? ??????");
		person1DeptLbl.setFont(new Font(person1DeptLbl.getFont().getName(), Font.PLAIN, 20));
		person1DeptPnl.add(person1DeptLbl);

		person1DeptSelLbl = new JLabel("??????");
		person1DeptSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
		person1DeptSelLbl.setHorizontalAlignment(JLabel.CENTER);
		person1DeptSelLbl.setFont(new Font(person1DeptLbl.getFont().getName(), Font.PLAIN, 20));
		person1DeptSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		person1DeptPnl.add(person1DeptSelLbl);

		paymentPnl.add(person1DeptPnl);

		if (!oneWayChk) {
			JPanel person1ArrvPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel person1ArrvLbl = new JLabel("????????? ??????");
			person1ArrvLbl.setFont(new Font(person1ArrvLbl.getFont().getName(), Font.PLAIN, 20));
			person1ArrvPnl.add(person1ArrvLbl);
			person1ArrvSelLbl = new JLabel("??????");
			person1ArrvSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
			person1ArrvSelLbl.setHorizontalAlignment(JLabel.CENTER);
			person1ArrvSelLbl.setFont(new Font(person1DeptLbl.getFont().getName(), Font.PLAIN, 20));
			person1ArrvSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			person1ArrvPnl.add(person1ArrvSelLbl);

			paymentPnl.add(person1ArrvPnl);

		}

		if (inform[4].equals("2")) {
			JPanel person2Pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel person2Lbl = new JLabel("????????? 2");
			person2Lbl.setFont(new Font(person2Lbl.getFont().getName(), Font.PLAIN, 20));
			person2Pnl.add(person2Lbl);
			paymentPnl.add(person2Pnl);

			JPanel person2DeptPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel person2DeptLbl = new JLabel("????????? ??????");
			person2DeptLbl.setFont(new Font(person2DeptLbl.getFont().getName(), Font.PLAIN, 20));
			person2DeptPnl.add(person2DeptLbl);

			person2DeptSelLbl = new JLabel("??????");
			person2DeptSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
			person2DeptSelLbl.setHorizontalAlignment(JLabel.CENTER);
			person2DeptSelLbl.setFont(new Font(person2DeptLbl.getFont().getName(), Font.PLAIN, 20));
			person2DeptSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			person2DeptPnl.add(person2DeptSelLbl);

			paymentPnl.add(person2DeptPnl);

			if (!oneWayChk) {
				JPanel person2ArrvPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
				JLabel person2ArrvLbl = new JLabel("????????? ??????");
				person2ArrvLbl.setFont(new Font(person2ArrvLbl.getFont().getName(), Font.PLAIN, 20));
				person2ArrvPnl.add(person2ArrvLbl);
				person2ArrvSelLbl = new JLabel("??????");
				person2ArrvSelLbl.setPreferredSize(new DimensionUIResource(150, 30));
				person2ArrvSelLbl.setHorizontalAlignment(JLabel.CENTER);
				person2ArrvSelLbl.setFont(new Font(person2DeptLbl.getFont().getName(), Font.PLAIN, 20));
				person2ArrvSelLbl.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
				person2ArrvPnl.add(person2ArrvSelLbl);

				paymentPnl.add(person2ArrvPnl);

			}
		}

		JPanel payInformLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel payInformLbl = new JLabel("?????? ??????");
		payInformLbl.setFont(new Font(payInformLbl.getFont().getName(), Font.PLAIN, 20));
		payInformLblPnl.add(payInformLbl);
		paymentPnl.add(payInformLblPnl);

		JPanel payPriceLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		payPriceLbl = new JLabel("??? 0???");
		payPriceLbl.setFont(new Font(payPriceLbl.getFont().getName(), Font.PLAIN, 20));
		payInformLblPnl.add(payPriceLbl);
		paymentPnl.add(payPriceLblPnl);

		JPanel mileageLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel mileageLbl = new JLabel("???????????? ??????");
		mileageLbl.setFont(new Font(mileageLbl.getFont().getName(), Font.PLAIN, 20));
		JButton mileageBtn = new JButton("??????");
		mileageBtn.setFont(new Font(mileageBtn.getFont().getName(), Font.PLAIN, 20));
		mileageLblPnl.add(mileageLbl);
		mileageLblPnl.add(mileageBtn);
		paymentPnl.add(mileageLblPnl);

		JPanel mileageUseLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		// ????????? ?????????????????? ????????? ???????????? ???????????? ????????????
		JLabel mileageUseLbl = new JLabel();
		mileageUseLbl.setFont(new Font(mileageUseLbl.getFont().getName(), Font.PLAIN, 20));
		mileageUseLblPnl.add(mileageUseLbl);
		paymentPnl.add(mileageUseLblPnl);

		JPanel mileageUseTfPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextField mileageUseTf = new JTextField(5);
		mileageUseTf.setEnabled(false);
		mileageUseTf.setText("0");
		mileageUseTf.setFont(new Font(mileageUseTf.getFont().getName(), Font.PLAIN, 20));
		mileageUseTfPnl.add(mileageUseTf);
		JButton mileageUseBtn = new JButton("??????");
		mileageUseBtn.setFont(new Font(mileageUseBtn.getFont().getName(), Font.PLAIN, 20));
		mileageUseTfPnl.add(mileageUseBtn);
		paymentPnl.add(mileageUseTfPnl);

		mileageBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (Customer cus : customers) {
					if (cus.getUid().equals(loginId)) {
						mileageUseLbl.setText(cus.getPoint() + "java ????????????");
						loginCustomer = cus;
						mileageUseTf.setEnabled(true);
					}
				}
			}
		});

		JPanel totalPayLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel totalPayLbl = new JLabel("?????? ?????? ??????");
		totalPayLbl.setFont(new Font(totalPayLbl.getFont().getName(), Font.PLAIN, 20));
		totalPayLblPnl.add(totalPayLbl);
		paymentPnl.add(totalPayLblPnl);

		JPanel totalPriceLblPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		totalPriceLbl = new JLabel("??? 0???");
		totalPriceLbl.setFont(new Font(totalPriceLbl.getFont().getName(), Font.PLAIN, 20));
		totalPriceLblPnl.add(totalPriceLbl);
		JButton finalPayBtn = new JButton("??????");
		finalPayBtn.setFont(new Font(finalPayBtn.getFont().getName(), Font.PLAIN, 20));
		totalPriceLblPnl.add(finalPayBtn);
		paymentPnl.add(totalPriceLblPnl);

		mileageUseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String temp = mileageUseTf.getText();
				for (int i = 0; i < temp.length(); i++) {
					if (!Character.isDigit(temp.charAt(i))) {
						JOptionPane.showMessageDialog(null, "????????? ???????????????");
						return;
					}
				}
				if (Integer.parseInt(temp) > loginCustomer.getPoint()) {
					JOptionPane.showMessageDialog(null, "??????????????? ????????? ??????????????????");
					return;
				}
				if (Integer.parseInt(temp) > price) {
					JOptionPane.showMessageDialog(null, "?????????????????? ????????? ??????????????????");
					return;
				}
				totalPriceLbl.setText("??? " + (price - Integer.parseInt(mileageUseTf.getText()) + "???"));
			}
		});

		finalPayBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String person1DeptSeat = person1DeptSelLbl.getText();
				String person1ArrvSeat = null;
				String person2DeptSeat = null;
				String person2ArrvSeat = null;
				List<UserSchedule> uscheList = null;
				if (loginCustomer.getSchedules() == null) {
					uscheList = new ArrayList<UserSchedule>();
					loginCustomer.setSchedules(uscheList);
				} else {
					uscheList = loginCustomer.getSchedules();
				}
				UserSchedule deptUsche = new UserSchedule();
				UserSchedule arrvUsche = null;
				if (person1DeptSeat.equals("??????")) {
					JOptionPane.showMessageDialog(null, "????????? ???????????????");
					return;
				}
				String deptPlaneNo = deptPlane.planeName;
				deptUsche.planeNo = deptPlaneNo;
				String deptDeptPlace = deptPlaneSched.deptPlace;
				deptUsche.deptPlace = deptDeptPlace;
				String deptArrvPlace = deptPlaneSched.arrvPlace;
				deptUsche.arrvPlace = deptArrvPlace;
				String detpDeptTime = deptPlaneSched.deptTime;
				deptUsche.deptTime = detpDeptTime;
				String detpDeptDate = deptPlaneSched.deptDate;
				deptUsche.deptDate = detpDeptDate;
				deptUsche.selectedSeat.add(person1DeptSeat);
				uscheList.add(deptUsche);
				if (!oneWayChk) {
					person1ArrvSeat = person1ArrvSelLbl.getText();
					if (person1ArrvSeat.equals("??????")) {
						JOptionPane.showMessageDialog(null, "????????? ???????????????");
						return;
					}
					arrvUsche = new UserSchedule();
					String arrvPlaneNo = arrvPlane.planeName;
					arrvUsche.planeNo = arrvPlaneNo;
					String arrvDeptPlace = arrvPlaneSched.deptPlace;
					arrvUsche.deptPlace = arrvDeptPlace;
					String arrvArrvPlace = arrvPlaneSched.arrvPlace;
					arrvUsche.arrvPlace = arrvArrvPlace;
					String arrvDeptTime = arrvPlaneSched.deptTime;
					arrvUsche.deptTime = arrvDeptTime;
					String arrvDeptDate = arrvPlaneSched.deptDate;
					arrvUsche.deptDate = arrvDeptDate;
					arrvUsche.selectedSeat.add(person1ArrvSeat);
					uscheList.add(arrvUsche);
				}
				if (inform[4].equals("2")) {
					person2DeptSeat = person2DeptSelLbl.getText();
					if (person2DeptSeat.equals("??????")) {
						JOptionPane.showMessageDialog(null, "????????? ???????????????");
						return;
					}
					deptUsche.selectedSeat.add(person2DeptSeat);
					if (!oneWayChk) {
						person2ArrvSeat = person2ArrvSelLbl.getText();
						if (person2ArrvSeat.equals("??????")) {
							JOptionPane.showMessageDialog(null, "????????? ???????????????");
							return;
						}
						arrvUsche.selectedSeat.add(person2ArrvSeat);
					}
				}
				for (int i = 0; i < 3; i++) {
					if (deptPlaneSched.seatV[i] != null) {
						if (deptPlaneSched.getSeatSelectedV[i] == null) {
							deptPlaneSched.getSeatSelectedV[i] = new ArrayList<Boolean>();
						}
						for (int j = 0; j < deptPlaneSched.seatV[i].size(); j++) {
							deptPlaneSched.getSeatSelectedV[i].add(false);
						}
					} else if (deptPlaneSched.seatG[i] != null) {
						if (deptPlaneSched.getSeatSelectedG[i] == null) {
							deptPlaneSched.getSeatSelectedG[i] = new ArrayList<Boolean>();
						}
						for (int j = 0; j < deptPlaneSched.seatG[i].size(); j++) {
							deptPlaneSched.getSeatSelectedG[i].add(false);
						}
					} else if (deptPlaneSched.seatS[i] != null) {
						if (deptPlaneSched.getSeatSelectedS[i] == null) {
							deptPlaneSched.getSeatSelectedS[i] = new ArrayList<Boolean>();
						}
						for (int j = 0; j < deptPlaneSched.seatS[i].size(); j++) {
							deptPlaneSched.getSeatSelectedS[i].add(false);
						}
					}
				}
				int section = Integer.parseInt(person1DeptSeat.substring(0, 1)) - 1;
				String seatGrade = person1DeptSeat.substring(6, 7);
				int seatNo = Integer.parseInt(person1DeptSeat.substring(8));
				if (seatGrade.equals("V")) {
					deptPlaneSched.getSeatSelectedV[section].set(seatNo - 1, true);
				} else if (seatGrade.equals("G")) {
					deptPlaneSched.getSeatSelectedG[section].set(seatNo - 1, true);
				} else if (seatGrade.equals("S")) {
					deptPlaneSched.getSeatSelectedS[section].set(seatNo - 1, true);
				}
				if (person1ArrvSeat != null) {
					section = Integer.parseInt(person1ArrvSeat.substring(0, 1)) - 1;
					seatGrade = person1ArrvSeat.substring(6, 7);
					seatNo = Integer.parseInt(person1ArrvSeat.substring(8));
					if (seatGrade.equals("V")) {
						arrvPlaneSched.getSeatSelectedV[section].set(seatNo - 1, true);
					} else if (seatGrade.equals("G")) {
						arrvPlaneSched.getSeatSelectedG[section].set(seatNo - 1, true);
					} else if (seatGrade.equals("S")) {
						arrvPlaneSched.getSeatSelectedS[section].set(seatNo - 1, true);
					}
				}
				if (person2DeptSeat != null) {
					section = Integer.parseInt(person2DeptSeat.substring(0, 1)) - 1;
					seatGrade = person2DeptSeat.substring(6, 7);
					seatNo = Integer.parseInt(person2DeptSeat.substring(8));
					if (seatGrade.equals("V")) {
						deptPlaneSched.getSeatSelectedV[section].set(seatNo - 1, true);
					} else if (seatGrade.equals("G")) {
						deptPlaneSched.getSeatSelectedG[section].set(seatNo - 1, true);
					} else if (seatGrade.equals("S")) {
						deptPlaneSched.getSeatSelectedS[section].set(seatNo - 1, true);
					}
				}
				if (person2ArrvSeat != null) {
					section = Integer.parseInt(person2ArrvSeat.substring(0, 1)) - 1;
					seatGrade = person2ArrvSeat.substring(6, 7);
					seatNo = Integer.parseInt(person2ArrvSeat.substring(8));
					if (seatGrade.equals("V")) {
						arrvPlaneSched.getSeatSelectedV[section].set(seatNo - 1, true);
					} else if (seatGrade.equals("G")) {
						arrvPlaneSched.getSeatSelectedG[section].set(seatNo - 1, true);
					} else if (seatGrade.equals("S")) {
						arrvPlaneSched.getSeatSelectedS[section].set(seatNo - 1, true);
					}
				}
				reserveCard.show(reserveCardPnl, "???????????????");
			}
		});

		cusSelectSeatPnl.add(paymentPnl);
	}

	private void addSeatPnl(JPanel[] jPanels, int type) {
		for (int i = 0; i < 3; i++) {
			if (deptPlaneSched.seatV[i] != null) {
				setSeatPosLbl(jPanels, deptPlaneSched.seatV[i], deptPlaneSched.getSeatSelectedV[i], i, type);
			} else if (deptPlaneSched.seatG[i] != null) { // V, G, S ?????? ????????? ?????? ?????????
				setSeatPosLbl(jPanels, deptPlaneSched.seatG[i], deptPlaneSched.getSeatSelectedG[i], i, type);
			} else if (deptPlaneSched.seatS[i] != null) { // ????????? ?????? ???????????? ???????????? ?????? ??????
				setSeatPosLbl(jPanels, deptPlaneSched.seatS[i], deptPlaneSched.getSeatSelectedS[i], i, type);
			}
		}
	}

	private void setSeatPosLbl(JPanel[] jPanels, List<JButton> seat, List<Boolean> seatSelected, int idx, int type) {
		deptSeatButton = new ArrayList<JButton>();
		arrvSeatButton = new ArrayList<JButton>();
		for (int i = 0; i < 2; i++) { // ?????????????????? ??????, ????????? 2?????? ????????? ?????????
			JPanel tempPnl = new JPanel();
			for (int j = 0; j < seat.size() / 2; j++) { // ?????? ???????????? ??????????????? ??????????????? ???????????? / 2
				int temp = (i * 1) + (j * 2) + 1; // ?????? ????????? ????????? ?????? ??????
				JButton tempBtn = new JButton();
				tempBtn.setText(seat.get(temp - 1).getText());
				tempBtn.setFont(new Font(tempBtn.getFont().getName(), Font.PLAIN, 20));
				tempBtn.setMargin(new Insets(0, 0, 0, 0));
				tempBtn.setPreferredSize(new Dimension(50, 100));
				tempPnl.add(tempBtn); // ????????? ????????? ????????? ?????????
				int index = Integer.parseInt(tempBtn.getText().substring(2)) - 1;
				if (seatSelected != null) {
					if (seatSelected.get(index)) {
						tempBtn.setEnabled(false);
					}
				}
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
								if (person1DeptSelLbl.getText().equals(idx + 1 + "?????? : " + clickedBtn.getText())) {
									person1DeptSelLbl.setText("??????");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (!person1DeptSelLbl.getText().equals("??????")) {
									JOptionPane.showMessageDialog(null, "??????????????? ????????? ???????????????");
									return;
								}
								person1DeptSelLbl.setText(idx + 1 + "?????? : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("??? " + price + "???");
								totalPriceLbl.setText("??? " + price + "???");
							} else if (type == 1) {
								if (person1ArrvSelLbl.getText().equals(idx + 1 + "?????? : " + clickedBtn.getText())) {
									person1ArrvSelLbl.setText("??????");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (!person1ArrvSelLbl.getText().equals("??????")) {
									JOptionPane.showMessageDialog(null, "??????????????? ????????? ???????????????");
									return;
								}
								person1ArrvSelLbl.setText(idx + 1 + "?????? : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("??? " + price + "???");
								totalPriceLbl.setText("??? " + price + "???");
							}
						} else {
							if (type == 0) {
								if (person1DeptSelLbl.getText().equals(idx + 1 + "?????? : " + clickedBtn.getText())) {
									person1DeptSelLbl.setText("??????");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (person2DeptSelLbl.getText().equals(idx + 1 + "?????? : " + clickedBtn.getText())) {
									person2DeptSelLbl.setText("??????");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (!person1DeptSelLbl.getText().equals("??????")
										&& person2DeptSelLbl.getText().equals("??????")) {
									person2DeptSelLbl.setText(idx + 1 + "?????? : " + clickedBtn.getText());
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price += 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price += 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price += 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (!person1DeptSelLbl.getText().equals("??????")
										&& !person2DeptSelLbl.getText().equals("??????")) {
									JOptionPane.showMessageDialog(null, "??????????????? ????????? ???????????????");
									return;
								}
								person1DeptSelLbl.setText(idx + 1 + "?????? : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("??? " + price + "???");
								totalPriceLbl.setText("??? " + price + "???");
							} else if (type == 1) {
								if (person1ArrvSelLbl.getText().equals(idx + 1 + "?????? : " + clickedBtn.getText())) {
									person1ArrvSelLbl.setText("??????");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (person2ArrvSelLbl.getText().equals(idx + 1 + "?????? : " + clickedBtn.getText())) {
									person2ArrvSelLbl.setText("??????");
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price -= 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price -= 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price -= 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (!person1ArrvSelLbl.getText().equals("??????")
										&& person2ArrvSelLbl.getText().equals("??????")) {
									person2ArrvSelLbl.setText(idx + 1 + "?????? : " + clickedBtn.getText());
									if (clickedBtn.getText().substring(0, 1).equals("V")) {
										price += 500000;
									} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
										price += 300000;
									} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
										price += 100000;
									}
									payPriceLbl.setText("??? " + price + "???");
									totalPriceLbl.setText("??? " + price + "???");
									return;
								}
								if (!person1ArrvSelLbl.getText().equals("??????")
										&& !person2ArrvSelLbl.getText().equals("??????")) {
									JOptionPane.showMessageDialog(null, "??????????????? ????????? ???????????????");
									return;
								}
								person1ArrvSelLbl.setText(idx + 1 + "?????? : " + clickedBtn.getText());
								if (clickedBtn.getText().substring(0, 1).equals("V")) {
									price += 500000;
								} else if (clickedBtn.getText().substring(0, 1).equals("G")) {
									price += 300000;
								} else if (clickedBtn.getText().substring(0, 1).equals("S")) {
									price += 100000;
								}
								payPriceLbl.setText("??? " + price + "???");
								totalPriceLbl.setText("??? " + price + "???");
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
		JButton roundTripBtn = new JButton("??????");
		JButton oneWayBtn = new JButton("??????");
		btnPnl.add(roundTripBtn);
		btnPnl.add(oneWayBtn);
		roundTripBtn.setBackground(new Color(101, 255, 94));
		selectSchedPnl.add(btnPnl);
		oneWayChk = false;

		JPanel deptPnl = new JPanel();
		JLabel deptLbl = new JLabel("??????");
		JComboBox<String> deptComb = new JComboBox<String>(PlaneScheAddDialog.airportPlaceDept);
		deptPnl.add(deptLbl);
		deptPnl.add(deptComb);
		selectSchedPnl.add(deptPnl);

		JPanel arrvPnl = new JPanel();
		JLabel arrvLbl = new JLabel("??????");
		JComboBox<String> arrvComb = new JComboBox<String>(PlaneScheAddDialog.airportPlaceArrv);
		arrvPnl.add(arrvLbl);
		arrvPnl.add(arrvComb);
		selectSchedPnl.add(arrvPnl);

		JPanel cntPnl = new JPanel();
		JLabel cntLbl = new JLabel("??????");
		JComboBox<Integer> cntComb = new JComboBox<Integer>(new Integer[] { 1, 2 });
		cntComb.setSelectedIndex(0);
		cntPnl.add(cntLbl);
		cntPnl.add(cntComb);
		selectSchedPnl.add(cntPnl);

		JPanel deptDatePnl = new JPanel();
		JLabel deptDateLbl = new JLabel("???????????? ??????");
		deptDatePnl.add(deptDateLbl);
		selectSchedPnl.add(deptDatePnl);

		JPanel deptDateBtnPnl = new JPanel();
		JButton deptDateBtn = new JButton("??????");
		deptDateBtnPnl.add(deptDateBtn);
		selectSchedPnl.add(deptDateBtnPnl);

		JPanel arrvDatePnl = new JPanel();
		JLabel arrvDateLbl = new JLabel("???????????? ??????");
		arrvDatePnl.add(arrvDateLbl);
		selectSchedPnl.add(arrvDatePnl);

		JPanel arrvDateBtnPnl = new JPanel();
		JButton arrvDateBtn = new JButton("??????");
		arrvDateBtnPnl.add(arrvDateBtn);
		selectSchedPnl.add(arrvDateBtnPnl);

		JPanel checkBtnPnl = new JPanel();
		JButton checkBtnComb = new JButton("????????????");
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
				if (!deptComb.getSelectedItem().equals("??????")) {
					if (!arrvComb.getSelectedItem().equals("??????")) {
						if (!deptDateBtn.getText().equals("??????")) {
							if (arrvDateBtn.getText().equals("??????") && !oneWayChk) {
								JOptionPane.showMessageDialog(null, "???????????? ????????? ???????????????");
							} else {
								// ???????????? ?????? ?????? ?????? ?????????
								String[] inform = new String[5];
								inform[0] = (String) deptComb.getSelectedItem();
								inform[1] = (String) arrvComb.getSelectedItem();
								inform[2] = deptDateBtn.getText();
								inform[3] = arrvDateBtn.getText();
								inform[4] = String.valueOf(cntComb.getSelectedIndex() + 1);
								setSelectPlainSeat(cusReservePnl, inform);
							}
						} else {
							JOptionPane.showMessageDialog(null, "?????? ????????? ???????????????");
						}
					} else {
						JOptionPane.showMessageDialog(null, "???????????? ???????????????");
					}
				} else {
					JOptionPane.showMessageDialog(null, "???????????? ???????????????");
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
				arrvDateBtn.setText("??????");
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
					deptDateBtn.setText("??????");
					return;
				}
				deptDateBtn.setText(Calendar.selectedDate);
			}
		});

		arrvDateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deptDateBtn.getText().equals("??????")) {
					JOptionPane.showMessageDialog(null, "??????????????? ?????? ???????????????", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				new Calendar(0, arrvDateBtn.getText());
				if (Calendar.selectedDate.equals("None")) {
					arrvDateBtn.setText("??????");
					return;
				}
				LocalDate deptDate = LocalDate.of(Integer.parseInt(deptDateBtn.getText().split("[.]")[0]) + 2000,
						Integer.parseInt(deptDateBtn.getText().split("[.]")[1]),
						Integer.parseInt(deptDateBtn.getText().split("[.]")[2]));
				LocalDate arrvDate = LocalDate.of(Integer.parseInt(Calendar.selectedDate.split("[.]")[0]) + 2000,
						Integer.parseInt(Calendar.selectedDate.split("[.]")[1]),
						Integer.parseInt(Calendar.selectedDate.split("[.]")[2]));
				if (deptDate.getDayOfYear() > arrvDate.getDayOfYear() || deptDate.getYear() > arrvDate.getYear()) {
					JOptionPane.showMessageDialog(null, "??????????????? ???????????? ???????????? ????????? ?????????", "", JOptionPane.ERROR_MESSAGE);
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
					planeLbl.setText("??????: " + plane.planeName + " / ????????????: " + sched.deptTime + " / ????????????: 1?????? / ????????????: V-"
							+ remainV + ", G-" + remainG + ", S-" + remainS);
					planeLbl.setToolTipText("?????? / ???????????? / ???????????? / ????????????");
					planeLbl.setPreferredSize(new Dimension(800, 60));
					planeLbl.setFont(new Font(planeLbl.getFont().getName(), Font.PLAIN, 23));
					plainListPnl.add(planeLbl);
					reservePlaneDeptLblList.add(planeLbl);

					JButton selectBtn = new JButton("??????");
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
								reserveCard.show(reserveCardPnl, "????????????");
								addCusSelectSeatPnl(inform);
							}
						}
					});

					deptSelectPnl.add(plainListPnl);
				}
			}
		}
		selectPlainSeatPnl.add(deptSelectPnl, "?????????");

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
						planeLbl.setText("??????: " + plane.planeName + " / ????????????: " + sched.deptTime
								+ " / ????????????: 1?????? / ????????????: V-" + remainV + ", G-" + remainG + ", S-" + remainS);
						planeLbl.setToolTipText("?????? / ???????????? / ???????????? / ????????????");
						planeLbl.setPreferredSize(new Dimension(800, 60));
						planeLbl.setFont(new Font(planeLbl.getFont().getName(), Font.PLAIN, 23));
						plainListPnl.add(planeLbl);
						reservePlaneArrvLblList.add(planeLbl);

						JButton selectBtn = new JButton("??????");
						selectBtn.setPreferredSize(new Dimension(60, 50));
						selectBtn.setMargin(new Insets(0, 0, 0, 0));
						selectBtn.setFont(new Font(selectBtn.getFont().getName(), Font.PLAIN, 23));
						plainListPnl.add(selectBtn);
						reservePlaneArrvBtnList.add(selectBtn);

						selectBtn.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								if (deptPlane == null) {
									JOptionPane.showMessageDialog(null, "???????????? ?????? ???????????????");
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
								reserveCard.show(reserveCardPnl, "????????????");
								addCusSelectSeatPnl(inform);
							}
						});

						arrvSelectPnl.add(plainListPnl);
					}
				}
			}

			selectPlainSeatPnl.add(arrvSelectPnl, "?????????");
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
					card.show(cardPnl, "?????????");
					setSize(1050, 500);
					setLocation(300, 250);
					checkAdmin = -1;
					revalidate();
					repaint();
				} else if (checkAdmin == 0) {
					card.show(cardPnl, "??????");
					setSize(1100, 600);
					checkAdmin = -1;
					revalidate();
					repaint();
					for (Customer cus : customers) {
						if (cus.getUid().equals(loginId)) {
							loginCustomer = cus;
						}
					}
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

		adminTabPane.add(scrlPlaneManPnl, "????????? ??????");
		adminTabPane.add(scheduleManPnl, "????????? ??????");
		adminTabPane.add(scrlCustomerManPnl, "?????? ??????");
		adminTabPane.add(reserveManPnl, "?????? ??????");

	}

	private void addReserveManPnl() {
		reserveManPnl = new JPanel();
	}

	void addCustomerManPnl() {
		customerManPnl.removeAll();
		customerManPnl.setLayout(new BoxLayout(customerManPnl, BoxLayout.Y_AXIS));

		JPanel searchPnl = new JPanel();
		searchPnl.setLayout(new FlowLayout(FlowLayout.RIGHT));

		JLabel searchLbl = new JLabel("????????????");
		searchLbl.setFont(new Font(searchLbl.getFont().getName(), Font.PLAIN, 20));
		searchLbl.setHorizontalAlignment(JLabel.RIGHT);
		searchPnl.add(searchLbl);

		String[] searchType = { "?????????", "??????" };
		JComboBox<String> searchTypeComb = new JComboBox<String>(searchType);
		searchTypeComb.setPreferredSize(new Dimension(90, 30));
		searchTypeComb.setFont(new Font(searchTypeComb.getFont().getName(), Font.PLAIN, 20));
		searchTypeComb.setSelectedIndex(1);
		searchPnl.add(searchTypeComb);

		JTextField inputSearch = new JTextField(6);
		inputSearch.setFont(new Font(inputSearch.getFont().getName(), Font.PLAIN, 20));
		searchPnl.add(inputSearch);

		JButton searchBtn = new JButton("??????");
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

		String[] cusGrade = { "??????", "??????", "??????", "?????????" };
		for (int i = 1; i < customers.size(); i++) {
			JPanel tempPnl = new JPanel();
			tempPnl.setPreferredSize(new Dimension(800, 45));
			tempPnl.setBorder(BorderFactory.createLineBorder(Color.black, 2));

			JLabel cusUidLbl = new JLabel("   ?????????");
			cusUidLbl.setFont(new Font(cusUidLbl.getFont().getName(), Font.PLAIN, 20));
			cusUidLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusUidLbl);

			JTextField cusUidTf = new JTextField(6);
			cusUidTf.setText(customers.get(i).getUid());
			cusUidTf.setEditable(false);
			cusUidTf.setFont(new Font(cusUidTf.getFont().getName(), Font.PLAIN, 20));
			cusUidTf.setHorizontalAlignment(JTextField.CENTER);
			tempPnl.add(cusUidTf);

			JLabel cusNameLbl = new JLabel("   ??????");
			cusNameLbl.setFont(new Font(cusNameLbl.getFont().getName(), Font.PLAIN, 20));
			cusNameLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusNameLbl);

			JTextField cusNameTf = new JTextField(5);
			cusNameTf.setText(customers.get(i).getName());
			cusNameTf.setEditable(false);
			cusNameTf.setFont(new Font(cusNameTf.getFont().getName(), Font.PLAIN, 20));
			cusNameTf.setHorizontalAlignment(JTextField.CENTER);
			tempPnl.add(cusNameTf);

			JLabel cusGradeLbl = new JLabel("   ??????");
			cusGradeLbl.setFont(new Font(cusGradeLbl.getFont().getName(), Font.PLAIN, 20));
			cusGradeLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusGradeLbl);

			JComboBox<String> cusGradeComb = new JComboBox<String>(cusGrade);
			cusGradeComb.setEnabled(false);
			cusGradeComb.setPreferredSize(new Dimension(90, 30));
			cusGradeComb.setFont(new Font(cusGradeLbl.getFont().getName(), Font.PLAIN, 20));
			cusGradeComb.setSelectedIndex(customers.get(i).getGrade() + 1);
			tempPnl.add(cusGradeComb);

			JLabel cusMileageLbl = new JLabel("   ????????????");
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

			JButton cusModBtn = new JButton("??????");
			cusModBtn.setPreferredSize(new Dimension(90, 30));
			cusModBtn.setFont(new Font(cusModBtn.getFont().getName(), Font.PLAIN, 20));
			tempPnl.add(cusModBtn);

			JButton cusDelBtn = new JButton("??????");
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
					if (tempBtn.getText().equals("??????")) {
						tempBtn.setText("??????");
						tempComb.setEnabled(true);
						tempTf.setEditable(true);
						tempDelBtn.setEnabled(false);
					} else if (tempBtn.getText().equals("??????")) {
						if (((String) tempComb.getSelectedItem()).equals("??????")) {
							JOptionPane.showMessageDialog(null, "????????? ???????????????", "????????????", JOptionPane.WARNING_MESSAGE);
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
						tempBtn.setText("??????");
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

		scheduleManTabPane.add(scrlPlaneSchModPnl, "????????????");

		planeSchModByDatePnl = new JPanel();
		JScrollPane scrlPlaneSchModByDatePnl = new JScrollPane(planeSchModByDatePnl);
		planeSchModByDatePnl.setLayout(new BoxLayout(planeSchModByDatePnl, BoxLayout.Y_AXIS));

		scheduleManTabPane.add(scrlPlaneSchModByDatePnl, "?????????");
		addPlaneCalPnl();

		scheduleManPnl.add(scheduleManTabPane);
	}

	private void addPlaneCalPnl() {
		planeSchModByDatePnl.setLayout(new BoxLayout(planeSchModByDatePnl, BoxLayout.Y_AXIS));
		calendar = new Calendar(1, "??????");
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
		JButton addBtn = new JButton("????????? ??????");
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
				"  " + planeName + ",  " + deptPlace + " ??? " + arrvPlace + ",  " + deptTime + ",  ????????????: "
						+ countSeatReserve[0] + "(V), " + countSeatReserve[1] + "(G), " + countSeatReserve[2] + "(S)");
		tempSchdLbl.setPreferredSize(new Dimension(780, 80));
		tempSchdLbl.setHorizontalAlignment(JLabel.LEFT);
		tempSchdLbl.setFont(new Font(tempSchdLbl.getFont().getName(), Font.PLAIN, 25));
		tempSchdLbl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		tempSchdPnl.add(tempSchdLbl);

		JButton modBtn = new JButton("??????");
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

		JButton delBtn = new JButton("??????");
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
			JButton tempBtn = new JButton(tempPlaName + " ???");
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
		JButton planeAddBtn = new JButton("????????? ??????");
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
					JButton tempBtn = new JButton(tempPla.planeName + " ???");
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
			informLbl.setToolTipText("??????  /  ????????? ????????????  /  ?????? ????????? ???");
			tempPnl.add(informLbl);

			JButton modBtn = new JButton("??????");
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
						JOptionPane.showMessageDialog(null, "???????????? ?????? ????????? ???????????????", "?????? ??????", JOptionPane.ERROR_MESSAGE);
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

			JButton delBtn = new JButton("??????");
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
							JOptionPane.showMessageDialog(null, "???????????? ?????? ????????? ???????????????", "?????? ??????",
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
		informSB.append("???, ");

		int cntSeatG = getCntSeat(tempPlane.seatG);
		informSB.append(" G : ");
		informSB.append(String.valueOf(cntSeatG));
		informSB.append("???, ");

		int cntSeatS = getCntSeat(tempPlane.seatS);
		informSB.append(" S : ");
		informSB.append(String.valueOf(cntSeatS));
		informSB.append("???  /  ");

		int cntSchedules = tempPlane.schedules.size();
		informSB.append(" ????????? : ");
		informSB.append(String.valueOf(cntSchedules));
		informSB.append(" ???");

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
		return JOptionPane.showConfirmDialog(null, "????????? ??? ????????????\n?????? ???????????????????", "??????", JOptionPane.OK_CANCEL_OPTION,
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
			Customer admin = new Customer("?????????", "", "", "admin", new char[] { 'a', 'd', 'm', 'i', 'n', '!' });
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
			int choice = JOptionPane.showConfirmDialog(null, "???????????? ???????????????????", "??????????????? ??????", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (choice == 0) {
				card.show(cardPnl, "??????");
				setSize(840, 500);
			}
		}
	}
}
