/*
 * 
 * 
 * 
 */
package com.cms.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity - 参数值
 * 
 * 
 * 
 */
public class ParameterValue implements Serializable {

	private static final long serialVersionUID = 1662250976814867003L;
	
	/** parameterValues正则 */
	public static final String PARAMETERVALUES_PATTERN="parameterValues\\[([\\d]*?)\\]";
	
	/** parameterValues.entries正则 */
	public static final String PARAMETERVALUES_ENTRIES_PATTERN="parameterValues\\[([\\d]*?)\\].entries\\[([\\d]*?)\\]";

	/** 参数组 */
	private String group;
	
	/** 排序 */
	private Integer sort;

	/** 条目 */
	private List<ParameterValue.Entry> entries = new ArrayList<ParameterValue.Entry>();

	/**
	 * 获取参数组
	 * 
	 * @return 参数组
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * 设置参数组
	 * 
	 * @param group
	 *            参数组
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * 获取条目
	 * 
	 * @return 条目
	 */
	public List<ParameterValue.Entry> getEntries() {
		return entries;
	}

	/**
	 * 设置条目
	 * 
	 * @param entries
	 *            条目
	 */
	public void setEntries(List<ParameterValue.Entry> entries) {
		this.entries = entries;
	}
	
	/**
	 * 获取排序
	 * 
	 * @return 排序
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 设置排序
	 * 
	 * @param sort
	 *           排序
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	
	/**
	 * Entity - 条目
	 * 
	 * 
	 * 
	 */
	public static class Entry implements Serializable {

		private static final long serialVersionUID = 6064272969056384535L;

		/** 名称 */
		private String name;

		/** 值 */
		private String value;
		
		/** 排序 */
		private Integer sort;

		/**
		 * 获取名称
		 * 
		 * @return 名称
		 */
		public String getName() {
			return name;
		}

		/**
		 * 设置名称
		 * 
		 * @param name
		 *            名称
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * 获取值
		 * 
		 * @return 值
		 */
		public String getValue() {
			return value;
		}

		/**
		 * 设置值
		 * 
		 * @param value
		 *            值
		 */
		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * 获取排序
		 * 
		 * @return 排序
		 */
		public Integer getSort() {
			return sort;
		}

		/**
		 * 设置排序
		 * 
		 * @param sort
		 *           排序
		 */
		public void setSort(Integer sort) {
			this.sort = sort;
		}

		
	}

}
