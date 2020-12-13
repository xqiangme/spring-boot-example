package com.example.elasticsearch.service.impl;

import com.example.elasticsearch.bean.EmployeeInfo;
import com.example.elasticsearch.repository.EmployeeInfoRepository;
import com.example.elasticsearch.service.EmployeeInfoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 员工数据-ES接口
 *
 * @author 程序员小强
 * @date 2020-12-08
 */
@Service
public class EmployeeInfoServiceImpl implements EmployeeInfoService {

    @Resource
    private EmployeeInfoRepository elasticRepository;
    @Resource
    private ElasticsearchRestTemplate elasticsearchTemplate;

    /**
     * 员工信息否存在
     *
     * @param id 文档ID
     */
    @Override
    public boolean exists(String id) {
        return elasticsearchTemplate.exists(id, EmployeeInfo.class);
    }

    /**
     * 新增或更新文档
     * 注：当前同ID数据已经存在时为更新
     *
     * @param docBean
     */
    @Override
    public void saveOrUpdate(EmployeeInfo docBean) {
        elasticRepository.save(docBean);
    }

    /**
     * 批量新增或-保存文档
     * 注意：当ID存在时为更新
     *
     * @param list
     */
    @Override
    public void batchSaveOrUpdate(List<EmployeeInfo> list) {
        elasticRepository.saveAll(list);
    }

    /**
     * 根据ID删除
     *
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        elasticRepository.deleteById(id);
    }
    /**
     * 删除
     *
     * @param docBean
     */
    @Override
    public void delete(EmployeeInfo docBean) {
        elasticRepository.delete(docBean);
    }

    /**
     * 批量删除
     *
     * @param list
     */
    @Override
    public void batchDelete(List<EmployeeInfo> list) {
        elasticRepository.deleteAll(list);
    }

    /**
     * 根据ID查询
     *
     * @param id
     * @return 员工数据
     */
    @Override
    public EmployeeInfo findById(Long id) {
        Optional<EmployeeInfo> employeeInfo = elasticRepository.findById(id);
        return employeeInfo.orElse(null);
    }

    /**
     * 查询索引下全部数据
     * 注：生产环境下慎用
     *
     * @return 员工数据
     */
    @Override
    public Iterator<EmployeeInfo> findAll() {
        return elasticRepository.findAll().iterator();
    }

    @Override
    public List<EmployeeInfo> findByName(String name) {
        return elasticRepository.findByName(name);
    }

    @Override
    public List<EmployeeInfo> findByNameAndAge(String name, Integer age) {
        return elasticRepository.findByNameAndAge(name, age);
    }

    @Override
    public List<EmployeeInfo> findByNameOrAge(String name, Integer age) {
        return elasticRepository.findByNameOrAge(name, age);
    }

    @Override
    public Page<EmployeeInfo> findByName(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return elasticRepository.findByName(name, pageable);
    }

    @Override
    public Page<EmployeeInfo> findByNameNot(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return elasticRepository.findByNameNot(name, pageable);
    }

    @Override
    public Page<EmployeeInfo> findByNameLike(String name, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return elasticRepository.findByNameLike(name, pageable);
    }


}
