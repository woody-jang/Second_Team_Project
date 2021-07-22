import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;

public class PlaneAddDialog extends JDialog {
	static int check = 0;
	static String planeModel = "";
	
	public PlaneAddDialog() {
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));
		
		Integer[] seatCnt = {2, 4, 6};
		JPanel comboPnl = new JPanel();
		JLabel gLbl = new JLabel(" G좌석");
		JComboBox<Integer> gComb = new JComboBox<Integer>(seatCnt);
		
		JLabel sLbl = new JLabel(" S좌석");
		JComboBox<Integer> sComb = new JComboBox<Integer>(seatCnt);
		
		JLabel dLbl = new JLabel(" D좌석");
		JComboBox<Integer> dComb = new JComboBox<Integer>(seatCnt);
		
		JRadioButton radioSection1 = new JRadioButton("1구역");
		JRadioButton radioSection2 = new JRadioButton("2구역");
		JRadioButton radioSection3 = new JRadioButton("3구역");
		
		JPanel planeNamePnl = new JPanel();
		JLabel planeName1 = new JLabel();
		planeName1.setPreferredSize(new Dimension(80, 80));
		planeName1.setFont(new Font(planeName1.getFont().getName(), Font.PLAIN, 35));
		planeName1.setHorizontalAlignment(JLabel.CENTER);
		planeName1.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		planeNamePnl.add(planeName1);
		JLabel planeName2 = new JLabel();
		planeName2.setPreferredSize(new Dimension(80, 80));
		planeName2.setFont(new Font(planeName2.getFont().getName(), Font.PLAIN, 35));
		planeName2.setHorizontalAlignment(JLabel.CENTER);
		planeName2.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		planeNamePnl.add(planeName2);
		JLabel planeName3 = new JLabel();
		planeName3.setPreferredSize(new Dimension(80, 80));
		planeName3.setFont(new Font(planeName3.getFont().getName(), Font.PLAIN, 35));
		planeName3.setHorizontalAlignment(JLabel.CENTER);
		planeName3.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		planeNamePnl.add(planeName3);
		JLabel planeName4 = new JLabel();
		planeName4.setPreferredSize(new Dimension(80, 80));
		planeName4.setFont(new Font(planeName4.getFont().getName(), Font.PLAIN, 35));
		planeName4.setHorizontalAlignment(JLabel.CENTER);
		planeName4.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		planeNamePnl.add(planeName4);
		JLabel planeName5 = new JLabel();
		planeName5.setPreferredSize(new Dimension(80, 80));
		planeName5.setFont(new Font(planeName5.getFont().getName(), Font.PLAIN, 35));
		planeName5.setHorizontalAlignment(JLabel.CENTER);
		planeName5.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		planeNamePnl.add(planeName5);
		
		planeName1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				new SelectPlaneModel();
				if (check == 1) {
					radioSection1.setEnabled(true);
					radioSection2.setEnabled(true);
					radioSection3.setEnabled(true);
					planeName1.setText(planeModel);
					int count = 0;
					char last = 64;
					for (int i = 0; i < Main.planes.size(); i++) {
						char tmpName = Main.planes.get(i).planeName.charAt(0);
						if (planeModel.equals(String.valueOf(tmpName))) {
							if (last + 1 == Main.planes.get(i).planeName.charAt(1)) {
								count++;
							}
							else
								break;
						}
					}
					char tempModel = (char) (count + 65);
					planeName2.setText(String.valueOf(tempModel));
				}
			}
		});
		mainPnl.add(planeNamePnl);
		
		JPanel seatPosPnl = new JPanel();
		JPanel seatPos1 = new JPanel();
		seatPos1.setLayout(new BoxLayout(seatPos1, BoxLayout.Y_AXIS));
		JLabel seatLbl1 = new JLabel("  1구역");
		JPanel seatPos2 = new JPanel();
		seatPos2.setLayout(new BoxLayout(seatPos2, BoxLayout.Y_AXIS));
		JLabel seatLbl2 = new JLabel("  2구역");
		JPanel seatPos3 = new JPanel();
		seatPos3.setLayout(new BoxLayout(seatPos3, BoxLayout.Y_AXIS));
		JLabel seatLbl3 = new JLabel("  3구역");
		
		JPanel radioBtnPnl = new JPanel();
		radioSection1.setFont(new Font(radioSection1.getFont().getName(), Font.PLAIN, 20));
		radioSection2.setFont(new Font(radioSection2.getFont().getName(), Font.PLAIN, 20));
		radioSection3.setFont(new Font(radioSection3.getFont().getName(), Font.PLAIN, 20));
		ButtonGroup radioGroup = new ButtonGroup();
		radioSection1.setEnabled(false);
		radioSection2.setEnabled(false);
		radioSection3.setEnabled(false);
		radioSection1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (radioSection1.isSelected()) {
					gComb.setEnabled(true);
					sComb.setEnabled(true);
					dComb.setEnabled(true);
				}
			}
		});
		radioGroup.add(radioSection1);
		radioGroup.add(radioSection2);
		radioGroup.add(radioSection3);
		radioBtnPnl.add(radioSection1);
		radioBtnPnl.add(radioSection2);
		radioBtnPnl.add(radioSection3);
		mainPnl.add(radioBtnPnl);
		
		gLbl.setPreferredSize(new Dimension(50, 20));
		gLbl.setFont(new Font(gLbl.getFont().getName(), Font.PLAIN, 15));
		gComb.setEnabled(false);
		gComb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				seatPos1.removeAll();
				seatPos1.add(seatLbl1);
				sComb.setEnabled(false);
				dComb.setEnabled(false);
				JComboBox<Integer> temp = (JComboBox<Integer>) e.getSource();
				int select = (temp.getSelectedIndex() + 1) * 2;
				int section = 0;
				if (radioSection1.isSelected())
					section = 1;
				else if (radioSection2.isSelected())
					section = 2;
				else if (radioSection3.isSelected())
					section = 3;
				switch (section) {
				case 1:
					if (select == 6) {
						for (int i = 0; i < 2; i++) {
							JPanel tempPnl = new JPanel();
							JLabel tempLbl1 = new JLabel("G 0" + (i + 5));
							tempLbl1.setPreferredSize(new Dimension(40, 50));
							tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
							tempLbl1.setHorizontalAlignment(JLabel.CENTER);
							JLabel tempLbl2 = new JLabel("G 0" + (i + 3));
							tempLbl2.setPreferredSize(new Dimension(40, 50));
							tempLbl2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
							tempLbl2.setHorizontalAlignment(JLabel.CENTER);
							JLabel tempLbl3 = new JLabel("G 0" + (i + 1));
							tempLbl3.setPreferredSize(new Dimension(40, 50));
							tempLbl3.setBorder(BorderFactory.createLineBorder(Color.black, 1));
							tempLbl3.setHorizontalAlignment(JLabel.CENTER);
							tempPnl.add(tempLbl1);
							tempPnl.add(tempLbl2);
							tempPnl.add(tempLbl3);
							seatPos1.add(tempPnl);
						}
						planeName3.setText(String.valueOf(6));
					}
					else if (select == 4) {
						for (int i = 0; i < 2; i++) {
							JPanel tempPnl = new JPanel();
							JLabel tempLbl1 = new JLabel("G 0" + (i + 3));
							tempLbl1.setPreferredSize(new Dimension(40, 50));
							tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
							tempLbl1.setHorizontalAlignment(JLabel.CENTER);
							JLabel tempLbl2 = new JLabel("G 0" + (i + 1));
							tempLbl2.setPreferredSize(new Dimension(40, 50));
							tempLbl2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
							tempLbl2.setHorizontalAlignment(JLabel.CENTER);
							tempPnl.add(tempLbl1);
							tempPnl.add(tempLbl2);
							seatPos1.add(tempPnl);
						}
						planeName3.setText(String.valueOf(4));
					}
					else if (select == 2) {
						for (int i = 0; i < 2; i++) {
							JPanel tempPnl = new JPanel();
							JLabel tempLbl1 = new JLabel("G 0" + (i + 1));
							tempLbl1.setPreferredSize(new Dimension(40, 50));
							tempLbl1.setBorder(BorderFactory.createLineBorder(Color.black, 1));
							tempLbl1.setHorizontalAlignment(JLabel.CENTER);
							tempPnl.add(tempLbl1);
							seatPos1.add(tempPnl);
						}
						planeName3.setText(String.valueOf(2));
					}
					repaint();
					revalidate();
					break;
				}
			}
		});
		comboPnl.add(gLbl);
		comboPnl.add(gComb);
		
		sLbl.setPreferredSize(new Dimension(50, 20));
		sLbl.setFont(new Font(sLbl.getFont().getName(), Font.PLAIN, 15));
		sComb.setEnabled(false);
		comboPnl.add(sLbl);
		comboPnl.add(sComb);
		
		dLbl.setPreferredSize(new Dimension(50, 20));
		dLbl.setFont(new Font(dLbl.getFont().getName(), Font.PLAIN, 15));
		dComb.setEnabled(false);
		comboPnl.add(dLbl);
		comboPnl.add(dComb);
		mainPnl.add(comboPnl);
		
		seatPos1.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		seatPos2.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		seatPos3.setBorder(BorderFactory.createLineBorder(Color.black, 2));
		seatPos1.setPreferredSize(new Dimension(150, 200));
		seatPos2.setPreferredSize(new Dimension(150, 200));
		seatPos3.setPreferredSize(new Dimension(150, 200));
		seatPos1.add(seatLbl1);
		seatPosPnl.add(seatPos1);
		seatPos2.add(seatLbl2);
		seatPosPnl.add(seatPos2);
		seatPos3.add(seatLbl3);
		seatPosPnl.add(seatPos3);
		mainPnl.add(seatPosPnl);
		
		add(mainPnl);
		
		setModal(true);
//		setSize(300, 300);
		pack();
		setLocation(650, 350);
		setVisible(true);
	}
}
