package com.sf.module.operation.biz;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.sf.framework.base.IPage;
import com.sf.module.operation.domain.GroupBaseInfo;

public interface IGroupBaseInfoBiz {
    void saveGroupBaseInfo(GroupBaseInfo groupBaseInfo) throws Exception;
    void remove(Long id);
    IPage<GroupBaseInfo> findPageByGroupBaseInfos(GroupBaseInfo baseInfo,int pageSize,int pageIndex);
    Collection<GroupBaseInfo> findPageByGroupBaseInfos();
    void delete(String groupBaseInfos);
    HashMap<String,String> readUpLoadGroupBaseInfos(File file, String fileName, Long deptId) throws Exception;
    String exportGroupBaseInfo(GroupBaseInfo groupBaseInfo);
    boolean noticeShow();
    List<GroupBaseInfo> noticeShowList();
}
