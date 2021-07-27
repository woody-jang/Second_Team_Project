import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
	JPanel calPnl;
	JLabel nowMonthLbl;
	JPanel mainPnl;
	int menu = 0;

	public Calendar(int menu, String inputDate) {
		selectedDate = "None";
		today = LocalDate.now();
		if (!inputDate.equals("미정")) {
			selectedDate = inputDate;
			today = LocalDate.of(Integer.parseInt(selectedDate.split("[.]")[0]) + 2000,
					Integer.parseInt(selectedDate.split("[.]")[1]), Integer.parseInt(selectedDate.split("[.]")[2]));
			selectedYear = today.getYear();
			selectedMonth = today.getMonthValue();
			selectedDay = today.getDayOfMonth();
		}
		this.menu = menu;
		mainPnl = new JPanel();
		mainPnl.setLayout(new BoxLayout(mainPnl, BoxLayout.Y_AXIS));

		JPanel titlePnl = new JPanel();
		JButton lastMonthBtn = new JButton("◀");
		lastMonthBtn.setFont(new Font(lastMonthBtn.getFont().getName(), Font.PLAIN, 20));
		lastMonthBtn.setMargin(new Insets(0, 0, 0, 0));
		lastMonthBtn.setPreferredSize(new Dimension(25, 25));
		lastMonthBtn.setBackground(Color.white);
		titlePnl.add(lastMonthBtn);

		nowMonthLbl = new JLabel(" " + today.getYear() + "." + today.getMonthValue() + " ");
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

		calPnl = new JPanel();
		calPnl.setLayout(new BoxLayout(calPnl, BoxLayout.Y_AXIS));

		if (!selectedDate.equals("None")) {
			repaintCalendar();
		} else {
			addWeedDayPnl();
			calPnl.add(addDayLbl());
		}
		mainPnl.add(calPnl);


		JPanel btnPnl = new JPanel();
		JButton okBtn = new JButton("확 인");
		okBtn.setFont(new Font(okBtn.getFont().getName(), Font.PLAIN, 20));
		okBtn.setPreferredSize(new Dimension(90, 30));
		okBtn.setMargin(new Insets(0, 5, 0, 5));
		btnPnl.add(okBtn);

		JButton goTodayBtn = new JButton("오늘으로");
		btnPnl.add(goTodayBtn);
		goTodayBtn.setFont(new Font(goTodayBtn.getFont().getName(), Font.PLAIN, 20));
		goTodayBtn.setPreferredSize(new Dimension(100, 30));
		goTodayBtn.setMargin(new Insets(0, 5, 0, 5));

		JButton cancelBtn = new JButton("취 소");
		cancelBtn.setFont(new Font(cancelBtn.getFont().getName(), Font.PLAIN, 20));
		cancelBtn.setPreferredSize(new Dimension(90, 30));
		cancelBtn.setMargin(new Insets(0, 5, 0, 5));
		btnPnl.add(cancelBtn);
		mainPnl.add(btnPnl);

		goTodayBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				today = LocalDate.now();
				repaintCalendar();
			}
		});

		nowMonthLbl.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int[] chooseDate = new int[2];
				new ChooseYearMonth(chooseDate);
				if (chooseDate[0] != 0) {
					today = LocalDate.of(chooseDate[0], chooseDate[1], 1);
					repaintCalendar();
				}
			}
		});

		lastMonthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				today = LocalDate.of(today.getYear(), today.getMonth(), 1);
				today = today.plusDays(-1);
				repaintCalendar();
			}

		});

		nextMonthBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				today = LocalDate.of(today.getYear(), today.getMonth(), today.getDayOfMonth());
				today = today.plusDays(1);
				repaintCalendar();
			}
		});

		okBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (selectedDate.equals("None")) {
					JOptionPane.showMessageDialog(null, "날짜를 선택하세요", "날짜 선택", JOptionPane.ERROR_MESSAGE);
				} else {
					LocalDate selected = null;
					selected = LocalDate.of(selectedYear, selectedMonth, selectedDay);
					if (selected.getDayOfYear() < LocalDate.now().getDayOfYear()
							|| selected.getYear() < LocalDate.now().getYear()) {
						JOptionPane.showMessageDialog(null, "오늘 이후를 선택하세요", "날짜 선택", JOptionPane.ERROR_MESSAGE);
					} else {
						int choice = JOptionPane.showConfirmDialog(null,
								"선택하신 날짜는 " + selectedDate + "입니다\n확인 버튼을 누르세요", "선택 완료", JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.INFORMATION_MESSAGE);
						if (choice == 0) {
							dispose();
						}
					}
				}
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

		if (menu == 0)
			showGUI();
	}

	private void showGUI() {
		setModal(true);
		pack();
		setLocation(730, 380);
		setVisible(true);
	}

	void repaintCalendar() {
		calPnl.removeAll();
		addWeedDayPnl();
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

	void addWeedDayPnl() {
		JPanel weekLblPnl = new JPanel();
		String[] weekDay = { "일", "월", "화", "수", "목", "금", "토" };
		for (int i = 0; i < 7; i++) {
			JLabel weekDayLbl = new JLabel(weekDay[i]);
			weekDayLbl.setFont(new Font(weekDayLbl.getFont().getName(), Font.PLAIN, 20));
			if (menu == 0)
				weekDayLbl.setPreferredSize(new Dimension(40, 30));
			else if (menu == 1)
				weekDayLbl.setPreferredSize(new Dimension(90, 30));
			weekDayLbl.setHorizontalAlignment(JLabel.CENTER);
			weekDayLbl.setBorder(BorderFactory.createLineBorder(Color.black, 1));
			weekLblPnl.add(weekDayLbl);
		}
		weekLblPnl.getComponent(0).setForeground(new Color(255, 0, 0));
		weekLblPnl.getComponent(6).setForeground(new Color(0, 0, 255));
		calPnl.add(weekLblPnl);
	}

	JPanel addDayLbl() {
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
						if (menu == 0)
							tempLbl.setPreferredSize(new Dimension(40, 30));
						else if (menu == 1)
							tempLbl.setPreferredSize(new Dimension(90, 30));
						temp.add(tempLbl);
					}
				}
				for (int j = startDay; j < i - 1; j++) {
					temp.add(dayBtns.get(j));
				}
				tempPnl.add(temp);
			}

			JButton nowBtn = new JButton(String.valueOf(today.getDayOfMonth()));
			if (menu == 0)
				nowBtn.setPreferredSize(new Dimension(40, 30));
			else if (menu == 1) {
				int count = 0;
				for (int j = 0; j < Main.planes.size(); j++) {
					if (Main.planes.get(j).schedules.size() != 0) {
						for (int k = 0; k < Main.planes.get(j).schedules.size(); k++) {
							String[] date = Main.planes.get(j).schedules.get(k).deptDate.split("[.]");
							int year = Integer.parseInt(date[0]) + 2000;
							int month = Integer.parseInt(date[1]);
							int day = Integer.parseInt(date[2]);
							if (today.getYear() == year && today.getMonthValue() == month
									&& today.getDayOfMonth() == day) {
								count++;
							}
						}
					}
				}
				if (today.getDayOfMonth() < 10)
					nowBtn.setText(" " + today.getDayOfMonth() + " (" + count + "건)");
				else
					nowBtn.setText(today.getDayOfMonth() + " (" + count + "건)");
				nowBtn.setPreferredSize(new Dimension(90, 30));
			}
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
					int idx = dayBtns.indexOf(tempPressedBtn);
					for (int i = 0; i < dayBtns.size(); i++) {
						if (tempPressedBtn.equals(dayBtns.get(i))) {
							StringBuffer sb = new StringBuffer();
							selectedYear = today.getYear();
							selectedMonth = today.getMonthValue();
							selectedDay = i + 1;
							sb.append(selectedYear % 100 + ".");
							sb.append(selectedMonth + ".");
							sb.append(String.valueOf(i + 1));
							selectedDate = sb.toString();
						} else {
							dayBtns.get(i).setBackground(Color.white);
						}
					}
					Main.main.setPlaneCalAction(idx + 1);
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
						if (menu == 0)
							tempLbl.setPreferredSize(new Dimension(40, 30));
						else if (menu == 1)
							tempLbl.setPreferredSize(new Dimension(90, 30));
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

	JPanel getCalendarPnl() {
		selectedDate = "None";
		calPnl.removeAll();
		addWeedDayPnl();
		calPnl.add(addDayLbl());
		((JPanel) mainPnl.getComponent(2)).remove(2);
		((JPanel) mainPnl.getComponent(2)).remove(0);
		repaint();
		revalidate();
		return mainPnl;
	}
}