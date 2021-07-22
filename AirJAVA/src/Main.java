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
	List<JPanel> planePnlList;
	JTabbedPane adminTabPane;
	JPanel planeManPnl;
	JPanel scheduleManPnl;
	JPanel customerManPnl;
	JPanel reserveManPnl;
	
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
		addPlane();
		
		CardLayout card = new CardLayout();
		JPanel cardPnl = new JPanel(card);
		
		addAdminTabPane();
		
		JPanel mainPnl = addMainPnl(card, cardPnl);
		
		cardPnl.add(mainPnl, "메인");
		cardPnl.add(adminTabPane, "관리자");
		
		add(cardPnl);

		setTitle("Hello World :-)");
		setLocation(450, 300);
		setSize(840, 500);
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
					pack();
				}
				else {
					//사용자 페이지
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
		
		adminTabPane.add(planeManPnl, "비행기 관리");
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
	}

	private void addPlaneManPnl() {
		planeManPnl = new JPanel();
		planeManPnl.setLayout(new BoxLayout(planeManPnl, BoxLayout.Y_AXIS));
		
		JPanel addBtnPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton planeAddBtn = new JButton("비행기 추가");
		planeAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new PlaneAddDialog();
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
			
			StringBuffer informSB = new StringBuffer();
			String planeName = tempPlane.planeName;
			informSB.append("  ");
			informSB.append(planeName);
			informSB.append("  /  ");
			
			int cntSeatG = getCntSeat(tempPlane.seatG);
			informSB.append(" G : ");
			informSB.append(String.valueOf(cntSeatG));
			informSB.append("석, ");
			
			int cntSeatS = getCntSeat(tempPlane.seatS);
			informSB.append(" S : ");
			informSB.append(String.valueOf(cntSeatS));
			informSB.append("석, ");
			
			int cntSeatD = getCntSeat(tempPlane.seatD);
			informSB.append(" D : ");
			informSB.append(String.valueOf(cntSeatD));
			informSB.append("석  /  ");
			
			int cntSchedules = tempPlane.schedules.size();
			informSB.append(" 스케쥴 : ");
			informSB.append(String.valueOf(cntSchedules));
			informSB.append(" 건");
			
			
			JLabel informLbl = new JLabel(informSB.toString());
			informLbl.setPreferredSize(new Dimension(800, 80));
			informLbl.setHorizontalAlignment(JLabel.LEFT);
			informLbl.setFont(new Font(informLbl.getFont().getName(), Font.PLAIN, 30));
			informLbl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
			informLbl.setToolTipText("편명  /  등급별 좌석갯수  /  현재 스케쥴 수");
			tempPnl.add(informLbl);
			
			JButton delBtn = new JButton("삭제");
			delBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tempBtn = (JButton) e.getSource();
					int i = 0;
					for (; i < planePnlList.size(); i++) {
						if (planePnlList.get(i).getComponent(1).equals(tempBtn))
							break;
					}
					if (planes.get(i).schedules.size() != 0) {
						JOptionPane.showMessageDialog(null, "스케쥴이 있어 삭제가 불가합니다", "삭제 불가", JOptionPane.ERROR_MESSAGE);
					}
					else {
						planes.remove(i);
						planeManPnl.remove(i + 1);
						planePnlList.remove(i);
					}
				}
			});
			delBtn.setFont(new Font(delBtn.getFont().getName(), Font.PLAIN, 20));
			delBtn.setPreferredSize(new Dimension(80, 80));
			tempPnl.add(delBtn);
			
			JButton modBtn = new JButton("수정");
			modBtn.setFont(new Font(modBtn.getFont().getName(), Font.PLAIN, 20));
			modBtn.setPreferredSize(new Dimension(80, 80));
			tempPnl.add(modBtn);
			
			planePnlList.add(tempPnl);
		}
	}

	private int getCntSeat(List<JButton>[] seat) {
		int cnt = 0;
		for (int i = 0; i < 3; i++) {
			if (seat[i] != null) {
				cnt = seat[i].size();
				break;
			}
		}
		return cnt;
	}

	private void addPlane() {
		Plane temp = new Plane();
		temp.planeName = "AA246";
		List<JButton> tempList = new ArrayList<JButton>();
		for (int i = 1; i < 3; i++) {
			tempList.add(new JButton("G" + i));
		}
		temp.seatG[1] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 1; i < 5; i++) {
			tempList.add(new JButton("S" + i));
		}
		temp.seatS[0] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 1; i < 7; i++) {
			tempList.add(new JButton("D" + i));
		}
		temp.seatD[2] = tempList;
		
		Plane temp1 = new Plane();
		temp1.planeName = "AB246";
		tempList = new ArrayList<JButton>();
		for (int i = 1; i < 3; i++) {
			tempList.add(new JButton("G" + i));
		}
		temp1.seatG[0] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 1; i < 5; i++) {
			tempList.add(new JButton("S" + i));
		}
		temp1.seatS[2] = tempList;
		tempList = new ArrayList<JButton>();
		for (int i = 1; i < 7; i++) {
			tempList.add(new JButton("D" + i));
		}
		temp1.seatD[1] = tempList;
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
			Customer admin = new Customer("관리자", "", "", "admin", new char[] {'a', 'd', 'm', 'i', 'n', '!'});
			admin.setPassword();
			customers.add(admin);
		}
	}

	public static void main(String[] args) {
		new Main();
	}

}
