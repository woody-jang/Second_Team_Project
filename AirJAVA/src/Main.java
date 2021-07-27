import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;

public class Main extends JFrame {
	static List<Customer> customers;
	static List<Plane> planes;
	static int checkAdmin = 0;
	static int checkAddPlane = 0;
	static int checkModPlaneSche = 0;
	static Main main;
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

		cardPnl.add(mainPnl, "메인");
		cardPnl.add(adminTotalPnl, "관리자");

		add(cardPnl);

		setTitle("Hello World :-)");
		setLocation(450, 300);
//		setSize(840, 500);
		setSize(1050, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
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
					checkAdmin = 0;
				} else {
					// 사용자 페이지
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

	private void addCustomerManPnl() {
		customerManPnl.setLayout(new GridLayout(0, 1));
		String[] cusGrade = { "선택", "실버", "골드", "다이아" };
		for (int i = 1; i < customers.size(); i++) {
			JPanel tempPnl = new JPanel();
			tempPnl.setPreferredSize(new Dimension(800, 45));
			tempPnl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			
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
			tempPnl.add(cusGradeComb);
			
			JLabel cusMileageLbl = new JLabel("   마일리지");
			cusMileageLbl.setFont(new Font(cusMileageLbl.getFont().getName(), Font.PLAIN, 20));
			cusMileageLbl.setHorizontalAlignment(JLabel.RIGHT);
			tempPnl.add(cusMileageLbl);
			
			JTextField cusMileageTf = new JTextField(5);
			cusMileageTf.setEditable(false);
			cusMileageTf.setText(String.valueOf(customers.get(i).getPoint()));
			cusMileageTf.setFont(new Font(cusMileageTf.getFont().getName(), Font.PLAIN, 20));
			cusMileageTf.setHorizontalAlignment(JTextField.CENTER);
			tempPnl.add(cusMileageTf);
			
			JButton cusModBtn = new JButton("수정");
			cusModBtn.setPreferredSize(new Dimension(90, 30));
			cusModBtn.setFont(new Font(cusGradeLbl.getFont().getName(), Font.PLAIN, 20));
			tempPnl.add(cusModBtn);
			
			customerManPnl.add(tempPnl);
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

		JPanel scheLblPnl = new JPanel();
		scheLblPnl.setLayout(new BoxLayout(scheLblPnl, BoxLayout.Y_AXIS));
		selectedDay = tempDay;
		getscheLblPnl(scheLblPnl);
		
		addBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DayScheAddDialog(calendar.today.getYear() - 2000, calendar.today.getMonthValue(), selectedDay);
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
					if (calendar.today.getYear() == year && calendar.today.getMonthValue() == month && selectedDay == day) {
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
					revalidate();
					repaint();
					planeSchModPnl.revalidate();
					planeSchModPnl.repaint();
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
					planeSchModPnl.repaint();
					planeSchModPnl.revalidate();

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
					int answer = JOptionPane.showConfirmDialog(null, "되돌릴 수 없습니다\n삭제 하시겠습니까?", "삭제",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (answer == 0) {
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
