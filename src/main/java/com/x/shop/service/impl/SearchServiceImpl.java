package com.x.shop.service.impl;

import com.x.shop.common.Page;
import com.x.shop.common.Pageable;
import com.x.shop.dao.ArticleDao;
import com.x.shop.dao.ProductDao;
import com.x.shop.entity.Article;
import com.x.shop.entity.Product;
import com.x.shop.entity.Product.OrderType;
import com.x.shop.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.*;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 搜索
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Service("searchServiceImpl")
@Transactional
public class SearchServiceImpl implements SearchService {

    /**
     * 模糊查询最小相似度
     * TODO 0.5F
     */
    private static final int FUZZY_QUERY_MINIMUM_SIMILARITY = 1;

    @PersistenceContext
    protected EntityManager entityManager;
    @Resource(name = "articleDaoImpl")
    private ArticleDao articleDao;
    @Resource(name = "productDaoImpl")
    private ProductDao productDao;

    @Override
    public void index() {
        index(Article.class);
        index(Product.class);
    }

    @Override
    public void index(Class<?> type) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        if (type == Article.class) {
            for (int i = 0; i < articleDao.count(); i += 20) {
                List<Article> articles = articleDao.findList(i, 20, new ArrayList<>(), new ArrayList<>());
                for (Article article : articles) {
                    fullTextEntityManager.index(article);
                }
                fullTextEntityManager.flushToIndexes();
                fullTextEntityManager.clear();
                articleDao.clear();
            }
        } else if (type == Product.class) {
            for (int i = 0; i < productDao.count(); i += 20) {
                List<Product> products = productDao.findList(i, 20, new ArrayList<>(), new ArrayList<>());
                for (Product product : products) {
                    fullTextEntityManager.index(product);
                }
                fullTextEntityManager.flushToIndexes();
                fullTextEntityManager.clear();
                productDao.clear();
            }
        }
    }

    @Override
    public void index(Article article) {
        if (article != null) {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.index(article);
        }
    }

    @Override
    public void index(Product product) {
        if (product != null) {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.index(product);
        }
    }

    @Override
    public void purge() {
        purge(Article.class);
        purge(Product.class);
    }

    @Override
    public void purge(Class<?> type) {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        if (type == Article.class) {
            fullTextEntityManager.purgeAll(Article.class);
        } else if (type == Product.class) {
            fullTextEntityManager.purgeAll(Product.class);
        }
    }

    @Override
    public void purge(Article article) {
        if (article != null) {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.purge(Article.class, article.getId());
        }
    }

    @Override
    public void purge(Product product) {
        if (product != null) {
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            fullTextEntityManager.purge(Product.class, product.getId());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<Article> search(String keyword, Pageable pageable) {
        if (StringUtils.isEmpty(keyword)) {
            return new Page<>();
        }
        if (pageable == null) {
            pageable = new Pageable();
        }
        try {
            String text = QueryParser.escape(keyword);
            QueryParser titleParser = new QueryParser("title", new SmartChineseAnalyzer());
            titleParser.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query titleQuery = titleParser.parse(text);
            FuzzyQuery titleFuzzyQuery = new FuzzyQuery(new Term("title", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
            Query contentQuery = new TermQuery(new Term("content", text));
            Query isPublicationQuery = new TermQuery(new Term("isPublication", "true"));
            BooleanQuery textQuery = new BooleanQuery();
            BooleanQuery query = new BooleanQuery();
            textQuery.add(titleQuery, Occur.SHOULD);
            textQuery.add(titleFuzzyQuery, Occur.SHOULD);
            textQuery.add(contentQuery, Occur.SHOULD);
            query.add(isPublicationQuery, Occur.MUST);
            query.add(textQuery, Occur.MUST);
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Article.class);
            fullTextQuery.setSort(new Sort(new SortField("isTop", SortField.Type.STRING, true), new SortField(null, SortField.Type.SCORE), new SortField("createDate", SortField.Type.LONG, true)));
            fullTextQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            fullTextQuery.setMaxResults(pageable.getPageSize());
            return new Page<Article>(fullTextQuery.getResultList(), fullTextQuery.getResultSize(), pageable);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Page<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<Product> search(String keyword, BigDecimal startPrice, BigDecimal endPrice, OrderType orderType, Pageable pageable) {
        if (StringUtils.isEmpty(keyword)) {
            return new Page<>();
        }
        if (pageable == null) {
            pageable = new Pageable();
        }
        try {
            String text = QueryParser.escape(keyword);
            TermQuery snQuery = new TermQuery(new Term("sn", text));
            Query keywordQuery = new QueryParser("keyword", new SmartChineseAnalyzer()).parse(text);
            QueryParser nameParser = new QueryParser("name", new SmartChineseAnalyzer());
            nameParser.setDefaultOperator(QueryParser.AND_OPERATOR);
            Query nameQuery = nameParser.parse(text);
            FuzzyQuery nameFuzzyQuery = new FuzzyQuery(new Term("name", text), FUZZY_QUERY_MINIMUM_SIMILARITY);
            TermQuery introductionQuery = new TermQuery(new Term("introduction", text));
            TermQuery isMarketableQuery = new TermQuery(new Term("isMarketable", "true"));
            TermQuery isListQuery = new TermQuery(new Term("isList", "true"));
            TermQuery isGiftQuery = new TermQuery(new Term("isGift", "false"));
            BooleanQuery textQuery = new BooleanQuery();
            BooleanQuery query = new BooleanQuery();
            textQuery.add(snQuery, Occur.SHOULD);
            textQuery.add(keywordQuery, Occur.SHOULD);
            textQuery.add(nameQuery, Occur.SHOULD);
            textQuery.add(nameFuzzyQuery, Occur.SHOULD);
            textQuery.add(introductionQuery, Occur.SHOULD);
            query.add(isMarketableQuery, Occur.MUST);
            query.add(isListQuery, Occur.MUST);
            query.add(isGiftQuery, Occur.MUST);
            query.add(textQuery, Occur.MUST);
            if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
                BigDecimal temp = startPrice;
                startPrice = endPrice;
                endPrice = temp;
            }
            if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0 && endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
                NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", startPrice.doubleValue(), endPrice.doubleValue(), true, true);
                query.add(numericRangeQuery, Occur.MUST);
            } else if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
                NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", startPrice.doubleValue(), null, true, false);
                query.add(numericRangeQuery, Occur.MUST);
            } else if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
                NumericRangeQuery<Double> numericRangeQuery = NumericRangeQuery.newDoubleRange("price", null, endPrice.doubleValue(), false, true);
                query.add(numericRangeQuery, Occur.MUST);
            }
            FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
            FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(query, Product.class);
            SortField[] sortFields = null;
            if (orderType == OrderType.priceAsc) {
                sortFields = new SortField[]{new SortField("price", SortField.Type.DOUBLE, false), new SortField("createDate", SortField.Type.LONG, true)};
            } else if (orderType == OrderType.priceDesc) {
                sortFields = new SortField[]{new SortField("price", SortField.Type.DOUBLE, true), new SortField("createDate", SortField.Type.LONG, true)};
            } else if (orderType == OrderType.salesDesc) {
                sortFields = new SortField[]{new SortField("sales", SortField.Type.INT, true), new SortField("createDate", SortField.Type.LONG, true)};
            } else if (orderType == OrderType.scoreDesc) {
                sortFields = new SortField[]{new SortField("score", SortField.Type.INT, true), new SortField("createDate", SortField.Type.LONG, true)};
            } else if (orderType == OrderType.dateDesc) {
                sortFields = new SortField[]{new SortField("createDate", SortField.Type.LONG, true)};
            } else {
                sortFields = new SortField[]{new SortField("isTop", SortField.Type.STRING, true), new SortField(null, SortField.Type.SCORE), new SortField("modifyDate", SortField.Type.LONG, true)};
            }
            fullTextQuery.setSort(new Sort(sortFields));
            fullTextQuery.setFirstResult((pageable.getPageNumber() - 1) * pageable.getPageSize());
            fullTextQuery.setMaxResults(pageable.getPageSize());
            return new Page<Product>(fullTextQuery.getResultList(), fullTextQuery.getResultSize(), pageable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Page<>();
    }

}