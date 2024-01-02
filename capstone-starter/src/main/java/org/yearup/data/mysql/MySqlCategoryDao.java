package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories()
    {
        List<Category> categories = new ArrayList<>();

        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT category_id, name, description FROM categories;");
            ResultSet resultSet = preparedStatement.executeQuery()){
            while (resultSet.next()){
                Category category = new Category(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
                categories.add(category);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public Category getById(int categoryId)
    {
        String query = "SELECT * FROM categories WHERE category_id = ?;";
        try(Connection connection = getConnection()){

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();

                if(resultSet.next()){
                    return mapRow(resultSet);
                }
            }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Category create(Category category)
    {
        String query = "INSERT INTO categories (name, description) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Insert failed, no rows affected!");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    category.setCategoryId(generatedId);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return category;
    }

    @Override
    public void update(int categoryId, Category category)
    {
        String query = "UPDATE categories SET name = ?, description = ? WHERE category_id = ?";
        try(Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)){

            preparedStatement.setString(1, category.getName());
            preparedStatement.setString(2, category.getDescription());
            preparedStatement.setInt(3, categoryId);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Update failed, no rows affected!");
            }

        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId)
    {
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, categoryId);

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Delete failed, no rows affected!");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category()
        {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
