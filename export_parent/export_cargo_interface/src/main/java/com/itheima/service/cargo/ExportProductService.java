package com.itheima.service.cargo;


import com.github.pagehelper.PageInfo;
import com.itheima.domain.cargo.ExportProduct;
import com.itheima.domain.cargo.ExportProductExample;

import java.util.List;


public interface ExportProductService {

	ExportProduct findById(String id);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	List<ExportProduct> findAll(ExportProductExample exportProductExample);
}
