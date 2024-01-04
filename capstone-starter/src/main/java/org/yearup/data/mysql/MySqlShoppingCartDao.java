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
    public ShoppingCart getByUserId(int userId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        String query = "SELECT p.* FROM shopping_cart s" +
                "JOIN products p" +
                "ON p.product_id = s.product_id " +
                "WHERE s.user_id = ?";
        try(Connection connection = getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);

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
