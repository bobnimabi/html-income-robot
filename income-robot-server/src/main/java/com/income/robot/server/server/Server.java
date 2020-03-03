package com.income.robot.server.server;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.bbin.common.dto.income.TenantIncomeShopOrderDTO;
import com.bbin.common.dto.income.TenantIncomeShopOrderPaySuccessDTO;
import com.bbin.common.response.ResponseResult;
import com.bbin.utils.project.MyBeanUtil;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import com.income.robot.code.entity.PddDict;
import com.income.robot.code.entity.PddTrack;
import com.income.robot.code.entity.PddTrackOrder;
import com.income.robot.code.service.IPddDictService;
import com.income.robot.code.service.IPddTrackOrderService;
import com.income.robot.code.service.IPddTrackService;
import com.income.robot.service.mq.MqSenter;
import com.income.robot.service.dto.GoodsWaitDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
public class Server {
  @Autowired
  private MqSenter mqSenter;

  @Autowired
  private StringRedisTemplate redisTemplate;
  
  @Value("${fileSystem.filePath}")
  private String filePath;
  
  @Autowired
  private IPddDictService dictService;
  
  @Autowired
  private IPddTrackService trackService;
  
  @Autowired
  private IPddTrackOrderService orderService;

  @Autowired
  private KongBaoServer kongBaoServer;
  
  private static final String filePathPrefix = "/files";
  
  private static final String USE_NUM = "use_num";
  
  public ResponseResult goodsOrder(TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
    Boolean aBoolean = this.redisTemplate.opsForValue().setIfAbsent("TRANSFER:ORDER_ID_ORDER:" + shopOrderDTO.getShopId() + "_" + shopOrderDTO.getOrderId(), "", Duration.ofMinutes(4L));
    if (!aBoolean.booleanValue()) {
        log.info("{}", shopOrderDTO.getShopId() + "_" + shopOrderDTO.getOrderId());
        return ResponseResult.SUCCESS();
    }
    String relativePath = getFilePath(shopOrderDTO);
    shopOrderDTO.setPayImageUrl("/files" + relativePath);
    this.mqSenter.sendMessage("income.orderExchange", "incomeOrderRouteKey", shopOrderDTO);
    return ResponseResult.SUCCESS();
  }
  
  private String getFilePath(TenantIncomeShopOrderDTO shopOrderDTO) {
      return "test";
  }
  
  public ResponseResult goodsWait(GoodsWaitDTO goodsWaitDTO) throws Exception {
    String trackNo = getTrackNo(goodsWaitDTO);
    if (!StringUtils.isEmpty(trackNo)) {
        log.info("{},orderId:{}", goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId());
        return ResponseResult.SUCCESS(trackNo);
    }
    StringBuilder error = new StringBuilder();
    String trackingNo = getTrackingNo(goodsWaitDTO, error);
    if (StringUtils.isEmpty(trackingNo)) {
        log.info("{},orderId:{}", goodsWaitDTO.getShopId(), goodsWaitDTO.getOrderId());
        return ResponseResult.FAIL(error.toString());
    }
    TenantIncomeShopOrderPaySuccessDTO paySuccessDTO = (TenantIncomeShopOrderPaySuccessDTO)MyBeanUtil.copyProperties(goodsWaitDTO, TenantIncomeShopOrderPaySuccessDTO.class);
    paySuccessDTO.setOrderType(Integer.valueOf(1));
    this.mqSenter.sendMessage("income.orderPaySuccessExchange", "incomeOrderPaySuccessRouteKey", paySuccessDTO);
    return ResponseResult.SUCCESS(trackingNo);
  }
  
  private String getTrackingNo(GoodsWaitDTO goodsWaitDTO, StringBuilder error) throws InterruptedException {
    String trackingNo = null;
    if (StringUtils.isNotEmpty(goodsWaitDTO.getLoginName())) {
      trackingNo = getTrackNoByMysql(goodsWaitDTO);
      if (StringUtils.isEmpty(trackingNo)) {
        trackingNo = kongBaoServer.getTrackNoByKongBao(goodsWaitDTO, error);
        if (StringUtils.isEmpty(trackingNo))
          return null; 
        saveTrack(goodsWaitDTO, trackingNo);
      }
    } else {
      trackingNo = kongBaoServer.getTrackNoByKongBao(goodsWaitDTO, error);
      if (StringUtils.isEmpty(trackingNo))
        return null;
    } 
    saveTrackOrder(trackingNo, goodsWaitDTO.getOrderId(), goodsWaitDTO.getShopId());
    return trackingNo;
  }
  
  private String getTrackNo(GoodsWaitDTO goodsWaitDTO) {
    PddTrackOrder trackOrder = this.orderService.getOne(new LambdaQueryWrapper<PddTrackOrder>()
        .eq(PddTrackOrder::getOrderNo, goodsWaitDTO.getOrderId())
        .eq(PddTrackOrder::getShopId, goodsWaitDTO.getShopId()));
    if (null == trackOrder)
      return null; 
    log.info("{},orderId:{},trackNo:{}", new Object[] { trackOrder.getShopId(), goodsWaitDTO.getOrderId(), trackOrder.getTrackNo() });
    return trackOrder.getTrackNo();
  }
  
  private void saveTrack(GoodsWaitDTO goodsWaitDTO, String trackingNo) {
    String useNum = getDict(USE_NUM);
    PddTrack track = new PddTrack();
    track.setMobileNum(goodsWaitDTO.getLoginName());
    track.setShopId(goodsWaitDTO.getShopId());
    track.setTrackNo(trackingNo);
    track.setUseNum(Integer.valueOf(1));
    int i = Integer.parseInt(useNum);
    track.setTotalNum(Integer.valueOf(i));
    track.setHasNum(Boolean.valueOf((track.getTotalNum().intValue() > track.getUseNum().intValue())));
    boolean isSave = this.trackService.save(track);
    if (isSave) {
      log.info("{}", JSON.toJSONString(track));
    } else {
      log.info("{}", JSON.toJSONString(track));
    } 
  }
  
  private String getDict(String key) {
    PddDict dict = dictService.getOne(new LambdaQueryWrapper<PddDict>()
            .eq(PddDict::getDictKey, key));
    return dict.getDictValue();
  }
  
  private void saveTrackOrder(String trackingNo, String orderId, String shopId) {
    PddTrackOrder trackOrder = new PddTrackOrder();
    trackOrder.setOrderNo(orderId);
    trackOrder.setShopId(shopId);
    trackOrder.setTrackNo(trackingNo);
    boolean isSave = this.orderService.save(trackOrder);
    if (isSave) {
      log.info("{},orderId:{},trackNo:{}", new Object[]{shopId, orderId, trackingNo});
    } else {
      log.info("{},orderId:{},trackNo:{}", new Object[]{shopId, orderId, trackingNo});
    } 
  }
  
  private String getTrackNoByMysql(GoodsWaitDTO goodsWaitDTO) {
    LocalDateTime time_20 = LocalDateTime.now().withHour(20).withMinute(0).withSecond(0);
    LocalDateTime startTime = (LocalDateTime.now().compareTo(time_20) < 0) ? time_20.minusDays(1L) : time_20;
    List<PddTrack> list = trackService.list(
            new LambdaQueryWrapper<PddTrack>()
                    .eq(PddTrack::getMobileNum, goodsWaitDTO.getLoginName())
                    .eq(PddTrack::getShopId, goodsWaitDTO.getShopId())
                    .eq(PddTrack::getHasNum, Boolean.valueOf(true))
                    .ge(PddTrack::getGmtCreateTime, startTime)
                    .orderByDesc(PddTrack::getGmtCreateTime)
    );
    if (CollectionUtils.isEmpty(list) || null == list.get(0))
      return null; 
    PddTrack trackNo = list.get(0);
    updateTrankNo(trackNo);
    return trackNo.getTrackNo();
  }

  /**
   * 更新运单号使用次数
   * 注意：此处不使用mysql乐观锁，因为次数并发概率很小，且偶有超标也无所谓
   * @param paddTrack
   */
  private void updateTrankNo(PddTrack paddTrack) {
    int newUserNum = paddTrack.getUseNum().intValue() + 1;
    LambdaUpdateWrapper<PddTrack> updateWrapper = new LambdaUpdateWrapper<PddTrack>();
    updateWrapper
        .eq(PddTrack::getId, paddTrack.getId())
        .set(PddTrack::getUseNum, Integer.valueOf(newUserNum));
    if (newUserNum >= paddTrack.getTotalNum()) {
        updateWrapper.set(PddTrack::getHasNum, Boolean.valueOf(false));
    }
    boolean isUpdate = this.trackService.update((Wrapper)updateWrapper);
    if (!isUpdate) {
        log.info("更新运单号使用次数->失败,id:{},num:{}", paddTrack.getId(), Integer.valueOf(newUserNum));
    } else {
        log.info("更新运单号使用次数->成功,id:{},num:{}", paddTrack.getId(), Integer.valueOf(newUserNum));
    }
  }

  public ResponseResult goodsComplete(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
    TenantIncomeShopOrderPaySuccessDTO tenantIncomeShopOrderPaySuccessDTO = (TenantIncomeShopOrderPaySuccessDTO)MyBeanUtil.copyProperties(shopOrderDTO, TenantIncomeShopOrderPaySuccessDTO.class);
    tenantIncomeShopOrderPaySuccessDTO.setOrderType(Integer.valueOf(1));
    this.mqSenter.sendMessage("income.OrderDeliverGoodsExchange", "incomeOrderDeliverGoodsRouteKey", tenantIncomeShopOrderPaySuccessDTO);
    return ResponseResult.SUCCESS();
  }
  
  public ResponseResult goodsConfirm(@RequestBody TenantIncomeShopOrderDTO shopOrderDTO) throws Exception {
    TenantIncomeShopOrderPaySuccessDTO tenantIncomeShopOrderPaySuccessDTO = (TenantIncomeShopOrderPaySuccessDTO)MyBeanUtil.copyProperties(shopOrderDTO, TenantIncomeShopOrderPaySuccessDTO.class);
    tenantIncomeShopOrderPaySuccessDTO.setOrderType(Integer.valueOf(1));
    this.mqSenter.sendMessage("income.orderConfirmReceiptExchange", "incomeOrderConfirmReceiptRouteKey", tenantIncomeShopOrderPaySuccessDTO);
    return ResponseResult.SUCCESS();
  }
}
