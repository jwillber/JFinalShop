package com.cms.entity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.cms.CommonAttribute;
import com.cms.entity.base.BaseOrder;
import com.cms.util.DBUtils;
import com.jfinal.plugin.activerecord.Page;


/**
 * Entity -订单
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class Order extends BaseOrder<Order> {
    
    /**
     * 会员
     */
    private Member member;
    
    /**
     * 订单项
     */
    private List<OrderItem> orderItems;
    
    /**
     * 支付
     */
    private Payment payment;
    
    /**
     * 获取会员
     * 
     * @return  会员
     */
    public Member getMember(){
        if(member == null){
            member = new Member().dao().findById(getMemberId());
        }
        return member;
    }
    
    /**
     * 获取支付
     * 
     * @return  支付
     */
    public Payment getPayment(){
        if(payment == null){
           new Payment().dao().find("select * from kf_payment where orderId=?",getId());
        }
        return payment;
    }
    
    /**
     * 获取支付方式
     * 
     * @return 支付名称
     */
    public String getPaymentMethod(){
        if(getPayment()!=null){
            return (String) CommonAttribute.paymentMethodNames.get(getPayment().getMethod());
        }
        return "";
    }
    
    /**
     * 获取配送名称
     * 
     * @return 配送名称
     */
    public String getShippingMethodName(){
        return (String) CommonAttribute.shippingMethodNames.get(getShippingMethod());
    }
    
    /**
     * 获取状态名称
     * 
     * @return  状态名称
     */
    public String getStatusName(){
        return (String) CommonAttribute.orderStatusNames.get(getStatus());
    }
    
    /**
     * 获取订单总价格
     * 
     * @return 订单总价格
     */
    public BigDecimal getTotalPrice() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (getOrderItems() != null) {
            for (OrderItem orderItem : getOrderItems()) {
                totalPrice = totalPrice.add(orderItem.getSubtotal());
            }
        }
        return totalPrice;
    }
    
    /**
     * 获取订单项
     * 
     * @return 订单项
     */
    public List<OrderItem> getOrderItems(){
        if(orderItems == null){
            orderItems = new OrderItem().dao().find("select * from kf_order_item where orderId=? ", getId());
        }
        return orderItems;
    }
    
    
    /**
     * 查找订单分页
     * 
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @return 订单分页
     */
    public Page<Order> findPage(String sn,Integer pageNumber,Integer pageSize){
        String filterSql = "";
        if(StringUtils.isNotBlank(sn)){
            filterSql+= " and sn like '%"+sn+"%'";
        }
        String orderBySql = DBUtils.getOrderBySql("createDate desc");
        return paginate(pageNumber, pageSize, "select *", "from kf_order where 1=1 "+filterSql+orderBySql);
    }
    
    
    /**
     * 查找订单分页
     * 
     * @param pageNumber
     *            页码
     * @param pageSize
     *            每页记录数
     * @param memberId
     *            会员ID
     * @return 订单分页
     */
    public Page<Order> findPage(Integer pageNumber,Integer pageSize,Long memberId,String status){
        String filterSql = "";
        if(memberId!=null){
            filterSql+=" and memberId="+memberId;
        }
        if(StringUtils.isNotBlank(status)){
            filterSql+=" and status='"+status+"'";
        }
        String orderBySql = DBUtils.getOrderBySql("createDate desc");
        return paginate(pageNumber, pageSize, "select *", "from kf_order where 1=1 "+filterSql+orderBySql);
    }
}
