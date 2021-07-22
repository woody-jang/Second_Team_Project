import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class SelectPlaneModel extends JDialog {
	public SelectPlaneModel() {
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		JPanel informPnl = new JPanel();
		JLabel informLbl = new JLabel("모델을 선택하세요");
		informLbl.setFont(new Font(informLbl.getFont().getName(), Font.PLAIN, 25));
		informPnl.add(informLbl);
		mainPnl.add(informPnl);
		
		ButtonGroup radioGroup = new ButtonGroup();
		List<JRadioButton> radioList = new ArrayList<>();
		for (int i = 0; i < 6; i++) {
			JPanel tempBtnPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
			for (int j = 0; j < 5; j++) {
				char ch = (char) ((i * 5) + j + 65);
				if (ch > 90)
					break;
				JRadioButton radioBtn = new JRadioButton(String.valueOf(ch));
				radioBtn.setPreferredSize(new Dimension(50, 30));
				radioBtn.setFont(new Font(radioBtn.getFont().getName(), Font.PLAIN, 20));
				tempBtnPnl.add(radioBtn);
				radioList.add(radioBtn);
				radioGroup.add(radioBtn);
			}
			mainPnl.add(tempBtnPnl);
		}
		
		JPanel btnPnl = new JPanel();
		JButton okBtn = new JButton("확인");
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < radioList.size(); i++) {
					if (radioList.get(i).isSelected()) {
						PlaneAddDialog.planeModel = String.valueOf((char) (i + 65));
						PlaneAddDialog.check = 1;
						dispose();
						return;
					}
				}
				JOptionPane.showMessageDialog(null, "기종을 선택하세요", "선택", JOptionPane.ERROR_MESSAGE);
			}
		});
		JButton cancelBtn = new JButton("취소");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnPnl.add(okBtn);
		btnPnl.add(cancelBtn);
		mainPnl.add(btnPnl);
		
		add(mainPnl);

		setModal(true);
		setLocation(650, 400);
		pack();
		setVisible(true);
	}
}
