
  /*******************************************************************
  * ShoppingItem.java
  * Generated by Backendless Corp.
  ********************************************************************/
		package com.mbaas.service;

public class ShoppingItem
{
    private java.lang.String objectId;
    private java.lang.String product;
    private float price;
    private int quantity;
    public void setObjectId(java.lang.String objectId)
    {
      this.objectId = objectId;
    }
    public void setProduct(java.lang.String product)
    {
      this.product = product;
    }
    public void setPrice(float price)
    {
      this.price = price;
    }
    public void setQuantity(int quantity)
    {
      this.quantity = quantity;
    }
    public java.lang.String getObjectId( )
    {
      return objectId;
    }
    public java.lang.String getProduct( )
    {
      return product;
    }
    public float getPrice( )
    {
      return price;
    }
    public int getQuantity( )
    {
      return quantity;
    }
}