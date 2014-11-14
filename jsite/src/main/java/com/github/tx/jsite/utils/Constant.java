package com.github.tx.jsite.utils;

public class Constant {
	
	public static final String PREMISSION_STRING = "perms[\"{0}\"]";
	
	public static final String PAGINATION_TYPE_AJAX = "ajax";
	
	public static final String PAGINATION_TYPE_TRAD = "tradition";
	
	public static final String PAGINATION_SIZE = "10";
	
	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**会员卡状态*/
	
	public static final Byte CARD_STATUS_UNUSE = 0;//未使用
	
	public static final Byte CARD_STATUS_NORMAL = 1;//正常
	
	public static final Byte CARD_STATUS_LOST = 2;//挂失
	
	public static final Byte CARD_STATUS_CANCEL = 3;//注销
	
	/**操作流水代码*/
	
	public static final Byte OPERATE_MAKE = 1;//制卡
	
	public static final Byte OPERATE_SEND = 2;//发卡
	
	public static final Byte OPERATE_RETURN = 3;//退卡
	
	public static final Byte OPERATE_LOST = 4;//挂失
	
	public static final Byte OPERATE_HANG = 5;//解挂
	
	public static final Byte OPERATE_CHARGE = 11;//充值
	
	public static final Byte OPERATE_PAY = 12;//支付
	
	/**附件类型*/
	
	public static final Byte ATTACHMENT_IMAGE = 1;
	public static final Byte ATTACHMENT_VIDEO = 2;
	
	/**状态代码**/
	
	public static final Byte VALID = 1;//有效
	public static final Byte NOT_VALID = 0;//无效
	
	/**菜品状态代码**/
	
	public static final Byte ON_SALE = 1;//在售
	public static final Byte NOT_SALE = 2;//停售
	public static final Byte OUT_OF_STOCK = 3;//沽清 
	
	/**会员照片上传路径**/
	public static final String CUSTOMER_PHOTO = "upload/customerPhoto/";
	
	/**临时上传目录路径**/
	public static final String TEMP_PATH = "temp";
	
	/**上传目录路径**/
	public static final String UPLOAD_PATH = "upload";
	
	/**excel模板下载路径**/
	public static final String EXCEL_TEMPLATE_PATH = "static/template/";
	
	/**excel模板类型与对应的栏位数目**/
	public static final String EXCEL_CARD = "card";
	public static final int EXCEL_CARD_COLUMN = 5;
	public static final String EXCEL_MENU = "menu";
}
