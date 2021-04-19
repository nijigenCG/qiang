package com.cg.kill.constants;

public enum ProductEnum {
  RX6700XT("100019084146","RX6700XT AMD原价卡"),
  MAOTAI("100012043978","53 飞天茅台"),
  RX6800("100009440073","RX6800 AMD原价卡"),
  RTX3070("100016046826","微星3070 ￥8299"),
  TEST_1660S("100009149314","测试用的抢购商品，傻逼才买"),
  TEST("30844672751","无需抢购的测试产品");

  ProductEnum(String sku, String desc) {
    this.sku = sku;
    this.desc = desc;
  }

  private String sku;
  private String desc;

  public String getSku() {
    return sku;
  }

  public String getDesc() {
    return desc;
  }


}
