import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

public class Calendar extends JDialog {
	static String selectedDate = "None";
	List<JButton> dayBtns;
	LocalDate today;
	int selectedYear;
	int selectedMonth;
	int selectedDay;

	public Calendar() {
		today = LocalDate.now();
		JPanel mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		JPanel titlePnl = new JPanel();
		JButton lastMonthBtn = new JButton("◀");
		lastMonthBtn.setFont(new Font(lastMonthBtn.getFont().getName(), Font.PLAIN, 20));
		lastMonthBtn.setMargin(new Insets(0, 0, 0, 0));
		lastMonthBtn.setPreferredSize(new Dimension(25, 25));
		lastMonthBtn.setBackground(Color.white);
		titlePnl.add(lastMonthBtn);

		JLabel nowMonthLbl = new JLabel(" " + today.getYear() + "." + today.getMonthValue() + " ");
		nowMonthLbl.setFont(new Font(nowMonthLbl.getFont().getName(), Font.PLAIN, 20));
		nowMonthLbl.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		titlePnl.add(nowMonthLbl);

		JButton nextMonthBtn = new JButton("▶");
		nextMonthBtn.setFont(new Font(nextMonthBtn.getFont().getName(), Font.PLAIN, 20));
		nextMonthBtn.setMargin(new Insets(0, 0, 0, 0));
		nextMonthBtn.setPreferredSize(new Dimension(25, 25));
		nextMonthBtn.setBackground(Color.white);
		titlePnl.add(nextMonthBtn);
		mainPnl.add(titlePnl);

		JPanel calPnl = new JPanel();
		calPnl.setLayout(new BoxLayout(calPnl, BoxLayout.Y_AXIS));

		addWeedDayPnl(calPnl);

		calPnl.add(addDayLbl());
		mainPnl.add(calPnl);

		JPanel cancelBtnPnl = new JPanel();
		JButton cancelBtn = new JButton("취소");
		cancelBtn.setFont(new Font(cancelBtn.getFont().getName(), Font.PLAIN, 20));
		cancelBtn.setMargin(new Insets(0, 5, 0, 5));
		cancelBtnPnl.add(cancelBtn);
		mainPnl.add(cancelBtnPnl);

		lastMonthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				today = LocalDate.of(today.getYear(), today.getMonth(), 1);
				today = today.plusDays(-1);
				calPnl.removeAll();
				addWeedDayPnl(calPnl);
				calPnl.add(addDayLbl());
				if (selectedDate.length() > 5) {
					if (selectedYear == today.getYear()) {
						if (selectedMonth == today.getMonthValue()) {
							dayBtns.get(selectedDay - 1).setBackground(new Color(101, 255, 94));
						}
					}
				}
				nowMonthLbl.setText(" " + today.getYear() + "." + today.getMonthValue() + " ");
				repaint();
				revalidate();
				pack();
			}
		});

		nextMonthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				today = LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth());
				today = today.plusDays(1);
				calPnl.removeAll();
				addWeedDayPnl(calPnl);
				calPnl.add(addDayLbl());
				if (selectedDate.length() > 5) {
					if (selectedYear == today.getYear()) {
						if (selectedMonth == today.getMonthValue()) {
							dayBtns.get(selectedDay - 1).setBackground(new Color(101, 255, 94));
						}
					}
				}
				nowMonthLbl.setText(" " + today.getYear() + "." + today.getMonthValue() + " ");
				repaint();
				revalidate();
				pack();
			}
		});
		
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedDate = "None";
				dispose();
			}
		});

		add(mainPnl);

		setModal(true);
		pack();
		setLocation(400, 380);
		setVisible(true);
	}

	private void addWeedDayPnl(JPanel calPnl) {
		JPanel weekLblPnl = new JPanel();
		String[] weekDay = { "일", "월", "화", "수", "목", "금", "토" };
		for (int i = 0; i < 7; i++) {
			JLabel weekDayLbl = new JLabel(weekDay[i]);
			weekDayLbl.setFont(new Font(weekDayLbl.getFont().getName(), Font.PLAIN, 20));
			weekDayLbl.setPreferredSize(new Dimension(40, 30));
			weekDayLbl.setHorizontalAlignment(JLabel.CENTER);
			weekDayLbl.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			weekLblPnl.add(weekDayLbl);
		}
		weekLblPnl.getComponent(0).setForeground(new Color(255, 0, 0));
		weekLblPnl.getComponent(6).setForeground(new Color(0, 0, 255));
		calPnl.add(weekLblPnl);
	}

	private JPanel addDayLbl() {
		dayBtns = new ArrayList<JButton>();
		today = LocalDate.of(today.getYear(), today.getMonth(), 1);
		JPanel tempPnl = new JPanel();
		int startOfMonth = today.getDayOfWeek().getValue();
		tempPnl.setLayout(new BoxLayout(tempPnl, BoxLayout.Y_AXIS));

		for (int i = 1; i <= today.lengthOfMonth(); i++) {
			int todayWeek = today.getDayOfWeek().getValue();
			if (i != 1 && todayWeek == 7) {
				JPanel temp = new JPanel();
				int startDay = (i - 7 > 0) ? i - 7 - 1 : 0;
				if (startDay == 0 && startOfMonth != 7) {
					for (int j = 0; j < startOfMonth; j++) {
						JLabel tempLbl = new JLabel(" ");
						tempLbl.setPreferredSize(new Dimension(40, 30));
						temp.add(tempLbl);
					}
				}
				for (int j = startDay; j < i - 1; j++) {
					temp.add(dayBtns.get(j));
				}
				tempPnl.add(temp);
			}

			JButton nowBtn = new JButton(String.valueOf(today.getDayOfMonth()));
			nowBtn.setPreferredSize(new Dimension(40, 30));
			nowBtn.setFont(new Font(nowBtn.getFont().getName(), Font.PLAIN, 20));
			nowBtn.setBackground(Color.white);
			nowBtn.setMargin(new Insets(0, 0, 0, 0));
			if (todayWeek == 7) {
				nowBtn.setForeground(new Color(255, 0, 0));
			} else if (todayWeek == 6) {
				nowBtn.setForeground(new Color(0, 0, 255));
			}
			dayBtns.add(nowBtn);

			nowBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JButton tempPressedBtn = (JButton) e.getSource();
					tempPressedBtn.setBackground(new Color(101, 255, 94));
					for (int i = 0; i < dayBtns.size(); i++) {
						if (tempPressedBtn.equals(dayBtns.get(i))) {
							StringBuffer sb = new StringBuffer();
							selectedYear = today.getYear();
							selectedMonth = today.getMonthValue();
							selectedDay = i + 1;
							sb.append(today.getYear() + ".");
							sb.append(today.getMonthValue() + ".");
							sb.append(String.valueOf(i + 1));
							selectedDate = sb.toString();
						} else {
							dayBtns.get(i).setBackground(Color.white);
						}
					}
				}
			});

			if (i == today.lengthOfMonth()) {
				JPanel temp = new JPanel();
				if (todayWeek == 7) {
					todayWeek = 0;
				}
				int lastTemp = i - todayWeek - 1;
				for (int j = lastTemp; j < dayBtns.size(); j++) {
					temp.add(dayBtns.get(j));
				}
				if (todayWeek != 6) {
					for (int j = 0; j < 6 - todayWeek; j++) {
						JLabel tempLbl = new JLabel(" ");
						tempLbl.setPreferredSize(new Dimension(40, 30));
						temp.add(tempLbl);
					}
				}
				tempPnl.add(temp);
				break;
			}
			today = today.plusDays(1);

		}
		return tempPnl;
	}
}