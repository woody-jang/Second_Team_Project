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
	JPanel scheLblPnl;

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

		cardPnl.add(adminTotalPnl, "관리자");
		cardPnl.add(mainPnl, "메인");

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
					revalidate();
					repaint();
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
					for(int i = 1; i < customers.size(); i++) {
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
