package com.jeff.tianti.cms.dao;

import com.jeff.tianti.cms.entity.Article;
import com.jeff.tianti.common.dao.CommonDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author xujianfang
 * @desc ArticleDao接口
 * @date 2017-03-16
 */
public interface ArticleDao extends ArticleDaoCustom,CommonDao<Article,String>{

    @Query("UPDATE Article set viewCount=?1 where id=?2")
    @Modifying
    int view(Integer view_count,String id );

}
