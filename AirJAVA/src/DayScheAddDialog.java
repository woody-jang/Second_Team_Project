import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class DayScheAddDialog extends JDialog {
	String selectedDate = "";
	public DayScheAddDialog(int year, int month, int day, PlaneSchedule schedule) {
		selectedDate = year + "." + month + "." + day;
		String[] planeNames = new String[Main.planes.size() + 1];
		planeNames[0] = "선택";
		for (int i = 0; i < Main.planes.size(); i++) {
			planeNames[i + 1] = Main.planes.get(i).planeName;
		}
		
		int idx = 0;
		for (int i = 0; i < Main.planes.size(); i++) {
			for (int j = 0; j < Main.planes.get(i).schedules.size(); j++) {
				if (Main.planes.get(i).schedules.get(j).equals(schedule)) {
					idx = i;
					break;
				}
			}
		}
		
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		JPanel firstPanel = new JPanel();
		JLabel planeNameLbl = new JLabel(" 편명");
		setLblDefault(planeNameLbl);
		firstPanel.add(planeNameLbl);

		JComboBox<String> planeNameComb = new JComboBox<String>(planeNames);
		setCombDefault(planeNameComb);
		firstPanel.add(planeNameComb);

		JLabel deptPlaceLbl = new JLabel("   출발지");
		setLblDefault(deptPlaceLbl);
		firstPanel.add(deptPlaceLbl);

		JComboBox<String> deptPlaceComb = new JComboBox<String>(PlaneScheAddDialog.airportPlaceDept);
		setCombDefault(deptPlaceComb);
		firstPanel.add(deptPlaceComb);

		JLabel arrvPlaceLbl = new JLabel("   도착지");
		setLblDefault(arrvPlaceLbl);
		firstPanel.add(arrvPlaceLbl);

		JComboBox<String> arrvPlaceComb = new JComboBox<String>(PlaneScheAddDialog.airportPlaceArrv);
		setCombDefault(arrvPlaceComb);
		firstPanel.add(arrvPlaceComb);

		JLabel deptTimeLbl = new JLabel("   출발시간");
		setLblDefault(deptTimeLbl);
		firstPanel.add(deptTimeLbl);

		JComboBox<String> deptTimeComb = new JComboBox<String>(PlaneScheAddDialog.deptTimeList);
		setCombDefault(deptTimeComb);
		firstPanel.add(deptTimeComb);

		JPanel btnPnl = new JPanel();
		JButton okBtn = new JButton("확 인");
		okBtn.setFont(new Font(okBtn.getFont().getName(), Font.PLAIN, 20));
		okBtn.setPreferredSize(new Dimension(100, 30));
		btnPnl.add(okBtn);

		JLabel blankLbl = new JLabel("   ");
		JButton cancelBtn = new JButton("취 소");
		cancelBtn.setFont(new Font(cancelBtn.getFont().getName(), Font.PLAIN, 20));
		cancelBtn.setPreferredSize(new Dimension(100, 30));
		btnPnl.add(blankLbl);
		btnPnl.add(cancelBtn);

		mainPnl.add(firstPanel);
		mainPnl.add(btnPnl);

		add(mainPnl);
		
		if (schedule != null) {
			planeNameComb.setSelectedItem(Main.planes.get(idx).planeName);
			deptPlaceComb.setSelectedItem(schedule.deptPlace);
			deptTimeComb.setSelectedItem(schedule.deptTime);
			arrvPlaceComb.removeAllItems();
			for (int i = 0; i < PlaneScheAddDialog.airportPlaceArrv.length; i++) {
				if (!PlaneScheAddDialog.airportPlaceArrv[i].equals(schedule.deptPlace)) {
					arrvPlaceComb.addItem(PlaneScheAddDialog.airportPlaceArrv[i]);
				}
			}
			arrvPlaceComb.setSelectedItem(schedule.arrvPlace);
		}

		deptPlaceComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIdx = deptPlaceComb.getSelectedIndex();
				arrvPlaceComb.removeAllItems();
				for (int i = 0; i < PlaneScheAddDialog.airportPlaceArrv.length; i++) {
					if (selectedIdx == 0) {
						arrvPlaceComb.addItem(PlaneScheAddDialog.airportPlaceArrv[i]);
					} else if (selectedIdx != i) {
						arrvPlaceComb.addItem(PlaneScheAddDialog.airportPlaceArrv[i]);
					}
				}
			}
		});

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String plane = (String) planeNameComb.getSelectedItem();
				String deptPlace = (String) deptPlaceComb.getSelectedItem();
				String arrvPlace = (String) arrvPlaceComb.getSelectedItem();
				String deptTime = (String) deptTimeComb.getSelectedItem();
				boolean schdChk = false;
				List<PlaneSchedule> schedules = new ArrayList<PlaneSchedule>();
				int idx = 0;
				if (schedule != null) {
					schedule.deptPlace = deptPlace;
					schedule.arrvPlace = arrvPlace;
					schedule.deptTime = deptTime;
				} else {
					for (; idx < Main.planes.size(); idx++) {
						if (Main.planes.get(idx).planeName.equals(plane)) {
							schedules = Main.planes.get(idx).schedules;
							break;
						}
					}
					if (schedules.size() != 0) {
						for (int i = 0; i < schedules.size(); i++) {
							if (schedules.get(i).deptDate.equals(selectedDate) && schedules.get(i).deptTime.equals(deptTime)) {
								schdChk = true;
							}
						}
					}
				}
				if (plane.equals("선택") || deptPlace.equals("선택") || arrvPlace.equals("선택") || deptTime.equals("선택")) {
					JOptionPane.showMessageDialog(null, "모두 선택하세요", "선택", JOptionPane.ERROR_MESSAGE);
				} else if (schdChk) {
					JOptionPane.showMessageDialog(null, "겹치는 스케쥴이 있습니다", "스케쥴 확인", JOptionPane.ERROR_MESSAGE);
				} else {
					schedules.add(new PlaneSchedule(deptPlace, arrvPlace, selectedDate, deptTime,
							Main.planes.get(idx).seatV, Main.planes.get(idx).seatG, Main.planes.get(idx).seatS));
					PlaneIO.save(Main.planes);
					dispose();
				}
			}
		});

		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		showGUI();
	}

	private void setCombDefault(JComboBox<String> nameComb) {
		nameComb.setFont(new Font(nameComb.getFont().getName(), Font.PLAIN, 20));
		nameComb.setPreferredSize(new Dimension(90, 30));
	}

	private void setLblDefault(JLabel nameLbl) {
		nameLbl.setFont(new Font(nameLbl.getFont().getName(), Font.PLAIN, 20));
		nameLbl.setHorizontalAlignment(JLabel.RIGHT);
	}

	void showGUI() {
		setTitle(selectedDate + "스케쥴 추가/수정");
		setModal(true);
		pack();
		setLocation(500, 370);
		setVisible(true);
	}
}
