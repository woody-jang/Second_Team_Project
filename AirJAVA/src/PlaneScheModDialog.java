import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class PlaneScheModDialog extends JDialog {
	JPanel mainPnl;
	JPanel titleLblPnl;
	JLabel titleLbl;
	JButton addSchdBtn;
	Plane plane;
	JPanel planeSchedPnl;
	List<JButton> modBtnList;
	List<JButton> delBtnList;

	public PlaneScheModDialog(Plane plane) {
		this.plane = plane;
//		plane.schedules = new ArrayList<PlaneSchedule>();
//		PlaneSchedule tempSchdPlane = new PlaneSchedule();
//		tempSchdPlane.arrvPlace = "제주";
//		tempSchdPlane.deptDate = "21.07.25";
//		tempSchdPlane.deptPlace = "부산";
//		tempSchdPlane.deptTime = "06:00";
//		tempSchdPlane.seatV = plane.seatV;
//		tempSchdPlane.seatG = plane.seatG;
//		tempSchdPlane.seatS = plane.seatS;
//		plane.schedules.add(tempSchdPlane);
		
		mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		titleLblPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		titleLbl = new JLabel("   " + plane.planeName + " 편 스케쥴 목록");
		titleLbl.setPreferredSize(new Dimension(850, 45));
		titleLbl.setHorizontalAlignment(JLabel.LEFT);
		titleLbl.setFont(new Font(titleLbl.getFont().getName(), Font.PLAIN, 25));
		titleLblPnl.add(titleLbl);
		
		addSchdBtn = new JButton("스케쥴 추가");
		addSchdBtn.addActionListener(new mySchdAddActionListener());
		addSchdBtn.setFont(new Font(addSchdBtn.getFont().getName(), Font.PLAIN, 20));
		addSchdBtn.setPreferredSize(new Dimension(160, 45));
		titleLblPnl.add(addSchdBtn);
		mainPnl.add(titleLblPnl);
		
		planeSchedPnl = new JPanel();
		planeSchedPnl.setLayout(new BoxLayout(planeSchedPnl, BoxLayout.Y_AXIS));

		addPlaSchds();
		mainPnl.add(planeSchedPnl);
		
		JPanel btnPnl = new JPanel();
		JButton okBtn = new JButton("확 인");
		okBtn.setFont(new Font(okBtn.getFont().getName(), Font.PLAIN, 20));
		okBtn.setPreferredSize(new Dimension(100, 30));
		btnPnl.add(okBtn);
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PlaneIO.save(Main.planes);
				Main.checkModPlaneSche = 1;
				dispose();
			}
		});
		
		JLabel blankLbl = new JLabel("   ");
		JButton cancelBtn = new JButton("취 소");
		cancelBtn.setFont(new Font(cancelBtn.getFont().getName(), Font.PLAIN, 20));
		cancelBtn.setPreferredSize(new Dimension(100, 30));
		btnPnl.add(blankLbl);
		btnPnl.add(cancelBtn);
		mainPnl.add(btnPnl);
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, "취소를 누르면 저장이 되지 않습니다!", "저장 불가", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (choice == 0) {
					dispose();
				}
			}
		});
		
		add(mainPnl);

		setTitle(plane.planeName);
		setModal(true);
		setLocation(400, 350);
		pack();
		setVisible(true);
	}

	private void addPlaSchds() {
		modBtnList = new ArrayList<JButton>();
		delBtnList = new ArrayList<JButton>();
		for (int i = 0; i < plane.schedules.size(); i++) {
			PlaneSchedule tempPlaSchd = plane.schedules.get(i);
			
			JPanel tempSchdPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));

			String deptDate = tempPlaSchd.deptDate;
			String deptPlace = tempPlaSchd.deptPlace;
			String arrvPlace = tempPlaSchd.arrvPlace;
			String deptTime = tempPlaSchd.deptTime;
			int[] countSeatReserve = new int[3];
			for (int j = 0; j < 3; j++) {
				if (tempPlaSchd.getSeatSelectedV[i] != null) {
					for (int k = 0; k < tempPlaSchd.getSeatSelectedV[i].size(); k++) {
						if (!tempPlaSchd.getSeatSelectedV[i].get(k))
							countSeatReserve[0]++;
					}
				}
				if (tempPlaSchd.getSeatSelectedG[i] != null) {
					for (int k = 0; k < tempPlaSchd.getSeatSelectedG[i].size(); k++) {
						if (!tempPlaSchd.getSeatSelectedG[i].get(k))
							countSeatReserve[1]++;
					}
				}
				if (tempPlaSchd.getSeatSelectedS[i] != null) {
					for (int k = 0; k < tempPlaSchd.getSeatSelectedS[i].size(); k++) {
						if (!tempPlaSchd.getSeatSelectedS[i].get(k))
							countSeatReserve[2]++;
					}
				}
			}
			
			JLabel tempSchdLbl = new JLabel("  " + deptDate + ",  " + deptTime + ",  " + deptPlace + " → " + arrvPlace + ",  총(예약)좌석: " +
			plane.getCntOfSeatV() + "(" + countSeatReserve[0] + " V), " + plane.getCntOfSeatG() + "(" + countSeatReserve[1] + " G), " +
			plane.getCntOfSeatS() + "(" + countSeatReserve[2] + " S)");
			tempSchdLbl.setPreferredSize(new Dimension(850, 80));
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
					int i = 0;
					for (; i < modBtnList.size(); i++) {
						if (modBtnList.get(i).equals((JButton) e.getSource())){
							break;
						}
					}
					new PlaneScheAddDialog(plane, i);
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
					JButton clickedBtn = (JButton) e.getSource();
					for (int j = 0; j < delBtnList.size(); j++) {
						if (delBtnList.get(j).equals(clickedBtn)) {
							int choice = JOptionPane.showConfirmDialog(null, "삭제하시겠습니까?\n복구할 수 없습니다", "삭제 확인",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
							if (choice == 0) {
								plane.schedules.remove(j);
								planeSchedPnl.remove(j);
								planeSchedPnl.repaint();
								planeSchedPnl.revalidate();
							}
						}
					}
				}
			});
			
			planeSchedPnl.add(tempSchdPnl);
		}
	}
	
	class mySchdAddActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new PlaneScheAddDialog(plane, -1);
			planeSchedPnl.removeAll();
			addPlaSchds();
			repaint();
			revalidate();
			pack();
		}
	}
}
