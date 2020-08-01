package com.x.shop.service.impl;

import com.x.shop.dao.AreaDao;
import com.x.shop.entity.Area;
import com.x.shop.service.AreaService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 地区
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("areaServiceImpl")
public class AreaServiceImpl extends BaseServiceImpl<Area, Long> implements AreaService {

    @Resource(name = "areaDaoImpl")
    private AreaDao areaDao;

    @Override
    @Transactional(readOnly = true)
    public List<Area> findRoots() {
        return areaDao.findRoots(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Area> findRoots(Integer count) {
        return areaDao.findRoots(count);
    }

    @Override
    @Transactional
    @CacheEvict(value = "area", allEntries = true)
    public void save(Area area) {
        super.save(area);
    }

    @Override
    @Transactional
    @CacheEvict(value = "area", allEntries = true)
    public Area update(Area area) {
        return super.update(area);
    }

    @Override
    @Transactional
    @CacheEvict(value = "area", allEntries = true)
    public Area update(Area area, String... ignoreProperties) {
        return super.update(area, ignoreProperties);
    }

    @Override
    @Transactional
    @CacheEvict(value = "area", allEntries = true)
    public void delete(Long id) {
        super.delete(id);
    }

    @Override
    @Transactional
    @CacheEvict(value = "area", allEntries = true)
    public void delete(Long... ids) {
        super.delete(ids);
    }

    @Override
    @Transactional
    @CacheEvict(value = "area", allEntries = true)
    public void delete(Area area) {
        super.delete(area);
    }

}