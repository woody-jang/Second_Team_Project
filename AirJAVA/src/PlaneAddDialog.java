import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class PlaneAddDialog extends JDialog {
	static int check = 0;
	static String planeModel = "";
	JPanel mainPnl;
	JPanel planeNamePnl;
	JRadioButton radioSection1;
	JRadioButton radioSection2;
	JRadioButton radioSection3;
	JComboBox<String> vComb;
	JComboBox<String> gComb;
	JComboBox<String> sComb;
	Plane tempPla;
	JPanel seatPos1;
	JPanel seatPos2;
	JPanel seatPos3;
	JLabel planeName1;
	JLabel planeName2;
	JLabel planeName3;
	JLabel planeName4;
	JLabel planeName5;

	public PlaneAddDialog(Plane tempPla) {
		this.tempPla = tempPla;
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		JPanel mainLblPnl = new JPanel();
		JLabel mainLbl = new JLabel("첫번째칸을 눌려 기종을 선택하세요");
		mainLbl.setHorizontalAlignment(JLabel.CENTER);
		mainLbl.setFont(new Font(mainLbl.getFont().getName(), Font.PLAIN, 15));
		mainLblPnl.add(mainLbl);
		mainPnl.add(mainLblPnl);

		planeNamePnl = new JPanel();
		planeName1 = new JLabel();
		planeName2 = new JLabel();
		planeName3 = new JLabel();

		planeName1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SelectPlaneModel();
				if (check == 1) {
					radioSection1.setEnabled(true);
					radioSection2.setEnabled(true);
					radioSection3.setEnabled(true);
					planeName1.setText(planeModel); // A
					char last = 65;
					for (int i = 0; i < Main.planes.size(); i++) {
						char tmpName = Main.planes.get(i).planeName.charAt(0);
						if (planeModel.equals(String.valueOf(tmpName))) { // 기종이 같고
							if (last == Main.planes.get(i).planeName.charAt(1)) { // 두번째 글자가 같다면
								last++;
							} else
								break;
						}
					}
					char tempModel = (char) last;
					planeName2.setText(String.valueOf(tempModel));
				}
			}
		});
		mainPnl.add(planeNamePnl);
		planeName4 = new JLabel();
		planeName5 = new JLabel();
		setNameLblDefault(planeName1, 0);
		setNameLblDefault(planeName2, 1);
		setNameLblDefault(planeName3, 2);
		setNameLblDefault(planeName4, 3);
		setNameLblDefault(planeName5, 4);

		JPanel radioBtnPnl = new JPanel();
		radioSection1 = new JRadioButton("1구역");
		radioSection2 = new JRadioButton("2구역");
		radioSection3 = new JRadioButton("3구역");
		setRadioSectionDefault();
		ButtonGroup radioGroup = new ButtonGroup();
		radioSection1.addActionListener(new myRadioActionListener());
		radioSection2.addActionListener(new myRadioActionListener());
		radioSection3.addActionListener(new myRadioActionListener());
		radioSection1.addItemListener(new myRadioItemListener());
		radioSection2.addItemListener(new myRadioItemListener());
		radioSection3.addItemListener(new myRadioItemListener());
		radioGroup.add(radioSection1);
		radioGroup.add(radioSection2);
		radioGroup.add(radioSection3);
		radioBtnPnl.add(radioSection1);
		radioBtnPnl.add(radioSection2);
		radioBtnPnl.add(radioSection3);
		mainPnl.add(radioBtnPnl);

		String[] seatCnt = { "선택", "2", "4", "6" };
		JPanel comboPnl = new JPanel();
		JLabel vLbl = new JLabel(" V좌석");
		vComb = new JComboBox<String>(seatCnt);

		JLabel gLbl = new JLabel(" G좌석");
		gComb = new JComboBox<String>(seatCnt);

		JLabel sLbl = new JLabel(" S좌석");
		sComb = new JComboBox<String>(seatCnt);

		vLbl.setPreferredSize(new Dimension(50, 20));
		vLbl.setFont(new Font(vLbl.getFont().getName(), Font.PLAIN, 15));
		vComb.setEnabled(false);
		vComb.addActionListener(new myCombBoxActionListener());
		gComb.addActionListener(new myCombBoxActionListener());
		sComb.addActionListener(new myCombBoxActionListener());
		comboPnl.add(vLbl);
		comboPnl.add(vComb);

		gLbl.setPreferredSize(new Dimension(50, 20));
		gLbl.setFont(new Font(gLbl.getFont().getName(), Font.PLAIN, 15));
		gComb.setEnabled(false);
		comboPnl.add(gLbl);
		comboPnl.add(gComb);

		sLbl.setPreferredSize(new Dimension(50, 20));
		sLbl.setFont(new Font(sLbl.getFont().getName(), Font.PLAIN, 15));
		sComb.setEnabled(false);
		comboPnl.add(sLbl);
		comboPnl.add(sComb);
		mainPnl.add(comboPnl);

		JPanel seatPosPnl = new JPanel();
		seatPos1 = new JPanel(); // 1구역 패널
		seatPos1.setLayout(new BoxLayout(seatPos1, BoxLayout.Y_AXIS));
		seatPos2 = new JPanel(); // 2구역 패널
		seatPos2.setLayout(new BoxLayout(seatPos2, BoxLayout.Y_AXIS));
		seatPos3 = new JPanel(); // 3구역 패널
		seatPos3.setLayout(new BoxLayout(seatPos3, BoxLayout.Y_AXIS));

		seatPos1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "1구역",
				TitledBorder.CENTER, TitledBorder.TOP));
		seatPos2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "2구역",
				TitledBorder.CENTER, TitledBorder.TOP));
		seatPos3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 2), "3구역",
				TitledBorder.CENTER, TitledBorder.TOP));
		seatPos1.setPreferredSize(new Dimension(150, 140));
		seatPos2.setPreferredSize(new Dimension(150, 140));
		seatPos3.setPreferredSize(new Dimension(150, 140));
		seatPosPnl.add(seatPos1);
		seatPosPnl.add(seatPos2);
		seatPosPnl.add(seatPos3);
		mainPnl.add(seatPosPnl);

		setSeatPosPnl(); // 기존 비행기를 수정하는 경우 시트를 그려놓기 위한 메소드

		JPanel okCanBtnPnl = new JPanel();
		JButton okBtn = new JButton("확인");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean[] check = new boolean[3];
				for (int i = 0; i < 3; i++) {
					if (tempPla.seatV[i] != null)
						check[i] = true;
					else if (tempPla.seatG[i] != null)
						check[i] = true;
					else if (tempPla.seatS[i] != null)
						check[i] = true;
				}
				if (!(check[0] && check[1] && check[2])) {
					JOptionPane.showMessageDialog(null, "모두 선택하세요", "", JOptionPane.ERROR_MESSAGE);
					return;
				}
				StringBuffer planeNameSB = new StringBuffer();
				planeNameSB.append(planeName1.getText());
				planeNameSB.append(planeName2.getText());
				planeNameSB.append(planeName3.getText());
				planeNameSB.append(planeName4.getText());
				planeNameSB.append(planeName5.getText());
				tempPla.planeName = planeNameSB.toString();
				Main.checkAddPlane = 1;
				dispose();
			}
		});
		JButton cancelBtn = new JButton("취소");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		okCanBtnPnl.add(okBtn);
		okCanBtnPnl.add(cancelBtn);
		mainPnl.add(okCanBtnPnl);

		add(mainPnl);

		showGUI();
	}

	private void setSeatPosPnl() {
		for (int i = 0; i < 3; i++) {
			if (tempPla.seatV[i] != null) { // 1구역부터 3구역까지 확인하면서
				setSeatPosLbl(tempPla.seatV[i], "V", i);
			} else if (tempPla.seatG[i] != null) { // V, G, S 중에 좌석이 이미 있다면
				setSeatPosLbl(tempPla.seatG[i], "G", i);
			} else if (tempPla.seatS[i] != null) { // 아래에 있는 메소드를 실행해서 좌석 배치
				setSeatPosLbl(tempPla.seatS[i], "S", i);
			}
		}
	}

	private void setSeatPosLbl(List<JButton> seat, String seatGrade, int idx) {
		for (int i = 0; i < 2; i++) { // 좌석배치에서 윗열, 아랫열 2개의 패널로 구성됨
			JPanel tempPnl = new JPanel();
			for (int j = 0; j < seat.size() / 2; j++) { // 윗열 아랫열로 나누어져서 반복횟수는 시트갯수 / 2
				int temp = (i * 1) + (j * 2) + 1; // 좌석 번호를 저장할 임시 변수
				JLabel tempLbl1 = new JLabel(seatGrade + " 0" + temp);
				tempLbl1.setPreferredSize(new Dimension(40, 50));
				tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				tempLbl1.setHorizontalAlignment(JLabel.CENTER);
				tempPnl.add(tempLbl1); // 패널에 윗열의 좌석을 추가함
			}
			if (idx == 0)
				seatPos1.add(tempPnl); // 1구역이라면 1구역 패널에 추가
			else if (idx == 1)
				seatPos2.add(tempPnl);
			else if (idx == 2)
				seatPos3.add(tempPnl);
		}
	}

	void showGUI() {
		setTitle("비행기 추가 / 수정");
		setModal(true);
//		setSize(300, 300);
		pack();
		setLocation(650, 350);
		setVisible(true);
	}

	private void setRadioSectionDefault() {
		radioSection1.setFont(new Font(radioSection1.getFont().getName(), Font.PLAIN, 20));
		radioSection2.setFont(new Font(radioSection2.getFont().getName(), Font.PLAIN, 20));
		radioSection3.setFont(new Font(radioSection3.getFont().getName(), Font.PLAIN, 20));
		if (tempPla.planeName == null) {
			radioSection1.setEnabled(false);
			radioSection2.setEnabled(false);
			radioSection3.setEnabled(false);
		}
	}

	private void setNameLblDefault(JLabel planeNameLbl, int idx) {
		planeNameLbl.setPreferredSize(new Dimension(80, 80));
		planeNameLbl.setFont(new Font(planeNameLbl.getFont().getName(), Font.PLAIN, 35));
		planeNameLbl.setHorizontalAlignment(JLabel.CENTER);
		planeNameLbl.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		if (tempPla.planeName != null) {
			planeNameLbl.setText(tempPla.planeName.substring(idx, idx + 1));
		}
		planeNamePnl.add(planeNameLbl);
	}

	private int getCountOfSeat(List<JButton>[] seat) {
		int count = 0;
		for (int i = 0; i < 3; i++) {
			if (seat[i] != null)
				count += seat[i].size();
		}
		return count;
	}

	class myRadioActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JRadioButton tempRadBtn = (JRadioButton) e.getSource();
			if (tempRadBtn.isSelected()) {
				vComb.setEnabled(true);
				gComb.setEnabled(true);
				sComb.setEnabled(true);
			}
		}
	}

	class myRadioItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			vComb.setSelectedIndex(0);
			gComb.setSelectedIndex(0);
			sComb.setSelectedIndex(0);
		}
	}

	class myCombBoxActionListener implements ActionListener {
		String seatGrade = "";
		int select;

		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<String> tempComb = (JComboBox<String>) e.getSource();
			if (tempComb.equals(vComb)) {
				gComb.setEnabled(false);
				sComb.setEnabled(false);
				seatGrade = "V";
			} else if (tempComb.equals(gComb)) {
				vComb.setEnabled(false);
				sComb.setEnabled(false);
				seatGrade = "G";
			} else if (tempComb.equals(sComb)) {
				vComb.setEnabled(false);
				gComb.setEnabled(false);
				seatGrade = "S";
			}
			select = tempComb.getSelectedIndex() * 2;
			int section = 0;
			if (radioSection1.isSelected())
				section = 1;
			else if (radioSection2.isSelected())
				section = 2;
			else if (radioSection3.isSelected())
				section = 3;
			if (select == 0)
				return;
			addSeatLbl(section);
		}

		private void addSeatLbl(int section) {
			if (section == 1)
				seatPos1.removeAll();
			else if (section == 2)
				seatPos2.removeAll();
			else if (section == 3)
				seatPos3.removeAll();
			if (seatGrade.equals("V")) {
				tempPla.seatV[section - 1] = new ArrayList<JButton>();
				tempPla.seatG[section - 1] = null;
				tempPla.seatS[section - 1] = null;
			} else if (seatGrade.equals("G")) {
				tempPla.seatV[section - 1] = null;
				tempPla.seatG[section - 1] = new ArrayList<JButton>();
				tempPla.seatS[section - 1] = null;
			} else if (seatGrade.equals("S")) {
				tempPla.seatV[section - 1] = null;
				tempPla.seatG[section - 1] = null;
				tempPla.seatS[section - 1] = new ArrayList<JButton>();
			}
			if (select == 6) {
				for (int i = 0; i < 2; i++) {
					JPanel tempPnl = new JPanel();
					JLabel tempLbl1 = new JLabel(seatGrade + " 0" + (i + 1));
					tempLbl1.setPreferredSize(new Dimension(40, 50));
					tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					tempLbl1.setHorizontalAlignment(JLabel.CENTER);
					JLabel tempLbl2 = new JLabel(seatGrade + " 0" + (i + 3));
					tempLbl2.setPreferredSize(new Dimension(40, 50));
					tempLbl2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					tempLbl2.setHorizontalAlignment(JLabel.CENTER);
					JLabel tempLbl3 = new JLabel(seatGrade + " 0" + (i + 5));
					tempLbl3.setPreferredSize(new Dimension(40, 50));
					tempLbl3.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					tempLbl3.setHorizontalAlignment(JLabel.CENTER);
					tempPnl.add(tempLbl1);
					tempPnl.add(tempLbl2);
					tempPnl.add(tempLbl3);
					if (section == 1)
						seatPos1.add(tempPnl);
					else if (section == 2)
						seatPos2.add(tempPnl);
					else if (section == 3)
						seatPos3.add(tempPnl);
					if (seatGrade.equals("V")) {
						tempPla.seatV[section - 1].add(new JButton("V 0" + (i + 1)));
						tempPla.seatV[section - 1].add(new JButton("V 0" + (i + 3)));
						tempPla.seatV[section - 1].add(new JButton("V 0" + (i + 5)));
					} else if (seatGrade.equals("G")) {
						tempPla.seatG[section - 1].add(new JButton("G 0" + (i + 1)));
						tempPla.seatG[section - 1].add(new JButton("G 0" + (i + 3)));
						tempPla.seatG[section - 1].add(new JButton("G 0" + (i + 5)));
					} else if (seatGrade.equals("S")) {
						tempPla.seatS[section - 1].add(new JButton("S 0" + (i + 1)));
						tempPla.seatS[section - 1].add(new JButton("S 0" + (i + 3)));
						tempPla.seatS[section - 1].add(new JButton("S 0" + (i + 5)));
					}
				}
				planeName3.setText(String.valueOf(getCountOfSeat(tempPla.seatV)));
				planeName4.setText(String.valueOf(getCountOfSeat(tempPla.seatG)));
				planeName5.setText(String.valueOf(getCountOfSeat(tempPla.seatS)));
			} else if (select == 4) {
				for (int i = 0; i < 2; i++) {
					JPanel tempPnl = new JPanel();
					JLabel tempLbl1 = new JLabel(seatGrade + " 0" + (i + 1));
					tempLbl1.setPreferredSize(new Dimension(40, 50));
					tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					tempLbl1.setHorizontalAlignment(JLabel.CENTER);
					JLabel tempLbl2 = new JLabel(seatGrade + " 0" + (i + 3));
					tempLbl2.setPreferredSize(new Dimension(40, 50));
					tempLbl2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					tempLbl2.setHorizontalAlignment(JLabel.CENTER);
					tempPnl.add(tempLbl1);
					tempPnl.add(tempLbl2);
					if (section == 1)
						seatPos1.add(tempPnl);
					else if (section == 2)
						seatPos2.add(tempPnl);
					else if (section == 3)
						seatPos3.add(tempPnl);
					if (seatGrade.equals("V")) {
						tempPla.seatV[section - 1].add(new JButton("V 0" + (i + 1)));
						tempPla.seatV[section - 1].add(new JButton("V 0" + (i + 3)));
					} else if (seatGrade.equals("G")) {
						tempPla.seatG[section - 1].add(new JButton("G 0" + (i + 1)));
						tempPla.seatG[section - 1].add(new JButton("G 0" + (i + 3)));
					} else if (seatGrade.equals("S")) {
						tempPla.seatS[section - 1].add(new JButton("S 0" + (i + 1)));
						tempPla.seatS[section - 1].add(new JButton("S 0" + (i + 3)));
					}
				}
				planeName3.setText(String.valueOf(getCountOfSeat(tempPla.seatV)));
				planeName4.setText(String.valueOf(getCountOfSeat(tempPla.seatG)));
				planeName5.setText(String.valueOf(getCountOfSeat(tempPla.seatS)));
			} else if (select == 2) {
				for (int i = 0; i < 2; i++) {
					JPanel tempPnl = new JPanel();
					JLabel tempLbl1 = new JLabel(seatGrade + " 0" + (i + 1));
					tempLbl1.setPreferredSize(new Dimension(40, 50));
					tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
					tempLbl1.setHorizontalAlignment(JLabel.CENTER);
					tempPnl.add(tempLbl1);
					if (section == 1)
						seatPos1.add(tempPnl);
					else if (section == 2)
						seatPos2.add(tempPnl);
					else if (section == 3)
						seatPos3.add(tempPnl);
					if (seatGrade.equals("V")) {
						tempPla.seatV[section - 1].add(new JButton("V 0" + (i + 1)));
					} else if (seatGrade.equals("G")) {
						tempPla.seatG[section - 1].add(new JButton("G 0" + (i + 1)));
					} else if (seatGrade.equals("S")) {
						tempPla.seatS[section - 1].add(new JButton("S 0" + (i + 1)));
					}
				}
				planeName3.setText(String.valueOf(getCountOfSeat(tempPla.seatV)));
				planeName4.setText(String.valueOf(getCountOfSeat(tempPla.seatG)));
				planeName5.setText(String.valueOf(getCountOfSeat(tempPla.seatS)));
			}
			revalidate();
			repaint();
		}
	}
}