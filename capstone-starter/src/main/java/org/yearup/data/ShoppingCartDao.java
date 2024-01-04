package org.yearup.data;

import org.yearup.models.ShoppingCart;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userID);
    // add additional method signatures here
    ShoppingCart addProduct(int userID, int productID);
    void clearCart(int userID);
}
