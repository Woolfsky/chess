package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import model.AuthData;

public class AdminService {
    /*
    AdminService handles one method: clear
     */
    DataAccess dAccess;

    public AdminService(DataAccess dAccess) {
        System.out.printf("Within AdminService constructor...");
        this.dAccess = dAccess;
    }

    public boolean delete() throws DataAccessException {
        return dAccess.deleteData();
    }



}
