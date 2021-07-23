import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class planeScheModDialog extends JDialog {
	JPanel mainPnl;
	JPanel titleLblPnl;
	JLabel titleLbl;
	JButton addSchdBtn;

	public planeScheModDialog(Plane plane) {
		plane.schedules = new ArrayList<PlaneSchedule>();
		PlaneSchedule tempSchdPlane = new PlaneSchedule();
		tempSchdPlane.arrvPlace = "제주";
		tempSchdPlane.deptDate = "21.07.25";
		tempSchdPlane.deptPlace = "부산";
		tempSchdPlane.deptTime = "06:10";
		tempSchdPlane.seatV = plane.seatV;
		tempSchdPlane.seatG = plane.seatG;
		tempSchdPlane.seatS = plane.seatS;
		plane.schedules.add(tempSchdPlane);
		
		mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		titleLblPnl = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		titleLbl = new JLabel("   " + plane.planeName + " 편 스케쥴 목록");
		titleLbl.setPreferredSize(new Dimension(850, 80));
		titleLbl.setHorizontalAlignment(JLabel.LEFT);
		titleLbl.setFont(new Font(titleLbl.getFont().getName(), Font.PLAIN, 25));
		titleLblPnl.add(titleLbl);

		addSchdBtn = new JButton("스케쥴 추가");
		addSchdBtn.addActionListener(new mySchdAddActionListener());
		addSchdBtn.setFont(new Font(addSchdBtn.getFont().getName(), Font.PLAIN, 20));
		addSchdBtn.setPreferredSize(new Dimension(160, 80));
		titleLblPnl.add(addSchdBtn);
		mainPnl.add(titleLblPnl);

		addPlaSchds(plane);
		
		add(mainPnl);

		setTitle(plane.planeName);
		setModal(true);
		setLocation(530, 350);
		pack();
		setVisible(true);
	}

	private void addPlaSchds(Plane plane) {
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

			JButton delBtn = new JButton("삭제");
			delBtn.setFont(new Font(delBtn.getFont().getName(), Font.PLAIN, 20));
			delBtn.setPreferredSize(new Dimension(80, 80));
			tempSchdPnl.add(delBtn);
			
			mainPnl.add(tempSchdPnl);
		}
	}
	
	class mySchdAddActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
	}
}
