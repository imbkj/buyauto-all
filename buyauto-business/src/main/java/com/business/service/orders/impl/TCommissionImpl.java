package com.business.service.orders.impl;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.service.orders.api.ITCommissionApi;
import com.buyauto.entity.orders.TOrders;
import com.buyauto.entity.orders.TOrdersCommission;
import com.buyauto.entity.sys.SysConfig;
import com.buyauto.entity.user.TUsers;
import com.buyauto.entity.user.TUsersRecommender;
import com.buyauto.mapper.orders.TOrdersCommissionMapper;
import com.buyauto.mapper.orders.TOrdersMapper;
import com.buyauto.mapper.sys.SysConfigMapper;
import com.buyauto.mapper.user.TUsersMapper;
import com.buyauto.mapper.user.TUsersRecommenderMapper;
import com.buyauto.util.method.CommonCode;
import com.buyauto.util.method.KeyUtil;

@Service
public class TCommissionImpl implements ITCommissionApi {

	private static final Logger logger = LoggerFactory.getLogger(TCommissionImpl.class);

	@Autowired
	private TOrdersMapper ordersMapper;
	@Autowired
	private TUsersRecommenderMapper tUsersRecommenderMapper;
	@Autowired
	private TUsersMapper tUsersMapper;
	@Autowired
	private SysConfigMapper sysConfigMapper;
	@Autowired
	private TOrdersCommissionMapper tOrdersCommissionMapper;

	/**
	 * @Title: commCalculate
	 * @Description: 计算佣金
	 * @param orderId
	 * @return Integer    返回类型
	 */
	@Override
	public Integer commCalculate(String orderId) {
		TOrders tOrders = ordersMapper.selectByPrimaryKey(Long.valueOf(orderId));

		// 检查订单数据有效性
		if (tOrders == null || tOrders.getStatus() != CommonCode.SaveStatus.COMPLETE || tOrders.getUserId() == null
				|| tOrders.getCarPrice() == null) {
			return CommonCode.CommissionCode.ERRORORDER;
		}
		TUsers tUsers = tUsersMapper.selectByPrimaryKey(tOrders.getUserId());
		if (tUsers != null && tUsers.getRealLevel() != null) {
			String Level = "";
			BigDecimal comm_personal = new BigDecimal(0);
			BigDecimal comm_father = new BigDecimal(0);
			BigDecimal comm_grandpa = new BigDecimal(0);
			SysConfig sysConfig = new SysConfig();

			// 计算本人佣金
			if (tUsers.getRealLevel() == CommonCode.SysLevel.SELLER) {
				Level = CommonCode.ConfigKey.SELF;
				sysConfig = sysConfigMapper.queryByGroupNameAndKey(CommonCode.ConfigGroup.COMMISSION, Level);
				if (sysConfig == null || sysConfig.getScValue() == null) {
					return CommonCode.CommissionCode.ERRORCONFIG;
				}
				comm_personal = tOrders.getCarPrice()
						.multiply(new BigDecimal(sysConfig.getScValue()).divide(new BigDecimal(100)))
						.setScale(2, BigDecimal.ROUND_HALF_UP);
				TOrdersCommission personCommission = new TOrdersCommission(KeyUtil.generateDBKey(),
						Long.valueOf(orderId), tOrders.getUserId(), tUsers.getId(), null, null, null,
						tOrders.getCount(),tOrders.getCarPrice(), comm_personal, tOrders.getCompleteTime(), new Date(),
						sysConfig.getScValue());
				tOrdersCommissionMapper.insertSelective(personCommission);

				// 查询订单受益人(Father)和(Grandpa)并计算佣金
				TUsersRecommender tUsersRecommender = tUsersRecommenderMapper.getRecommenderByUid(tOrders.getUserId());
				if (tUsersRecommender.getFatherId() != null) {
					TUsers fatherUsers = tUsersMapper.selectByPrimaryKey(tUsersRecommender.getFatherId());
					if (fatherUsers != null && fatherUsers.getRealLevel() != null
							&& fatherUsers.getRealLevel() == CommonCode.SysLevel.SELLER) {
						Level = CommonCode.ConfigKey.NEXT;
						sysConfig = sysConfigMapper.queryByGroupNameAndKey(CommonCode.ConfigGroup.COMMISSION, Level);
						if (sysConfig == null || sysConfig.getScValue() == null) {
							return CommonCode.CommissionCode.ERRORCONFIG;
						}
						comm_father = tOrders.getCarPrice()
								.multiply(new BigDecimal(sysConfig.getScValue()).divide(new BigDecimal(100)))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
						TOrdersCommission fatherCommission = new TOrdersCommission(KeyUtil.generateDBKey(),
								Long.valueOf(orderId), tOrders.getUserId(), fatherUsers.getId(), null, null, null,
								tOrders.getCount(), tOrders.getCarPrice(), comm_father, tOrders.getCompleteTime(),
								new Date(), sysConfig.getScValue());
						tOrdersCommissionMapper.insertSelective(fatherCommission);
					}
				}
				if (tUsersRecommender.getGrandpaId() != null) {
					TUsers grandpaUsers = tUsersMapper.selectByPrimaryKey(tUsersRecommender.getGrandpaId());
					if (grandpaUsers != null && grandpaUsers.getRealLevel() != null
							&& grandpaUsers.getRealLevel() == CommonCode.SysLevel.SELLER) {
						Level = CommonCode.ConfigKey.SECONDARY;
						sysConfig = sysConfigMapper.queryByGroupNameAndKey(CommonCode.ConfigGroup.COMMISSION, Level);
						if (sysConfig == null || sysConfig.getScValue() == null) {
							return CommonCode.CommissionCode.ERRORCONFIG;
						}
						comm_grandpa = tOrders.getCarPrice()
								.multiply(new BigDecimal(sysConfig.getScValue()).divide(new BigDecimal(100)))
								.setScale(2, BigDecimal.ROUND_HALF_UP);
					}
					TOrdersCommission grandpaCommission = new TOrdersCommission(KeyUtil.generateDBKey(),
							Long.valueOf(orderId), tOrders.getUserId(), grandpaUsers.getId(), null, null, null,
							tOrders.getCount(), tOrders.getCarPrice(), comm_grandpa, tOrders.getCompleteTime(),
							new Date(), sysConfig.getScValue());
					tOrdersCommissionMapper.insertSelective(grandpaCommission);
				}
			}
		} else {
			return CommonCode.CommissionCode.ERRORUSER;
		}
		return CommonCode.CommissionCode.SUCCESS;
	}

}
