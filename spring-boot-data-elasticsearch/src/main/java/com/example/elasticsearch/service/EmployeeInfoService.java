package com.example.elasticsearch.service;

import com.example.elasticsearch.bean.EmployeeInfo;
import org.springframework.data.domain.Page;

import java.util.Iterator;
import java.util.List;

/**
 * 员工数据-ES接口
 *
 * @author 程序员小强
 * @date 2020-12-08
 */
public interface EmployeeInfoService {

    /**
     * 员工信息否存在
     *
     * @param id 文档ID
     */
    boolean exists(String id);

    /**
     * 新增或更新文档
     * 注：当前同ID数据已经存在时为更新
     *
     * @param docBean
     */
    void saveOrUpdate(EmployeeInfo docBean);

    /**
     * 批量新增或-保存文档
     * 注意：当同ID数据已经存在时为更新
     *
     * @param list
     */
    void batchSaveOrUpdate(List<EmployeeInfo> list);

    /**
     * 根据ID删除
     *
     * @param id
     */
    void deleteById(Long id);

    /**
     * 删除
     *
     * @param docBean
     */
    void delete(EmployeeInfo docBean);

    /**
     * 批量删除
     *
     * @param list
     */
    void batchDelete(List<EmployeeInfo> list);

    /**
     * 根据数据ID查询
     *
     * @param id
     * @return
     */
    EmployeeInfo findById(Long id);

    /**
     * 查询索引下全部数据
     * 注：生产环境下慎用
     *
     * @return 员工数据
     */
    Iterator<EmployeeInfo> findAll();

    /**
     * 精确查找
     *
     * @param name
     * @return 员工数据集
     */
    List<EmployeeInfo> findByName(String name);

    /**
     * AND 语句查询
     *
     * @param name
     * @param age
     * @return 员工数据集
     */
    List<EmployeeInfo> findByNameAndAge(String name, Integer age);

    /**
     * OR 语句查询
     *
     * @param name
     * @param age
     * @return 员工数据集
     */
    List<EmployeeInfo> findByNameOrAge(String name, Integer age);

    /**
     * 分页查询员工信息
     * <p>
     * 等同于下面代码
     *
     * @param name
     * @param pageNo   当前页
     * @param pageSize 页容量
     * @return 员工数据集
     */
    Page<EmployeeInfo> findByName(String name, int pageNo, int pageSize);

    /**
     * NOT 语句查询
     *
     * @param name
     * @param pageNo   当前页
     * @param pageSize 页容量
     * @return 员工数据集
     */
    Page<EmployeeInfo> findByNameNot(String name, int pageNo, int pageSize);

    /**
     * LIKE 语句查询
     *
     * @param name
     * @param pageNo   当前页
     * @param pageSize 页容量
     * @return 员工数据集
     */
    Page<EmployeeInfo> findByNameLike(String name, int pageNo, int pageSize);

}
