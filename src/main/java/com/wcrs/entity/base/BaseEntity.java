package com.wcrs.entity.base;

import java.io.Serializable;
import java.util.List;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import com.wcrs.common.util.BeanUtility;

/**
 * 基础实体Bean，包含系统表中公共的字段 <br>
 * 重写 toString() clone() equals() hashCode()
 * 
 * @author yongtree
 * @date 2009-4-29 下午01:43:42
 * @version 1.0
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1962905939086138888L;

	private transient BeanUtility eoutil;

	protected boolean selected;

	@Transient
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Transient
	protected BeanUtility getBeanUtility() {
		if (eoutil == null) {
			eoutil = new BeanUtility(this);
		}
		return eoutil;
	}

	@Override
	public String toString() {
		return getBeanUtility().beanToString();
	}

	@Override
	public boolean equals(Object obj) {
		return getBeanUtility().equalsBean(obj);
	}

	@Override
	public int hashCode() {
		return getBeanUtility().hashCodeBean();
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Object obj = null;
		try {
			obj = getBeanUtility().cloneBean();
		} catch (Exception e) {
			throw new CloneNotSupportedException(e.getMessage());
		}

		return obj;
	}

	/**
	 * 得到所有可持久化字段的名称
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:59:34
	 * @modifyNote
	 * @return 名称列表
	 */
	@Transient
	public String[] getAttributeNames() {
		return getBeanUtility().getAttributeNames();
	}

	/**
	 * 得到某个字段的值
	 * 
	 * @author slx
	 * @date 2009-7-17 上午08:59:58
	 * @modifyNote
	 * @param attributeName
	 *            字段名
	 * @return 值
	 */
	@Transient
	public Object getAttributeValue(String attributeName) {
		return getBeanUtility().getAttributeValue(attributeName);
	}

	/**
	 * 设置某个字段的值
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:00:26
	 * @modifyNote
	 * @param attributeName
	 *            字段名
	 * @param value
	 *            值
	 */
	@Transient
	public void setAttributeValue(String attributeName, Object value) {
		getBeanUtility().setAttributeValue(attributeName, value);
	}

	@SuppressWarnings("static-access")
	@Transient
	public String getEnumDescription(String enumAttributeName) {
		Object value = getAttributeValue(enumAttributeName);

		return getBeanUtility().getEnumDescription(value);
	}

	/**
	 * 获得实体对应的表名
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:00:57
	 * @modifyNote
	 * @return
	 */
	@Transient
	public String getTableName() {
		return getBeanUtility().getTableName();
	}

	/**
	 * 比较此对象与另一个对象的差别，并返回值不同的字段的名称。
	 * 
	 * @author slx
	 * @date 2009-7-17 上午09:34:39
	 * @modifyNote
	 * @param antherBean
	 *            将要比较的对象
	 * @return 值不同的字段名
	 */
	@Transient
	public List<String> getDifferentField(BaseEntity anotherBean) {
		return getBeanUtility().getDifferentField(anotherBean);
	}

	/**
	 * 获取主键值
	 * 
	 * @author slx
	 * @date 2009-6-12 上午09:15:11
	 * @modifyNote
	 * @return 主键值
	 */
	@Transient
	public abstract Object getPrimaryKey();

	/**
	 * 比较主键值是否相同
	 * 
	 * @author yongtree
	 * @date 2009-9-15 下午04:09:21
	 * @modifyNote
	 * @param obj
	 * @return
	 */
	@Transient
	public boolean equalsPK(Object obj) {
		if (obj == null)// 对象为空不比较
			return false;
		// 类型不同不必进行比较
		if (!this.getClass().equals(obj.getClass())) {
			return false;
		}

		// 不是BaseEntity，不必比较
		if (!(obj instanceof BaseEntity)) {
			return false;
		}

		BaseEntity eo = (BaseEntity) obj;

		if (getPrimaryKey() != null && eo.getPrimaryKey() != null) {
			if (getPrimaryKey().equals(eo.getPrimaryKey()))
				return true;
			return false;
		} else {
			return false;
		}

	}

	/**
	 * 拷贝另一个eo对象中的字段值到当前对象中
	 * 
	 * @author slx
	 * @date 2009-12-6 上午11:04:49
	 * @modifyNote
	 * @param fromEO
	 *            从哪里拷贝
	 * @param copyAttributes
	 *            拷贝哪些字段
	 */
	public void copyAttributeValue(BaseEntity fromEO, String[] copyAttributes) {
		if (copyAttributes == null)
			return;

		for (String attr : copyAttributes) {
			this.setAttributeValue(attr, fromEO.getAttributeValue(attr));
		}
	}

	/**
	 * 加载所有延迟加载字段
	 * 
	 * @modifyNote
	 */
	public void loadLazyAttributes() {
		getBeanUtility().loadLazyField();
	}

}