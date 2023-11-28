package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Admin;
import model.Book;
import service.AdminService;
import utils.Tools;

/**
 * - 管理员用户界面
 * - 高：500
 * - 宽：800
 * - 快捷键：alt+shift+r：批量修改变量名
 * @author Administrator
 *
 */
public class AdminView {
	//service和admin
	AdminService adminService = new AdminService();
	private Admin admin = new Admin();
	//主窗口
	private JFrame adminView = new JFrame("Library Management System(Admin)");
	private Container c = adminView.getContentPane();
	//顶部面板组件
	private JPanel topPanel = new JPanel(); //顶部面板
	private JLabel topAdminMsg = new JLabel(); //管理员信息
	private JComboBox<String> topFindBy = new JComboBox<String>(); //下拉列表
	private String items[] = {"Search by Book Name", "Search by Author", "Search by Book Number", "Search All Books"};
	private JTextField topInput = new JTextField(); //检索输入框
	private JLabel topTime = new JLabel(); //用来显示登录时间
	//侧边面板和组件
	private JPanel sidePanel = new JPanel(); //侧边面板
	private JButton sideSubmit = new JButton("Search"); //查
	private JButton sideAddBtn = new JButton("  Add Book  "); //增
	private JButton sideDeleteBtn = new JButton("Delete Book"); //删
	private JButton sideUpdateBtn = new JButton("Update Book"); //改
	//功能面板
	private JPanel addPanel = new JPanel();
	private JPanel deletePanel = new JPanel();
	
	private JScrollPane findPanel = new JScrollPane(); //检索图书表格面板
	private JPanel updataPanel = new JPanel();
	private JLabel functionMsg = new JLabel(); //当前正在使用的什么功能？
	//添加功能组件
	private JLabel addName = new JLabel("Book Name");
	private JLabel addAuthor = new JLabel("Book Author");
	private JLabel addNumber = new JLabel("Book Number");
	private JLabel addLocation = new JLabel("Book Location");
	private JLabel addBorrow = new JLabel("Borrowing Information");
	private JLabel addQuantity = new JLabel("Quantity of Books");
	private JComboBox<String> addBorrowText = new JComboBox<String>();
	private String[] addBorrowList = {"Y","N"};
	private JTextField addNameText = new JTextField(30);
	private JTextField addAuthorText = new JTextField(30);
	private JTextField addNumberText = new JTextField(30);
	private JTextField addLocationText = new JTextField(30);
	private JTextField addQuantityText = new JTextField(30);
	private JButton addCommitBtn = new JButton("Add"); //提交按钮
	private JButton addCleanBtn = new JButton("Clear"); //清空按钮
	//查找功能组件
	private JLabel findTitle = new JLabel("Book Information"); //表格标题
	private JTable findTable = new JTable();
	String[] findTableTitle = new String[]{"Book Name", "Book Author", "Book Number", "Borrowing Info", "Location", "Quantity of Books"};//表格头字段
	/**
	 * 	如何实现动态获取图书列表，使二维数组大小与获取图书列表的数量一致？？？
	 */
//	private String[][] findTableData = new String[20][5];//表格内容
	private String[][] findTableData;;//表格内容
	//删除功能组件
	private JLabel deletenum = new JLabel("Please enter the number of the book to be deleted:");
	private JTextField deletenumText = new JTextField(30); //获得图书编号
	private JButton deletenumBtn = new JButton("Delete");
	//修改功能组件
	private JLabel updateNum = new JLabel("Please enter the number of the book to be updated:");
	private JTextField updateNumText0 = new JTextField(30);
	private JButton updateFindBtn = new JButton("Search");
	private JButton updateBtn = new JButton("Update");
	private JTextField updateNameText = new JTextField(30);
	private JTextField updateAuthorText = new JTextField(30);
	private JTextField updateNumText = new JTextField(30);
	private JTextField updateLocationText = new JTextField(30);
	private JComboBox<String> updateBorrowText = new JComboBox<String>();
	private JTextField updateQuantityText = new JTextField(30);
	private int id;
	//实例
	private Book book = null;
	private List<Book> books = new ArrayList<Book>();
	//删除的dialog
	private JDialog bookDialog = new JDialog(adminView, "Book Information", true); //展示删除图书信息的窗口
	private JButton sure = new JButton("Confirm Deletion");
	private JButton nosure = new JButton("Cancel");
	//错误信息
	private String msg = "";
	
	public AdminView() {
		init();
		adminView.setVisible(true);
	}
	
	public AdminView(Admin admin) {
		this.admin = admin;
		init();
		adminView.setVisible(true);
	}
	
	private void init() {
		c.setLayout(null);
		adminView.setSize(800, 500);
		adminView.setLocationRelativeTo(null);
		adminView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		adminView.setResizable(false);
		
		createTopPanel(); //生成顶部面板
		createSidePanel(); //生成侧边面板
		selectFunctionListener(); //侧边按钮监听：功能选择加载不同面板
		createListener(); //监听器
	}
	
	/**
	 * 设置功能提示字体
	 * @param msg
	 */
	private void setFunctionMsg(String msg) {
		
		functionMsg.setBounds(200, 10, 700, 30);
		functionMsg.setText(msg);
		functionMsg.setFont(new Font("Arial", Font.BOLD, 25));
		functionMsg.setForeground(Color.orange);
	}
	
	/**
	 * 顶部面板：800*100
	 */
	private void createTopPanel() {
		topPanel.setLayout(null);
		topPanel.setBounds(0,0,800,100);
		topPanel.setBackground(Color.GRAY);
		
		//显示登录用户的名字
		topAdminMsg.setBounds(10,5,200,20);
		topAdminMsg.setFont(new Font("Arial",Font.PLAIN,15));
		topAdminMsg.setText("Welcome (Administrator): "+admin.getUsername());
		
		//检索方式按钮
		ComboBoxModel<String> model = new DefaultComboBoxModel<>(items);
		topFindBy.setModel(model);
		topFindBy.setFont(new Font("Verdana", Font.PLAIN, 9));
		topFindBy.setBounds(120,40,150,25);
		
		//检索输入框
		topInput.setBounds(280, 40, 280, 25);
		
		//检索按钮
		sideSubmit.setBounds(580, 40, 80, 25);
		
		//当前时间
		topTime.setBounds(600, 70, 200, 25);
		topTime.setFont(new Font("Times New Roman", Font.ITALIC, 11));
		topTime.setText("Login Time: "+Tools.getTime());
		
		topPanel.add(topAdminMsg);
		topPanel.add(topFindBy);
		topPanel.add(topInput);
		topPanel.add(sideSubmit);
		topPanel.add(topTime);
		c.add(topPanel);
		topPanel.setVisible(true);
	}
	
	/**
	 * 侧边面板：增删查改按钮(100*400)
	 */
	private void createSidePanel() {
		sidePanel.setLayout(new FlowLayout());
		sidePanel.setBounds(0, 100, 100, 400);
		sidePanel.setBackground(Color.lightGray);
		
		sidePanel.add(sideAddBtn);
		sidePanel.add(sideDeleteBtn);
		sidePanel.add(sideUpdateBtn);
		c.add(sidePanel);
		sidePanel.setVisible(true);
	}
	
	/**
	 * 添加功能的面板：700*700
	 */
	private JPanel createAddPanel() {
		addPanel.setLayout(null);
		addPanel.setBounds(100, 100, 700, 700);
		
		//设置功能提示字体
		setFunctionMsg("You are using the add function.");
		
		//添加
		addName.setBounds(80, 60, 150, 30);
		addAuthor.setBounds(80, 90, 150, 30);
		addNumber.setBounds(80, 120, 150, 30);
		addLocation.setBounds(80, 150, 150, 30);
		addQuantity.setBounds(80, 180, 150, 30);
		addBorrow.setBounds(80, 210, 150, 30);
		
		addNameText.setBounds(220, 60, 300, 25);
		addAuthorText.setBounds(220, 90, 300, 25);
		addNumberText.setBounds(220, 120, 300, 25);
		addLocationText.setBounds(220, 150, 300, 25);
		addQuantityText.setBounds(220, 180, 300, 25);
		ComboBoxModel<String> model = new DefaultComboBoxModel<String>(addBorrowList);
		addBorrowText.setModel(model);
		addBorrowText.setBounds(220, 210, 80, 25);
		
		
		//按钮
		addCommitBtn.setBounds(280, 280, 60, 25);
		addCleanBtn.setBounds(350, 280, 60, 25);
		
		addPanel.add(functionMsg);
		addPanel.add(addName);
		addPanel.add(addAuthor);
		addPanel.add(addNumber);
		addPanel.add(addBorrow);
		addPanel.add(addQuantity);
		addPanel.add(addLocation);
		addPanel.add(addNameText);
		addPanel.add(addAuthorText);
		addPanel.add(addNumberText);
		addPanel.add(addBorrowText);
		addPanel.add(addQuantityText);
		addPanel.add(addLocationText);
		addPanel.add(addCommitBtn);
		addPanel.add(addCleanBtn);
		return addPanel;
	}

	/**
	 * 查找功能面板：700*300
	 */
	private JScrollPane createFindPanel() {
		//设置功能提示字体
		setFunctionMsg("You are using the search function.");
		functionMsg.setBounds(300, 110, 700, 30);
		//标题
		findTitle.setBounds(400, 150, 150, 25);
		
		//表格的设置
		findTable.getTableHeader().setReorderingAllowed(false);	//列不能移动
		findTable.getTableHeader().setResizingAllowed(false); 	//不可拉动表格
		findTable.setEnabled(false);							//不可更改数据
		findTable.setRowHeight(20);
		findPanel.setBounds(150, 190, 600, 250);
		findPanel.setViewportView(findTable);	//视口装入表格,暂时不知道什么用，但是放在后面，第二次点击检索时，排版会出现问题！
		
		c.add(functionMsg);
		c.add(findTitle);
		
		return findPanel;
	}
	
	/**
	 * 删除功能面板：700*700
	 */
	private JPanel createDeletePanel() {
		deletePanel.setLayout(null);
		deletePanel.setBounds(80, 100, 700, 700);
		
		//设置功能提示字体
		setFunctionMsg("You are using the delete function.");
		
		//首先搜索
		deletenum.setBounds(50, 70, 400, 30);
		deletenumText.setBounds(150, 100, 330, 25);
		deletenumBtn.setBounds(520, 100, 80, 25);
		
		deletePanel.add(functionMsg);
		deletePanel.add(deletenum);
		deletePanel.add(deletenumText);
		deletePanel.add(deletenumBtn);
		return deletePanel;
	}
	
	/**
	 * 修改功能面板：700*700
	 */
	private JPanel createUpdataPanel() {
		
		updataPanel.setLayout(null);
		updataPanel.setBounds(80, 100, 700, 700);
		
		//功能提示字体
		setFunctionMsg("You are using the update function.");
		
		//搜索第一
		updateNum.setBounds(50, 70, 400, 30);
		updateNumText0.setBounds(150, 100, 330, 25);
		updateFindBtn.setBounds(520, 100, 80, 25);
		
		updataPanel.add(functionMsg);
		updataPanel.add(updateNum);
		updataPanel.add(updateNumText0);
		updataPanel.add(updateFindBtn);
		return updataPanel;
	}
	
	/**
	 * 主要面板：功能实现区
	 * 根据点击不同按钮，实现传入不同面板
	 */
	private void createMainPanel(JPanel jp) {
		c.remove(findTitle); //在检索面板中，主窗口加入了findTitle，在其他面板中应该删除。
		c.add(jp);
		jp.setVisible(true);
	}
	private void createMainPanel(JScrollPane jp) {
		c.add(jp);
		jp.setVisible(true);
	}
	
//------------监听器------------------------------------------------------------------
	/**
	 * - 侧边面板 功能选择 监听器
	 */
	private void selectFunctionListener() {
		//添加功能
		sideAddBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//首先，取消其他面板
				//如果不讲所有的面板设为不可见，那么在第一次点击按钮时，面板显示会出现问题
				//这是为什么呢？？？
				//还好被我发现了QAQ
				setVisibleFalse();
				
				//创建添加功能面板
				createMainPanel(createAddPanel());
			}
		});
		
		//删除功能
		sideDeleteBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//首先，取消其他面板
				setVisibleFalse();
				
				//创建删除功能面板
				createMainPanel(createDeletePanel());
			}
		});
		
		//修改功能
		sideUpdateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//首先，取消其他面板
				setVisibleFalse();
				
				//创建删除功能面板
				createMainPanel(createUpdataPanel());
			}
		});
	}
	
	/**
	 *-  监听器
	 *- emmm这个方法，可能有点乱...
	 *- 这段代码，只有我和上帝知道是什么意思
	 *- 再过几个月，只有上帝能看懂
	 */
	private void createListener() {
		//检索按钮，复制的UserView的...
		//userService改成了adminService，因为俺让adminService继承了userService。
		//可以吗？QAQ
		sideSubmit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//首先，将所有面板设置为不可见
				setVisibleFalse();
				
				//然后，创建检索面板
				createMainPanel(createFindPanel());
				
//				Tools.cleanTwoArray(findTableData); //首先清空表格内容
				String findMsg = ""; //用来获得检索书目使输入的内容
				findMsg = topInput.getText();
				
				//查询书目列表
				if(topFindBy.getSelectedItem().equals(items[0])) {
					//通过书名检索,用0代表
					books = adminService.findBooks(findMsg, 0);//qaq我也不知道这种模式用的对不对
					System.out.println("Search by Book Name");
				}
				if(topFindBy.getSelectedItem().equals(items[1])) {
					//通过作者检索，用1代表
					books = adminService.findBooks(findMsg, 1);
					System.out.println("Search by Author");
				}
				if(topFindBy.getSelectedItem().equals(items[2])) {
					//通过编号检索，用2代表
					books = adminService.findBooks(findMsg, 2);
					System.out.println("Search by Book Number");
				}
				if (topFindBy.getSelectedItem().equals(items[3])) {
					//检索所有图书，3
					books = adminService.findBooks(findMsg, 3);
					System.out.println("Search All Books");
				}
				
				if(books != null) {
					//将检索的书目列表展示到表格中
					findTableData = new String[books.size()][6];
					for(int i=0; i<books.size(); i++) {
						Book book = books.get(i);
						if(book != null) {
							//获取书目数据，存为二维数组
							findTableData[i][0] = book.getBookname();
							findTableData[i][1] = book.getAuthor();
							findTableData[i][2] = book.getNum().toString(); //long类型的编号，转为String类型
							findTableData[i][3] = book.getBorrow();
							findTableData[i][4] = book.getLocation();
							findTableData[i][5] = book.getQuantity().toString();
						}
					}
				}
				//将内容添加以模型方式添加到表格中
				TableModel data = new DefaultTableModel(findTableData,findTableTitle);
				findTable.setModel(data);
				topInput.setText("");//检索完毕，将搜索框内容置空
			}
		});
	
		//添加图书按钮
		addCommitBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//获取数据
				String bookname = "";
				String author = "";
				Long number = -1l;
				String location = "";
				Integer quantity = 0;
				String borrow = "";
				//图书编号不能有空格或者是空，否则赋值时错误。LONG类型的
				if (addNumberText.getText().contains(" ") || addNumberText.getText().equals("")) {
					msg = "Book Number cannot contain spaces or be empty!";
				}else if(!Tools.isNumeric(addNumberText.getText())) {
					//编号是中文怎么办？
					msg = "The Number must be numeric.";
				}else {
					bookname = addNameText.getText();
					author = addAuthorText.getText();
					number = Long.parseLong(addNumberText.getText());
					borrow = addBorrowText.getSelectedItem().toString();
					quantity = Integer.parseInt(addQuantityText.getText());
					location = addLocationText.getText();
					
					book = new Book(bookname, author, number, borrow, quantity,location);
					//将数据交给service层处理
					msg = adminService.addBook(book);
				}
				
				//该工具类的方法，创建一个JDialog对话框，展示操作成功与否的信息
				Tools.createMsgDialog(adminView, msg);
			}
		});
	
		//清空添加图书信息
		addCleanBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addNameText.setText("");
				addAuthorText.setText("");
				addNumberText.setText("");
				addLocationText.setText("");
				addBorrowText.setSelectedIndex(0);
				addQuantityText.setText("");
			}
		});
	
		//删除图书按钮
		deletenumBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//首先，搜索要删除的图书信息
				book = adminService.findByNumber(deletenumText.getText());
				if (book!=null) {
					showBookMsgDialog(book);
				}else {
					Tools.createMsgDialog(adminView, "This book does not exist!");
					deletenumText.setText("");
				}
			}
		});
		
		//确定删除的监听器
		//这样写对吗....有种不祥的预感
		sure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = adminService.deleteByNum(book.getNum().toString());
				Tools.createMsgDialog(adminView, msg);
				bookDialog.dispose();
			}
		});
		
		//取消删除的监听器
		nosure.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bookDialog.dispose();
			}
		});
		
		
		//修改图书中搜索按钮
		updateFindBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//首先，搜索要删除的图书信息
				book = adminService.findByNumber(updateNumText0.getText());
				if (book != null) {
					showBookMsgOnUpdataPanel(book);
				}else {
					Tools.createMsgDialog(adminView, "This book does not exist!");
				}
			}
		});
		
		//修改按钮
		updateBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String msg = "";
				if (updateNumText.getText().equals("") || updateNumText.getText().contains(" ")) {
					Tools.createMsgDialog(adminView, "Incorrect format for quantity!");
				}else {
					//封装为Book对象
					System.out.println(updateBorrowText.getSelectedItem().toString());
					book = new Book(id ,updateNameText.getText(), updateAuthorText.getText(),
							Long.parseLong(updateNumText.getText()), updateBorrowText.getSelectedItem().toString(),Integer.parseInt(updateQuantityText.getText()),
							updateLocationText.getText());
					msg = adminService.update(book);
					Tools.createMsgDialog(adminView, msg);
					cleanBookMsgOnUppdataPanel(); //修改完毕，清除搜索到的图书展示信息
				}
			}
		});
	}
	
	/**
	 * - 设置面板为不可见，并且remove面板
	 */
	private void setVisibleFalse() {
		addPanel.setVisible(false);
		deletePanel.setVisible(false);
		findPanel.setVisible(false);
		updataPanel.setVisible(false);
		
		c.remove(addPanel);
		c.remove(deletePanel);
		c.remove(findPanel);
		c.remove(updataPanel);
	}
	
	/**
	 * - 用来显示 删除或修改图书信息的JDialog
	 */
	private void showBookMsgDialog(Book book) {
		
		Container bookContainer = bookDialog.getContentPane();
		bookContainer.removeAll();
		bookContainer.setLayout(null);
		
		bookDialog.setSize(500, 300);
		bookDialog.setLocationRelativeTo(null);
		
		//获取图书信息
		JLabel name = new JLabel("Name: "+book.getBookname());
		JLabel author = new JLabel("Author: "+book.getAuthor());
		JLabel num = new JLabel("Number: "+book.getNum().toString());
		JLabel borrow = new JLabel("Borrowing Info: "+book.getBorrow());
		JLabel quantity = new JLabel("Quantity of Books: "+book.getQuantity().toString());
		JLabel location = new JLabel("Location: "+book.getLocation());
		
		//按钮
		//在顶部
		
		name.setBounds(100, 50, 300, 30);
		author.setBounds(100, 80, 300, 30);
		num.setBounds(100, 110, 300, 30);
		borrow.setBounds(100, 140, 300, 30);
		quantity.setBounds(100,170,300,30);
		location.setBounds(100, 200, 300, 30);
		sure.setBounds(110, 240, 150, 25);
		nosure.setBounds(260, 240, 100, 25);
		
		bookContainer.add(name);
		bookContainer.add(author);
		bookContainer.add(num);
		bookContainer.add(location);
		bookContainer.add(borrow);
		bookContainer.add(quantity);
		bookContainer.add(sure);
		bookContainer.add(nosure);
		bookDialog.setVisible(true);
	}

	/**
	 * - 修改图书时，首先搜索要修改的图书，将信息展示在修改面板中
	 * @param book
	 */
	private void showBookMsgOnUpdataPanel(Book book) {
		//获取图书信息
		JLabel name = new JLabel("Name: ");
		JLabel author = new JLabel("Author: ");
		JLabel num = new JLabel("Number: ");
		JLabel location = new JLabel("Location: ");
		JLabel borrow = new JLabel("Borrowing Info: ");
		JLabel quantity = new JLabel("Quantity of Books: ");
		ComboBoxModel<String> model = new DefaultComboBoxModel<String>(addBorrowList);
		updateBorrowText.setModel(model);
		
		//设置搜索的图书信息
		id = book.getBookid();
		updateNameText.setText(book.getBookname());
		updateAuthorText.setText(book.getAuthor());
		updateNumText.setText(book.getNum().toString());
		updateLocationText.setText(book.getLocation());
		if (book.getBorrow().equals("N")) {
			updateBorrowText.setSelectedIndex(1);
		}
		
		name.setBounds(160, 130, 120, 30);
		author.setBounds(160, 160, 120, 30);
		num.setBounds(160, 190, 120, 30);
		location.setBounds(160, 220, 120, 30);
		borrow.setBounds(160, 250, 120, 30);
		quantity.setBounds(160, 280, 150, 30);
		
		updateNameText.setBounds(300, 130, 200, 25);
		updateAuthorText.setBounds(300, 160, 200, 25);
		updateNumText.setBounds(300, 190, 200, 25);
		updateLocationText.setBounds(300, 220, 200, 25);
		updateBorrowText.setBounds(300, 250, 80, 25);
		updateQuantityText.setBounds(300, 280, 200, 25);
		updateBtn.setBounds(200, 310, 312, 25);
		
		updataPanel.add(name);
		updataPanel.add(author);
		updataPanel.add(num);
		updataPanel.add(location);
		updataPanel.add(borrow);
		updataPanel.add(quantity);
		updataPanel.add(updateNameText);
		updataPanel.add(updateAuthorText);
		updataPanel.add(updateNumText);
		updataPanel.add(updateLocationText);
		updataPanel.add(updateBorrowText);
		updataPanel.add(updateQuantityText);
		updataPanel.add(updateBtn);
		//实现刷新！
		updataPanel.setVisible(false);
		updataPanel.setVisible(true);
	}

	/**
	 * 	在修改图书功能中：清除搜索到的图书内容
	 */
	private void cleanBookMsgOnUppdataPanel() {
		updateNameText.setText("");
		updateAuthorText.setText("");
		updateNumText.setText("");
		updateLocationText.setText("");
		updateBorrowText.setSelectedIndex(0);
		updateQuantityText.setText("");
	}
	
}
