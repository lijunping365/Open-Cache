package com.saucesubfresh.cache.admin.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.saucesubfresh.cache.admin.convert.OpenCacheLogConvert;
import com.saucesubfresh.cache.api.dto.create.OpenCacheLogCreateDTO;
import com.saucesubfresh.cache.api.dto.req.OpenCacheLogReqDTO;
import com.saucesubfresh.cache.api.dto.resp.OpenCacheLogRespDTO;
import com.saucesubfresh.cache.admin.entity.OpenCacheLogDO;
import com.saucesubfresh.cache.admin.mapper.OpenCacheLogMapper;
import com.saucesubfresh.cache.admin.service.OpenCacheLogService;
import com.saucesubfresh.cache.common.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OpenCacheLogServiceImpl extends ServiceImpl<OpenCacheLogMapper, OpenCacheLogDO> implements OpenCacheLogService {

    @Autowired
    private OpenCacheLogMapper openCacheLogMapper;

    @Override
    public PageResult<OpenCacheLogRespDTO> selectPage(OpenCacheLogReqDTO OpenCacheLogReqDTO) {
        Page<OpenCacheLogDO> page = openCacheLogMapper.queryPage(OpenCacheLogReqDTO);
        IPage<OpenCacheLogRespDTO> convert = page.convert(OpenCacheLogConvert.INSTANCE::convert);
        return PageResult.build(convert);
    }

    @Override
    public OpenCacheLogRespDTO getById(Long id) {
        OpenCacheLogDO OpenCacheLogDO = openCacheLogMapper.selectById(id);
        return OpenCacheLogConvert.INSTANCE.convert(OpenCacheLogDO);
    }

    @Override
    public void save(OpenCacheLogCreateDTO OpenCacheLogCreateDTO) {
        openCacheLogMapper.insert(OpenCacheLogConvert.INSTANCE.convert(OpenCacheLogCreateDTO));
    }

    @Override
    public boolean deleteById(Long id) {
        openCacheLogMapper.deleteById(id);
        return true;
    }
}