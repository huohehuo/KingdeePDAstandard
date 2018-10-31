package com.fangzuo.assist.Dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by NB on 2017/8/3.
 */
@Entity
public class T_main {
    public long orderId;
    @Id
    public String FIndex;
    public String orderDate;
    public String saleRangeId;
    public String saleRange;
    public String saleWayId;
    public String saleWay;//销售方式 采购方式
    public String sourceOrderTypeId;
    public String sourceOrderType;
    public String FDeliveryType;
    public String FDeliveryTypeId; /// 交货方式
    public String FPaymentTypeId;
    public String FPaymentType; //付款方式
    public String FPaymentDate;
    public String FDeliveryAddress;//配送地址
    public String FDepartmentId;
    public String FDepartment;
    public String FSalesManId;
    public String FSalesMan;//业务员
    public String FDirectorId;
    public String FDirector;//主管
    public String FPurchaseUnitId;//采购单位
    public String FPurchaseUnit;
    public String FRemark;      //摘要（表头
    public String FIdentity;
    public String FBusinessId; /// 业务类型
    public String FMaker; /// 制单人
    public String FMakerId;
    public String FBusiness;
    public String FSendOutId;
    public String FSendOut;/// 发货
    public String FCustodyId;
    public String FCustody;/// 保管
    public String FRedBlue;
    public String FAcountID;/// 往来科目ID
    public String FAcount;
    public String Rem;
    public String supplier;
    public String supplierId;
    public String IMIE;   //用于记录多订单下推时的InterId，获取ordercode
    public int activity;
    public String getFAcount() {
        return this.FAcount;
    }
    public void setFAcount(String FAcount) {
        this.FAcount = FAcount;
    }
    public String getFAcountID() {
        return this.FAcountID;
    }
    public void setFAcountID(String FAcountID) {
        this.FAcountID = FAcountID;
    }
    public String getFRedBlue() {
        return this.FRedBlue;
    }
    public void setFRedBlue(String FRedBlue) {
        this.FRedBlue = FRedBlue;
    }
    public String getFCustody() {
        return this.FCustody;
    }
    public void setFCustody(String FCustody) {
        this.FCustody = FCustody;
    }
    public String getFCustodyId() {
        return this.FCustodyId;
    }
    public void setFCustodyId(String FCustodyId) {
        this.FCustodyId = FCustodyId;
    }
    public String getFSendOut() {
        return this.FSendOut;
    }
    public void setFSendOut(String FSendOut) {
        this.FSendOut = FSendOut;
    }
    public String getFSendOutId() {
        return this.FSendOutId;
    }
    public void setFSendOutId(String FSendOutId) {
        this.FSendOutId = FSendOutId;
    }
    public String getFBusiness() {
        return this.FBusiness;
    }
    public void setFBusiness(String FBusiness) {
        this.FBusiness = FBusiness;
    }
    public String getFMaker() {
        return this.FMaker;
    }
    public void setFMaker(String FMaker) {
        this.FMaker = FMaker;
    }
    public String getFBusinessId() {
        return this.FBusinessId;
    }
    public void setFBusinessId(String FBusinessId) {
        this.FBusinessId = FBusinessId;
    }
    public String getFIdentity() {
        return this.FIdentity;
    }
    public void setFIdentity(String FIdentity) {
        this.FIdentity = FIdentity;
    }
    public String getFRemark() {
        return this.FRemark;
    }
    public void setFRemark(String FRemark) {
        this.FRemark = FRemark;
    }
    public String getFPurchaseUnit() {
        return this.FPurchaseUnit;
    }
    public void setFPurchaseUnit(String FPurchaseUnit) {
        this.FPurchaseUnit = FPurchaseUnit;
    }
    public String getFPurchaseUnitId() {
        return this.FPurchaseUnitId;
    }
    public void setFPurchaseUnitId(String FPurchaseUnitId) {
        this.FPurchaseUnitId = FPurchaseUnitId;
    }
    public String getFDirector() {
        return this.FDirector;
    }
    public void setFDirector(String FDirector) {
        this.FDirector = FDirector;
    }
    public String getFDirectorId() {
        return this.FDirectorId;
    }
    public void setFDirectorId(String FDirectorId) {
        this.FDirectorId = FDirectorId;
    }
    public String getFSalesMan() {
        return this.FSalesMan;
    }
    public void setFSalesMan(String FSalesMan) {
        this.FSalesMan = FSalesMan;
    }
    public String getFSalesManId() {
        return this.FSalesManId;
    }
    public void setFSalesManId(String FSalesManId) {
        this.FSalesManId = FSalesManId;
    }
    public String getFDepartment() {
        return this.FDepartment;
    }
    public void setFDepartment(String FDepartment) {
        this.FDepartment = FDepartment;
    }
    public String getFDepartmentId() {
        return this.FDepartmentId;
    }
    public void setFDepartmentId(String FDepartmentId) {
        this.FDepartmentId = FDepartmentId;
    }
    public String getFDeliveryAddress() {
        return this.FDeliveryAddress;
    }
    public void setFDeliveryAddress(String FDeliveryAddress) {
        this.FDeliveryAddress = FDeliveryAddress;
    }
    public String getFPaymentDate() {
        return this.FPaymentDate;
    }
    public void setFPaymentDate(String FPaymentDate) {
        this.FPaymentDate = FPaymentDate;
    }
    public String getFPaymentType() {
        return this.FPaymentType;
    }
    public void setFPaymentType(String FPaymentType) {
        this.FPaymentType = FPaymentType;
    }
    public String getFPaymentTypeId() {
        return this.FPaymentTypeId;
    }
    public void setFPaymentTypeId(String FPaymentTypeId) {
        this.FPaymentTypeId = FPaymentTypeId;
    }
    public String getFDeliveryTypeId() {
        return this.FDeliveryTypeId;
    }
    public void setFDeliveryTypeId(String FDeliveryTypeId) {
        this.FDeliveryTypeId = FDeliveryTypeId;
    }
    public String getFDeliveryType() {
        return this.FDeliveryType;
    }
    public void setFDeliveryType(String FDeliveryType) {
        this.FDeliveryType = FDeliveryType;
    }
    public String getSourceOrderType() {
        return this.sourceOrderType;
    }
    public void setSourceOrderType(String sourceOrderType) {
        this.sourceOrderType = sourceOrderType;
    }
    public String getSourceOrderTypeId() {
        return this.sourceOrderTypeId;
    }
    public void setSourceOrderTypeId(String sourceOrderTypeId) {
        this.sourceOrderTypeId = sourceOrderTypeId;
    }
    public String getSaleWay() {
        return this.saleWay;
    }
    public void setSaleWay(String saleWay) {
        this.saleWay = saleWay;
    }
    public String getSaleWayId() {
        return this.saleWayId;
    }
    public void setSaleWayId(String saleWayId) {
        this.saleWayId = saleWayId;
    }
    public String getSaleRange() {
        return this.saleRange;
    }
    public void setSaleRange(String saleRange) {
        this.saleRange = saleRange;
    }
    public String getSaleRangeId() {
        return this.saleRangeId;
    }
    public void setSaleRangeId(String saleRangeId) {
        this.saleRangeId = saleRangeId;
    }
    public String getOrderDate() {
        return this.orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
    public long getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public int getActivity() {
        return this.activity;
    }
    public void setActivity(int activity) {
        this.activity = activity;
    }
    public String getSupplierId() {
        return this.supplierId;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    public String getSupplier() {
        return this.supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getRem() {
        return this.Rem;
    }
    public void setRem(String Rem) {
        this.Rem = Rem;
    }
    public String getFMakerId() {
        return this.FMakerId;
    }
    public void setFMakerId(String FMakerId) {
        this.FMakerId = FMakerId;
    }
    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
    public String getFIndex() {
        return this.FIndex;
    }
    public void setFIndex(String FIndex) {
        this.FIndex = FIndex;
    }
    @Generated(hash = 1327486114)
    public T_main(long orderId, String FIndex, String orderDate, String saleRangeId,
            String saleRange, String saleWayId, String saleWay,
            String sourceOrderTypeId, String sourceOrderType, String FDeliveryType,
            String FDeliveryTypeId, String FPaymentTypeId, String FPaymentType,
            String FPaymentDate, String FDeliveryAddress, String FDepartmentId,
            String FDepartment, String FSalesManId, String FSalesMan,
            String FDirectorId, String FDirector, String FPurchaseUnitId,
            String FPurchaseUnit, String FRemark, String FIdentity,
            String FBusinessId, String FMaker, String FMakerId, String FBusiness,
            String FSendOutId, String FSendOut, String FCustodyId, String FCustody,
            String FRedBlue, String FAcountID, String FAcount, String Rem,
            String supplier, String supplierId, String IMIE, int activity) {
        this.orderId = orderId;
        this.FIndex = FIndex;
        this.orderDate = orderDate;
        this.saleRangeId = saleRangeId;
        this.saleRange = saleRange;
        this.saleWayId = saleWayId;
        this.saleWay = saleWay;
        this.sourceOrderTypeId = sourceOrderTypeId;
        this.sourceOrderType = sourceOrderType;
        this.FDeliveryType = FDeliveryType;
        this.FDeliveryTypeId = FDeliveryTypeId;
        this.FPaymentTypeId = FPaymentTypeId;
        this.FPaymentType = FPaymentType;
        this.FPaymentDate = FPaymentDate;
        this.FDeliveryAddress = FDeliveryAddress;
        this.FDepartmentId = FDepartmentId;
        this.FDepartment = FDepartment;
        this.FSalesManId = FSalesManId;
        this.FSalesMan = FSalesMan;
        this.FDirectorId = FDirectorId;
        this.FDirector = FDirector;
        this.FPurchaseUnitId = FPurchaseUnitId;
        this.FPurchaseUnit = FPurchaseUnit;
        this.FRemark = FRemark;
        this.FIdentity = FIdentity;
        this.FBusinessId = FBusinessId;
        this.FMaker = FMaker;
        this.FMakerId = FMakerId;
        this.FBusiness = FBusiness;
        this.FSendOutId = FSendOutId;
        this.FSendOut = FSendOut;
        this.FCustodyId = FCustodyId;
        this.FCustody = FCustody;
        this.FRedBlue = FRedBlue;
        this.FAcountID = FAcountID;
        this.FAcount = FAcount;
        this.Rem = Rem;
        this.supplier = supplier;
        this.supplierId = supplierId;
        this.IMIE = IMIE;
        this.activity = activity;
    }
    @Generated(hash = 884209494)
    public T_main() {
    }

    @Override
    public String toString() {
        return "T_main{" +
                "orderId=" + orderId +
                ", FIndex='" + FIndex + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", saleRangeId='" + saleRangeId + '\'' +
                ", saleRange='" + saleRange + '\'' +
                ", saleWayId='" + saleWayId + '\'' +
                ", saleWay='" + saleWay + '\'' +
                ", sourceOrderTypeId='" + sourceOrderTypeId + '\'' +
                ", sourceOrderType='" + sourceOrderType + '\'' +
                ", FDeliveryType='" + FDeliveryType + '\'' +
                ", FDeliveryTypeId='" + FDeliveryTypeId + '\'' +
                ", FPaymentTypeId='" + FPaymentTypeId + '\'' +
                ", FPaymentType='" + FPaymentType + '\'' +
                ", FPaymentDate='" + FPaymentDate + '\'' +
                ", FDeliveryAddress='" + FDeliveryAddress + '\'' +
                ", FDepartmentId='" + FDepartmentId + '\'' +
                ", FDepartment='" + FDepartment + '\'' +
                ", FSalesManId='" + FSalesManId + '\'' +
                ", FSalesMan='" + FSalesMan + '\'' +
                ", FDirectorId='" + FDirectorId + '\'' +
                ", FDirector='" + FDirector + '\'' +
                ", FPurchaseUnitId='" + FPurchaseUnitId + '\'' +
                ", FPurchaseUnit='" + FPurchaseUnit + '\'' +
                ", FRemark='" + FRemark + '\'' +
                ", FIdentity='" + FIdentity + '\'' +
                ", FBusinessId='" + FBusinessId + '\'' +
                ", FMaker='" + FMaker + '\'' +
                ", FMakerId='" + FMakerId + '\'' +
                ", FBusiness='" + FBusiness + '\'' +
                ", FSendOutId='" + FSendOutId + '\'' +
                ", FSendOut='" + FSendOut + '\'' +
                ", FCustodyId='" + FCustodyId + '\'' +
                ", FCustody='" + FCustody + '\'' +
                ", FRedBlue='" + FRedBlue + '\'' +
                ", FAcountID='" + FAcountID + '\'' +
                ", FAcount='" + FAcount + '\'' +
                ", Rem='" + Rem + '\'' +
                ", supplier='" + supplier + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", activity=" + activity +
                ", IMIE=" + IMIE +
                '}';
    }
    public String getIMIE() {
        return this.IMIE;
    }
    public void setIMIE(String IMIE) {
        this.IMIE = IMIE;
    }
}
