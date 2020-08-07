package com.x.shop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.x.shop.common.BigDecimalNumericFieldBridge;
import com.x.shop.common.CommonAttributes;
import com.x.shop.util.FreemarkerUtils;
import freemarker.template.TemplateException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.dom4j.io.SAXReader;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Index;
import org.hibernate.validator.constraints.Length;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Entity - 商品
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Indexed
@Entity
@Table(name = "xx_product")
public class Product extends BaseEntity {

    private static final long serialVersionUID = 2167830430439593293L;

    /**
     * 点击数缓存名称
     */
    public static final String HITS_CACHE_NAME = "productHits";

    /**
     * 点击数缓存更新间隔时间
     */
    public static final int HITS_CACHE_INTERVAL = 600000;

    /**
     * 商品属性值属性个数
     */
    public static final int ATTRIBUTE_VALUE_PROPERTY_COUNT = 20;

    /**
     * 商品属性值属性名称前缀
     */
    public static final String ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX = "attributeValue";

    /**
     * 全称规格前缀
     */
    public static final String FULL_NAME_SPECIFICATION_PREFIX = "[";

    /**
     * 全称规格后缀
     */
    public static final String FULL_NAME_SPECIFICATION_SUFFIX = "]";

    /**
     * 全称规格分隔符
     */
    public static final String FULL_NAME_SPECIFICATION_SEPARATOR = " ";

    /**
     * 单页静态路径
     */
    private static String singleStaticPath;
    /**
     * 综合静态路径
     */
    private static String complexStaticPath;

    /**
     * 排序类型
     */
    public enum OrderType {

        /**
         * 置顶降序
         */
        topDesc,

        /**
         * 价格升序
         */
        priceAsc,

        /**
         * 价格降序
         */
        priceDesc,

        /**
         * 销量降序
         */
        salesDesc,

        /**
         * 评分降序
         */
        scoreDesc,

        /**
         * 日期降序
         */
        dateDesc
    }

    /**
     * 模式
     */
    public enum Mode {
        /**
         * 单页
         */
        single,

        /**
         * 综合
         */
        complex
    }

    /**
     * 编号
     */
    private String sn;

    /**
     * 名称
     */
    private String name;

    /**
     * 全称
     */
    private String fullName;

    /**
     * 销售价
     */
    private BigDecimal price;

    /**
     * 成本价
     */
    private BigDecimal cost;

    /**
     * 市场价
     */
    private BigDecimal marketPrice;

    /**
     * 展示图片
     */
    private String image;

    /**
     * 单位
     */
    private String unit;

    /**
     * 重量
     */
    private Integer weight;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 已分配库存
     */
    private Integer allocatedStock;

    /**
     * 库存备注
     */
    private String stockMemo;

    /**
     * 赠送积分
     */
    private Long point;

    /**
     * 是否上架
     */
    private Boolean isMarketable;

    /**
     * 是否列出
     */
    private Boolean isList;

    /**
     * 是否置顶
     */
    private Boolean isTop;

    /**
     * 是否为赠品
     */
    private Boolean isGift;

    /**
     * 介绍
     */
    private String introduction;

    /**
     * 备注
     */
    private String memo;

    /**
     * 搜索关键词
     */
    private String keyword;

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
     * 评分
     */
    private Float score;

    /**
     * 总评分
     */
    private Long totalScore;

    /**
     * 评分数
     */
    private Long scoreCount;

    /**
     * 点击数
     */
    private Long hits;

    /**
     * 周点击数
     */
    private Long weekHits;

    /**
     * 月点击数
     */
    private Long monthHits;

    /**
     * 销量
     */
    private Long sales;

    /**
     * 周销量
     */
    private Long weekSales;

    /**
     * 月销量
     */
    private Long monthSales;

    /**
     * 周点击数更新日期
     */
    private Date weekHitsDate;

    /**
     * 月点击数更新日期
     */
    private Date monthHitsDate;

    /**
     * 周销量更新日期
     */
    private Date weekSalesDate;

    /**
     * 月销量更新日期
     */
    private Date monthSalesDate;

    /**
     * 商品属性值0
     */
    private String attributeValue0;

    /**
     * 商品属性值1
     */
    private String attributeValue1;

    /**
     * 商品属性值2
     */
    private String attributeValue2;

    /**
     * 商品属性值3
     */
    private String attributeValue3;

    /**
     * 商品属性值4
     */
    private String attributeValue4;

    /**
     * 商品属性值5
     */
    private String attributeValue5;

    /**
     * 商品属性值6
     */
    private String attributeValue6;

    /**
     * 商品属性值7
     */
    private String attributeValue7;

    /**
     * 商品属性值8
     */
    private String attributeValue8;

    /**
     * 商品属性值9
     */
    private String attributeValue9;

    /**
     * 商品属性值10
     */
    private String attributeValue10;

    /**
     * 商品属性值11
     */
    private String attributeValue11;

    /**
     * 商品属性值12
     */
    private String attributeValue12;

    /**
     * 商品属性值13
     */
    private String attributeValue13;

    /**
     * 商品属性值14
     */
    private String attributeValue14;

    /**
     * 商品属性值15
     */
    private String attributeValue15;

    /**
     * 商品属性值16
     */
    private String attributeValue16;

    /**
     * 商品属性值17
     */
    private String attributeValue17;

    /**
     * 商品属性值18
     */
    private String attributeValue18;

    /**
     * 商品属性值19
     */
    private String attributeValue19;

    /**
     * 商品分类
     */
    private ProductCategory productCategory;

    /**
     * 货品
     */
    private Goods goods;

    /**
     * 品牌
     */
    private Brand brand;

    /**
     * 商品图片
     */
    private List<ProductImage> productImages = new ArrayList<ProductImage>();

    /**
     * 标签
     */
    private Set<Tag> tags = new HashSet<>();

    /**
     * 规格
     */
    private Set<Specification> specifications = new HashSet<>();

    /**
     * 规格值
     */
    private Set<SpecificationValue> specificationValues = new HashSet<>();

    /**
     * 促销
     */
    private Set<Promotion> promotions = new HashSet<>();

    /**
     * 购物车项
     */
    private Set<CartItem> cartItems = new HashSet<>();

    /**
     * 订单项
     */
    private Set<OrderItem> orderItems = new HashSet<>();

    /**
     * 赠品项
     */
    private Set<GiftItem> giftItems = new HashSet<>();

    /**
     * 参数值
     */
    private Map<Parameter, String> parameterValue = new HashMap<>();

    private Mode mode;

    private String imageI;

    private String introductionB;

    static {
        try {
            File shopXmlFile = new ClassPathResource(CommonAttributes.SHOP_XML_PATH).getFile();
            org.dom4j.Document document = new SAXReader().read(shopXmlFile);
            org.dom4j.Element singleElement = (org.dom4j.Element) document.selectSingleNode("/shop/template[@id='productContentSingle']");
            org.dom4j.Element complexElement = (org.dom4j.Element) document.selectSingleNode("/shop/template[@id='productContentComplex']");
            singleStaticPath = singleElement.attributeValue("staticPath");
            complexStaticPath = complexElement.attributeValue("staticPath");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @Pattern(regexp = "^[0-9a-zA-Z_-]+$")
    @Length(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = SmartChineseAnalyzer.class))
    @NotEmpty
    @Length(max = 200)
    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.NO)
    @Column(nullable = false)
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @NumericField
    @FieldBridge(impl = BigDecimalNumericFieldBridge.class)
    @NotNull
    @Min(0)
    @Digits(integer = 21, fraction = 2)
    @Column(nullable = false, precision = 21, scale = 2)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Min(0)
    @Digits(integer = 21, fraction = 2)
    @Column(precision = 21, scale = 2)
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Min(0)
    @Digits(integer = 21, fraction = 2)
    @Column(nullable = false, precision = 21, scale = 2)
    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.NO)
    @Length(max = 200)
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.NO)
    @Length(max = 200)
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Min(0)
    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Min(0)
    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Column(nullable = false)
    public Integer getAllocatedStock() {
        return allocatedStock;
    }

    public void setAllocatedStock(Integer allocatedStock) {
        this.allocatedStock = allocatedStock;
    }

    @Length(max = 200)
    public String getStockMemo() {
        return stockMemo;
    }

    public void setStockMemo(String stockMemo) {
        this.stockMemo = stockMemo;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Min(0)
    @Column(nullable = false)
    public Long getPoint() {
        return point;
    }

    public void setPoint(Long point) {
        this.point = point;
    }

    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @NotNull
    @Column(nullable = false)
    public Boolean getIsMarketable() {
        return isMarketable;
    }

    public void setIsMarketable(Boolean isMarketable) {
        this.isMarketable = isMarketable;
    }

    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @NotNull
    @Column(nullable = false)
    public Boolean getIsList() {
        return isList;
    }

    public void setIsList(Boolean isList) {
        this.isList = isList;
    }

    @NotNull
    @Column(nullable = false)
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @SortableField
    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    @JsonProperty
    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @NotNull
    @Column(nullable = false)
    public Boolean getIsGift() {
        return isGift;
    }

    public void setIsGift(Boolean isGift) {
        this.isGift = isGift;
    }

    @Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = SmartChineseAnalyzer.class))
    @Lob
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Length(max = 200)
    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    @Field(store = Store.YES, index = Index.YES, analyzer = @Analyzer(impl = SmartChineseAnalyzer.class))
    @Length(max = 200)
    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        if (keyword != null) {
            keyword = keyword.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
        }
        this.keyword = keyword;
    }

    @Length(max = 200)
    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    @Length(max = 200)
    public String getSeoKeywords() {
        return seoKeywords;
    }

    public void setSeoKeywords(String seoKeywords) {
        if (seoKeywords != null) {
            seoKeywords = seoKeywords.replaceAll("[,\\s]*,[,\\s]*", ",").replaceAll("^,|,$", "");
        }
        this.seoKeywords = seoKeywords;
    }

    @Length(max = 200)
    public String getSeoDescription() {
        return seoDescription;
    }

    public void setSeoDescription(String seoDescription) {
        this.seoDescription = seoDescription;
    }

    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @NumericField
    @Digits(integer = 1, fraction = 2)
    @Column(nullable = false, precision = 1, scale = 2)
    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    @Column(nullable = false)
    public Long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Long totalScore) {
        this.totalScore = totalScore;
    }

    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @Column(nullable = false)
    public Long getScoreCount() {
        return scoreCount;
    }

    public void setScoreCount(Long scoreCount) {
        this.scoreCount = scoreCount;
    }

    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @Column(nullable = false)
    public Long getHits() {
        return hits;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Column(nullable = false)
    public Long getWeekHits() {
        return weekHits;
    }

    public void setWeekHits(Long weekHits) {
        this.weekHits = weekHits;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Column(nullable = false)
    public Long getMonthHits() {
        return monthHits;
    }

    public void setMonthHits(Long monthHits) {
        this.monthHits = monthHits;
    }

    @Field(store = Store.YES, index = Index.YES, analyze = Analyze.NO)
    @Column(nullable = false)
    public Long getSales() {
        return sales;
    }

    public void setSales(Long sales) {
        this.sales = sales;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Column(nullable = false)
    public Long getWeekSales() {
        return weekSales;
    }

    public void setWeekSales(Long weekSales) {
        this.weekSales = weekSales;
    }

    @Field(store = Store.YES, index = Index.NO)
    @Column(nullable = false)
    public Long getMonthSales() {
        return monthSales;
    }

    public void setMonthSales(Long monthSales) {
        this.monthSales = monthSales;
    }

    @Column(nullable = false)
    public Date getWeekHitsDate() {
        return weekHitsDate;
    }

    public void setWeekHitsDate(Date weekHitsDate) {
        this.weekHitsDate = weekHitsDate;
    }

    @Column(nullable = false)
    public Date getMonthHitsDate() {
        return monthHitsDate;
    }

    public void setMonthHitsDate(Date monthHitsDate) {
        this.monthHitsDate = monthHitsDate;
    }

    @Column(nullable = false)
    public Date getWeekSalesDate() {
        return weekSalesDate;
    }

    public void setWeekSalesDate(Date weekSalesDate) {
        this.weekSalesDate = weekSalesDate;
    }

    @Column(nullable = false)
    public Date getMonthSalesDate() {
        return monthSalesDate;
    }

    public void setMonthSalesDate(Date monthSalesDate) {
        this.monthSalesDate = monthSalesDate;
    }

    @Length(max = 200)
    public String getAttributeValue0() {
        return attributeValue0;
    }

    public void setAttributeValue0(String attributeValue0) {
        this.attributeValue0 = attributeValue0;
    }

    @Length(max = 200)
    public String getAttributeValue1() {
        return attributeValue1;
    }

    public void setAttributeValue1(String attributeValue1) {
        this.attributeValue1 = attributeValue1;
    }

    @Length(max = 200)
    public String getAttributeValue2() {
        return attributeValue2;
    }

    public void setAttributeValue2(String attributeValue2) {
        this.attributeValue2 = attributeValue2;
    }

    @Length(max = 200)
    public String getAttributeValue3() {
        return attributeValue3;
    }

    public void setAttributeValue3(String attributeValue3) {
        this.attributeValue3 = attributeValue3;
    }

    @Length(max = 200)
    public String getAttributeValue4() {
        return attributeValue4;
    }

    public void setAttributeValue4(String attributeValue4) {
        this.attributeValue4 = attributeValue4;
    }

    @Length(max = 200)
    public String getAttributeValue5() {
        return attributeValue5;
    }

    public void setAttributeValue5(String attributeValue5) {
        this.attributeValue5 = attributeValue5;
    }

    @Length(max = 200)
    public String getAttributeValue6() {
        return attributeValue6;
    }

    public void setAttributeValue6(String attributeValue6) {
        this.attributeValue6 = attributeValue6;
    }

    @Length(max = 200)
    public String getAttributeValue7() {
        return attributeValue7;
    }

    public void setAttributeValue7(String attributeValue7) {
        this.attributeValue7 = attributeValue7;
    }

    @Length(max = 200)
    public String getAttributeValue8() {
        return attributeValue8;
    }

    public void setAttributeValue8(String attributeValue8) {
        this.attributeValue8 = attributeValue8;
    }

    @Length(max = 200)
    public String getAttributeValue9() {
        return attributeValue9;
    }

    public void setAttributeValue9(String attributeValue9) {
        this.attributeValue9 = attributeValue9;
    }

    @Length(max = 200)
    public String getAttributeValue10() {
        return attributeValue10;
    }

    public void setAttributeValue10(String attributeValue10) {
        this.attributeValue10 = attributeValue10;
    }

    @Length(max = 200)
    public String getAttributeValue11() {
        return attributeValue11;
    }

    public void setAttributeValue11(String attributeValue11) {
        this.attributeValue11 = attributeValue11;
    }

    @Length(max = 200)
    public String getAttributeValue12() {
        return attributeValue12;
    }

    public void setAttributeValue12(String attributeValue12) {
        this.attributeValue12 = attributeValue12;
    }

    @Length(max = 200)
    public String getAttributeValue13() {
        return attributeValue13;
    }

    public void setAttributeValue13(String attributeValue13) {
        this.attributeValue13 = attributeValue13;
    }

    @Length(max = 200)
    public String getAttributeValue14() {
        return attributeValue14;
    }

    public void setAttributeValue14(String attributeValue14) {
        this.attributeValue14 = attributeValue14;
    }

    @Length(max = 200)
    public String getAttributeValue15() {
        return attributeValue15;
    }

    public void setAttributeValue15(String attributeValue15) {
        this.attributeValue15 = attributeValue15;
    }

    @Length(max = 200)
    public String getAttributeValue16() {
        return attributeValue16;
    }

    public void setAttributeValue16(String attributeValue16) {
        this.attributeValue16 = attributeValue16;
    }

    @Length(max = 200)
    public String getAttributeValue17() {
        return attributeValue17;
    }

    public void setAttributeValue17(String attributeValue17) {
        this.attributeValue17 = attributeValue17;
    }

    @Length(max = 200)
    public String getAttributeValue18() {
        return attributeValue18;
    }

    public void setAttributeValue18(String attributeValue18) {
        this.attributeValue18 = attributeValue18;
    }

    @Length(max = 200)
    public String getAttributeValue19() {
        return attributeValue19;
    }

    public void setAttributeValue19(String attributeValue19) {
        this.attributeValue19 = attributeValue19;
    }

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, updatable = false)
    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Valid
    @ElementCollection
    @CollectionTable(name = "xx_product_product_image")
    public List<ProductImage> getProductImages() {
        return productImages;
    }

    public void setProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_product_tag",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    @OrderBy("order asc")
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_product_specification",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "specification_id", referencedColumnName = "id")})
    @OrderBy("order asc")
    public Set<Specification> getSpecifications() {
        return specifications;
    }

    public void setSpecifications(Set<Specification> specifications) {
        this.specifications = specifications;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "xx_product_specification_value",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "specification_value_id", referencedColumnName = "id")})
    @OrderBy("specification asc")
    public Set<SpecificationValue> getSpecificationValues() {
        return specificationValues;
    }

    public void setSpecificationValues(Set<SpecificationValue> specificationValues) {
        this.specificationValues = specificationValues;
    }

    @ManyToMany(mappedBy = "products", fetch = FetchType.LAZY)
    public Set<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(Set<Promotion> promotions) {
        this.promotions = promotions;
    }

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Set<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(Set<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    @OneToMany(mappedBy = "gift", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<GiftItem> getGiftItems() {
        return giftItems;
    }

    public void setGiftItems(Set<GiftItem> giftItems) {
        this.giftItems = giftItems;
    }

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "xx_product_parameter_value")
    public Map<Parameter, String> getParameterValue() {
        return parameterValue;
    }

    public void setParameterValue(Map<Parameter, String> parameterValue) {
        this.parameterValue = parameterValue;
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public String getImageI() {
        return imageI;
    }

    public void setImageI(String imageI) {
        this.imageI = imageI;
    }

    public String getIntroductionB() {
        return introductionB;
    }

    public void setIntroductionB(String introductionB) {
        this.introductionB = introductionB;
    }

    /**
     * 获取商品属性值
     *
     * @param attribute 商品属性
     * @return 商品属性值
     */
    @Transient
    public String getAttributeValue(Attribute attribute) {
        if (attribute != null && attribute.getPropertyIndex() != null) {
            try {
                String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
                return (String) PropertyUtils.getProperty(this, propertyName);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 设置商品属性值
     *
     * @param attribute 商品属性
     * @param value     商品属性值
     */
    @Transient
    public void setAttributeValue(Attribute attribute, String value) {
        if (attribute != null && attribute.getPropertyIndex() != null) {
            if (StringUtils.isEmpty(value)) {
                value = null;
            }
            if (value == null || (attribute.getOptions() != null && attribute.getOptions().contains(value))) {
                try {
                    String propertyName = ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + attribute.getPropertyIndex();
                    PropertyUtils.setProperty(this, propertyName, value);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取同货品商品
     *
     * @return 同货品商品，不包含自身
     */
    @Transient
    public List<Product> getSiblings() {
        List<Product> siblings = new ArrayList<Product>();
        if (getGoods() != null && getGoods().getProducts() != null) {
            for (Product product : getGoods().getProducts()) {
                if (!this.equals(product)) {
                    siblings.add(product);
                }
            }
        }
        return siblings;
    }

    /**
     * 获取访问路径
     *
     * @return 访问路径
     */
    @JsonProperty
    @Transient
    public String getPath() {
        Map<String, Object> model = new HashMap<>();
        model.put("id", getId());
        model.put("createDate", getCreateDate());
        model.put("modifyDate", getModifyDate());
        model.put("sn", getSn());
        model.put("name", getName());
        model.put("fullName", getFullName());
        model.put("seoTitle", getSeoTitle());
        model.put("seoKeywords", getSeoKeywords());
        model.put("seoDescription", getSeoDescription());
        model.put("productCategory", getProductCategory());
        try {
            if (mode == Mode.single) {
                return FreemarkerUtils.process(singleStaticPath, model);
            } else {
                return FreemarkerUtils.process(complexStaticPath, model);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取访问路径
     *
     * @return 访问路径
     */
    @JsonIgnore
    @Transient
    public List<String> getAllPath() {
        Map<String, Object> model = new HashMap<>();
        model.put("id", getId());
        model.put("createDate", getCreateDate());
        model.put("modifyDate", getModifyDate());
        model.put("sn", getSn());
        model.put("name", getName());
        model.put("fullName", getFullName());
        model.put("seoTitle", getSeoTitle());
        model.put("seoKeywords", getSeoKeywords());
        model.put("seoDescription", getSeoDescription());
        model.put("productCategory", getProductCategory());
        List<String> paths = new ArrayList<>();
        try {
            paths.add(FreemarkerUtils.process(singleStaticPath, model));
            paths.add(FreemarkerUtils.process(complexStaticPath, model));
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
        return paths;
    }

    /**
     * 获取缩略图
     *
     * @return 缩略图
     */
    @JsonProperty
    @Transient
    public String getThumbnail() {
        if (getProductImages() != null && !getProductImages().isEmpty()) {
            return getProductImages().get(0).getThumbnail();
        }
        return null;
    }

    /**
     * 获取有效促销
     *
     * @return 有效促销
     */
    @Transient
    public Set<Promotion> getValidPromotions() {
        Set<Promotion> allPromotions = new HashSet<Promotion>();
        if (getPromotions() != null) {
            allPromotions.addAll(getPromotions());
        }
        if (getProductCategory() != null && getProductCategory().getPromotions() != null) {
            allPromotions.addAll(getProductCategory().getPromotions());
        }
        if (getBrand() != null && getBrand().getPromotions() != null) {
            allPromotions.addAll(getBrand().getPromotions());
        }
        Set<Promotion> validPromotions = new TreeSet<>();
        for (Promotion promotion : allPromotions) {
            if (promotion != null && promotion.hasBegun() && !promotion.hasEnded()) {
                validPromotions.add(promotion);
            }
        }
        return validPromotions;
    }

    /**
     * 获取可用库存
     *
     * @return 可用库存
     */
    @Transient
    public Integer getAvailableStock() {
        Integer availableStock = null;
        if (getStock() != null && getAllocatedStock() != null) {
            availableStock = getStock() - getAllocatedStock();
            if (availableStock < 0) {
                availableStock = 0;
            }
        }
        return availableStock;
    }

    /**
     * 获取是否缺货
     *
     * @return 是否缺货
     */
    @Transient
    public Boolean getIsOutOfStock() {
        return getStock() != null && getAllocatedStock() != null && getAllocatedStock() >= getStock();
    }

    /**
     * 判断促销是否有效
     *
     * @param promotion 促销
     * @return 促销是否有效
     */
    @Transient
    public boolean isValid(Promotion promotion) {
        if (promotion == null || !promotion.hasBegun() || promotion.hasEnded()) {
            return false;
        }
        if (getValidPromotions().contains(promotion)) {
            return true;
        }
        return false;
    }

    /**
     * 删除前处理
     */
    @PreRemove
    public void preRemove() {
        Set<Promotion> promotions = getPromotions();
        if (promotions != null) {
            for (Promotion promotion : promotions) {
                promotion.getProducts().remove(this);
            }
        }
        Set<OrderItem> orderItems = getOrderItems();
        if (orderItems != null) {
            for (OrderItem orderItem : orderItems) {
                orderItem.setProduct(null);
            }
        }
    }

    /**
     * 持久化前处理
     */
    @PrePersist
    public void prePersist() {
        if (getStock() == null) {
            setAllocatedStock(0);
        }
        setScore(0F);
    }

    /**
     * 更新前处理
     */
    @PreUpdate
    public void preUpdate() {
        if (getStock() == null) {
            setAllocatedStock(0);
        }
        if (getTotalScore() != null && getScoreCount() != null && getScoreCount() != 0) {
            setScore((float) getTotalScore() / getScoreCount());
        } else {
            setScore(0F);
        }
    }

}