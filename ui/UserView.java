package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Book;
import model.User;
import service.UserService;
import utils.Tools;

/**
 * 普通用户界面
 * 宽：800
 * 高：500
 * @author Administrator
 *
 */
public class UserView{
	private JFrame jf;
	User user = null;
	UserService userService = new UserService();
	private JFrame userView = new JFrame("Library Management System");
	private Container c = userView.getContentPane();
	private JPanel topPanel = new JPanel(); //顶部面板
	private JLabel userMsg = new JLabel(); //用来显示用户姓名
	private JLabel time = new JLabel(); //用来显示登录时间
	private JLabel title = new JLabel("Book Information"); //表格标题
	private JScrollPane mainPanel = new JScrollPane(); //图书信息展览面板
	private JTable table = new JTable();
	private JComboBox<String> findBy = new JComboBox<>(); //检索方式下拉列表
	private String items[] = {"Search by Book Name", "Search by Author", "Search by Book Number", "Search All Books"};
	private JTextField input = new JTextField(); //检索输入框
	private JButton submit = new JButton("Search");
	private JButton borrowBooks = new JButton("Borrow"); // new
	private JButton returnBooks = new JButton("Return"); // new
	
	private List<Book> books = null; //检索到的图书列表
	String[] tableTitle = new String[]{"Book Name", "Book Author", "Book Number","Borrowing Info","Quantity of Books","Location"};//表格头字段
	private String[][] tableData = new String[1][6];//表格内容
	
	
	public UserView(User loginUser) {
		this.user = loginUser;
		init();
		userView.setVisible(true);
	}
	
	/*
	 *  初始化界面
	 */
	private void init() {
		c.setLayout(null);
		userView.setSize(800,500);//先size在location，否则会乱
		userView.setLocationRelativeTo(null);
		userView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		userView.setResizable(false);
		
		topPanel();//加载顶部面板
		mainPanel();//加载图书目录列表的面板
		
		createListener();//创建监听器，监听检索事件...
	}
	
	/**
	 *  -顶部面板
	 */
	private void topPanel() {
		topPanel.setLayout(null);
		topPanel.setBounds(0,0,800,100);
		topPanel.setBackground(Color.GRAY);
		
		
		//显示登录用户的名字
		userMsg.setBounds(10,5,200,20);
		userMsg.setFont(new Font("Arial",Font.PLAIN,15));
		userMsg.setText("Welcome: "+user.getUserName());
		
		//检索方式按钮
		
		ComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
		findBy.setModel(model);
		findBy.setFont(new Font("Verdana", Font.PLAIN, 9));
		findBy.setBounds(50,40,160,25);
		
		//检索输入框
		input.setBounds(230, 40, 280, 25);
		
		//检索按钮
		submit.setBounds(550, 40, 80, 25);
		borrowBooks.setBounds(630,40,80,25);
		returnBooks.setBounds(710,40, 80,25);

		
		//当前时间
		time.setBounds(600, 70, 200, 25);
		time.setFont(new Font("Times New Roman", Font.ITALIC, 11));
		time.setText("Login Time: "+Tools.getTime());
		
		topPanel.add(userMsg);
		topPanel.add(findBy);
		topPanel.add(input);
		topPanel.add(submit);
		topPanel.add(borrowBooks);
		topPanel.add(returnBooks);
		topPanel.add(time);
		c.add(topPanel);
		topPanel.setVisible(true);
	}
	
	/**
	 * -图书信息展览面板
	 */
	private void mainPanel() {
		/**
		 * - 存在问题：表格内容如何自动换行？
		 */
//		mainPanel.setLayout(null);
		
		//表格的设置
		table.getTableHeader().setReorderingAllowed(false);	//列不能移动
		table.getTableHeader().setResizingAllowed(false); 	//不可拉动表格
		table.setEnabled(false);							//不可更改数据
		table.setRowHeight(20);
		mainPanel.setBounds(45, 140, 700, 300);
		mainPanel.setViewportView(table);	//视口装入表格
		
		//标题
		title.setBounds(350, 110, 120, 25);
		
		c.add(mainPanel);
		c.add(title);
		mainPanel.setVisible(true);
	}
	
	
	
	/**
	 * - 给按钮添加监听事件
	 */
	private void createListener() {
		//检索按钮
		//这段代码，只有我和上帝知道是什么意思
		//再过几个月，只有上帝能看懂
		submit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				Tools.cleanTwoArray(tableData); //首先清空表格内容
				String findMsg = ""; //用来获得检索书目使输入的内容
				findMsg = input.getText();
				
				//查询书目列表
				if(findBy.getSelectedItem().equals(items[0])) {
					//通过书名检索,用0代表
					books = userService.findBooks(findMsg, 0);//qaq我也不知道这种模式用的对不对
					System.out.println("Search by Book Name");
				}
				if(findBy.getSelectedItem().equals(items[1])) {
					//通过作者检索，用1代表
					books = userService.findBooks(findMsg, 1);
					System.out.println("Search by Author");
				}
				if(findBy.getSelectedItem().equals(items[2])) {
					//通过编号检索，用2代表
					books = userService.findBooks(findMsg, 2);
					System.out.println("Search by Book Number");
				}
				if (findBy.getSelectedItem().equals(items[3])) {
					//检索所有图书，3
					books = userService.findBooks(findMsg, 3);
					System.out.println("Search for all books");
				}
				
				if(books != null) {
					//将检索的书目列表展示到表格中
					tableData = new String[books.size()][6];
					for(int i=0; i<books.size(); i++) {
						Book book = books.get(i);
						if(book != null) {
							//获取书目数据，存为二维数组
							tableData[i][0] = book.getBookname();
							tableData[i][1] = book.getAuthor();
							tableData[i][2] = book.getNum().toString(); //long类型的编号，转为String类型
							tableData[i][3] = book.getBorrow();
							tableData[i][4] = book.getQuantity().toString();
							tableData[i][5] = book.getLocation();
						}
					}
				}
				//将内容添加以模型方式添加到表格中
				TableModel data = new DefaultTableModel(tableData,tableTitle);
				table.setModel(data);
				input.setText("");
			}
		});
	
    
	borrowBooks.addActionListener(new ActionListener() {
         @Override
			public void actionPerformed(ActionEvent e) {
//				Tools.cleanTwoArray(tableData); //首先清空表格内容
				String findMsg = ""; //用来获得检索书目使输入的内容
				findMsg = input.getText();
				String msg = "";
				
				//查询书目列表
				if(findBy.getSelectedItem().equals(items[0])) {
					//通过书名检索,用0代表
					books = userService.findBooks(findMsg, 0);//qaq我也不知道这种模式用的对不对
					System.out.println("Search by Book Name");
				}
				if(findBy.getSelectedItem().equals(items[1])) {
					//通过作者检索，用1代表
					books = userService.borrowBooks(findMsg, 1);
					System.out.println("Search by Author");
				}
				if(findBy.getSelectedItem().equals(items[2])) {
					//通过编号检索，用2代表
					books = userService.borrowBooks(findMsg, 2);
					System.out.println("Search by Book Number");
				}
				if (findBy.getSelectedItem().equals(items[3])) {
					//检索所有图书，3
					books = userService.borrowBooks(findMsg, 3);
					System.out.println("Search for all books");
				}
				
				if(books != null) {
					//将检索的书目列表展示到表格中
					tableData = new String[books.size()][6];
					for(int i=0; i<books.size(); i++) {
						Book book = books.get(i);
						if(book != null) {
							//获取书目数据，存为二维数组
							tableData[i][0] = book.getBookname();
							tableData[i][1] = book.getAuthor();
							tableData[i][2] = book.getNum().toString(); //long类型的编号，转为String类型
							tableData[i][3] = book.getBorrow();
							tableData[i][4] = book.getQuantity().toString();
							tableData[i][5] = book.getLocation();
						}
					}
                    msg = "Books borrowed successfully !";
					TableModel data = new DefaultTableModel(tableData,tableTitle);
				    table.setModel(data);
				    input.setText("");
				}else{
					msg = "The book isn't available now!";
				}
					Tools.createMsgDialog(jf, msg);

				}
                   //将内容添加以模型方式添加到表格中	
		});


		returnBooks.addActionListener(new ActionListener() {
         @Override
			public void actionPerformed(ActionEvent e) {
//				Tools.cleanTwoArray(tableData); //首先清空表格内容
				String findMsg = ""; //用来获得检索书目使输入的内容
				findMsg = input.getText();
				String msg = "";
				
				//查询书目列表
				if(findBy.getSelectedItem().equals(items[0])) {
					//通过书名检索,用0代表
					books = userService.findBooks(findMsg, 0);//qaq我也不知道这种模式用的对不对
					System.out.println("Search by Book Name");
				}
				if(findBy.getSelectedItem().equals(items[1])) {
					//通过作者检索，用1代表
					books = userService.returnBooks(findMsg, 1);
					System.out.println("Search by Author");
				}
				if(findBy.getSelectedItem().equals(items[2])) {
					//通过编号检索，用2代表
					books = userService.returnBooks(findMsg, 2);
					System.out.println("Search by Book Number");
				}
				if (findBy.getSelectedItem().equals(items[3])) {
					//检索所有图书，3
					books = userService.returnBooks(findMsg, 3);
					System.out.println("Search for all books");
				}
				
				if(books != null) {
					//将检索的书目列表展示到表格中
					tableData = new String[books.size()][6];
					for(int i=0; i<books.size(); i++) {
						Book book = books.get(i);
						if(book != null) {
							//获取书目数据，存为二维数组
							tableData[i][0] = book.getBookname();
							tableData[i][1] = book.getAuthor();
							tableData[i][2] = book.getNum().toString(); //long类型的编号，转为String类型
							tableData[i][3] = book.getBorrow();
							tableData[i][4] = book.getQuantity().toString();
							tableData[i][5] = book.getLocation();
						}
					}
				}
				    msg = "Books returned successfully!";
					TableModel data = new DefaultTableModel(tableData,tableTitle);
				    table.setModel(data);
				    input.setText("");
					Tools.createMsgDialog(jf, msg);
			}
		});
    }
}
