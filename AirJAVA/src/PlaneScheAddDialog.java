import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PlaneScheAddDialog extends JDialog {
	static String[] airportPlaceDept = { "선택", "서울", "대구", "부산", "제주" };
	static String[] airportPlaceArrv = { "선택", "서울", "대구", "부산", "제주" };
	static String[] deptTimeList = { "선택", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00" };

	public PlaneScheAddDialog(Plane plane, int selectedBtn) {
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		JPanel informPnl = new JPanel();
		JLabel scheDateLbl = new JLabel(" 출발일 ");
		scheDateLbl.setFont(new Font(scheDateLbl.getFont().getName(), Font.PLAIN, 20));
		informPnl.add(scheDateLbl);

		String scheDateBtnStr = "미정";
		if (selectedBtn != -1) {
			scheDateBtnStr = plane.schedules.get(selectedBtn).deptDate;
		}
		JButton scheDateBtn = new JButton(scheDateBtnStr);
		scheDateBtn.setFont(new Font(scheDateLbl.getFont().getName(), Font.PLAIN, 20));
		scheDateBtn.setPreferredSize(new Dimension(120, 30));
		informPnl.add(scheDateBtn);

		JLabel deptPlaceLbl = new JLabel("출발지 ");
		deptPlaceLbl.setFont(new Font(deptPlaceLbl.getFont().getName(), Font.PLAIN, 20));
		deptPlaceLbl.setPreferredSize(new Dimension(90, 30));
		deptPlaceLbl.setHorizontalAlignment(JLabel.RIGHT);
		informPnl.add(deptPlaceLbl);

		JComboBox<String> deptPlaceComb = new JComboBox<String>(airportPlaceDept);
		deptPlaceComb.setFont(new Font(deptPlaceComb.getFont().getName(), Font.PLAIN, 20));
		deptPlaceComb.setPreferredSize(new Dimension(70, 30));
		informPnl.add(deptPlaceComb);
		
		JLabel arrvPlaceLbl = new JLabel("도착지 ");
		arrvPlaceLbl.setFont(new Font(arrvPlaceLbl.getFont().getName(), Font.PLAIN, 20));
		arrvPlaceLbl.setPreferredSize(new Dimension(90, 30));
		arrvPlaceLbl.setHorizontalAlignment(JLabel.RIGHT);
		informPnl.add(arrvPlaceLbl);
		
		JComboBox<String> arrvPlaceComb = new JComboBox<String>(airportPlaceArrv);
		arrvPlaceComb.setFont(new Font(arrvPlaceComb.getFont().getName(), Font.PLAIN, 20));
		arrvPlaceComb.setPreferredSize(new Dimension(70, 30));
		informPnl.add(arrvPlaceComb);
		
		JLabel deptTimeLbl = new JLabel("출발시간 ");
		deptTimeLbl.setFont(new Font(deptTimeLbl.getFont().getName(), Font.PLAIN, 20));
		deptTimeLbl.setPreferredSize(new Dimension(110, 30));
		deptTimeLbl.setHorizontalAlignment(JLabel.RIGHT);
		informPnl.add(deptTimeLbl);
		
		JComboBox<String> deptTimeComb = new JComboBox<String>(deptTimeList);
		deptTimeComb.setFont(new Font(deptTimeComb.getFont().getName(), Font.PLAIN, 20));
		deptTimeComb.setPreferredSize(new Dimension(80, 30));
		informPnl.add(deptTimeComb);
		
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
		
		deptPlaceComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIdx = deptPlaceComb.getSelectedIndex();
				arrvPlaceComb.removeAllItems();
				for (int i = 0; i < airportPlaceArrv.length; i++) {
					if (selectedIdx == 0) {
						arrvPlaceComb.addItem(airportPlaceArrv[i]);
					}
					else if (selectedIdx != i) {
						arrvPlaceComb.addItem(airportPlaceArrv[i]);
					}
				}
			}
		});

		scheDateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new Calendar(0, scheDateBtn.getText());
				if (!Calendar.selectedDate.equals("None")) {
					scheDateBtn.setText(Calendar.selectedDate);
					Calendar.selectedDate = "None";
				}
			}
		});
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String settedDate = scheDateBtn.getText();
				String deptPlaceStr = (String) deptPlaceComb.getSelectedItem();
				String arrvPlaceStr = (String) arrvPlaceComb.getSelectedItem();
				String deptTimeStr = (String) deptTimeComb.getSelectedItem();
				if (!(settedDate.equals("미정") || deptPlaceStr.equals("선택") || arrvPlaceStr.equals("선택") || deptTimeStr.equals("선택"))) {
					for (int i = 0; i < plane.schedules.size(); i++) {
						if (plane.schedules.get(i).deptDate.equals(settedDate)) {
							int time1 = Integer.parseInt(plane.schedules.get(i).deptTime.substring(0, 2));
							int time2 = Integer.parseInt(deptTimeStr.substring(0, 2));
							if (time1  == time2) {
								JOptionPane.showMessageDialog(null, "겹치는 스케쥴이 있습니다", "스케쥴 겹침", JOptionPane.ERROR_MESSAGE);
								return;
							}
						}
					}
					PlaneSchedule tempSchd = new PlaneSchedule(deptPlaceStr, arrvPlaceStr, settedDate, deptTimeStr,
							plane.seatV, plane.seatG, plane.seatS);
					plane.schedules.add(tempSchd);
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
		
		mainPnl.add(informPnl);
		mainPnl.add(btnPnl);

		add(mainPnl);

		showGUI();
	}

	void showGUI() {
		setTitle("스케쥴 추가 / 수정");
		setModal(true);
		pack();
		setLocation(500, 370);
		setVisible(true);
	}
}
