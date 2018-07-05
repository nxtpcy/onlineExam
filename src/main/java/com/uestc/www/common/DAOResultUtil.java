package com.uestc.www.common;

import com.uestc.www.common.StatusType;

public class DAOResultUtil {
	private DAOResultUtil(){
		
	}
	/**
	 * 根据数据库返回的行数判断成功与否
	 * @param operateRows 数据库返回的行数
	 * @param standardRows 判断成功与否的行数
	 * @return
	 */
	public static StatusType getAddUpDateRemoveResult(int operateRows,int standardRows){
		if(operateRows > standardRows){
			return StatusType.SUCCESS;
		}
		else{
			return StatusType.ERROR;
		}
	}
	public static StatusType getBatchResult(int operateRows,int standardRows){
		if(operateRows == standardRows){
			return StatusType.SUCCESS;
		}
		else{
			return StatusType.ERROR;
		}
	}
}
