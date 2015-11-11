package com.wcrs.dao.base;
 
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.wcrs.common.orm.QueryResult;
import com.wcrs.entity.base.BaseEntity;
 
/**
 * 底层数据操作的工具类
 * 
 */
public interface IAbstractDao {
 
    /**
     * 清除一级缓存的数据
     */
    public void clear();
 
    /**
     * 新增实体
     * 
     * @param entity
     *            实体
     */
    public <T extends BaseEntity> void create(T entity);
 
    /**
     * 批量新增实体
     * 
     * @param entitys
     *            实体列表
     */
    public <T extends BaseEntity> void createBatch(List<T> entitys);
 
    /**
     * 更新实体
     * 
     * @param entity
     *            实体
     */
    public <T extends BaseEntity> void update(T entity);
 
    /**
     * 删除实体
     * 
     * @param entityClass
     *            实体类
     * @param entityid
     *            实体id
     */
    public <T extends BaseEntity> void delete(Class<T> entityClass, Object entityid);
 
    /**
     * 删除实体
     * 
     * @param entityClass
     *            实体类
     * @param entityids
     *            实体id数组
     */
    public <T extends BaseEntity> void delete(Class<T> entityClass,
            Object[] entityids);
 
    /**
     * 根据条件删除
     * 
     * @author slx
     * @date 2009-11-24 下午05:52:04
     * @modifyNote
     * @param entityClass
     * @param where
     * @param delParams
     */
    public <T extends BaseEntity> void deleteByWhere(Class<T> entityClass,
            String where, Object[] delParams);
 
    /**
     * 获取实体
     * 
     * @param <T>
     * @param entityClass
     *            实体类
     * @param entityId
     *            实体id
     * @return
     */
    public <T extends BaseEntity> T find(Class<T> entityClass, Object entityId);
     
    /**
     * 根据where条件查询单个对象
     * @author slx
     * @date 2010-7-19 上午10:33:20
     * @modifyNote
     * @param <T>
     * @param entityClass
     *          类型
     * @param where
     *          条件
     * @param params
     *          参数
     * @return
     */
    public <T extends BaseEntity> T findByWhere(Class<T> entityClass, String where ,Object[] params);
     
 
    /**
     * 获取实体，具有延迟加载的作用（和find相比）
     * 
     * @param <T>
     * @param entityClass
     *            实体类
     * @param entityId
     *            实体id
     * @return
     */
    public <T extends BaseEntity> T load(Class<T> entityClass, Object entityId);
 
    /**
     * 根据条件判断实体是否存在
     * 
     * @author slx
     * @date 2009-7-8 上午11:49:13
     * @modifyNote
     * @param entityClass
     *            实体类
     * @param whereql
     *            查询条件(可空,可为 field1=? and field2=? 形式,也可为field1='value1' and
     *            field2='value2'的形式)
     * @param queryParams
     *            参数(可空，但是当条件使用了field1=? and field2=? 的形式后参数不能为空)
     * @return 是否存在
     */
    public <T extends BaseEntity> boolean isExistedByWhere(Class<T> entityClass,
            String whereql, Object[] queryParams);
 
    /**
     * 获取记录总数
     * 
     * @param entityClass
     *            实体类
     * @return
     */
    public <T extends BaseEntity> long getCount(Class<T> entityClass);
 
    /**
     * 根据条件和参数获取记录总数
     * 
     * @author slx
     * @date 2009-7-8 上午11:34:41
     * @modifyNote
     * @param <T>
     * @param entityClass
     *            实体类
     * @param whereql
     *            查询条件(可空,可为 field1=? and field2=? 形式,也可为field1='value1' and
     *            field2='value2'的形式)
     * @param queryParams
     *            参数(可空，但是当条件使用了field1=? and field2=? 的形式后参数不能为空)
     * @return 记录行数
     */
    public <T extends BaseEntity> long getCountByWhere(Class<T> entityClass,
            String whereql, Object[] queryParams);
 
    /**
     * 获取分页数据
     * 
     * @param <T>
     * @param entityClass
     *            实体类
     * @param firstindex
     *            开始索引
     * @param maxresult
     *            需要获取的记录数
     * @return
     */
    public <T extends BaseEntity> QueryResult<T> getScrollData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, Object[] queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseEntity> QueryResult<T> getScrollData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, List<Object> queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseEntity> QueryResult<T> getScrollData(
            Class<T> entityClass, int firstindex, int maxresult,
            String wherejpql, Map<String, Object> queryParams,
            LinkedHashMap<String, String> orderby);
 
    /**
     * 查询实体部分字段，获取分页数据
     * 
     * 返回的结果将重新组装到实体属性中，没有查询的字段为NULL<br>
     * 注意：使用该接口时，要确保实体类中有对应的查询字段的有参数构造方法，并且参数的顺序要和此处的queryfields数组的元素一致
     * 
     * @author yongtree
     * @date 2010-4-13 下午12:56:03
     * @modifyNote
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
    public <T extends BaseEntity> QueryResult<T> getScrollData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, Object[] queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseEntity> QueryResult<T> getScrollData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, List<Object> queryParams,
            LinkedHashMap<String, String> orderby);
 
    public <T extends BaseEntity> QueryResult<T> getScrollData(
            Class<T> entityClass, String[] queryfields, int firstindex,
            int maxresult, String wherejpql, Map<String, Object> queryParams,
            LinkedHashMap<String, String> orderby);
 
    /**
     * 根据条件查询实体中的指定几个字段 <br>
     * 返回结果List<String[]>格式如下： <br>
     * 行1： 字段1value , 字段2value , 字段3value <br>
     * 行2： 字段1value , 字段2value , 字段3value
     * 
     * @author slx
     * @date 2009-5-14 下午01:14:23
     * @modifyNote
     * @param <T>
     * @param entityClass
     * @param queryfields
     * @param wheresql
     * @param queryParams
     * @return
     */
    public <T extends BaseEntity> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams);
 
    public <T extends BaseEntity> List<Object[]> queryFieldValues(
            Class<T> entityClass, String[] queryfields, String wheresql,
            Object[] queryParams, int startRow, int rows);
 
    /**
     * 根据条件查询实体中的指定几个字段 <br>
     * 返回的结果将重新组装到实体属性中，没有查询的字段为NULL<br>
     * 注意：使用该接口时，要确保实体类中有对应的查询字段的有参数构造方法，并且参数的顺序要和此处的queryfields数组的元素一致
     * 
     * @author yongtree
     * @date 2010-4-13 上午11:45:27
     * @modifyNote
     * @param <T>
     * @param entityClass
     * @param queryfields
     * @param wheresql
     * @param queryParams
     * @return
     */
    public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams);
 
    public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
            String[] queryfields, String wheresql, Object[] queryParams,
            int startRow, int rows);
 
    /**
     * 根据where条件查询实体bean列表 <br>
     * where和queryParams可空
     * 
     * @author slx
     * @date 2009-5-14 下午01:20:19
     * @modifyNote
     * @param <T>
     * @param entityClass
     * @param wheresql
     * @param queryParams
     * @return
     */
    public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
            String wheresql, Object[] queryParams);
 
    /**
     * 根据where条件查询实体bean列表,可指定取第几行到第几行 <br>
     * where和queryParams可空
     * 
     * @modifyNote
     * @param <T>
     * @param entityClass
     * @param wheresql
     * @param queryParams
     * @param startRow
     *            开始行
     * @param rows
     *            共多少行
     * @return
     */
    public <T extends BaseEntity> List<T> queryByWhere(Class<T> entityClass,
            String wheresql, Object[] queryParams, int startRow, int rows);
 
}