package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userID);
    // add additional method signatures here
    ShoppingCart addItem(int userID, int productID);
    void clearCart(int userID);
    void updateItem(int userID, int productID, ShoppingCartItem shoppingCartItem);
}
