package com.example.elasticsearch.repository;

import com.example.elasticsearch.bean.EmployeeInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @author 程序员小强
 * @date 2020-12-08 16:34
 */
public interface EmployeeInfoRepository extends ElasticsearchRepository<EmployeeInfo, Long> {

    /**
     * 精确查找
     * 方法名规则：finByxxx
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
     *
     * @param name
     * @param page
     * @return 员工数据集
     * 注：等同于下面代码 @Query("{\"bool\" : {\"must\" : {\"term\" : {\"name\" : \"?0\"}}}}")
     */
    Page<EmployeeInfo> findByName(String name, Pageable page);

    /**
     * NOT 语句查询
     *
     * @param name
     * @param page
     * @return 员工数据集
     */
    Page<EmployeeInfo> findByNameNot(String name, Pageable page);

    /**
     * LIKE 语句查询
     *
     * @param name
     * @param page
     * @return 员工数据集
     */
    Page<EmployeeInfo> findByNameLike(String name, Pageable page);

}
