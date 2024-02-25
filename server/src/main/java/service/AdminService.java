package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;

public class AdminService {
    /*
    AdminService handles one method: clear
     */
    DataAccess dAccess;

    public AdminService(DataAccess dAccess) {
        this.dAccess = dAccess;
    }

    public boolean delete() throws DataAccessException {
        return dAccess.deleteData();
    }



}
