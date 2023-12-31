package service;

import dao.RegistDao;
import model.Admin;
import model.AdminCode;
import model.Register;
import model.User;

public class RegistService {
	
	private RegistDao registDao = new RegistDao();
	
	/**
	 * - 注册
	 * - 逻辑：首先验证姓名、账号、密码是否包含空格或者为空。是-->返回错误信息
	 * -	否-->判断是否为普通用户注册。是-->注册
	 * -  	否-->判断密钥是否正确。是->注册
	 * 		否->结束注册，返回错误信息
	 * @param register
	 * @return
	 */
	public String regist(Register register) {
		String msg = "";
		if(register.getUsername().contains(" ") || register.getUsername().equals("") ||  
				register.getPassword().contains(" ") || register.getPassword().equals("")) {
			msg = "Input cannot contain spaces or be left empty.";
		}else if(register.getUsername().length() < 2 ||  register.getPassword().length()<5){
			//姓名长度大于等于2，账号大于等于5，密码大于等于5
			msg = "Account and password must be at least 5 characters, name at least 2 characters.";
		}else if (isExist(register)) {
			//用户已经存在（注册）
			msg = "This account already exists.";
		}else if(register.getMold().equals("Student")) {
			//普通用户注册
			msg = registDao.userRegist(register);
		}else if(checkAdminCode(register.getAdminCode())){//管理员注册，首先验证密钥是否正确
			//密钥正确，管理员注册
			msg = registDao.adminRegist(register);
		}else {
			//密钥不正确
			msg = "The key is incorrect or has been used too many times.";
		}
		return msg;
	}

	/**
	 * - 验证密钥
	 * @param adminCode
	 * @return
	 */
	private boolean checkAdminCode(String adminCode) {
		AdminCode adminCodeMold = new AdminCode();
		adminCodeMold = registDao.checkAdminCode(adminCode);
		//密钥是否存在
		if (adminCodeMold != null) {
			//检查密钥使用次数是否使用完
			if (adminCodeMold.getCount() != 0) {
				//没有使用完,有效次数减1
				registDao.updateAdminCode(adminCodeMold);
				return true;
			}else {
				//使用完
				return false;
			}
		}else {
			//不存在
			return false;
		}
	}
	
	/**
	 * 	判断用户或管理员是否注册
	 * @param register
	 * @return
	 */
	private boolean isExist(Register register) {
		User user = registDao.findUserByCode(register.getUsername());
		if (user != null) {
			//用户存在
			return true;
		}else {
			//用户不存在，查找是否是管理员
			Admin admin = registDao.findAdminByCode(register.getUsername());
			if (admin != null) {
				//管理员
				return true;
			}
		}
		//既不是普通用户，也不是管理员=不存在
		return false;
	}
}
