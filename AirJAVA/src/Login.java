import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.text.ParseException;
import java.util.List;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

// 회원가입은 카드레이아웃으로 바꾸기
public class Login extends JDialog {
	private Boolean nameCheck = false;
	private Boolean birthCheck = false;
	private Boolean cellNumCheck = false;
	private Boolean uidCheck = false;
	private Boolean upwdCheck = false;
	private Boolean upwdDoubleCheck = false;

	public Login() {
		CardLayout card = new CardLayout();
		JPanel cardPnl = new JPanel(card);

		URL logoLblImage = Login.class.getClassLoader().getResource("logo.png");
		ImageIcon logoLblIcon = new ImageIcon(logoLblImage);
		
		// 회원가입용 패널
		JPanel signinPnl = new JPanel();
		signinPnl.setLayout(new BoxLayout(signinPnl, BoxLayout.Y_AXIS));
		
		JPanel imagePnl = new JPanel();
		JLabel imageLbl = new JLabel(logoLblIcon);
		imagePnl.add(imageLbl);
		signinPnl.add(imagePnl);
		
		// 이름 입력용 패널
		JPanel inputNamePnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputNameLbl = new JLabel("이 름");
		inputNameLbl.setPreferredSize(new Dimension(95,12));
		inputNameLbl.setHorizontalAlignment(JLabel.CENTER);
		JTextField inputNameTf = new JTextField(18);
		inputNameTf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				boolean checkName = true;
				String tempName = inputNameTf.getText();
				if (!tempName.trim().equals("")) {
					for (int i = 0; i < tempName.length(); i++) {
						char tempChar = tempName.charAt(i);
						if (!Character.isLetterOrDigit(tempChar)) {
							checkName = false;
							break;
						}
					}
					if (checkName)
						nameCheck = true;
					else
						nameCheck = false;
				}
				else
					nameCheck = false;
			}
		});
		inputNamePnl.add(inputNameLbl);
		inputNamePnl.add(inputNameTf);
		signinPnl.add(inputNamePnl);
		
		// 생일 입력용 패널
		JPanel inputBirthPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputBirthLbl = new JLabel("생년월일");
		inputBirthLbl.setPreferredSize(new Dimension(95,12));
		inputBirthLbl.setHorizontalAlignment(JLabel.CENTER);
		JTextField inputBirthTf = new JTextField(18);
		inputBirthTf.setText("주민번호 앞자리");
		inputBirthTf.addFocusListener(new FocusListener() {
			@Override // 텍스트필드에 안내 문구 적는 메소드
			public void focusLost(FocusEvent e) {
				if (inputBirthTf.getText().trim().equals(""))
					inputBirthTf.setText("주민번호 앞자리");
				else if (!inputBirthTf.getText().equals("주민번호 앞자리")) {
					int tempBirth = Integer.parseInt(inputBirthTf.getText());
					int year = tempBirth / 10000;
					int month = tempBirth / 100 % 100;
					int day = tempBirth % 100;
					if (month < 13 && month > 0 && day > 0 && day < 32)
						birthCheck = true;
					else
						birthCheck = false;
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (inputBirthTf.getText().equals("주민번호 앞자리"))
					inputBirthTf.setText("");
			}
		});
		inputBirthTf.addKeyListener(new KeyAdapter() {
			@Override // 생년월일을 잘못입력할 경우 경고창
			public void keyTyped(KeyEvent e) {
				String tempStr = inputBirthTf.getText();
				if (tempStr.length() >= 6) {
					JOptionPane.showMessageDialog(null, "주민번호 앞자리를 입력하세요",
							"생년월일 입력", JOptionPane.ERROR_MESSAGE);
					e.consume();
					inputBirthTf.setText("");
					return;
				}
				char temp = e.getKeyChar();
				if (!(Character.isDigit(temp) || temp == 127 || temp == 8)) {
					JOptionPane.showMessageDialog(null, "숫자만 입력하세요",
							"아이디 입력", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});
		inputBirthPnl.add(inputBirthLbl);
		inputBirthPnl.add(inputBirthTf);
		signinPnl.add(inputBirthPnl);
		
		JPanel inputCellNumPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputCellNumLbl = new JLabel("전화번호");
		inputCellNumLbl.setPreferredSize(new Dimension(95,12));
		inputCellNumLbl.setHorizontalAlignment(JLabel.CENTER);
		MaskFormatter cellNumFormat = null;
		try {
			cellNumFormat = new MaskFormatter("010-####-####");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		JFormattedTextField inputCellNumTf = new JFormattedTextField(cellNumFormat);
		inputCellNumTf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String tempCellNum = inputCellNumTf.getText();
				if (Character.isDigit(tempCellNum.charAt(12)))
					cellNumCheck = true;
				else
					cellNumCheck = false;
			}
		});
		inputCellNumTf.setColumns(18);
		inputCellNumPnl.add(inputCellNumLbl);
		inputCellNumPnl.add(inputCellNumTf);
		signinPnl.add(inputCellNumPnl);
		
		JPanel inputUidPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputUidLbl = new JLabel("아이디");
		inputUidLbl.setPreferredSize(new Dimension(95,12));
		inputUidLbl.setHorizontalAlignment(JLabel.CENTER);
		JTextField inputUidTf = new JTextField(18);
		inputUidTf.setText("소문자, 숫자, 4자이상, 10자이하");
		inputUidTf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char temp = e.getKeyChar();
				if (!(Character.isLowerCase(temp) || Character.isDigit(temp) || temp == 127 || temp == 8)) {
					JOptionPane.showMessageDialog(null, "소문자와 숫자만 입력하세요",
							"아이디 입력", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
				String tempStr = inputUidTf.getText();
				if (tempStr.length() >= 10) {
					JOptionPane.showMessageDialog(null, "10글자 이하로 입력하세요",
							"아이디 입력", JOptionPane.ERROR_MESSAGE);
					e.consume();
				}
			}
		});
		inputUidTf.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (inputUidTf.getText().trim().equals(""))
					inputUidTf.setText("소문자, 숫자, 4자이상, 10자이하");
				else if (!inputUidTf.getText().equals("소문자, 숫자, 4자이상, 10자이하")) {
					String tempUid = inputUidTf.getText();
					if (tempUid.length() < 4)
						JOptionPane.showMessageDialog(null, "4자 이상 입력하세요", "아이디 입력", JOptionPane.ERROR_MESSAGE);
				}
				uidCheck = false;
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (inputUidTf.getText().equals("소문자, 숫자, 4자이상, 10자이하"))
					inputUidTf.setText("");
			}
		});
		JButton uidCheckBtn = new JButton("중복확인");
		uidCheckBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String tempUid = inputUidTf.getText();
				if (tempUid.equals("소문자, 숫자, 4자이상, 10자이하"))
					JOptionPane.showMessageDialog(null, "아이디를 입력하세요", "아이디 입력", JOptionPane.ERROR_MESSAGE);
				boolean checkUid = true;
				if (CustomerIO.CUSTOMER_LIST.exists()) {
					List<Customer> temp = Main.customers;
					for (int i = 0; i < temp.size(); i++) {
						if (temp.get(i).getUid().equals(tempUid)) {
							inputUidTf.setText("소문자, 숫자, 4자이상, 10자이하");
							JOptionPane.showMessageDialog(null, "아이디가 중복되었습니다", "아이디 중복", JOptionPane.ERROR_MESSAGE);
							checkUid = false;
							break;
						}
					}
				}
				if (checkUid) {
					JOptionPane.showMessageDialog(null, "사용가능한 ID 입니다");
					uidCheck = true;
				}
				else
					uidCheck = false;
			}
		});
		uidCheckBtn.setMargin(new Insets(0, 0, 0, 0));
		inputUidPnl.add(inputUidLbl);
		inputUidPnl.add(inputUidTf);
		inputUidPnl.add(uidCheckBtn);
		signinPnl.add(inputUidPnl);
		
		JPanel inputWarning1Pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputWarning1Lbl1 = new JLabel(" ");
		inputWarning1Lbl1.setPreferredSize(new Dimension(95,12));
		inputWarning1Lbl1.setHorizontalAlignment(JLabel.CENTER);
		JLabel inputWarning1Lbl2 = new JLabel("비밀번호 입력이 잘못되었습니다");
		inputWarning1Lbl2.setForeground(Color.red);
		inputWarning1Pnl.add(inputWarning1Lbl1);
		inputWarning1Pnl.add(inputWarning1Lbl2);
		
		JPanel inputWarning2Pnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputWarning2Lbl1 = new JLabel(" ");
		inputWarning2Lbl1.setPreferredSize(new Dimension(95,12));
		inputWarning2Lbl1.setHorizontalAlignment(JLabel.CENTER);
		JLabel inputWarning2Lbl2 = new JLabel("비밀번호가 같지 않습니다");
		inputWarning2Lbl2.setForeground(Color.red);
		inputWarning2Pnl.add(inputWarning2Lbl1);
		inputWarning2Pnl.add(inputWarning2Lbl2);
		
		JPanel inputUpwdPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputUpwdLbl = new JLabel("비밀번호");
		inputUpwdLbl.setPreferredSize(new Dimension(95,12));
		inputUpwdLbl.setHorizontalAlignment(JLabel.CENTER);
		JPasswordField inputUpwdPf = new JPasswordField(18);
		inputUpwdPf.setText("특수문자 ! @ # ^ 사용가능, 10자 이하");
		inputUpwdPf.setEchoChar((char) 0);
		
		JPanel inputUpwdChkPnl = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel inputUpwdChkLbl = new JLabel("비밀번호 확인");
		inputUpwdChkLbl.setPreferredSize(new Dimension(95,12));
		inputUpwdChkLbl.setHorizontalAlignment(JLabel.CENTER);
		JPasswordField inputUpwdChkPf = new JPasswordField(18);
		inputUpwdChkPf.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String originPwd = new String(inputUpwdPf.getPassword());
				String checkPwd = new String(inputUpwdChkPf.getPassword());
				if (originPwd.equals(checkPwd)) {
					inputWarning2Lbl2.setText("비밀번호가 같습니다.");
					inputWarning2Lbl2.setForeground(new Color(47, 157, 39));
					upwdDoubleCheck = true;
				}
				else
					upwdDoubleCheck = false;
			}
		});
		
		inputUpwdPf.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (new String(inputUpwdPf.getPassword()).equals(new String(inputUpwdChkPf.getPassword())) && !new String(inputUpwdPf.getPassword()).equals("")) {
					inputWarning2Lbl2.setText("비밀번호가 같습니다.");
					inputWarning2Lbl2.setForeground(new Color(47, 157, 39));
				}
				else {
					inputWarning2Lbl2.setText("비밀번호가 같지 않습니다");
					inputWarning2Lbl2.setForeground(Color.red);
				}
				if (new String(inputUpwdPf.getPassword()).trim().equals("")) {
					inputUpwdPf.setText("특수문자 ! @ # ^ 사용가능, 10자 이하");
					inputUpwdPf.setEchoChar((char) 0);
				}
				else {
					String tempPwd = new String(inputUpwdPf.getPassword());
					boolean checkPwd1 = true;
					boolean checkPwd2 = false;
					boolean checkPwd3 = true;
					for (int i = 0; i < tempPwd.length(); i++) {
						char temp = tempPwd.charAt(i);
						if (!(Character.isLetterOrDigit(temp) || temp == '!' || temp == '@' || temp == '#' || temp == '^')) {
							checkPwd1 = false;
						}
						if (temp == '!' || temp == '@' || temp == '#' || temp == '^') {
							checkPwd2 = true;
						}
						if (i > 0 && temp == tempPwd.charAt(i - 1)) {
							checkPwd3 = false;
						}
					}
					if (checkPwd1 && checkPwd2 && checkPwd3 && tempPwd.length() >= 5 && tempPwd.length() < 11) {
						inputWarning1Lbl2.setText("강력한 암호입니다");
						inputWarning1Lbl2.setForeground(new Color(47, 157, 39));
						upwdCheck = true;
					}
					else
						upwdCheck = false;
				}
			}
			@Override
			public void focusGained(FocusEvent e) {
				if (new String(inputUpwdPf.getPassword()).equals("특수문자 ! @ # ^ 사용가능, 10자 이하")) {
					inputUpwdPf.setText("");
					inputUpwdPf.setEchoChar((char) UIManager.get("PasswordField.echoChar"));
				}
			}
		});
		inputUpwdPf.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				inputWarning2Lbl2.setText("비밀번호가 같지 않습니다");
				inputWarning2Lbl2.setForeground(Color.red);
				
				String tempPwd = new String(inputUpwdPf.getPassword());
				boolean checkPwd1 = true;
				boolean checkPwd2 = false;
				boolean checkPwd3 = true;
				for (int i = 0; i < tempPwd.length(); i++) {
					char temp = tempPwd.charAt(i);
					if (!(Character.isLetterOrDigit(temp) || temp == '!' || temp == '@' || temp == '#' || temp == '^')) {
						checkPwd1 = false;
					}
					if (temp == '!' || temp == '@' || temp == '#' || temp == '^') {
						checkPwd2 = true;
					}
					if (i > 0 && temp == tempPwd.charAt(i - 1)) {
						checkPwd3 = false;
					}
				}
				if (checkPwd1 && checkPwd2 && checkPwd3 && tempPwd.length() >= 5 && tempPwd.length() < 11) {
					inputWarning1Lbl2.setText("강력한 암호입니다");
					inputWarning1Lbl2.setForeground(new Color(47, 157, 39));
				}
				else if (checkPwd1 && tempPwd.length() < 5) {
					inputWarning1Lbl2.setText("비밀번호는 5자 이상으로 해주세요");
					inputWarning1Lbl2.setForeground(Color.red);
				}
				else if (checkPwd1 && tempPwd.length() >= 10) {
					inputWarning1Lbl2.setText("비밀번호는 10자 이하로 해주세요");
					inputWarning1Lbl2.setForeground(Color.red);
					e.consume();
				}
				else if (!checkPwd2) {
					inputWarning1Lbl2.setText("! @ # ^ 특수문자를 하나이상 넣으세요");
					inputWarning1Lbl2.setForeground(Color.red);
				}
				else if (!checkPwd3) {
					inputWarning1Lbl2.setText("연속된 문자를 사용하지 마세요");
					inputWarning1Lbl2.setForeground(Color.red);
				}
			}
		});
		inputUpwdPnl.add(inputUpwdLbl);
		inputUpwdPnl.add(inputUpwdPf);
		signinPnl.add(inputUpwdPnl);
		signinPnl.add(inputWarning1Pnl);
		
		inputUpwdChkPnl.add(inputUpwdChkLbl);
		inputUpwdChkPnl.add(inputUpwdChkPf);
		signinPnl.add(inputUpwdChkPnl);
		signinPnl.add(inputWarning2Pnl);
		
		JPanel returnBtnPnl = new JPanel();
		JButton returnBtn = new JButton("가입하기");
		returnBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (nameCheck && birthCheck && cellNumCheck && uidCheck && upwdCheck && upwdDoubleCheck) {
					Customer temp = new Customer(inputNameTf.getText(), inputBirthTf.getText(),
							inputCellNumTf.getText(), inputUidTf.getText(), inputUpwdPf.getPassword());
					temp.setPassword();
					JOptionPane.showMessageDialog(null, inputNameTf.getText() + " 님 가입을 환영합니다!\n1000 java가 적립되었습니다");
					Main.customers.add(temp);
					CustomerIO.save(Main.customers);
					
					inputNameTf.setText("");
					inputBirthTf.setText("주민번호 앞자리");
					inputCellNumTf.setText("");
					inputUidTf.setText("소문자, 숫자, 4자이상, 10자이하");
					inputUpwdPf.setText("특수문자 ! @ # ^ 사용가능, 10자 이하");
					inputUpwdPf.setEchoChar((char) 0);
					inputUpwdChkPf.setText("");
					inputWarning1Lbl2.setText("비밀번호 입력이 잘못되었습니다");
					inputWarning1Lbl2.setForeground(Color.red);
					inputWarning2Lbl2.setText("비밀번호가 같지 않습니다");
					inputWarning2Lbl2.setForeground(Color.red);
					
					
					card.show(cardPnl, "로그인");
					setSize(280, 250);
				}
				else if (!nameCheck) {
					JOptionPane.showMessageDialog(null, "이름을 다시 확인하세요", "이름 입력", JOptionPane.ERROR_MESSAGE);
				}
				else if (!birthCheck) {
					JOptionPane.showMessageDialog(null, "생일을 다시 확인하세요", "생일 입력", JOptionPane.ERROR_MESSAGE);
				}
				else if (!cellNumCheck) {
					JOptionPane.showMessageDialog(null, "휴대폰 번호를 다시 확인하세요", "휴대폰 번호 입력", JOptionPane.ERROR_MESSAGE);
				}
				else if (!uidCheck) {
					JOptionPane.showMessageDialog(null, "아이디 중복확인을 하세요", "아이디 입력", JOptionPane.ERROR_MESSAGE);
				}
				else if (!upwdCheck) {
					JOptionPane.showMessageDialog(null, "비밀번호를 다시 확인하세요", "비밀번호 입력", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton cancelBtn = new JButton("취소");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(cardPnl, "로그인");
				setSize(280, 250);
			}
		});
		returnBtnPnl.add(returnBtn);
		returnBtnPnl.add(cancelBtn);
		signinPnl.add(returnBtnPnl);

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------------위쪽은 가입-------------------아래쪽은 로그인----------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
		
		JPanel loginPnl = new JPanel();
		loginPnl.setLayout(new BoxLayout(loginPnl, BoxLayout.Y_AXIS));

		JPanel logoPnl = new JPanel();
		JLabel logoLbl = new JLabel(logoLblIcon);
		logoPnl.add(logoLbl);
		loginPnl.add(logoPnl);

		JPanel uidPnl = new JPanel();
		JLabel uidLbl = new JLabel("아이디");
		uidLbl.setPreferredSize(new Dimension(65,12));
		uidLbl.setHorizontalAlignment(JLabel.CENTER);
		JTextField uidTf = new JTextField(8);
		uidPnl.add(uidLbl);
		uidPnl.add(uidTf);
		loginPnl.add(uidPnl);

		JPanel upwdPnl = new JPanel();
		JLabel upwdLbl = new JLabel("비밀번호");
		upwdLbl.setPreferredSize(new Dimension(65,12));
		upwdLbl.setHorizontalAlignment(JLabel.CENTER);
		JPasswordField upwdPf = new JPasswordField(8);
		upwdPnl.add(upwdLbl);
		upwdPnl.add(upwdPf);
		loginPnl.add(upwdPnl);

		JPanel btnPnl = new JPanel();
		JButton loginBtn = new JButton("로그인");
		loginBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String loginId = uidTf.getText();
				String loginPwd = new String(upwdPf.getPassword());
				boolean checkId = false;
				boolean checkPwd = false;
				Customer tempCustomer = null;
				for (int i = 0; i < Main.customers.size(); i++) {
					if (loginId.equals(Main.customers.get(i).getUid())) {
						checkId = true;
						Main.customers.get(i).getPassword();
						if (loginPwd.equals(new String(Main.customers.get(i).getUpwd()))) {
							checkPwd = true;
						}
						Main.customers.get(i).setPassword();
						tempCustomer = Main.customers.get(i);
						break;
					}
				}
				URL logoLblImage = Login.class.getClassLoader().getResource("star.png");
				ImageIcon logoLblIcon = new ImageIcon(logoLblImage);
				if (checkId && checkPwd) {
					JOptionPane.showMessageDialog(null, tempCustomer.getName() + " 님 어서오세요\nWelcome " + tempCustomer.getName() + " :D", "Welcome :D", JOptionPane.INFORMATION_MESSAGE,logoLblIcon);
					if (loginId.equals("admin"))
						Main.checkAdmin = 1;
					dispose();
				}
				else if(checkId) {
					JOptionPane.showMessageDialog(null, "비밀번호를 확인하세요", "비밀번호 에러", JOptionPane.ERROR_MESSAGE);
				}
				else {
					JOptionPane.showMessageDialog(null, "아이디를 확인하세요", "아이디 에러", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		JButton signinBtn = new JButton("회원가입");
		signinBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				card.show(cardPnl, "회원가입");
				pack();
			}
		});
		btnPnl.add(loginBtn);
		btnPnl.add(signinBtn);
		loginPnl.add(btnPnl);

		cardPnl.add(loginPnl, "로그인");
		cardPnl.add(signinPnl, "회원가입");

		add(cardPnl);

		setTitle("Hello World :-)");
		setModal(true);
		setLocation(700, 400);
		setSize(280, 250);
		setVisible(true);
	}
}
