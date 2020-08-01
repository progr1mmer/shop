package com.x.shop.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity - 文章分类
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Entity
@Table(name = "xx_article_category")
public class ArticleCategory extends BaseOrderEntity {

    private static final long serialVersionUID = -5132652107151648662L;

    /**
     * 树路径分隔符
     */
    public static final String TREE_PATH_SEPARATOR = ",";

    /**
     * 访问路径前缀
     */
    private static final String PATH_PREFIX = "/article/list";

    /**
     * 访问路径后缀
     */
    private static final String PATH_SUFFIX = ".html";

    /**
     * 名称
     */
    private String name;

    /**
     * 页面标题
     */
    private String seoTitle;

    /**
     * 页面关键词
     */
    private String seoKeywords;

    /**
     * 页面描述
     */
    private String seoDescription;

    /**
     * 树路径
     */
    private String treePath;

    /**
     * 层级
     */
    private Integer grade;

    /**
     * 上级分类
     */
    private ArticleCategory parent;

    /**
     * 下级分类
     */
    private Set<ArticleCategory> children = new HashSet<>();

    /**
     * 文章
     */
    private Set<Article> articles = new HashSet<>();

    /**
     * 获取名称
     *
     * @return 名称
     */
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取页面标题
     *
     * @return 页面标题
     */
    @Length(max = 200)
    public String getSeoTitle() {
        return seoTitle;
    }

    /**
     * 设置页面标题
     *
     * @param seoTitle 页面标题
     */
    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    /**
     * 获取页面关键词
     *
     * @return 页面关键词
     */
    @Length(max = 200)
    public String getSeoKeywords() {
        return seoKeywords;
    }

    /**
     * 设置页面关键词
     *
     * @param seoKeywords 页面关键词
     */
    public void setSeoKeywords(String seoKeywords) {
        this.seoKeywords = seoKeywords;
    }

    /**
     * 获取页面描述
     *
     * @return 页面描述
     */
    @Length(max = 200)
    public String getSeoDescription() {
        return seoDescription;
    }

    /**
     * 设置页面描述
     *
     * @param seoDescription 页面描述
     */
    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    /**
     * 获取树路径
     *
     * @return 树路径
     */
    @Column(nullable = false)
    public String getTreePath() {
        return treePath;
    }

    /**
     * 设置树路径
     *
     * @param treePath 树路径
     */
    public void setTreePath(String treePath) {
        this.treePath = treePath;
    }

    /**
     * 获取层级
     *
     * @return 层级
     */
    @Column(nullable = false)
    public Integer getGrade() {
        return grade;
    }

    /**
     * 设置层级
     *
     * @param grade 层级
     */
    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    /**
     * 获取上级分类
     *
     * @return 上级分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public ArticleCategory getParent() {
        return parent;
    }

    /**
     * 设置上级分类
     *
     * @param parent 上级分类
     */
    public void setParent(ArticleCategory parent) {
        this.parent = parent;
    }

    /**
     * 获取下级分类
     *
     * @return 下级分类
     */
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("order asc")
    public Set<ArticleCategory> getChildren() {
        return children;
    }

    /**
     * 设置下级分类
     *
     * @param children 下级分类
     */
    public void setChildren(Set<ArticleCategory> children) {
        this.children = children;
    }

    /**
     * 获取文章
     *
     * @return 文章
     */
    @OneToMany(mappedBy = "articleCategory", fetch = FetchType.LAZY)
    public Set<Article> getArticles() {
        return articles;
    }

    /**
     * 设置文章
     *
     * @param articles 文章
     */
    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    /**
     * 获取树路径
     *
     * @return 树路径
     */
    @Transient
    public List<Long> getTreePaths() {
        List<Long> treePaths = new ArrayList<Long>();
        String[] ids = StringUtils.split(getTreePath(), TREE_PATH_SEPARATOR);
        if (ids != null) {
            for (String id : ids) {
                treePaths.add(Long.valueOf(id));
            }
        }
        return treePaths;
    }

    /**
     * 获取访问路径
     *
     * @return 访问路径
     */
    @Transient
    public String getPath() {
        if (getId() != null) {
            return PATH_PREFIX + "/" + getId() + PATH_SUFFIX;
        }
        return null;
    }

}