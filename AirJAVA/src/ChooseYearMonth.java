import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ChooseYearMonth extends JDialog {
	JComboBox<Integer> yearComb;
	JComboBox<Integer> monthComb;
	int[] yearMonth;

	public ChooseYearMonth(int[] yearMonth) {
		this.yearMonth = yearMonth;
		Integer[] years = new Integer[51];
		for (int i = 0; i < 51; i++) {
			years[i] = i + 1980;
		}
		Integer[] months = new Integer[12];
		for (int i = 0; i < 12; i++) {
			months[i] = i + 1;
		}
		
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));
		
		JPanel yearPnl = new JPanel();
		JLabel yearLbl = new JLabel("년도 선택");
		yearLbl.setFont(new Font(yearLbl.getFont().getName(), Font.PLAIN, 15));
		yearLbl.setPreferredSize(new Dimension(70, 20));
		yearLbl.setHorizontalAlignment(JLabel.CENTER);
		yearComb = new JComboBox<Integer>(years);
		yearPnl.add(yearLbl);
		yearPnl.add(yearComb);
		mainPnl.add(yearPnl);
		
		JPanel monthPnl = new JPanel();
		JLabel monthLbl = new JLabel("년도 선택");
		monthLbl.setFont(new Font(monthLbl.getFont().getName(), Font.PLAIN, 15));
		monthLbl.setPreferredSize(new Dimension(70, 20));
		monthLbl.setHorizontalAlignment(JLabel.CENTER);
		monthComb = new JComboBox<Integer>(months);
		monthPnl.add(monthLbl);
		monthPnl.add(monthComb);
		mainPnl.add(monthPnl);
		
		JPanel btnPnl = new JPanel();
		JButton okBtn = new JButton("확인");
		btnPnl.add(okBtn);
		JButton cancelBtn = new JButton("취소");
		btnPnl.add(cancelBtn);
		mainPnl.add(btnPnl);
		
		yearComb.addActionListener(new CombActionListener());
		monthComb.addActionListener(new CombActionListener());
		
		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (yearMonth[0] == 0 || yearMonth[1] == 0) {
					JOptionPane.showMessageDialog(null, "선택해주세요", "선택", JOptionPane.ERROR_MESSAGE);
				} else {
					dispose();
				}
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				yearMonth[0] = 0;
				yearMonth[1] = 0;
				dispose();
			}
		});
		
		add(mainPnl);
		
		setModal(true);
		pack();
		setLocation(800, 400);
		setVisible(true);
	}
	
	class CombActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox<Integer> selectedComb = (JComboBox<Integer>) e.getSource();
			if (selectedComb.equals(yearComb)) {
				yearMonth[0] = (int) selectedComb.getSelectedItem();
			} else {
				yearMonth[1] = (int) selectedComb.getSelectedItem();
			}
		}
	}
}
