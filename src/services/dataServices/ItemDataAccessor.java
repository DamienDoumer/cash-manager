package services.dataServices;

import models.Item;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ItemDataAccessor implements IDataAccessor<Item>{

    @Override
    public void create(Item obj) {
        SQLiteConnection.getInstance().Connect();
        Connection connection = SQLiteConnection.getInstance().getConnection();
        try {
            String sql = "INSERT INTO `Items`(`Name`,`Price`,`DateCreated`) VALUES ('"+
                    obj.getName() + "'," + obj.getPrice() +",'" + obj.getDateCreated().toString() +"');";
            System.out.println(sql);
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            statement.close();
            connection.commit();
        } catch (SQLException e) {
            System.out.println("Error occured while creating an item");
            e.printStackTrace();
        } finally {
            SQLiteConnection.getInstance().DisConnect();
        }
    }

    @Override
    public List<Item> readAll() {
        return null;
    }

    @Override
    public Item read(int id) {
        Item item = null;
        try{
            SQLiteConnection.getInstance().Connect();
            Connection connection = SQLiteConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet set = statement.executeQuery("SELECT * From Items WHERE ID = "+id+";");
            Item bufferItem = new Item.Builder().Build();
            while (set.next()){
                bufferItem.setId(set.getInt("ID"));
                bufferItem.setName(set.getString("Name"));
                bufferItem.setPrice(set.getFloat("Price"));
                String s = set.getString("DateCreated");
                bufferItem.setDateCreated(new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                        Locale.ENGLISH).parse(s));
            }
            item = bufferItem;
            set.close();
            statement.close();
        } catch (SQLException e){
            System.out.println("Error occured while getting this item");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Error occured while parsing date in this item");
            e.printStackTrace();
        } finally {
            SQLiteConnection.getInstance().DisConnect();
        }
        return item;
    }

    @Override
    public boolean delete(Item obj) {
        return false;
    }

    @Override
    public boolean update(Item obj) {
        return false;
    }
}