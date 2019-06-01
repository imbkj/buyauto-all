package com.buyauto.util.method;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 
 * @Title:后台权限
 * @Package com.phoenix.util.core.tool
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xujinyang
 * @date 2015年4月9日 下午6:20:43
 * @version phoenix 1.0
 */
public enum AuthBackend {	
	
	/*******************平行进口车项目*******************************/
		/*******************车辆销售***********************/
		PURCHASE(6301),
		/*******************我的订单***********************/
		MYORDER(6302),
		/*******************帐房操作***********************/
		ORDEROPERATION(6201),
		/*******************员工操作***********************/
		PERSONOPERATION(6102),
		/*******************经销商后台操作*******************/
		DISTRIBUTOROPERATION(6101),
		/*******************订单管理************************/
		ORDERMANAGEMENT(6303),
   /***********************经销商结束*******************************/	
		
   /************************backend*******************************/
	 /************************商品管理***************************/
		/*******************商品列表***********************/
		PRODUCTLIST(1101),
		/*******************草稿箱*************************/
		DRAFTS(1201),
		/*******************回收站*************************/
		RECYCLEBIN(1301),
		
	 /************************配置管理***************************/
		/*******************提车点配置***********************/
		ADDRESSLIST(2101),
		/*******************品牌配置*************************/
		BRANDLIST(2201),
		/*******************抽佣规则配置*************************/
		COMMISSIONCONFIG(2301),
		/*******************用户升降级*************************/
		RELEGATION(2401),
		
		
		
	/************************系统功能***************************/
		/*******************用户管理***********************/
		USERMANAGEMENT(3101),
		/*******************角色管理*************************/
		ROLEMANAGEMENT(3201),	
		
	/************************CMS管理***************************/
		/*******************banner管理***********************/
		BANNERMANAGEMENT(4101),
		/*******************新闻管理**************************/
		JOURNALISMMANAGEMENT(4201),	
		
	/************************订单管理***************************/
		/*******************订单列表***********************/
		ORDERLIST(5101),
	
	/************************审核管理***************************/
		/*******************审核列表***********************/
		AUDITLIST(7101),	
		/*******************经销商管理***********************/
		Distributor(7201),
		/*******************供应商管理***********************/
		SUPPLIER(7301),
		/*******************供应商金融管理***********************/
		SUPPLIERFINANCE(7401),
		/*******************个人金融管理***********************/
		PERSONALFINANCE(7501),
		/*******************个人管理***********************/
		PERSONAL(7601),
	/************************首页管理***************************/
		/*******************数据概览***********************/
		DATAOVERVIEW(8101),	
	/************************供应商管理***************************/
		/*******************供应商个人中心（商品列表）*****************/
		SUPPLIERPRODUCTLIST(10001),
		/******************供应商我的订单**********************/
		SUPPLIERMYORDER(10021),
		/*******************金融方案**********************/
		SUPPLIERPROGRAMME(10011),
		/*******************上传三证**********************/
		SUPPLIERUPLOADCERTIFICATE(10022),
		/*******************商品列表**********************/
		SUPPLIERPRODUCTLISTS(10023),
		/*******************商品新增**********************/
		SUPPLIERNEWMERCHANDISE(10024),
		/*******************商品修改**********************/
		SUPPLIERCOMMODITYMODIFICATION(10026),
		/*******************商品草稿箱**********************/
		SUPPLIERDRAFTS(10025),
		/*******************商品回收站**********************/
		SUPPLIERECYCLEBIN(10027),
		/*******************商品详情预览**********************/
		SUPPLIERPREVIEW(10028),
	/************************个人管理***************************/
		/*******************个人中心（我的订单）*****************/
		PERSONALORDER(9101),
		/*******************返佣信息*****************/
		PERSONALCOMMISSION(9102),
		/*******************我的等级*****************/
		PERSONALGRADE(9103),
		/*******************我的消息中心*****************/
		PERSONALMESSAGECENTER(9104),
		/*******************我的接收邀请*****************/
		PERSONALINVITATION(9105),
		
	/************************************************文档结束*******************************************************/	
		
	;
	/**
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300001', '理财师新增', '理财师新增', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300002', '理财师列表', '理财师列表', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300003', '启用与停用', '启用与停用', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300004', '理财师编辑', '理财师编辑', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300005', '解绑用户', '解绑用户', '0', NULL, NULL, NULL, '1', '1', '6');
	*/

	long value;

	AuthBackend(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(
				new FileReader(
						"E:\\WorkSpace\\association-all\\association-util-root\\association-util-core\\src\\main\\java\\com\\association\\util\\core\\asenum\\AuthBackend.java"));
		String line;
		int i = 1;
		int i2 = 1;
		String m = "1";
		while ((line = br.readLine()) != null) {
			if(line.contains("文档结束")){
				return;
			}
			if (line.contains("//m")) {
				String nextLine = br.readLine();
				m = nextLine.substring(nextLine.indexOf("(") + 1, nextLine.indexOf(")"));
				System.out
						.println("INSERT INTO `association_sh`.`sys_rule_moudle` (`id`, `top_num`, `moudle_father`, `moudle_name`, `moudle_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`) VALUES ('"
								+ m + "', '" + i2++ + "', NULL, 'AAAAA', 'AAAAA', '0', NULL, NULL, NULL, '1');");
			} else if (line.contains("//")) {
				String nextLine = br.readLine();
				String name = line.replace("//", "").trim();
				String id = nextLine.substring(nextLine.indexOf("(") + 1, nextLine.indexOf(")"));
				System.out.println("INSERT INTO `association_sh`.`sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('"+id+"', '"+name+"', '"+name+"', '0', NULL, NULL, NULL, '1', '"+m+"', '"+i+++"');");
			}
		}
	}
}
