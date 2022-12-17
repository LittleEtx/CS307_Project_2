package com.littleetx.cs307_project_2.database.user;

import com.littleetx.cs307_project_2.database.DatabaseMapping;
import main.interfaces.ItemState;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SeaportOfficer extends User {

    public SeaportOfficer(Connection conn, Integer id) {
        super(conn, id);
    }

    /**
     * Look for all the items that is currently waiting at this officer’s working
     * seaport.
     *
     * @return an array of String containing the item names.
     */
    public String[] getAllItemsAtPort() {
        try{
            PreparedStatement stmt=conn.prepareStatement("select city_id from staff_city where staff_id = ? ");
            stmt.setInt(1,this.id);
            ResultSet rs= stmt.executeQuery();
            if (rs.next()) {
                int staffCity = rs.getInt(1);
                stmt=conn.prepareStatement("select count(*) from item_route a join item_state b on a.item_name=b.item_name " +
                        "where a.city_id= ? and ((a.stage='EXPORT' and b.state= ? ) or (a.stage='IMPORT' and b.state= ? ))");
                stmt.setInt(1,staffCity);
                stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.ExportChecking));
                stmt.setString(3,DatabaseMapping.getStateDatabaseString(ItemState.ImportChecking));
                rs=stmt.executeQuery();
                if (rs.next()){
                    int length=rs.getInt(1);
                    stmt=conn.prepareStatement("select a.item_name from item_route a join item_state b on a.item_name=b.item_name " +
                            "where a.city_id= ? and ((a.stage='EXPORT' and b.state= ? ) or (a.stage='IMPORT' and b.state= ? ))");
                    stmt.setInt(1,staffCity);
                    stmt.setString(2, DatabaseMapping.getStateDatabaseString(ItemState.ExportChecking));
                    stmt.setString(3,DatabaseMapping.getStateDatabaseString(ItemState.ImportChecking));
                    rs=stmt.executeQuery();
                    String []ans=new String[length];
                    for (int i=0;i<length;i++){
                        rs.next();
                        ans[i]=rs.getString(1);
                    }
                    return ans;
                }
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Set whether the item’s checking state is success or not. Returns false
     * if item does not exist or is in illegal state
     */
    public boolean setItemCheckState(String itemName, boolean success) {
        try{
            PreparedStatement stmt=conn.prepareStatement("select state from item_state where item_name= ? ");
            stmt.setString(1,itemName);
            ResultSet rs=stmt.executeQuery();
            if (rs.next()){
                String state=rs.getString(1);
                if (state.equals(DatabaseMapping.getStateDatabaseString(ItemState.ImportChecking))){
                    stmt=conn.prepareStatement("update item_state set state= ? where item_name= ? ");
                    if (success){
                        stmt.setString(1,DatabaseMapping.getStateDatabaseString(ItemState.FromImportTransporting));
                    }
                    else{
                        stmt.setString(1,DatabaseMapping.getStateDatabaseString(ItemState.ImportCheckFailed));
                    }
                    stmt.setString(2,itemName);
                    stmt.execute();
                    return true;
                }
                else if (state.equals(DatabaseMapping.getStateDatabaseString(ItemState.ExportChecking))){
                    stmt=conn.prepareStatement("update item_state set state= ? where item_name= ? ");
                    if (success){
                        stmt.setString(1,DatabaseMapping.getStateDatabaseString(ItemState.PackingToContainer));
                    }
                    else{
                        stmt.setString(1,DatabaseMapping.getStateDatabaseString(ItemState.ExportCheckFailed));
                    }
                    stmt.setString(2,itemName);
                    stmt.execute();
                    return true;
                }
                else {return false;}
            }

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
        return false;
    }
}
