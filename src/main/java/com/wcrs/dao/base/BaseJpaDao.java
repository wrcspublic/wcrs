package com.wcrs.dao.base;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wcrs.common.orm.QLBuilder;
import com.wcrs.common.orm.QueryResult;
import com.wcrs.entity.base.BaseEntity;

/**
 * 封装常用增删改查操作
 * 
 * @since 2.0 泛型T挪到方法声明出,声明对象时不需要指定泛型.
 */
@SuppressWarnings("unchecked")
@Repository
public abstract class BaseJpaDao implements IAbstractDao {

	@PersistenceContext
	private EntityManager em;

	private QLBuilder sqlBuilder = new QLBuilder();

	public void clear() {
		em.clear();
	}

	@Transactional
	public <T extends BaseEntity> void create(T entity) {
		em.persist(entity);
	}

	public <T extends BaseEntity> void createBatch(List<T> entitys) {
		for (T entity : entitys) {
			create(entity);
		}
	}

	@Transactional
	public <T extends BaseEntity> void update(T entity) {
		em.merge(entity);
	}

	@Transactional
	public <T extends BaseEntity> void saveAll(List<T> entitys) {
		for (int i = 0; i < entitys.size(); i++) {
			T entity = entitys.get(i);
			save(entity);
		}
	}

	@Transactional
	public <T extends BaseEntity> void save(T entity) {
		if (entity.getPrimaryKey() == null) {
			this.create(entity);
		} else {
			this.update(entity);
		}
	}

	@Transactional
	public <T extends BaseEntity> void delete(Class<T> entityClass,
			Object entityid) {
		delete(entityClass, new Object[] { entityid });
	}

	@Transactional
	public <T extends BaseEntity> void delete(Class<T> entityClass,
			Object[] entityids) {
		// StringBuffer sf_QL = new StringBuffer(" DELETE FROM ").append(
		// sqlBuilder.getEntityName(entityClass)).append(" o WHERE ")
		// .append(sqlBuilder.getPkField(entityClass, "o")).append("=? ");
		// Query query = em.createQuery(sf_QL.toString());
		for (Object id : entityids) {
			em.remove(em.find(entityClass, id));
			// query.setParameter(1, id).executeUpdate();
		}
	}

	@Transactional
	public <T extends BaseEntity> void deleteByWhere(Class<T> entityClass,
			String where, Object[] delParams) {
		StringBuffer sf_QL = new StringBuffer("DELETE FROM ").append(
				sqlBuilder.getEntityName(entityClass)).append(" o WHERE 1=1 ");
		if (where != null && where.length() != 0) {
			sf_QL.append(" AND ").append(where);
		}
		Query query = em.createQuery(sf_QL.toString());
		this.setQueryParams(query, delParams);

		query.executeUpdate();
	}

	public <T extends BaseEntity> T find(Class<T> entityClass, Object entityId) {
		return em.find(entityClass, entityId);
	}

	public <T extends BaseEntity> long getCount(Class<T> entityClass) {
		return getCountByWhere(entityClass, null, null);
	}

	public <T extends BaseEntity> long getCountByWhere(Class<T> entityClass,
			String whereql, Object[] queryParams) {
		StringBuffer sf_QL = new StringBuffer("SELECT COUNT(")
				.append(sqlBuilder.getPkField(entityClass, "o"))
				.append(") FROM ")
				.append(sqlBuilder.getEntityName(entityClass))
				.append(" o WHERE 1=1 ");
		if (whereql != null && whereql.length() != 0) {
			sf_QL.append(" AND ").append(whereql);
		}
		Query query = em.createQuery(sf_QL.toString());
		this.setQueryParams(query, queryParams);
		return (Long) query.getSingleResult();
	}

	public <T extends BaseEntity> boolean isExistedByWhere(
			Class<T> entityClass, String whereql, Object[] queryParams) {
		long count = getCountByWhere(entityClass, whereql, queryParams);
		return count > 0 ? true : false;
	}

	public <T extends BaseEntity> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return scroll(entityClass, firstindex, maxresult, wherejpql,
				queryParams, orderby);
	}

	public <T extends BaseEntity> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, List<Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		Object[] ps = null;
		if (queryParams != null) {
			ps = queryParams.toArray();
		}
		return getScrollData(entityClass, firstindex, maxresult, wherejpql, ps,
				orderby);
	}

	public <T extends BaseEntity> QueryResult<T> getScrollData(
			Class<T> entityClass, int firstindex, int maxresult,
			String wherejpql, Map<String, Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		return scroll(entityClass, firstindex, maxresult, wherejpql,
				queryParams, orderby);
	}

	/**
	 * 根据条件查询某个实体的列表
	 * 
	 * @author slx
	 * @param <T>
	 * @param entityClass
	 *            实体类型
	 * @param firstindex
	 *            开始行
	 * @param maxresult
	 *            结束行
	 * @param wherejpql
	 *            where条件
	 * @param queryParams
	 *            参数
	 * @param orderby
	 *            排序条件
	 * @return
	 */
	private <T extends BaseEntity> QueryResult<T> scroll(Class<T> entityClass,
			int firstindex, int maxresult, String wherejpql,
			Object queryParams, LinkedHashMap<String, String> orderby) {
		QueryResult<T> qr = new QueryResult<T>();
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = em.createQuery("SELECT o FROM " + entityname + " o "
				+ (StringUtils.isEmpty(wherejpql) ? "" : "WHERE " + wherejpql)
				+ sqlBuilder.buildOrderby(orderby));
		setQueryParams(query, queryParams);
		if (firstindex != -1 && maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult)
					.setHint("org.hibernate.cacheable", true);
		qr.setResultlist(query.getResultList());
		query = em.createQuery("SELECT COUNT("
				+ sqlBuilder.getPkField(entityClass, "o") + ") FROM "
				+ entityname + " o "
				+ (StringUtils.isEmpty(wherejpql) ? "" : "WHERE " + wherejpql));
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.getSingleResult());
		return qr;
	}

	/**
	 * 根据条件查询实体指定字段的值并回填到实体内. <br/>
	 * <b>注意:</b> <br/>
	 * 实体必须有包括要查询的字段为参数的构造函数.
	 * 
	 * @param <T>
	 * @param entityClass
	 * @param queryfields
	 * @param firstindex
	 * @param maxresult
	 * @param wherejpql
	 * @param queryParams
	 * @param orderby
	 * @return
	 */
	private <T extends BaseEntity> QueryResult<T> scroll(Class<T> entityClass,
			String[] queryfields, int firstindex, int maxresult,
			String wherejpql, Object queryParams,
			LinkedHashMap<String, String> orderby) {
		QueryResult<T> qr = new QueryResult<T>();
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = em
				.createQuery((sqlBuilder.buildSelect(entityname, queryfields,
						"o")
						+ "FROM "
						+ entityname
						+ " o "
						+ (StringUtils.isEmpty(wherejpql) ? "" : "WHERE "
								+ wherejpql) + sqlBuilder.buildOrderby(orderby)));
		setQueryParams(query, queryParams);
		if (firstindex != -1 && maxresult != -1)
			query.setFirstResult(firstindex).setMaxResults(maxresult)
					.setHint("org.hibernate.cacheable", true);
		qr.setResultlist(query.getResultList());
		query = em.createQuery("SELECT COUNT("
				+ sqlBuilder.getPkField(entityClass, "o") + ") FROM "
				+ entityname + " o "
				+ (StringUtils.isEmpty(wherejpql) ? "" : "WHERE " + wherejpql));
		setQueryParams(query, queryParams);
		qr.setTotalrecord((Long) query.getSingleResult());
		return qr;
	}

	public <T extends BaseEntity> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, List<Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		return this.scroll(entityClass, queryfields, firstindex, maxresult,
				wherejpql, queryParams, orderby);
	}

	public <T extends BaseEntity> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, Map<String, Object> queryParams,
			LinkedHashMap<String, String> orderby) {
		return this.scroll(entityClass, queryfields, firstindex, maxresult,
				wherejpql, queryParams, orderby);
	}

	public <T extends BaseEntity> QueryResult<T> getScrollData(
			Class<T> entityClass, String[] queryfields, int firstindex,
			int maxresult, String wherejpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby) {
		return this.scroll(entityClass, queryfields, firstindex, maxresult,
				wherejpql, queryParams, orderby);
	}

	protected void setQueryParams(Query query, Object queryParams) {
		sqlBuilder.setQueryParams(query, queryParams);
	}

	public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = em.createQuery("SELECT o FROM "
				+ entityname
				+ " o "
				+ ((wheresql == null || wheresql.length() == 0) ? "" : "WHERE "
						+ wheresql));
		setQueryParams(query, queryParams);
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
			String wheresql, Object[] queryParams, int startRow, int rows) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = em.createQuery("SELECT o FROM "
				+ entityname
				+ " o "
				+ ((wheresql == null || wheresql.length() == 0) ? "" : "WHERE "
						+ wheresql));
		setQueryParams(query, queryParams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		query.setHint("org.hibernate.cacheable", true);
		return query.getResultList();
	}

	public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
			String[] queryfields, String wheresql, Object[] queryParams) {
		return queryByWhere(entityClass, queryfields, wheresql, queryParams,
				-1, -1);
	}

	public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
			String[] queryfields, String wheresql, Object[] queryParams,
			int startRow, int rows) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = em.createQuery(sqlBuilder.buildSelect(entityname,
				queryfields, "o")
				+ " FROM "
				+ entityname
				+ " o "
				+ (wheresql == null ? "" : "WHERE " + wheresql));
		setQueryParams(query, queryParams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		return query.getResultList();
	}

	public <T extends BaseEntity> List<Object[]> queryFieldValues(
			Class<T> entityClass, String[] queryfields, String wheresql,
			Object[] queryParams) {
		return queryFieldValues(entityClass, queryfields, wheresql,
				queryParams, -1, -1);
	}

	public <T extends BaseEntity> List<Object[]> queryFieldValues(
			Class<T> entityClass, String[] queryfields, String wheresql,
			Object[] queryParams, int startRow, int rows) {
		String entityname = sqlBuilder.getEntityName(entityClass);
		Query query = em.createQuery(sqlBuilder.buildSelect(queryfields, "o")
				+ " FROM " + entityname + " o "
				+ (wheresql == null ? "" : "WHERE " + wheresql));
		setQueryParams(query, queryParams);
		if (startRow >= 0) {
			query.setFirstResult(startRow);
		}
		if (rows > 0) {
			query.setMaxResults(rows);
		}
		return query.getResultList();
	}

	/**
	 * 设置查询参数
	 * 
	 * @author slx
	 * @date 2009-7-8 上午10:02:55
	 * @modifyNote
	 * @param query
	 *            查询
	 * @param queryParams
	 *            查询参数
	 */
	protected void setQueryParams(Query query, Object[] queryParams) {
		sqlBuilder.setQueryParams(query, queryParams);
	}

	public <T extends BaseEntity> T load(Class<T> entityClass, Object entityId) {
		try {
			return em.getReference(entityClass, entityId);
		} catch (Exception e) {
			return null;
		}
	}

	public <T extends BaseEntity> T findByWhere(Class<T> entityClass,
			String where, Object[] params) {
		List<T> l = queryByWhere(entityClass, where, params);
		if (l != null && l.size() == 1) {
			return l.get(0);
		} else if (l.size() > 1) {
			throw new RuntimeException("查寻到的结果不止一个.");
		} else {
			return null;
		}
	}

}