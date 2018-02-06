package com.lianjiu.mapper;

import org.apache.ibatis.annotations.Param;

public interface PictureServerTransferMapper {

	int updateCategory(@Param("oldIp") String oldIp, @Param("newIp") String newIp);

	int updateProduct(@Param("oldIp") String oldIp, @Param("newIp") String newIp);

	int updateProductExcellent(@Param("oldIp") String oldIp, @Param("newIp") String newIp);
}
