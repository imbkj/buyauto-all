package com.buyauto.util.method;

import java.math.BigDecimal;

public class CommonCode {

	/**
	 * 
	 * @ClassName: CmsType
	 * @Description: 短信发送类别
	 * @author cxz
	 * @date 2017年5月17日 下午2:06:51
	 *
	 */
	public static class CmsType {

		public final static int Infomartion = 0;

		public final static int Email = 1;

	}

	/**
	 * 成功
	 */
	public static final String SUCCESS = "SUCCESS";
	/**
	 * 操作数据库返回影响条数
	 */
	public static final Integer RESULTNUM = 0;
	public static final Integer LIMITNUM = 2;
	
	public static class CrmUserCode {
		public final static int REQUEST_ERROR = 500;
		public final static int UPDATE_SUCCESS = 201;
		public final static int INSERT_SUCCESS = 200;
	}

	/**
	 * 登录返回结果
	 */
	public static class LoginResult {
		public final static int SUCCESS = 200;// 成功
		public final static int SUCCESS_JUMP = 201;// 成功并且跳转至经销商页面
		public final static int SUCCESS_JUMP_EDITPWD = 202;// 成功并且跳转至经销商页面
		public final static int NACCOUTN = 301;//账号为空
		public final static int NPWD = 302;//密码为空
		public final static int CODE_ERROR = 500;// 验证码错误
		public final static int ERROR = 501;// 账户名或密码错误
		public final static int USER_ERROR = 502;// 用户状态已销户
		public final static int ACCOUNT_ERROR = 504;//请输入正确的账户
		public static final int STATUS_AUDIT_WAIT=505;//用户审核中
		public static final int STATUS_NOTAUDIT=506;//该用户审核不通过
		public static final int STATUS_FROZEN=507;//该用户已冻结
		public static final int FREEZE_FAIL=508;//该用户对应经销商已冻结
	}
	
	public static class LoginState {
		public final static String CODE = "user_code";
		public final static String OBJ = "user_object";
		public final static String INFO = "info";//审核不通过原因
		public final static String TOKEN = "token-";//手机端登录返回一个标识
	}

	// 用户登录成功或者失败
	public static class LoginOperation {
		// 成功
		public final static int SUCCESS = 0;
		// 失败
		public final static int FAIL = 1;

	}

	/**
	 * 
	 * @ClassName: SysRuleLogin
	 * @Description: backend登录code
	 * @author cxz
	 * @date 2016年5月31日 下午2:43:46
	 * 
	 */
	public static class SysRuleLogin {
		/* 登录成功 */
		public final static int SUCCESS = 200;

		/* 登录失败 */
		public final static int FAIL = 400;
		/* 用户名密码错误 */
		public final static int FAIL_USER_PWD = 401;
		/* 用户禁止登录 */
		public final static int FAIL_IS_FALSE = 402;
		/* 用户角色禁用 */
		public final static int FAIL_ROLE_FALSE = 403;
		/* 用户无角色 */
		public final static int FAIL_NO_ROLE = 404;
		/* 用户角色未设置权限 */
		public final static int FAIL_NO_RULE = 405;

	}

	/**
	 * 
	 * @ClassName: OprDevice
	 * @Description: 展示终端
	 * @author cxz
	 * @date 2015年9月1日 上午11:10:11
	 *
	 */
	public static class TDDevice {
		public static final int PC = 1;// 电脑
		public static final int Wechat = 1;// 微信
		public static final int App = 1;// IOS
	}

	public static class DBSuccess {
		public static final int Success = 1;
		public static final int Fail = 0;
	}
	public static class FTPSuccess {
		public static final int Success = 1;
		public static final int Fail = 0;
	}
	public static class Version {
		public static final int Default = 0;
	}

	/**
	 * 
	 * @ClassName: OprDevice
	 * @Description: 展示终端
	 * @author cxz
	 * @date 2015年9月1日 上午11:10:11
	 *
	 */
	public static class OprDevice {
		public static final int ALL = 0;// 全部
		public static final int PC = 1;// 电脑
		public static final int Wechat = 2;// 微信
		public static final int IOSApp = 3;// IOS应用
		public static final int AndroidApp = 4;// android应用

	}

	public static class YesOrNo {
		public static final int Yes = 1; // 是
		public static final int No = 2;// 否

	}

	/**
	 * sys_config表group_name维护
	 * 
	 * @author devx
	 *
	 */
	public static class ConfigGroup {
		public static final String COMMISSION_CONFIG = "commission_edit";
		public static final String COMMISSION = "commission";
		public static final String EXTRACT_ADDRESS = "提车点";
		public static final String CAR_BRAND = "车辆品牌"; 
		public static final String OPTIONAL_CONFIG = "可选配置"; 
		public static final String ASSESS_MONTHS = "总考核月数";
		public static final String UPGRADE_CONFIG = "升级配置";
		public static final String DOWNGRADE_CONFIG = "降级配置";
		public static final String UPGRADE_DOWNGRADE = "Upgrade_downgrade";//升降级配置
		public static final String ACCOUNT_NUMBER = "Account_Number";//银行对公账号

	}

	/**
	 * sys_config表state维护
	 * 
	 * @author devx
	 *
	 */
	public static class ConfigState {
		public static final int ENABLE = 0; // 启用
		public static final int DISABLE = 1; // 禁用
		public static final int WAITPASS = 2; // 待审核
	}

	/**
	 * sys_config表sc_type维护
	 * 
	 * @author devx
	 *
	 */
	public static class ConfigType {
		public static final int TEXT = 0; // 文本
		public static final int PICTURE = 1; // 图片
		public static final int LINK = 2;// 链接

	}

	/**
	 * 是否上线
	 */
	public static class isOnline {
		public static final int isOnline_Yes = 1; // 上线
		public static final int isOnline_No = 0;// 下线
	}

	/**
	 * （新闻管理） 操作结果返回
	 */
	public static class operateRes {
		// 成功
		public static final int YES = 1;
		// 失败
		public static final int NO = 0;

	}

	/**
	 * 新闻状态
	 */
	public static class newsStatus {
		// 已发布
		public static final int PUBLISH = 1;
		// 未发布
		public static final int UNPUBLISH = 0;
		// 禁用
		public static final int DISABLED = 2;
		// 标题已存在
		public static final int ISEXIST = 3;
	}

	/**
	 * 改什么(有关新闻操作)
	 */
	public static class chgWhat {
		// 改是否热点
		public static final Integer HOT = 0;
		// 改新闻状态
		public static final Integer STATUS = 1;
		// 是否置顶
		public static final Integer TOP = 2;
	}

	/**
	 * cms
	 */
	public static class CmsResult {
		public static final String TITLEEXISTS = "标题已存在";
	}
	
	/**
	 * banner
	 */
	public static class CmsBanner {
		public static final Integer isOnline_Yes = 1; // 上线
		public static final Integer isOnline_No = 0;// 未上线
		public static final Integer MAXONLINE = 10;//banner 最大上线条数
		public static final Integer MAXONLINE_CODE = 101;//超过  banner最大上线条数
	}
	
	/**
	 * 新闻
	 */
	public static class CmsNews {
		public static final Integer isHOte = 6;//新闻最大热点条数
		public static final Integer MAX_NOTICE_CODE = 101;//超过  公告最大热点条数
		public static final Integer MAX_INFORMATION_CODE = 102;//超过  资讯最大热点条数
		public static final Integer TOP = 1;//新闻置顶
		public static final Integer UNTOP = 0;//新闻不置顶
		public static final Integer HOT = 1;//热点
		public static final Integer UNHOT = 0;
		
	}

	/**
	 * 审核
	 */
	public static class Audit {
		public static final Integer PASS = 0;// 通过
		public static final Integer NOTPASS = 1;// 不通过
		public static final Integer AUDITTYPEONE = 0;// 经销商注册
		public static final Integer AUDITTYPETWO = 1;// 订单审核
		public static final Integer AUDITTYPETHREE = 2;// 订单取消审核
		public static final Integer AUDITTYPEFORE = 3;// 供应商注册
		public static final Integer AUDITTYPEFIVE = 4;// 个人升级销售
		public static final Integer AUDITTYPESIX = 5;// 佣金配置
		public static final Integer PRODUCTEXAMINE = 6;// 产品审核
		public static final Integer AUDITTYPESEVEN = 7;// 金融方案审核
		public static final Integer SUCCESS = 200;// 成功
		public static final Integer ERROR = 500;// 失败
		public static final Integer WRONGDATA = 501;// 错误数据
	}

	/**
	 * 失败
	 */
	public static class Fail {
		public static final String INSERTFAIL = "新增失败";
		public static final String EDITFAIL = "编辑失败";
		public static final String DELFAIL = "删除失败";
		public static final String DATAISDELETED = "该数据已经被删除";
		public static final String SENDFAIL = "发送失败";
	}

	/**
	 * 车辆产品状态
	 * 
	 * @author devx
	 *
	 */
	public static class SaveState {
		public static final int DRAFT = 0;// 草稿
		public static final int NEWLY = 1;// 新建(审核中)
		public static final int SHELVES = 2;// 上架
		public static final int RECYCLE = 3;// 回收站
		public static final int ADOPT = 4;// 审核通过
		public static final int NOT_PASS = 5;// 审核不通过
	}

	/**
	 * 订单状态修改
	 */
	public static class SaveStatus{
		public static final int NEWCREATE=110;//待系统确认
		public static final int CONFIRMING=100;//确认中
		public static final int WAITDEPOSIT=101;//待支付定金
		public static final int WAITCHK=102;//定金待审核
		public static final int WAITORDERS=103;//待发货
		public static final int WAITTAILMONEY=104;//待支付尾款
		public static final int CHECKTAILMONEY=105;//尾款待审核
		public static final int WAITTHREECARDS=106;//待上传三证
		public static final int WAITSHIP=107;//可交车（待交车）
		public static final int CANCELWAITCHECK=108;//取消待审核
		public static final int CANCELCHECKNOTPASS=109;//取消审核不通过
		public static final int COMPLETE=200;//完成
		
		public static final int FIRSTCHK=401;//库存确认不通过
		public static final int SECONDCHK=402;//定金审核不通过
		public static final int TAILMOENYUNPASS=403;//尾款审核不通过
		public static final int CANCELORDER=404;//订单取消
		public static final int CLOSEORDER=405;//订单关闭
		
		
		public static final int UNPAIED=0;//未支付
		public static final int OVERTIME=1;//定金支付超时
		public static final int DEPOSITPAID=10;//定金已支付
		public static final int TAILMONEYPAID=11;//已支付
		public static final int DEPOSITBACK=20;//定金退款
		public static final int TAILMONEYBACK=12;//定金退款
	}
	/**
	 * 订单图片类型
	 */
	public static class OrdersFilesType{
		public static final int DEPOSIT=0;//定金
		public static final int TAILMONEY=1;//尾款支付
		public static final int REFUNDS=2;//定金退款
		public final static Integer TAILMONEYBACK = 3;//尾款退款
		public final static Integer CARCERTIFICATE = 4;//车辆三证
	}
	

	/**
	 * 产品图片类型
	 * 
	 * @author devx
	 *
	 */
	public static class ProductsImageType {
		public static final int THUMBNAIL = 0;// 缩略
		public static final int COVER = 1;// 封面
		public static final int RECOMMEND = 2;// 推荐
		public static final int DETAILS = 3;// 详情
		public static final int PRODUCTFILE = 4;// 车辆三证
	}

	/**
	 * 产品页面状态
	 * 
	 * @author devx
	 *
	 */
	public static class ProductStatus {
		public static final int PRODUCT = 1;// 产品
		public static final int DRAFT = 0;// 草稿
		public static final int RECYCLE = 3;// 回收站
	}

	/**
	 * 产品推荐状态
	 * 
	 * @author devx
	 *
	 */
	public static class RecommendStatus {
		public static final int NOT_RECOMMENDED = 0;// 不推荐
		public static final int RECOMMENDED = 1;// 推荐

	}

	
	/**
	 * 登陆日志 
	 */
	public static class LogLogin{
		public static final int SUCCESS=0;//成功
		public static final int FAIL=1;//失败
	}
	/**
	 * 用户状态 
	 */
	public static class UserState{
		public static final String CODE="code";//返回后台编码
		public static final String USERINFO="userInfo";//用户信息
		public static final String CODE_IMG_REGISTER="code_img_register";//经销商注册图片验证码key
		public static final String CODE_PHONE_REGISTER="code_phone_register";//经销商注册手机验证码key
		public static final String CODE_IMG_FORGET="code_img_forget";//经销商忘记密码图片验证码key
		public static final String CODE_PHONE_FORGET="code_phone_forget";//经销商注册手机验证码key
		public static final String CODE_STATUS_SUCCESS="code_status_success";//可用员工key
		public static final String CODE_STATUS_FROZEN="code_status_frozen";//冻结员工key
		public static final String CODE_ACCOUNT_COUNT="code_account_count";//查找经销商名下员工数量key
		public static final String CODE_IMG_SUPPLIER="code_img_supplier";//供应商注册密码图片验证码key
		public static final String CODE_PHONE_SUPPLIER="code_phone_supplier";//供应商注册手机验证码key
		
		public static final int POSITION_ADMIN=0;//职位-管理员
		public static final int POSITION_SALE=1;//职位-销售
		public static final int POSITION_FINANCE=2;//职位-财务
		public static final int POSITION_SUPPLIER=3;//职位-供应商
		public static final int POSITION_PERSONAL=4;//职位-个人
		
		public static final int STATUS_AUDIT_WAIT=0;//状态-待审核
		public static final int STATUS_SUCCESS=1;//状态-可用
		public static final int STATUS_NOTAUDIT=2;//状态-审核不通过
		public static final int STATUS_FROZEN=3;//状态-冻结
		public static final int FREEZE_SUCESS=0;//个人状态(经销商冻结状态)-可用
		public static final int FREEZE_FAIL=1;//个人状态(经销商冻结状态)-禁用
		public static final int ISFIRSTLOGIN_YES=0;//是否首次登录:0是
		public static final int ISFIRSTLOGIN_NO=1;//是否首次登录:1否
		public static final int TYPE_DISTRIBUTOR=0;//类型-经销商
		public static final int TYPE_SUPPLIER=1;//类型-供应商
		public static final int TYPE_PERSONAL=2;//类型-个人
		public static final int SELF_SUPPORT_NO=0;//自营供应商-否
		public static final int SELF_SUPPORT=1;//自营供应商-是
		
		public static final int ACCOUNT_COUNT=20;//经销商新建子员工数量
		
		public static final String TOKEN = "token";//唯一标识key(返回手机端)
		public static final String PHONE = "phone-";//唯一标识key(返回手机端)
	}
	
	/**
	 * 新闻公告类型
	 * @author devx
	 *
	 */
	public static class CmsNewsType{
		public static final int NOTICE=1;//公告
		public static final int NEWS=2;//新闻
		public static final Integer ISHOT_YES = 1;//是否热点：是
		public static final Integer ISHOT_NO = 0;//是否热点：否
	}
	
	/**
	 * 注册向页面返回CODE
	 */
	public static class RegisterResult {
		public final static Integer SUCCESS = 200;// 成功
		public final static Integer NACCOUTN = 301;//经销商、供应商 名称为空
		public final static Integer NPWD = 302;//密码为空
		public final static Integer NCOMPANY_NAME = 303;//公司名称为空
		public final static Integer NNAME = 304;//姓名为空
		public final static Integer NPHONE = 305;//手机为空
		public final static Integer NIMGCODE = 306;//图片验证码为空
		public final static Integer NPHONECODE = 307;//手机验证码为空
		public final static Integer NEMAIL = 308;//邮箱为空
		public final static Integer NFILE = 309;//证件凭证为空
		public final static Integer NCODEPHONE_SERVICE = 310;//请先发送手机验证码
		public final static Integer NCODEIMG_SERVICE = 311;//请先刷新图片验证码
		public final static Integer NADDRESS=312;//办工场所为空
		
		public final static Integer SIZEOUTFILE = 401;//证件凭证最大只能上传3张
		public final static Integer FIMG_CODE = 402;//图片验证码错误
		public final static Integer FPHONE_CODE = 403;//手机验证码错误
		public final static Integer ERROR = 501;//插入数据失败
		public final static Integer INVALID_IMGCODE = 502;//图片验证码已失效
		public final static Integer ALEADY_ACCOUNT = 601;//账户已存在
		public final static Integer ALEADY_EMAIL = 602;//邮箱已存在
		public final static Integer ALEADY_PHONE = 603;//手机已存在
		public final static Integer ALEADY_COMPANYNAME = 604;//公司名称已存在
		public final static Integer REG_PHONE = 701;//手机格式不正确
		public final static Integer REG_EMAIL = 702;//邮箱格式不正确
		public final static Integer ERROR_PARAM = 801;//参数不正确
		public final static Integer REC_PHONE = 901;//推荐人手机格式不正确
		public final static Integer OUT_RANGE = 902;//推荐人上限超额（2人）
		public final static Integer NRECOMMENDER = 903;//推荐人不存在
		
		public final static Integer NLOGIN = 1001;//用户未登录
//		public final static Integer NOLDPWD = 1002;//旧密码不正确
//		public final static Integer NPWDTWO = 1003;//两次密码输入不一致
	}
	
	
	
	/**
	 * 经销商找回密码向页面返回CODE
	 */
	public static class ForgetResult {
		public final static Integer SUCCESS = 200;// 成功
		public final static Integer ERROR = 201;// 修改密码失败
		public final static Integer NPHONE = 301;//手机为空
		public final static Integer NIMGCODE = 302;//图片验证码为空
		public final static Integer NPHONECODE = 303;//手机验证码为空
		public final static Integer NPWD = 304;//密码为空
		public final static Integer FIMG_CODE = 401;//图片验证码错误
		public final static Integer FPHONE_CODE = 402;//手机验证码错误
		public final static Integer INVALID_IMGCODE = 501;//图片验证码已失效
		public final static Integer INVALID_PHONE = 502;//手机验证码已失效
		public final static Integer STATUS_AUDIT_WAIT=503;//用户审核中
		public final static Integer STATUS_FROZEN=504;//该用户已冻结
		public final static Integer REG_PHONE = 701;//手机格式不正确
		public final static Integer CK_EDITPWD = 801;//手机验证码验证失败(修改密码)
		public final static Integer CK_USER = 802;//只有管理员才能修改密码
		public final static Integer NO_USER = 803;//未查到该用户
	}
	
	/**
	 * 新建、编辑子员工 返回 CODE
	 */
	public static class UserResult{
		public final static Integer SUCCESS = 200;// 成功
		public final static Integer NACCOUTN = 301;//账户名称为空
		public final static Integer NPWD = 302;//密码为空
		public final static Integer NCOMPANY_NAME = 303;//经销商名称为空
		public final static Integer NNAME = 304;//姓名为空
		public final static Integer NPHONE = 305;//手机为空
		public final static Integer NPOSITION = 306;//岗位为空
		public final static Integer NID = 307;//修改无主键
		public final static Integer ERROR = 501;//插入数据失败
		public final static Integer ERROR_PAMMER = 502;//参数错误
		public final static Integer ALEADY_ACCOUNT = 601;//账户已存在
		public final static Integer ALEADY_EMAIL = 602;//邮箱已存在
		public final static Integer ALEADY_PHONE = 603;//手机已存在
		public final static Integer ALEADY_JOBNUMBER = 604;//工号重复
		public final static Integer REG_PHONE = 701;//手机格式不正确
		public final static Integer REG_EMAIL = 702;//邮箱格式不正确
		public final static Integer COUNT_ACCOUNT = 801;//经销商名下子员工已到最大数量
		public final static Integer EMPLOYEE_ACCOUNT = 802;//此员工不是当前经销商名下员工
		public final static Integer NO_POWER = 803;//无权限
	}
	/**
	 * 车辆状态
	 * @author whz
	 *
	 * 2017年5月24日
	 */
	public static class ProductCarStatus{
		public final static Integer AVAILABLE = 0;//可用
		public final static Integer DISABLE = 1;//禁用
		public final static Integer SHIPPED = 2;//已发货
	}
	
	public static class deliveryMode{
		public final static String EXTRACT = "0";//自提
		public final static String DISTRIBUTION = "1";//配送
	}
	
	/**
	 * 新增提车地址反馈
	 * @author devx
	 *
	 */
	public static class feedback{
		public final static Integer SUCCESS = 0;
		public final static Integer FAIL = 1;
		public final static Integer REPEAT = 2;//重复
		
	}

	public static class BigDecimalCommon {
		public static final BigDecimal ZERO = new BigDecimal(0);
		public static final BigDecimal YEAR = new BigDecimal(360);
		public static final BigDecimal PROPORTION = new BigDecimal(100);
		public static final BigDecimal THOUSAND = new BigDecimal(1000);
	}
	
	/**
	 * sys配置状态
	 * @author devx
	 *
	 */
	public static class ConfigureType{
		public static final int OPTIONALCONFIG = 1;//可选配置
	}
	
	/*
	 * ftp路径
	 */
	public static class FtpPath{
		public static final String CARLIST="/carlist";//车辆基本信息
		public static final String ORDERPRE="/orderPre";
		public static final String ORDER="/order";
		public static final String ORDERPAY="/orderPay";
		public static final String CARLICENSE="/carLicense";
		public static final String ORDERFINISH="/orderFinish";
		public static final String UPLOAD="/upload";
		public static final String TMP="/tmp";
		public static final String RESULT="/result";
	}
	
	/**
	 * 经销商角色
	 * @author devx
	 *
	 */
	public static class DistributorRole{
		public static final String DISTRIBUTOR = "经销商";//经销商
		public static final String TREASURER = "财务员";//财务
		public static final String SALESPERSON = "销售员";//销售
		public static final String SUPPLIER = "供应商权限";//供应商权限
		public static final String PERSONAL = "个人用户权限";//供应商权限
	}
	
	
	public static class ProductTempStatus{
		public final static Integer NEW = 0;//导入
		public final static Integer INSERTR = 1;//入库
		public final static Integer FAIL = 2;//失败
	}
	
	/**
	 * 车架号关联 操作状态
	 * @author devx
	 *
	 */
	public static class ProductsTempCarStatus{
		public final static Integer AVAILABLE = 0;//可用
		public final static Integer DISABLE = 1;//禁用
	}
	
	/**
	 * 车辆价格区间
	 * @author devx
	 *
	 */
	public static class CarPrice{
		public final static Integer FIVE_HUNDRED_THOUSAND = 500000;//50W
		public final static Integer ONE_MILLION = 1000000;//100W
		public final static Integer ONE_MILLION_FIVE_HUNDRED  = 1500000;//150W
	}
	
	/**
	 * 价格区分
	 * @author devx
	 *
	 */
	public static class PriceRange{
		public final static Integer FIVE_HUNDRED_THOUSAND = 0;//50W以下
		public final static Integer ONE_MILLION = 1;//100W以下
		public final static Integer ONE_MILLION_FIVE_HUNDRED  = 2;//150W以下
		public final static Integer EXCEED  = 3;//150W以上
	}
	
	/**
	 * 创建订单错误类型
	 * @author devx
	 *
	 */
	public static class OrderErrorType{
		public final static String USER_DOES_NOT_EXIST = "1";//用户不存在
		public final static String THE_ADDRESS_NOT_PRESENT = "2";//提车地址不存在
		public final static String DELIVERY_MODE_IS_INCORRECT = "3";//配送方式有误
		public final static String NO_FRAME_NUMBER = "4";//没有车架号
		public final static String FRAME_NUMBER_BINDING_FAILED = "5";//车架号绑定失败
		public final static String PRODUCT_SHELF = "6";//产品已经下架
		public final static String ROLE_PROHIBIT = "7";//角色禁用
		public final static String INSURANCE_ERROR = "8";//保险信息有误
		public final static String INSUFFICIENT_STOCK = "9";//库存不足
	}
	/**
	 * 发给经销商财务短信内容
	 * @author whz
	 *
	 * 2017年6月8日
	 */
	public static class MsgToFinanceOrOperation{
		public final static String DEPOSITCHECKNOTPASS = "上传的定金支付凭证有误，请重新上传";
		public final static String DEPOSITCHECKPASS = "定金审核已通过，车辆已发出，请尽快支付尾款并上传支付凭证";
		public final static String TAILMONEYCHECKNOTPASS = "上传的尾款支付凭证有误，请重新上传";
		public final static String TAILMONEYCHECKPASS = "尾款审核已通过，车辆已在运输途中";
		//public final static String ORDERWAITCHECK = "经销商已取消，请及时审核"; //无用
		public final static String DEPOSITWAITFORCHECK = "经销商已上传定金支付凭证，请及时审核";
		public final static String TAILMONEYWAITFORCHECK = "经销商已上传尾款支付凭证，请及时审核";
		public final static String ORDERCREATION = "已经创建成功，请在48小时内支付定金并上传支付凭证";
		public final static String ORDERCANCELNOTPASS = "取消失败，详细原因请登录后前往订单详情页查看";
		public final static String ORDERCANCELSUCCESS = "已取消成功";
	}
	
	/**
	 * 车辆配送方式
	 * @author devx
	 *
	 */
	public static  class ExtractionMethod{
		public final static int DISTRIBUTION = 0;//配送
		public final static int SINCE = 1;//自提
		
	}
	/**
	 * 订单意外情况邮件通知列表
	 */
	public static final String[] emailList = {
		"liuzikun@nahong.com.cn",
		"chenjun@nahong.com.cn",
		"chenxinzhe@nahong.com.cn",
		"gongrong@nahong.com.cn"
	};
	
	/**
	 * 子员工强制修改密码返回CODE
	 */
	public static  class  RestPwd{
		public final static Integer SUCCESS = 200;//修改成功
		public final static Integer SUCCESS_JUMP = 201;//成功并且跳转至经销商页面
		public final static Integer ERROR = 500;//用户未登录或无ID
		public final static Integer NPWD = 501;//密码为空
		public final static Integer FADMIN = 601;//只有子员工才能修改密码
	}
	
	/**
	 * 个人等级:0个人,1销售,2经销商
	 */
	public static  class  SysLevel{
		public final static Integer PERSONAL = 0;
		public final static Integer SELLER = 1;
		public final static Integer BOSS = 2;
	}
	
	/**
	 * 0:经销商,1:供应商,2:个人
	 */
	public static  class  UserType{
		public final static Integer SELLER = 0;
		public final static Integer SUPPLIER = 1;
		public final static Integer PERSONAL = 2;
	}
	
	/**
	 *0待审核,1可用,2审核不通过
	 */
	public static  class SalesStatus{
		public final static Integer WAITPASS = 0;
		public final static Integer PASS = 1;
		public final static Integer NOPASS = 2;
	}
	
	/**
	 * 0无,1邀请为个人销售,2邀请为经销商
	 */
	public static  class  MsgType{
		public final static Integer NONE = 0;
		public final static Integer LEVELUP = 1;
		public final static Integer LEVELTOP = 2;
	}
	
	/**
	 * 0未读,1已读
	 */
	public static  class  MsgStatus{
		public final static Integer NOREAD = 0;
		public final static Integer READ = 1;
	}
	
	
	
	/**
	 * 升级审核
	 */
	public static class LevelUpResult {
		public final static Integer SUCCESS = 200;// 成功
		public final static Integer ERRORLEVEL = 301;//非法数据
		public final static Integer INDOING = 401;//正在审核中
		public final static Integer HASDONE = 402;//已经通过审核
	}
	
	public static class DowngradeOrUpgrade{
		public final static String PERSONAL_UPGRADE = "1";//个人升级
		public final static String SALE_UPGRADE = "3";//销售升级
		public final static String SALE_DOWNGRADE = "2";//销售降级
		public final static String DISTRIBUTOR_DOWNGRADE = "4";//经销商降级
	}
	
	public static class DowngradeOrUpgradeScRemark{
		public final static String PERSONAL_UPGRADE = "个人升级配置";//个人升级
		public final static String SALE_UPGRADE = "个人销售升级配置";//销售升级
		public final static String SALE_DOWNGRADE = "个人销售降级配置";//销售降级
		public final static String DISTRIBUTOR_DOWNGRADE = "个人经销商降级配置";//经销商降级
	}
	/**
	 * 关系类型 0全部,1:"直接下级",2:"间接下级",3:"直接上级",4:"间接上级"
	 */
	public static class RecommderType{
		public final static Integer ALL = 0;
		public final static Integer SON = 1;
		public final static Integer GRANDSON = 2;
		public final static Integer FATHER = 3;
		public final static Integer GRANDFATHER = 4;
	}
	/**
	 * 手机端
	 */
	public static class phoneIndex{
		public final static String BANNER = "banner";//轮播图
		public final static String PRODUCTSHOT = "productsHot";//热门推荐
		public final static String PRODUCTSLIST = "productsList";//精选推荐
	}
	
	/**
	 * 是否为自营
	 * @author devx
	 */
	public static class SelfSupporting{
		public final static Integer YES = 0;//不是自营
		public final static Integer No = 1;//是自营
	}
	
	/**
	 * 用户角色
	 */
	public static class UserPosition {
		public final static Integer ADMIN = 0;//管理员
		public final static Integer SALE = 1;//销售
		public final static Integer FINANCE = 2;//财务
		public final static Integer SUPPLIER = 3;//供应商
		public final static Integer PERSONAL = 4;//个人
		
	}
	
		/**
	 * 保险配置group_name
	 */
	public static class Insurance{
		public final static String TYPE_ONE = "保险配置1";//管理员
		public final static String TYPE_TWO = "保险配置2";//管理员
	}
	
	/**
	 * 保险方式
	 */
	public static class InsuranceTyep{
		public final static Integer TYPE_ONE = 1;//保险配置1
		public final static Integer TYPE_TWO = 2;//保险配置2
	}
	
	/**
	 * 佣金计算结果返回code
	 */
	public static class CommissionCode {
		public final static Integer ERRORORDER = 401;//订单错误
		public final static Integer ERRORUSER = 402;//订单用户id错误
		public final static Integer ERRORCONFIG = 403;//佣金配置表错误
		public final static Integer SUCCESS = 200;//成功
		
	}
	
	/**
	 * 配置表sc_key
	 */
	public static class ConfigKey {
		public final static String SELF = "self";
		public final static String NEXT = "next";
		public final static String SECONDARY = "secondary";
		
	}
	
	
	/**
	 * 产品审核
	 * @author devx
	 *
	 */
	public static class ProductExamine{
		public final static Integer PASS = 1;//通过
		public final static Integer NOTPASS = 2;//不通过
	}
	/**
	 * 供应商保存修改产品
	 */
	public static class Product{
		public final static String CODE = "code";//KEY
		public final static String OBJ = "obj";//KEY - 对象
		public final static Integer SUCCESS = 200;//成功
		public final static Integer ERROR = 500;//失败
		public final static Integer NTITLE = 601;//标题为空
		public final static Integer NBRANDID = 602;//品牌ID为空
		public final static Integer NPOSITION = 603;//车辆状态为空
		public final static Integer NPRICERANGE = 604;//价格区间为空
		public final static Integer NCARTYPE = 605;//车辆类型为空
		public final static Integer NDEPOSIT = 606;//定金支付比例不能大于100或者小于0
		public final static Integer NFACTORYDATE = 607;//出厂日期为空
		public final static Integer NCARPRICE = 608;//裸车价格为空
		public final static Integer NMUSTCONFIGUREPRICE = 609;//必选总价为空
		public final static Integer NCHINAPRICE = 610;//国内指导价为空
		public final static Integer NSTOCK = 611;//库存为空
		public final static Integer NINDEXIMAGEH = 612;//缩略图为空
		public final static Integer NSHRINKIMAGEH = 613;//封面图为空
		public final static Integer NDETAILEDIMAGEH = 614;//详情图为空
		public final static Integer NRECOMMENDIMAGEH = 615;//推荐图为空
		public final static Integer NCONTENT = 616;//产品正文为空
		public final static Integer NFILEPRODUCT = 617;//车辆三证为空
		public final static Integer NQPRODUCT = 618;//未查到该产品
	}
	
	/**
	 * APP返回CODE
	 */
	public static class RetAppInfo{
		public final static String CODE = "code";//KEY
		public final static String OBJ = "obj";//KEY - 对象
		public final static String INSURANCE = "insurance";//车辆保险配置
		public final static String ADDRESS = "address";//提车点
		public final static String PRODUCT = "product";//车辆信息
		public final static String SUCCESS = "200";//请求成功
		public final static String FPHONE_CODE = "403";//手机验证码错误
		public final static String ERROR = "500";//插入数据失败
		public final static String NUSEROBJ = "601";//用户未登录
		public final static String NQUERYPRODUCT ="602";//"未查到该车辆信息"
		public final static String NPRODUCTID = "603";//产品ID为空
		public final static String NPRODUCTIDNUM = "604";//产品ID必须为数字
		public final static String NSINCE = "605";//(自提必传)提车地址ID
		public final static String NEXTRACTIONADDRESS = "606";//(配送必传)配送地址
		public final static String NINSURANCE = "607";//未选择保险方式
		public final static String EINSURANCE = "608";//传递保险方式不正确
		public final static String NTERM = "609";//未选在年限(贷款必传)
		public final static String EORDER = "610";//下订单失败
		
		public final static String NOLDPWD = "612";//旧密码不正确
		public final static String NPWDTWO = "613";//两次密码输入不一致
		
		
		public final static String ORDERN = "614";//未查到该订单
		public final static String NMSG = "615";//消息ID不能为空
		
		public final static String REG_PHONE = "701";//手机格式不正确
		
		public final static String NUSER = "801";//未找到该用户
		public final static String NUSERTYPE = "802";//该用户不是个人用户
		
		public final static Integer WRONGDATA = 901;//错误数据
		public final static Integer WAITID = 902;//用户证件待审核
		public final static Integer PASS = 903;//审核通过
		public final static Integer NOUSERCARD = 904;//用户证件未上传
		public final static Integer NFACADE = 905;//姓名或卡号为空
		public final static Integer NCONTENT = 906;//身份证或开户行为空
		public final static Integer NCARDFILE= 907;//身份证正面文件或银行卡照片为空
		public final static Integer NCARDBFILE= 908;//身份证反面文件
		public final static Integer ERRORTYPE= 909;//类型错误
		public final static Integer NOCARD= 910;//身份证未上传
		public final static Integer NODEBIT= 911;//借记卡未上传
		public final static Integer NOCREDIT= 912;//信用卡未上传
		
		
	}
	
	
	public static class CardType{
		public final static String CARDONE= "1";//1:身份证 
		public final static String CARDTWO= "2";//2：借记卡
		public final static String CARDTHREE= "3";// 3：信用卡
	}
	/**
	 * 供应商融资审核0提交申请,1通过,2不通过
	 */
	public static class SupplierFinanceStatus{
		public final static Integer WAITPASS = 0;
		public final static Integer PASS = 1;//通过
		public final static Integer NOTPASS = 2;//不通过
	}
	
	/**
	 * 金融方案期数
	 */
	public static class FinanceTerm{
		public final static Integer ONE = 1;
		public final static Integer TWO = 2;
		public final static Integer THERE = 3;
	}
	
	/**
	 * 个人金融方案 1不使用 2使用
	 */
	public static class IsFinance{
		public final static Integer FALSE = 1;
		public final static Integer TRUE = 2;
	}
	
	/**
	 * 权限管理所隐藏的树状图
	 * @author devx
	 *
	 */
	public static class HidderTree{
		public final static String DISTRIBUTOR = "经销商管理";
		public final static String PERSONAL = "个人管理";
		public final static String SUPPLIER = "供应商管理";
		
	}
	/**
	 * 短信
	 */
	public static class Message{
		public final static Integer TYPEONE = 1;//1:注册验证码  
		public final static Integer TYPETWO = 2;// 2：找回密码 
		public final static Integer TYPETHREE = 3;//3：修改密码
		
		public final static String TYPESONE = "Register";//1:注册验证码  
		public final static String TYPESTWO = "ForgetPwd";// 2：找回密码 
		public final static String TYPESTHREE = "EditPwd";//3：修改密码
	}
	
	
	public final static class AppVersionResponse {
		public static final String VersionLatest = "1";// 最新
		public static final String VersionUseable = "2";// 可用,但不是最新版本
		public static final String VersionBlock = "3";// 不可用
	}
	
	/**
	 * 升降级规则配置
	 * @author devx
	 *
	 */
	public final static class UpgradeDowngradeRule{
		public static final String MONTH = "month";// 考核月数
		public static final String TOTALSALES = "totalSales";// 考核总数
		public static final String TOTALAMOUNT = "totalAmount";// 考核金额
		public static final String MONTHLYSALES = "monthlySales";// 每月销售数
		public static final String MONTHLYAMOUNT = "monthlyAmount";// 每月销售额
	}
	
	
}
