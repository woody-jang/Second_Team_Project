import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class PlaneScheAddDialog extends JDialog {
	String[] airportPlaceDept = { "선택", "서울", "대구", "울산", "부산", "제주" };
	String[] airportPlaceArrv = { "선택", "서울", "대구", "울산", "부산", "제주" };
	String[] deptTimeList = { "선택", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00" };

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
		deptTimeComb.setPreferredSize(new Dimension(70, 30));
		informPnl.add(deptTimeComb);
		
		JPanel btnPnl = new JPanel();
		JButton okBtn = new JButton("확 인");
		okBtn.setFont(new Font(okBtn.getFont().getName(), Font.PLAIN, 20));
		okBtn.setPreferredSize(new Dimension(100, 30));
		btnPnl.add(okBtn);
		
		JLabel blankLbl = new JLabel("   ");
		JButton calcelBtn = new JButton("취 소");
		calcelBtn.setFont(new Font(calcelBtn.getFont().getName(), Font.PLAIN, 20));
		calcelBtn.setPreferredSize(new Dimension(100, 30));
		btnPnl.add(blankLbl);
		btnPnl.add(calcelBtn);
		
		deptPlaceComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JComboBox<String> tempComb = (JComboBox<String>) e.getSource();
				int selectedIdx = tempComb.getSelectedIndex();
				deptTimeComb.removeAllItems();
				for (int i = 0; i < airportPlaceArrv.length; i++) {
					if (selectedIdx == 0) {
						deptTimeComb.addItem(airportPlaceArrv[i]);
					}
					else if (selectedIdx != i) {
						deptTimeComb.addItem(airportPlaceArrv[i]);
					}
				}
			}
		});

		mainPnl.add(informPnl);
		mainPnl.add(btnPnl);

		add(mainPnl);

		setTitle("스케쥴 추가 / 수정");
		setModal(true);
		pack();
		setLocation(500, 370);
		setVisible(true);
	}
}
