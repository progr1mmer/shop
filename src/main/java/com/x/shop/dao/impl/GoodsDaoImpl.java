package com.x.shop.dao.impl;

import com.x.shop.dao.GoodsDao;
import com.x.shop.dao.ProductDao;
import com.x.shop.dao.SnDao;
import com.x.shop.entity.Goods;
import com.x.shop.entity.Product;
import com.x.shop.entity.Sn;
import com.x.shop.entity.SpecificationValue;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.*;

/**
 * Dao - 货品
 *
 * @author progr1mmer
 * @date Created on 2020/2/13
 */
@Repository("goodsDaoImpl")
public class GoodsDaoImpl extends BaseDaoImpl<Goods, Long> implements GoodsDao {

    @Resource(name = "productDaoImpl")
    private ProductDao productDao;
    @Resource(name = "snDaoImpl")
    private SnDao snDao;

    /**
     * 设置值并保存
     *
     * @param goods 货品
     */
    @Override
    public void persist(Goods goods) {
        Assert.notNull(goods);

        if (goods.getProducts() != null) {
            for (Product product : goods.getProducts()) {
                setValue(product);
            }
        }
        super.persist(goods);
    }

    /**
     * 设置值并更新
     *
     * @param goods 货品
     * @return 货品
     */
    @Override
    public Goods merge(Goods goods) {
        Assert.notNull(goods);

        if (goods.getProducts() != null) {
            for (Product product : goods.getProducts()) {
                if (product.getId() != null) {
                    if (!product.getIsGift()) {
                        String jpql = "delete from GiftItem giftItem where giftItem.gift = :product";
                        entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
                    }
                    if (!product.getIsMarketable() || product.getIsGift()) {
                        String jpql = "delete from CartItem cartItem where cartItem.product = :product";
                        entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
                    }
                }
                setValue(product);
            }
        }
        return super.merge(goods);
    }

    /**
     * 设置值
     *
     * @param product 商品
     */
    private void setValue(Product product) {
        if (product == null) {
            return;
        }
        if (StringUtils.isEmpty(product.getSn())) {
            String sn;
            do {
                sn = snDao.generate(Sn.Type.product);
            } while (productDao.snExists(sn));
            product.setSn(sn);
        }
        StringBuilder fullName = new StringBuilder(product.getName());
        if (product.getSpecificationValues() != null && !product.getSpecificationValues().isEmpty()) {
            List<SpecificationValue> specificationValues = new ArrayList<>(product.getSpecificationValues());
            specificationValues.sort((a1, a2) -> new CompareToBuilder().append(a1.getSpecification(), a2.getSpecification()).toComparison());
            fullName.append(Product.FULL_NAME_SPECIFICATION_PREFIX);
            int i = 0;
            for (Iterator<SpecificationValue> iterator = specificationValues.iterator(); iterator.hasNext(); i++) {
                if (i != 0) {
                    fullName.append(Product.FULL_NAME_SPECIFICATION_SEPARATOR);
                }
                fullName.append(iterator.next().getName());
            }
            fullName.append(Product.FULL_NAME_SPECIFICATION_SUFFIX);
        }
        product.setFullName(fullName.toString());
    }

}