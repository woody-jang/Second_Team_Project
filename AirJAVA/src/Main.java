import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Main extends JFrame {
	static List<Customer> customers;
	static List<Plane> planes;
	static int checkAdmin = 0;
	static int checkAddPlane = 0;
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

	public Main() {
		if (CustomerIO.CUSTOMER_LIST.exists())
			customers = CustomerIO.load();
		else
			customers = new ArrayList<Customer>();

		if (PlaneIO.PLANE_LIST.exists())
			planes = PlaneIO.load();
		else
			planes = new ArrayList<Plane>();

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
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
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
		addPlaneManPnl();
		addScheduleManPnl();
		addCustomerManPnl();
		addReserveManPnl();

		adminTabPane.add(scrlPlaneManPnl, "비행기 관리");
		adminTabPane.add(scheduleManPnl, "스케쥴 관리");
		adminTabPane.add(customerManPnl, "회원 관리");
		adminTabPane.add(reserveManPnl, "예약 관리");
		
	}

	private void addReserveManPnl() {
		reserveManPnl = new JPanel();
	}

	private void addCustomerManPnl() {
		customerManPnl = new JPanel();
	}

	private void addScheduleManPnl() {
		scheduleManPnl = new JPanel();
		JTabbedPane scheduleManTabPane = new JTabbedPane();
		JScrollPane scrlScheduleManTabPane = new JScrollPane(scheduleManTabPane);

		planeSchModPnl = new JPanel();
		planeSchModPnl.setLayout(new BoxLayout(planeSchModPnl, BoxLayout.Y_AXIS));
		planeSchModPnl.setPreferredSize(new Dimension(900, 350));

		planeBtnList = new ArrayList<JButton>();
		addPlaneBtns();

		scheduleManTabPane.add(planeSchModPnl, "비행기별");

		scheduleManPnl.add(scrlScheduleManTabPane);
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
				tempPla.plaAddDia = new PlaneAddDialog(tempPla);
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
					planeBtnList.add(tempBtn);
					tempPnl.add(tempBtn);
					planeSchModPnl.add(tempPnl);
					repaint();
					revalidate();
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
					planes.get(i).plaAddDia.show();
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
	}

	public static void main(String[] args) {
		new Main();
		new Calendar();
	}

	class myCancelBtnActionLis implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			int choice = JOptionPane.showConfirmDialog(null, "로그아웃 하시겠습니까?", "관리자모드 종료",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (choice == 0) {
				card.show(cardPnl, "메인");
			}
		}
	}
}
