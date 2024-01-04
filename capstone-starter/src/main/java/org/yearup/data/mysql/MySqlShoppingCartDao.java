package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {
    /*---------------VARIABLES---------------*/



    /*--------------CONSTRUCTORS-------------*/

    public MySqlShoppingCartDao(DataSource dataSource) {
        super(dataSource);
    }

    /*------------GETTERS/SETTERS------------*/



    /*---------------FUNCTIONS---------------*/


    @Override
    public ShoppingCart getByUserId(int userID) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT p.*, s.quantity FROM shopping_cart s " +
                "JOIN products p " +
                "ON s.product_id = p.product_id " +
                "WHERE s.user_id = ?";
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                shoppingCartItem.setProduct(mapRow(resultSet));
                shoppingCart.add(shoppingCartItem);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return shoppingCart;
    }

    @Override
    public ShoppingCart addItem(int userID, int productID) {
        String query;
        ShoppingCart shoppingCart = this.getByUserId(userID);

        //Check to see if item is already in cart
            //if item already in cart
            if (shoppingCart.contains(productID)){
                ShoppingCartItem item = shoppingCart.get(productID);
                int newQuantity = item.getQuantity()+1;
                item.setQuantity(newQuantity);
                try(Connection connection = getConnection()) {
                    query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, newQuantity);
                    preparedStatement.setInt(2, userID);
                    preparedStatement.setInt(3, productID);
                    int rows = preparedStatement.executeUpdate();
                    if (rows == 0) {
                        throw new SQLException("Update failed, no rows affected!");
                    }
                    shoppingCart.getItems().put(productID, item); //Add item with updated quantity back into cart
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            //If item not already in cart
            else{
                query = "SELECT * FROM products WHERE product_id = ?";
                try(Connection connection = getConnection()) {
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, productID);

                    ResultSet resultSet = preparedStatement.executeQuery();
                    if(resultSet.next()){
                        ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                        shoppingCartItem.setProduct(mapRow(resultSet));
                        shoppingCartItem.setQuantity(1);
                        shoppingCart.add(shoppingCartItem);

                        query = "INSERT INTO shopping_cart (user_id, product_id, quantity) VALUES (?,?,1)";
                        preparedStatement=connection.prepareStatement(query);
                        preparedStatement.setInt(1, userID);
                        preparedStatement.setInt(2, productID);
                        preparedStatement.executeUpdate();
                    }
                }
                catch (SQLException e){
                    throw new RuntimeException(e);
                }
            }
        return shoppingCart;
    }

    @Override
    public void clearCart(int userID) {
        String query = "DELETE FROM shopping_cart WHERE user_id = ?";
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userID);
            preparedStatement.executeUpdate();

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Delete failed, no rows affected!");
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateItem(int userID, int productID, ShoppingCartItem shoppingCartItem) {
        String query = "UPDATE shopping_cart SET quantity = ? WHERE user_id = ? AND product_id = ?";
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, shoppingCartItem.getQuantity());
            preparedStatement.setInt(2, userID);
            preparedStatement.setInt(3, productID);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Update failed, no rows affected!");
            }
        }
        catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    protected static Product mapRow(ResultSet resultSet) throws SQLException
    {
        //Create product item from row
        int productID = resultSet.getInt("product_id");
        String name = resultSet.getString("name");
        BigDecimal price = resultSet.getBigDecimal("price");
        int categoryID = resultSet.getInt("category_id");
        String description = resultSet.getString("description");
        String color = resultSet.getString("color");
        String imageURL = resultSet.getString("image_url");
        int stock = resultSet.getInt("stock");
        boolean featured = resultSet.getInt("featured") != 0; //Sets to false if not featured, true if it is
        return new Product(productID, name, price, categoryID, description, color, stock, featured, imageURL);
    }
}
