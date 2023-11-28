package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import model.Register;
import service.RegistService;
import utils.Tools;

/**
 * 	注册页面
 * 		管理员注册时，加入了密钥
 * 		注册条件：姓名长度>2    账号密码长度>5  ，账号密码在后端逻辑上暂且无法过滤中文
 * 		注册类别：分为普通用户和管理员
 * @author Administrator
 *
 */
public class RegisterDialog {
	private RegistService registService = new RegistService();
	private Register register = null;
	private JDialog registerView;
	private Container c;
	private JFrame jf;
	
	//字段文本
	private JLabel registerName = new JLabel("Account:");
	private JLabel registerPassword = new JLabel("Password:");
	private JLabel registerPassword2 = new JLabel("Repeat Password:");
	private JLabel registerAdminCode = new JLabel("Administrator Key:"); //注册为管理员时，需要输入密钥！
	private JLabel adminCodeNotice = new JLabel("Note: The admin key is only valid when registering as an administrator.");
	private JLabel registMold = new JLabel("Registration Type:");

	//字段输入框
	private JTextField nameField = new JTextField();
	private JPasswordField passwordField = new JPasswordField();
	private JPasswordField passwordField2 = new JPasswordField();
	private JTextField adminCodeField = new JTextField();
	private JComboBox<String> registMoldField = new JComboBox<String>();
	private String[] registMoldList = {"Student", "Administrator"};
	private ComboBoxModel<String> model = new DefaultComboBoxModel<String>(registMoldList);
	
	//按钮
	private JButton registBtn = new JButton("Register");
	private JButton resetBtn = new JButton("Reset");
	
	public RegisterDialog(JFrame jf, String title) {
		this.jf = jf;
		init(title);
		registerView.setVisible(true);
	}
	
	//初始化窗口
	private void init(String title) {
		//窗口
		registerView = new JDialog(jf, title, true);
		c = registerView.getContentPane();
		c.setLayout(null);
		registerView.setSize(600, 400);
		registerView.setLocationRelativeTo(null);
		
		//组件
		registerName.setBounds(120, 52, 130, 30);
		registerPassword.setBounds(120,80, 130, 30);
		registerPassword2.setBounds(120, 110, 130, 30);
		registerAdminCode.setBounds(120, 140, 130, 30);//密钥
		adminCodeNotice.setBounds(120, 160, 500, 30);//"注意"
		adminCodeNotice.setForeground(Color.RED);;//"注意"字体
		registMold.setBounds(120, 190, 130, 30); //注册类型
		
		nameField.setBounds(300, 52, 200, 25);
		passwordField.setBounds(300, 82, 200, 25);
		passwordField2.setBounds(300, 112, 200, 25);
		adminCodeField.setBounds(300, 142, 200, 25); //密钥字段
		registMoldField.setBounds(300, 195, 200, 25);
		
		registBtn.setBounds(180, 230, 100, 25);
		resetBtn.setBounds(360, 230, 100, 25);
		
		registMoldField.setModel(model);
		
		c.add(registerName);
		c.add(registerPassword);
		c.add(registerPassword2);
		c.add(registerAdminCode);
		c.add(registMold);
		c.add(adminCodeNotice);
		c.add(nameField);
		c.add(passwordField);
		c.add(passwordField2);
		c.add(adminCodeField);
		c.add(registMoldField);
		c.add(registBtn);
		c.add(resetBtn);
		
		//监听器
		createListener();
	}

	/**
	 * - 监听器
	 * - 还未解决问题：
	 * 	2.注册信息合法问题，中文如何排除
	 */
	
	private void createListener() {
		//注册按钮
		registBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Start registration");
				String msg = "";
				//判断两次密码输入是否一致
				if (Arrays.equals(passwordField.getPassword(),passwordField2.getPassword())) {
					//一致
					String str = new String(passwordField.getPassword());
					register = new Register(nameField.getText(),str,
							adminCodeField.getText(),registMoldField.getSelectedItem().toString());
					
					msg = registService.regist(register);
				}else {
					//不一致
					msg = "The two password entries are not the same!";//0.0
					passwordField.setText(""); //清空密码
					passwordField2.setText("");
				}
				Tools.createMsgDialog(jf, msg);
			}
		});
		
		//重置按钮
		resetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				nameField.setText("");
				passwordField.setText("");
				passwordField2.setText("");
				registMoldField.setSelectedIndex(0); //设置为普通用户注册
			}
		});
	}
}
